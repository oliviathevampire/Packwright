# Changelog

## 0.16.0

Targets Minecraft 26.2/26.3 snapshot data pack formats (pack format 110). Both example
dimensions (`SkyIslandsWorldgen`, `EmberWastesWorldgen`) load and generate in-game.

### Minecraft 26.3 format support

- Features: registry moved from `worldgen/configured_feature` to `worldgen/feature`, configuration
  now inlined in the root object; rewritten around per-type builders (`Features.*`)
- Carvers: registry renamed from `worldgen/configured_carver` to `worldgen/carver`, flattened config,
  new cave fields (`count`, `thickness`, `weird_thickness_bias`, ...), canyon `y_scale` moved into `shape`
- Material rules & conditions: new `worldgen/material_rule` and `worldgen/material_condition`
  registries with typed builders; noise settings' `surface_rule` renamed to `material_rule`
  (inline or ID reference)
- Data-driven brewing recipes (`minecraft:brewing`) via `Recipe.brewing(...)`
- Number providers: new `number_provider` registry, plus the `number_dispatcher`,
  `conditional_value` and `weighted_list` provider types
- New `slot_source` registry support
- Int providers: added `minecraft:very_biased_to_bottom`
- Instruments: added `durability_damage`; `use_duration` may be `0`
- Trim materials: `asset_name`/`override_armor_assets` replaced by `palette`
- Environment attributes: object-valued attributes (bed rules with `can_set_spawn`/`can_sleep`/
  `destroy_on_use`/`destroy_on_leave`), new `gameplay/straw_bed_rule`
- Timelines: required `clock` field (defaults to `minecraft:overworld`); registry folder corrected
  to `timeline`
- Dimension types: added required `has_ender_dragon_fight`
- Structure sets: `minecraft:dimension_origin` placement
- Deprecated builders for feature types removed from the game (`random_patch`, `replace_blobs`);
  use `projected_random_patchy_square` and block-targeted ores (`Features.oreInBlock`) instead

### New registry support

- `predicate` (reusable loot conditions) and `item_modifier` (reusable loot functions)
- `chat_type`, `enchantment_provider`
- Plain-text functions via `addMcFunction(id, commands)`
- Modern `pack.mcmeta` (`min_format`/`max_format`) via `addPackMcmeta(description, major, minor)`
  and the `addDataPackMcmeta`/`addResourcePackMcmeta` shortcuts

### Fixes

- Fields the game requires are now always serialized, even at their default values
  (biome `carvers`/`features`/`attributes`, template pool `fallback`/`processors`/`projection`,
  processor rule `location_predicate`, world preset `settings`/`biome`, structure `spawn_overrides`,
  noise settings `spawn_target`/`aquifers_enabled`/`ore_veins_enabled`/`disable_mob_generation`)
- Structure `start_height` encodes as a vertical anchor object instead of a bare int
- `would_survive` block predicates encode `state` as a block state object
- Tree features: `dirt_provider` renamed to `below_trunk_provider`, `decorators` always present
- `stone_depth` material condition: proper `secondary_depth_range` (int) and `surface_type` fields

### Config rewrite

- Config moved to `config/arrp.properties` (the loader's config directory), with clean keys:
  `threads`, `dump_on_close`, `dump_directory`, `debug_performance`, `pretty_json`
- Values from the legacy `config/rrp.properties` are migrated automatically
- New options: `dump_directory` (where `dump()`/`dump_on_close` write packs) and `pretty_json`
  (disable to emit compact JSON)
- Every key can be overridden per run with `-Darrp.<key>=<value>`
- A partially invalid config now logs which key is bad and keeps the file, instead of
  silently overwriting it with defaults

### Project layout

- Material rules/conditions moved to `net.vampirestudios.arrp.data.worldgen.material`
- New examples: `EmberWastesWorldgen` (complete custom dimension), `DataRegistryExamples`
  (predicates, item modifiers, chat types, slot sources, functions); every worldgen example
  dumps a ready-to-use datapack with `pack.mcmeta` under `dumps/`
