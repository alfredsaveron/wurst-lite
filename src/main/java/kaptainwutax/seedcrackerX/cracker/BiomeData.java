/*
 * Copyright (c) 2014-2026 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package kaptainwutax.seedcrackerX.cracker;

import com.seedfinding.mcbiome.biome.Biome;
import com.seedfinding.mcbiome.source.BiomeSource;
import com.seedfinding.mccore.version.MCVersion;
import kaptainwutax.seedcrackerX.config.Config;

public class BiomeData
{
	
	public final Biome biome;
	public final int x;
	public final int z;
	
	public BiomeData(Biome biome, int x, int z)
	{
		this.biome = biome;
		this.x = x;
		this.z = z;
	}
	
	public boolean test(BiomeSource source)
	{
		if(Config.get().getVersion().isNewerOrEqualTo(MCVersion.v1_15))
		{
			return source.getBiomeForNoiseGen(this.x, 0, this.z) == this.biome;
		}else
		{
			return source.getBiome(this.x, 0, this.z) == this.biome;
		}
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(this == o)
			return true;
		if(!(o instanceof BiomeData))
			return false;
		BiomeData data = (BiomeData)o;
		return this.biome == data.biome;
	}
	
	@Override
	public int hashCode()
	{
		return this.biome.getName().hashCode();
	}
}
