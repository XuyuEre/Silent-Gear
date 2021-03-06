# Changelog

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [0.6.0] - 2019-05-04
### Added
- Config to use blueprint-less recipes (under items.gear.recipes). These recipes are shaped and cannot be changed, unlike the 1.13 versions. Note that this introduces the possibility of recipe conflicts, which is one of the reasons blueprints exist in the first place.

## [0.5.3] - 2019-04-25
### Fixed
- Armor crafting not working on servers

## [0.5.2] - 2019-03-01
### Fixed
- Magmatic dupe bug [#21]

## [0.5.1] - 2019-02-05
### Added
- New traits: adamant, aquatic, chilled, and stellar [#18]
- Potion effect traits. These allow tools and armor to give potion effects when equipped. Armor can require a full set, or give different levels of an effect based on the number of pieces. These traits have their own JSON format, see the new traits for examples.
### Fixed
- Hammers breaking unbreaking blocks [#20]

## [0.5.0] - 2019-01-14
This version makes some changes to the `display` object of part JSON files. Old files should still work, but I recommend updating them. For an example, please see [this file](https://github.com/SilentChaos512/Silent-Gear/blob/1.12/src/main/resources/assets/silentgear/materials/main_test2.json).
### Added
- User-defined traits. Currently this is limited to stat modifier traits only. Additional types may be available in the future.
- New display property, `highlight`. Adds a few transparent white pixels on top of tool heads. Optional, disabled by default.
### Changed
- Changed part display properties to allow textures and colors to be set for different gear types. Old files are still compatible. The `all` object is the default for everything. Use `armor` to override all armor types. Then use the item name (`sword`, `bow`, etc.) to override specific gear types.

## [0.4.2] - 2018-12-23
### Added
- Config to set flaxseed drop weight, set 0 to disable [#14]
- Config settings for blue flowers (cluster chance/size and dimension blacklist), set chance or size to 0 to disable [#14]
- Config settings for netherwood trees, similar to those for flowers
### Changed
- Shovels no longer make paths while sneaking (more consistent block placing)
- Convert most remaining recipe types to JSON. Should not have any effect, just 1.13 prep.
### Fixed
- Broken shovels no longer create path blocks [#15]

## [0.4.1] - 2018-12-04
### Added
- Blueprint book - This item stores blueprints. Several features are missing, but eventually you will be able to select a blueprint somehow (without removing it from the book) to use for crafting gear.
- New tool/armor textures made mainly for custom materials. These should replace the "iron" textures suffix when possible. There are two types, `generic_hc` and `generic_lc` (high contrast, low contrast). The "HC" has a very shiny appearance, and "LC" looks more dull.
- Missing textures for crimson iron/steel armor
- Tool heads warn about ungraded parts in the tooltip
- New traits (suggested by KelleyEngineering)
    - Ancient - Increased XP dropped from blocks and mobs
    - Eroded - Increase harvest speed and decrease attack damage as gear is damaged
    - Magmatic - Auto-smelting
    - Refractive - Places lights in dark areas. Adds a new phantom light block.
### Changed
- Disabled parts should no longer appear in randomized gear
- Trait level calculations are a little different
### Fixed
- Worn armor from custom materials will now load the correct textures
- Worn armor is actually colored now (let me know if this doesn't work for you, I'm not sure how I even fixed this :/)

## [0.4.0] - 2018-11-27
### Added
- Netherwood trees, which can be found in the Nether (adds logs, planks, leaves, and saplings, no stairs or slabs yet)
- A new type of fruit
- Quick tool crafting. A few tools can now be crafted without blueprints, but only with rough rods (reduces durability and synergy). This is intended more as an emergency or early-game feature.
- Textures for the salvager (KelleyEngineering)
- Trait: Crude - Reduces synergy bonuses, cancels with Synergy Boost (found on rough rod)
- Trait: Holy - Extra damage to undead (added to lapis upgrade for now)
- Message in action bar when gear breaks (does not work with armor yet)
### Changed
- Parts with no crafting item will attempt to get one from the ore dictionary instead. The `item` field is now optional if you set `oredict`. If you care what item is displayed, you may still set both.
- Synergy calculations tweaked (higher max value, lower drop off)
- Synergy boost effect changed to _add_ 4% to synergy per level (instead of a multiplier)
### Fixed
- Part detection will favor specific items more consistently
- Part tooltips should display all modifiers correctly now (such as negative values in some cases)

## [0.3.2] - 2018-11-20
### Added
- Crimson iron - Harvest level 2. Ore can be found in the Nether.
- Crimson steel - Harvest level 4. Crafted from crimson iron.
- Salvager has a recipe... finally
- texture_domain option in material JSONs, allows textures from other mods to be used (fixes a texture issue in Silent's Gems)
- Chipping trait - As the item is damaged, armor loses a little protection, tools gain harvest speed (repairing the item will reverse the effect, of course).
### Changed
- Vanilla's repair recipe (which destroys NBT) is now replaced. Will ignore any gear items and behave as normal otherwise.
- Gear items are now considered "repairable" thanks to the previous item, which should greatly improve mod compatibility
- Gold tool heads and tips have new, shinier textures
- Hammers had a slight design change (a little bulkier). Silent's Gems textures _not updated yet_ (that will be fun...)
- Traits are now displayed a little differently on parts, hold control to see the full list at once
- Item part GUI no longer pauses the game
### Fixed
- JSON overrides crashing the game if sections are omitted [#13]

## [0.3.1] - 2018-11-12
Major crafting station overhaul! I haven't found any item loss/duplication, but keep an eye out.

Please remember the traits system is still WIP. Anything is subject to change or balancing.
### Added
- Crafting station keeps items in the crafting grid when closed
- Crafting station has a proper button for the parts GUI now
- Crafting station has a 'parts grid' next to the crafting grid. This allows tools to be crafted in a single step! Place the tool head recipe in the crafting grid, then fill the parts grid with any required parts or upgrades.
- More traits!
    - Bulky - Item loses attack speed. This is intended for a new upgrade part I have planned, using on main parts is not recommended (just change the stats).
    - Jagged - Item gains attack (melee) damage as the item loses durability.
### Changed
- Crafting station has even better shift-click support now
- Raised max level on most traits to four (some are five now)
- Other balancing/tweaking of traits
### Fixed
- Crafting station losing items when RealBench is installed [#4]
- Disabled parts working in gear crafting
- Malleable and Brittle activating when no durability is lost

## [0.3.0] - 2018-11-02
### Added
- Two configs to control how strict AOE tools are when matching blocks.
- Traits for gear materials (WIP). Traits are added in the material JSONs. Some traits will be opposites and will cancel out the other, so material mixing can be used to your advantage. The math for calculating levels is also a bit weird right now, but works well for main parts at least. This feature needs a lot of work, expect anything to change.
    - Brittle - Gear will sometimes lose an _extra_ point of durability, chance increases with level. Cancels with Malleable.
    - Malleable - Gear will sometimes lose one _less_ point of durability, chance increases with level. Cancels with Brittle.
    - Soft - Tools will lose some harvest speed as they are damaged. Repairing will restore lost speed.
    - speed_boost_light (name TBD) - Tools gain a harvest speed bonus when in light. The brighter the light, the bigger the bonus.
    - Synergistic (synergy_boost) - Adds a bonus multiplier on synergy, if it is greater than 100% (mix those materials!)
- Salvager, breaks down gear (vanilla and SGear) into their components. The more damaged the item, the greater the chance of losing parts (configurable, default 0% - 50%). Set min and max rates to 0 to disable part loss.
- GUI that lists all available parts and can sort them (by name, type, and all stats). Incomplete, but usable. Currently accessed through the crafting station (may change).
- Flax bowstring
- Leather scrap item, no particular use right now
### Changed
- Optimize ore dictionary lookup for materials and tooltip creation (should improve load times slightly, related to [Silent's Gems #341](https://github.com/SilentChaos512/SilentGems/issues/341))
- AOE tools will now break blocks of lower or equal harvest levels, instead of just equal (except in STRICT mode). Non-ores can be broken when targeting ores.
- Part analyzer operates significantly faster now.
### Fixed
- The "anvil only" config for upgrades actually works now

## [0.2.1]
### Fixed
- Server crash (#12)

## [0.2.0]
Multiple internal changes, watch out for bugs!
### Added
- Bindings. There are none defined by default, but can be created with JSON files like all other part types. Use texture suffix "generic", type "binding". Most tool classes do not have a binding texture (layer will be blank).
- Lock stats subcommand (freezes an item's stats, mainly for pack/map makers) (/sgear lock_stats)
- Tool classes can be controlled with Tool Progression to some extent. Harvest levels cannot be set there, you would still need to use Gear's material JSONs. (#11)
### Changed
- Equipment JSONs can now specify any part type as a required ingredient (instead of just mains, rods, and bowstrings)
### Fixed
- Crash with Tool Progression mod (#11)

## [0.1.3]
### Added
- Configs to change repair multiplier for quick and anvil repairs
- Quark runes can now control the effect color of gear
### Changed
- Improved block matching for hammers and excavators. Ores will only match the same block, but most others can be mined together.
- Default enchanted effect color to purple (vanilla)
- Updated all tool textures with consistent shading (base mod only)

## [0.1.2]
### Added
- Configs to control vanilla gear nerfing (to disable, remove all items from the list)
- Blueprint package, an item that gives blueprints when used (pulls from a loot table)
- Rod blueprints/templates, to work around recipe conflicts (#5)
- Loot table function 'silentgear:select_tier' which can be used to generate random gear
### Changed
- Anvil repairs improved (50% of material durability), quick repair reduced to 35% of material durability
- Nerfed gear defaults to 50% max durability (up from 10%)
- Upgrades can now be applied in an anvil
- Root advancement no longer gives blueprints, player spawns with a blueprint package instead (#7)
- Stone and iron rods now accept "rodStone" and "rodIron"

## [0.1.1]
### Added
- Wool and leather grips (craft leather or a block of wool with an existing tool)
### Fixed
- Quick repair recipe matching when it shouldn't, causing some recipes to stop working (#3)
- Some tools being broken by other mods (axes on bonsai pots, for example)

## [0.1.0]
Updated for Silent Lib 3.0.0
### Added
- Main materials now display their tier in the tooltip
- More advancements!
### Changed
- New machete textures
- Blueprint outline colors, remove shift function
- Hammers and excavators are faster (50% penalty instead of 75%)
### Fixed
- Mattocks not actually working as shovels or axes
- Broken tools still appearing broken after repair

## [0.0.11]
Updated for Silent Lib 2.3.18
### Added
- Missing texture for spoon upgrade

## [0.0.10]
### Added
- Blaze/end rods are now tool rods
- Quartz-tipped upgrade
- Reach distance stat ("reach_distance" in the JSON files)
- Spoon upgrade, allows pickaxes to work as shovels
- Red card upgrade, allows items to break permanently (could be useful in machines that use tools...)
- Root advancement gives players some blueprints
- More JEI example recipes (applying tip upgrades, for instance)
- JEI info pages for many items (more to come)
### Changed
- Tool/armor items now give just a few randomized samples, instead of one for each main material (less JEI lag)
- Tooltips for tool heads improved, removed some redundant information
### Fixed
- Crafting station now works with the '+' (move items) button in JEI

Numerous other tweaks and minor fixes

## [0.0.9]
### Added
- Katanas
- Machetes
- Excavators (AOE shovels)
### Fixed
- Crafting station losing inventory when broken (#2)

## [0.0.8]
### Added
- Missing textures and models
### Changed
- Dried sinew is now a smelting recipe
### Fixed
- Some armor items not being colored

## [0.0.7] (preview 7, 1.12.2)
### Added
- New main parts: obsidian, netherrack, and terracotta
- Config to specify items that work with block-placing tools. Default list includes dank/null and a couple other items.
### Changed
- Improvements to how gear part data is passed around, which seems to improve frame rate (FPS)
- Gear can no longer be repaired with lower-tier materials
- Un-nerfed vanilla hoes. You're welcome.
### Fixed
- Materials not considering grade when displaying stats
- Armor durability being silly (added "armor durability" stat to correct this)
- Armor breaking permanently (hopefully)
- A rare(?) crash with hammers
- JEI should now recognize the crafting station as a crafting table

## [0.0.6] (preview 6, 1.12.2)
### Added
- Part analyzer, basically the same as the material grade from Silent's Gems
- Tools and armor that have ungraded parts will be assigned random grades. The grades selected have a lower average and lower maximum than those graded by the analyzer.
### Changed
- The lowest material grades now reduce stats slightly, with grade C providing no bonuses. Higher grade bonuses increased.

## [0.0.5] (preview 5, 1.12.2)
### Added
- Blueprints/templates now display an outline of the item they craft if you are holding shift
- Template textures
- More advancements! And localizations for all of them
### Changed
- Blueprints/templates have their own items now (old ones will disappear, sorry)
- Tip upgrades now have their own items (same story as above)
### Fixed
- Many(?) missing recipes

## [0.0.4] (preview 4, 1.12.2)
### Added
- Quick repair recipe, which replaces decorating
### Changed
- Block placing tools now require sneaking (fixes placing blocks when click on blocks with GUIs and such)
- Crafting materials (upgrade base, rods, sinew) have individual items now, existing ones will disappear
### Fixed
- Dagger localizations
- Command usage text

## [0.0.3] (preview 3, 1.12.2)
### Added
- User-defined materials are now working!
- Subcommand to repair held gear
### Fixed
- Example recipes done right, hopefully
- Various other minor fixes

## [0.0.2] (preview 2, 1.12.2)
### Added
- Missing blueprint recipes for daggers and sickles
- Block placing with tools handler. Entirely configurable, you can set literally any item to work with this now. Defaults to Silent Gear pickaxes, shovels, and axes.
### Fixed
- Example swords having wood guards
- NoClassDefFoundError crash, jline/internal/InputStreamReader (#1)

## [0.0.1] (preview 1, 1.12.2)
- Initial preview build
