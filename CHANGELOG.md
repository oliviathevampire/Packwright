# Changelog

## 1.0.0 - ARRP is now Packwright

Recently I decided to rewrite the API for the library as it was kinda a mess
before, but now I feel like it's way easier to use and I added way more things!

Targets Minecraft 26.3.

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
- Worldgen names were updated for 26.3: `ConfiguredFeature` -> `Feature`,
  `ConfiguredCarver` -> `Carver`, with matching `ResourceTypes.FEATURE` and
  `ResourceTypes.CARVER` constants plus `addFeature`/`addCarver` methods

### Minecraft 26.3 format support

Mojang changed a lot of datapack formats in the 26.3 snapshots, and Packwright
tracks those changes:

- Configured features are now just features. They live in `worldgen/feature`,
  and the old nested `config` object is gone
- Carvers now live in `worldgen/carver`, with config fields inlined at the root.
  `replaceable`, `lava_level` and `debug_settings` were removed; caves gained
  fields like `count`, `thickness` and `weird_thickness_bias`, and canyons keep
  `y_scale` inside `shape`
- Surface rules moved out of noise settings. `surface_rule` became
  `material_rule`, and rules/conditions can now live in
  `worldgen/material_rule` and `worldgen/material_condition`
- Brewing recipes are data-driven now; `Recipe.brewing(...)` covers the new
  `minecraft:brewing` type
- Number providers gained their own registry plus `number_dispatcher`,
  `conditional_value` and `weighted_list`
- Slot sources can be datapack files now, so Packwright has a `slot_source`
  registry type too
- Instruments can consume durability through `durability_damage`, and a
  `use_duration` of `0` means no cooldown
- Trim materials now use one `palette` texture ID instead of `asset_name` plus
  `override_armor_assets`
- Bed rules are proper objects now: `can_set_spawn`, `can_sleep`,
  `destroy_on_use` and `destroy_on_leave`
- Timelines are tied to the World Clock system, and the registry folder is
  `timeline`, not `timelines`
- Dimension types require `has_ender_dragon_fight`
- Structure sets can use the new `dimension_origin` placement type
- The game removed `random_patch` and `replace_blobs`; those builders are
  deprecated, so use `projected_random_patchy_square` and
  `Features.oreInBlock` instead

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
`SkyIslandsWorldgen` is updated to 26.3, and `DataRegistryExamples` shows off
predicates, item modifiers, chat types, slot sources and functions.

### Notes

- 26.3 is a snapshot target, so Mojang may still shuffle formats around before
  release
- Found a format mismatch or a builder that's missing? Tell me at
  https://github.com/oliviathevampire/Packwright/issues

---

Packwright is a heavily rewritten continuation of ARRP by HalfOf2/Devan-Kerman.
Thanks for the original foundation.
