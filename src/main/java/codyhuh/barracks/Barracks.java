package codyhuh.barracks;

import codyhuh.barracks.data.BarracksModdedBiomeSlices;
import codyhuh.barracks.registry.*;
import com.teamabnormals.blueprint.core.registry.BlueprintDataPackRegistries;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.AbstractFish;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Mod(Barracks.MOD_ID)
public class Barracks {
    public static final String MOD_ID = "barracks";

    public Barracks() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        ModEntities.ENTITIES.register(bus);
        ModItems.ITEMS.register(bus);
        ModMenus.MENUS.register(bus);
        ModFeatures.FEATURES.register(bus);

        bus.addListener(this::populateTabs);
        bus.addListener(this::createAttributes);
        bus.addListener(this::dataSetup);
    }


    private void dataSetup(final GatherDataEvent event) {
        final RegistrySetBuilder BUILDER = new RegistrySetBuilder().add(Registries.BIOME, ModBiomes::bootstrap).add(BlueprintDataPackRegistries.MODDED_BIOME_SLICES, BarracksModdedBiomeSlices::bootstrap);

        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        boolean server = event.includeServer();

        DatapackBuiltinEntriesProvider datapackBuiltinEntriesProvider = new DatapackBuiltinEntriesProvider(packOutput, lookupProvider, BUILDER, Set.of(Barracks.MOD_ID));
        generator.addProvider(server, datapackBuiltinEntriesProvider);
    }

    private void createAttributes(EntityAttributeCreationEvent e) {
        e.put(ModEntities.FAA.get(), AbstractFish.createAttributes().build());
    }

    private void populateTabs(BuildCreativeModeTabContentsEvent e) {
        if (e.getTabKey() == CreativeModeTabs.COMBAT) {
            for (RegistryObject<Item> item :ModItems.ITEMS.getEntries()) {
                e.accept(item.get());
            }
        }
    }

    // todo- add Util.getMillis() check for if the entity has changed in the last 1/4 of a second, and if not don't update hit result
    public static EntityHitResult getLookAtEntity(Player player, Level level, double range) {
        Vec3 eyePos = player.getEyePosition(1.0F);
        Vec3 viewVec = player.getViewVector(1.0F);
        Vec3 endVec = eyePos.add(viewVec.x * range, viewVec.y * range, viewVec.z * range);

        return ProjectileUtil.getEntityHitResult(level, player, eyePos, endVec, player.getBoundingBox().inflate(range), e -> e instanceof LivingEntity);
    }
}
