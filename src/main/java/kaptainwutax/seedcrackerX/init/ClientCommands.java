/*
 * Copyright (c) 2014-2026 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package kaptainwutax.seedcrackerX.init;

import com.mojang.brigadier.CommandDispatcher;
import kaptainwutax.seedcrackerX.command.ClientCommand;
import kaptainwutax.seedcrackerX.command.CrackerCommand;
import kaptainwutax.seedcrackerX.command.DataCommand;
import kaptainwutax.seedcrackerX.command.DatabaseCommand;
import kaptainwutax.seedcrackerX.command.FinderCommand;
import kaptainwutax.seedcrackerX.command.GuiCommand;
import kaptainwutax.seedcrackerX.command.RenderCommand;
import kaptainwutax.seedcrackerX.command.VersionCommand;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

import java.util.ArrayList;
import java.util.List;

public class ClientCommands
{
	
	public static final String PREFIX = "seedcracker";
	public static final List<ClientCommand> COMMANDS = new ArrayList<>();
	
	public static RenderCommand RENDER;
	public static FinderCommand FINDER;
	public static DataCommand DATA;
	public static CrackerCommand CRACKER;
	public static VersionCommand VERSION;
	public static GuiCommand GUI;
	public static DatabaseCommand DATABASE;
	
	static
	{
		COMMANDS.add(RENDER = new RenderCommand());
		COMMANDS.add(FINDER = new FinderCommand());
		COMMANDS.add(DATA = new DataCommand());
		COMMANDS.add(CRACKER = new CrackerCommand());
		COMMANDS.add(VERSION = new VersionCommand());
		COMMANDS.add(GUI = new GuiCommand());
		COMMANDS.add(DATABASE = new DatabaseCommand());
	}
	
	public static void registerCommands(
		CommandDispatcher<FabricClientCommandSource> dispatcher)
	{
		COMMANDS.forEach(clientCommand -> clientCommand.register(dispatcher));
	}
	
}
