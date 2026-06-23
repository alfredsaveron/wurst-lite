/*
 * Copyright (c) 2014-2026 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.minecraft.util;

public class ARGB
{
	public static int color(int r, int g, int b)
	{
		return color(255, r, g, b);
	}
	
	public static int color(int a, int r, int g, int b)
	{
		return (a & 255) << 24 | (r & 255) << 16 | (g & 255) << 8 | (b & 255);
	}
	
	public static float alphaFloat(int argb)
	{
		return (float)(argb >>> 24) / 255.0F;
	}
	
	public static int opaque(int argb)
	{
		return argb | 0xFF000000;
	}
}
