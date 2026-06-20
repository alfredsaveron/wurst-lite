/*
 * Copyright (c) 2014-2026 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.util;

import java.util.List;
import java.util.StringJoiner;

import net.minecraft.client.GuiMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import net.wurstclient.WurstClient;

public enum ChatUtils
{
	;
	
	private static final Minecraft MC = WurstClient.MC;
	
	private static final String WURST_NAME =
		"\u00a7dW\u00a7du\u00a7dr\u00a7ds\u00a7dt \u00a7dL\u00a7di\u00a7dt\u00a7de";
	
	public static final String WURST_PREFIX =
		WURST_NAME + "\u00a7r \u00a7u\u00bb\u00a7r ";
	private static final String WARNING_PREFIX =
		WURST_NAME + "\u00a7r \u00a7u\u00bb\u00a7r ";
	private static final String ERROR_PREFIX =
		WURST_NAME + "\u00a7r \u00a7u\u00bb\u00a7r ";
	private static final String SYNTAX_ERROR_PREFIX =
		WURST_NAME + "\u00a7r \u00a7u\u00bb\u00a7r ";
	
	private static boolean enabled = true;
	
	public static void setEnabled(boolean enabled)
	{
		ChatUtils.enabled = enabled;
	}
	
	public static void component(Component component)
	{
		if(!enabled)
			return;
		
		ChatComponent chatHud = MC.gui.getChat();
		chatHud.addMessage(component);
	}
	
	public static void message(String message)
	{
		if(!enabled)
			return;
		
		ChatComponent chatHud = MC.gui.getChat();
		String[] lines = message.split("\n");
		for(String line : lines)
		{
			String formatted = line;
			if(!formatted.startsWith("\u00a7"))
				formatted = "\u00a77" + formatted;
			chatHud.addMessage(Component.literal(WURST_PREFIX + formatted));
		}
	}
	
	public static void warning(String message)
	{
		if(!enabled)
			return;
		ChatComponent chatHud = MC.gui.getChat();
		chatHud.addMessage(
			Component.literal(WARNING_PREFIX + "\u00a77" + message));
	}
	
	public static void error(String message)
	{
		if(!enabled)
			return;
		ChatComponent chatHud = MC.gui.getChat();
		chatHud.addMessage(
			Component.literal(ERROR_PREFIX + "\u00a77" + message));
	}
	
	public static void syntaxError(String message)
	{
		if(!enabled)
			return;
		ChatComponent chatHud = MC.gui.getChat();
		chatHud.addMessage(
			Component.literal(SYNTAX_ERROR_PREFIX + "\u00a77" + message));
	}
	
	public static String getAsString(GuiMessage.Line visible)
	{
		return getAsString(visible.content());
	}
	
	public static String getAsString(FormattedCharSequence text)
	{
		JustGiveMeTheStringVisitor visitor = new JustGiveMeTheStringVisitor();
		text.accept(visitor);
		return visitor.toString();
	}
	
	public static final String wrapText(String text, int width)
	{
		return wrapText(text, width, Style.EMPTY);
	}
	
	public static final String wrapText(String text, int width, Style style)
	{
		List<FormattedText> lines =
			MC.font.getSplitter().splitLines(text, width, Style.EMPTY);
		
		StringJoiner joiner = new StringJoiner("\n");
		lines.stream().map(FormattedText::getString)
			.forEach(s -> joiner.add(s));
		
		return joiner.toString();
	}
}
