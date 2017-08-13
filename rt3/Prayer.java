package rt3;

import com.zentek.colorbot.client.api.image.filter.RGBMultiFilter;
import com.zentek.colorbot.client.api.image.ocr.OCR;
import com.zentek.colorbot.client.api.input.MouseButton;
import com.zentek.colorbot.client.api.util.Calculations;

import java.awt.*;
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
 * <br> File: Prayer.java
 * <br> Purpose: Handles in-game Prayer and Powers.
 * <br>
 * <br>Copyright (c) 2016 - 2017 Colorbot (<a href="https://www.colorbot.org">https://www.colorbot.org</a>) and contributors.
 * All rights reserved.
 * <br>
 *
 * @author <b>Colorbot</b>
 */
public final class Prayer {

	public Power THICK_SKIN, BURST_OF_STRENGTH, CLARITY_OF_THOUGHT, SHARP_EYE, MYSTIC_WILL, ROCK_SKIN,
			SUPER_HUMAN_STRENGTH, IMPROVED_REFLEXES, RAPID_RESTORE, RAPID_HEAL, PROTECT_ITEM, HAWK_EYE, MYSTIC_LORE,
			STEEL_SKIN, ULTIMATE_STRENGTH, INCREDIBLE_REFLEXES, PROTECT_FROM_MAGIC, PROTECT_FROM_MISSILES,
			PROTECT_FROM_MELEE, EAGLE_EYE, MYSTIC_MIGHT, RETRIBUTION, REDEMPTION, SMITE, PRESERVE, CHIVALRY, PIETY,
			RIGOUR, AUGURY;

	private static final int HIGHLIGHT_COLOR = Calculations.packRGB(202, 178, 113);
	private static final int SLOT_WIDTH = 33;
	private static final int SLOT_HEIGHT = 33;

	private final RT3Library library;
	private final OCR indicatorReader;

	protected Prayer(final RT3Library library) {
		this.library = library;

		THICK_SKIN = new Power("Thick skin", library);
		BURST_OF_STRENGTH = new Power("Burst of strength", library);
		CLARITY_OF_THOUGHT = new Power("Clarity of thought", library);
		SHARP_EYE = new Power("Sharp eye", library);
		MYSTIC_WILL = new Power("Mystic will", library);
		ROCK_SKIN = new Power("Rock skin", library);
		SUPER_HUMAN_STRENGTH = new Power("Super human strength", library);
		IMPROVED_REFLEXES = new Power("Improved reflexes", library);
		RAPID_RESTORE = new Power("Rapid restore", library);
		RAPID_HEAL = new Power("Rapid heal", library);
		PROTECT_ITEM = new Power("Protect item", library);
		HAWK_EYE = new Power("Hawk eye", library);
		MYSTIC_LORE = new Power("Mystic lore", library);
		STEEL_SKIN = new Power("Steel skin", library);
		ULTIMATE_STRENGTH = new Power("Ultimate strength", library);
		INCREDIBLE_REFLEXES = new Power("Incredible reflexes", library);
		PROTECT_FROM_MAGIC = new Power("Protect from magic", library);
		PROTECT_FROM_MISSILES = new Power("Protect from missiles", library);
		PROTECT_FROM_MELEE = new Power("Protect from melee", library);
		EAGLE_EYE = new Power("Eagle eye", library);
		MYSTIC_MIGHT = new Power("Mystic might", library);
		RETRIBUTION = new Power("Retribution", library);
		REDEMPTION = new Power("Redemption", library);
		SMITE = new Power("Smite", library);
		PRESERVE = new Power("Preserve", library);
		CHIVALRY = new Power("Chivalry", library);
		PIETY = new Power("Piety", library);
		RIGOUR = new Power("Rigour", library);
		AUGURY = new Power("Augury", library);

		final RGBMultiFilter indicatorFilter = new RGBMultiFilter(Calculations.packRGB(104, 90, 75), Calculations.packRGB(0, 0, 0));
		indicatorFilter.setNegativeMode(true);

		this.indicatorReader = new OCR(library.screen, library.fontStore.get("rt3/resources/numbers.font"), indicatorFilter, 100);
		this.calibrate();
	}

	/**
	 * Calibrates the bounding boxes of the {@code Prayer.Power}.
	 */
	public final void calibrate() {
		if (!library.game.isResizableMode()) {
			int row = -1;
			int numSlot = 0;
			final Power[] powers = new Power[] {THICK_SKIN, BURST_OF_STRENGTH, CLARITY_OF_THOUGHT, SHARP_EYE,
					MYSTIC_WILL, ROCK_SKIN, SUPER_HUMAN_STRENGTH, IMPROVED_REFLEXES, RAPID_RESTORE, RAPID_HEAL,
					PROTECT_ITEM, HAWK_EYE, MYSTIC_LORE, STEEL_SKIN, ULTIMATE_STRENGTH, INCREDIBLE_REFLEXES,
					PROTECT_FROM_MAGIC, PROTECT_FROM_MISSILES, PROTECT_FROM_MELEE, EAGLE_EYE, MYSTIC_MIGHT, RETRIBUTION,
					REDEMPTION, SMITE, PRESERVE, CHIVALRY, PIETY, RIGOUR, AUGURY};
			for (final Power power : powers) {
				if (numSlot % 5 == 0) {
					row += 1;
					numSlot = 0;
				}
				power.setLocation(551 + (numSlot * SLOT_WIDTH) + (numSlot * 4), 214 + (row * SLOT_HEIGHT) + (row * 4));
				numSlot += 1;
			}
		}
	}

	/**
	 * Check if quick prayer is enabled.
	 *
	 * @return <b>true</b> if quick prayer is enabled.
	 */
	public final boolean isQuickPrayerEnabled() {
		return library.screen.getBitmap().getRGB(557, 102) != Calculations.packRGB(218, 238, 222);
	}

	/**
	 * Enable or disable quick prayer.
	 *
	 * @param enableQuickPrayer enable quick prayer
	 */
	public final boolean setQuickPrayerEnabled(final boolean enableQuickPrayer) {
		return isQuickPrayerEnabled() == enableQuickPrayer ||
				library.mouse.click(new Rectangle(520, 90, 48, 25), MouseButton.LEFT);
	}

	/**
	 * Get all the selected powers.
	 * <b>note:</b> Opens the Prayer tab.
	 *
	 * @return a list of the current activated {@link Prayer.Power} powers.
	 */
	public final List<Power> getActivated() {
		if (library.tabs.getSelectedTab() != Tabs.PRAYER) {
			Tabs.PRAYER.click(MouseButton.LEFT);
		}

		final List<Power> activatedPowers = new ArrayList<>();
		final Power[] powers = new Power[] {THICK_SKIN, BURST_OF_STRENGTH, CLARITY_OF_THOUGHT, SHARP_EYE, MYSTIC_WILL,
				ROCK_SKIN, SUPER_HUMAN_STRENGTH, IMPROVED_REFLEXES, RAPID_RESTORE, RAPID_HEAL, PROTECT_ITEM, HAWK_EYE,
				MYSTIC_LORE, STEEL_SKIN, ULTIMATE_STRENGTH, INCREDIBLE_REFLEXES, PROTECT_FROM_MAGIC,
				PROTECT_FROM_MISSILES, PROTECT_FROM_MELEE, EAGLE_EYE, MYSTIC_MIGHT, RETRIBUTION, REDEMPTION, SMITE,
				PRESERVE, CHIVALRY, PIETY, RIGOUR, AUGURY};
		
		for (final Power power : powers) {
			if (power.isActive()) {
				activatedPowers.add(power);
			}
		}
		return activatedPowers;
	}
	
	public final List<Prayer.Power> getPowers() {
		return Arrays.asList(THICK_SKIN, BURST_OF_STRENGTH, CLARITY_OF_THOUGHT, SHARP_EYE, MYSTIC_WILL, ROCK_SKIN,
				SUPER_HUMAN_STRENGTH, IMPROVED_REFLEXES, RAPID_RESTORE, RAPID_HEAL, PROTECT_ITEM, HAWK_EYE, MYSTIC_LORE,
				STEEL_SKIN, ULTIMATE_STRENGTH, INCREDIBLE_REFLEXES, PROTECT_FROM_MAGIC, PROTECT_FROM_MISSILES,
				PROTECT_FROM_MELEE, EAGLE_EYE, MYSTIC_MIGHT, RETRIBUTION, REDEMPTION, SMITE, PRESERVE, CHIVALRY, PIETY,
				RIGOUR, AUGURY);
	}

	/**
	 * Get the current prayer points from the minimap indicator.
	 *
	 * @return the current prayer points or -1 if not found.
	 */
	public final int getCurrentPoints() {
		final String text = indicatorReader.readTextBlock(new Rectangle(521, 99, 21, 13)).getText();
		return text.matches("[0-9]+") ? Integer.parseInt(text) : -1;
	}

	public class Power extends Widget {

		private final String name;
		
		public Power(final String name, final RT3Library library) {
			super(0, 0, SLOT_WIDTH, SLOT_HEIGHT, library);
			this.name = name;
		}
		
		public final String getName() {
			return name;
		}

		private void setLocation(final int x, final int y) {
			this.x = x;
			this.y = y;
		}

		/**
		 * Click on a given {@code slot} selecting a specific {@code action}.
		 *
		 * @param action action to select
		 */
		@Override
		public boolean click(final String action) {
			if (library.tabs.getSelectedTab() != Tabs.PRAYER) {
				Tabs.PRAYER.click(MouseButton.LEFT);
			}
			return super.click(action);
		}

		/**
		 * Click on a given {@code slot} with a specific {@code mouseButton}.
		 *
		 * @param mouseButton {@link MouseButton} mouseButton
		 * @return <b>true</b> when the action is completed.
		 */
		@Override
		public boolean click(final MouseButton mouseButton) {
			if (library.tabs.getSelectedTab() != Tabs.PRAYER) {
				Tabs.PRAYER.click(MouseButton.LEFT);
			}
			return super.click(mouseButton);
		}

		/**
		 * Check if a {@code power} is active.
		 *
		 * @return <b>true</b> if the {@code power} is selected.
		 */
		public final boolean isActive() {
			return getBitmap().getRGB((SLOT_WIDTH / 2), 1) == HIGHLIGHT_COLOR;
		}

	}
}
