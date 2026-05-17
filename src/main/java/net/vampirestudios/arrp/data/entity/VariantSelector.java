package net.vampirestudios.arrp.data.entity;

import com.google.gson.JsonObject;

public final class VariantSelector {
    public JsonObject condition; // optional
    public Integer priority;     // optional

    public static VariantSelector of(JsonObject condition, int priority) {
        VariantSelector s = new VariantSelector();
        s.condition = condition;
        s.priority = priority;
        return s;
    }

    public static VariantSelector fallback() {
        VariantSelector s = new VariantSelector();
        s.priority = 0;
        return s;
    }

    public VariantSelector condition(JsonObject condition) { this.condition = condition; return this; }
    public VariantSelector priority(int priority) { this.priority = priority; return this; }
}
