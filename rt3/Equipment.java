package rt3;

import com.zentek.colorbot.client.api.input.MouseButton;

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
 * <br> File: Equipment.java
 * <br> Purpose: Handles in-game equipment.
 * <br>
 * <br>Copyright (c) 2016 - 2017 Colorbot (<a href="https://www.colorbot.org">https://www.colorbot.org</a>) and contributors.
 * All rights reserved.
 * <br>
 *
 * @author <b>Colorbot</b>
 */
public final class Equipment {

	private final RT3Library library;

	private static Slot HEAD, NECK, CHEST, WEAPON, SHIELD, QUIVER, LEGS, BOOTS, RING, CAPE, GLOVES;
	private static final int SLOT_WIDTH = 35;
	private static final int SLOT_HEIGHT = 35;

	protected Equipment(final RT3Library library) {
		this.library = library;

		HEAD = new Slot("Head", library);
		NECK = new Slot("Neck", library);
		CHEST = new Slot("Chest", library);
		WEAPON = new Slot("Weapon", library);
		SHIELD = new Slot("Shield", library);
		QUIVER = new Slot("Quiver", library);
		LEGS = new Slot("Legs", library);
		BOOTS = new Slot("Boots", library);
		RING = new Slot("Ring", library);
		CAPE = new Slot("Cape", library);
		GLOVES = new Slot("Gloves", library);

		this.calibrate();
	}

	/**
	 * Calculates the bounding rectangles of all {@link Equipment.Slot} slots.
	 */
	public final void calibrate() {
		if (!library.game.isResizableMode()) {
			HEAD.setLocation(624, 209);
			NECK.setLocation(624, 248);
			CHEST.setLocation(624, 287);
			LEGS.setLocation(624, 327);
			BOOTS.setLocation(624, 367);
			WEAPON.setLocation(568, 287);
			GLOVES.setLocation(568, 367);
			SHIELD.setLocation(680, 287);
			QUIVER.setLocation(665, 248);
			RING.setLocation(680, 367);
			CAPE.setLocation(583, 248);
		}
	}

	public List<Equipment.Slot> getSlots() {
		return Arrays.asList(HEAD, NECK, CHEST, WEAPON, SHIELD, QUIVER, LEGS, BOOTS, RING, CAPE, GLOVES);
	}

	public class Slot extends Widget {

		private final String name;

		public Slot(final String name, final RT3Library library) {
			super(0, 0, SLOT_WIDTH, SLOT_HEIGHT, library);
			this.name = name;
		}

		private void setLocation(final int x, final int y) {
			this.x = x;
			this.y = y;
		}

		public final String getName() {
			return name;
		}

		/**
		 * Click on a given {@code slot} selecting a specific {@code action}.
		 *
		 * @param action action to select
		 */
		@Override
		public boolean click(final String action) {
			if (library.tabs.getSelectedTab() != Tabs.EQUIPMENT) {
				Tabs.EQUIPMENT.click(MouseButton.LEFT);
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
			if (library.tabs.getSelectedTab() != Tabs.EQUIPMENT) {
				Tabs.EQUIPMENT.click(MouseButton.LEFT);
			}
			return super.click(mouseButton);
		}

		public final boolean isEmpty() {
			return getBitmap().findRGB(46, 43, 35).size() > 80;
		}
	}
}
