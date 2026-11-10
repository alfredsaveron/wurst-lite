/*
 * Copyright (c) 2014-2026 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.hacks;

import net.wurstclient.Category;
import net.wurstclient.SearchTags;
import net.wurstclient.hack.DontSaveState;
import net.wurstclient.hack.Hack;
import net.wurstclient.settings.SignTextSetting;

@SearchTags({"auto sign"})
@DontSaveState
public final class AutoSignHack extends Hack
{
	private final SignTextSetting signText = new SignTextSetting("Sign Text",
		"Text to automatically write on signs.",
		new String[]{"", "", "", ""});
	
	public AutoSignHack()
	{
		super("AutoSign");
		setCategory(Category.BLOCKS);
		addSetting(signText);
	}
	
	public String[] getSignText()
	{
		if(!isEnabled())
			return null;
		
		if(signText.isEmpty())
			return null;
		
		return signText.getLines();
	}
	
	public void setSignText(String[] signText)
	{
		if(isEnabled() && this.signText.isEmpty())
			this.signText.setLines(signText);
	}
	
	public SignTextSetting getSignTextSetting()
	{
		return signText;
	}
}
