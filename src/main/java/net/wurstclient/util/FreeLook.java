/*
 * Copyright (c) 2014-2026 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.util;

import org.lwjgl.glfw.GLFW;

import com.mojang.blaze3d.platform.InputConstants;

import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;

public enum FreeLook
{
	;
	
	public static boolean active = false;
	public static float yaw = 0.0F;
	public static float pitch = 0.0F;
	public static float targetYaw = 0.0F;
	public static float targetPitch = 0.0F;
	public static float currentDistance = 0.0F;
	private static CameraType prevCameraType = CameraType.FIRST_PERSON;
	
	public static void tick()
	{
		Minecraft mc = Minecraft.getInstance();
		if(mc.player == null)
		{
			active = false;
			return;
		}
		
		boolean isPressed = InputConstants.isKeyDown(mc.getWindow().getWindow(),
			GLFW.GLFW_KEY_LEFT_ALT);
		if(isPressed)
		{
			if(!active)
			{
				active = true;
				prevCameraType = mc.options.getCameraType();
				mc.options.setCameraType(CameraType.THIRD_PERSON_BACK);
				yaw = targetYaw = mc.player.getYRot();
				pitch = targetPitch = mc.player.getXRot();
				currentDistance = 0.0F;
			}
		}else
		{
			if(active)
			{
				active = false;
				mc.options.setCameraType(prevCameraType);
			}
		}
	}
	
	public static boolean turn(double y, double x)
	{
		if(!active)
			return false;
		
		targetYaw += y * 0.15F;
		targetPitch += x * 0.15F;
		targetPitch = Mth.clamp(targetPitch, -90.0F, 90.0F);
		return true;
	}
	
	public static void interpolate()
	{
		if(active)
		{
			yaw = Mth.lerp(0.12F, yaw, targetYaw);
			pitch = Mth.lerp(0.12F, pitch, targetPitch);
			currentDistance = Mth.lerp(0.08F, currentDistance, 4.0F);
		}
	}
}
