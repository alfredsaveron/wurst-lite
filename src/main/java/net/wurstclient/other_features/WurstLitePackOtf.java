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
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackRepository;
import net.wurstclient.DontBlock;
import net.wurstclient.other_feature.OtherFeature;
import net.wurstclient.settings.CheckboxSetting;

@DontBlock
public final class WurstLitePackOtf extends OtherFeature
{
	private static final String PACK_ID_PREFIX = "wurst:default_pack";
	
	private final CheckboxSetting enabled =
		new CheckboxSetting("Wurst Lite Pack via vanillatweaks.net", false);
	
	public WurstLitePackOtf()
	{
		super("WurstLitePack",
			"Enables the built-in Wurst Lite resource pack created with vanillatweaks.net.");
		addSetting(enabled);
	}
	
	private String findActualPackId(PackRepository repo)
	{
		for(Pack pack : repo.getAvailablePacks())
			if(pack.getId().contains(PACK_ID_PREFIX))
				return pack.getId();
		return "fabric/wurst:default_pack";
	}
	
	public boolean isPackActive()
	{
		Minecraft mc = Minecraft.getInstance();
		PackRepository repo = mc.getResourcePackRepository();
		if(repo == null)
			return enabled.isChecked();
		
		String packId = findActualPackId(repo);
		return repo.getSelectedIds().contains(packId);
	}
	
	public void togglePack()
	{
		Minecraft mc = Minecraft.getInstance();
		PackRepository repo = mc.getResourcePackRepository();
		if(repo == null)
			return;
		
		repo.reload();
		String packId = findActualPackId(repo);
		boolean currentlyActive = repo.getSelectedIds().contains(packId);
		List<String> selectedIds = new ArrayList<>(repo.getSelectedIds());
		
		if(currentlyActive)
		{
			selectedIds.remove(packId);
			enabled.setChecked(false);
		}else
		{
			if(!selectedIds.contains(packId))
				selectedIds.add(packId);
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
