/*
 * Copyright (c) 2014-2026 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.wurstclient.WurstClient;
import net.wurstclient.altmanager.screens.AltManagerScreen;

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen
{
	private AbstractWidget realmsButton = null;
	private Button altsButton;
	
	private TitleScreenMixin(WurstClient wurst, Component title)
	{
		super(title);
	}
	
	@Inject(at = @At("RETURN"), method = "createNormalMenuOptions(II)V")
	private void onInitWidgetsNormal(int y, int spacingY, CallbackInfo ci)
	{
		if(!WurstClient.INSTANCE.isEnabled())
			return;
		
		for(AbstractWidget button : Screens.getButtons(this))
		{
			if(!button.getMessage().getString().equals(I18n.get("menu.online")))
				continue;
			
			realmsButton = button;
			break;
		}
		
		if(realmsButton == null)
			throw new IllegalStateException("Couldn't find realms button!");
		
		realmsButton.setWidth(98);
		
		addRenderableWidget(altsButton = Button
			.builder(Component.literal("Alt Manager"),
				b -> minecraft.setScreen(new AltManagerScreen(this,
					WurstClient.INSTANCE.getAltManager())))
			.bounds(width / 2 + 2, realmsButton.getY(), 98, 20).build());
	}
	
	@Inject(at = @At("RETURN"), method = "tick()V")
	private void onTick(CallbackInfo ci)
	{
		if(realmsButton == null || altsButton == null)
			return;
		
		altsButton.setY(realmsButton.getY());
	}
	
	@Inject(at = @At("HEAD"),
		method = "getMultiplayerDisabledReason()Lnet/minecraft/network/chat/Component;",
		cancellable = true)
	private void onGetMultiplayerDisabledText(
		CallbackInfoReturnable<Component> cir)
	{
		cir.setReturnValue(null);
	}
}
