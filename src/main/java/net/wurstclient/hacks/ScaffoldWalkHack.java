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
import net.minecraft.world.phys.Vec3;
import net.wurstclient.Category;
import net.wurstclient.SearchTags;
import net.wurstclient.events.UpdateListener;
import net.wurstclient.hack.Hack;
import net.wurstclient.mixinterface.IKeyBinding;
import net.wurstclient.settings.CheckboxSetting;
import net.wurstclient.util.BlockUtils;
import net.wurstclient.util.RotationUtils;

@SearchTags({"scaffold walk", "BridgeWalk", "bridge walk", "AutoBridge",
	"auto bridge", "tower"})
public final class ScaffoldWalkHack extends Hack implements UpdateListener
{
	private final CheckboxSetting legitMode = new CheckboxSetting("Legit mode",
		"Sneaks at edges to look like a legit fast-bridger.", true);
	
	private boolean sneaking;
	
	public ScaffoldWalkHack()
	{
		super("ScaffoldWalk");
		setCategory(Category.BLOCKS);
		addSetting(legitMode);
	}
	
	@Override
	protected void onEnable()
	{
		EVENTS.add(UpdateListener.class, this);
		sneaking = false;
	}
	
	@Override
	protected void onDisable()
	{
		EVENTS.remove(UpdateListener.class, this);
		if(sneaking)
			setSneaking(false);
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
		BlockPos belowPlayer =
			BlockPos.containing(MC.player.position()).below();
		
		if(!BlockUtils.getState(belowPlayer).canBeReplaced())
		{
			if(sneaking)
				setSneaking(false);
			return;
		}
		
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
		{
			if(sneaking)
				setSneaking(false);
			return;
		}
		
		boolean shouldSneak = legitMode.isChecked() && MC.player.onGround();
		if(shouldSneak)
			setSneaking(true);
		else if(sneaking)
			setSneaking(false);
		
		int oldSlot = MC.player.getInventory().selected;
		MC.player.getInventory().selected = newSlot;
		
		scaffoldTo(belowPlayer);
		
		MC.player.getInventory().selected = oldSlot;
	}
	
	private void scaffoldTo(BlockPos belowPlayer)
	{
		if(placeBlock(belowPlayer))
			return;
		
		Direction[] sides = Direction.values();
		for(Direction side : sides)
		{
			BlockPos neighbor = belowPlayer.relative(side);
			if(placeBlock(neighbor))
				return;
		}
		
		for(Direction side : sides)
			for(Direction side2 : Arrays.copyOfRange(sides, side.ordinal(), 6))
			{
				if(side.getOpposite().equals(side2))
					continue;
				
				BlockPos neighbor = belowPlayer.relative(side).relative(side2);
				if(placeBlock(neighbor))
					return;
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
