package rt3;

import com.zentek.colorbot.client.api.image.Bitmap;
import com.zentek.colorbot.client.api.input.MouseButton;

import java.awt.*;

/**
 * Copyright (c) 2016 - 2017 Colorbot (<a href="https://colorbot.org">https://colorbot.org</a>) and contributors.
 * <br>
 * <br>Licensed under the Colorbot License, Version 1.0 (the "License");
 * <br>you may not use this file except in compliance with the License.
 * <br>You may obtain a copy of the License at:
 * <br>
 * <br> <a href="https://colorbot.org/license/LICENSE-1.0">https://colorbot.org/license/LICENSE-1.0</a>
 * <br>
 * <br>Unless required by applicable law or agreed to in writing, software
 * <br>distributed under the License is distributed on an "AS IS" BASIS,
 * <br>WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * <br>See the License for the specific language governing permissions and limitations under the License.
 * <br>
 * <br> Package: rt3
 * <br> File: Widget.java
 * <br> Purpose: Represents an in-game 2D widget.
 * <br>
 * <br>Copyright (c) 2016 - 2017 Colorbot (<a href="https://colorbot.org">https://colorbot.org</a>) and contributors.
 * All rights reserved.
 * <br>
 *
 * @author <b>Colorbot</b>
 */
public abstract class Widget {

	public int x, y, width, height;
	protected final RT3Library library;

	public Widget(final Rectangle rectangle, final RT3Library library) {
		this(rectangle.x, rectangle.y, rectangle.width, rectangle.height, library);
	}

	public Widget(final int x, final int y, final int width, final int height, final RT3Library library) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.library = library;
	}

	/**
	 * Get the x-coordinate of this {@code Widget}.
	 *
	 * @return the x-coordinate of this {@code Widget}.
	 */
	public int getX() {
		return x;
	}

	/**
	 * Get the y-coordinate of this {@code Widget}.
	 *
	 * @return the y-coordinate of this {@code Widget}.
	 */
	public int getY() {
		return y;
	}

	/**
	 * Get the width of this {@code Widget}.
	 *
	 * @return the width of this {@code Widget}.
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Get the height of this {@code Widget}.
	 *
	 * @return the height of this {@code Widget}.
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Get the location of this {@code Widget}.
	 *
	 * @return a {@link Point} containing the location of this {@code Widget}.
	 */
	public Point getLocation() {
		return new Point(x, y);
	}

	/**
	 * Get the center point of this {@code Widget}.
	 *
	 * @return a {@link Point} describing the center.
	 */
	public Point getCenterPoint() {
		return new Point(x + (width / 2), y + (height / 2));
	}

	/**
	 * Get the bounds of this {@code Widget}.
	 *
	 * @return a {@link Rectangle} containing the location and bounds of this {@code Widget}.
	 */
	public Rectangle getBounds() {
		return new Rectangle(x, y, width, height);
	}

	/**
	 * Get a {@link Bitmap} of this {@code Widget};
	 * @return a {@link Bitmap} of this {@code Widget}.
	 */
	public Bitmap getBitmap() {
		return library.screen.getBitmap(getBounds());
	}

	/**
	 * Move the mouse over the {@code Widget}.
	 */
	public boolean hover() {
		final Rectangle bounds = getBounds();
		return bounds.contains(library.mouse.getMouseLocation()) || library.mouse.moveTo(bounds);
	}

	/**
	 * Click on the {@code Widget} selecting a specific {@code action} by left clicking or opening the menu.
	 *
	 * @param action action to select
	 */
	public boolean click(final String action) {
		if(hover()) {
			if (library.game.getTopText().contains(action)) {
				return library.mouse.click(MouseButton.LEFT);
			}

			if (!library.menu.isOpen()) {
				library.mouse.click(MouseButton.RIGHT);
			}

			return library.menu.click(action);
		}
		return false;
	}

	/**
	 * Click on this {@code Widget} with a specific {@code mouseButton}.
	 *
	 * @param mouseButton {@link MouseButton} mouseButton
	 * @return <b>true</b> when the action is completed.
	 */
	public boolean click(final MouseButton mouseButton) {
		return library.mouse.click(getBounds(), mouseButton);
	}
}
