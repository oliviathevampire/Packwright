package net.vampirestudios.packwright.api;

@FunctionalInterface
public interface ResourceMerger<T> {

    T merge(T existing, T incoming);

    static <T> ResourceMerger<T> replace() {
        return (existing, incoming) -> incoming;
    }
}