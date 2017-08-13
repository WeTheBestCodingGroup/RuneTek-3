package rt3;

import com.zentek.colorbot.client.api.image.Bitmap;
import com.zentek.colorbot.client.api.image.filter.Filter;
import com.zentek.colorbot.client.api.image.filter.RGBMultiFilter;
import com.zentek.colorbot.client.api.image.ocr.OCR;
import com.zentek.colorbot.client.api.input.MouseButton;
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
 * <br> File: Inventory.java
 * <br> Purpose: Represents the in-game inventory.
 * <br>
 * <br>Copyright (c) 2016 - 2017 Colorbot (<a href="https://www.colorbot.org">https://www.colorbot.org</a>) and contributors.
 * All rights reserved.
 * <br>
 *
 * @author <b>Colorbot</b>
 */
public final class Inventory {

	private static final int SLOT_WIDTH = 32;
	private static final int SLOT_HEIGHT = 32;

	private final RT3Library library;
	private final OCR amountReader;
	private final Filter itemFilter;

	private final Slot[] slots = new Slot[28];

	protected Inventory(final RT3Library library) {
		this.library = library;
		this.itemFilter = new Filter(Calculations.packRGB(66, 64, 56), 20D) {
			@Override
			public Bitmap filter(final Bitmap bitmap) {
				for (int x = 0; x < bitmap.getWidth(); x++) {
					for (int y = 0; y < bitmap.getHeight(); y++) {
						if (isIndexColor(bitmap.getSamples(x, y))) {
							bitmap.setRGB(x, y, -1);
						}
					}
				}
				return bitmap;
			}
		};

		final Filter amountFilter = new RGBMultiFilter(Calculations.packRGB(255, 255, 0),
				Calculations.packRGB(255, 255, 255), Calculations.packRGB(0, 255, 128));
		this.amountReader = new OCR(library.screen, library.fontStore.get("rt3/resources/amount.font"), amountFilter, 5);
		this.calibrate();
	}

	/**
	 * Calculates the bounding rectangles of all {@link Inventory.Slot} slots.
	 */
	public final void calibrate() {
		if (!library.game.isResizableMode()) {
			int row = -1;
			int numSlot = 0;

			for(int i = 0; i < slots.length; i++) {
				if (numSlot % 4 == 0) {
					row += 1;
					numSlot = 0;
				}

				final Slot currSlot = slots[i];
				if(currSlot == null) {
					slots[i] = new Slot(i, 562 + (numSlot * SLOT_WIDTH) + (numSlot * 10), 213 +
							(row * SLOT_HEIGHT) + (row * 4), library);
				} else {
					currSlot.setLocation(562 + (numSlot * SLOT_WIDTH) + (numSlot * 10), 213 + (row * SLOT_HEIGHT) + (row * 4));
				}
				numSlot += 1;
			}
		}
	}

	/**
	 * Check if an item is selected.
	 * <br><b>note:</b> Opens the inventory.
	 *
	 * @return <b>true</b> if an item is selected.
	 */
	public final boolean isItemSelected() {
		return getSelectedItem() != null;
	}

	/**
	 * Get the current selected item.
	 * <br><b>note:</b> Opens the inventory.
	 *
	 * @return {@link Bitmap} image of the selected item or null if none is selected.
	 */
	public final Slot getSelectedItem() {
		for (final Slot slot : slots) {
			if (slot.isEmpty()) {
				continue;
			}

			if (slot.isSelected()) {
				return slot;
			}
		}
		return null;
	}

	/**
	 * Checks if the inventory contains the {@code item}.
	 * <br><b>note:</b> Opens the inventory.
	 *
	 * @see Inventory#getSlot(Bitmap)
	 * @param item full or partial {@link Bitmap} image of the item with a whitespace background
	 * @return <b>true</b> if the inventory contains the item.
	 */
	public final boolean contains(final Bitmap item) {
		return getSlot(item) != null;
	}

	/**
	 * Get the first {@link Inventory.Slot} where a {@code item} is located at.
	 * <br><b>note:</b> Opens the inventory.
	 *
	 * @param item full or partial {@link Bitmap} image of the item with a whitespace background
	 * @return the first slot the {@code item} is located at or null.
	 */
	public final Slot getSlot(final Bitmap item) {
		for (final Slot slot : this.slots) {
			if (slot.getItem().findBitmap(item, 5D, 70D, false).size() > 0) {
				return slot;
			}
		}
		return null;
	}

	/**
	 * Get all slots where a given {@code item} is located at.
	 * <br><b>note:</b> Opens the inventory.
	 *
	 * @param item full or partial {@link Bitmap} image of the item with a whitespace background
	 * @return {@link Inventory.Slot} array containing all the slots the {@code item} is located at.
	 */
	public final List<Slot> getSlots(final Bitmap item) {
		final List<Slot> slots = new ArrayList<>();
		for (final Slot slot : this.slots) {
				final Bitmap bitmap = slot.getItem();
				final List<Rectangle> list = bitmap.findBitmap(item, 5D, 70D, false);
				if (list.size() > 0) {
					slots.add(slot);
				}
		}
		return slots;
	}

	/**
	 * Get all the {@link Bitmap} images with a whitespace background of all items in the inventory.
	 * <br><b>note:</b> Opens the inventory.
	 *
	 * @return {@link Bitmap} list of all the items in the inventory.
	 */
	public final List<Bitmap> getItems() {
		if (library.tabs.getSelectedTab() != Tabs.INVENTORY && !library.bank.isOpen()) {
			Tabs.INVENTORY.click(MouseButton.LEFT);
		}

		final List<Bitmap> items = new ArrayList<>();
		for (final Slot slot : slots) {
			if (!slot.isEmpty()) {
				items.add(slot.getItem());
			}
		}
		return items;
	}

	/**
	 * Get the count of the taken inventory slots.
	 * <br><b>note:</b> Opens the inventory.
	 *
	 * @return item count of inventory.
	 */
	public final int getCount() {
		return getCount(false);
	}

	/**
	 * Get the count of the taken inventory slots.
	 * <br><b>note:</b> Opens the inventory.
	 *
	 * @param countStack count the stacks of the taken inventory slots
	 * @return item count of inventory.
	 */
	public final int getCount(final boolean countStack) {
		int count = 0;

		for (final Slot slot : slots) {
			if (!slot.isEmpty()) {
				if(!countStack) {
					count += 1;
				} else {
					slot.getCount(true);
				}
			}
		}
		return count;
	}

	/**
	 * Check if the inventory is full.
	 * <br><b>note:</b> Opens the inventory.
	 *
	 * @return <b>true</b> if the inventory is full.
	 */
	public final boolean isFull() {
		return getCount() == 28;
	}

	/**
	 * Check if the inventory is empty.
	 * <br><b>note:</b> Opens the inventory.
	 *
	 * @return <b>true</b> if the inventory is empty.
	 */
	public final boolean isEmpty() {
		return getCount() == 0;
	}

	public final List<Inventory.Slot> getSlots() {
		return Arrays.asList(slots);
	}

	public class Slot extends Widget {

		private final int idx;

		private Slot(final int idx, final int x, final int y, final RT3Library library) {
			super(x, y, SLOT_WIDTH, SLOT_HEIGHT, library);
			this.idx = idx;
		}

		private void setLocation(final int x, final int y) {
			this.x = x;
			this.y = y;
		}

		/**
		 * Get the index of this {@code Slot}.
		 * @return the index of the {@code Slot}.
		 */
		public final int getIndex() {
			return idx;
		}

		/**
		 * Check if this {@code slot} is empty.
		 * <br><b>note:</b> Opens the inventory.
		 *
		 * @return <b>true</b> if the {@code slot} is empty.
		 */
		public final boolean isEmpty() {
			if (library.tabs.getSelectedTab() != Tabs.INVENTORY && !library.bank.isOpen()) {
				Tabs.INVENTORY.click(MouseButton.LEFT);
			}
			return getBitmap().findRGB(20D, 66, 64, 56).size() > 950;
		}

		@Override
		public final boolean click(final String action) {
			if (library.tabs.getSelectedTab() != Tabs.INVENTORY && !library.bank.isOpen()) {
				Tabs.INVENTORY.click(MouseButton.LEFT);
			}
			return super.click(action);
		}

		/**
		 * Click on a given {@code slot} with a specific {@code mouseButton}.
		 *
		 * @param mouseButton {@link MouseButton} mouseButton
		 * @return <b>true</b> when the action is completed.
		 */
		@Override
		public boolean click(final MouseButton mouseButton) {
			if (library.tabs.getSelectedTab() != Tabs.INVENTORY && !library.bank.isOpen()) {
				Tabs.INVENTORY.click(MouseButton.LEFT);
			}
			return super.click(mouseButton);
		}

		/**
		 * Get the filtered {@link Bitmap} image of an item with a whitespace background at this {@code slot}.
		 * <br><b>note:</b> Opens the inventory.
		 *
		 * @return {@link Bitmap} image of the item the {@code slot}.
		 */
		public final Bitmap getItem() {
			if (library.tabs.getSelectedTab() != Tabs.INVENTORY && !library.bank.isOpen()) {
				Tabs.INVENTORY.click(MouseButton.LEFT);
			}
			return itemFilter.filter(getBitmap());
		}

		public final boolean isSelected() {
			return !isEmpty() && getBitmap().findRGB(255, 255, 255).size() > 20;
		}

		public final int getCount(final boolean countStack) {
			if(isEmpty()) {
				return 0;
			}

			if(!countStack) {
				return 1;
			}

			final String text = amountReader.readTextBlock(new Rectangle(x, y, width, 9)).getText();
			if (text.isEmpty()) {
				return 1;
			}
			return text.replace("K", "000").replace("M", "000000").matches("[0-9]+") ? Integer.parseInt(text) : -1;
		}
	}
}
