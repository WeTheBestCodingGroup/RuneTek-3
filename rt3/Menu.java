package rt3;

import com.zentek.colorbot.client.api.image.Bitmap;
import com.zentek.colorbot.client.api.image.filter.RGBMultiFilter;
import com.zentek.colorbot.client.api.image.ocr.OCR;
import com.zentek.colorbot.client.api.image.ocr.OCRFont;
import com.zentek.colorbot.client.api.input.MouseButton;
import com.zentek.colorbot.client.api.random.Random;
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
 * <br> File: Menu.java
 * <br> Purpose: Represents the in-game right click Menu.
 * <br>
 * <br>Copyright (c) 2016 - 2017 Colorbot (<a href="https://www.colorbot.org">https://www.colorbot.org</a>) and contributors.
 * All rights reserved.
 * <br>
 *
 * @author <b>Colorbot</b>
 */
public final class Menu extends Widget {

	private static final int[] BACKGROUND_COLORS = new int[]{Calculations.packRGB(93, 84, 71),
			Calculations.packRGB(0, 0, 0)};

	private static final int ENTRY_HEIGHT = 15;

	private final RT3Library library;
	private final OCR menuReader;

	protected Menu(final RT3Library library) {
		super(0, 0, 0, 0, library);
		this.library = library;

		final OCRFont font = library.fontStore.get("rt3/resources/menu.font");
		final RGBMultiFilter menuFilter = new RGBMultiFilter(BACKGROUND_COLORS);
		menuFilter.setNegativeMode(true);

		this.menuReader = new OCR(library.screen, font, menuFilter, 4);
	}

	/**
	 * Check if the menu is opened/visible.
	 *
	 * @return <b>true</b> if the menu is opened.
	 */
	public final boolean isOpen() {
		final Bitmap bitmap = library.screen.getBitmap();
		final Point m = library.mouse.getMouseLocation();
		final Point p = new Point(Math.max(0, m.x - 150), Math.max(0, m.y - 100));
		final Dimension size = new Dimension(Math.max(0, Math.min(bitmap.getWidth() - p.x, 300)),
				Math.max(0, Math.min(bitmap.getHeight() - p.y, 200)));

		final Bitmap area = bitmap.getSubBitmap(new Rectangle(p.x, p.y, size.width, size.height));
		return area.findRGB(BACKGROUND_COLORS[0]).size() >= 120;
	}

	/**
	 * Get all the visible menu actions.
	 * <br><b>note:</b> the menu must be opened.
	 *
	 * @return an array containing the current menu actions.
	 */
	public final List<String> getEntries() {
		final Rectangle bounds = getBounds();
		if (bounds.x == 0 && bounds.y == 0 && bounds.width == 0 && bounds.height == 0) {
			return new ArrayList<>();
		}

		final int items = (bounds.height - 21) / ENTRY_HEIGHT;
		final String[] entries = new String[items < 0 ? 0 : items];
		for (int i = 0; i < items; i++) {
			entries[i] = menuReader.readTextBlock(new Rectangle(bounds.x + 2, bounds.y + 21 + (i * 15),
					bounds.width - 4, ENTRY_HEIGHT)).getText();
		}
		return Arrays.asList(entries);
	}

	/**
	 * Select a given menu {@code action}.
	 * <br><b>note:</b> the menu must be opened.
	 *
	 * @param action menu action
	 * @return <b>true</b> when the action is completed.
	 */
	@Override
	public final boolean click(final String action) {
		return click(indexOf(action));
	}

	/**
	 * Select a menu action by a given {@code index}.
	 * <br><b>note:</b> the menu must be opened.
	 *
	 * @param index index of action
	 * @return <b>true</b> when the action is completed.
	 */
	public final boolean click(final int index) {
		if (index < 0) {
			return false;
		}
		final Rectangle r = getBounds(index);
		return (r.x == 0 && r.y == 0 && r.width == 0 && r.height == 0) || library.mouse.click(r, MouseButton.LEFT);
	}

	/**
	 * Close the menu by moving the mouse outside of the menu bounds.
	 *
	 * @return <b>true</b> when the action is completed.
	 */
	public final boolean close() {
		if (!isOpen()) {
			return true;
		}

		final Dimension screenSize = library.screen.getSize();
		final Point generated = Calculations.createPoint(library.mouse.getMouseLocation(),
				Random.nextInt(0, 360), Random.nextInt(50, 100));
		//TODO: make this accurate and actually compute a point considering menu bounds & screen bounds
		generated.x = Math.max(0, Math.min(screenSize.width, generated.x));
		generated.y = Math.max(0, Math.min(screenSize.width, generated.y));

		return library.mouse.moveTo(generated);
	}

	/**
	 * Get the index of a menu {@code action}.
	 * <br><b>note:</b> the menu must be opened.
	 *
	 * @param action action
	 * @return the index of {@code action}.
	 */
	public final int indexOf(final String action) {
		final List<String> entries = getEntries();
		for (int i = 0; i < entries.size(); i++) {
			if (entries.get(i).contains(action)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Get the bounds of a menu item at a given {@code index}.
	 * <br><b>note:</b> the menu must be opened.
	 *
	 * @param index index of the menu item
	 * @return the bounding rectangle of the menu item at {@code index}.
	 */
	public final Rectangle getBounds(final int index) {
		final Rectangle bounds = getBounds();
		if (bounds.x == 0 && bounds.y == 0 && bounds.width == 0 && bounds.height == 0) {
			return bounds;
		}
		return new Rectangle(bounds.x + 2, bounds.y + 21 + (index * ENTRY_HEIGHT), bounds.width - 4, ENTRY_HEIGHT - 1);
	}

	/**
	 * Get the current bounds of the visible menu.
	 * <br><b>note:</b> the menu must be opened.
	 *
	 * @return the bounding rectangle of the menu as a {@link Rectangle} or (0, 0, 0, 0) if not found.
	 */
	@Override
	public final Rectangle getBounds() {
		if (!isOpen()) {
			return new Rectangle(0, 0, 0, 0);
		}

		final Bitmap bitmap = library.screen.getBitmap();
		final Point m = library.mouse.getMouseLocation();
		final Point p = new Point(Math.max(0, m.x - 150), Math.max(0, m.y - 100));
		final Dimension size = new Dimension(Math.max(0, Math.min(bitmap.getWidth() - p.x, 300)),
				Math.max(0, Math.min(bitmap.getHeight() - p.y, 200)));

		final Bitmap area = bitmap.getSubBitmap(new Rectangle(p.x, p.y, size.width, size.height));
		final List<Point> colors = area.findRGB(BACKGROUND_COLORS[0]);

		final Point[] startPts = new Point[colors.size()];
		for(int i = 0; i < colors.size(); i ++) {
			final Point startPt = colors.get(i);
			startPt.translate(p.x, p.y);
			startPts[i] = startPt;
		}
		return Calculations.get2DBoundingBox(bitmap, 0D, BACKGROUND_COLORS[0], startPts).getBounds();
	}
}
