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
	
	@Inject(at = @At("RETURN"), method = "getSkin()Lnet/minecraft/client/resources/PlayerSkin;", cancellable = true)
	private void onGetSkin(CallbackInfoReturnable<PlayerSkin> cir)
	{
		Minecraft mc = Minecraft.getInstance();
		if(mc.player == null)
			return;
		
		GameProfile profile = getProfile();
		if(profile == null)
			return;
		
		if(profile.getId().equals(mc.player.getUUID()))
		{
			if(WurstClient.INSTANCE.isEnabled() && WurstClient.INSTANCE.getHax() != null)
			{
				ResourceLocation stolenCape = WurstClient.INSTANCE.getHax().capeStealerHack.getStolenCape();
				if(stolenCape != null)
				{
					PlayerSkin original = cir.getReturnValue();
					cir.setReturnValue(new PlayerSkin(
						original.texture(),
						original.textureUrl(),
						stolenCape,
						original.elytraTexture() != null ? original.elytraTexture() : stolenCape,
						original.model(),
						original.secure()
					));
				}
			}
		}
	}
}
