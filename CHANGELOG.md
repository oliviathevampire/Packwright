# Changelog

## 1.0.0 - ARRP is now Packwright

Recently I decided to rewrite the API for the library as it was kinda a mess
before, but now I feel like it's way easier to use and I added way more things!

Targets Minecraft 26.2.

### Migrating from ARRP

The rename touches basically everything, but it's all mechanical. Here's the
main find-and-replace list:

- Mod id: `advanced_runtime_resource_pack` -> `packwright`; maven artifact:
  `net.vampirestudios:arrp` -> `net.vampirestudios:packwright`
- Packages: `net.vampirestudios.arrp` -> `net.vampirestudios.packwright`
- Classes: `RRPCallback` -> `PackwrightCallback`, `SidedRRPCallback` ->
  `SidedPackwrightCallback`, `RRPPreGenEntrypoint` ->
  `PackwrightPreGenEntrypoint`, `ARRPException` -> `PackwrightException`
- Entrypoint key: `rrp:pregen` -> `packwright:pregen`; the old key still runs,
  with a warning
- Config: `config/packwright.properties`; old `rrp.properties` and
  `arrp.properties` values migrate automatically; system property prefix is now
  `packwright.`
- Default dump directory: `rrp.debug` -> `packwright.debug`; the
  `DEFAULT_OUTPUT` constant was removed because `dump()` now uses the configured
  directory

### New registry and asset support

Packwright can now write a bunch of things ARRP did not cover:

- Reusable predicates (`predicate` folder, built with the same `Condition` API
  as loot tables) and item modifiers (`item_modifier`, built as
  `LootFunction`s)
- Chat types and enchantment providers
- Actual `.mcfunction` files through `addMcFunction(id, commands)`, with one
  command string per line
- Modern `pack.mcmeta` helpers: `addDataPackMcmeta(description)` and
  `addResourcePackMcmeta(description)` fill in the current format numbers
- Item model definitions, equipment models, entity variants, world clocks and
  timelines

### Fixes

I also squashed a whole pile of serialization bugs, most of them found by
loading generated packs in-game and letting the registry loader yell at me:

- Fields the game requires are now always written, even when they're at their
  default values. This fixed biome `carvers`/`features`/`attributes`, template
  pool `fallback`/`processors`/`projection`, processor rule
  `location_predicate`, world preset `settings`/`biome`, structure
  `spawn_overrides` and several noise settings fields
- Structure `start_height` writes a proper vertical anchor object instead of a
  bare int
- `would_survive` block predicates write `state` as a block state object, not a
  string
- Tree features write `below_trunk_provider` and always include `decorators`
- The `stone_depth` material condition has proper `secondary_depth_range` and
  `surface_type` fields now

### Config rewrite

The config got a proper rewrite too. It now lives at
`config/packwright.properties` with sane keys: `threads`, `dump_on_close`,
`dump_directory`, `debug_performance` and `pretty_json`.

- Your old `rrp.properties`/`arrp.properties` values migrate automatically, no
  action needed
- Two new options: `dump_directory`, where `dump()`/`dump_on_close` write packs,
  and `pretty_json`, which can be turned off for compact JSON
- Every key can be overridden for a single run with
  `-Dpackwright.<key>=<value>`
- A typo in one value no longer nukes your whole config file: it logs which key
  is bad, uses the default for just that key and leaves the file alone

### Examples & datapack export

Packwright doubles as a datapack authoring tool now. `dump()` and
`dumpDirect(path)` write a complete, ready-to-use datapack: folder structure,
`pack.mcmeta` and all.

If you want something to copy from, there are full end-to-end examples in
`src/test/java/test/`: `EmberWastesWorldgen` builds an entire custom dimension,
`SkyIslandsWorldgen` shows a lighter sky-islands pack, and
`DataRegistryExamples` shows registry/data helpers such as predicates, item
modifiers, chat types and functions.

### About 26.3

This release intentionally does not target the 26.3 snapshot formats. Some
26.3-only pieces were removed from this branch or marked as unavailable here,
including data-driven brewing recipes, reusable number providers, slot source
registries, material rule/condition registries, `dimension_origin` structure
placement and the newer feature types such as `projected_random_patchy_square`.

26.3 also renames or flattens several worldgen formats, so use a dedicated
26.3 build/branch for those changes instead of this `26.2` artifact.

---

Packwright is a heavily rewritten continuation of ARRP by HalfOf2/Devan-Kerman.
Thanks for the original foundation.
