/*
 * Copyright (c) 2014-2026 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package kaptainwutax.seedcrackerX.mixin;

import kaptainwutax.seedcrackerX.SeedCracker;
import kaptainwutax.seedcrackerX.config.StructureSave;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.WritableLevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientLevel.class)
public abstract class ClientLevelMixin extends Level
{
	
	protected ClientLevelMixin(WritableLevelData properties,
		ResourceKey<Level> registryRef, RegistryAccess registryManager,
		Holder<DimensionType> dimensionEntry,
		java.util.function.Supplier<net.minecraft.util.profiling.ProfilerFiller> profiler,
		boolean isClient, boolean debugWorld, long biomeAccess,
		int maxChainedNeighborUpdates)
	{
		super(properties, registryRef, registryManager, dimensionEntry,
			profiler, isClient, debugWorld, biomeAccess,
			maxChainedNeighborUpdates);
	}
	
	@Inject(method = "disconnect", at = @At("HEAD"))
	private void disconnect(Component reason, CallbackInfo ci)
	{
		StructureSave
			.saveStructures(SeedCracker.get().getDataStorage().baseSeedData);
		SeedCracker.get().reset();
	}
	
	@Inject(method = "getUncachedNoiseBiome",
		at = @At("HEAD"),
		cancellable = true)
	private void getGeneratorStoredBiome(int x, int y, int z,
		CallbackInfoReturnable<Holder<Biome>> ci)
	{
		var biome = registryAccess().lookupOrThrow(Registries.BIOME)
			.get(Biomes.THE_VOID);
		biome.ifPresent(ci::setReturnValue);
	}
}
