package com.templeosrs.util.collections.utils;

import com.templeosrs.util.collections.CollectionLogCategorySlug;
import com.templeosrs.util.collections.data.CollectionLogCategory;
import java.util.Arrays;
import java.util.LinkedHashSet;
import net.runelite.api.gameval.ItemID;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class CollectionLogCategoryUtils {
    /**
     * Category aliases to be used in the !col command. e.g. "!col kree"
     */
    public static final Map<String, CollectionLogCategorySlug> CATEGORY_ALIASES = Map.<String, CollectionLogCategorySlug>ofEntries(
            // === Bosses ===
            Map.entry("armadyl", CollectionLogCategorySlug.kreearra),
            Map.entry("kree", CollectionLogCategorySlug.kreearra),
            Map.entry("arma", CollectionLogCategorySlug.kreearra),
            Map.entry("archaeologist", CollectionLogCategorySlug.crazy_archaeologist),
            Map.entry("crazy", CollectionLogCategorySlug.crazy_archaeologist),
            Map.entry("barrows", CollectionLogCategorySlug.barrows_chests),
			Map.entry("bryo", CollectionLogCategorySlug.bryophyta),
			Map.entry("bear", CollectionLogCategorySlug.callisto_and_artio),
            Map.entry("artio", CollectionLogCategorySlug.callisto_and_artio),
            Map.entry("callisto", CollectionLogCategorySlug.callisto_and_artio),
            Map.entry("vetion", CollectionLogCategorySlug.vetion_and_calvarion),
            Map.entry("calvarion", CollectionLogCategorySlug.vetion_and_calvarion),
            Map.entry("calv", CollectionLogCategorySlug.vetion_and_calvarion),
            Map.entry("vet", CollectionLogCategorySlug.vetion_and_calvarion),
			Map.entry("cerb", CollectionLogCategorySlug.cerberus),
            Map.entry("corp", CollectionLogCategorySlug.corporeal_beast),
            Map.entry("corporeal", CollectionLogCategorySlug.corporeal_beast),
            Map.entry("deranged", CollectionLogCategorySlug.deranged_archaeologist),
            Map.entry("dagannoth", CollectionLogCategorySlug.dagannoth_kings),
            Map.entry("dk", CollectionLogCategorySlug.dagannoth_kings),
            Map.entry("dks", CollectionLogCategorySlug.dagannoth_kings),
			Map.entry("delve", CollectionLogCategorySlug.doom_of_mokhaiotl),
			Map.entry("doom", CollectionLogCategorySlug.doom_of_mokhaiotl),
			Map.entry("mok", CollectionLogCategorySlug.doom_of_mokhaiotl),
            Map.entry("duke", CollectionLogCategorySlug.duke_sucellus),
            Map.entry("sucellus", CollectionLogCategorySlug.duke_sucellus),
			Map.entry("ele", CollectionLogCategorySlug.chaos_elemental),
            Map.entry("elemental", CollectionLogCategorySlug.chaos_elemental),
            Map.entry("fanatic", CollectionLogCategorySlug.chaos_fanatic),
            Map.entry("graardor", CollectionLogCategorySlug.general_graardor),
            Map.entry("bandos", CollectionLogCategorySlug.general_graardor),
            Map.entry("grotesque", CollectionLogCategorySlug.grotesque_guardians),
            Map.entry("gg", CollectionLogCategorySlug.grotesque_guardians),
            Map.entry("ggs", CollectionLogCategorySlug.grotesque_guardians),
            Map.entry("hydra", CollectionLogCategorySlug.alchemical_hydra),
            Map.entry("alchemical" , CollectionLogCategorySlug.alchemical_hydra),
            Map.entry("kbd", CollectionLogCategorySlug.king_black_dragon),
            Map.entry("kalphite", CollectionLogCategorySlug.kalphite_queen),
            Map.entry("kril", CollectionLogCategorySlug.kril_tsutsaroth),
            Map.entry("zammy", CollectionLogCategorySlug.kril_tsutsaroth),
            Map.entry("zamorak", CollectionLogCategorySlug.kril_tsutsaroth),
            Map.entry("leviathan", CollectionLogCategorySlug.the_leviathan),
            Map.entry("levi", CollectionLogCategorySlug.the_leviathan),
            Map.entry("mole", CollectionLogCategorySlug.giant_mole),
            Map.entry("moons", CollectionLogCategorySlug.moons_of_peril),
            Map.entry("muspah", CollectionLogCategorySlug.phantom_muspah),
            Map.entry("nightmare", CollectionLogCategorySlug.the_nightmare),
            Map.entry("phosani", CollectionLogCategorySlug.the_nightmare),
            Map.entry("phn", CollectionLogCategorySlug.the_nightmare),
            Map.entry("sire", CollectionLogCategorySlug.abyssal_sire),
            Map.entry("spider", CollectionLogCategorySlug.venenatis_and_spindel),
            Map.entry("spindel", CollectionLogCategorySlug.venenatis_and_spindel),
            Map.entry("venenatis", CollectionLogCategorySlug.venenatis_and_spindel),
            Map.entry("thermonuclear", CollectionLogCategorySlug.thermonuclear_smoke_devil),
            Map.entry("thermy", CollectionLogCategorySlug.thermonuclear_smoke_devil),
            Map.entry("titans", CollectionLogCategorySlug.royal_titans),
            Map.entry("royal", CollectionLogCategorySlug.royal_titans),
            Map.entry("rt", CollectionLogCategorySlug.royal_titans),
            Map.entry("tormented", CollectionLogCategorySlug.tormented_demons),
            Map.entry("tds", CollectionLogCategorySlug.tormented_demons),
            Map.entry("whisperer", CollectionLogCategorySlug.the_whisperer),
            Map.entry("whisp", CollectionLogCategorySlug.the_whisperer),
            Map.entry("zilyana", CollectionLogCategorySlug.commander_zilyana),
            Map.entry("sara", CollectionLogCategorySlug.commander_zilyana),
            Map.entry("zily" , CollectionLogCategorySlug.commander_zilyana),
            Map.entry("saradomin" , CollectionLogCategorySlug.commander_zilyana),
            Map.entry("amox", CollectionLogCategorySlug.amoxliatl),
            Map.entry("vork", CollectionLogCategorySlug.vorkath),
            Map.entry("huey", CollectionLogCategorySlug.the_hueycoatl),
            Map.entry("vard", CollectionLogCategorySlug.vardorvis),
            Map.entry("zul", CollectionLogCategorySlug.zulrah),

            // === Clue Scrolls ===
            Map.entry("beginner_clue", CollectionLogCategorySlug.beginner_treasure_trails),
            Map.entry("easy_clue", CollectionLogCategorySlug.easy_treasure_trails),
            Map.entry("elite_clue", CollectionLogCategorySlug.elite_treasure_trails),
            Map.entry("hard_clue", CollectionLogCategorySlug.hard_treasure_trails),
            Map.entry("master_clue", CollectionLogCategorySlug.master_treasure_trails),
            Map.entry("medium_clue", CollectionLogCategorySlug.medium_treasure_trails),
            Map.entry("shared_clue", CollectionLogCategorySlug.shared_treasure_trail_rewards),
            Map.entry("beginner", CollectionLogCategorySlug.beginner_treasure_trails),
            Map.entry("easy", CollectionLogCategorySlug.easy_treasure_trails),
            Map.entry("medium", CollectionLogCategorySlug.medium_treasure_trails),
            Map.entry("hard", CollectionLogCategorySlug.hard_treasure_trails),
            Map.entry("elite", CollectionLogCategorySlug.elite_treasure_trails),
            Map.entry("master", CollectionLogCategorySlug.master_treasure_trails),
            Map.entry("shared", CollectionLogCategorySlug.shared_treasure_trail_rewards),
            Map.entry("cases", CollectionLogCategorySlug.scroll_cases),

            // === Minigames & Activities ===
            Map.entry("aerial", CollectionLogCategorySlug.aerial_fishing),
            Map.entry("champions", CollectionLogCategorySlug.champions_challenge),
            Map.entry("chompy", CollectionLogCategorySlug.chompy_bird_hunting),
            Map.entry("creature", CollectionLogCategorySlug.creature_creation),
            Map.entry("den", CollectionLogCategorySlug.rogues_den),
            Map.entry("druids", CollectionLogCategorySlug.elder_chaos_druids),
			Map.entry("randoms", CollectionLogCategorySlug.random_events),
            Map.entry("events", CollectionLogCategorySlug.random_events),
            Map.entry("experiment", CollectionLogCategorySlug.gloughs_experiments),
            Map.entry("glough", CollectionLogCategorySlug.gloughs_experiments),
            Map.entry("fossil", CollectionLogCategorySlug.fossil_island_notes),
            Map.entry("gotr", CollectionLogCategorySlug.guardians_of_the_rift),
            Map.entry("rift", CollectionLogCategorySlug.guardians_of_the_rift),
			Map.entry("rumours", CollectionLogCategorySlug.hunter_guild),
            Map.entry("hunter", CollectionLogCategorySlug.hunter_guild),
            Map.entry("lms", CollectionLogCategorySlug.last_man_standing),
            Map.entry("mixology", CollectionLogCategorySlug.mastering_mixology),
            Map.entry("mlm", CollectionLogCategorySlug.motherlode_mine),
            Map.entry("monkey", CollectionLogCategorySlug.monkey_backpacks),
            Map.entry("notes", CollectionLogCategorySlug.my_notes),
            Map.entry("pc", CollectionLogCategorySlug.pest_control),
            Map.entry("pest", CollectionLogCategorySlug.pest_control),
            Map.entry("revs", CollectionLogCategorySlug.revenants),
            Map.entry("rev", CollectionLogCategorySlug.revenants),
            Map.entry("rooftop", CollectionLogCategorySlug.rooftop_agility),
            Map.entry("sep", CollectionLogCategorySlug.hallowed_sepulchre),
            Map.entry("sepulchre", CollectionLogCategorySlug.hallowed_sepulchre),
            Map.entry("hs", CollectionLogCategorySlug.hallowed_sepulchre),
            Map.entry("shades", CollectionLogCategorySlug.shades_of_mortton),
            Map.entry("shayzien", CollectionLogCategorySlug.shayzien_armour),
            Map.entry("stars", CollectionLogCategorySlug.shooting_stars),
            Map.entry("trekking", CollectionLogCategorySlug.temple_trekking),
            Map.entry("tithe", CollectionLogCategorySlug.tithe_farm),
            Map.entry("brewing", CollectionLogCategorySlug.trouble_brewing),
            Map.entry("trouble", CollectionLogCategorySlug.trouble_brewing),
            Map.entry("tb", CollectionLogCategorySlug.trouble_brewing),
            Map.entry("volcanic", CollectionLogCategorySlug.volcanic_mine),
            Map.entry("wyrm", CollectionLogCategorySlug.colossal_wyrm_agility),
            Map.entry("ba", CollectionLogCategorySlug.barbarian_assault),
            Map.entry("assault", CollectionLogCategorySlug.barbarian_assault),
            Map.entry("barbarian", CollectionLogCategorySlug.barbarian_assault),
            Map.entry("brimhaven", CollectionLogCategorySlug.brimhaven_agility_arena),
            Map.entry("trawler", CollectionLogCategorySlug.fishing_trawler),
            Map.entry("wars", CollectionLogCategorySlug.castle_wars),
            Map.entry("totems", CollectionLogCategorySlug.vale_totems),
            Map.entry("restaurant", CollectionLogCategorySlug.gnome_restaurant),
            Map.entry("mta", CollectionLogCategorySlug.magic_training_arena),
            Map.entry("foundry", CollectionLogCategorySlug.giants_foundry),
            Map.entry("mh", CollectionLogCategorySlug.mahogany_homes),
            Map.entry("homes", CollectionLogCategorySlug.mahogany_homes),
            Map.entry("mahogany", CollectionLogCategorySlug.mahogany_homes),
			Map.entry("temp", CollectionLogCategorySlug.tempoross),

            // === Miscellaneous ===
            Map.entry("misc", CollectionLogCategorySlug.miscellaneous),
            Map.entry("pets", CollectionLogCategorySlug.all_pets),
            Map.entry("skilling", CollectionLogCategorySlug.skilling_pets),

            // === Raids & Endgame ===
            Map.entry("cg", CollectionLogCategorySlug.the_gauntlet),
            Map.entry("gauntlet", CollectionLogCategorySlug.the_gauntlet),
            Map.entry("colosseum", CollectionLogCategorySlug.fortis_colosseum),
            Map.entry("colo", CollectionLogCategorySlug.fortis_colosseum),
            Map.entry("cox", CollectionLogCategorySlug.chambers_of_xeric),
            Map.entry("chambers", CollectionLogCategorySlug.chambers_of_xeric),
            Map.entry("fightcaves", CollectionLogCategorySlug.the_fight_caves),
            Map.entry("jad", CollectionLogCategorySlug.the_fight_caves),
            Map.entry("inferno", CollectionLogCategorySlug.the_inferno),
            Map.entry("zuk", CollectionLogCategorySlug.the_inferno),
            Map.entry("toa", CollectionLogCategorySlug.tombs_of_amascut),
            Map.entry("tombs", CollectionLogCategorySlug.tombs_of_amascut),
            Map.entry("tob", CollectionLogCategorySlug.theatre_of_blood),
            Map.entry("theatre", CollectionLogCategorySlug.theatre_of_blood)
    );

    /**
     * Builds a list of alises grouped by their category slug,
     * e.g. kreearra=[armadyl, kree, arma]
     * Used to provide a list of aliases when using the help commands
     */
    public static final Map<String, Set<String>> INVERTED_ALIASES = CATEGORY_ALIASES
            .entrySet()
            .stream()
            .collect(
                Collectors.groupingBy(
                    item -> item.getValue().toString(),
                    Collectors.mapping(
                        Map.Entry::getKey,
                        Collectors.toSet()
                    )
                )
            );

    public static final Map<String, CollectionLogCategory> CUSTOM_CATEGORIES = Map.ofEntries(
            Map.entry(
                CollectionLogCategorySlug.gilded.name(),
                new CollectionLogCategory(
                    "Gilded",
					new LinkedHashSet<>(
						Arrays.asList(
							ItemID.RUNE_FULL_HELM_GOLDPLATE,
							ItemID.RUNE_PLATEBODY_GOLDPLATE,
							ItemID.RUNE_PLATELEGS_GOLDPLATE,
							ItemID.RUNE_PLATESKIRT_GOLDPLATE,
							ItemID.RUNE_KITESHIELD_GOLDPLATE,
							ItemID.RUNE_MED_HELM_GOLD,
							ItemID.RUNE_CHAINBODY_GOLD,
							ItemID.RUNE_SQ_SHIELD_GOLD,
							ItemID.RUNE_2H_SWORD_GOLD,
							ItemID.RUNE_SPEAR_GOLD,
							ItemID.BRUT_RUNE_SPEAR_GOLD,
							ItemID.RUNE_SCIMITAR_GOLD,
							ItemID.RUNE_BOOTS_GOLD,
							ItemID.TRAIL_GILDED_DHIDE_COIF,
							ItemID.TRAIL_GILDED_DHIDE_VAMBRACES,
							ItemID.TRAIL_GILDED_DHIDE_TOP,
							ItemID.TRAIL_GILDED_DHIDE_CHAPS,
							ItemID.TRAIL_GILDED_PICKAXE,
							ItemID.TRAIL_GILDED_AXE,
							ItemID.TRAIL_GILDED_SPADE
						)
					)
                )
            ),
            Map.entry(
                CollectionLogCategorySlug.thirdage.name(),
                new CollectionLogCategory(
                    "Third age",
					new LinkedHashSet<>(
						Arrays.asList(
							ItemID._3A_PICKAXE,
							ItemID._3A_AXE,
							ItemID._3A_DRUIDIC_TOP,
							ItemID._3A_DRUIDIC_BOTTOMS,
							ItemID._3A_DRUIDIC_STAFF,
							ItemID._3A_DRUIDIC_CLOAK,
							ItemID.TRAIL_FIGHTER_SWORD,
							ItemID.TRAIL_MAGE_WAND,
							ItemID.TRAIL_THIRD_CAPE,
							ItemID.TRAIL_RANGER_BOW,
							ItemID.TRAIL_RANGER_COIF,
							ItemID.TRAIL_RANGER_TORSO,
							ItemID.TRAIL_RANGER_LEGS,
							ItemID.TRAIL_RANGER_VAMBRACES,
							ItemID.TRAIL_MAGE_HAT,
							ItemID.TRAIL_MAGE_TORSO,
							ItemID.TRAIL_MAGE_LEGS,
							ItemID.TRAIL_MAGE_AMULET,
							ItemID.TRAIL_FIGHTER_HELM,
							ItemID.TRAIL_SILVER_PLATE_CHEST,
							ItemID.TRAIL_SILVER_PLATE_LEGS,
							ItemID.TRAIL_SILVER_PLATE_SKIRT,
							ItemID.TRAIL_FIGHTER_SHIELD
						)
					)
                )
            )
    );
}