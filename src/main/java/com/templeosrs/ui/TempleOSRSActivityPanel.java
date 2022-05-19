/*
 * Copyright (c) 2017, Adam <Adam@sigterm.info>
 * Copyright (c) 2018, Psikoi <https://github.com/psikoi>
 * Copyright (c) 2019, Bram91 <https://github.com/bram91>
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

package com.templeosrs.ui;

import com.google.common.collect.ImmutableList;
import com.templeosrs.util.TempleOSRSHiscoreSkill;
import static com.templeosrs.util.TempleOSRSHiscoreSkill.*;
import com.templeosrs.util.TempleOSRSPlayer;
import com.templeosrs.util.playerinfo.TempleOSRSData;
import com.templeosrs.util.playerinfo.TempleOSRSSkill;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.inject.Inject;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import net.runelite.client.hiscore.HiscoreSkillType;
import net.runelite.client.ui.ColorScheme;

public class TempleOSRSActivityPanel extends JPanel
{
	private static final List<TempleOSRSHiscoreSkill> SKILLS = ImmutableList.of(
		ATTACK, DEFENCE, STRENGTH,
		HITPOINTS, RANGED, PRAYER,
		MAGIC, COOKING, WOODCUTTING,
		FLETCHING, FISHING, FIREMAKING,
		CRAFTING, SMITHING, MINING,
		HERBLORE, AGILITY, THIEVING,
		SLAYER, FARMING, RUNECRAFT,
		HUNTER, CONSTRUCTION
	);

	private static final List<TempleOSRSHiscoreSkill> BOSSES = ImmutableList.of(
		ABYSSAL_SIRE, ALCHEMICAL_HYDRA, BARROWS_CHESTS,
		BRYOPHYTA, CALLISTO, CERBERUS,
		CHAMBERS_OF_XERIC, CHAMBERS_OF_XERIC_CHALLENGE_MODE, CHAOS_ELEMENTAL,
		CHAOS_FANATIC, COMMANDER_ZILYANA, CORPOREAL_BEAST,
		DAGANNOTH_PRIME, DAGANNOTH_REX, DAGANNOTH_SUPREME,
		CRAZY_ARCHAEOLOGIST, DERANGED_ARCHAEOLOGIST, GENERAL_GRAARDOR,
		GIANT_MOLE, GROTESQUE_GUARDIANS, HESPORI,
		KALPHITE_QUEEN, KING_BLACK_DRAGON, KRAKEN,
		KREEARRA, KRIL_TSUTSAROTH, MIMIC,
		NEX, THE_NIGHTMARE, PHOSANIS_NIGHTMARE,
		OBOR, SARACHNIS, SCORPIA,
		SKOTIZO, TEMPOROSS, THE_GAUNTLET,
		THE_CORRUPTED_GAUNTLET, THEATRE_OF_BLOOD, THEATRE_OF_BLOOD_CHALLENGE_MODE,
		THERMONUCLEAR_SMOKE_DEVIL, TZKAL_ZUK, TZTOK_JAD,
		VENENATIS, VETION, VORKATH,
		WINTERTODT, ZALCANO, ZULRAH
	);

	private static final Color[] COLORS = {ColorScheme.DARK_GRAY_HOVER_COLOR, ColorScheme.SCROLL_TRACK_COLOR};

	final Map<String, TempleOSRSTableRow> map = new HashMap<>();

	final TempleOSRSSortPanel sortPanel;

	final TempleOSRSTableRow overall;

	ArrayList<TempleOSRSTableRow> rows = new ArrayList<>();

	HiscoreSkillType hiscoreSkillType;

	long total;

	@Inject
	TempleOSRSActivityPanel(HiscoreSkillType type)
	{
		hiscoreSkillType = type;

		setLayout(new GridLayout(0, 1));
		setBorder(new EmptyBorder(5, 5, 5, 5));
		setBackground(ColorScheme.DARK_GRAY_HOVER_COLOR);

		sortPanel = new TempleOSRSSortPanel(this, hiscoreSkillType);

		overall = new TempleOSRSTableRow("overall", "Overall", COLORS[1], HiscoreSkillType.OVERALL);
		map.put("overall", overall);

		initialize();
	}

	private void initialize()
	{
		rows.clear();

		add(sortPanel);
		add(overall);

		List<TempleOSRSHiscoreSkill> list = (hiscoreSkillType.equals(HiscoreSkillType.SKILL) ? SKILLS : BOSSES);

		for (int i = 0; i < list.size(); i++)
		{
			TempleOSRSHiscoreSkill skill = list.get(i);
			String formattedKey = skill.getName().replaceAll("[^A-Za-z0-9]", "").toLowerCase();

			TempleOSRSTableRow row = new TempleOSRSTableRow(formattedKey, skill.getName(), COLORS[i % 2], hiscoreSkillType);
			map.put(formattedKey, row);
			rows.add(row);
			add(row);
		}
	}

	void update(TempleOSRSPlayer result)
	{
		TempleOSRSData playerData = hiscoreSkillType.equals(HiscoreSkillType.SKILL) ? result.playerSkillsData.data : result.playerBossingData.data;

		for (TempleOSRSHiscoreSkill skill : TempleOSRSHiscoreSkill.values())
		{
			String formattedKey = skill.getName().replaceAll("[^A-Za-z0-9]", "").toLowerCase();

			if (map.containsKey(formattedKey))
			{
				TempleOSRSTableRow row = map.get(formattedKey);
				TempleOSRSSkill skillData = playerData.table.get(skill.getName());

				long total = Objects.nonNull(skillData.xp) ? skillData.xp.longValue() : 0;
				long rank = Objects.nonNull(skillData.rank) ? skillData.rank.longValue() : 0;
				double ehp = hiscoreSkillType.equals(HiscoreSkillType.SKILL) ? (Objects.nonNull(skillData.ehp) ? skillData.ehp : 0) : (Objects.nonNull(skillData.ehb) ? skillData.ehb : 0);

				this.total += total;

				row.update(total, rank, ehp);
			}
		}
	}

	void update(long rank, double ehp)
	{
		overall.update(total, rank, ehp);
	}

	void reset()
	{
		total = 0;
		overall.reset();
		sortPanel.reset();
		removeAll();
		initialize();
	}

	private void rebuild()
	{
		int i = 0;
		for (TempleOSRSTableRow row : rows)
		{
			String skill = row.name;
			TempleOSRSTableRow entry = map.get(skill);
			entry.setBackground(COLORS[i++ % 2]);
			add(entry);
		}
		revalidate();
	}

	void sort(Comparator<TempleOSRSTableRow> comparator)
	{
		rows.sort(comparator);
		rebuild();
	}
}
