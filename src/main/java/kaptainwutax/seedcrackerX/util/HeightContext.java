/*
 * Copyright (c) 2014-2026 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package kaptainwutax.seedcrackerX.util;

public class HeightContext
{
	private final int bottomY;
	private final int topY;
	
	public HeightContext(int minY, int maxY)
	{
		this.bottomY = minY;
		this.topY = maxY;
	}
	
	public int getTopY()
	{
		return topY;
	}
	
	public int getBottomY()
	{
		return bottomY;
	}
	
	public int getHeight()
	{
		return topY - bottomY;
	}
	
	public int getDistanceToBottom(int yValue)
	{
		return yValue - bottomY;
	}
	
	public int getDistanceToTop(int yValue)
	{
		return topY - yValue - 1;
	}
}
