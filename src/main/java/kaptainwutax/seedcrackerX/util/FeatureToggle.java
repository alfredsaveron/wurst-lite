/*
 * Copyright (c) 2014-2026 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package kaptainwutax.seedcrackerX.util;

public class FeatureToggle
{
	
	// This is for The featureToggles in the config object
	// It allows for the booleans to be passed around by reference
	// (I know that it's a hacky workaround)
	private boolean enabled;
	
	public FeatureToggle(boolean flag)
	{
		enabled = flag;
	}
	
	public void set(boolean flag)
	{
		enabled = flag;
	}
	
	public boolean get()
	{
		return enabled;
	}
}
