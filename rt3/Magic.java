package rt3;

import com.zentek.colorbot.client.api.image.Bitmap;
import com.zentek.colorbot.client.api.image.ColorSpace;
import com.zentek.colorbot.client.api.image.filter.Filter;
import com.zentek.colorbot.client.api.input.MouseButton;
import com.zentek.colorbot.client.api.util.Calculations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Copyright (c) 2016 - 2017 Colorbot (<a href="https://www.colorbot.org">https://www.colorbot.org</a>) and contributors.
 * <br>
 * <br>Licensed under the Colorbot License, Version 1.0 (the "License");
 * <br>you may not use this file except in compliance with the License.
 * <br>You may obtain a copy of the License at:
 * <br>
 * <br> <a href="https://www.colorbot.org/license/LICENSE-1.0">https://www.colorbot.org/license/LICENSE-1.0</a>
 * <br>
 * <br>Unless required by applicable law or agreed to in writing, software
 * <br>distributed under the License is distributed on an "AS IS" BASIS,
 * <br>WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * <br>See the License for the specific language governing permissions and limitations under the License.
 * <br>
 * <br> Package: rt3
 * <br> File: Magic.java
 * <br> Purpose: Handles the Magic tab and it's spells.
 * <br>
 * <br>Copyright (c) 2016 - 2017 Colorbot (<a href="https://www.colorbot.org">https://www.colorbot.org</a>) and contributors.
 * All rights reserved.
 * <br>
 *
 * @author <b>Colorbot</b>
 */
public final class Magic {

	private final RT3Library library;
	private SpellBook cachedSpellBook = SpellBook.NORMAL;

	private static final int SLOT_WIDTH = 22;
	private static final int SLOT_HEIGHT = 22;

	private final Filter spellFilter;

	public enum SpellBook {
		NORMAL, ANCIENT, LUNAR
	}

	//Normal spells
	public static Spell HOME_TELEPORT, WIND_STRIKE, CONFUSE, ENCHANT_CROSSBOW_BOLT, WATER_STRIKE, LVL_1_ENCHANT,
			EARTH_STRIKE, WEAKEN, FIRE_STRIKE, BONES_TO_BANANAS, WIND_BOLT, CURSE, BIND, LOW_ALCHEMY,
			WATER_BOLT, VARROCK_TELEPORT, LVL_2_ENCHANT, EARTH_BOLT, LUMBRIDGE_TELEPORT, TELEKINETIC_GRAB, FIRE_BOLT,
			FALADOR_TELEPORT, CRUMBLE_UNDEAD, TELEPORT_TO_HOUSE, WIND_BLAST, SUPERHEAT_ITEM, CAMELOT_TELEPORT, WATER_BLAST,
			LVL_3_ENCHANT, IBAN_BLAST, SNARE, MAGIC_DART, ARDOUGNE_TELEPORT, EARTH_BLAST, HIGH_ALCHEMY, CHANGE_WATER_ORB,
			LVL_4_ENCHANT, WATCH_TOWER_TELEPORT, FIRE_BLAST, CHANGE_EARTH_ORB, BONES_TO_PEACHES, SARADOMIN_STRIKE,
			CLAWS_OF_GUTHIX, FLAMES_OF_ZAMORAK, TROLLHEIM_TELEPORT, WIND_WAVE, CHANGE_FIRE_ORB, TELEPORT_TO_APE_ATOLL,
			WATER_WAVE, CHANGE_AIR_ORB, VULNERABILITY, LVL_5_ENCHANT, TELEPORT_TO_KOUREND, EARTH_WAVE, ENFEEBLE,
			TELEOTHER_LUMBRIDGE, FIRE_WAVE, ENTANGLE, STUN, CHARGE, TELEOTHER_FALADOR, TELE_BLOCK,
			TELEPORT_TO_BOUNTY_TARGET, LVL_6_ENCHANT, TELEOTHER_CAMELOT, LVL_7_ENCHANT;

	//Ancient spells
	public static Spell SMOKE_RUSH, SHADOW_RUSH, BLOOD_RUSH, ICE_RUSH, SMOKE_BURST, SHADOW_BURST, BLOOD_BURST,
			ICE_BURST, SMOKE_BLITZ, SHADOW_BLITZ, BLOOD_BLITZ, ICE_BLITZ, SMOKE_BARRAGE, SHADOW_BARRAGE,
			BLOOD_BARRAGE, ICE_BARRAGE;

	//Lunar spells
	public static Spell LUNAR_HOME_TELEPORT, BAKE_PIE, GEOMANCY, CURE_PLANT, MONSTER_EXAMINE, NPC_CONTACT, CURE_OTHER,
			HUMIDIFY, MOONCLAN_TELEPORT, TELEGROUP_MOONCLAN, CURE_ME, HUNTER_KIT, WATERBIRTH_TELEPORT, TELEGROUP_WATERBIRTH,
			CURE_GROUP, BARBARIAN_TELEPORT, STAT_SPY, TELEGROUP_BARBARIAN, SUPERGLASS_MAKE, KHAZARD_TELEPORT, TAN_LEATHER,
			TELEGROUP_KHAZARD, DREAM, STRING_JEWELLERY, STAT_RESTORE_POT_SHARE, MAGIC_IMBUE, FERTILE_SOIL, BOOST_POTION_SHARE,
			FISHING_GUILD_TELEPORT, TELEGROUP_FISHING_GUILD, PLANK_MAKE, CATHERBY_TELEPORT, TELEGROUP_CATHERBY,
			ICE_PLATEAU_TELEPORT, RECHARGE_DRAGONSTONE, TELEGROUP_ICE_PLATEAU, ENERGY_TRANSFER, HEAL_OTHER, VENGEANCE_OTHER,
			VENGEANCE, HEAL_GROUP, SPELL_BOOK_SWAP;


	protected Magic(final RT3Library library) {
		this.library = library;
		this.spellFilter = new Filter(Calculations.packRGB(66, 64, 56), 20D) {
			@Override
			public Bitmap filter(final Bitmap bitmap) {
				for (int x = 0; x < bitmap.getWidth(); x++) {
					for (int y = 0; y < bitmap.getHeight(); y++) {
						if (isIndexColor(bitmap.getSamples(x, y))) {
							bitmap.setRGB(x, y, 0);
						}
					}
				}
				return bitmap;
			}
		};

		//Normal spells
		HOME_TELEPORT = new Spell("Home teleport", library);
		WIND_STRIKE = new Spell("Wind strike", library);
		CONFUSE = new Spell("Confuse", library);
		ENCHANT_CROSSBOW_BOLT = new Spell("Enchant crossbow bolt", library);
		WATER_STRIKE = new Spell("Water strike", library);
		LVL_1_ENCHANT = new Spell("Lvl 1 enchant", library);
		EARTH_STRIKE = new Spell("Earth strike", library);
		WEAKEN = new Spell("Weaken", library);
		FIRE_STRIKE = new Spell("Fire strike", library);
		BONES_TO_BANANAS = new Spell("Bones to bananas", library);
		WIND_BOLT = new Spell("Wind bolt", library);
		CURSE = new Spell("Curse", library);
		BIND = new Spell("Bind", library);
		LOW_ALCHEMY = new Spell("Low alchemy", library);
		WATER_BOLT = new Spell("Water bolt", library);
		VARROCK_TELEPORT = new Spell("Varrock teleport", library);
		LVL_2_ENCHANT = new Spell("Lvl 2 enchant", library);
		EARTH_BOLT = new Spell("Earth bolt", library);
		LUMBRIDGE_TELEPORT = new Spell("Lumbridge teleport", library);
		TELEKINETIC_GRAB = new Spell("Telekinetic grab", library);
		FIRE_BOLT = new Spell("Fire bolt", library);
		FALADOR_TELEPORT = new Spell("Falador teleport", library);
		CRUMBLE_UNDEAD = new Spell("Crumble undead", library);
		TELEPORT_TO_HOUSE = new Spell("Teleport to house", library);
		WIND_BLAST = new Spell("Wind blast", library);
		SUPERHEAT_ITEM = new Spell("Superheat item", library);
		CAMELOT_TELEPORT = new Spell("Camelot teleport", library);
		WATER_BLAST = new Spell("Water blast", library);
		LVL_3_ENCHANT = new Spell("Lvl 3 enchant", library);
		IBAN_BLAST = new Spell("Iban blast", library);
		SNARE = new Spell("Snare", library);
		MAGIC_DART = new Spell("Magic dart", library);
		ARDOUGNE_TELEPORT = new Spell("Ardougne teleport", library);
		EARTH_BLAST = new Spell("Earth blast", library);
		HIGH_ALCHEMY = new Spell("High alchemy", library);
		CHANGE_WATER_ORB = new Spell("Change water orb", library);
		LVL_4_ENCHANT = new Spell("Lvl 4 enchant", library);
		WATCH_TOWER_TELEPORT = new Spell("Watch tower teleport", library);
		FIRE_BLAST = new Spell("Fire blast", library);
		CHANGE_EARTH_ORB = new Spell("Change earth orb", library);
		BONES_TO_PEACHES = new Spell("Bones to peaches", library);
		SARADOMIN_STRIKE = new Spell("Saradomin strike", library);
		CLAWS_OF_GUTHIX = new Spell("Claws of guthix", library);
		FLAMES_OF_ZAMORAK = new Spell("Flames of zamorak", library);
		TROLLHEIM_TELEPORT = new Spell("Trollheim teleport", library);
		WIND_WAVE = new Spell("Wind wave", library);
		CHANGE_FIRE_ORB = new Spell("Change fire orb", library);
		TELEPORT_TO_APE_ATOLL = new Spell("Teleport to ape atoll", library);
		WATER_WAVE = new Spell("Water wave", library);
		CHANGE_AIR_ORB = new Spell("Change air orb", library);
		VULNERABILITY = new Spell("Vulnerability", library);
		LVL_5_ENCHANT = new Spell("Lvl 5 enchant", library);
		TELEPORT_TO_KOUREND = new Spell("Teleport to kourend", library);
		EARTH_WAVE = new Spell("Earth wave", library);
		ENFEEBLE = new Spell("Enfeeble", library);
		TELEOTHER_LUMBRIDGE = new Spell("Teleother lumbridge", library);
		FIRE_WAVE = new Spell("Fire wave", library);
		ENTANGLE = new Spell("Entangle", library);
		STUN = new Spell("Stun", library);
		CHARGE = new Spell("Charge", library);
		TELEOTHER_FALADOR = new Spell("Teleother falador", library);
		TELE_BLOCK = new Spell("Tele block", library);
		TELEPORT_TO_BOUNTY_TARGET = new Spell("Teleother to bounty target", library);
		LVL_6_ENCHANT = new Spell("Lvl 6 enchant", library);
		TELEOTHER_CAMELOT = new Spell("Teleother camelot", library);
		LVL_7_ENCHANT = new Spell("Lvl 7 enchant", library);

		//Ancient spells
		SMOKE_RUSH = new Spell("Smoke rush", library);
		SHADOW_RUSH = new Spell("Shadow rush", library);
		BLOOD_RUSH = new Spell("Blood rush", library);
		ICE_RUSH = new Spell("Ice rush", library);
		SMOKE_BURST = new Spell("Smoke burst", library);
		SHADOW_BURST = new Spell("Shadow burst", library);
		BLOOD_BURST = new Spell("Blood burst", library);
		ICE_BURST = new Spell("Ice burst", library);
		SMOKE_BLITZ = new Spell("Smoke blitz", library);
		SHADOW_BLITZ = new Spell("Shadow blitz", library);
		BLOOD_BLITZ = new Spell("Blood blitz", library);
		ICE_BLITZ = new Spell("Ice blitz", library);
		SMOKE_BARRAGE = new Spell("Smoke barrage", library);
		SHADOW_BARRAGE = new Spell("Shadow barrage", library);
		BLOOD_BARRAGE = new Spell("Blood barrage", library);
		ICE_BARRAGE = new Spell("Ice barrage", library);

		//Lunar spells
		LUNAR_HOME_TELEPORT = new Spell("Lunar home teleport", library);
		BAKE_PIE = new Spell("Bake pie", library);
		GEOMANCY = new Spell("Geomancy", library);
		CURE_PLANT = new Spell("Cure plant", library);
		MONSTER_EXAMINE = new Spell("Monster examine", library);
		NPC_CONTACT = new Spell("NPC contact", library);
		CURE_OTHER = new Spell("Cure other", library);
		HUMIDIFY = new Spell("Humidify", library);
		MOONCLAN_TELEPORT = new Spell("Moonclan teleport", library);
		TELEGROUP_MOONCLAN = new Spell("Telegroup moonclan", library);
		CURE_ME = new Spell("Cure me", library);
		HUNTER_KIT = new Spell("", library);
		WATERBIRTH_TELEPORT = new Spell("Waterbirth teleport", library);
		TELEGROUP_WATERBIRTH = new Spell("Telegroup waterbirth", library);
		CURE_GROUP = new Spell("Cure group", library);
		BARBARIAN_TELEPORT = new Spell("Barbarian teleport", library);
		STAT_SPY = new Spell("Stat spy", library);
		TELEGROUP_BARBARIAN = new Spell("Telegroup barbarian", library);
		SUPERGLASS_MAKE = new Spell("Superglass make", library);
		KHAZARD_TELEPORT = new Spell("Khazard teleport", library);
		TAN_LEATHER = new Spell("Tan leather", library);
		TELEGROUP_KHAZARD = new Spell("Telegroup khazard", library);
		DREAM = new Spell("Dream", library);
		STRING_JEWELLERY = new Spell("String jewellery", library);
		STAT_RESTORE_POT_SHARE = new Spell("Stat restore pot share", library);
		MAGIC_IMBUE = new Spell("Magic imbue", library);
		FERTILE_SOIL = new Spell("Fertile soil", library);
		BOOST_POTION_SHARE = new Spell("Boost potion share", library);
		FISHING_GUILD_TELEPORT = new Spell("Fishing guild teleport", library);
		TELEGROUP_FISHING_GUILD = new Spell("Telegroup fishing guild", library);
		PLANK_MAKE = new Spell("Plank make", library);
		CATHERBY_TELEPORT = new Spell("Catherby teleport", library);
		TELEGROUP_CATHERBY = new Spell("Telegroup catherby", library);
		ICE_PLATEAU_TELEPORT = new Spell("Ice plateau teleport", library);
		RECHARGE_DRAGONSTONE = new Spell("Recharge dragonstone", library);
		TELEGROUP_ICE_PLATEAU = new Spell("Telegroup ice plateau", library);
		ENERGY_TRANSFER = new Spell("Energy transfer", library);
		HEAL_OTHER = new Spell("Heal other", library);
		VENGEANCE_OTHER = new Spell("Vengeance other", library);
		VENGEANCE = new Spell("Vengeance", library);
		HEAL_GROUP = new Spell("Heal group", library);
		SPELL_BOOK_SWAP = new Spell("Spell book swap", library);

		this.calibrate();
	}

	/**
	 * Calculates the bounding rectangles of all {@link Magic.Spell} spells.
	 */
	public final void calibrate() {
		if (!library.game.isResizableMode()) {
			int row = -1;
			int numSlot = 0;
			for (final Spell spells : getSpells()) {
				if (numSlot % 7 == 0) {
					row += 1;
					numSlot = 0;
				}
				spells.setLocation(559 + (numSlot * SLOT_WIDTH) + (numSlot * 2), 220 + (row * SLOT_HEIGHT) + (row * 2));
				numSlot += 1;
			}
		}
	}

	/**
	 * Select a {@code spell}.
	 * @param spell spell to cast
	 * @return <b>true</b> if the spell is already selected or has been clicked.
	 */
	public final boolean select(final Spell spell) {
		if(library.tabs.getSelectedTab() != Tabs.MAGIC) {
			library.tabs.select(Tabs.MAGIC);
		}
		return spell.isSelected() || spell.click(MouseButton.LEFT);
	}

	/**
	 * Get the selected spell.
	 * <br><b>note:</b> Opens the magic tab.
	 *
	 * @return the selected {@link Magic.Spell} spell or null if none is selected.
	 */
	public final Spell getSelectedSpell() {
		for (final Spell spell : getSpells()) {
			if (spell.isSelected()) {
				return spell;
			}
		}
		return null;
	}

	/**
	 * Check if a spell is selected.
	 * <br><b>note:</b> Opens the magic tab.
	 *
	 * @return <b>true</b> if a spell is selected.
	 */
	public final boolean isSpellSelected() {
		return getSelectedSpell() != null;
	}

	/**
	 * Get the current spell book.
	 * <br><b>note:</b> Opens the magic tab.
	 *
	 * @param openTab open magic tab or use cached spell book type
	 * @return the current {@link Magic.SpellBook}
	 */
	public final SpellBook getSpellBook(final boolean openTab) {
		if (openTab && library.tabs.getSelectedTab() != Tabs.MAGIC) {
			Tabs.MAGIC.click(MouseButton.LEFT);
		}
		//TODO: detect which spellBook is currently loaded (detect few colors at static positions for all spellbooks
		return SpellBook.NORMAL;
	}

	public final List<Spell> getSpells() {
		switch (getSpellBook(false)) {

			case NORMAL:
				return Arrays.asList(HOME_TELEPORT, WIND_STRIKE, CONFUSE, ENCHANT_CROSSBOW_BOLT, WATER_STRIKE, LVL_1_ENCHANT,
						EARTH_STRIKE, WEAKEN, FIRE_STRIKE, BONES_TO_BANANAS, WIND_BOLT, CURSE, BIND, LOW_ALCHEMY,
						WATER_BOLT, VARROCK_TELEPORT, LVL_2_ENCHANT, EARTH_BOLT, LUMBRIDGE_TELEPORT, TELEKINETIC_GRAB,
						FIRE_BOLT, FALADOR_TELEPORT, CRUMBLE_UNDEAD, TELEPORT_TO_HOUSE, WIND_BLAST, SUPERHEAT_ITEM,
						CAMELOT_TELEPORT, WATER_BLAST, LVL_3_ENCHANT, IBAN_BLAST, SNARE, MAGIC_DART, ARDOUGNE_TELEPORT,
						EARTH_BLAST, HIGH_ALCHEMY, CHANGE_WATER_ORB, LVL_4_ENCHANT, WATCH_TOWER_TELEPORT, FIRE_BLAST,
						CHANGE_EARTH_ORB, BONES_TO_PEACHES, SARADOMIN_STRIKE, CLAWS_OF_GUTHIX, FLAMES_OF_ZAMORAK,
						TROLLHEIM_TELEPORT, WIND_WAVE, CHANGE_FIRE_ORB, TELEPORT_TO_APE_ATOLL, WATER_WAVE, CHANGE_AIR_ORB,
						VULNERABILITY, LVL_5_ENCHANT, TELEPORT_TO_KOUREND, EARTH_WAVE, ENFEEBLE, TELEOTHER_LUMBRIDGE,
						FIRE_WAVE, ENTANGLE, STUN, CHARGE, TELEOTHER_FALADOR, TELE_BLOCK, TELEPORT_TO_BOUNTY_TARGET,
						LVL_6_ENCHANT, TELEOTHER_CAMELOT, LVL_7_ENCHANT);
			case ANCIENT:
				return Arrays.asList(SMOKE_RUSH, SHADOW_RUSH, BLOOD_RUSH, ICE_RUSH, SMOKE_BURST, SHADOW_BURST, BLOOD_BURST,
						ICE_BURST, SMOKE_BLITZ, SHADOW_BLITZ, BLOOD_BLITZ, ICE_BLITZ, SMOKE_BARRAGE, SHADOW_BARRAGE,
						BLOOD_BARRAGE, ICE_BARRAGE);
			case LUNAR:
				Arrays.asList(LUNAR_HOME_TELEPORT, BAKE_PIE, GEOMANCY, CURE_PLANT, MONSTER_EXAMINE, NPC_CONTACT, CURE_OTHER,
						HUMIDIFY, MOONCLAN_TELEPORT, TELEGROUP_MOONCLAN, CURE_ME, HUNTER_KIT, WATERBIRTH_TELEPORT, TELEGROUP_WATERBIRTH,
						CURE_GROUP, BARBARIAN_TELEPORT, STAT_SPY, TELEGROUP_BARBARIAN, SUPERGLASS_MAKE, KHAZARD_TELEPORT, TAN_LEATHER,
						TELEGROUP_KHAZARD, DREAM, STRING_JEWELLERY, STAT_RESTORE_POT_SHARE, MAGIC_IMBUE, FERTILE_SOIL, BOOST_POTION_SHARE,
						FISHING_GUILD_TELEPORT, TELEGROUP_FISHING_GUILD, PLANK_MAKE, CATHERBY_TELEPORT, TELEGROUP_CATHERBY,
						ICE_PLATEAU_TELEPORT, RECHARGE_DRAGONSTONE, TELEGROUP_ICE_PLATEAU, ENERGY_TRANSFER, HEAL_OTHER, VENGEANCE_OTHER,
						VENGEANCE, HEAL_GROUP, SPELL_BOOK_SWAP);
		}
		return new ArrayList<>();
	}

	public class Spell extends Widget {

		private final String name;

		public Spell(final String name, final RT3Library library) {
			super(0, 0, SLOT_WIDTH, SLOT_HEIGHT, library);
			this.name = name;
		}

		public final String getName() {
			return name;
		}

		public void setLocation(final int x, final int y) {
			this.x = x;
			this.y = y;
		}

		/**
		 * Click on a given {@code spell} selecting a specific {@code action}.
		 *
		 * @param action action to select
		 */
		public boolean click(final String action) {
			if (library.tabs.getSelectedTab() != Tabs.MAGIC) {
				Tabs.MAGIC.click(MouseButton.LEFT);
			}
			return super.click(action);
		}

		/**
		 * Click on a given {@code spell} with a specific {@code mouseButton}.
		 *
		 * @param mouseButton {@link MouseButton} mouseButton
		 * @return <b>true</b> when the action is completed.
		 */
		public boolean click(final MouseButton mouseButton) {
			if (library.tabs.getSelectedTab() != Tabs.MAGIC) {
				Tabs.MAGIC.click(MouseButton.LEFT);
			}
			return super.click(mouseButton);
		}

		/**
		 * Check if a spell is enabled.
		 * <b>note:</b> Magic tab must be opened.
		 * @return <b>true</b> if there are enough runes left to cast the spell.
		 */
		public final boolean isEnabled() {
			final Bitmap spell = spellFilter.filter(getBitmap());
				for (int x = 0; x < width; x++) {
					for (int y = 0; y < height; y++) {
						final double[] rgb = Calculations.unpackRGB(spell.getRGB(x, y));
						final float[] hsl = ColorSpace.getInstance(ColorSpace.CS_HSB).fromRGB(new float[]
								{(float) rgb[0], (float) rgb[1], (float) rgb[2]});
						if(hsl[2] > 0.3f) {//Check if pixel is bright
							return true;
						}
					}
				}
			return false;
		}

		/**
		 * Check if the spell is selected.
		 * <b>note:</b> Magic tab must be opened.
		 * @return <b>true</b> if the spell is selected for casting.
		 */
		public final boolean isSelected() {
			return library.tabs.getSelectedTab() == Tabs.MAGIC && getBitmap().findRGB(255, 255, 255).size() > 20;
		}
	}
}
