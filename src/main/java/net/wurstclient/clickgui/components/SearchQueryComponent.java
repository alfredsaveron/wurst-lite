/*
 * Copyright (c) 2014-2026 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.clickgui.components;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.wurstclient.clickgui.ClickGui;
import net.wurstclient.clickgui.Component;
import net.wurstclient.util.RenderUtils;

public final class SearchQueryComponent extends Component
{
	private static final ClickGui GUI = WURST.getGui();
	private static final Font TR = MC.font;
	
	public SearchQueryComponent()
	{
		setWidth(getDefaultWidth());
		setHeight(getDefaultHeight());
	}
	
	@Override
	public void render(GuiGraphics context, int mouseX, int mouseY,
		float partialTicks)
	{
		int x1 = getX();
		int x2 = x1 + getWidth();
		int y1 = getY();
		int y2 = y1 + getHeight();
		
		context.fill(x1, y1, x2, y2, 0xFF050505);
		RenderUtils.drawBorder2D(context, x1, y1, x2, y2,
			RenderUtils.toIntColor(GUI.getAcColor(), 0.5F));
		
		String query = GUI.getSearchQuery();
		String display = "Search: " + query;
		if(System.currentTimeMillis() / 500 % 2 == 0)
			display += "|";
		
		context.drawString(TR, display, x1 + 4, y1 + 3, GUI.getTxtColor(),
			false);
	}
	
	@Override
	public int getDefaultWidth()
	{
		return 150;
	}
	
	@Override
	public int getDefaultHeight()
	{
		return 14;
	}
}
