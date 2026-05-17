package net.vampirestudios.arrp.assets.blockstates;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.resources.Identifier;

public final class Connectables {
    private Connectables() {}

    /* ---------- FENCE-LIKE: post + N/E/S/W arms ---------- */
    public static Multipart[] fenceLike(Identifier postModel, Identifier armModel,
                                         String northProp, String eastProp, String southProp, String westProp,
                                         boolean uvlock) {
        List<Multipart> parts = new ArrayList<>(5);

        // post (always)
        parts.add(part(BlockState.when(), BlockState.model(postModel), 0, uvlock));

        // arms
        if (northProp != null) parts.add(part(BlockState.when().isTrue(northProp), BlockState.model(armModel),   0, uvlock));
        if (eastProp  != null) parts.add(part(BlockState.when().isTrue(eastProp),  BlockState.model(armModel),  90, uvlock));
        if (southProp != null) parts.add(part(BlockState.when().isTrue(southProp), BlockState.model(armModel), 180, uvlock));
        if (westProp  != null) parts.add(part(BlockState.when().isTrue(westProp),  BlockState.model(armModel), 270, uvlock));

        return parts.toArray(Multipart[]::new);
    }

    /* ---------- PANE-LIKE: optional center + sides ---------- */
    public static Multipart[] paneLike(Identifier centerModelOrNull, Identifier sideModel,
                                        String northProp, String eastProp, String southProp, String westProp,
                                        boolean uvlock) {
        List<Multipart> parts = new ArrayList<>(5);

        if (centerModelOrNull != null)
            parts.add(part(BlockState.when(), BlockState.model(centerModelOrNull), 0, uvlock));

        if (northProp != null) parts.add(part(BlockState.when().isTrue(northProp), BlockState.model(sideModel),   0, uvlock));
        if (eastProp  != null) parts.add(part(BlockState.when().isTrue(eastProp),  BlockState.model(sideModel),  90, uvlock));
        if (southProp != null) parts.add(part(BlockState.when().isTrue(southProp), BlockState.model(sideModel), 180, uvlock));
        if (westProp  != null) parts.add(part(BlockState.when().isTrue(westProp),  BlockState.model(sideModel), 270, uvlock));

        return parts.toArray(Multipart[]::new);
    }

    /* ---------- COUNTER-LIKE: top + edges + optional corners ---------- */
    public static Multipart[] counterLike(Identifier topModel, Identifier edgeModel,
                                           Identifier innerCornerOrNull, Identifier outerCornerOrNull,
                                           String northProp, String eastProp, String southProp, String westProp,
                                           boolean uvlock) {
        List<Multipart> parts = new ArrayList<>();

        // top (always)
        parts.add(part(BlockState.when(), BlockState.model(topModel), 0, uvlock));

        // edges
        if (northProp != null) parts.add(part(BlockState.when().isTrue(northProp), BlockState.model(edgeModel),   0, uvlock));
        if (eastProp  != null) parts.add(part(BlockState.when().isTrue(eastProp),  BlockState.model(edgeModel),  90, uvlock));
        if (southProp != null) parts.add(part(BlockState.when().isTrue(southProp), BlockState.model(edgeModel), 180, uvlock));
        if (westProp  != null) parts.add(part(BlockState.when().isTrue(westProp),  BlockState.model(edgeModel), 270, uvlock));

        // outer corners: both sides true
        if (outerCornerOrNull != null) {
            parts.add(part(BlockState.when().add(BlockState.whenStateBuilder().isTrue(northProp).isTrue(eastProp)),  BlockState.model(outerCornerOrNull),  90, uvlock));
            parts.add(part(BlockState.when().add(BlockState.whenStateBuilder().isTrue(southProp).isTrue(eastProp)),  BlockState.model(outerCornerOrNull), 180, uvlock));
            parts.add(part(BlockState.when().add(BlockState.whenStateBuilder().isTrue(southProp).isTrue(westProp)),  BlockState.model(outerCornerOrNull), 270, uvlock));
            parts.add(part(BlockState.when().add(BlockState.whenStateBuilder().isTrue(northProp).isTrue(westProp)),  BlockState.model(outerCornerOrNull),   0, uvlock));
        }

        // inner corners: both sides false (optional)
        if (innerCornerOrNull != null) {
            parts.add(part(BlockState.when().add(BlockState.whenStateBuilder().isFalse(northProp).isFalse(eastProp)), BlockState.model(innerCornerOrNull),  90, uvlock));
            parts.add(part(BlockState.when().add(BlockState.whenStateBuilder().isFalse(southProp).isFalse(eastProp)), BlockState.model(innerCornerOrNull), 180, uvlock));
            parts.add(part(BlockState.when().add(BlockState.whenStateBuilder().isFalse(southProp).isFalse(westProp)), BlockState.model(innerCornerOrNull), 270, uvlock));
            parts.add(part(BlockState.when().add(BlockState.whenStateBuilder().isFalse(northProp).isFalse(westProp)), BlockState.model(innerCornerOrNull),   0, uvlock));
        }

        return parts.toArray(Multipart[]::new);
    }

    /* ---------- Convenience ---------- */

    /** Build a complete state from parts. */
    public static BlockState stateFrom(Multipart... parts) {
        return BlockState.state(parts);
    }

    private static Multipart part(When when, BlockModel model, int yRot, boolean uvlock) {
        if (yRot != 0) model = model.y(yRot);
        if (uvlock) model = model.uvlock();
        return BlockState.multipart(model).when(when);
    }
}
