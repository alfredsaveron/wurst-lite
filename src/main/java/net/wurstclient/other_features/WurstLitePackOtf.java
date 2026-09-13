/*
 * Copyright (c) 2014-2026 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.other_features;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.repository.PackRepository;
import net.wurstclient.DontBlock;
import net.wurstclient.other_feature.OtherFeature;
import net.wurstclient.settings.CheckboxSetting;

@DontBlock
public final class WurstLitePackOtf extends OtherFeature
{
	private static final String PACK_ID = "fabric/wurst:default_pack";
	
	private final CheckboxSetting enabled =
		new CheckboxSetting("Wurst Lite Pack via Vanilla Tweaks", false);
	
	public WurstLitePackOtf()
	{
		super("WurstLitePack",
			"Enables the built-in Wurst Lite resource pack created with Vanilla Tweaks.");
		addSetting(enabled);
	}
	
	public boolean isPackActive()
	{
		Minecraft mc = Minecraft.getInstance();
		if(mc.getResourcePackRepository() == null)
			return enabled.isChecked();
		
		return mc.getResourcePackRepository().getSelectedIds()
			.contains(PACK_ID);
	}
	
	public void togglePack()
	{
		Minecraft mc = Minecraft.getInstance();
		PackRepository repo = mc.getResourcePackRepository();
		if(repo == null)
			return;
		
		boolean currentlyActive = repo.getSelectedIds().contains(PACK_ID);
		List<String> selectedIds = new ArrayList<>(repo.getSelectedIds());
		
		if(currentlyActive)
		{
			selectedIds.remove(PACK_ID);
			enabled.setChecked(false);
		}else
		{
			if(!selectedIds.contains(PACK_ID))
				selectedIds.add(PACK_ID);
			enabled.setChecked(true);
		}
		
		repo.setSelected(selectedIds);
		mc.options.updateResourcePacks(repo);
		mc.reloadResourcePacks();
	}
	
	@Override
	public void doPrimaryAction()
	{
		togglePack();
	}
}
