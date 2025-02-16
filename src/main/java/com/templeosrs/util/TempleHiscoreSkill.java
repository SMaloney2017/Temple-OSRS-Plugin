/*
 * Copyright (c) 2017, Adam <Adam@sigterm.info>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.templeosrs.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.runelite.client.hiscore.HiscoreSkillType;
import static net.runelite.client.hiscore.HiscoreSkillType.ACTIVITY;
import static net.runelite.client.hiscore.HiscoreSkillType.BOSS;
import static net.runelite.client.hiscore.HiscoreSkillType.SKILL;

@AllArgsConstructor
@Getter
public enum TempleHiscoreSkill
{
	OVERALL("Overall", SKILL),
	ATTACK("Attack", SKILL),
	DEFENCE("Defence", SKILL),
	STRENGTH("Strength", SKILL),
	HITPOINTS("Hitpoints", SKILL),
	RANGED("Ranged", SKILL),
	PRAYER("Prayer", SKILL),
	MAGIC("Magic", SKILL),
	COOKING("Cooking", SKILL),
	WOODCUTTING("Woodcutting", SKILL),
	FLETCHING("Fletching", SKILL),
	FISHING("Fishing", SKILL),
	FIREMAKING("Firemaking", SKILL),
	CRAFTING("Crafting", SKILL),
	SMITHING("Smithing", SKILL),
	MINING("Mining", SKILL),
	HERBLORE("Herblore", SKILL),
	AGILITY("Agility", SKILL),
	THIEVING("Thieving", SKILL),
	SLAYER("Slayer", SKILL),
	FARMING("Farming", SKILL),
	RUNECRAFT("Runecraft", SKILL),
	HUNTER("Hunter", SKILL),
	CONSTRUCTION("Construction", SKILL),
	EHP("Ehp", SKILL),
	CLUE_ALL("Clue All", ACTIVITY),
	CLUE_BEGINNER("Clue Beginner", ACTIVITY),
	CLUE_EASY("Clue Easy", ACTIVITY),
	CLUE_MEDIUM("Clue Medium", ACTIVITY),
	CLUE_HARD("Clue Hard", ACTIVITY),
	CLUE_ELITE("Clue Elite", ACTIVITY),
	CLUE_MASTER("Clue Master", ACTIVITY),
	LAST_MAN_STANDING("Last Man Standing", ACTIVITY),
	ABYSSAL_SIRE("Abyssal Sire", BOSS),
	ALCHEMICAL_HYDRA("Alchemical Hydra", BOSS),
	BARROWS_CHESTS("Barrows Chests", BOSS),
	BRYOPHYTA("Bryophyta", BOSS),
	CALLISTO("Callisto", BOSS),
	CERBERUS("Cerberus", BOSS),
	CHAMBERS_OF_XERIC("Chambers of Xeric", BOSS),
	CHAMBERS_OF_XERIC_CHALLENGE_MODE("Chambers of Xeric Challenge Mode", BOSS),
	CHAOS_ELEMENTAL("Chaos Elemental", BOSS),
	CHAOS_FANATIC("Chaos Fanatic", BOSS),
	COMMANDER_ZILYANA("Commander Zilyana", BOSS),
	CORPOREAL_BEAST("Corporeal Beast", BOSS),
	CRAZY_ARCHAEOLOGIST("Crazy Archaeologist", BOSS),
	DAGANNOTH_PRIME("Dagannoth Prime", BOSS),
	DAGANNOTH_REX("Dagannoth Rex", BOSS),
	DAGANNOTH_SUPREME("Dagannoth Supreme", BOSS),
	DERANGED_ARCHAEOLOGIST("Deranged Archaeologist", BOSS),
	GENERAL_GRAARDOR("General Graardor", BOSS),
	GIANT_MOLE("Giant Mole", BOSS),
	GROTESQUE_GUARDIANS("Grotesque Guardians", BOSS),
	HESPORI("Hespori", BOSS),
	KALPHITE_QUEEN("Kalphite Queen", BOSS),
	KING_BLACK_DRAGON("King Black Dragon", BOSS),
	KRAKEN("Kraken", BOSS),
	KREEARRA("KreeArra", BOSS),
	KRIL_TSUTSAROTH("Kril Tsutsaroth", BOSS),
	MIMIC("Mimic", BOSS),
	OBOR("Obor", BOSS),
	SARACHNIS("Sarachnis", BOSS),
	SCORPIA("Scorpia", BOSS),
	SKOTIZO("Skotizo", BOSS),
	THE_GAUNTLET("The Gauntlet", BOSS),
	THE_CORRUPTED_GAUNTLET("The Corrupted Gauntlet", BOSS),
	THEATRE_OF_BLOOD("Theatre of Blood", BOSS),
	THERMONUCLEAR_SMOKE_DEVIL("Thermonuclear Smoke Devil", BOSS),
	TZKAL_ZUK("TzKal-Zuk", BOSS),
	TZTOK_JAD("TzTok-Jad", BOSS),
	VENENATIS("Venenatis", BOSS),
	VETION("Vetion", BOSS),
	VORKATH("Vorkath", BOSS),
	WINTERTODT("Wintertodt", BOSS),
	ZALCANO("Zalcano", BOSS),
	ZULRAH("Zulrah", BOSS),
	EHB("Ehb", BOSS),
	IRON_EHB("Iron Ehb", BOSS),
	EHP_EHB("Ehp + Ehb", BOSS),
	IRON_EHP("Iron Ehp", SKILL),
	F2P_EHP("F2P Ehp", SKILL),
	LVL3_EHP("Lvl-3 Ehp", SKILL),
	THE_NIGHTMARE("The Nightmare", BOSS),
	SOUL_WARS_ZEAL("Soul Wars Zeal", ACTIVITY),
	TEMPOROSS("Tempoross", BOSS),
	THEATRE_OF_BLOOD_CHALLENGE_MODE("Theatre of Blood Challenge Mode", BOSS),
	BOUNTY_HUNTER_HUNTER("Bounty Hunter - Hunter", ACTIVITY),
	BOUNTY_HUNTER_ROGUE("Bounty Hunter - Rogue", ACTIVITY),
	PHOSANIS_NIGHTMARE("Phosanis Nightmare", BOSS),
	NEX("Nex", BOSS),
	RIFTS_CLOSED("Rifts closed", BOSS),
	UIM_EHP("UIM Ehp", SKILL),
	PVP_ARENA("PvP Arena", SKILL),
	TOMBS_OF_AMASCUT("Tombs of Amascut", BOSS),
	TOMBS_OF_AMASCUT_EXPERT("Tombs of Amascut Expert", BOSS),
	PHANTOM_MUSPAH("Phantom Muspah", BOSS),
	ARTIO("Artio", BOSS),
	CALVARION("Calvarion", BOSS),
	SPINDEL("Spindel", BOSS),
	FANTASY_EHP("Fantasy Ehp", SKILL),
	DUKE_SUCELLUS("Duke Sucellus", BOSS),
	THE_LEVIATHAN("The Leviathan", BOSS),
	THE_WHISPERER("The Whisperer", BOSS),
	VARDORVIS("Vardorvis", BOSS),
	SCURRIUS("Scurrius", BOSS),
	LUNAR_CHESTS("Lunar Chests", BOSS),
	SOL_HEREDIT("Sol Heredit", BOSS),
	ARAXXOR("Araxxor", BOSS),
	HUEYCOATL("Hueycoatl", BOSS),
	AMOXLIATL("Amoxliatl", BOSS),
	COLLECTIONS("Collections", BOSS),
	THE_ROYAL_TITANS("The Royal Titans", BOSS);


	private final String name;
	private final HiscoreSkillType type;
}
