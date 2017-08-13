package rt3;

import com.zentek.colorbot.client.api.image.Bitmap;
import com.zentek.colorbot.client.api.image.filter.Filter;
import com.zentek.colorbot.client.api.image.filter.RGBMultiFilter;
import com.zentek.colorbot.client.api.image.ocr.OCR;
import com.zentek.colorbot.client.api.input.MouseButton;
import com.zentek.colorbot.client.api.random.Random;
import com.zentek.colorbot.client.api.util.Calculations;
import com.zentek.colorbot.client.api.util.Range;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

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
 * <br> File: Bank.java
 * <br> Purpose: Handles in-game banking.
 * <br>
 * <br>Copyright (c) 2016 - 2017 Colorbot (<a href="https://www.colorbot.org">https://www.colorbot.org</a>) and contributors.
 * All rights reserved.
 * <br>
 *
 * @author <b>Colorbot</b>
 */
public final class Bank extends Widget {

	private static final int SELECTION_COLOR = Calculations.packRGB(160, 38, 35);
	private static final int ASTERISK_COLOR = Calculations.packRGB(0, 0, 128);

	private static final Rectangle SCROLL_BAR = new Rectangle(483, 99, 13, 177);
	private static final Rectangle SCROLL_UP = new Rectangle(482, 83, 15, 14);
	private static final Rectangle SCROLL_DOWN = new Rectangle(482, 278, 15, 14);

	private final Filter bankFilter;
	private final Filter itemFilter;

	private final OCR slotReader;
	private final OCR bankReader;

	public enum Amount {
		ONE, FIVE, TEN, ALL_BUT_ONE, ALL
	}

	public enum WithdrawMode {
		ITEM, NOTE, NULL
	}

	public enum RearrangeMode {
		SWAP, INSERT, NULL
	}

	private final RT3Library library;

	protected Bank(final RT3Library library) {
		super(0, 0, 0, 0, library);
		this.library = library;

		this.bankFilter = new RGBMultiFilter(20D, Calculations.packRGB(70, 65, 50), Calculations.packRGB(62, 53, 41));
		this.bankFilter.setNegativeMode(true);

		this.itemFilter = new RGBMultiFilter(20D, Calculations.packRGB(70, 65, 50), Calculations.packRGB(62, 53, 41)) {
			public Bitmap filter(final Bitmap bitmap) {
				for (int x = 0; x < bitmap.getWidth(); x++) {
					for (int y = 0; y < bitmap.getHeight(); y++) {
						if(isIndexColor(bitmap.getRGB(x, y))) {
							bitmap.setRGB(x, y, -1);
						}
					}
				}
				return bitmap;
			}
		};

		this.slotReader = new OCR(library.screen, library.fontStore.get("rt3/resources/amount.font"),
				new Filter(Calculations.packRGB(255, 152, 31), 0D), 5);

		this.bankReader = new OCR(library.screen, library.fontStore.get("rt3/resources/amount.font"),
				new RGBMultiFilter(Calculations.packRGB(255, 255, 0), Calculations.packRGB(255, 255, 255),
						Calculations.packRGB(0, 255, 128)), 5);
	}

	public final boolean isOpen() {
		final Bitmap bitmap = library.screen.getBitmap();
		return bitmap.getRGB(495, 39) == Calculations.packRGB(52, 52, 49) &&
				bitmap.getRGB(500, 9) == Calculations.packRGB(63, 64, 60);
	}

	public final boolean close() {
		return library.mouse.click(new Rectangle(476, 13, 19, 20), MouseButton.LEFT);
	}

	public final boolean contains(final Bitmap bitmap) {
		return isOpen() && findItem(bitmap) != null;
	}

	public int getScrollBarLocation() {
		final List<Point> colors = library.screen.getBitmap(SCROLL_BAR).findRGB(0, 0, 1);
		int y = Integer.MAX_VALUE;
		for(final Point color : colors) {
			if(color.y < y) {
				y = color.y;
			}
		}
		return y;
	}

	public int getMaxScrollBarLocation() {
		final List<Point> colors = library.screen.getBitmap(SCROLL_BAR).findRGB(0, 0, 1);
		int lowY = Integer.MAX_VALUE;
		int highY = 0;
		for(final Point color : colors) {
			if(color.y > highY) {
				highY = color.y;
			}
			if(color.y < lowY) {
				lowY = color.y;
			}
		}
		return SCROLL_BAR.height - highY + lowY;
	}

	public void scroll(final boolean up) {
		final Point mouse = library.mouse.getMouseLocation();
		final Rectangle button = up ? SCROLL_UP : SCROLL_DOWN;

		if(!button.contains(mouse)) {
			library.mouse.moveTo(button);
		}

		library.mouse.pressButton(MouseButton.LEFT);
		library.sleep(300, 900);
		library.mouse.releaseButton(MouseButton.LEFT);
	}

	public final Bank.Slot findItem(final Bitmap item) {
		if (!isOpen()) {
			return null;
		}

		List<Bank.Slot> slots = library.bank.getBankSlots();
		for (final Bank.Slot slot : slots) {
			if (slot.getItem().findBitmap(item, 5D, 90D).size() > 0) {
				return slot;
			}
		}

		final int maxScrollBarLocation = getMaxScrollBarLocation() - 1;
		boolean top = getScrollBarLocation() == 0;

		while (slots.size() > 0 && getScrollBarLocation() < maxScrollBarLocation) {
			if (!top) {
				scroll(true);
				if(getScrollBarLocation() == 0) {
					top = true;
				}
			} else {
				scroll(false);
			}

			slots = getBankSlots();
			for (final Bank.Slot slot : slots) {
				if (slot.getItem().findBitmap(item, 5D, 90D).size() > 0) {
					return slot;
				}
			}
			library.sleep(Random.nextHumanDelay());
		}
		return null;
	}

	public final boolean searchItem(final String itemName, final boolean pressEnter) {
		if(isOpen() && library.mouse.click(new Rectangle(382, 295, 35, 35), MouseButton.LEFT)) {
			if(library.sleep(new Callable<Boolean>() {
				@Override
				public Boolean call() throws Exception {
					return library.screen.getBitmap().getRGB(258, 428) == ASTERISK_COLOR;
				}
			}, new Range(100, 300), 2500)) {
				return library.keyboard.typeText(itemName, pressEnter);
			}
		}
		return false;
	}

	public final boolean setPlaceHolders() {
		return isOpen() && library.mouse.click(new Rectangle(381, 295, 35, 35), MouseButton.LEFT);
	}

	public final boolean arePlaceHoldersEnabled() {
		return isOpen() && library.screen.getBitmap().getRGB(350, 300) == Calculations.packRGB(117, 28, 26);
	}

	public final boolean depositInventory() {
		return isOpen() && !library.inventory.isEmpty() && library.mouse.click(new Rectangle(426, 295, 35, 35),
				MouseButton.LEFT);
	}

	public final boolean depositEquipment() {
		return isOpen() && library.mouse.click(new Rectangle(462, 295, 35, 35), MouseButton.LEFT);
	}

	public final int getFreeSlots() {
		final String text = slotReader.readTextBlock(new Rectangle(22, 12, 25, 10)).getText();
		return text.matches("[0-9]+") ? Integer.parseInt(text) : -1;
	}

	public final int getTotalSlots() {
		final String text = slotReader.readTextBlock(new Rectangle(22, 25, 25, 10)).getText();
		return text.matches("[0-9]+") ? Integer.parseInt(text) : -1;
	}

	public final boolean setWithdrawMode(final WithdrawMode withdrawMode) {
		if (!isOpen()) {
			return true;
		}

		switch (withdrawMode) {
			case ITEM:
				return library.mouse.click(new Rectangle(180, 310, 75, 21), MouseButton.LEFT);
			case NOTE:
				return library.mouse.click(new Rectangle(256, 310, 75, 21), MouseButton.LEFT);
		}
		return true;
	}

	public final WithdrawMode getWithdrawMode() {
		if (!isOpen()) {
			return WithdrawMode.NULL;
		}

		final Bitmap bitmap = library.screen.getBitmap();
		return bitmap.getRGB(245, 330) == SELECTION_COLOR ? WithdrawMode.ITEM :
				bitmap.getRGB(325, 330) == SELECTION_COLOR ? WithdrawMode.NOTE : WithdrawMode.NULL;
	}

	public final boolean setRearrangeMode(final RearrangeMode rearrangeMode) {
		if (!isOpen()) {
			return true;
		}

		switch (rearrangeMode) {
			case SWAP:
				return library.mouse.click(new Rectangle(21, 310, 75, 21), MouseButton.LEFT);
			case INSERT:
				return library.mouse.click(new Rectangle(97, 310, 75, 21), MouseButton.LEFT);
		}
		return true;
	}

	public final RearrangeMode getRearrangeMode() {
		if (!isOpen()) {
			return RearrangeMode.NULL;
		}
		final Bitmap bitmap = library.screen.getBitmap();
		return bitmap.getRGB(145, 330) == SELECTION_COLOR ? RearrangeMode.INSERT :
				bitmap.getRGB(50, 330) == SELECTION_COLOR ? RearrangeMode.SWAP : RearrangeMode.NULL;
	}

	public final boolean withdraw(final Bank.Slot slot, final Bank.Amount amount) {
		if (!isOpen()) {
			return true;
		}

		if (amount == Amount.ONE) {
			return slot.click(MouseButton.LEFT);
		}

		if (!library.menu.isOpen() && slot.click(MouseButton.RIGHT)) {
			library.sleep(new Callable<Boolean>() {
				@Override
				public Boolean call() throws Exception {
					return library.menu.isOpen();
				}
			}, new Range(100, 300), 2500);
		}

		if (library.menu.isOpen()) {
			switch (amount) {
				case FIVE:
					return library.menu.click("Withdraw-5");
				case TEN:
					return library.menu.click("Withdraw-10");
				case ALL:
					return library.menu.click("Withdraw-All-but-one");
				case ALL_BUT_ONE:
					return library.menu.click("Withdraw-All");
			}
		}
		return true;
	}

	public final boolean withdraw(final Bank.Slot slot, final int amount) {
		if (!isOpen()) {
			return true;
		}

		if (amount == 1) {
			return slot.click(MouseButton.LEFT);
		} else if(amount == 5) {
			return withdraw(slot, Amount.FIVE);
		} else if(amount == 10) {
			return withdraw(slot, Amount.TEN);
		}

		if (!library.menu.isOpen() && slot.click(MouseButton.RIGHT)) {
			library.sleep(new Callable<Boolean>() {
				@Override
				public Boolean call() throws Exception {
					return library.menu.isOpen();
				}
			}, new Range(100, 300), 2500);
		}

		if (library.menu.isOpen()) {
			final String strAmount = String.valueOf(amount);
			final int idx = library.menu.indexOf("Withdraw-" + strAmount);

			if (idx != -1) {
				return library.menu.click(idx);
			} else if (library.menu.click("Withdraw-X")) {
				if(library.sleep(new Callable<Boolean>() {
					@Override
					public Boolean call() throws Exception {
						return library.screen.getBitmap().getRGB(258, 428) == ASTERISK_COLOR;
					}
				}, new Range(100, 300), 2500)) {
					return library.keyboard.typeText(strAmount, true);
				}
			}
		}
		return true;
	}

	public final boolean deposit(final Inventory.Slot slot, final Bank.Amount amount) {
		if (!isOpen()) {
			return true;
		}

		if (amount == Amount.ONE) {
			return slot.click(MouseButton.LEFT);
		}

		if (!library.menu.isOpen() && slot.click(MouseButton.RIGHT)) {
			library.sleep(new Callable<Boolean>() {
				@Override
				public Boolean call() throws Exception {
					return library.menu.isOpen();
				}
			}, new Range(100, 300), 2500);
		}

		if (library.menu.isOpen()) {
			switch (amount) {
				case FIVE:
					return library.menu.click("Deposit-5");
				case TEN:
					return library.menu.click("Deposit-10");
				case ALL_BUT_ONE:
					return library.menu.click("Deposit-All-but-one");
				case ALL:
					return library.menu.click("Deposit-All");
			}
		}
		return true;
	}

	public final boolean deposit(final Inventory.Slot slot, final int amount) {
		if (!isOpen()) {
			return true;
		}

		if (amount == 1) {
			return slot.click(MouseButton.LEFT);
		} else if(amount == 5) {
			return deposit(slot, Amount.FIVE);
		} else if(amount == 10) {
			return deposit(slot, Amount.TEN);
		}

		if (!library.menu.isOpen() && slot.click(MouseButton.RIGHT)) {
			library.sleep(new Callable<Boolean>() {
				@Override
				public Boolean call() throws Exception {
					return library.menu.isOpen();
				}
			}, new Range(100, 300), 2500);
		}

		if (library.menu.isOpen()) {
			final String strAmount = String.valueOf(amount);
			final int idx = library.menu.indexOf("Deposit-" + strAmount);

			if (idx != -1) {
				return library.menu.click(idx);
			} else if (library.menu.click("Deposit-X")) {
				if(library.sleep(new Callable<Boolean>() {
					@Override
					public Boolean call() throws Exception {
						return library.screen.getBitmap().getRGB(258, 428) == ASTERISK_COLOR;
					}
				}, new Range(100, 300), 2500)) {
					return library.keyboard.typeText(strAmount, true);
				}
			}
		}
		return true;
	}

	public List<Bank.Slot> getBankSlots() {
		if (!isOpen()) {
			return new ArrayList<>();
		}

		final Bitmap bankWindow = bankFilter.filter(library.screen.getBitmap(new Rectangle(60, 82, 390, 212)));
		final int w = bankWindow.getWidth();
		final int h = bankWindow.getHeight();

		final List<Integer> vPoints = new ArrayList<>();
		final List<Integer> hPoints = new ArrayList<>();

		boolean columnEnd = false;
		Vertical:
		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y++) {
				if (bankWindow.getRGB(x, y) != -1) {
					if (!columnEnd) {
						vPoints.add(x);
						columnEnd = true;
					}
					continue Vertical;
				}
				if ((h - y) <= 1 && columnEnd) {
					vPoints.add(x);
					columnEnd = false;
					continue Vertical;
				}
			}
		}

		boolean rowEnd = false;
		Horizontal:
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				if (bankWindow.getRGB(x, y) != -1) {
					if (!rowEnd) {
						hPoints.add(y);
						rowEnd = true;
					}
					continue Horizontal;
				}
				if ((w - x) <= 1 && rowEnd) {
					hPoints.add(y);
					rowEnd = false;
					continue Horizontal;
				}
			}
		}

		final List<Bank.Slot> bankSlots = new ArrayList<>();
		for (int row = 1; row < hPoints.size(); row += 2) {
			for (int column = 1; column < vPoints.size(); column += 2) {
				final Point nw = new Point(vPoints.get(column - 1), hPoints.get(row - 1));
				final Point se = new Point(vPoints.get(column), hPoints.get(row));
				final Rectangle bounds = new Rectangle(nw.x, nw.y, se.x - nw.x, se.y - nw.y);

				if(bankWindow.getSubBitmap(bounds).findRGB(bankFilter.getBackground()).size() > 50) {
					bounds.translate(bankWindow.getX(), bankWindow.getY());
					bankSlots.add(new Bank.Slot(bounds, library));
				}
			}
		}
		return bankSlots;
	}

	public class Slot extends Widget {

		public Slot(Rectangle rectangle, RT3Library library) {
			super(rectangle, library);
		}

		public final Bitmap getItem() {
			return itemFilter.filter(getBitmap());
		}

		public final int getCount() {
			if (!isOpen()) {
				return -1;
			}

			final String text = bankReader.readTextBlock(new Rectangle(x - 1, y, width + 1, 9)).getText();
			if (text.isEmpty()) {
				return 1;
			}
			return text.toLowerCase().replace("k", "000").replace("m", "000000").matches("[0-9]+") ?
					Integer.parseInt(text) : -1;
		}
	}
}
