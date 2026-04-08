package com.templeosrs.ui.ranks;

import com.google.common.collect.ImmutableList;
import com.templeosrs.util.TempleHiscoreSkill;
import static com.templeosrs.util.TempleHiscoreSkill.ABYSSAL_SIRE;
import static com.templeosrs.util.TempleHiscoreSkill.AGILITY;
import static com.templeosrs.util.TempleHiscoreSkill.ALCHEMICAL_HYDRA;
import static com.templeosrs.util.TempleHiscoreSkill.AMOXLIATL;
import static com.templeosrs.util.TempleHiscoreSkill.ARAXXOR;
import static com.templeosrs.util.TempleHiscoreSkill.ARTIO;
import static com.templeosrs.util.TempleHiscoreSkill.ATTACK;
import static com.templeosrs.util.TempleHiscoreSkill.BARROWS_CHESTS;
import static com.templeosrs.util.TempleHiscoreSkill.BRUTUS;
import static com.templeosrs.util.TempleHiscoreSkill.BRYOPHYTA;
import static com.templeosrs.util.TempleHiscoreSkill.CALLISTO;
import static com.templeosrs.util.TempleHiscoreSkill.CALVARION;
import static com.templeosrs.util.TempleHiscoreSkill.CERBERUS;
import static com.templeosrs.util.TempleHiscoreSkill.CHAMBERS_OF_XERIC;
import static com.templeosrs.util.TempleHiscoreSkill.CHAMBERS_OF_XERIC_CHALLENGE_MODE;
import static com.templeosrs.util.TempleHiscoreSkill.CHAOS_ELEMENTAL;
import static com.templeosrs.util.TempleHiscoreSkill.CHAOS_FANATIC;
import static com.templeosrs.util.TempleHiscoreSkill.COLLECTIONS;
import static com.templeosrs.util.TempleHiscoreSkill.COMMANDER_ZILYANA;
import static com.templeosrs.util.TempleHiscoreSkill.CONSTRUCTION;
import static com.templeosrs.util.TempleHiscoreSkill.COOKING;
import static com.templeosrs.util.TempleHiscoreSkill.CORPOREAL_BEAST;
import static com.templeosrs.util.TempleHiscoreSkill.CRAFTING;
import static com.templeosrs.util.TempleHiscoreSkill.CRAZY_ARCHAEOLOGIST;
import static com.templeosrs.util.TempleHiscoreSkill.DAGANNOTH_PRIME;
import static com.templeosrs.util.TempleHiscoreSkill.DAGANNOTH_REX;
import static com.templeosrs.util.TempleHiscoreSkill.DAGANNOTH_SUPREME;
import static com.templeosrs.util.TempleHiscoreSkill.DEFENCE;
import static com.templeosrs.util.TempleHiscoreSkill.DERANGED_ARCHAEOLOGIST;
import static com.templeosrs.util.TempleHiscoreSkill.DOOM_OF_MOKHAIOTL;
import static com.templeosrs.util.TempleHiscoreSkill.DUKE_SUCELLUS;
import static com.templeosrs.util.TempleHiscoreSkill.FARMING;
import static com.templeosrs.util.TempleHiscoreSkill.FIREMAKING;
import static com.templeosrs.util.TempleHiscoreSkill.FISHING;
import static com.templeosrs.util.TempleHiscoreSkill.FLETCHING;
import static com.templeosrs.util.TempleHiscoreSkill.GENERAL_GRAARDOR;
import static com.templeosrs.util.TempleHiscoreSkill.GIANT_MOLE;
import static com.templeosrs.util.TempleHiscoreSkill.GROTESQUE_GUARDIANS;
import static com.templeosrs.util.TempleHiscoreSkill.HERBLORE;
import static com.templeosrs.util.TempleHiscoreSkill.HESPORI;
import static com.templeosrs.util.TempleHiscoreSkill.HITPOINTS;
import static com.templeosrs.util.TempleHiscoreSkill.HUEYCOATL;
import static com.templeosrs.util.TempleHiscoreSkill.HUNTER;
import static com.templeosrs.util.TempleHiscoreSkill.KALPHITE_QUEEN;
import static com.templeosrs.util.TempleHiscoreSkill.KING_BLACK_DRAGON;
import static com.templeosrs.util.TempleHiscoreSkill.KRAKEN;
import static com.templeosrs.util.TempleHiscoreSkill.KREEARRA;
import static com.templeosrs.util.TempleHiscoreSkill.KRIL_TSUTSAROTH;
import static com.templeosrs.util.TempleHiscoreSkill.LUNAR_CHESTS;
import static com.templeosrs.util.TempleHiscoreSkill.MAGIC;
import static com.templeosrs.util.TempleHiscoreSkill.MIMIC;
import static com.templeosrs.util.TempleHiscoreSkill.MINING;
import static com.templeosrs.util.TempleHiscoreSkill.NEX;
import static com.templeosrs.util.TempleHiscoreSkill.OBOR;
import static com.templeosrs.util.TempleHiscoreSkill.PHANTOM_MUSPAH;
import static com.templeosrs.util.TempleHiscoreSkill.PHOSANIS_NIGHTMARE;
import static com.templeosrs.util.TempleHiscoreSkill.PRAYER;
import static com.templeosrs.util.TempleHiscoreSkill.RANGED;
import static com.templeosrs.util.TempleHiscoreSkill.RUNECRAFT;
import static com.templeosrs.util.TempleHiscoreSkill.SAILING;
import static com.templeosrs.util.TempleHiscoreSkill.SARACHNIS;
import static com.templeosrs.util.TempleHiscoreSkill.SCORPIA;
import static com.templeosrs.util.TempleHiscoreSkill.SCURRIUS;
import static com.templeosrs.util.TempleHiscoreSkill.SHELLBANE_GRYPHON;
import static com.templeosrs.util.TempleHiscoreSkill.SKOTIZO;
import static com.templeosrs.util.TempleHiscoreSkill.SLAYER;
import static com.templeosrs.util.TempleHiscoreSkill.SMITHING;
import static com.templeosrs.util.TempleHiscoreSkill.SOL_HEREDIT;
import static com.templeosrs.util.TempleHiscoreSkill.SPINDEL;
import static com.templeosrs.util.TempleHiscoreSkill.STRENGTH;
import static com.templeosrs.util.TempleHiscoreSkill.TEMPOROSS;
import static com.templeosrs.util.TempleHiscoreSkill.THEATRE_OF_BLOOD;
import static com.templeosrs.util.TempleHiscoreSkill.THEATRE_OF_BLOOD_CHALLENGE_MODE;
import static com.templeosrs.util.TempleHiscoreSkill.THERMONUCLEAR_SMOKE_DEVIL;
import static com.templeosrs.util.TempleHiscoreSkill.THE_CORRUPTED_GAUNTLET;
import static com.templeosrs.util.TempleHiscoreSkill.THE_GAUNTLET;
import static com.templeosrs.util.TempleHiscoreSkill.THE_LEVIATHAN;
import static com.templeosrs.util.TempleHiscoreSkill.THE_NIGHTMARE;
import static com.templeosrs.util.TempleHiscoreSkill.THE_ROYAL_TITANS;
import static com.templeosrs.util.TempleHiscoreSkill.THE_WHISPERER;
import static com.templeosrs.util.TempleHiscoreSkill.THIEVING;
import static com.templeosrs.util.TempleHiscoreSkill.TOMBS_OF_AMASCUT;
import static com.templeosrs.util.TempleHiscoreSkill.TOMBS_OF_AMASCUT_EXPERT;
import static com.templeosrs.util.TempleHiscoreSkill.TZKAL_ZUK;
import static com.templeosrs.util.TempleHiscoreSkill.TZTOK_JAD;
import static com.templeosrs.util.TempleHiscoreSkill.VARDORVIS;
import static com.templeosrs.util.TempleHiscoreSkill.VENENATIS;
import static com.templeosrs.util.TempleHiscoreSkill.VETION;
import static com.templeosrs.util.TempleHiscoreSkill.VORKATH;
import static com.templeosrs.util.TempleHiscoreSkill.WINTERTODT;
import static com.templeosrs.util.TempleHiscoreSkill.WOODCUTTING;
import static com.templeosrs.util.TempleHiscoreSkill.ZALCANO;
import static com.templeosrs.util.TempleHiscoreSkill.ZULRAH;
import com.templeosrs.util.player.TemplePlayer;
import com.templeosrs.util.player.TemplePlayerData;
import com.templeosrs.util.player.TemplePlayerSkill;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.inject.Inject;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import net.runelite.client.hiscore.HiscoreSkillType;
import net.runelite.client.ui.ColorScheme;

public class TempleActivity extends JPanel
{
	public static final List<TempleHiscoreSkill> SKILLS = ImmutableList.of(
		ATTACK, DEFENCE, STRENGTH,
		HITPOINTS, RANGED, PRAYER,
		MAGIC, COOKING, WOODCUTTING,
		FLETCHING, FISHING, FIREMAKING,
		CRAFTING, SMITHING, MINING,
		HERBLORE, AGILITY, THIEVING,
		SLAYER, FARMING, RUNECRAFT,
		HUNTER, CONSTRUCTION, SAILING
	);

	public static final List<TempleHiscoreSkill> BOSSES = ImmutableList.of(
		ABYSSAL_SIRE, ALCHEMICAL_HYDRA, AMOXLIATL, ARAXXOR, ARTIO, BARROWS_CHESTS, BRUTUS,
		BRYOPHYTA, CALLISTO, CALVARION, CERBERUS,
		CHAMBERS_OF_XERIC, CHAMBERS_OF_XERIC_CHALLENGE_MODE, CHAOS_ELEMENTAL,
		CHAOS_FANATIC, COMMANDER_ZILYANA, CORPOREAL_BEAST,
		DAGANNOTH_PRIME, DAGANNOTH_REX, DAGANNOTH_SUPREME,
		CRAZY_ARCHAEOLOGIST, DERANGED_ARCHAEOLOGIST, DOOM_OF_MOKHAIOTL, DUKE_SUCELLUS, GENERAL_GRAARDOR,
		GIANT_MOLE, GROTESQUE_GUARDIANS, HESPORI,
		KALPHITE_QUEEN, KING_BLACK_DRAGON, KRAKEN,
		KREEARRA, KRIL_TSUTSAROTH, LUNAR_CHESTS, MIMIC,
		NEX, THE_NIGHTMARE, PHOSANIS_NIGHTMARE,
		OBOR, PHANTOM_MUSPAH, SARACHNIS, SCORPIA, SCURRIUS, SHELLBANE_GRYPHON,
		SKOTIZO, SOL_HEREDIT, SPINDEL, TEMPOROSS, THE_GAUNTLET,
		THE_CORRUPTED_GAUNTLET, HUEYCOATL, THE_LEVIATHAN, THE_ROYAL_TITANS, THE_WHISPERER, THEATRE_OF_BLOOD, THEATRE_OF_BLOOD_CHALLENGE_MODE,
		THERMONUCLEAR_SMOKE_DEVIL, TOMBS_OF_AMASCUT, TOMBS_OF_AMASCUT_EXPERT, TZKAL_ZUK, TZTOK_JAD, VARDORVIS,
		VENENATIS, VETION, VORKATH,
		WINTERTODT, ZALCANO, ZULRAH, COLLECTIONS
	);

	private static final Color[] COLORS = {ColorScheme.DARKER_GRAY_COLOR, ColorScheme.DARK_GRAY_HOVER_COLOR};

	final Map<String, TempleActivityTableRow> map = new HashMap<>();

	final TempleActivitySortHeader sortPanel;

	final TempleActivityTableRow overall;

	ArrayList<TempleActivityTableRow> rows = new ArrayList<>();

	HiscoreSkillType hiscoreSkillType;

	long total;

	@Inject
	public TempleActivity(HiscoreSkillType type)
	{
		hiscoreSkillType = type;

		setLayout(new GridLayout(0, 1));
		setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED, ColorScheme.DARK_GRAY_COLOR, ColorScheme.SCROLL_TRACK_COLOR), new EmptyBorder(5, 5, 5, 5)));
		setBackground(ColorScheme.DARKER_GRAY_COLOR);

		sortPanel = new TempleActivitySortHeader(this, hiscoreSkillType);

		overall = new TempleActivityTableRow("overall", "Overall", COLORS[1], HiscoreSkillType.OVERALL);

		initialize();
	}

	private void initialize()
	{
		/* reset rows */
		rows.clear();

		/* add default panels */
		add(sortPanel);
		add(overall);

		/* get correct list of HiscoreSkills */
		List<TempleHiscoreSkill> list = (hiscoreSkillType.equals(HiscoreSkillType.SKILL) ? SKILLS : BOSSES);

		/* for each skill in list */
		for (int i = 0; i < list.size(); i++)
		{
			TempleHiscoreSkill skill = list.get(i);
			String formattedKey = skill.getName().replaceAll("[^A-Za-z0-9]", "").toLowerCase();

			/* create a skill-row, add <key, row> to map, add row to rows-list */
			TempleActivityTableRow row = new TempleActivityTableRow(formattedKey, skill.getName(), COLORS[i % 2], hiscoreSkillType);
			map.put(formattedKey, row);
			rows.add(row);
			add(row);
		}
	}

	public void update(TemplePlayer result)
	{
		/* determine and get type of json */
		TemplePlayerData playerData = hiscoreSkillType.equals(HiscoreSkillType.SKILL) ? result.playerSkillsOverview.data : result.playerBossesOverview.data;

		/* for each entry in playerData */
		for (Map.Entry<String, TemplePlayerSkill> entry : playerData.table.entrySet())
		{
			/* get the HiscoreSkill of that entry by index */
			TempleHiscoreSkill skill;
			try
			{
				/* If a skill can't be found, continue */
				skill = TempleHiscoreSkill.values()[entry.getValue().index];
			}
			catch (IndexOutOfBoundsException ignored)
			{
				continue;
			}

			String formattedKey = skill.getName().replaceAll("[^A-Za-z0-9]", "").toLowerCase();

			/* if map contains this HighscoreSkill */
			if (map.containsKey(formattedKey))
			{
				/* get the mapped, skill-row */
				TempleActivityTableRow row = map.get(formattedKey);
				TemplePlayerSkill skillData = playerData.table.get(skill.getName());

				long total = Objects.nonNull(skillData.xp) ? skillData.xp.longValue() : 0;
				long levels = Objects.nonNull(skillData.level) ? skillData.level.longValue() : 0;
				long rank = Objects.nonNull(skillData.rank) ? skillData.rank.longValue() : 0;
				double ehp = hiscoreSkillType.equals(HiscoreSkillType.SKILL) ? (Objects.nonNull(skillData.ehp) ? skillData.ehp : 0) : (Objects.nonNull(skillData.ehb) ? skillData.ehb : 0);

				this.total += total;

				/* update skill-row's values */
				row.update(total, levels, rank, ehp);
			}
		}
	}

	/* update overall row */
	public void update(long rank, double ehp)
	{
		overall.update(total, 0, rank, ehp);
	}

	/* reset activity panel to defaults */
	public void reset()
	{
		total = 0;

		overall.reset();
		sortPanel.reset();

		removeAll();

		initialize();
	}

	/* re-build list of skills by sorted row-list */
	private void rebuild()
	{
		int i = 0;
		for (TempleActivityTableRow row : rows)
		{
			String skill = row.name;
			TempleActivityTableRow entry = map.get(skill);
			entry.setBackground(COLORS[i++ % 2]);
			add(entry);
		}

		repaint();
		revalidate();
	}

	/* sort row-list with comparator passed in through sort-filter mouse event */
	void sort(Comparator<TempleActivityTableRow> comparator)
	{
		rows.sort(comparator);
		rebuild();
	}
}
