package codyhuh.barracks.registry;

import codyhuh.barracks.Barracks;
import codyhuh.barracks.common.entities.AllegiantArrow;
import codyhuh.barracks.common.entities.Faa;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Barracks.MOD_ID);

    public static final RegistryObject<EntityType<AllegiantArrow>> ALLEGIANT_ARROW = ENTITIES.register("allegiant_arrow", () -> EntityType.Builder.<AllegiantArrow>of(AllegiantArrow::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20).build("allegiant_arrow"));
    public static final RegistryObject<EntityType<Faa>> FAA = ENTITIES.register("faa", () -> EntityType.Builder.of(Faa::new, MobCategory.WATER_AMBIENT).sized(0.25F, 0.25F).build("faa"));
}
