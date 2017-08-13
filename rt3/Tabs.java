package rt3;

import com.zentek.colorbot.client.api.input.MouseButton;
import com.zentek.colorbot.client.api.util.Calculations;

import java.awt.*;
import java.util.List;
import java.util.Arrays;

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
 * <br> File: Tabs.java
 * <br> Purpose: Handles the in-game tabs.
 * <br>
 * <br>Copyright (c) 2016 - 2017 Colorbot (<a href="https://www.colorbot.org">https://www.colorbot.org</a>) and contributors.
 * All rights reserved.
 * <br>
 *
 * @author <b>Colorbot</b>
 */
public final class Tabs {

	public static Tab ATTACK, STATS, QUEST, INVENTORY, EQUIPMENT, PRAYER, MAGIC, CLAN_CHAT, FRIENDS, IGNORE, LOGOUT,
			SETTINGS, EMOTIONS, MUSIC_PLAYER, NULL;

	private static final int TAB_WIDTH = 33;
	private static final int TAB_HEIGHT = 33;

	private final RT3Library library;

	protected Tabs(final RT3Library library) {
		this.library = library;

		ATTACK = new Tab("Attack", library);
		STATS = new Tab("Stats", library);
		QUEST = new Tab("Quest", library);
		INVENTORY = new Tab("Inventory", library);
		EQUIPMENT = new Tab("Equipment", library);
		PRAYER = new Tab("Prayer", library);
		MAGIC = new Tab("Magic", library);
		CLAN_CHAT = new Tab("Clan chat", library);
		FRIENDS = new Tab("Friend list", library);
		IGNORE = new Tab("Ignore list", library);
		LOGOUT = new Tab("Logout", library);
		SETTINGS = new Tab("Settings", library);
		EMOTIONS = new Tab("Emotions", library);
		MUSIC_PLAYER = new Tab("Music player", library);
		NULL = new Tab("null", library);

		this.calibrate();
	}

	/**
	 * Calculates the bounding rectangles of all {@link Tabs.Tab} tabs.
	 */
	public final void calibrate() {
		final boolean resizableMode = library.game.isResizableMode();
		final Dimension dimension = library.screen.getSize();
		final int width = dimension.width;
		int height = dimension.height;

		if (resizableMode && width >= 950) {
			final Tab[] tabs = new Tab[] {ATTACK, STATS, QUEST, INVENTORY, EQUIPMENT, PRAYER, MAGIC,
					CLAN_CHAT, FRIENDS, IGNORE, LOGOUT, SETTINGS, EMOTIONS, MUSIC_PLAYER};
			for (int i = 0; i < tabs.length; i++) {
				tabs[i].setLocation((width - ((tabs.length - i) * TAB_WIDTH)), (height - TAB_HEIGHT));
			}
			return;
		}

		final Tab[] firstRow = new Tab[]{ATTACK, STATS, QUEST, INVENTORY, EQUIPMENT, PRAYER, MAGIC};
		final int rowHeight = resizableMode ? height - (TAB_HEIGHT * 2) : 169;

		for (int i = 0; i < firstRow.length; i++) {
			firstRow[i].setLocation((width - 7) - ((firstRow.length - i) * TAB_WIDTH), rowHeight);
		}

		final Tab[] secondRow;
		if (resizableMode) {
			secondRow = new Tab[]{null, FRIENDS, IGNORE, CLAN_CHAT, SETTINGS, EMOTIONS, MUSIC_PLAYER};
		} else {
			secondRow = new Tab[]{CLAN_CHAT, FRIENDS, IGNORE, LOGOUT, SETTINGS, EMOTIONS, MUSIC_PLAYER};
		}

		for (int i = resizableMode ? 1 : 0; i < secondRow.length; i++) {
			secondRow[i].setLocation(((width - 7) - ((firstRow.length - i) * TAB_WIDTH)), (height - TAB_HEIGHT) - 3);
		}
	}

	/**
	 * Select a {@code tab}.
	 * @param tab tab to open
	 */
	public final boolean select(final Tab tab) {
		return getSelectedTab() == tab || tab.click(MouseButton.LEFT);
	}

	/**
	 * Get the selected tab.
	 *
	 * @return the selected {@link Tabs.Tab} or null if not found
	 */
	public final Tab getSelectedTab() {
		final Tab[] tabs = new Tab[] {ATTACK, STATS, QUEST, INVENTORY, EQUIPMENT, PRAYER, MAGIC,
				CLAN_CHAT, FRIENDS, IGNORE, LOGOUT, SETTINGS, EMOTIONS, MUSIC_PLAYER};

		for (final Tab tab : tabs) {
			if(tab.isSelected()) {
				return tab;
			}
		}
		return NULL;
	}

	/**
	 * Get a collection of all available tabs.
	 * @return a collection of all available {@link Tab}s.
	 */
	public final List<Tab> getTabs() {
		return Arrays.asList(ATTACK, STATS, QUEST, INVENTORY, EQUIPMENT, PRAYER, MAGIC,
				CLAN_CHAT, FRIENDS, IGNORE, LOGOUT, SETTINGS, EMOTIONS, MUSIC_PLAYER);
	}

	public class Tab extends Widget {

		private final String name;
		
		public Tab(final String name, final RT3Library library) {
			super(0, 0, TAB_WIDTH, TAB_HEIGHT, library);
			this.name = name;
		}

		private void setLocation(final int x, final int y) {
			this.x = x;
			this.y = y;
		}
		
		public String getName() {
			return name;
		}

		public final boolean isSelected() {
			return Calculations.getRGBDistance(getBitmap().getRGB(13, 2), Calculations.packRGB(100, 34, 25)) < 75;
		}
		
	}
}
