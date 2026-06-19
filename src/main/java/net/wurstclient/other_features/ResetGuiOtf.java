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

@SearchTags({"reset gui layout", "reset gui", "reset layout"})
@DontBlock
public final class ResetGuiOtf extends OtherFeature
{
	public ResetGuiOtf()
	{
		super("Reset GUI Layout",
			"Resets the position of all ClickGUI windows to their default layout.");
	}
	
	@Override
	public String getPrimaryAction()
	{
		return "Reset";
	}
	
	@Override
	public void doPrimaryAction()
	{
		WURST.getGui().resetGui();
	}
}
