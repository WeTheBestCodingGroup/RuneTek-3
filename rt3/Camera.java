package rt3;

import com.zentek.colorbot.client.api.image.Bitmap;
import com.zentek.colorbot.client.api.random.Random;
import com.zentek.colorbot.client.api.util.Calculations;

import java.awt.*;
import java.awt.event.KeyEvent;
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
 * <br> File: Camera.java
 * <br> Purpose: Represents the in-game camera.
 * <br>
 * <br>Copyright (c) 2016 - 2017 Colorbot (<a href="https://www.colorbot.org">https://www.colorbot.org</a>) and contributors.
 * All rights reserved.
 * <br>
 *
 * @author <b>Colorbot</b>
 */
public final class Camera {

	private static final Point CENTER_POINT = new Point(561, 20);
	private static final Point CENTER_POINT_RELATIVE = new Point(17, 17);

	private final RT3Library library;
	private double pitch;

	protected Camera(final RT3Library library) {
		this.library = library;
		this.pitch = 0;
	}

	/**
	 * Set the camera yaw rotation to a specific {@code angle}.
	 *
	 * @param angle angle in degrees
	 * @return <b>true</b> when the action is completed.
	 */
	public final boolean setYaw(final double angle) {
		final double currAngle = getYaw();
		final double rotation = angle - currAngle;
		final double r = rotation > 0 ? rotation - Random.nextInt(0, 30) : rotation + Random.nextInt(0, 30);

		final int key;
		if (r > 0) {
			key = KeyEvent.VK_LEFT;
		} else if (r < 0) {
			key = KeyEvent.VK_RIGHT;
		} else {
			key = Random.nextBoolean() ? KeyEvent.VK_LEFT : KeyEvent.VK_RIGHT;
		}

		while(Math.abs(angle - getYaw()) > 5) {
			if(!library.keyboard.isKeyDown(key)) {
				library.keyboard.pressKey(key);
			}
		}
		return library.keyboard.releaseKey(key);
	}

	/**
	 * Get the current camera yaw rotation.
	 *
	 * @return current yaw of the camera in degrees.
	 */
	public final double getYaw() {
		final Bitmap compass = library.screen.getBitmap().getSubBitmap(CENTER_POINT, 34D);
		final List<Point> pixels = compass.findRGB(49, 41, 29);

		int x = 0;
		int y = 0;
		for (final Point p : pixels) {
			x += p.x;
			y += p.y;
		}

		final int len = pixels.size();
		return len == 0 ? -1 : (int) Math.round(Calculations.getAngle(CENTER_POINT_RELATIVE, new Point(x / len, y / len)));
	}

	/**
	 * Moves the camera all the way up or all the way down. Used to calibrate the pitch level and to determine
	 * how long to press the key in {@link #setPitch(double)}.
	 *
	 * @param up up
	 * @return <b>true</b> when the action is completed.
	 */
	public final boolean setPitch(final boolean up) {
		setPitch(up ? Random.nextInt(100, 130) : Random.nextInt(-30, 0));
		this.pitch = up ? 100 : 0;
		return true;
	}

	/**
	 * Set the current pitch level.
	 * <br><b>note:</b> this method is not always accurate
	 *
	 * @param pitch pitch level (0-100)
	 * @return <b>true</b> when the action is completed.
	 * @see #setPitch(boolean)
	 */
	public final boolean setPitch(final double pitch) {
		final int key = pitch > this.pitch ? KeyEvent.VK_UP : KeyEvent.VK_DOWN;

		library.keyboard.pressKey(key);
		library.sleep(Random.nextInt(3000, 5000)); //TODO: Make this more accurate
		return library.keyboard.releaseKey(key);
	}

	/**
	 * Get the current pitch level of the camera.
	 *
	 * @return current pitch in degrees.
	 */
	public final double getPitch() {
		return pitch;
	}
}
