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

import com.mojang.blaze3d.platform.InputConstants;

import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.KeyboardInput;
import net.wurstclient.clickgui.screens.ClickGuiScreen;
import net.wurstclient.mixinterface.IKeyBinding;

@Mixin(KeyboardInput.class)
public class KeyboardInputMixin extends Input
{
	@Inject(at = @At("TAIL"), method = "tick(ZF)V")
	private void onTick(boolean slowDown, float slowDownFactor, CallbackInfo ci)
	{
		Minecraft mc = Minecraft.getInstance();
		if(mc.screen instanceof ClickGuiScreen)
		{
			long window = mc.getWindow().getWindow();
			Options opt = mc.options;
			
			int keyUp = IKeyBinding.get(opt.keyUp).getKey().getValue();
			int keyDown = IKeyBinding.get(opt.keyDown).getKey().getValue();
			int keyLeft = IKeyBinding.get(opt.keyLeft).getKey().getValue();
			int keyRight = IKeyBinding.get(opt.keyRight).getKey().getValue();
			int keyJump = IKeyBinding.get(opt.keyJump).getKey().getValue();
			int keyShift = IKeyBinding.get(opt.keyShift).getKey().getValue();
			
			up = InputConstants.isKeyDown(window, keyUp);
			down = InputConstants.isKeyDown(window, keyDown);
			left = InputConstants.isKeyDown(window, keyLeft);
			right = InputConstants.isKeyDown(window, keyRight);
			jumping = InputConstants.isKeyDown(window, keyJump);
			shiftKeyDown = InputConstants.isKeyDown(window, keyShift);
			
			forwardImpulse = 0.0F;
			if(up)
				forwardImpulse += 1.0F;
			if(down)
				forwardImpulse -= 1.0F;
			
			leftImpulse = 0.0F;
			if(left)
				leftImpulse += 1.0F;
			if(right)
				leftImpulse -= 1.0F;
			
			if(slowDown)
			{
				leftImpulse *= slowDownFactor;
				forwardImpulse *= slowDownFactor;
			}
		}
	}
}
