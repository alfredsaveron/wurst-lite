/*
 * Copyright (c) 2014-2026 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.hacks;

import java.util.Arrays;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.EmptyBlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.wurstclient.Category;
import net.wurstclient.SearchTags;
import net.wurstclient.events.UpdateListener;
import net.wurstclient.hack.Hack;
import net.wurstclient.mixinterface.IKeyBinding;
import net.wurstclient.settings.CheckboxSetting;
import net.wurstclient.settings.SliderSetting;
import net.wurstclient.settings.SliderSetting.ValueDisplay;
import net.wurstclient.util.BlockUtils;
import net.wurstclient.util.RotationUtils;

@SearchTags({"scaffold walk", "BridgeWalk", "bridge walk", "AutoBridge",
	"auto bridge", "tower"})
public final class ScaffoldWalkHack extends Hack implements UpdateListener
{
	private final CheckboxSetting legitMode = new CheckboxSetting("Legit mode",
		"Sneaks at edges to look like a legit fast-bridger.", true);
	
	private final SliderSetting unsneakDelay =
		new SliderSetting("Unsneak delay",
			"The number of ticks to stay unsneaked after placing a block.", 3,
			1, 10, 1, ValueDisplay.INTEGER.withSuffix(" ticks"));
	
	private boolean sneaking;
	private int unsneakTicks;
	
	public ScaffoldWalkHack()
	{
		super("ScaffoldWalk");
		setCategory(Category.BLOCKS);
		addSetting(legitMode);
		addSetting(unsneakDelay);
	}
	
	@Override
	protected void onEnable()
	{
		EVENTS.add(UpdateListener.class, this);
		sneaking = false;
		unsneakTicks = 0;
	}
	
	@Override
	protected void onDisable()
	{
		EVENTS.remove(UpdateListener.class, this);
		if(sneaking)
			setSneaking(false);
		unsneakTicks = 0;
	}
	
	public boolean isLegitEnabled()
	{
		return isEnabled() && legitMode.isChecked();
	}
	
	private void setSneaking(boolean sneaking)
	{
		IKeyBinding sneakKey = IKeyBinding.get(MC.options.keyShift);
		if(sneaking)
			sneakKey.setDown(true);
		else
			sneakKey.resetPressedState();
		this.sneaking = sneaking;
	}
	
	@Override
	public void onUpdate()
	{
		if(!legitMode.isChecked())
		{
			if(sneaking)
				setSneaking(false);
			unsneakTicks = 0;
		}
		
		if(unsneakTicks > 0)
		{
			unsneakTicks--;
			setSneaking(false);
			return;
		}
		
		BlockPos belowPlayer =
			BlockPos.containing(MC.player.position()).below();
		
		boolean nearEdge = false;
		if(legitMode.isChecked() && MC.player.onGround())
		{
			AABB box = MC.player.getBoundingBox();
			AABB adjustedBox = box.expandTowards(0, -MC.player.maxUpStep(), 0)
				.inflate(-0.05, 0, -0.05);
			if(MC.level.noCollision(MC.player, adjustedBox))
				nearEdge = true;
		}
		
		if(nearEdge)
			setSneaking(true);
		else if(sneaking)
			setSneaking(false);
		
		if(!BlockUtils.getState(belowPlayer).canBeReplaced())
			return;
		
		int newSlot = -1;
		for(int i = 0; i < 9; i++)
		{
			ItemStack stack = MC.player.getInventory().getItem(i);
			if(stack.isEmpty() || !(stack.getItem() instanceof BlockItem))
				continue;
			
			Block block = Block.byItem(stack.getItem());
			BlockState state = block.defaultBlockState();
			if(!state.isCollisionShapeFullBlock(EmptyBlockGetter.INSTANCE,
				BlockPos.ZERO))
				continue;
			
			if(block instanceof FallingBlock && FallingBlock
				.isFree(BlockUtils.getState(belowPlayer.below())))
				continue;
			
			newSlot = i;
			break;
		}
		
		if(newSlot == -1)
			return;
		
		int oldSlot = MC.player.getInventory().selected;
		MC.player.getInventory().selected = newSlot;
		
		boolean placed = false;
		if(placeBlock(belowPlayer))
			placed = true;
		else
		{
			Direction[] sides = Direction.values();
			for(Direction side : sides)
			{
				BlockPos neighbor = belowPlayer.relative(side);
				if(placeBlock(neighbor))
				{
					placed = true;
					break;
				}
			}
			if(!placed)
			{
				for(Direction side : sides)
					for(Direction side2 : Arrays.copyOfRange(sides,
						side.ordinal(), 6))
					{
						if(side.getOpposite().equals(side2))
							continue;
						
						BlockPos neighbor =
							belowPlayer.relative(side).relative(side2);
						if(placeBlock(neighbor))
						{
							placed = true;
							break;
						}
					}
			}
		}
		
		MC.player.getInventory().selected = oldSlot;
		
		if(placed && legitMode.isChecked())
		{
			unsneakTicks = unsneakDelay.getValueI();
			setSneaking(false);
		}
	}
	
	private boolean placeBlock(BlockPos pos)
	{
		Vec3 eyesPos = RotationUtils.getEyesPos();
		
		for(Direction side : Direction.values())
		{
			BlockPos neighbor = pos.relative(side);
			Direction side2 = side.getOpposite();
			
			if(eyesPos.distanceToSqr(Vec3.atCenterOf(pos)) >= eyesPos
				.distanceToSqr(Vec3.atCenterOf(neighbor)))
				continue;
			
			if(!BlockUtils.canBeClicked(neighbor))
				continue;
			
			Vec3 hitVec = Vec3.atCenterOf(neighbor)
				.add(Vec3.atLowerCornerOf(side2.getNormal()).scale(0.5));
			
			if(eyesPos.distanceToSqr(hitVec) > 18.0625)
				continue;
			
			RotationUtils.getNeededRotations(hitVec).sendPlayerLookPacket();
			
			IMC.getInteractionManager().rightClickBlock(neighbor, side2,
				hitVec);
			MC.player.swing(InteractionHand.MAIN_HAND);
			MC.rightClickDelay = 4;
			
			return true;
		}
		
		return false;
	}
}
