package net.vampirestudios.arrp.json.blockstate;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.resources.Identifier;

public final class Connectables {
    private Connectables() {}

    /* ---------- FENCE-LIKE: post + N/E/S/W arms ---------- */
    public static JMultipart[] fenceLike(Identifier postModel, Identifier armModel,
                                         String northProp, String eastProp, String southProp, String westProp,
                                         boolean uvlock) {
        List<JMultipart> parts = new ArrayList<>(5);

        // post (always)
        parts.add(part(JState.when(), JState.model(postModel), 0, uvlock));

        // arms
        if (northProp != null) parts.add(part(JState.when().isTrue(northProp), JState.model(armModel),   0, uvlock));
        if (eastProp  != null) parts.add(part(JState.when().isTrue(eastProp),  JState.model(armModel),  90, uvlock));
        if (southProp != null) parts.add(part(JState.when().isTrue(southProp), JState.model(armModel), 180, uvlock));
        if (westProp  != null) parts.add(part(JState.when().isTrue(westProp),  JState.model(armModel), 270, uvlock));

        return parts.toArray(JMultipart[]::new);
    }

    /* ---------- PANE-LIKE: optional center + sides ---------- */
    public static JMultipart[] paneLike(Identifier centerModelOrNull, Identifier sideModel,
                                        String northProp, String eastProp, String southProp, String westProp,
                                        boolean uvlock) {
        List<JMultipart> parts = new ArrayList<>(5);

        if (centerModelOrNull != null)
            parts.add(part(JState.when(), JState.model(centerModelOrNull), 0, uvlock));

        if (northProp != null) parts.add(part(JState.when().isTrue(northProp), JState.model(sideModel),   0, uvlock));
        if (eastProp  != null) parts.add(part(JState.when().isTrue(eastProp),  JState.model(sideModel),  90, uvlock));
        if (southProp != null) parts.add(part(JState.when().isTrue(southProp), JState.model(sideModel), 180, uvlock));
        if (westProp  != null) parts.add(part(JState.when().isTrue(westProp),  JState.model(sideModel), 270, uvlock));

        return parts.toArray(JMultipart[]::new);
    }

    /* ---------- COUNTER-LIKE: top + edges + optional corners ---------- */
    public static JMultipart[] counterLike(Identifier topModel, Identifier edgeModel,
                                           Identifier innerCornerOrNull, Identifier outerCornerOrNull,
                                           String northProp, String eastProp, String southProp, String westProp,
                                           boolean uvlock) {
        List<JMultipart> parts = new ArrayList<>();

        // top (always)
        parts.add(part(JState.when(), JState.model(topModel), 0, uvlock));

        // edges
        if (northProp != null) parts.add(part(JState.when().isTrue(northProp), JState.model(edgeModel),   0, uvlock));
        if (eastProp  != null) parts.add(part(JState.when().isTrue(eastProp),  JState.model(edgeModel),  90, uvlock));
        if (southProp != null) parts.add(part(JState.when().isTrue(southProp), JState.model(edgeModel), 180, uvlock));
        if (westProp  != null) parts.add(part(JState.when().isTrue(westProp),  JState.model(edgeModel), 270, uvlock));

        // outer corners: both sides true
        if (outerCornerOrNull != null) {
            parts.add(part(JState.when().add(JState.whenStateBuilder().isTrue(northProp).isTrue(eastProp)),  JState.model(outerCornerOrNull),  90, uvlock));
            parts.add(part(JState.when().add(JState.whenStateBuilder().isTrue(southProp).isTrue(eastProp)),  JState.model(outerCornerOrNull), 180, uvlock));
            parts.add(part(JState.when().add(JState.whenStateBuilder().isTrue(southProp).isTrue(westProp)),  JState.model(outerCornerOrNull), 270, uvlock));
            parts.add(part(JState.when().add(JState.whenStateBuilder().isTrue(northProp).isTrue(westProp)),  JState.model(outerCornerOrNull),   0, uvlock));
        }

        // inner corners: both sides false (optional)
        if (innerCornerOrNull != null) {
            parts.add(part(JState.when().add(JState.whenStateBuilder().isFalse(northProp).isFalse(eastProp)), JState.model(innerCornerOrNull),  90, uvlock));
            parts.add(part(JState.when().add(JState.whenStateBuilder().isFalse(southProp).isFalse(eastProp)), JState.model(innerCornerOrNull), 180, uvlock));
            parts.add(part(JState.when().add(JState.whenStateBuilder().isFalse(southProp).isFalse(westProp)), JState.model(innerCornerOrNull), 270, uvlock));
            parts.add(part(JState.when().add(JState.whenStateBuilder().isFalse(northProp).isFalse(westProp)), JState.model(innerCornerOrNull),   0, uvlock));
        }

        return parts.toArray(JMultipart[]::new);
    }

    /* ---------- Convenience ---------- */

    /** Build a complete state from parts. */
    public static JState stateFrom(JMultipart... parts) {
        return JState.state(parts);
    }

    private static JMultipart part(JWhen when, JBlockModel model, int yRot, boolean uvlock) {
        if (yRot != 0) model = model.y(yRot);
        if (uvlock) model = model.uvlock();
        return JState.multipart(model).when(when);
    }
}
