/*
 * Copyright (c) 2014-2026 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.hacks;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.wurstclient.Category;
import net.wurstclient.SearchTags;
import net.wurstclient.events.RenderListener;
import net.wurstclient.events.RightClickListener;
import net.wurstclient.events.UpdateListener;
import net.wurstclient.hack.Hack;
import net.wurstclient.settings.CheckboxSetting;
import net.wurstclient.settings.SliderSetting;
import net.wurstclient.settings.SliderSetting.ValueDisplay;
import net.wurstclient.util.InteractionSimulator;
import net.wurstclient.util.RenderUtils;

@SearchTags({"air place"})
public final class AirPlaceHack extends Hack
	implements RightClickListener, UpdateListener, RenderListener
{
	private static final int COLOR = 0xA273A6;
	
	private final SliderSetting range =
		new SliderSetting("Range", 5, 1, 6, 0.05, ValueDisplay.DECIMAL);
	
	private final CheckboxSetting guide = new CheckboxSetting("Guide",
		"description.wurst.setting.airplace.guide", true);
	
	private BlockPos renderPos;
	
	public AirPlaceHack()
	{
		super("AirPlace");
		setCategory(Category.BLOCKS);
		addSetting(range);
		addSetting(guide);
	}
	
	@Override
	protected void onEnable()
	{
		EVENTS.add(UpdateListener.class, this);
		EVENTS.add(RenderListener.class, this);
		EVENTS.add(RightClickListener.class, this);
		renderPos = null;
	}
	
	@Override
	protected void onDisable()
	{
		EVENTS.remove(UpdateListener.class, this);
		EVENTS.remove(RenderListener.class, this);
		EVENTS.remove(RightClickListener.class, this);
	}
	
	@Override
	public void onRightClick(RightClickEvent event)
	{
		BlockHitResult hitResult = getHitResultIfMissed();
		if(hitResult == null)
			return;
		
		MC.rightClickDelay = 4;
		if(MC.player.isHandsBusy())
			return;
		
		InteractionSimulator.rightClickBlock(hitResult);
		event.cancel();
	}
	
	@Override
	public void onUpdate()
	{
		renderPos = null;
		
		if(!guide.isChecked())
			return;
		
		if(MC.player.getMainHandItem().isEmpty()
			&& MC.player.getOffhandItem().isEmpty())
			return;
		
		if(MC.player.isHandsBusy())
			return;
		
		BlockHitResult hitResult = getHitResultIfMissed();
		if(hitResult != null)
			renderPos = hitResult.getBlockPos();
	}
	
	private BlockHitResult getHitResultIfMissed()
	{
		HitResult hitResult = MC.player.pick(range.getValue(), 0, false);
		if(hitResult.getType() != HitResult.Type.MISS)
			return null;
		
		if(!(hitResult instanceof BlockHitResult blockHitResult))
			return null;
		
		return blockHitResult;
	}
	
	@Override
	public void onRender(PoseStack matrixStack, float partialTicks)
	{
		if(renderPos == null)
			return;
		
		AABB box = new AABB(renderPos);
		
		int quadColor = (0x1A << 24) | COLOR;
		RenderUtils.drawSolidBox(matrixStack, box, quadColor, false);
		
		int lineColor = (0xC0 << 24) | COLOR;
		RenderUtils.drawOutlinedBox(matrixStack, box, lineColor, false);
	}
}
