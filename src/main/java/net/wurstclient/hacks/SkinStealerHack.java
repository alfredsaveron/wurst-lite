/*
 * Copyright (c) 2014-2026 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.hacks;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.yggdrasil.ProfileResult;

import net.minecraft.client.resources.PlayerSkin;
import net.wurstclient.Category;
import net.wurstclient.SearchTags;
import net.wurstclient.altmanager.SkinStealer;
import net.wurstclient.events.UpdateListener;
import net.wurstclient.hack.Hack;
import net.wurstclient.settings.TextFieldSetting;

@SearchTags({"skin stealer", "copy skin", "skin"})
public final class SkinStealerHack extends Hack implements UpdateListener
{
	private final TextFieldSetting username = new TextFieldSetting("Username",
		"The username to copy the skin from", "notch");
	
	private PlayerSkin stolenSkin = null;
	private String lastFetched = "";
	
	public SkinStealerHack()
	{
		super("SkinStealer");
		setCategory(Category.RENDER);
		addSetting(username);
	}
	
	@Override
	protected void onEnable()
	{
		EVENTS.add(UpdateListener.class, this);
		updateSkin();
	}
	
	@Override
	protected void onDisable()
	{
		EVENTS.remove(UpdateListener.class, this);
	}
	
	@Override
	public void onUpdate()
	{
		String current = username.getValue();
		if(!current.equalsIgnoreCase(lastFetched))
			updateSkin();
	}
	
	private void updateSkin()
	{
		String name = username.getValue();
		lastFetched = name;
		stolenSkin = null;
		
		if(name.isEmpty())
			return;
		
		CompletableFuture.runAsync(() -> {
			UUID uuid = SkinStealer.getUUIDOrNull(name);
			if(uuid == null)
				return;
			
			ProfileResult result =
				MC.getMinecraftSessionService().fetchProfile(uuid, true);
			if(result == null)
				return;
			
			GameProfile profile = result.profile();
			MC.getSkinManager().getOrLoad(profile).thenAccept(skin -> {
				if(name.equalsIgnoreCase(lastFetched))
					stolenSkin = skin;
			});
		});
	}
	
	public PlayerSkin getStolenSkin()
	{
		return isEnabled() ? stolenSkin : null;
	}
}
