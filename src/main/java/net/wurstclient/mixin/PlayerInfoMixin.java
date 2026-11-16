/*
 * Copyright (c) 2014-2026 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.mojang.authlib.GameProfile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.resources.ResourceLocation;
import net.wurstclient.WurstClient;

@Mixin(PlayerInfo.class)
public abstract class PlayerInfoMixin
{
	@Shadow
	public abstract GameProfile getProfile();
	
	@Inject(at = @At("RETURN"),
		method = "getSkin()Lnet/minecraft/client/resources/PlayerSkin;",
		cancellable = true)
	private void onGetSkin(CallbackInfoReturnable<PlayerSkin> cir)
	{
		Minecraft mc = Minecraft.getInstance();
		GameProfile profile = getProfile();
		if(profile == null)
			return;
		
		if(profile.getId().equals(mc.getUser().getProfileId()))
		{
			if(WurstClient.INSTANCE.isEnabled()
				&& WurstClient.INSTANCE.getHax() != null)
			{
				PlayerSkin current = cir.getReturnValue();
				PlayerSkin stolenSkin =
					WurstClient.INSTANCE.getHax().skinStealerHack
						.getStolenSkin();
				ResourceLocation stolenCape =
					WurstClient.INSTANCE.getHax().capeStealerHack
						.getStolenCape();
				
				if(stolenSkin != null || stolenCape != null)
				{
					ResourceLocation skinTexture = stolenSkin != null
						? stolenSkin.texture() : current.texture();
					String skinUrl = stolenSkin != null
						? stolenSkin.textureUrl() : current.textureUrl();
					PlayerSkin.Model model = stolenSkin != null
						? stolenSkin.model() : current.model();
					
					ResourceLocation capeTexture =
						stolenCape != null ? stolenCape : current.capeTexture();
					ResourceLocation elytraTexture = stolenCape != null
						? (current.elytraTexture() != null
							? current.elytraTexture() : stolenCape)
						: current.elytraTexture();
					
					cir.setReturnValue(new PlayerSkin(skinTexture, skinUrl,
						capeTexture, elytraTexture, model, stolenSkin != null
							? stolenSkin.secure() : current.secure()));
				}
			}
		}
	}
}
