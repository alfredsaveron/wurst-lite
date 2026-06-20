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
import net.wurstclient.events.ChatInputListener;
import net.wurstclient.hack.DontSaveState;
import net.wurstclient.hack.Hack;
import net.wurstclient.util.ChatUtils;

@SearchTags({"plugin list", "plugins", "server plugins", "pl"})
@DontSaveState
public final class PluginListHack extends Hack implements ChatInputListener
{
	private boolean waiting;
	
	public PluginListHack()
	{
		super("PluginList");
		setCategory(Category.OTHER);
	}
	
	@Override
	protected void onEnable()
	{
		if(MC.getConnection() == null || MC.player == null)
		{
			ChatUtils.error("You must be connected to a server.");
			setEnabled(false);
			return;
		}
		
		if(MC.isLocalServer())
		{
			ChatUtils.error("This only works on multiplayer servers.");
			setEnabled(false);
			return;
		}
		
		waiting = true;
		EVENTS.add(ChatInputListener.class, this);
		MC.getConnection().sendCommand("pl");
		
		ChatUtils.message("Requesting plugin list...");
	}
	
	@Override
	protected void onDisable()
	{
		EVENTS.remove(ChatInputListener.class, this);
		waiting = false;
	}
	
	@Override
	public void onReceivedMessage(ChatInputEvent event)
	{
		if(!waiting)
			return;
		
		String msg = event.getComponent().getString();
		
		if(msg.contains("Plugins") && msg.contains(":"))
		{
			waiting = false;
			setEnabled(false);
			return;
		}
		
		if(msg.toLowerCase().contains("unknown command")
			|| msg.toLowerCase().contains("permission"))
		{
			ChatUtils.error("Server denied the plugin list request.");
			waiting = false;
			setEnabled(false);
		}
	}
	
	@Override
	public String getDescription()
	{
		return "Lists server plugins by sending /pl.";
	}
}
