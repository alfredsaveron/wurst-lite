/*
 * Copyright (c) 2014-2026 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.hacks;

import kaptainwutax.seedcrackerX.config.Config;
import net.wurstclient.Category;
import net.wurstclient.hack.Hack;
import net.wurstclient.util.ChatUtils;

public final class SeedCrackerHack extends Hack
{
	public SeedCrackerHack()
	{
		super("SeedCracker");
		setCategory(Category.OTHER);
	}
	
	@Override
	public void onEnable()
	{
		Config.get().active = true;
		ChatUtils
			.message("SeedCracker is active. Explore to gather structures.");
	}
	
	@Override
	public void onDisable()
	{
		Config.get().active = false;
		ChatUtils.message("SeedCracker is inactive.");
	}
}
