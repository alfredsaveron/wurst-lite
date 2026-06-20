/*
 * Copyright (c) 2014-2026 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.hacks;

import net.wurstclient.DontBlock;
import net.wurstclient.SearchTags;
import net.wurstclient.clickgui.screens.ClickGuiScreen;
import net.wurstclient.hack.DontSaveState;
import net.wurstclient.hack.Hack;
import net.wurstclient.settings.SliderSetting;
import net.wurstclient.settings.SliderSetting.ValueDisplay;

@DontSaveState
@DontBlock
@SearchTags({"click gui", "WindowGUI", "window gui", "HackMenu", "hack menu"})
public final class ClickGuiHack extends Hack
{
	private final SliderSetting ttOpacity = new SliderSetting("Tooltip opacity",
		"Opacity of tooltips", 0.75, 0.15, 1, 0.01, ValueDisplay.PERCENTAGE);
	
	public ClickGuiHack()
	{
		super("ClickGUI");
		addSetting(ttOpacity);
	}
	
	@Override
	protected void onEnable()
	{
		MC.setScreen(new ClickGuiScreen(WURST.getGui()));
		setEnabled(false);
	}
	
	public float[] getBackgroundColor()
	{
		return new float[]{0.039215686F, 0.039215686F, 0.039215686F};
	}
	
	public int getTextColor()
	{
		return 0xFFF0F0F0;
	}
	
	public float getOpacity()
	{
		return 0.5F;
	}
	
	public float getTooltipOpacity()
	{
		return ttOpacity.getValueF();
	}
	
	public int getMaxHeight()
	{
		return 0;
	}
	
	public int getMaxSettingsHeight()
	{
		return 0;
	}
}
