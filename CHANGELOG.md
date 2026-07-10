# Changelog

## 1.0.0 — ARRP is now Packwright

Recently I decided to rewrite the api for the library as it was kinda a mess 
before, but now I feel like it's way easier to use and also I added way more 
new things!

Targets Minecraft 26.2/26.3 snapshots (data pack format 110).

### Migrating from ARRP

The rename touches basically everything, but it's all mechanical — here's the whole
find-and-replace list:

- Mod id: `advanced_runtime_resource_pack` → `packwright`; maven artifact:
  `net.vampirestudios:arrp` → `net.vampirestudios:packwright`
- Packages: `net.vampirestudios.arrp` → `net.vampirestudios.packwright`
- Classes: `RRPCallback` → `PackwrightCallback`, `SidedRRPCallback` → `SidedPackwrightCallback`,
  `RRPPreGenEntrypoint` → `PackwrightPreGenEntrypoint`, `ARRPException` → `PackwrightException`
- Entrypoint key: `rrp:pregen` → `packwright:pregen` (the old key still runs, with a warning)
- Config: `config/packwright.properties`; old `rrp.properties`/`arrp.properties` values
  migrate automatically; system property prefix is now `packwright.`
- Default dump directory: `rrp.debug` → `packwright.debug`; the `DEFAULT_OUTPUT` constant
  was removed (`dump()` uses the configured directory)
- Coming from a recent ARRP build: the worldgen API was also renamed for 26.3 —
  `ConfiguredFeature` → `Feature`, `ConfiguredCarver` → `Carver` (with matching
  `ResourceTypes.FEATURE`/`CARVER` constants and `addFeature`/`addCarver` methods),
  and material rules/conditions live in `data.worldgen.material`

### Minecraft 26.3 format support

Mojang changed a *lot* of datapack formats in the 26.3 snapshots, and Packwright follows
along with all of it:

- Minecraft renamed configured features to just "features": they live in the
  `worldgen/feature` folder now and the separate `config` object is gone — the whole
  configuration sits right in the root object. Packwright matches that, with proper
  builders for every feature type in `Features.*`
- Carvers got the same treatment in the game: `worldgen/carver` folder, config inlined,
  and `replaceable`/`lava_level`/`debug_settings` were removed entirely. Caves gained new
  knobs like `count`, `thickness` and `weird_thickness_bias`, and canyons keep their
  `y_scale` inside `shape` — all covered by the `Carver` builder
- Surface rules escaped from noise settings! What used to be the `surface_rule` field is
  called `material_rule` now, and 26.3 lets rules and conditions live in their own
  registries (`worldgen/material_rule`, `worldgen/material_condition`) so you can reference
  them by ID instead of inlining everything — Packwright has typed builders for the lot
- Brewing recipes are data-driven now! `Recipe.brewing(...)` covers the new
  `minecraft:brewing` type, and yes, the game lets any item go in any slot
- 26.3 gave number providers their own registry plus some fun new types —
  `number_dispatcher`, `conditional_value` and `weighted_list` — all supported
- Slot sources (what `/item` and `/execute if slots` select) can be datapack files now,
  so there's a `slot_source` registry type too, and the new `very_biased_to_bottom`
  int provider is in as well
- Instruments can consume durability now (`durability_damage`) and a `use_duration` of `0`
  means no cooldown
- Trim materials got simplified by the game: one `palette` texture ID instead of
  `asset_name` + `override_armor_assets`
- Bed rules are proper objects now (`can_set_spawn`, `can_sleep`, `destroy_on_use`,
  `destroy_on_leave`), and the new straw bed has its own `gameplay/straw_bed_rule`
- Timelines are tied to the new World Clock system (`clock`, defaults to
  `minecraft:overworld`) — also fixed the registry folder, it's `timeline` not `timelines`
- Dimension types require `has_ender_dragon_fight` now, so that's in too
- Structure sets can use the new `dimension_origin` placement — perfect for placing one
  arrival structure at 0,0 of your dimension
- Heads up: the game removed `random_patch` and `replace_blobs`, so those builders are
  deprecated — use `projected_random_patchy_square` and `Features.oreInBlock` instead

### New registry support

Packwright can also write a bunch of things it never could before:

- Reusable predicates (`predicate` folder, built with the same `Condition` API as loot
  tables) and item modifiers (`item_modifier`, built as `LootFunction`s) — reference them
  from loot tables or `/item modify`
- Chat types and enchantment providers
- Actual `.mcfunction` files! `addMcFunction(id, commands)` takes a list of command
  strings, one per line — the first non-JSON thing Packwright generates
- Proper modern `pack.mcmeta` with `min_format`/`max_format`: just call
  `addDataPackMcmeta(description)` (or `addResourcePackMcmeta`) and the current format
  numbers are filled in for you

### Fixes

I also squashed a whole pile of serialization bugs — most of them found by actually
loading the generated packs in-game and letting the registry loader yell at me:

- Fields the game requires are now always written, even when they're at their default
  values — this silently broke biome `carvers`/`features`/`attributes`, template pool
  `fallback`/`processors`/`projection`, processor rule `location_predicate`, world preset
  `settings`/`biome`, structure `spawn_overrides` and several noise settings fields
- Structure `start_height` writes a proper vertical anchor object instead of a bare int
- `would_survive` block predicates write `state` as a block state object, not a string
- Tree features write `below_trunk_provider` (the game renamed `dirt_provider`) and
  always include `decorators`
- The `stone_depth` material condition has proper `secondary_depth_range` (int) and
  `surface_type` fields now

### Config rewrite

The config got a proper rewrite too — it now lives at `config/packwright.properties`
with sane keys (`threads`, `dump_on_close`, `dump_directory`, `debug_performance`,
`pretty_json`; no more keys with spaces in them):

- Your old `rrp.properties`/`arrp.properties` values migrate automatically, no action needed
- Two new options: `dump_directory` (where `dump()`/`dump_on_close` write packs) and
  `pretty_json` (turn it off for compact JSON)
- Every key can be overridden for a single run with `-Dpackwright.<key>=<value>` —
  handy in dev
- A typo in one value no longer nukes your whole config file: it logs which key is bad,
  uses the default for just that key and leaves the file alone

### Examples & datapack export

My favorite side effect of all this: Packwright doubles as a datapack authoring tool now.
`dump()`/`dumpDirect(path)` write a complete, ready-to-use datapack — folder structure,
`pack.mcmeta` and all — that you can drop straight into a world's `datapacks` folder.

If you want something to copy from, there are full end-to-end examples in
`src/test/java/test/`: `EmberWastesWorldgen` builds an entire custom dimension (dimension
type, noise settings, material rules, carvers, features, biome, structures, sky timeline),
`SkyIslandsWorldgen` got updated to 26.3, and `DataRegistryExamples` shows off predicates,
item modifiers, chat types, slot sources and functions. Both dimensions load and generate
in-game — that's how most of the fixes above were found.

### Notes

- 26.3 is a snapshot target, so Mojang may still shuffle formats around before release —
  I'll keep tracking them
- Found a format mismatch or a builder that's missing? Tell me at
  https://github.com/oliviathevampire/Packwright/issues

---

Packwright is a heavily rewritten continuation of ARRP by HalfOf2/Devan-Kerman —
thanks for the original foundation.
