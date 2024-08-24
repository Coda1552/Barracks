package codyhuh.barracks.registry;

import codyhuh.barracks.Barracks;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.AquaticPlacements;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.Musics;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Barracks.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModBiomes {
    public static final ResourceKey<Biome> SEA_STACKS = createKey("sea_stacks");

    public static void bootstrap(BootstapContext<Biome> context) {
        HolderGetter<PlacedFeature> features = context.lookup(Registries.PLACED_FEATURE);
        HolderGetter<ConfiguredWorldCarver<?>> carvers = context.lookup(Registries.CONFIGURED_CARVER);

        context.register(SEA_STACKS, seaStacks(features, carvers));
    }

    private static ResourceKey<Biome> createKey(String name) {
        return ResourceKey.create(Registries.BIOME, new ResourceLocation(Barracks.MOD_ID, name));
    }

    private static Biome seaStacks(HolderGetter<PlacedFeature> features, HolderGetter<ConfiguredWorldCarver<?>> carvers) {
        BiomeGenerationSettings.Builder biomeGenBuilder = new BiomeGenerationSettings.Builder(features, carvers);
        BiomeDefaultFeatures.addFossilDecoration(biomeGenBuilder);
        globalOverworldGeneration(biomeGenBuilder);
        BiomeDefaultFeatures.addDefaultOres(biomeGenBuilder);
        BiomeDefaultFeatures.addDefaultSoftDisks(biomeGenBuilder);

        biomeGenBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, AquaticPlacements.SEAGRASS_WARM);

        MobSpawnSettings.Builder mobSpawnBuilder = new MobSpawnSettings.Builder();
        BiomeDefaultFeatures.warmOceanSpawns(mobSpawnBuilder, 10, 4);
        mobSpawnBuilder.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.PARROT, 6, 2, 4));

        return biome(true, 0.5F, 1.5F, biomeGenBuilder, mobSpawnBuilder)
                .specialEffects(new BiomeSpecialEffects.Builder()
                        .backgroundMusic(Musics.createGameMusic(SoundEvents.MUSIC_BIOME_JUNGLE))
                        .waterColor(4445678)
                        .waterFogColor(270131)
                        .fogColor(12638463)
                        .skyColor(calculateSkyColor(1.5F))
                        .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                        .build()
                )
                .build();
    }

    private static Biome.BiomeBuilder biome(boolean hasPrecipitation, float downfall, float temperature, BiomeGenerationSettings.Builder biomeGenBuilder, MobSpawnSettings.Builder mobSpawnBuilder) {
        return new Biome.BiomeBuilder().hasPrecipitation(hasPrecipitation).downfall(downfall).temperature(temperature).generationSettings(biomeGenBuilder.build()).mobSpawnSettings(mobSpawnBuilder.build());
    }

    protected static int calculateSkyColor(float temperature) {
        float i = temperature / 3.0F;
        i = Mth.clamp(i, -1.0F, 1.0F);
        return Mth.hsvToRgb(0.62222224F - i * 0.05F, 0.5F + i * 0.1F, 1.0F);
    }

    private static void globalOverworldGeneration(BiomeGenerationSettings.Builder biomeGenBuilder) {
        BiomeDefaultFeatures.addDefaultCarversAndLakes(biomeGenBuilder);
        BiomeDefaultFeatures.addDefaultCrystalFormations(biomeGenBuilder);
        BiomeDefaultFeatures.addDefaultMonsterRoom(biomeGenBuilder);
        BiomeDefaultFeatures.addDefaultUndergroundVariety(biomeGenBuilder);
        BiomeDefaultFeatures.addDefaultSprings(biomeGenBuilder);
        BiomeDefaultFeatures.addSurfaceFreezing(biomeGenBuilder);
    }
}
