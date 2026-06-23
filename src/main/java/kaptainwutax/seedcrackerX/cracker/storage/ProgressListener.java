/*
 * Copyright (c) 2014-2026 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package kaptainwutax.seedcrackerX.cracker.storage;

import kaptainwutax.seedcrackerX.util.Log;

public class ProgressListener
{
	
	protected float progress;
	protected int count = 0;
	
	public ProgressListener()
	{
		this(0.0F);
	}
	
	public ProgressListener(float progress)
	{
		this.progress = progress;
	}
	
	public synchronized void addPercent(float percent, boolean debug)
	{
		if((this.count & 3) == 0 && debug)
		{
			Log.debug(Log.translate("tmachine.progress") + ": " + this.progress
				+ "%");
		}
		
		this.count++;
		this.progress += percent;
	}
	
}
