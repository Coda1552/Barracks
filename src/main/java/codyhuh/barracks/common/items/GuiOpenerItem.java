package codyhuh.barracks.common.items;

import codyhuh.barracks.client.screen.CritterpediaScreen;
import codyhuh.barracks.client.screen.LetterScreen;
import codyhuh.barracks.common.menus.LetterMenu;
import codyhuh.barracks.registry.ModMenus;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class GuiOpenerItem extends Item {

    public GuiOpenerItem(Properties builder) {
        super(builder);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {

        if (level.isClientSide) {
            Minecraft.getInstance().setScreen(new CritterpediaScreen());
        }

        return super.use(level, player, hand);
    }
}
