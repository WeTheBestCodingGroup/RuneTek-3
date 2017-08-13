package rt3;

import com.zentek.colorbot.client.api.desktop.window.ChildWindow;
import com.zentek.colorbot.client.api.desktop.window.PopupWindow;
import com.zentek.colorbot.client.api.desktop.window.WindowNode;
import com.zentek.colorbot.client.api.image.BitmapStore;
import com.zentek.colorbot.client.api.image.ocr.FontStore;
import com.zentek.colorbot.client.api.script.DesktopLibrary;

import java.awt.*;
import java.util.logging.Level;

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
 * <br> File: RT3Library.java
 * <br> Purpose: A library of all available RuneScape(R) OldSchool API functions.
 * <br>
 * <br>Copyright (c) 2016 - 2017 Colorbot (<a href="https://www.colorbot.org">https://www.colorbot.org</a>) and contributors.
 * All rights reserved.
 * <br>
 *
 * @author <b>Colorbot</b>
 */
public class RT3Library extends DesktopLibrary {

	public enum CLIENT {
		OFFICIAL_CLIENT, OSBUDDY_CLIENT
	}

	public Camera camera;
	public Game game;
	public Player player;
	public Bank bank;
	public Tabs tabs;
	public Stats stats;
	public Inventory inventory;
	public Equipment equipment;
	public Prayer prayer;
	public Magic magic;
	public Chat chat;
	public Dialog dialog;
	public Menu menu;
	public Movement movement;

	private CLIENT client;

	public RT3Library(final BitmapStore bitmapStore, final FontStore fontStore, final String username,
	                  final String password) {
		super(bitmapStore, fontStore, username, password);

		init();

		this.game = new Game(this);
		this.camera = new Camera(this);
		this.player = new Player(this);
		this.bank = new Bank(this);
		this.tabs = new Tabs(this);
		this.stats = new Stats(this);
		this.inventory = new Inventory(this);
		this.equipment = new Equipment(this);
		this.prayer = new Prayer(this);
		this.magic = new Magic(this);
		this.chat = new Chat(this);
		this.dialog = new Dialog(this);
		this.menu = new Menu(this);
		this.movement = new Movement(this);

		if(game.isResizableMode()) {
			logger.log(Level.SEVERE, "Resizable mode is not currently supported. Switch to fixed size.");
		}
	}

	/**
	 * Calibrates all elements in the library, required once switched from fixed to fullscreen or when the
	 * screen has been resized.
	 */
	public void calibrate() {
		tabs.calibrate();
		stats.calibrate();
		inventory.calibrate();
		equipment.calibrate();
		prayer.calibrate();
		magic.calibrate();
	}

	/**
	 * Retrieve the used client type.
	 * @return the used client type.
	 */
	public CLIENT getClientType() {
		return client;
	}

	private void init() {
		final String windowTitle = window.getTitle();
		ChildWindow inputWindow = null;

		if(windowTitle.startsWith("Old School RuneScape")) {
			client = CLIENT.OFFICIAL_CLIENT;

			int bestScore = 0;
			for (final ChildWindow childWindow : window.getChildWindows()) {
				if (childWindow.getClassName().equals("SunAwtCanvas")) {
					final int score = childWindow.getBounds().x;
					if (inputWindow == null || bestScore < score) {
						inputWindow = childWindow;
						bestScore = score;
					}
				}
			}

			if(inputWindow != null) {
				window.setScreenCapture(inputWindow);
				window.redirectMouseInput(inputWindow);
				window.redirectKeyboardInput(inputWindow);
			}

		} else if(windowTitle.startsWith("OSBuddy")) {
			client = CLIENT.OSBUDDY_CLIENT;

			int i = 0;
			for (final ChildWindow childWindow : window.getChildWindows()) {
				if (childWindow.getClassName().equals("SunAwtCanvas")) {
					if (i == 1) {
						inputWindow = childWindow;
					}
					i += 1;
				}
			}

			PopupWindow keyboardInputWindow = null;
			for(final PopupWindow popupWindow : window.getPopupWindows()) {
				if(popupWindow.getClassName().equals("SunAwtFrame")) {
					keyboardInputWindow = popupWindow;
					break;
				}
			}

			if(inputWindow != null) {
				window.setScreenCapture(inputWindow);
				window.redirectMouseInput(inputWindow);
				System.out.println("inputWindow: " + inputWindow.toString());
				//screen.setScreenRect(new Rectangle(0, 0, Game.FIXED_SIZE.width, Game.FIXED_SIZE.height));
			}

			if(keyboardInputWindow != null) {
				window.redirectKeyboardInput(keyboardInputWindow);
				System.out.println("keyboardInputWindow: " + keyboardInputWindow.toString());
			}

			System.out.println("Window size: " + window.getSize().toString());

		} else {
			logger.log(Level.SEVERE, "Unsupported client type. Use the official client or OSBuddy client.");
			return;
		}

		if(inputWindow == null) {
			logger.log(Level.SEVERE, "Failed to find the client screen.");
			return;
		}
		window.setAbsolutePositioning(false);
	}
}
