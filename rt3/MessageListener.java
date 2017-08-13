package rt3;

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
 * <br> File: MessageListener.java
 * <br> Purpose: Listens for in-game chat messages.
 * <br>
 * <br>Copyright (c) 2016 - 2017 Colorbot (<a href="https://colorbot.org">https://colorbot.org</a>) and contributors.
 * All rights reserved.
 * <br>
 *
 * @author <b>Colorbot</b>
 */
public interface MessageListener {

	void onMessageEvent(final String message);
}
