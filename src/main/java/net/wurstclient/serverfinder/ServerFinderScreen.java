package net.wurstclient.serverfinder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.lwjgl.glfw.GLFW;

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
import net.minecraft.client.multiplayer.ServerStatusPinger;
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
	
	private final ServerStatusPinger pinger = new ServerStatusPinger();
	private final List<String> queue = new ArrayList<>();
	private final Set<String> activeAddresses = new HashSet<>();
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
		
		minPlayersBox = new EditBox(font, width / 2 - 110, 36, 40, 18, Component.empty());
		minPlayersBox.setValue("5");
		minPlayersBox.setMaxLength(5);
		addWidget(minPlayersBox);
		
		maxPlayersBox = new EditBox(font, width / 2 - 20, 36, 40, 18, Component.empty());
		maxPlayersBox.setValue("25");
		maxPlayersBox.setMaxLength(5);
		addWidget(maxPlayersBox);
		
		searchButton = Button.builder(Component.literal(searching ? "Stop" : "Find"), b -> toggleSearch())
			.bounds(width / 2 + 30, 35, 60, 20).build();
		addRenderableWidget(searchButton);
		
		listGui = new ServerFinderList(minecraft, this);
		addWidget(listGui);
		
		int buttonY = height - 28;
		joinButton = Button.builder(Component.literal("Join Server"), b -> joinSelected())
			.bounds(width / 2 - 154, buttonY, 100, 20).build();
		addRenderableWidget(joinButton);
		
		addButton = Button.builder(Component.literal("Add to List"), b -> addSelected())
			.bounds(width / 2 - 50, buttonY, 100, 20).build();
		addRenderableWidget(addButton);
		
		Button backButton = Button.builder(Component.literal("Back"), b -> onClose())
			.bounds(width / 2 + 54, buttonY, 100, 20).build();
		addRenderableWidget(backButton);
		
		updateButtons();
	}
	
	private void toggleSearch()
	{
		if(searching)
		{
			searching = false;
			pinger.removeAll();
			activeAddresses.clear();
			searchButton.setMessage(Component.literal("Find"));
			return;
		}
		
		searching = true;
		searchButton.setMessage(Component.literal("Stop"));
		listGui.clearEntries();
		queue.clear();
		activeAddresses.clear();
		queue.addAll(Arrays.asList(ServerPool.SERVERS));
		Collections.shuffle(queue);
		scannedCount = 0;
		pumpQueue();
	}
	
	private void pumpQueue()
	{
		if(!searching)
			return;
		
		while(activeAddresses.size() < 12 && !queue.isEmpty())
		{
			String ip = queue.remove(0);
			activeAddresses.add(ip);
			scannedCount++;
			ServerData server = new ServerData("", ip, ServerData.Type.OTHER);
			
			try
			{
				pinger.pingServer(server, () -> onPingSuccess(server, ip), () -> {});
			}
			catch(Exception e)
			{
				activeAddresses.remove(ip);
			}
		}
		
		if(queue.isEmpty() && activeAddresses.isEmpty())
		{
			searching = false;
			searchButton.setMessage(Component.literal("Find"));
		}
	}
	
	private void onPingSuccess(ServerData server, String ip)
	{
		minecraft.execute(() -> {
			activeAddresses.remove(ip);
			if(server.players != null)
			{
				int online = server.players.online();
				int min = parseNumber(minPlayersBox.getValue(), 0);
				int max = parseNumber(maxPlayersBox.getValue(), 9999);
				if(online >= min && online <= max)
					listGui.addServer(server);
			}
			pumpQueue();
		});
	}
	
	private int parseNumber(String text, int fallback)
	{
		try
		{
			return Integer.parseInt(text.trim());
		}
		catch(Exception e)
		{
			return fallback;
		}
	}
	
	private void joinSelected()
	{
		ServerFinderList.Entry selected = listGui.getSelected();
		if(selected == null)
			return;
		
		ServerData server = selected.server;
		ConnectScreen.startConnecting(this, minecraft, ServerAddress.parseString(server.ip), server, false, null);
	}
	
	private void addSelected()
	{
		ServerFinderList.Entry selected = listGui.getSelected();
		if(selected == null)
			return;
		
		ServerData server = selected.server;
		ServerList serverList = new ServerList(minecraft);
		serverList.load();
		
		if(serverList.get(server.ip) == null)
		{
			String name = server.name != null && !server.name.isEmpty() ? server.name : server.ip;
			serverList.add(new ServerData(name, server.ip, ServerData.Type.OTHER), false);
			serverList.save();
			addButton.setMessage(Component.literal("Added!"));
		}
		else
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
		pinger.tick();
		if(searching)
			pumpQueue();
		updateButtons();
	}
	
	@Override
	public void render(GuiGraphics context, int mouseX, int mouseY, float partialTicks)
	{
		renderBackground(context, mouseX, mouseY, partialTicks);
		listGui.render(context, mouseX, mouseY, partialTicks);
		
		context.drawCenteredString(font, "Server Finder", width / 2, 10, 0xFFFFFF);
		
		context.drawString(font, "Min:", width / 2 - 135, 41, 0xAAAAAA);
		minPlayersBox.render(context, mouseX, mouseY, partialTicks);
		
		context.drawString(font, "Max:", width / 2 - 45, 41, 0xAAAAAA);
		maxPlayersBox.render(context, mouseX, mouseY, partialTicks);
		
		String status;
		if(searching)
			status = "Searching... (" + scannedCount + " checked, " + listGui.children().size() + " matches)";
		else if(listGui.children().isEmpty())
			status = "Set player range and click Find.";
		else
			status = "Found " + listGui.children().size() + " matching servers.";
		
		context.drawCenteredString(font, status, width / 2, 60, searching ? 0x55FF55 : 0xAAAAAA);
		
		super.render(context, mouseX, mouseY, partialTicks);
	}
	
	@Override
	public void onClose()
	{
		searching = false;
		pinger.removeAll();
		minecraft.setScreen(prevScreen);
	}
	
	public static final class ServerFinderList extends ObjectSelectionList<ServerFinderList.Entry>
	{
		private final ServerFinderScreen screen;
		
		public ServerFinderList(Minecraft minecraft, ServerFinderScreen screen)
		{
			super(minecraft, screen.width, screen.height - 105, 74, 34);
			this.screen = screen;
		}
		
		public void addServer(ServerData server)
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
			private final ServerData server;
			private final ServerFinderScreen screen;
			private long lastClickTime;
			
			public Entry(ServerData server, ServerFinderScreen screen)
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
			public boolean mouseClicked(double mouseX, double mouseY, int button)
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
			public void render(GuiGraphics context, int index, int y, int x, int entryWidth,
				int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta)
			{
				Font font = Minecraft.getInstance().font;
				
				String ip = server.ip;
				context.drawString(font, ip, x + 4, y + 3, 0xFFFFFF);
				
				String playerCount = "";
				if(server.players != null)
					playerCount = server.players.online() + "/" + server.players.max() + " players";
				
				int countWidth = font.width(playerCount);
				context.drawString(font, playerCount, x + entryWidth - countWidth - 6, y + 3, 0x55FF55);
				
				String motd = "";
				if(server.motd != null)
					motd = StringUtil.stripColor(server.motd.getString()).trim().replaceAll("\\s+", " ");
				if(motd.isEmpty() && server.version != null)
					motd = server.version.getString();
				if(motd.length() > 45)
					motd = motd.substring(0, 42) + "...";
				
				context.drawString(font, motd, x + 4, y + 17, 0x888888);
				
				String pingText = server.ping > 0 ? server.ping + "ms" : "";
				int pingWidth = font.width(pingText);
				context.drawString(font, pingText, x + entryWidth - pingWidth - 6, y + 17, 0xAAAAAA);
			}
		}
	}
}

