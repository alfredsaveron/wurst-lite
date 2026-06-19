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
	
	private final String buttonText = "Reset";
	private final int buttonWidth = TR.width(buttonText) + 4;
	
	public ResetGuiButton()
	{
		setWidth(getDefaultWidth());
		setHeight(getDefaultHeight());
	}
	
	@Override
	public void handleMouseClick(double mouseX, double mouseY, int mouseButton)
	{
		if(mouseButton != GLFW.GLFW_MOUSE_BUTTON_LEFT)
			return;
		
		if(mouseX < getX() + getWidth() - buttonWidth - 4)
			return;
		
		GUI.resetGui();
	}
	
	@Override
	public void render(GuiGraphics context, int mouseX, int mouseY,
		float partialTicks)
	{
		int x1 = getX();
		int x2 = x1 + getWidth();
		int x3 = x2 - buttonWidth - 4;
		int y1 = getY();
		int y2 = y1 + getHeight();
		
		boolean hovering = isHovering(mouseX, mouseY);
		boolean hBox = hovering && mouseX >= x3;
		
		context.fill(x1, y1, x3, y2, getFillColor(false));
		context.fill(x3, y1, x2, y2, getFillColor(hBox));
		
		int outlineColor = RenderUtils.toIntColor(GUI.getAcColor(), 0.5F);
		RenderUtils.drawBorder2D(context, x3, y1, x2, y2, outlineColor);
		
		int txtColor = GUI.getTxtColor();
		context.drawString(TR, "Reset GUI Layout", x1, y1 + 2, txtColor, false);
		context.drawString(TR, buttonText, x3 + 2, y1 + 2, txtColor, false);
	}
	
	private int getFillColor(boolean hovering)
	{
		float opacity = GUI.getOpacity() * (hovering ? 1.5F : 1);
		return RenderUtils.toIntColor(GUI.getBgColor(), opacity);
	}
	
	@Override
	public int getDefaultWidth()
	{
		return TR.width("Reset GUI Layout") + buttonWidth + 8;
	}
	
	@Override
	public int getDefaultHeight()
	{
		return 11;
	}
}
