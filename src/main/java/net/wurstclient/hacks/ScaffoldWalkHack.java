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
import net.wurstclient.util.Rotation;
import net.wurstclient.util.RotationUtils;

@SearchTags({"scaffold walk", "BridgeWalk", "bridge walk", "AutoBridge",
	"auto bridge", "tower"})
public final class ScaffoldWalkHack extends Hack implements UpdateListener
{
	private final CheckboxSetting legitMode = new CheckboxSetting("Legit mode",
		"Sneaks at edges to look like a legit fast-bridger.", true);
	
	private final SliderSetting edgeDistance = new SliderSetting(
		"Sneak edge distance",
		"How close ScaffoldWalk will let you get to the edge before sneaking.",
		0.05, 0.05, 0.25, 0.001, ValueDisplay.DECIMAL.withSuffix("m"));
	
	private boolean sneaking;
	
	public ScaffoldWalkHack()
	{
		super("ScaffoldWalk");
		setCategory(Category.BLOCKS);
		addSetting(legitMode);
		addSetting(edgeDistance);
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
		
		boolean nearEdge = false;
		if(legitMode.isChecked() && MC.player.onGround())
		{
			AABB box = MC.player.getBoundingBox();
			AABB adjustedBox = box.expandTowards(0, -MC.player.maxUpStep(), 0)
				.inflate(-edgeDistance.getValue(), 0, -edgeDistance.getValue());
			if(MC.level.noCollision(MC.player, adjustedBox))
				nearEdge = true;
		}
		
		if(nearEdge)
			setSneaking(true);
		else if(sneaking)
			setSneaking(false);
		
		Vec3 velocity = MC.player.getDeltaMovement();
		double dx = velocity.x;
		double dz = velocity.z;
		if(Math.abs(dx) < 0.01 && Math.abs(dz) < 0.01)
		{
			Direction facing = MC.player.getDirection();
			dx = -facing.getStepX();
			dz = -facing.getStepZ();
		}
		Direction moveDir = Direction.getNearest(dx, 0.0, dz);
		
		BlockPos targetPos = BlockUtils.getState(belowPlayer).canBeReplaced()
			? belowPlayer : belowPlayer.relative(moveDir);
		Vec3 targetHitVec = getHitVec(targetPos);
		
		if(targetHitVec != null)
		{
			Rotation rotation = RotationUtils.getNeededRotations(targetHitVec);
			rotation.sendPlayerLookPacket();
			rotation.applyToClientPlayer();
		}
		
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
		
		scaffoldTo(belowPlayer);
		
		MC.player.getInventory().selected = oldSlot;
	}
	
	private void scaffoldTo(BlockPos belowPlayer)
	{
		if(placeBlock(belowPlayer))
			return;
		
		Vec3 velocity = MC.player.getDeltaMovement();
		double dx = velocity.x;
		double dz = velocity.z;
		if(Math.abs(dx) < 0.01 && Math.abs(dz) < 0.01)
		{
			Direction facing = MC.player.getDirection();
			dx = -facing.getStepX();
			dz = -facing.getStepZ();
		}
		
		Direction[] horizontalSides =
			{Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST};
		final double finalDx = dx;
		final double finalDz = dz;
		Arrays.sort(horizontalSides, (d1, d2) -> {
			double dot1 = d1.getStepX() * finalDx + d1.getStepZ() * finalDz;
			double dot2 = d2.getStepX() * finalDx + d2.getStepZ() * finalDz;
			return Double.compare(dot2, dot1);
		});
		
		for(Direction side : horizontalSides)
		{
			BlockPos neighbor = belowPlayer.relative(side);
			if(placeBlock(neighbor))
				return;
		}
		
		for(int i = 0; i < horizontalSides.length; i++)
			for(int j = i + 1; j < horizontalSides.length; j++)
			{
				Direction d1 = horizontalSides[i];
				Direction d2 = horizontalSides[j];
				if(d1.getOpposite().equals(d2))
					continue;
				
				BlockPos neighbor = belowPlayer.relative(d1).relative(d2);
				if(placeBlock(neighbor))
					return;
			}
		
		Direction[] verticalSides = {Direction.DOWN, Direction.UP};
		for(Direction side : verticalSides)
		{
			BlockPos neighbor = belowPlayer.relative(side);
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
			
			Rotation rotation = RotationUtils.getNeededRotations(hitVec);
			rotation.sendPlayerLookPacket();
			rotation.applyToClientPlayer();
			
			IMC.getInteractionManager().rightClickBlock(neighbor, side2,
				hitVec);
			MC.player.swing(InteractionHand.MAIN_HAND);
			MC.rightClickDelay = 4;
			
			return true;
		}
		
		return false;
	}
	
	private Vec3 getHitVec(BlockPos pos)
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
			
			return hitVec;
		}
		
		return null;
	}
}
