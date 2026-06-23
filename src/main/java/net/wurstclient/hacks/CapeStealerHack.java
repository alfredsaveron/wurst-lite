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
import net.minecraft.resources.ResourceLocation;
import net.wurstclient.Category;
import net.wurstclient.SearchTags;
import net.wurstclient.hack.Hack;
import net.wurstclient.settings.TextFieldSetting;
import net.wurstclient.altmanager.SkinStealer;
import net.wurstclient.events.UpdateListener;

@SearchTags({"cape stealer", "copy cape", "cape"})
public final class CapeStealerHack extends Hack implements UpdateListener
{
	private final TextFieldSetting username = new TextFieldSetting("Username",
		"The username to copy the cape from", "notch");
	
	private ResourceLocation stolenCape = null;
	private String lastFetched = "";
	
	public CapeStealerHack()
	{
		super("CapeStealer");
		setCategory(Category.RENDER);
		addSetting(username);
	}
	
	@Override
	protected void onEnable()
	{
		EVENTS.add(UpdateListener.class, this);
		updateCape();
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
			updateCape();
	}
	
	private void updateCape()
	{
		String name = username.getValue();
		lastFetched = name;
		stolenCape = null;
		
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
					stolenCape = skin.capeTexture();
			});
		});
	}
	
	public ResourceLocation getStolenCape()
	{
		return isEnabled() ? stolenCape : null;
	}
}
