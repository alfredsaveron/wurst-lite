/*
 * Copyright (c) 2014-2026 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package kaptainwutax.seedcrackerX.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.Util;

import java.net.URI;

public class DatabaseCommand extends ClientCommand
{
	
	public static URI DATABASE_URL = URI.create(
		"https://docs.google.com/spreadsheets/d/1tuQiE-0leW88em9OHbZnH-RFNhVqgoHhIt9WQbeqqWw/edit?usp=sharing");
	
	@Override
	public String getName()
	{
		return "database";
	}
	
	@Override
	public void build(LiteralArgumentBuilder<FabricClientCommandSource> builder)
	{
		builder.executes(this::openURL);
	}
	
	public int openURL(CommandContext<FabricClientCommandSource> context)
	{
		Util.getPlatform().openUri(DATABASE_URL);
		return 0;
	}
}
