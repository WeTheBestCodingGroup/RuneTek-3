package rt3;

import com.zentek.colorbot.client.api.image.Bitmap;
import com.zentek.colorbot.client.api.image.filter.Filter;
import com.zentek.colorbot.client.api.image.filter.RGBMultiFilter;
import com.zentek.colorbot.client.api.image.ocr.OCR;
import com.zentek.colorbot.client.api.util.Calculations;

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
 * <br> File: Player.java
 * <br> Purpose: Represents the in-game player.
 * <br>
 * <br>Copyright (c) 2016 - 2017 Colorbot (<a href="https://colorbot.org">https://colorbot.org</a>) and contributors.
 * All rights reserved.
 * <br>
 *
 * @author <b>Colorbot</b>
 */
public final class Player {

    private final RT3Library library;
    private final OCR indicatorReader;
	private final Filter minimapFilter;
    
	private int prevAnimChanges = -1;
	private int prevMoveChanges = -1;

    private static final int HEALTH_BAR_GREEN = Calculations.packRGB(0, 255, 0);
    private static final int HEALTH_BAR_RED = Calculations.packRGB(255, 0, 0);

    protected Player(final RT3Library library) {
        this.library = library;

        final RGBMultiFilter indicatorFilter = new RGBMultiFilter(Calculations.packRGB(104, 90, 75),
		        Calculations.packRGB(0, 0, 0));

        indicatorFilter.setNegativeMode(true);

        this.indicatorReader = new OCR(library.screen, library.fontStore.get("rt3/resources/numbers.font"),
		        indicatorFilter, 100);

	    this.minimapFilter = new RGBMultiFilter(30D, Calculations.packRGB(240, 0, 0), Calculations.packRGB(240, 240, 240),
			    Calculations.packRGB(250, 250, 250), Calculations.packRGB(250, 50, 50), Calculations.packRGB(240, 240, 0),
			    Calculations.packRGB(255, 255, 10), Calculations.packRGB(0, 0, 0)) {
		    @Override
		    public Bitmap filter(final Bitmap bitmap) {
			    for(int x = 0; x < bitmap.getWidth(); x++) {
				    for(int y = 0; y < bitmap.getHeight(); y++) {
					    final int RGB = bitmap.getRGB(x, y);
					    bitmap.setRGB(x, y, isIndexColor(RGB) ? Color.CYAN.getRGB() : RGB);
				    }
			    }
			    return bitmap;
		    }
	    };
    }

    /**
     * Get the current player health from the minimap indicator.
     *
     * @return the player's current health points or -1 if not found.
     */
    public final int getCurrentHealth() {
        final String text = indicatorReader.readTextBlock(new Rectangle(520, 55, 21, 13)).getText();
        return text.matches("[0-9]+") ? Integer.parseInt(text) : -1;
    }

    /**
     * Calculates the estimate health percentage of the player based on the amount of red pixels in the
     * minimap indicator.
     *
     * @return the player's current health percentage.
     */
    public final int getHealthPercent() {
        final Bitmap health = library.screen.getBitmap().getSubBitmap(new Rectangle(543, 45, 26, 26));
        return (int) (100 - ((health.findRGB(25D, 10, 10, 10).size() / 363D) * 100D));
    }

    /**
     * Check if the player is performing an animation.
     * <br><b>Does not work properly in crowded areas.</b>
     *
     * @return <b>true</b> if the player is animating.
     */
    public final boolean isAnimating() {
	    final Rectangle target = new Rectangle(230, 130, 75, 75);

	    int changes = 0, threshold = 500;

	    o:
	    for (int i = 0; i < 5; i++) {
		    final int[] firstPixels = library.screen.getBitmap().getSubBitmap(target).getPixels();

		    library.sleep(50);

		    final int[] secondPixels = library.screen.getBitmap().getSubBitmap(target).getPixels();

		    for (int j = 0; j < firstPixels.length; j++) {
			    if (firstPixels[j] != secondPixels[j]) {
				    changes++;

				    if (changes > threshold) {
					    break o;
				    }
			    }
		    }
	    }

	    if (prevAnimChanges > threshold && changes < threshold) {
		    prevAnimChanges = -1;
		    return isAnimating();
	    }

	    prevAnimChanges = changes;
	    return changes > threshold;
    }

    /**
     * Check if the player is in combat.
     * <br><b>NOT WORKING</b>
     *
     * @return <b>true</b> if the player is in combat.
     */
    public final boolean isInCombat() {
       /* return library.screen.getItem().getSubBitmap(new Rectangle(0, 0, 0, 0)).findRGB(
                0, HEALTH_BAR_GREEN, HEALTH_BAR_RED).size() > 100;*/
        return false;
    }

	/**
	 * Check if the player is moving.
	 *
	 * @return <b>true</b> if the player is in motion.
	 */
	public final boolean isMoving() {
		if(library.movement.getDestination() != null) {
			return true;
		}

		final Bitmap first = minimapFilter.filter(library.movement.getMinimap());
		library.sleep(50);
		final Bitmap second = minimapFilter.filter(library.movement.getMinimap());

		int changes = 0, threshold = 300;

		o:
		for (int i = 0; i < 5; i++) {
			for (int x = 0; x < first.getWidth(); x++) {
				for (int y = 0; y < first.getHeight(); y++) {
					if (first.getRGB(x, y) != second.getRGB(x, y)) {
						changes++;
					}

					if (changes > threshold) {
						break o;
					}
				}
			}
		}
		if (prevMoveChanges > threshold && changes < threshold) {
			prevMoveChanges = -1;
			return isAnimating();
		}

		prevMoveChanges = changes;
		return changes > threshold;
	}
}
