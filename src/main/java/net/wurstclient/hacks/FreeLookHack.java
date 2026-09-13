/*
 * Copyright (c) 2014-2026 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.hacks;

import org.lwjgl.glfw.GLFW;

import com.mojang.blaze3d.platform.InputConstants;

import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.wurstclient.Category;
import net.wurstclient.SearchTags;
import net.wurstclient.events.UpdateListener;
import net.wurstclient.hack.Hack;
import net.wurstclient.settings.SliderSetting;
import net.wurstclient.settings.SliderSetting.ValueDisplay;

@SearchTags({"free look", "freelook", "perspective", "360 view"})
public final class FreeLookHack extends Hack implements UpdateListener
{
	private final SliderSetting sensitivity =
		new SliderSetting("Sensitivity", 1.0, 0.1, 3.0, 0.05,
			ValueDisplay.DECIMAL);
	private final SliderSetting smoothing =
		new SliderSetting("Smoothing", 0.08, 0.01, 0.5, 0.01,
			ValueDisplay.DECIMAL);
	private final SliderSetting zoomSpeed =
		new SliderSetting("Zoom Speed", 0.025, 0.005, 0.2, 0.005,
			ValueDisplay.DECIMAL);
	private final SliderSetting distance =
		new SliderSetting("Distance", 4.0, 1.0, 20.0, 0.5,
			ValueDisplay.DECIMAL);
	
	private boolean active = false;
	private float yaw = 0.0F;
	private float pitch = 0.0F;
	private float targetYaw = 0.0F;
	private float targetPitch = 0.0F;
	private float currentDistance = 0.0F;
	private CameraType prevCameraType = CameraType.FIRST_PERSON;
	
	public FreeLookHack()
	{
		super("FreeLook");
		setCategory(Category.RENDER);
		addSetting(sensitivity);
		addSetting(smoothing);
		addSetting(zoomSpeed);
		addSetting(distance);
	}
	
	@Override
	public void onEnable()
	{
		EVENTS.add(UpdateListener.class, this);
	}
	
	@Override
	public void onDisable()
	{
		EVENTS.remove(UpdateListener.class, this);
		if(active)
		{
			active = false;
			Minecraft mc = Minecraft.getInstance();
			if(mc.options != null)
				mc.options.setCameraType(prevCameraType);
		}
	}
	
	@Override
	public void onUpdate()
	{
		Minecraft mc = Minecraft.getInstance();
		if(mc.player == null)
		{
			if(active)
			{
				active = false;
				mc.options.setCameraType(prevCameraType);
			}
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
	
	public boolean turn(double y, double x)
	{
		if(!isEnabled() || !active)
			return false;
		
		targetYaw += y * 0.10F * sensitivity.getValueF();
		targetPitch += x * 0.10F * sensitivity.getValueF();
		targetPitch = Mth.clamp(targetPitch, -90.0F, 90.0F);
		return true;
	}
	
	public void interpolate()
	{
		if(isEnabled() && active)
		{
			float smoothFactor = smoothing.getValueF();
			yaw = Mth.lerp(smoothFactor, yaw, targetYaw);
			pitch = Mth.lerp(smoothFactor, pitch, targetPitch);
			currentDistance =
				Mth.lerp(zoomSpeed.getValueF(), currentDistance,
					distance.getValueF());
		}
	}
	
	public boolean isActive()
	{
		return isEnabled() && active;
	}
	
	public float getYaw()
	{
		return yaw;
	}
	
	public float getPitch()
	{
		return pitch;
	}
	
	public float getCurrentDistance()
	{
		return currentDistance;
	}
}
