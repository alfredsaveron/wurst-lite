/*
 * Copyright (c) 2014-2026 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.serverfinder;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.lwjgl.glfw.GLFW;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.screens.ConnectScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerList;
import net.minecraft.client.multiplayer.resolver.ServerAddress;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringUtil;

public final class ServerFinderScreen extends Screen
{
	private final Screen prevScreen;
	
	private EditBox minPlayersBox;
	private EditBox maxPlayersBox;
	private Button searchButton;
	private Button joinButton;
	private Button addButton;
	private ServerFinderList listGui;
	
	private final List<String> queue = new ArrayList<>();
	private final AtomicInteger activeThreads = new AtomicInteger();
	private ExecutorService executor;
	private boolean searching = false;
	private int scannedCount = 0;
	
	public ServerFinderScreen(Screen prevScreen)
	{
		super(Component.literal("Server Finder"));
		this.prevScreen = prevScreen;
	}
	
	@Override
	public void init()
	{
		Font font = minecraft.font;
		
		minPlayersBox =
			new EditBox(font, width / 2 - 110, 36, 40, 18, Component.empty());
		minPlayersBox.setValue("5");
		minPlayersBox.setMaxLength(5);
		addWidget(minPlayersBox);
		
		maxPlayersBox =
			new EditBox(font, width / 2 - 20, 36, 40, 18, Component.empty());
		maxPlayersBox.setValue("25");
		maxPlayersBox.setMaxLength(5);
		addWidget(maxPlayersBox);
		
		searchButton =
			Button
				.builder(Component.literal(searching ? "Stop" : "Find"),
					b -> toggleSearch())
				.bounds(width / 2 + 30, 35, 60, 20).build();
		addRenderableWidget(searchButton);
		
		listGui = new ServerFinderList(minecraft, this);
		addWidget(listGui);
		
		int buttonY = height - 28;
		joinButton = Button
			.builder(Component.literal("Join Server"), b -> joinSelected())
			.bounds(width / 2 - 154, buttonY, 100, 20).build();
		addRenderableWidget(joinButton);
		
		addButton =
			Button.builder(Component.literal("Add to List"), b -> addSelected())
				.bounds(width / 2 - 50, buttonY, 100, 20).build();
		addRenderableWidget(addButton);
		
		Button backButton =
			Button.builder(Component.literal("Back"), b -> onClose())
				.bounds(width / 2 + 54, buttonY, 100, 20).build();
		addRenderableWidget(backButton);
		
		updateButtons();
	}
	
	private void toggleSearch()
	{
		if(searching)
		{
			stopSearch();
			return;
		}
		
		searching = true;
		searchButton.setMessage(Component.literal("Stop"));
		listGui.clearEntries();
		queue.clear();
		queue.addAll(Arrays.asList(ServerPool.SERVERS));
		Collections.shuffle(queue);
		scannedCount = 0;
		executor = Executors.newFixedThreadPool(8);
		pumpQueue();
	}
	
	private void stopSearch()
	{
		searching = false;
		searchButton.setMessage(Component.literal("Find"));
		if(executor != null)
		{
			executor.shutdownNow();
			executor = null;
		}
		activeThreads.set(0);
	}
	
	private void pumpQueue()
	{
		if(!searching || executor == null || executor.isShutdown())
			return;
		
		while(activeThreads.get() < 8 && !queue.isEmpty())
		{
			String ip = queue.remove(0);
			activeThreads.incrementAndGet();
			
			executor.submit(() -> {
				try
				{
					DiscoveredServer server = pingServer(ip, 2500);
					minecraft.execute(() -> {
						scannedCount++;
						if(!searching)
							return;
						
						if(server != null)
						{
							int min = parseNumber(minPlayersBox.getValue(), 0);
							int max =
								parseNumber(maxPlayersBox.getValue(), 9999);
							if(server.online >= min && server.online <= max)
								listGui.addServer(server);
						}
					});
				}finally
				{
					activeThreads.decrementAndGet();
					minecraft.execute(() -> {
						if(searching)
						{
							if(queue.isEmpty() && activeThreads.get() == 0)
								stopSearch();
							else
								pumpQueue();
						}
					});
				}
			});
		}
	}
	
	private static DiscoveredServer pingServer(String address, int timeoutMs)
	{
		String host = address;
		int port = 25565;
		int colonIndex = address.indexOf(':');
		if(colonIndex != -1)
		{
			host = address.substring(0, colonIndex);
			try
			{
				port = Integer.parseInt(address.substring(colonIndex + 1));
			}catch(NumberFormatException ignored)
			{
			}
		}
		
		long start = System.currentTimeMillis();
		try(Socket socket = new Socket())
		{
			socket.connect(new InetSocketAddress(host, port), timeoutMs);
			socket.setSoTimeout(timeoutMs);
			
			DataOutputStream out =
				new DataOutputStream(socket.getOutputStream());
			DataInputStream in = new DataInputStream(socket.getInputStream());
			
			ByteArrayOutputStream handshakeBytes = new ByteArrayOutputStream();
			DataOutputStream handshake = new DataOutputStream(handshakeBytes);
			handshake.writeByte(0x00);
			writeVarInt(handshake, 767);
			writeString(handshake, host);
			handshake.writeShort(port);
			writeVarInt(handshake, 1);
			
			writeVarInt(out, handshakeBytes.size());
			out.write(handshakeBytes.toByteArray());
			
			out.writeByte(0x01);
			out.writeByte(0x00);
			out.flush();
			
			readVarInt(in);
			int packetId = readVarInt(in);
			if(packetId != 0x00)
				return null;
			
			String jsonString = readString(in);
			long ping = System.currentTimeMillis() - start;
			
			JsonObject json =
				JsonParser.parseString(jsonString).getAsJsonObject();
			JsonObject players = json.getAsJsonObject("players");
			int online = players.get("online").getAsInt();
			int max = players.get("max").getAsInt();
			
			String motd = "";
			if(json.has("description"))
				motd = parseMotd(json.get("description"));
			
			return new DiscoveredServer(address, online, max, motd, ping);
			
		}catch(Throwable t)
		{
			return null;
		}
	}
	
	private static void writeVarInt(DataOutputStream out, int value)
		throws IOException
	{
		while((value & 0xFFFFFF80) != 0L)
		{
			out.writeByte((value & 0x7F) | 0x80);
			value >>>= 7;
		}
		out.writeByte(value & 0x7F);
	}
	
	private static int readVarInt(DataInputStream in) throws IOException
	{
		int value = 0;
		int position = 0;
		byte currentByte;
		while(true)
		{
			currentByte = in.readByte();
			value |= (currentByte & 0x7F) << position;
			if((currentByte & 0x80) == 0)
				break;
			position += 7;
			if(position >= 32)
				throw new IOException("VarInt is too big");
		}
		return value;
	}
	
	private static void writeString(DataOutputStream out, String string)
		throws IOException
	{
		byte[] bytes = string.getBytes(StandardCharsets.UTF_8);
		writeVarInt(out, bytes.length);
		out.write(bytes);
	}
	
	private static String readString(DataInputStream in) throws IOException
	{
		int length = readVarInt(in);
		if(length < 0 || length > 262144)
			throw new IOException("String too long: " + length);
		byte[] bytes = new byte[length];
		in.readFully(bytes);
		return new String(bytes, StandardCharsets.UTF_8);
	}
	
	private static String parseMotd(JsonElement element)
	{
		if(element == null)
			return "";
		if(element.isJsonPrimitive())
			return element.getAsString();
		if(element.isJsonObject())
		{
			JsonObject obj = element.getAsJsonObject();
			StringBuilder sb = new StringBuilder();
			if(obj.has("text"))
				sb.append(obj.get("text").getAsString());
			if(obj.has("extra") && obj.get("extra").isJsonArray())
			{
				for(JsonElement e : obj.getAsJsonArray("extra"))
					sb.append(parseMotd(e));
			}
			return sb.toString();
		}
		return "";
	}
	
	private int parseNumber(String text, int fallback)
	{
		try
		{
			return Integer.parseInt(text.trim());
		}catch(Exception e)
		{
			return fallback;
		}
	}
	
	private void joinSelected()
	{
		ServerFinderList.Entry selected = listGui.getSelected();
		if(selected == null)
			return;
		
		DiscoveredServer server = selected.server;
		ServerData serverData =
			new ServerData(server.ip, server.ip, ServerData.Type.OTHER);
		ConnectScreen.startConnecting(this, minecraft,
			ServerAddress.parseString(server.ip), serverData, false, null);
	}
	
	private void addSelected()
	{
		ServerFinderList.Entry selected = listGui.getSelected();
		if(selected == null)
			return;
		
		DiscoveredServer server = selected.server;
		ServerList serverList = new ServerList(minecraft);
		serverList.load();
		
		if(serverList.get(server.ip) == null)
		{
			serverList.add(
				new ServerData(server.ip, server.ip, ServerData.Type.OTHER),
				false);
			serverList.save();
			addButton.setMessage(Component.literal("Added!"));
		}else
		{
			addButton.setMessage(Component.literal("Already in list"));
		}
	}
	
	private void updateButtons()
	{
		boolean hasSelection = listGui.getSelected() != null;
		joinButton.active = hasSelection;
		addButton.active = hasSelection;
	}
	
	@Override
	public void tick()
	{
		updateButtons();
	}
	
	@Override
	public void render(GuiGraphics context, int mouseX, int mouseY,
		float partialTicks)
	{
		renderBackground(context, mouseX, mouseY, partialTicks);
		listGui.render(context, mouseX, mouseY, partialTicks);
		
		context.drawCenteredString(font, "Server Finder", width / 2, 10,
			0xFFFFFF);
		
		context.drawString(font, "Min:", width / 2 - 135, 41, 0xAAAAAA);
		minPlayersBox.render(context, mouseX, mouseY, partialTicks);
		
		context.drawString(font, "Max:", width / 2 - 45, 41, 0xAAAAAA);
		maxPlayersBox.render(context, mouseX, mouseY, partialTicks);
		
		String status;
		if(searching)
			status = "Searching... (" + scannedCount + " checked, "
				+ listGui.children().size() + " matches)";
		else if(listGui.children().isEmpty())
			status = "Set player range and click Find.";
		else
			status =
				"Found " + listGui.children().size() + " matching servers.";
		
		context.drawCenteredString(font, status, width / 2, 60,
			searching ? 0x55FF55 : 0xAAAAAA);
		
		super.render(context, mouseX, mouseY, partialTicks);
	}
	
	@Override
	public void onClose()
	{
		stopSearch();
		minecraft.setScreen(prevScreen);
	}
	
	public static final class DiscoveredServer
	{
		public final String ip;
		public final int online;
		public final int max;
		public final String motd;
		public final long ping;
		
		public DiscoveredServer(String ip, int online, int max, String motd,
			long ping)
		{
			this.ip = ip;
			this.online = online;
			this.max = max;
			this.motd = motd;
			this.ping = ping;
		}
	}
	
	public static final class ServerFinderList
		extends ObjectSelectionList<ServerFinderList.Entry>
	{
		private final ServerFinderScreen screen;
		
		public ServerFinderList(Minecraft minecraft, ServerFinderScreen screen)
		{
			super(minecraft, screen.width, screen.height - 105, 74, 34);
			this.screen = screen;
		}
		
		public void addServer(DiscoveredServer server)
		{
			for(Entry e : children())
				if(e.server.ip.equalsIgnoreCase(server.ip))
					return;
			addEntry(new Entry(server, screen));
		}
		
		public void clearEntries()
		{
			super.clearEntries();
			setSelected(null);
		}
		
		public static final class Entry extends ObjectSelectionList.Entry<Entry>
		{
			public final DiscoveredServer server;
			private final ServerFinderScreen screen;
			private long lastClickTime;
			
			public Entry(DiscoveredServer server, ServerFinderScreen screen)
			{
				this.server = Objects.requireNonNull(server);
				this.screen = screen;
			}
			
			@Override
			public Component getNarration()
			{
				return Component.literal(server.ip);
			}
			
			@Override
			public boolean mouseClicked(double mouseX, double mouseY,
				int button)
			{
				if(button != GLFW.GLFW_MOUSE_BUTTON_LEFT)
					return false;
				
				long now = Util.getMillis();
				if(now - lastClickTime < 250)
					screen.joinSelected();
				
				lastClickTime = now;
				screen.listGui.setSelected(this);
				return true;
			}
			
			@Override
			public void render(GuiGraphics context, int index, int y, int x,
				int entryWidth, int entryHeight, int mouseX, int mouseY,
				boolean hovered, float tickDelta)
			{
				Font font = Minecraft.getInstance().font;
				
				String ip = server.ip;
				context.drawString(font, ip, x + 4, y + 3, 0xFFFFFF);
				
				String playerCount =
					server.online + "/" + server.max + " players";
				int countWidth = font.width(playerCount);
				context.drawString(font, playerCount,
					x + entryWidth - countWidth - 6, y + 3, 0x55FF55);
				
				String motd = "";
				if(server.motd != null)
					motd = StringUtil.stripColor(server.motd).trim()
						.replaceAll("\\s+", " ");
				if(motd.length() > 45)
					motd = motd.substring(0, 42) + "...";
				
				context.drawString(font, motd, x + 4, y + 17, 0x888888);
				
				String pingText = server.ping > 0 ? server.ping + "ms" : "";
				int pingWidth = font.width(pingText);
				context.drawString(font, pingText,
					x + entryWidth - pingWidth - 6, y + 17, 0xAAAAAA);
			}
		}
	}
}
