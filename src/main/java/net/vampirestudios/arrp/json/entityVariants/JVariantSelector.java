package net.vampirestudios.arrp.json.entityVariants;

import com.google.gson.JsonObject;

public final class JVariantSelector {
    public JsonObject condition; // optional
    public Integer priority;     // optional

    public static JVariantSelector of(JsonObject condition, int priority) {
        JVariantSelector s = new JVariantSelector();
        s.condition = condition;
        s.priority = priority;
        return s;
    }

    public static JVariantSelector fallback() {
        JVariantSelector s = new JVariantSelector();
        s.priority = 0;
        return s;
    }

    public JVariantSelector condition(JsonObject condition) { this.condition = condition; return this; }
    public JVariantSelector priority(int priority) { this.priority = priority; return this; }
}
