/*
 * Copyright (c) 2014-2026 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.hacks;

import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.AbstractClientPlayer;
import net.wurstclient.Category;
import net.wurstclient.SearchTags;
import net.wurstclient.hack.Hack;
import net.wurstclient.settings.CheckboxSetting;
import net.wurstclient.settings.TextFieldSetting;

@SearchTags({"name protect"})
public final class NameProtectHack extends Hack
{
	private final TextFieldSetting customName =
		new TextFieldSetting("Name", "Name to display", "Me");
	private final CheckboxSetting boldSetting =
		new CheckboxSetting("Bold", false);
	private final CheckboxSetting italicSetting =
		new CheckboxSetting("Italic", true);
	
	public NameProtectHack()
	{
		super("NameProtect");
		setCategory(Category.RENDER);
		addSetting(customName);
		addSetting(boldSetting);
		addSetting(italicSetting);
	}
	
	public String protect(String string)
	{
		if(!isEnabled() || MC.player == null)
			return string;
		
		String me = MC.getUser().getName();
		if(string.contains(me))
		{
			String replacement = customName.getValue();
			String prefix = "";
			if(boldSetting.isChecked())
				prefix += "\u00a7l";
			if(italicSetting.isChecked())
				prefix += "\u00a7o";
			return string.replace(me, prefix + replacement + "\u00a7r");
		}
		
		int i = 0;
		for(PlayerInfo info : MC.player.connection.getOnlinePlayers())
		{
			i++;
			String name =
				info.getProfile().getName().replaceAll("\u00a7(?:\\w|\\d)", "");
			
			if(string.contains(name))
				return string.replace(name, "\u00a7oPlayer" + i + "\u00a7r");
		}
		
		for(AbstractClientPlayer player : MC.level.players())
		{
			i++;
			String name = player.getName().getString();
			
			if(string.contains(name))
				return string.replace(name, "\u00a7oPlayer" + i + "\u00a7r");
		}
		
		return string;
	}
}
