/*
 * Copyright (c) 2014-2026 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.hacks;

import net.wurstclient.Category;
import net.wurstclient.hack.Hack;
import net.wurstclient.util.ChatUtils;

public final class SeedCrackerHack extends Hack {

	public SeedCrackerHack() {
		super("SeedCracker");
		setCategory(Category.OTHER);
	}

	@Override
	public void onEnable() {
		if (isModInstalled()) {
			setModActive(true);
			ChatUtils.message("SeedCracker is active. Explore to gather structures.");
		} else {
			ChatUtils.error("SeedCrackerX mod is not installed!");
			ChatUtils.message("Please place the SeedCrackerX mod jar in your mods folder to use this feature.");
			setEnabled(false);
		}
	}

	@Override
	public void onDisable() {
		if (isModInstalled()) {
			setModActive(false);
			ChatUtils.message("SeedCracker is inactive.");
		}
	}

	private boolean isModInstalled() {
		try {
			Class.forName("kaptainwutax.seedcrackerX.config.Config");
			return true;
		} catch (ClassNotFoundException e) {
			return false;
		}
	}

	private void setModActive(boolean active) {
		try {
			Class<?> configClass = Class.forName("kaptainwutax.seedcrackerX.config.Config");
			Object configInstance = configClass.getMethod("get").invoke(null);
			configClass.getField("active").set(configInstance, active);
		} catch (Exception e) {
		}
	}
}
