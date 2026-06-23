/*
 * Copyright (c) 2014-2026 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package kaptainwutax.seedcrackerX.finder;

import kaptainwutax.seedcrackerX.finder.decorator.ore.EmeraldOreFinder;
import kaptainwutax.seedcrackerX.finder.structure.*;
import kaptainwutax.seedcrackerX.util.HeightContext;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;

public class ReloadFinders
{
	public Minecraft client = Minecraft.getInstance();
	
	public static void reloadHeight(int minY, int maxY)
	{
		Finder.CHUNK_POSITIONS.clear();
		for(int x = 0; x < 16; x++)
		{
			for(int z = 0; z < 16; z++)
			{
				for(int y = minY; y < maxY; y++)
				{
					Finder.CHUNK_POSITIONS.add(new BlockPos(x, y, z));
				}
			}
		}
		Finder.heightContext = new HeightContext(minY, maxY);
		
		EmeraldOreFinder.reloadSearchPositions();
		AbstractTempleFinder.reloadSearchPositions();
		BuriedTreasureFinder.reloadSearchPositions();
		MonumentFinder.reloadSearchPositions();
		OutpostFinder.reloadSearchPositions();
		IglooFinder.reloadSearchPositions();
		TrialChambersFinder.reloadSearchPositions();
	}
	
	public void reload()
	{
		int renderdistance = client.options.renderDistance().get();
		
		int playerChunkX = (int)(Math.round(client.player.getX()) >> 4);
		int playerChunkZ = (int)(Math.round(client.player.getZ()) >> 4);
		for(int i = playerChunkX - renderdistance; i < playerChunkX
			+ renderdistance; i++)
		{
			for(int j = playerChunkZ - renderdistance; j < playerChunkZ
				+ renderdistance; j++)
			{
				FinderQueue.get().onChunkData(client.level, new ChunkPos(i, j));
			}
		}
	}
}
