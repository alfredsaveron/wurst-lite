package net.wurstclient.clickgui.components;

import java.util.Objects;

import net.wurstclient.clickgui.screens.EditSignTextScreen;
import net.wurstclient.settings.Setting;
import net.wurstclient.settings.SignTextSetting;

public final class SignTextEditButton extends AbstractListEditButton
{
	private final SignTextSetting setting;
	
	public SignTextEditButton(SignTextSetting setting)
	{
		this.setting = Objects.requireNonNull(setting);
		setWidth(getDefaultWidth());
		setHeight(getDefaultHeight());
	}
	
	@Override
	protected void openScreen()
	{
		MC.setScreen(new EditSignTextScreen(MC.screen, setting));
	}
	
	@Override
	protected String getText()
	{
		return setting.getName();
	}
	
	@Override
	protected Setting getSetting()
	{
		return setting;
	}
}

