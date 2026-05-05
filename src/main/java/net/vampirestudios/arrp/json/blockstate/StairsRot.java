package net.vampirestudios.arrp.json.blockstate;

final class StairsRot {
    private StairsRot() {}

    /** Vanilla y-rotation for models authored facing EAST. */
    static int yForFacing(String facing) {
        return switch (facing) {
            case "east"  -> 0;
            case "south" -> 90;
            case "west"  -> 180;
            case "north" -> 270;
            default      -> 0; // safe fallback
        };
    }

    /** Vanilla x-rotation for top/bottom halves. */
    static int xForHalf(String half) {
        return "top".equals(half) ? 180 : 0;
    }
}
