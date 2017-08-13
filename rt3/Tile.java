package rt3;

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
 * <br> File: Tile.java
 * <br> Purpose: Represents an in-game tile.
 * <br>
 * <br>Copyright (c) 2016 - 2017 Colorbot (<a href="https://colorbot.org">https://colorbot.org</a>) and contributors.
 * All rights reserved.
 * <br>
 *
 * @author <b>Colorbot</b>
 */
public final class Tile {

	private final int x, y, plane;

	/**
	 * Construct a new in-game tile.
	 * @param x x value of the tile coordinate
	 * @param y y value of the tile coordinate
	 * @param plane plane or z value of the tile coordinate
	 */
	public Tile(final int x, final int y, final int plane) {
		this.x = x;
		this.y = y;
		this.plane = plane;
	}

	/**
	 * Get the x value of the tile coordinate.
	 * @return the x value of the tile coordinate.
	 */
	public final int getX() {
		return x;
	}

	/**
	 * Get the y value of the tile coordinate.
	 * @return the y value of the tile coordinate.
	 */
	public final int getY() {
		return y;
	}

	/**
	 * Get the plane or Z value of the tile coordinate.
	 * @return the plane or Z value of the tile coordinate.
	 */
	public final int getPlane() {
		return plane;
	}

	/**
	 * Not working.
	 * @return Not working.
	 */
	public final Polygon toScreen() {
		return new Polygon();
	}

	/**
	 * Convert the tile to a point on the minimap.
	 * @return the tile point on the minimap.
	 */
	public final Point toMinimap() {
		return new Point(x * 4 + Movement.MINIMAP_CENTER.x, y * 4 + Movement.MINIMAP_CENTER.y);
	}

	/**
	 * Draw the tile on the minimap.
 	 * @param g graphics context
	 */
	public final void draw(final Graphics g) {
		final Point point =  toMinimap();
		if(g.getColor() != Color.GREEN) {
			g.setColor(Color.GREEN);
		}
		g.drawRect(point.x - 2, point.y - 2, 4, 4);
	}

}
