/*
 * Copyright (c) 2014-2026 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package kaptainwutax.seedcrackerX.cracker;

import com.seedfinding.mccore.rand.seed.WorldSeed;
import com.seedfinding.mcseed.rand.JRand;

public class HashedSeedData
{
	
	private final long hashedSeed;
	
	public HashedSeedData(long hashedSeed)
	{
		this.hashedSeed = hashedSeed;
	}
	
	public boolean test(long seed, JRand rand)
	{
		return WorldSeed.toHash(seed) == this.hashedSeed;
	}
	
	public long getHashedSeed()
	{
		return this.hashedSeed;
	}
	
}
