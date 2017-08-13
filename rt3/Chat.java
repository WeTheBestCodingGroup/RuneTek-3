package rt3;

import com.zentek.colorbot.client.api.image.Bitmap;
import com.zentek.colorbot.client.api.image.filter.Filter;
import com.zentek.colorbot.client.api.image.ocr.OCR;
import com.zentek.colorbot.client.api.util.Calculations;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
 * <br> File: Chat.java
 * <br> Purpose: Represents the in-game chat-box.
 * <br>
 * <br>Copyright (c) 2016 - 2017 Colorbot (<a href="https://www.colorbot.org">https://www.colorbot.org</a>) and contributors.
 * All rights reserved.
 * <br>
 *
 * @author <b>Colorbot</b>
 * @version <b>1.0.0</b>
 */
public final class Chat extends Widget {

	public static final int LINE_HEIGHT = 14;
	public static final int CHAT_BACKGROUND = Calculations.packRGB(178, 163, 132);

	private final RT3Library library;
	private final OCR chatReader;
	private final Map<MessageListener, ExecutorService> messageListeners;

	protected Chat(final RT3Library library) {
		super(new Rectangle(10, 444, 485, 112), library);
		this.library = library;
		this.messageListeners = new HashMap<>();

		final Filter chatFilter = new Filter(CHAT_BACKGROUND, 140D);
		chatFilter.setNegativeMode(true);

		this.chatReader = new OCR(library.screen, library.fontStore.get("rt3/resources/chat.font"), chatFilter, 3);
	}

	public final boolean isOpen() {
		final int RGB = Calculations.packRGB(128, 118, 96);
		final Bitmap bitmap = library.screen.getBitmap();
		return bitmap.getRGB(494, 458) == RGB || bitmap.getRGB(50, 458) == RGB;
	}

	/**
	 * Register a new {@code messageListener}.
	 *
	 * @param messageListener the class where the MessageListener is implemented
	 */
	public final void registerMessageListener(final MessageListener messageListener) {
		if (!messageListeners.containsKey(messageListener)) {
			final ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();

			messageListeners.put(messageListener, service);
			service.scheduleAtFixedRate(new Runnable() {
				List<String> lastMessages = new ArrayList<>();

				@Override
				public void run() {
					final List<String> messages = getMessages();
					List<String> subList;

					int idx = -1;
					for (int i = lastMessages.size(); i > 0; i--) {
						subList = lastMessages.subList(0, i);
						idx = Collections.lastIndexOfSubList(messages, subList);

						if (idx > -1) {
							break;
						}
					}

					for (int i = 0; i < (idx == -1 ? messages.size() : idx); i++) {
						messageListener.onMessageEvent(messages.get(i));
					}
					lastMessages = messages;
				}
			}, 0, 500, TimeUnit.MILLISECONDS);
		}
	}

	/**
	 * Unregister a {@code messageListener}.
	 *
	 * @param messageListener the class where the MessageListener is implemented
	 */
	public final void unregisterMessageListener(final MessageListener messageListener) {
		if (messageListeners.containsKey(messageListener)) {
			messageListeners.get(messageListener).shutdownNow();
			messageListeners.remove(messageListener);
		}
	}

	/**
	 * Read all the text in the chat box.
	 *
	 * @return a list containing all the text in the chat box.
	 */
	public final List<String> getMessages() {
		return readText(0, 8);
	}

	/**
	 * Reads text {@code from} a given line index {@code to} another. <b>0</b> Is the bottom of the chat box.
	 *
	 * @param from start index
	 * @param to   end index
	 * @return the text from and to specific line indexes.
	 */
	public final List<String> readText(final int from, final int to) {
		final List<String> textLines = new ArrayList<>();

		for (int i = from; i < to; i++) {
			final String text = readText(i);
			if (!text.isEmpty()) {
				textLines.add(readText(i));
			}
		}

		return textLines;
	}

	/**
	 * Reads text at a specific {@code lineIndex}. <b>0</b> Is the bottom of the chat box.
	 *
	 * @param lineIndex line index
	 * @return the text at the given lineIndex
	 */
	public final String readText(final int lineIndex) {
		return chatReader.readTextBlock(indexToRectangle(lineIndex)).getText();
	}

	private Rectangle indexToRectangle(final int lineIndex) {
		return new Rectangle(10, 444 - (lineIndex * LINE_HEIGHT), width, LINE_HEIGHT);
	}
}
