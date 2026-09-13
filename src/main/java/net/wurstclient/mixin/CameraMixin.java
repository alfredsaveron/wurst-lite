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
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.Camera;
import net.minecraft.world.level.material.FogType;
import net.wurstclient.WurstClient;
import net.wurstclient.hacks.CameraDistanceHack;

@Mixin(Camera.class)
public abstract class CameraMixin
{
	@Shadow
	private float yRot;
	
	@Shadow
	private float xRot;
	
	@Inject(at = @At("HEAD"),
		method = "setup(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/world/entity/Entity;ZZF)V")
	private void onSetupHead(net.minecraft.world.level.BlockGetter area,
		net.minecraft.world.entity.Entity focusedEntity, boolean thirdPerson,
		boolean inverseView, float tickDelta, CallbackInfo ci)
	{
		WurstClient.INSTANCE.getHax().freeLookHack.interpolate();
	}
	
	@ModifyVariable(method = "setRotation(FF)V",
		at = @At("HEAD"),
		ordinal = 0,
		argsOnly = true)
	private float modifyYRot(float yRot)
	{
		return WurstClient.INSTANCE.getHax().freeLookHack.isActive()
			? WurstClient.INSTANCE.getHax().freeLookHack.getYaw() : yRot;
	}
	
	@ModifyVariable(method = "setRotation(FF)V",
		at = @At("HEAD"),
		ordinal = 1,
		argsOnly = true)
	private float modifyXRot(float xRot)
	{
		return WurstClient.INSTANCE.getHax().freeLookHack.isActive()
			? WurstClient.INSTANCE.getHax().freeLookHack.getPitch() : xRot;
	}
	
	@ModifyVariable(at = @At("HEAD"),
		method = "getMaxZoom(F)F",
		argsOnly = true)
	private float changeClipToSpaceDistance(float desiredCameraDistance)
	{
		if(WurstClient.INSTANCE.getHax().freeLookHack.isActive())
			return WurstClient.INSTANCE.getHax().freeLookHack
				.getCurrentDistance();
		
		CameraDistanceHack cameraDistance =
			WurstClient.INSTANCE.getHax().cameraDistanceHack;
		if(cameraDistance.isEnabled())
			return cameraDistance.getDistance();
		
		return desiredCameraDistance;
	}
	
	@Inject(at = @At("HEAD"), method = "getMaxZoom(F)F", cancellable = true)
	private void onClipToSpace(float desiredCameraDistance,
		CallbackInfoReturnable<Float> cir)
	{
		if(WurstClient.INSTANCE.getHax().cameraNoClipHack.isEnabled())
			cir.setReturnValue(desiredCameraDistance);
	}
	
	@Inject(at = @At("HEAD"),
		method = "getFluidInCamera()Lnet/minecraft/world/level/material/FogType;",
		cancellable = true)
	private void onGetSubmersionType(CallbackInfoReturnable<FogType> cir)
	{
		if(WurstClient.INSTANCE.getHax().noOverlayHack.isEnabled())
			cir.setReturnValue(FogType.NONE);
	}
}
