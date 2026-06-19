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
import net.wurstclient.util.RenderUtils;

public final class ResetGuiButton extends Component
{
	private static final ClickGui GUI = WURST.getGui();
	private static final Font TR = MC.font;
	
	public ResetGuiButton()
	{
		setWidth(getDefaultWidth());
		setHeight(getDefaultHeight());
	}
	
	@Override
	public void handleMouseClick(double mouseX, double mouseY, int mouseButton)
	{
		if(mouseButton == GLFW.GLFW_MOUSE_BUTTON_LEFT)
			GUI.resetGui();
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
		
		float opacity = GUI.getOpacity() * (hovering ? 1.5F : 1);
		int color = RenderUtils.toIntColor(GUI.getBgColor(), opacity);
		context.fill(x1, y1, x2, y2, color);
		int outlineColor = RenderUtils.toIntColor(GUI.getAcColor(), 0.5F);
		RenderUtils.drawBorder2D(context, x1, y1, x2, y2, outlineColor);
		
		int txtColor = GUI.getTxtColor();
		String text = "Reset GUI Layout";
		context.drawString(TR, text, x1 + (getWidth() - TR.width(text)) / 2,
			y1 + 2, txtColor, false);
	}
	
	@Override
	public int getDefaultWidth()
	{
		return 100;
	}
	
	@Override
	public int getDefaultHeight()
	{
		return 11;
	}
}
