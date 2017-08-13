package rt3;

import com.zentek.colorbot.client.api.image.Bitmap;
import com.zentek.colorbot.client.api.image.filter.Filter;
import com.zentek.colorbot.client.api.image.filter.RGBMultiFilter;
import com.zentek.colorbot.client.api.image.ocr.OCR;
import com.zentek.colorbot.client.api.image.ocr.TextLine;
import com.zentek.colorbot.client.api.input.MouseButton;
import com.zentek.colorbot.client.api.util.Calculations;

import java.awt.*;
import java.util.ArrayList;
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
 * <br> File: Dialog.java
 * <br> Purpose: Represents the in-game dialog box.
 * <br>
 * <br>Copyright (c) 2016 - 2017 Colorbot (<a href="https://www.colorbot.org">https://www.colorbot.org</a>) and contributors.
 * All rights reserved.
 * <br>
 *
 * @author <b>Colorbot</b>
 */
public final class Dialog extends Widget {

	private final RT3Library library;
	private final OCR dialogReader;

	public static final int[] DIALOG_FONT_COLORS = new int[] {
			Calculations.packRGB(0, 0, 128), Calculations.packRGB(0, 0, 0), Calculations.packRGB(0, 0, 255)
	};

	protected Dialog(final RT3Library library) {
		super(new Rectangle(10, 444, 485, 112), library);
		this.library = library;

		final Filter dialogFilter = new RGBMultiFilter(0D, DIALOG_FONT_COLORS);
		this.dialogReader = new OCR(library.screen, library.fontStore.get("rt3/resources/dialog.font"), dialogFilter, 5);
	}

	/**
	 * Checks if a dialog is opened.
	 *
	 * @return <b>true</b> if a dialog is open.
	 */
	public final boolean isOpen() {
		final int RGB = Calculations.packRGB(128, 118, 96);
		final Bitmap bitmap = library.screen.getBitmap();
		return bitmap.getRGB(494, 458) != RGB && bitmap.getRGB(50, 458) != RGB;
	}

	/**
	 * Check if the {@code Continue} option is visible in the dialog.
	 *
	 * @return <b>true</b> if the player can press continue.
	 */
	public final boolean canContinue() {
		final String text = dialogReader.readTextBlock(new Rectangle(206, 436, 162, 17)).getText();
		if(text.isEmpty()) {
			return false;
		}
		return isOpen() && Calculations.matchText(text, "Click here to continue", 60D);
	}

	/**
	 * Select the {@code Continue} option.
	 *
	 * @return <b>true</b> when the action is completed.
	 * @see #canContinue()
	 */
	public final boolean clickContinue() {
		return library.mouse.click(new Rectangle(206, 436, 162, 17), MouseButton.LEFT);
	}

	/**
	 * Read all visible dialog text.
	 *
	 * @return a list containing all visible dialog text lines.
	 */
	public final List<String> getText() {
		final List<String> text = new ArrayList<>();
		for(final TextLine textLine : dialogReader.readTextBlock(new Rectangle(118, 366, 385, 92)).getTextLines()) {
			text.add(textLine.getText());
		}
		return text;
	}

	/**
	 * Select an {@code option}.
	 *
	 * @param option option to select
	 * @return <b>true</b> when the action is completed.
	 */
	public final boolean clickOption(final String option) {
		//TODO: find boundaries of textline options
		return false;
	}

	/**
	 * Select an option by a given {@code index}.
	 *
	 * @param index index of option to select
	 * @return <b>true</b> when the action is completed.
	 */
	public final boolean clickOption(final int index) {
		//TODO: find boundaries of textline options
		return false;
	}
}
