/*
 * Copyright (c) 2014-2026 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.settings;

import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import net.wurstclient.WurstClient;
import net.wurstclient.clickgui.Component;
import net.wurstclient.clickgui.components.SignTextEditButton;
import net.wurstclient.keybinds.PossibleKeybind;
import net.wurstclient.util.text.WText;

public final class SignTextSetting extends Setting
{
	private final String[] lines = new String[]{"", "", "", ""};
	private final String[] defaultLines;
	
	public SignTextSetting(String name, WText description, String[] defaultLines)
	{
		super(name, description);
		Objects.requireNonNull(defaultLines);
		for(int i = 0; i < 4; i++)
			lines[i] = i < defaultLines.length && defaultLines[i] != null ? defaultLines[i] : "";
		this.defaultLines = lines.clone();
	}
	
	public SignTextSetting(String name, String description, String[] defaultLines)
	{
		this(name, WText.literal(description), defaultLines);
	}
	
	public String[] getLines()
	{
		return lines.clone();
	}
	
	public String getLine(int index)
	{
		if(index < 0 || index >= 4)
			return "";
		return lines[index];
	}
	
	public void setLine(int index, String line)
	{
		if(index < 0 || index >= 4)
			return;
		lines[index] = line == null ? "" : line;
		WurstClient.INSTANCE.saveSettings();
	}
	
	public void setLines(String[] newLines)
	{
		if(newLines == null)
			return;
		for(int i = 0; i < 4; i++)
			lines[i] = i < newLines.length && newLines[i] != null ? newLines[i] : "";
		WurstClient.INSTANCE.saveSettings();
	}
	
	public boolean isEmpty()
	{
		for(String line : lines)
			if(!line.isEmpty())
				return false;
		return true;
	}
	
	public void clear()
	{
		Arrays.fill(lines, "");
		WurstClient.INSTANCE.saveSettings();
	}
	
	public void resetToDefaults()
	{
		System.arraycopy(defaultLines, 0, lines, 0, 4);
		WurstClient.INSTANCE.saveSettings();
	}
	
	@Override
	public Component getComponent()
	{
		return new SignTextEditButton(this);
	}
	
	@Override
	public void fromJson(JsonElement json)
	{
		if(json == null || !json.isJsonArray())
			return;
		
		JsonArray array = json.getAsJsonArray();
		for(int i = 0; i < 4; i++)
		{
			if(i < array.size())
			{
				JsonElement element = array.get(i);
				lines[i] = element.isJsonPrimitive() ? element.getAsString() : "";
			}
			else
				lines[i] = "";
		}
	}
	
	@Override
	public JsonElement toJson()
	{
		JsonArray array = new JsonArray();
		for(String line : lines)
			array.add(new JsonPrimitive(line));
		return array;
	}
	
	@Override
	public JsonObject exportWikiData()
	{
		JsonObject json = new JsonObject();
		json.addProperty("name", getName());
		json.addProperty("description", getDescription());
		json.addProperty("type", "SignText");
		return json;
	}
	
	@Override
	public Set<PossibleKeybind> getPossibleKeybinds(String featureName)
	{
		return Collections.emptySet();
	}
}

