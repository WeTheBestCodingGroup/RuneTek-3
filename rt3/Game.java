package rt3;

import com.zentek.colorbot.client.api.image.Bitmap;
import com.zentek.colorbot.client.api.image.filter.Filter;
import com.zentek.colorbot.client.api.image.ocr.OCR;
import com.zentek.colorbot.client.api.image.ocr.OCRFont;
import com.zentek.colorbot.client.api.input.MouseButton;
import com.zentek.colorbot.client.api.util.Calculations;

import java.awt.*;
import java.awt.image.BufferedImage;

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
 * <br> File: Game.java
 * <br> Purpose: Handles the game status and game actions.
 * <br>
 * <br>Copyright (c) 2016 - 2017 Colorbot (<a href="https://www.colorbot.org">https://www.colorbot.org</a>) and contributors.
 * All rights reserved.
 * <br>
 *
 * @author <b>Colorbot</b>
 */
public final class Game {

	public enum State {
		LOGGED_IN, WELCOME_SCREEN, LOGGED_OUT, NULL
	}

	public static final Dimension FIXED_SIZE = new Dimension(765, 503);

	private final RT3Library library;
	private final OCR topTextReader;

	protected Game(final RT3Library library) {
		this.library = library;
		final OCRFont font = library.fontStore.get("rt3/resources/menu.font");
		this.topTextReader = new OCR(library.screen, font, new TopTextFilter(), 4);
	}

	/**
	 * Check if the client is in resizable mode.
	 *
	 * @return <b>true</b> if the client is in resizable mode.
	 */
	public final boolean isResizableMode() {
		final Rectangle screenRect = library.screen.getScreenRect();
		final Dimension windowSize = library.window.getSize();
		final Dimension size = screenRect == null ? new Dimension(windowSize.width, windowSize.height) :
				new Dimension(screenRect.width, screenRect.height);
		return size.width > FIXED_SIZE.width || size.height > FIXED_SIZE.height;
	}

	/**
	 * Get the current {@link Game.State}.
	 *
	 * @return the current {@link Game.State} or {@link Game.State#NULL} if not detected.
	 */
	public final State getGameState() {
		final int color = library.screen.getBitmap().getRGB(670, 2);
		final int loggedIn = Calculations.packRGB(58, 51, 28);
		final int lobby = Calculations.packRGB(0, 0, 0);
		final int loggedOut = Calculations.packRGB(50, 47, 42);
		return !library.game.isResizableMode() ? color == loggedOut ? State.LOGGED_OUT :
				color == lobby ? State.WELCOME_SCREEN : color == loggedIn ? State.LOGGED_IN : State.NULL : State.NULL;
	}

	/**
	 * Get the current top text.
	 * <br><b>note:</b> this can be bugged depending what kind of background is behind the text.
	 *
	 * @return the current visible top text.
	 */
	public final String getTopText() {
		final String text = topTextReader.readTextBlock(new Rectangle(8, 6, 380, 17)).getText();
		if (text.length() < 4) {
			return "";
		}
		return text.contains("/") ? text.substring(0, text.indexOf("/")).trim() : text;
	}

	/**
	 * Check if the player is logged in.
	 *
	 * @return <b>true</b> if the player state equals {@link Game.State#LOGGED_IN}.
	 */
	public final boolean isLoggedIn() {
		return getGameState() == State.LOGGED_IN;
	}

	/**
	 * Check if the player is at the welcome screen.
	 *
	 * @return <b>true</b> if the player state equals {@link Game.State#WELCOME_SCREEN}.
	 */
	public final boolean isAtWelcomeScreen() {
		return getGameState() == State.WELCOME_SCREEN;
	}

	/**
	 * Check if the player is logged out.
	 *
	 * @return <b>true</b> if the player state equals {@link Game.State#LOGGED_OUT}.
	 */
	public final boolean isLoggedOut() {
		return getGameState() == State.LOGGED_OUT;
	}

	/**
	 * Logs the player out.
	 *
	 * @return <b>true</b> if the player is logged out.
	 */
	public final boolean logout() {
		if (!isLoggedIn()) {
			return true;
		}

		if (library.tabs.getSelectedTab() != Tabs.LOGOUT) {
			Tabs.LOGOUT.click(MouseButton.LEFT);
		}
		return library.mouse.click(new Rectangle(570, 401, 144, 35), MouseButton.LEFT);
	}

	/**
	 * Switch world to a given {@code world}.
	 *
	 * @param world  world
	 * @return <b>true</b> if the player switched worlds
	 */
	public final boolean switchWorld(final int world) {
		if (library.tabs.getSelectedTab() != Tabs.LOGOUT) {
			Tabs.LOGOUT.click(MouseButton.LEFT);
		}

		library.mouse.click(new Rectangle(570, 343, 144, 35), MouseButton.LEFT);
		//TODO: continue
		return true;
	}

	private class TopTextFilter extends Filter {

		private final float[][] colorObj;
		private final float[][] colorNpc;
		private final float[][] colorItem;
		private final float[][] colorText;

		private final int foreground;
		private final int background;

		public TopTextFilter() {
			this.colorObj = new float[][]{{10f, 220f, 220f}, {0f, 200f, 200f}};
			this.colorNpc = new float[][]{{215f, 215f, 0f}, {240f, 240f, 20f}};
			this.colorItem = new float[][]{{235f, 135f, 65f}, {200f, 115f, 50f}, {215, 128, 26}};
			this.colorText = new float[][]{{215f, 215f, 215f}, {225f, 255f, 255f}, {200f, 200f, 200f}, {230f, 230f, 230f}};
			this.foreground = getForeground();
			this.background = getBackground();
		}

		@Override
		public Bitmap filter(Bitmap bitmap) {
			for (int x = 0; x < bitmap.getWidth(); x++) {
				for (int y = 0; y < bitmap.getHeight(); y++) {
					final float[] color = bitmap.getSamples(x, y);
					bitmap.setRGB(x, y, isMatch(color, colorText) || isMatch(color, colorObj) ||
							isMatch(color, colorNpc) || isMatch(color, colorItem) ? foreground : background);
				}
			}
			return bitmap;
		}

		private boolean isMatch(final float[] color, final float[][] idxColors) {
			for (float[] idxColor : idxColors) {
				if (getColorDistance(color, idxColor) <= 25) {
					return true;
				}
			}
			return false;
		}

		@Override
		public BufferedImage filter(BufferedImage image) {
			return new BufferedImage(0, 0, 0);
		}
	}
}
