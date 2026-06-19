/*
 * Copyright (c) 2014-2026 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.clickgui.screens;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;
import net.wurstclient.clickgui.ClickGui;

public final class ClickGuiScreen extends Screen
{
	private static final ResourceLocation WURST_LOGO_TEXTURE =
		ResourceLocation.fromNamespaceAndPath("wurst", "wurstlite.png");
	
	private final ClickGui gui;
	
	public ClickGuiScreen(ClickGui gui)
	{
		super(net.minecraft.network.chat.Component.literal(""));
		this.gui = gui;
	}
	
	@Override
	public boolean isPauseScreen()
	{
		return false;
	}
	
	@Override
	public void render(GuiGraphics context, int mouseX, int mouseY,
		float partialTicks)
	{
		context.fill(0, 0, this.width, this.height, 0x75000000);
		gui.render(context, mouseX, mouseY, partialTicks);
		
		net.minecraft.client.gui.Font tr = this.minecraft.font;
		context.drawString(tr,
			"Wurst Client by Alexander01998 - Wurst Lite by alfredsaveron", 5,
			this.height - tr.lineHeight - 5, 0x80FFFFFF, false);
		
		if(net.wurstclient.WurstClient.INSTANCE.getOtfs().wurstLogoOtf
			.isShowInGui())
		{
			int logoWidth = 115;
			int logoHeight = 71;
			int x = this.width - logoWidth - 5;
			int y = this.height - logoHeight - 5;
			context.blit(WURST_LOGO_TEXTURE, x, y, logoWidth, logoHeight, 0.0F,
				0.0F, 574, 356, 574, 356);
		}
	}
	
	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers)
	{
		if(keyCode == org.lwjgl.glfw.GLFW.GLFW_KEY_BACKSPACE)
		{
			String current = gui.getSearchQuery();
			if(!current.isEmpty())
				gui.setSearchQuery(current.substring(0, current.length() - 1));
			return true;
		}
		return super.keyPressed(keyCode, scanCode, modifiers);
	}
	
	@Override
	public boolean charTyped(char chr, int modifiers)
	{
		if(chr >= 32 && chr != 127)
		{
			gui.setSearchQuery(gui.getSearchQuery() + chr);
			return true;
		}
		return super.charTyped(chr, modifiers);
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton)
	{
		gui.handleMouseClick((int)mouseX, (int)mouseY, mouseButton);
		return super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int mouseButton)
	{
		gui.handleMouseRelease(mouseX, mouseY, mouseButton);
		return super.mouseReleased(mouseX, mouseY, mouseButton);
	}
	
	@Override
	public boolean mouseScrolled(double mouseX, double mouseY,
		double horizontalAmount, double verticalAmount)
	{
		gui.handleMouseScroll(mouseX, mouseY, verticalAmount);
		return super.mouseScrolled(mouseX, mouseY, horizontalAmount,
			verticalAmount);
	}
}
