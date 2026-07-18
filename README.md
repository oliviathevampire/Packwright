# Packwright

**Build data packs and resource packs at runtime, for Fabric.**

Packwright lets mods generate everything a pack can contain — block/item models, blockstates,
languages, loot tables, recipes, advancements, tags, predicates, item modifiers, enchantments,
dialogs, entity variants, and complete worldgen (biomes, dimensions, features, carvers, material
rules, structures, noise settings) — from plain Java builders. No JSON files, no clutter: in
theory an entire content mod can ship nothing but `.java` files and its mod json.

Packwright is a heavily rewritten fork of [ARRP](https://github.com/Devan-Kerman/RRP)
(Advanced Runtime Resource Packs) by HalfOf2/Devan-Kerman. The runtime-pack core remains,
but the builder API, codecs, registries and worldgen support were written from scratch for
modern Minecraft. This branch targets Minecraft 26.2.

## Adding Packwright

```groovy
// works for both Groovy and Kotlin DSL
dependencies {
    modImplementation("net.vampirestudios:packwright:1.0.1+build.1-26.2")
}
```

## Quick start

```java
RuntimeResourcePack pack = RuntimeResourcePack.create("mymod:pack");
pack.addLootTable(Identifier.of("mymod", "blocks/ruby_ore"), LootTable.block()
        .pool(Pool.of().rolls(1)
                .entry(Entry.item("mymod:ruby"))
                .condition(Condition.survivesExplosion())));
PackwrightCallback.BEFORE_VANILLA.register(resources -> resources.add(pack));
```

Every generated pack can also be dumped to disk as a regular, ready-to-use datapack —
see the `dump()` methods and the examples in `src/test/java/test/` (a complete custom
dimension lives in `EmberWastesWorldgen`).

## Configuration

`config/packwright.properties` (created on first run, every key overridable per run
with `-Dpackwright.<key>=<value>`):

| key | default | description |
|---|---|---|
| `threads` | half your cores | worker threads for resource generation |
| `dump_on_close` | `false` | dump every pack's contents when it closes |
| `dump_directory` | `packwright.debug` | where dumps are written |
| `debug_performance` | `false` | log lock-wait times |
| `pretty_json` | `true` | pretty-print generated JSON |

## FAQ

### Is this compatible with resource packs?
Yes — Packwright injects packs through the vanilla pack system, right above Minecraft's
own priority. Generated packs can override vanilla assets, but not other mods' assets.

### Migrating from ARRP?
Rename imports from `net.vampirestudios.arrp` to `net.vampirestudios.packwright`,
`RRPCallback` → `PackwrightCallback`, and the `rrp:pregen` entrypoint key to
`packwright:pregen`. Your old `rrp.properties`/`arrp.properties` config migrates
automatically. See [CHANGELOG.md](CHANGELOG.md) for the full list.

## License

[MPLv2](LICENSE) — same license as the original ARRP.
