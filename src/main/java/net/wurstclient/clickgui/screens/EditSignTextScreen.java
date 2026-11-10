package net.wurstclient.clickgui.screens;

import org.lwjgl.glfw.GLFW;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.wurstclient.settings.SignTextSetting;

public final class EditSignTextScreen extends Screen
{
	private final Screen prevScreen;
	private final SignTextSetting setting;
	private final EditBox[] lineFields = new EditBox[4];
	
	private Button doneButton;
	private Button clearButton;
	private Button cancelButton;
	
	private int boardX;
	private int boardY;
	private final int boardWidth = 192;
	private final int boardHeight = 96;
	
	public EditSignTextScreen(Screen prevScreen, SignTextSetting setting)
	{
		super(Component.literal("Edit AutoSign Text"));
		this.prevScreen = prevScreen;
		this.setting = setting;
	}
	
	@Override
	public void init()
	{
		boardX = (width - boardWidth) / 2;
		boardY = height / 2 - 58;
		
		Font tr = minecraft.font;
		int lineWidth = boardWidth - 16;
		
		for(int i = 0; i < 4; i++)
		{
			int lineY = boardY + 16 + i * 16;
			EditBox field = new EditBox(tr, boardX + 8, lineY, lineWidth, 14, Component.empty());
			field.setBordered(false);
			field.setMaxLength(384);
			field.setValue(setting.getLine(i));
			field.setTextColor(0x000000);
			lineFields[i] = field;
			addRenderableWidget(field);
		}
		
		setFocused(lineFields[0]);
		lineFields[0].setFocused(true);
		
		int buttonY = boardY + boardHeight + 36;
		
		doneButton = Button.builder(Component.literal("Done"), b -> done())
			.bounds(width / 2 - 106, buttonY, 68, 20).build();
		addRenderableWidget(doneButton);
		
		clearButton = Button.builder(Component.literal("Clear"), b -> clear())
			.bounds(width / 2 - 32, buttonY, 64, 20).build();
		addRenderableWidget(clearButton);
		
		cancelButton = Button.builder(Component.literal("Cancel"), b -> cancel())
			.bounds(width / 2 + 38, buttonY, 68, 20).build();
		addRenderableWidget(cancelButton);
	}
	
	private void done()
	{
		String[] lines = new String[4];
		for(int i = 0; i < 4; i++)
			lines[i] = lineFields[i].getValue();
		setting.setLines(lines);
		minecraft.setScreen(prevScreen);
	}
	
	private void clear()
	{
		for(EditBox field : lineFields)
			field.setValue("");
		if(lineFields.length > 0)
		{
			setFocused(lineFields[0]);
			lineFields[0].setFocused(true);
		}
	}
	
	private void cancel()
	{
		minecraft.setScreen(prevScreen);
	}
	
	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers)
	{
		if(keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_KP_ENTER)
		{
			for(int i = 0; i < 4; i++)
			{
				if(lineFields[i].isFocused())
				{
					if(i < 3)
					{
						lineFields[i].setFocused(false);
						setFocused(lineFields[i + 1]);
						lineFields[i + 1].setFocused(true);
						return true;
					}
					else
					{
						done();
						return true;
					}
				}
			}
		}
		else if(keyCode == GLFW.GLFW_KEY_DOWN)
		{
			for(int i = 0; i < 3; i++)
			{
				if(lineFields[i].isFocused())
				{
					lineFields[i].setFocused(false);
					setFocused(lineFields[i + 1]);
					lineFields[i + 1].setFocused(true);
					return true;
				}
			}
		}
		else if(keyCode == GLFW.GLFW_KEY_UP)
		{
			for(int i = 1; i < 4; i++)
			{
				if(lineFields[i].isFocused())
				{
					lineFields[i].setFocused(false);
					setFocused(lineFields[i - 1]);
					lineFields[i - 1].setFocused(true);
					return true;
				}
			}
		}
		else if(keyCode == GLFW.GLFW_KEY_ESCAPE)
		{
			cancel();
			return true;
		}
		
		return super.keyPressed(keyCode, scanCode, modifiers);
	}
	
	@Override
	public void render(GuiGraphics context, int mouseX, int mouseY, float partialTicks)
	{
		renderBackground(context, mouseX, mouseY, partialTicks);
		
		context.drawCenteredString(font, "Edit AutoSign Text", width / 2, boardY - 30, 0xFFFFFF);
		context.drawCenteredString(font, "Text to write on signs automatically", width / 2, boardY - 18, 0xAAAAAA);
		
		int postWidth = 8;
		int postHeight = 32;
		int postX = (width - postWidth) / 2;
		int postY = boardY + boardHeight;
		context.fill(postX, postY, postX + postWidth, postY + postHeight, 0xFF5C3E1E);
		
		context.fill(boardX - 2, boardY - 2, boardX + boardWidth + 2, boardY + boardHeight + 2, 0xFF3D2813);
		context.fill(boardX, boardY, boardX + boardWidth, boardY + boardHeight, 0xFF9E7342);
		context.fill(boardX + 1, boardY + 1, boardX + boardWidth - 1, boardY + 2, 0xFFAF8453);
		context.fill(boardX + 1, boardY + 1, boardX + 2, boardY + boardHeight - 1, 0xFFAF8453);
		context.fill(boardX + 1, boardY + boardHeight - 2, boardX + boardWidth - 1, boardY + boardHeight - 1, 0xFF7D5528);
		context.fill(boardX + boardWidth - 2, boardY + 1, boardX + boardWidth - 1, boardY + boardHeight - 1, 0xFF7D5528);
		
		for(int i = 0; i < 4; i++)
		{
			int lineY = boardY + 16 + i * 16;
			context.fill(boardX + 8, lineY + 13, boardX + boardWidth - 8, lineY + 14, 0x263D2813);
			if(lineFields[i].isFocused())
				context.fill(boardX + 6, lineY - 1, boardX + boardWidth - 6, lineY + 14, 0x1AFFFFFF);
		}
		
		super.render(context, mouseX, mouseY, partialTicks);
	}
	
	@Override
	public boolean isPauseScreen()
	{
		return false;
	}
	
	@Override
	public boolean shouldCloseOnEsc()
	{
		return false;
	}
}
