package rt3;

import com.zentek.colorbot.client.api.image.Bitmap;
import com.zentek.colorbot.client.api.image.filter.RGBMultiFilter;
import com.zentek.colorbot.client.api.image.ocr.OCR;
import com.zentek.colorbot.client.api.input.MouseButton;
import com.zentek.colorbot.client.api.util.Calculations;

import java.util.ArrayList;
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
 * <br> File: Movement.java
 * <br> Purpose: Handles movement of the player.
 * <br>
 * <br>Copyright (c) 2016 - 2017 Colorbot (<a href="https://www.colorbot.org">https://www.colorbot.org</a>) and contributors.
 * All rights reserved.
 * <br>
 *
 * @author <b>Colorbot</b>
 */
public final class Movement {

	public static final Point MINIMAP_CENTER = new Point(642, 84);
	public static final Bitmap DESTINATION_FLAG = new Bitmap("iVBORw0KGgoAAAANSUhEUgAAAAcAAAAPCAYAAAAoAdW+AAAAMUlEQV" +
			"R42mP4//8/A1bMwIBbArskVAJTEkkCVRJNAiGJRQIiiUMCt2uB+FgYy3CSBAB7LhcBy9D1WQAAAABJRU5ErkJggg==");

	private final RT3Library library;
	private final OCR indicatorReader;

	protected Movement(final RT3Library library) {
		this.library = library;

		final RGBMultiFilter indicatorFilter = new RGBMultiFilter(10D, Calculations.packRGB(104, 90, 75),
				Calculations.packRGB(0, 0, 0));
		indicatorFilter.setNegativeMode(true);

		this.indicatorReader = new OCR(library.screen, library.fontStore.get("rt3/resources/numbers.font"),
				indicatorFilter, 100);
	}

	/**
	 * Get the tiles relative to the player on the minimap with a specific {@code RGBColor}.
	 * @param RGBColor tile color
	 * @return a list of tiles relative to the player position.
	 */
	public final List<Tile> getTiles(final int RGBColor) {
		return getTiles(0D, RGBColor);
	}

	/**
	 * Get the tiles relative to the player on the minimap with a specific {@code RGBColor} and maximum {@code tolerance}.
	 * @param tolerance maximum tolerance
	 * @param RGBColor tile color
	 * @return a list of tiles relative to the player position.
	 */
	public final List<Tile> getTiles(final double tolerance, final int RGBColor) {
		final List<Tile> tiles = new ArrayList<>();
		final List<Point> points = getMinimap().findRGB(tolerance, new int[] {RGBColor});

		for(final Point point : points) {
			tiles.add(new Tile(Math.round(Math.abs(point.x - MINIMAP_CENTER.x)) / 4,
					Math.round(Math.abs(point.y - MINIMAP_CENTER.y)) / 4, 0));
		}
		return tiles;
	}

	/**
	 * Get the current minimap in a bitmap image.
	 *
	 * @return a circular {@link Bitmap} containing the minimap pixels
	 */
	public final Bitmap getMinimap() {
		return library.screen.getBitmap().getSubBitmap(MINIMAP_CENTER, 144D);
	}

	/**
	 * Click on a tile on the minimap relative from the player position (center minimap).
	 *
	 * @param angle    angle between player and tile
	 * @param distance distance from center in pixels, max 72 (minimap radius)
	 */
	public final boolean clickTile(final double angle, double distance) {
		if(distance > 72) {
			distance = 72;
		}
		return library.mouse.click(Calculations.createPoint(MINIMAP_CENTER, angle, distance), MouseButton.LEFT);
	}

	/**
	 * Click on a tile on the minimap relative from the player position (center minimap).
	 *
	 * @param tile tile relative to player position
	 */
	public final boolean clickTile(final Tile tile) {
		return library.mouse.click(tile.getX() * 4 + MINIMAP_CENTER.x, tile.getY() * 4 + MINIMAP_CENTER.y,
				MouseButton.LEFT);
	}

	/**
	 * Get the destination tile relative from the player position (center minimap).
	 * @see Player#isMoving()
	 * @return the destination tile or null if no destination.
	 */
	public final Tile getDestination() {
		final Bitmap minimap = getMinimap();
		final List<Rectangle> flagPosition = minimap.findBitmap(DESTINATION_FLAG, 0D, 100D, false);
		if(flagPosition.size() == 0) {
			return null;
		}

		final Rectangle rectangle = flagPosition.get(0);
		rectangle.translate(minimap.getX(), minimap.getY());

		final Point destination = new Point(rectangle.x + rectangle.width, rectangle.y + rectangle.height);
		final Point relative = new Point(destination.x - MINIMAP_CENTER.x, destination.y - MINIMAP_CENTER.y);
		return new Tile(Math.round(relative.x / 4), Math.round(relative.y / 4), 0);
	}

	/**
	 * Set run enabled.
	 *
	 * @param enableRun enable run
	 * @return <b>true</b> when the action is completed.
	 */
	public final boolean setRunEnabled(final boolean enableRun) {
		return isRunEnabled() == enableRun || library.mouse.click(new Rectangle(544, 126, 48, 25), MouseButton.LEFT);
	}

	/**
	 * Check if run is enabled.
	 *
	 * @return <b>true</b> if run is enabled.
	 */
	public final boolean isRunEnabled() {
		return !library.game.isResizableMode() &&
				library.screen.getBitmap().getRGB(583, 141) == Calculations.packRGB(236, 218, 103);
	}

	/**
	 * Get the current run energy from the minimap indicator.
	 *
	 * @return the player's current run energy or -1 if not found.
	 */
	public final int getRunEnergy() {
		final String text = indicatorReader.readTextBlock(new Rectangle(545, 135, 21, 13)).getText();
		return text.matches("[0-9]+") ? Integer.parseInt(text) : -1;
	}
}
