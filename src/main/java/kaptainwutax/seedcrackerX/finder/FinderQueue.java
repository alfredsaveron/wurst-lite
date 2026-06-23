/*
 * Copyright (c) 2014-2026 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package kaptainwutax.seedcrackerX.finder;

import com.mojang.blaze3d.vertex.PoseStack;
import kaptainwutax.seedcrackerX.config.Config;
import kaptainwutax.seedcrackerX.render.Cuboid;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class FinderQueue
{
	private final static FinderQueue INSTANCE = new FinderQueue();
	private static final Logger log =
		LoggerFactory.getLogger(FinderQueue.class);
	public static ExecutorService SERVICE = Executors.newFixedThreadPool(5);
	
	public FinderControl finderControl = new FinderControl();
	private final Set<Cuboid> cuboidsToRender = new HashSet<>();
	
	private FinderQueue()
	{
		this.clear();
	}
	
	public static void registerEvents()
	{
		WorldRenderEvents.LAST.register(worldRenderContext -> {
			FinderQueue.get().extractCuboids(worldRenderContext.camera());
			FinderQueue.get().renderCuboids(worldRenderContext.consumers(),
				worldRenderContext.matrixStack());
		});
	}
	
	public static FinderQueue get()
	{
		return INSTANCE;
	}
	
	public void onChunkData(Level world, ChunkPos chunkPos)
	{
		if(!Config.get().active)
			return;
		if(!world.dimensionType().natural())
			return;
		
		getActiveFinderTypes().forEach(type -> {
			SERVICE.submit(() -> {
				try
				{
					List<Finder> finders =
						type.finderBuilder.build(world, chunkPos);
					
					finders.forEach(finder -> {
						if(finder.isValidDimension(world.dimensionType()))
						{
							finder.findInChunk();
							this.finderControl.addFinder(type, finder);
						}
					});
				}catch(Exception e)
				{
					e.printStackTrace();
				}
			});
		});
	}
	
	private void extractCuboids(Camera camera)
	{
		cuboidsToRender.clear();
		if(Config.get().render == Config.RenderType.OFF)
		{
			return;
		}
		this.finderControl.getActiveFinders().forEach(finder -> {
			if(finder.shouldRender())
			{
				finder.cuboids.forEach(
					cuboid -> cuboidsToRender.add(cuboid.offset(camera)));
			}
		});
	}
	
	public void renderCuboids(MultiBufferSource bufferSource,
		PoseStack poseStack)
	{
		if(cuboidsToRender.isEmpty())
		{
			return;
		}
		cuboidsToRender
			.forEach(cuboid -> cuboid.render(poseStack, bufferSource));
	}
	
	public List<Finder.Type> getActiveFinderTypes()
	{
		return Arrays.stream(Finder.Type.values())
			.filter(type -> type.enabled.get()).collect(Collectors.toList());
	}
	
	public void clear()
	{
		this.finderControl = new FinderControl();
	}
}
