/*
 * Copyright (c) 2014-2026 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package kaptainwutax.seedcrackerX.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.AABB;

public class Cuboid
{
	private final AABB box;
	private final int argb;
	private final BlockPos centerPos;
	
	public Cuboid(AABB box, int argb)
	{
		this.box = box;
		this.argb = argb;
		this.centerPos = BlockPos.containing(box.getCenter());
	}
	
	public Cuboid(BoundingBox boundingBox, int argb)
	{
		this(AABB.of(boundingBox), argb);
	}
	
	public Cuboid(BlockPos pos, int argb)
	{
		this(new AABB(pos), argb);
	}
	
	public Cuboid(BlockPos pos, Vec3i size, int argb)
	{
		this(AABB.encapsulatingFullBlocks(pos, pos.offset(size)), argb);
	}
	
	public BlockPos getCenterPos()
	{
		return this.centerPos;
	}
	
	public Cuboid offset(Camera camera)
	{
		return new Cuboid(this.box.move(camera.getPosition().scale(-1)),
			this.argb);
	}
	
	public void render(PoseStack poseStack, MultiBufferSource bufferSource)
	{
		net.wurstclient.util.RenderUtils.drawOutlinedBox(poseStack,
			bufferSource
				.getBuffer(net.wurstclient.WurstRenderLayers.getLines(false)),
			this.box, this.argb);
	}
}
