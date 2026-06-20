/*
 * Copyright (c) 2014-2026 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.other_features;

import net.wurstclient.DontBlock;
import net.wurstclient.SearchTags;
import net.wurstclient.clickgui.Component;
import net.wurstclient.clickgui.components.SearchQueryComponent;
import net.wurstclient.other_feature.OtherFeature;
import net.wurstclient.settings.Setting;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import java.util.Collections;
import java.util.Set;
import net.wurstclient.keybinds.PossibleKeybind;
import net.wurstclient.util.text.WText;

@SearchTags({"search", "find", "filter"})
@DontBlock
public final class SearchOtf extends OtherFeature
{
	public SearchOtf()
	{
		super("Search", "Allows searching and filtering hacks.");
		addSetting(new Setting("Search Query", WText.empty())
		{
			@Override
			public Component getComponent()
			{
				return new SearchQueryComponent();
			}
			
			@Override
			public void fromJson(JsonElement json)
			{}
			
			@Override
			public JsonElement toJson()
			{
				return JsonNull.INSTANCE;
			}
			
			@Override
			public JsonObject exportWikiData()
			{
				return new JsonObject();
			}
			
			@Override
			public Set<PossibleKeybind> getPossibleKeybinds(String name)
			{
				return Collections.emptySet();
			}
		});
	}
}
