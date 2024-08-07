package codyhuh.barracks;

import codyhuh.barracks.registry.ModEntities;
import codyhuh.barracks.registry.ModFeatures;
import codyhuh.barracks.registry.ModItems;
import codyhuh.barracks.registry.ModMenus;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.AbstractFish;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegistryObject;

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
