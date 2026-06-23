/*
 * Copyright (c) 2014-2026 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package kaptainwutax.seedcrackerX.structures;

import com.seedfinding.mcbiome.biome.Biome;
import com.seedfinding.mccore.state.Dimension;
import com.seedfinding.mccore.version.MCVersion;
import com.seedfinding.mccore.version.VersionMap;
import com.seedfinding.mcfeature.structure.RegionStructure;
import com.seedfinding.mcfeature.structure.UniformStructure;

public class TrialChambers extends UniformStructure<TrialChambers>
{
	
	public static final VersionMap<Config> CONFIGS = new VersionMap<Config>()
		.add(MCVersion.v1_21, new Config(34, 12, 94251327));
	
	public TrialChambers(MCVersion version)
	{
		this(CONFIGS.getAsOf(version), version);
	}
	
	public TrialChambers(RegionStructure.Config config, MCVersion version)
	{
		super(config, version);
	}
	
	public static String name()
	{
		return "trial_chambers";
	}
	
	@Override
	public Dimension getValidDimension()
	{
		return Dimension.OVERWORLD;
	}
	
	@Override
	public boolean isValidBiome(Biome biome)
	{
		// FIXME: Deep Dark doesn't exist
		return true;
	}
}
