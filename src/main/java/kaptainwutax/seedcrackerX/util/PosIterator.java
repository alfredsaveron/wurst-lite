/*
 * Copyright (c) 2014-2026 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package kaptainwutax.seedcrackerX.util;

import net.minecraft.core.BlockPos;

import java.util.HashSet;
import java.util.Set;

public class PosIterator
{
	
	public static Set<BlockPos> create(BlockPos start, BlockPos end)
	{
		Set<BlockPos> result = new HashSet<>();
		
		for(int x = start.getX(); x <= end.getX(); x++)
		{
			for(int z = start.getZ(); z <= end.getZ(); z++)
			{
				for(int y = start.getY(); y <= end.getY(); y++)
				{
					result.add(new BlockPos(x, y, z));
				}
			}
		}
		
		return result;
	}
	
}
