/*
 * Copyright (c) 2014-2026 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.clickgui.components;

import java.util.Objects;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.wurstclient.Feature;
import net.wurstclient.clickgui.ClickGui;
import net.wurstclient.clickgui.Component;
import net.wurstclient.clickgui.SettingsWindow;
import net.wurstclient.clickgui.Window;
import net.wurstclient.hacks.TooManyHaxHack;
import net.wurstclient.util.ChatUtils;

public final class FeatureButton extends Component
{
	private static final ClickGui GUI = WURST.getGui();
	private static final Font TR = MC.font;
	
	private final Feature feature;
	private final boolean hasSettings;
	
	private Window settingsWindow;
	
	public FeatureButton(Feature feature)
	{
		this.feature = Objects.requireNonNull(feature);
		setWidth(getDefaultWidth());
		setHeight(getDefaultHeight());
		hasSettings = !feature.getSettings().isEmpty()
			|| (feature instanceof net.wurstclient.hack.Hack);
	}
	
	@Override
	public void handleMouseClick(double mouseX, double mouseY, int mouseButton)
	{
		if(mouseButton != 0)
			return;
		
		if(hasSettings && (mouseX > getX() + getWidth() - 14
			|| feature.getPrimaryAction().isEmpty()))
		{
			toggleSettingsWindow();
			return;
		}
		
		TooManyHaxHack tooManyHax = WURST.getHax().tooManyHaxHack;
		if(tooManyHax.isEnabled() && tooManyHax.isBlocked(feature))
		{
			ChatUtils.error(feature.getName() + " is blocked by TooManyHax.");
			return;
		}
		
		feature.doPrimaryAction();
	}
	
	private boolean isSettingsWindowOpen()
	{
		return settingsWindow != null && !settingsWindow.isClosing();
	}
	
	private void toggleSettingsWindow()
	{
		if(!isSettingsWindowOpen())
		{
			settingsWindow = new SettingsWindow(feature, getParent(), getY());
			GUI.addWindow(settingsWindow);
			
		}else
		{
			settingsWindow.close();
			settingsWindow = null;
		}
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
		boolean hSettings = hovering && mouseX >= x2 - 14;
		boolean hFeature = hovering && !hSettings;
		
		if(hFeature)
			GUI.setTooltip(feature.getWrappedDescription(200));
		
		String query = GUI.getSearchQuery().trim().toLowerCase();
		boolean matched =
			!query.isEmpty() && (feature.getName().toLowerCase().contains(query)
				|| feature.getSearchTags().toLowerCase().contains(query));
		
		if(matched)
		{
			float pulse =
				(float)(Math.sin(System.currentTimeMillis() / 150.0) * 0.5
					+ 0.5);
			int highlightColor = ((int)(0x15 + pulse * 0x30) << 24) | 0xA273A6;
			context.fill(x1, y1, x2, y2, highlightColor);
		}
		
		if(hovering)
			context.fill(x1, y1, x2, y2, 0x1AFFFFFF);
		
		int textColor = feature.isEnabled() ? 0xFFD8A8DC
			: (hovering ? 0xFFFFFFFF : 0xFFB0B0B0);
		
		String name = feature.getName();
		context.drawString(TR, ClickGui.modern(name), x1 + 6, y1 + 3, textColor,
			false);
		
		if(hasSettings)
		{
			int dotColor = feature.isEnabled() ? 0xFFD8A8DC
				: (isSettingsWindowOpen() || hSettings ? 0xFFFFFFFF
					: 0xFF8E9094);
			int dotX = x2 - 8;
			int centerY = y1 + 7;
			context.fill(dotX, centerY - 4, dotX + 2, centerY - 2, dotColor);
			context.fill(dotX, centerY - 1, dotX + 2, centerY + 1, dotColor);
			context.fill(dotX, centerY + 2, dotX + 2, centerY + 4, dotColor);
		}
	}
	
	@Override
	public int getDefaultWidth()
	{
		int width = TR.width(feature.getName());
		width += hasSettings ? 20 : 12;
		return width;
	}
	
	@Override
	public int getDefaultHeight()
	{
		return 14;
	}
}
