package rt3;

import com.zentek.colorbot.client.api.image.Bitmap;
import com.zentek.colorbot.client.api.image.filter.Filter;
import com.zentek.colorbot.client.api.image.ocr.OCR;
import com.zentek.colorbot.client.api.input.MouseButton;
import com.zentek.colorbot.client.api.util.Calculations;

import java.util.Arrays;
import java.util.List;
import java.awt.*;

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
 * <br> File: Stats.java
 * <br> Purpose: Handles player stats and levels.
 * <br>
 * <br>Copyright (c) 2016 - 2017 Colorbot (<a href="https://www.colorbot.org">https://www.colorbot.org</a>) and contributors.
 * All rights reserved.
 * <br>
 *
 * @author <b>Colorbot</b>
 */
public final class Stats {

	public static Skill ATTACK, HITPOINTS, MINING, STRENGTH, AGILITY, SMITHING,
		DEFENCE, HERBLORE, FISHING, RANGED, THIEVING, COOKING, PRAYER, CRAFTING, FIREMAKING,
		MAGIC, FLETCHING, WOODCUTTING, RUNECRAFTING, SLAYER, FARMING, CONSTRUCTION, HUNTER;

	private static final int SLOT_WIDTH = 61;
	private static final int SLOT_HEIGHT = 30;
	private static final int BACKGROUND_COLOR = Calculations.packRGB(255, 255, 160);

	private final RT3Library library;
	private final OCR ocr;

	protected Stats(final RT3Library library) {
		this.library = library;

		ATTACK = new Skill("Attack", library);
		HITPOINTS = new Skill("Hitpoints", library);
		MINING = new Skill("Mining", library);
		STRENGTH = new Skill("Strength", library);
		AGILITY = new Skill("Agility", library);
		SMITHING = new Skill("Smithing", library);
		DEFENCE = new Skill("Defence", library);
		HERBLORE = new Skill("Herblore", library);
		FISHING = new Skill("Fishing", library);
		RANGED = new Skill("Ranged", library);
		THIEVING = new Skill("Thieving", library);
		COOKING = new Skill("Cooking", library);
		PRAYER = new Skill("Prayer", library);
		CRAFTING = new Skill("Crafting", library);
		FIREMAKING = new Skill("Firemaking", library);
		MAGIC = new Skill("Magic", library);
		FLETCHING = new Skill("Fletching", library);
		WOODCUTTING = new Skill("Woodcutting", library);
		RUNECRAFTING = new Skill("Runecrafting", library);
		SLAYER = new Skill("Slayer", library);
		FARMING = new Skill("Farming", library);
		CONSTRUCTION = new Skill("Construction", library);
		HUNTER = new Skill("Hunter", library);

		this.ocr = new OCR(library.screen, library.fontStore.get("rt3/resources/numbers.font"),
				new Filter(Calculations.packRGB(255, 255, 0), 0), 20);
		this.calibrate();
	}

	/**
	 * Calculates the bounding rectangles of all {@link Stats.Skill} skills.
	 */
	public final void calibrate() {
		if (!library.game.isResizableMode()) {
			int row = -1;
			int numSlot = 0;
			final Skill[] skills = new Skill[] {ATTACK, HITPOINTS, MINING, STRENGTH, AGILITY, SMITHING,
					DEFENCE, HERBLORE, FISHING, RANGED, THIEVING, COOKING, PRAYER, CRAFTING, FIREMAKING,
					MAGIC, FLETCHING, WOODCUTTING, RUNECRAFTING, SLAYER, FARMING, CONSTRUCTION, HUNTER};
			for (final Skill skill : skills) {
				if (numSlot % 3 == 0) {
					row += 1;
					numSlot = 0;
				}
				skill.setLocation(548 + (numSlot * SLOT_WIDTH) + (numSlot * 2), 206 + (row * SLOT_HEIGHT) + (row));
				numSlot += 1;
			}
		}
	}

	/**
	 * Get the total level of all skills.
	 * <br><b>note:</b> Opens stats tab.
	 *
	 * @return the total level of all {@link Stats.Skill} skills.
	 */
	public final int getTotalLevel() {
		if (library.tabs.getSelectedTab() != Tabs.STATS) {
			Tabs.STATS.click(MouseButton.LEFT);
		}

		final String text = ocr.readTextBlock(new Rectangle(680, 442, 51, 9)).getText();
		return text.matches("[0-9]+") ? Integer.parseInt(text) : -1;
	}

	public final List<Skill> getSkills() {
		return Arrays.asList(ATTACK, HITPOINTS, MINING, STRENGTH, AGILITY, SMITHING,
				DEFENCE, HERBLORE, FISHING, RANGED, THIEVING, COOKING, PRAYER, CRAFTING, FIREMAKING,
				MAGIC, FLETCHING, WOODCUTTING, RUNECRAFTING, SLAYER, FARMING, CONSTRUCTION, HUNTER);
	}

	public class Skill extends Widget {

		private final String name;
		
		public Skill(final String name, final RT3Library library) {
			super(0, 0, SLOT_WIDTH, SLOT_HEIGHT, library);
			this.name = name;
		}

		private void setLocation(final int x, final int y) {
			this.x = x;
			this.y = y;
		}
		
		public String getName() {
			return name;
		}

		/**
		 * Click on a given {@code slot} selecting a specific {@code action}.
		 *
		 * @param action action to select
		 */
		@Override
		public boolean click(final String action) {
			if (library.tabs.getSelectedTab() != Tabs.STATS) {
				Tabs.STATS.click(MouseButton.LEFT);
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
			if (library.tabs.getSelectedTab() != Tabs.STATS) {
				Tabs.STATS.click(MouseButton.LEFT);
			}
			return super.click(mouseButton);
		}

		/**
		 * Get the current level of this {@code skill}.
		 * <br><b>note:</b> Opens stats tab.
		 *
		 * @return the current level of the {@code skill}.
		 */
		public final int getCurrentLevel() {
			if (library.tabs.getSelectedTab() != Tabs.STATS) {
				Tabs.STATS.click(MouseButton.LEFT);
			}

			final String text = ocr.readTextBlock(new Rectangle(x + 33, y + 5, 12, 8)).getText();
			return text.matches("[0-9]+") ? Integer.parseInt(text) : -1;
		}

		/**
		 * Get the real level of a {@code skill}.
		 * <br><b>note:</b> Opens stats tab.
		 *
		 * @return the real level of the {@code skill}.
		 */
		public final int getRealLevel() {
			if (library.tabs.getSelectedTab() != Tabs.STATS) {
				Tabs.STATS.click(MouseButton.LEFT);
			}
			final String text = ocr.readTextBlock(new Rectangle(x + 45, y + 17, 12, 8)).getText();
			return text.matches("[0-9]+") ? Integer.parseInt(text) : -1;
		}

		/**
		 * Get the experience of this {@code skill} by hovering over the {@code skill}.
		 * <br><b>note:</b> Opens stats tab.
		 *
		 * @return the current experience in the {@code skill}.
		 */
		public final int getExperience() {
			if (library.tabs.getSelectedTab() != Tabs.STATS) {
				Tabs.STATS.click(MouseButton.LEFT);
			}

			if(library.mouse.moveTo(getBounds())) {
				final Rectangle experienceBounds = getExperienceBounds();//TODO; load font, get bounds of the line idx
				final String text = ocr.readTextBlock(experienceBounds).getText();
				return text.matches("[0-9]+") ? Integer.parseInt(text) : -1;
			}
			return 0;
		}

		/**
		 * Get the experience until next level of this {@code skill} by hovering over the {@code skill}.
		 * <br><b>note:</b> Opens stats tab.
		 *
		 * @return the remainder experience until next level of the {@code skill}.
		 */
		public final int getRemainder() {
			if (library.tabs.getSelectedTab() != Tabs.STATS) {
				Tabs.STATS.click(MouseButton.LEFT);
			}

			if(library.mouse.moveTo(getBounds())) {
				final Rectangle experienceBounds = getExperienceBounds();//TODO; load font, get bounds of the line idx
				final String text = ocr.readTextBlock(experienceBounds).getText();
				return text.matches("[0-9]+") ? Integer.parseInt(text) : -1;
			}
			return -1;
		}

		private Rectangle getExperienceBounds() {
			final Point mousePos = library.mouse.getMouseLocation();
			final Bitmap bitmap = library.screen.getBitmap();

			final Point pos = new Point(Math.max(0, mousePos.x - 40), Math.max(0, mousePos.y - 40));
			final Dimension dimension = new Dimension(Math.min(bitmap.getWidth() - pos.x, 80),
					Math.min(bitmap.getHeight() - pos.y, 80));

			final Bitmap area = bitmap.getSubBitmap(new Rectangle(pos.x, pos.y, dimension.width, dimension.height));
			final List<Point> startPts = area.findRGB(BACKGROUND_COLOR);

			return Calculations.get2DBoundingBox(bitmap, 0D, BACKGROUND_COLOR, startPts.get(0)).getBounds();
		}
	}
}
