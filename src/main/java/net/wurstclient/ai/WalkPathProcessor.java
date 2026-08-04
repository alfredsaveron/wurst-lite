/*
 * Copyright (c) 2014-2026 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.ai;

import java.util.ArrayList;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LadderBlock;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.phys.Vec3;
import net.wurstclient.WurstClient;
import net.wurstclient.util.BlockUtils;
import net.wurstclient.util.RotationUtils;

public class WalkPathProcessor extends PathProcessor
{
	public WalkPathProcessor(ArrayList<PathPos> path)
	{
		super(path);
	}
	
	@Override
	public void process()
	{
		var player = WurstClient.MC.player;
		BlockPos pos = player.onGround()
			? BlockPos.containing(player.getX(), player.getY() + 0.5, player.getZ())
			: BlockPos.containing(player.position());
		PathPos nextPos = path.get(index);
		int posIndex = path.indexOf(pos);
		
		ticksOffPath = posIndex == -1 ? ticksOffPath + 1 : 0;
		
		if(pos.equals(nextPos))
		{
			index++;
			if(index >= path.size())
				done = true;
			return;
		}
		if(posIndex > index)
		{
			index = posIndex + 1;
			if(index >= path.size())
				done = true;
			return;
		}
		
		lockControls();
		player.getAbilities().flying = false;
		
		facePosition(nextPos);
		if(Mth.wrapDegrees(Math.abs(RotationUtils
			.getHorizontalAngleToLookVec(Vec3.atCenterOf(nextPos)))) > 90)
			return;
		
		if(WURST.getHax().jesusHack.isEnabled())
		{
			if(player.getY() < nextPos.getY() && (player.isInWater() || player.isInLava()))
				return;
			
			if(player.getY() - nextPos.getY() > 0.5 && (player.isInWater() || player.isInLava() || WURST.getHax().jesusHack.isOverLiquid()))
				MC.options.keyShift.setDown(true);
		}
		
		if(pos.getX() != nextPos.getX() || pos.getZ() != nextPos.getZ())
		{
			MC.options.keyUp.setDown(true);
			if(index > 0 && path.get(index - 1).isJumping() || pos.getY() < nextPos.getY())
				MC.options.keyJump.setDown(true);
		}else if(pos.getY() != nextPos.getY())
		{
			if(pos.getY() < nextPos.getY())
			{
				Block block = BlockUtils.getBlock(pos);
				if(block instanceof LadderBlock || block instanceof VineBlock)
				{
					WURST.getRotationFaker().faceVectorClientIgnorePitch(
						BlockUtils.getBoundingBox(pos).getCenter());
					MC.options.keyUp.setDown(true);
				}else
				{
					if(index < path.size() - 1 && !nextPos.above().equals(path.get(index + 1)))
						index++;
					MC.options.keyJump.setDown(true);
				}
			}else
			{
				while(index < path.size() - 1 && path.get(index).below().equals(path.get(index + 1)))
					index++;
				if(player.onGround())
					MC.options.keyUp.setDown(true);
			}
		}
	}
	
	@Override
	public boolean canBreakBlocks()
	{
		return MC.player.onGround();
	}
}
