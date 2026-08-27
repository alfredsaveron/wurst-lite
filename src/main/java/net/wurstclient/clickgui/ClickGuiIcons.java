/*
 * Copyright (c) 2014-2026 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.clickgui;

import org.joml.Matrix4f;

import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.wurstclient.WurstRenderLayers;
import net.wurstclient.util.RenderUtils;

public enum ClickGuiIcons
{
	;
	
	public static void drawMinimizeArrow(GuiGraphics context, float x1,
		float y1, float x2, float y2, boolean hovering, boolean minimized)
	{
		float size = 5;
		float cx = (x1 + x2) / 2F;
		float cy = (y1 + y2) / 2F;
		int color = hovering ? 0xFF1EBB97 : 0xFF8E9094;
		
		if(minimized)
		{
			float xa1 = cx - size / 2F;
			float xa3 = cx + size / 2F;
			float ya1 = cy - size / 4F;
			float ya2 = cy + size / 4F;
			RenderUtils.drawLine2D(context, xa1, ya1, cx, ya2, color);
			RenderUtils.drawLine2D(context, cx, ya2, xa3, ya1, color);
		}else
		{
			float xa1 = cx - size / 2F;
			float xa3 = cx + size / 2F;
			float ya1 = cy + size / 4F;
			float ya2 = cy - size / 4F;
			RenderUtils.drawLine2D(context, xa1, ya1, cx, ya2, color);
			RenderUtils.drawLine2D(context, cx, ya2, xa3, ya1, color);
		}
	}
	
	public static void drawPin(GuiGraphics context, float x1, float y1,
		float x2, float y2, boolean hovering, boolean pinned)
	{
		int needleColor = hovering ? 0xFFFFFFFF : 0xFFD9D9D9;
		int outlineColor = 0x80101010;
		
		if(pinned)
		{
			float xk1 = x1 + 2;
			float xk2 = x2 - 2;
			float xk3 = x1 + 1;
			float xk4 = x2 - 1;
			float yk1 = y1 + 2;
			float yk2 = y2 - 2;
			float yk3 = y2 - 0.5F;
			
			int knobColor = hovering ? 0xFFFF0000 : 0xFFD90000;
			RenderUtils.fill2D(context, xk1, yk1, xk2, yk2, knobColor);
			RenderUtils.fill2D(context, xk3, yk2, xk4, yk3, knobColor);
			
			float xn1 = x1 + 3.5F;
			float xn2 = x2 - 3.5F;
			float yn1 = y2 - 0.5F;
			float yn2 = y2;
			
			RenderUtils.fill2D(context, xn1, yn1, xn2, yn2, needleColor);
			
			RenderUtils.drawBorder2D(context, xk1, yk1, xk2, yk2, outlineColor);
			RenderUtils.drawBorder2D(context, xk3, yk2, xk4, yk3, outlineColor);
			RenderUtils.drawBorder2D(context, xn1, yn1, xn2, yn2, outlineColor);
			
		}else
		{
			float xk1 = x2 - 3.5F;
			float xk2 = x2 - 0.5F;
			float xk3 = x2 - 3;
			float xk4 = x1 + 3;
			float xk5 = x1 + 2;
			float xk6 = x2 - 2;
			float xk7 = x1 + 1;
			float yk1 = y1 + 0.5F;
			float yk2 = y1 + 3.5F;
			float yk3 = y2 - 3;
			float yk4 = y1 + 3;
			float yk5 = y1 + 2;
			float yk6 = y2 - 2;
			float yk7 = y2 - 1;
			
			int knobColor = hovering ? 0xFF00FF00 : 0xFF00D900;
			Matrix4f matrix = context.pose().last().pose();
			
			VertexConsumer guiBuffer =
				RenderUtils.getVCP().getBuffer(RenderType.gui());
			guiBuffer.addVertex(matrix, xk4, yk4, 0).setColor(knobColor);
			guiBuffer.addVertex(matrix, xk3, yk3, 0).setColor(knobColor);
			guiBuffer.addVertex(matrix, xk2, yk2, 0).setColor(knobColor);
			guiBuffer.addVertex(matrix, xk1, yk1, 0).setColor(knobColor);
			guiBuffer.addVertex(matrix, xk5, yk5, 0).setColor(knobColor);
			guiBuffer.addVertex(matrix, xk7, yk4, 0).setColor(knobColor);
			guiBuffer.addVertex(matrix, xk3, yk7, 0).setColor(knobColor);
			guiBuffer.addVertex(matrix, xk6, yk6, 0).setColor(knobColor);
			
			float xn1 = x1 + 3;
			float xn2 = x1 + 4;
			float xn3 = x1 + 1;
			float yn1 = y2 - 4;
			float yn2 = y2 - 3;
			float yn3 = y2 - 1;
			
			VertexConsumer debugBuffer = RenderUtils.getVCP()
				.getBuffer(RenderType.debugFilledBox());
			debugBuffer.addVertex(matrix, xn3, yn3, 1).setColor(needleColor);
			debugBuffer.addVertex(matrix, xn2, yn2, 1).setColor(needleColor);
			debugBuffer.addVertex(matrix, xn1, yn1, 1).setColor(needleColor);
			
			VertexConsumer lineStripBuffer = RenderUtils.getVCP()
				.getBuffer(WurstRenderLayers.ONE_PIXEL_LINE_STRIP);
			lineStripBuffer.addVertex(matrix, xk4, yk4, 1).setColor(outlineColor);
			lineStripBuffer.addVertex(matrix, xk3, yk3, 1).setColor(outlineColor);
			lineStripBuffer.addVertex(matrix, xk2, yk2, 1).setColor(outlineColor);
			lineStripBuffer.addVertex(matrix, xk1, yk1, 1).setColor(outlineColor);
			lineStripBuffer.addVertex(matrix, xk4, yk4, 1).setColor(outlineColor);
			
			lineStripBuffer.addVertex(matrix, xk5, yk5, 1).setColor(outlineColor);
			lineStripBuffer.addVertex(matrix, xk7, yk4, 1).setColor(outlineColor);
			lineStripBuffer.addVertex(matrix, xk3, yk7, 1).setColor(outlineColor);
			lineStripBuffer.addVertex(matrix, xk6, yk6, 1).setColor(outlineColor);
			lineStripBuffer.addVertex(matrix, xk5, yk5, 1).setColor(outlineColor);
			
			lineStripBuffer.addVertex(matrix, xn3, yn3, 1).setColor(outlineColor);
			lineStripBuffer.addVertex(matrix, xn2, yn2, 1).setColor(outlineColor);
			lineStripBuffer.addVertex(matrix, xn1, yn1, 1).setColor(outlineColor);
			lineStripBuffer.addVertex(matrix, xn3, yn3, 1).setColor(outlineColor);
		}
	}
	
	public static void drawCheck(GuiGraphics context, float x1, float y1,
		float x2, float y2, boolean hovering, boolean grayedOut)
	{
		float xc1 = x1 + 2.5F;
		float xc2 = x1 + 3.5F;
		float xc3 = (x1 + x2) / 2 - 1;
		float xc4 = x2 - 3.5F;
		float xc5 = x2 - 2.5F;
		float yc1 = y1 + 2.5F;
		float yc2 = y1 + 3.5F;
		float yc3 = (y1 + y2) / 2;
		float yc4 = yc3 + 1;
		float yc5 = y2 - 4.5F;
		float yc6 = y2 - 2.5F;
		
		int checkColor =
			grayedOut ? 0xC0808080 : hovering ? 0xFFF2D5F7 : 0xFFD8A8DC;
		Matrix4f matrix = context.pose().last().pose();
		
		VertexConsumer guiBuffer =
			RenderUtils.getVCP().getBuffer(RenderType.gui());
		guiBuffer.addVertex(matrix, xc2, yc3, 0).setColor(checkColor);
		guiBuffer.addVertex(matrix, xc1, yc4, 0).setColor(checkColor);
		guiBuffer.addVertex(matrix, xc3, yc6, 0).setColor(checkColor);
		guiBuffer.addVertex(matrix, xc3, yc5, 0).setColor(checkColor);
		guiBuffer.addVertex(matrix, xc3, yc5, 0).setColor(checkColor);
		guiBuffer.addVertex(matrix, xc3, yc6, 0).setColor(checkColor);
		guiBuffer.addVertex(matrix, xc5, yc2, 0).setColor(checkColor);
		guiBuffer.addVertex(matrix, xc4, yc1, 0).setColor(checkColor);
		
		int outlineColor = 0x80101010;
		VertexConsumer lineStripBuffer = RenderUtils.getVCP()
			.getBuffer(WurstRenderLayers.ONE_PIXEL_LINE_STRIP);
		lineStripBuffer.addVertex(matrix, xc2, yc3, 1).setColor(outlineColor);
		lineStripBuffer.addVertex(matrix, xc3, yc5, 1).setColor(outlineColor);
		lineStripBuffer.addVertex(matrix, xc4, yc1, 1).setColor(outlineColor);
		lineStripBuffer.addVertex(matrix, xc5, yc2, 1).setColor(outlineColor);
		lineStripBuffer.addVertex(matrix, xc3, yc6, 1).setColor(outlineColor);
		lineStripBuffer.addVertex(matrix, xc1, yc4, 1).setColor(outlineColor);
		lineStripBuffer.addVertex(matrix, xc2, yc3, 1).setColor(outlineColor);
		lineStripBuffer.addVertex(matrix, xc2, yc3, 1).setColor(outlineColor);
	}
	
	public static void drawIndeterminateCheck(GuiGraphics context, float x1,
		float y1, float x2, float y2, boolean hovering, boolean grayedOut)
	{
		float xc1 = x1 + 2.5F;
		float xc2 = x2 - 2.5F;
		float yc1 = y1 + 2.5F;
		float yc2 = y2 - 2.5F;
		
		int checkColor =
			grayedOut ? 0xC0808080 : hovering ? 0xFF00FF00 : 0xFF00D900;
		RenderUtils.fill2D(context, xc1, yc1, xc2, yc2, checkColor);
		
		int outlineColor = 0x80101010;
		RenderUtils.drawBorder2D(context, xc1, yc1, xc2, yc2, outlineColor);
	}
	
	public static void drawCross(GuiGraphics context, float x1, float y1,
		float x2, float y2, boolean hovering)
	{
		float xc1 = x1 + 2;
		float xc2 = x1 + 3;
		float xc3 = x2 - 2;
		float xc4 = x2 - 3;
		float xc5 = x1 + 3.5F;
		float xc6 = (x1 + x2) / 2;
		float xc7 = x2 - 3.5F;
		float yc1 = y1 + 3;
		float yc2 = y1 + 2;
		float yc3 = y2 - 3;
		float yc4 = y2 - 2;
		float yc5 = y1 + 3.5F;
		float yc6 = (y1 + y2) / 2;
		float yc7 = y2 - 3.5F;
		
		int crossColor = hovering ? 0xFFFF0000 : 0xFFD90000;
		Matrix4f matrix = context.pose().last().pose();
		
		VertexConsumer guiBuffer =
			RenderUtils.getVCP().getBuffer(RenderType.gui());
		guiBuffer.addVertex(matrix, xc2, yc2, 0).setColor(crossColor);
		guiBuffer.addVertex(matrix, xc1, yc1, 0).setColor(crossColor);
		guiBuffer.addVertex(matrix, xc4, yc4, 0).setColor(crossColor);
		guiBuffer.addVertex(matrix, xc3, yc3, 0).setColor(crossColor);
		guiBuffer.addVertex(matrix, xc3, yc1, 0).setColor(crossColor);
		guiBuffer.addVertex(matrix, xc4, yc2, 0).setColor(crossColor);
		guiBuffer.addVertex(matrix, xc6, yc5, 0).setColor(crossColor);
		guiBuffer.addVertex(matrix, xc7, yc6, 0).setColor(crossColor);
		guiBuffer.addVertex(matrix, xc6, yc7, 0).setColor(crossColor);
		guiBuffer.addVertex(matrix, xc5, yc6, 0).setColor(crossColor);
		guiBuffer.addVertex(matrix, xc1, yc3, 0).setColor(crossColor);
		guiBuffer.addVertex(matrix, xc2, yc4, 0).setColor(crossColor);
		
		int outlineColor = 0x80101010;
		VertexConsumer lineStripBuffer = RenderUtils.getVCP()
			.getBuffer(WurstRenderLayers.ONE_PIXEL_LINE_STRIP);
		lineStripBuffer.addVertex(matrix, xc1, yc1, 1).setColor(outlineColor);
		lineStripBuffer.addVertex(matrix, xc2, yc2, 1).setColor(outlineColor);
		lineStripBuffer.addVertex(matrix, xc6, yc5, 1).setColor(outlineColor);
		lineStripBuffer.addVertex(matrix, xc4, yc2, 1).setColor(outlineColor);
		lineStripBuffer.addVertex(matrix, xc3, yc1, 1).setColor(outlineColor);
		lineStripBuffer.addVertex(matrix, xc7, yc6, 1).setColor(outlineColor);
		lineStripBuffer.addVertex(matrix, xc3, yc3, 1).setColor(outlineColor);
		lineStripBuffer.addVertex(matrix, xc4, yc4, 1).setColor(outlineColor);
		lineStripBuffer.addVertex(matrix, xc6, yc7, 1).setColor(outlineColor);
		lineStripBuffer.addVertex(matrix, xc2, yc4, 1).setColor(outlineColor);
		lineStripBuffer.addVertex(matrix, xc1, yc3, 1).setColor(outlineColor);
		lineStripBuffer.addVertex(matrix, xc5, yc6, 1).setColor(outlineColor);
		lineStripBuffer.addVertex(matrix, xc1, yc1, 1).setColor(outlineColor);
	}
}
