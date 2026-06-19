/*
 * Copyright (c) 2014-2026 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.other_features;

import net.wurstclient.DontBlock;
import net.wurstclient.SearchTags;
import net.wurstclient.other_feature.OtherFeature;
import net.wurstclient.settings.CheckboxSetting;

@SearchTags({"wurst logo", "top left corner"})
@DontBlock
public final class WurstLogoOtf extends OtherFeature
{
	private final CheckboxSetting showInGui = new CheckboxSetting("Show in GUI",
		"Shows the logo in the bottom-right corner when WurstLite menu is open.",
		true);
	
	public WurstLogoOtf()
	{
		super("WurstLite Logo",
			"Shows the WurstLite logo and version on the screen.");
		addSetting(showInGui);
	}
	
	public boolean isVisible()
	{
		return true;
	}
	
	public boolean isShowInGui()
	{
		return showInGui.isChecked();
	}
	
	public int getBackgroundColor()
	{
		return 0x80A273A6;
	}
	
	public int getTextColor()
	{
		return 0xFFFFFFFF;
	}
}
