/*
 * Copyright (c) 2014-2026 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package kaptainwutax.seedcrackerX.cracker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class PillarData
{
	
	private final List<Integer> heights;
	
	public PillarData(List<Integer> heights)
	{
		this.heights = heights;
	}
	
	public boolean test(long seed)
	{
		List<Integer> h = this.getPillarHeights((int)seed);
		return h.equals(this.heights);
	}
	
	public List<Integer> getPillarHeights(int pillarSeed)
	{
		List<Integer> indices = new ArrayList<>();
		
		for(int i = 0; i < 10; i++)
		{
			indices.add(i);
		}
		
		Collections.shuffle(indices, new Random(pillarSeed));
		
		List<Integer> heights = new ArrayList<>();
		
		for(Integer index : indices)
		{
			heights.add(76 + index * 3);
		}
		
		return heights;
	}
	
}
