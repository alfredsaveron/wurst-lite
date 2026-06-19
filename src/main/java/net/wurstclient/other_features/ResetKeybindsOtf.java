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
import net.wurstclient.util.ChatUtils;

@SearchTags({"reset keybinds", "reset binds", "clear keybinds", "clear binds"})
@DontBlock
public final class ResetKeybindsOtf extends OtherFeature
{
	public ResetKeybindsOtf()
	{
		super("Reset Keybinds",
			"Resets all custom keybinds to default (empty).");
	}
	
	@Override
	public String getPrimaryAction()
	{
		return "Reset";
	}
	
	@Override
	public void doPrimaryAction()
	{
		WURST.getKeybinds().removeAll();
		ChatUtils.message("Reset all keybinds.");
	}
}
