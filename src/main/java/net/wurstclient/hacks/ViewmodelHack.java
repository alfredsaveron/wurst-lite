/*
 * Copyright (c) 2014-2026 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.hacks;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.world.InteractionHand;
import net.wurstclient.Category;
import net.wurstclient.DontBlock;
import net.wurstclient.SearchTags;
import net.wurstclient.hack.Hack;
import net.wurstclient.settings.SliderSetting;
import net.wurstclient.settings.SliderSetting.ValueDisplay;

@SearchTags({"viewmodel", "hand offset", "item offset", "csgo hand"})
@DontBlock
public final class ViewmodelHack extends Hack
{
	private final SliderSetting xOffset =
		new SliderSetting("X Offset", "Horizontal translation of the hand.",
			0.0, -2.0, 2.0, 0.05, ValueDisplay.DECIMAL);
	
	private final SliderSetting yOffset =
		new SliderSetting("Y Offset", "Vertical translation of the hand.", 0.0,
			-2.0, 2.0, 0.05, ValueDisplay.DECIMAL);
	
	private final SliderSetting zOffset = new SliderSetting("Z Offset",
		"Forward/backward translation of the hand.", 0.0, -2.0, 2.0, 0.05,
		ValueDisplay.DECIMAL);
	
	public ViewmodelHack()
	{
		super("Viewmodel");
		setCategory(Category.RENDER);
		addSetting(xOffset);
		addSetting(yOffset);
		addSetting(zOffset);
	}
	
	public void apply(PoseStack matrices, InteractionHand hand)
	{
		if(!isEnabled())
			return;
		
		float x = xOffset.getValueF();
		float y = yOffset.getValueF();
		float z = zOffset.getValueF();
		
		if(hand == InteractionHand.OFF_HAND)
			x = -x;
		
		matrices.translate(x, y, z);
	}
}
