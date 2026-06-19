/*
 * Copyright (c) 2014-2026 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.clickgui.components;

import org.lwjgl.glfw.GLFW;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.wurstclient.clickgui.ClickGui;
import net.wurstclient.clickgui.Component;
import net.wurstclient.hack.Hack;
import net.wurstclient.util.RenderUtils;

public final class FeatureBindButton extends Component
{
	private static final ClickGui GUI = WURST.getGui();
	private static final Font TR = MC.font;
	
	public static FeatureBindButton currentlyBinding = null;
	
	private final Hack hack;
	
	public FeatureBindButton(Hack hack)
	{
		this.hack = hack;
		setWidth(getDefaultWidth());
		setHeight(getDefaultHeight());
	}
	
	@Override
	public void handleMouseClick(double mouseX, double mouseY, int mouseButton)
	{
		if(mouseButton == GLFW.GLFW_MOUSE_BUTTON_LEFT)
		{
			if(currentlyBinding == this)
				currentlyBinding = null;
			else
				currentlyBinding = this;
		}else if(mouseButton == GLFW.GLFW_MOUSE_BUTTON_RIGHT)
		{
			String boundKey = null;
			for(net.wurstclient.keybinds.Keybind kb : WURST.getKeybinds()
				.getAllKeybinds())
			{
				if(kb.getCommands().equalsIgnoreCase(hack.getName()))
				{
					boundKey = kb.getKey();
					break;
				}
			}
			if(boundKey != null)
				WURST.getKeybinds().remove(boundKey);
			currentlyBinding = null;
		}
	}
	
	public void onKeyCaptured(int keyCode, int scanCode)
	{
		if(keyCode == GLFW.GLFW_KEY_ESCAPE)
		{
			currentlyBinding = null;
			return;
		}
		
		String keyName = com.mojang.blaze3d.platform.InputConstants
			.getKey(keyCode, scanCode).getName();
		
		WURST.getKeybinds().remove(keyName);
		
		String boundKey = null;
		for(net.wurstclient.keybinds.Keybind kb : WURST.getKeybinds()
			.getAllKeybinds())
		{
			if(kb.getCommands().equalsIgnoreCase(hack.getName()))
			{
				boundKey = kb.getKey();
				break;
			}
		}
		if(boundKey != null)
			WURST.getKeybinds().remove(boundKey);
		
		WURST.getKeybinds().add(keyName, hack.getName().toLowerCase());
		currentlyBinding = null;
	}
	
	@Override
	public void render(GuiGraphics context, int mouseX, int mouseY,
		float partialTicks)
	{
		int x1 = getX();
		int x2 = x1 + getWidth();
		int y1 = getY();
		int y2 = y1 + getHeight();
		
		boolean hovering = isHovering(mouseX, mouseY);
		
		context.fill(x1, y1, x2, y2, getFillColor(hovering));
		
		int outlineColor = RenderUtils.toIntColor(GUI.getAcColor(), 0.5F);
		RenderUtils.drawBorder2D(context, x1, y1, x2, y2, outlineColor);
		
		String display;
		if(currentlyBinding == this)
		{
			display = "Bind: Press a key...";
		}else
		{
			String boundKey = "NONE";
			for(net.wurstclient.keybinds.Keybind kb : WURST.getKeybinds()
				.getAllKeybinds())
			{
				if(kb.getCommands().equalsIgnoreCase(hack.getName()))
				{
					boundKey = kb.getKey().replace("key.keyboard.", "")
						.replace("key.mouse.", "").toUpperCase();
					break;
				}
			}
			display = "Bind: " + boundKey;
		}
		
		int txtColor = GUI.getTxtColor();
		context.drawString(TR, display, x1 + 4, y1 + 3, txtColor, false);
	}
	
	private int getFillColor(boolean hovering)
	{
		float opacity = GUI.getOpacity() * (hovering ? 1.5F : 1);
		return RenderUtils.toIntColor(GUI.getBgColor(), opacity);
	}
	
	@Override
	public int getDefaultWidth()
	{
		return 100;
	}
	
	@Override
	public int getDefaultHeight()
	{
		return 14;
	}
}
