package codyhuh.barracks.client.screen;

import codyhuh.barracks.Barracks;
import codyhuh.barracks.common.menus.LetterMenu;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.controls.KeyBindsList;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundRenameItemPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;

public class LetterScreen extends AbstractContainerScreen<LetterMenu> {
    private static final ResourceLocation INV = new ResourceLocation(Barracks.MOD_ID, "textures/gui/letter.png");
    private final Component title = Component.translatable("menu.barracks.gui");
    private EditBox name;

    public LetterScreen(LetterMenu menu, Inventory inv, Component component) {
        super(menu, inv, component);
    }

    @Override
    public void containerTick() {
        super.containerTick();
        this.name.tick();
    }

    @Override
    protected void init() {
        super.init();
        //this.minecraft.keyboardHandler.setSendRepeatsToGui(true);
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        this.name = new EditBox(this.font, i + 36, j + 18, 103, 12, Component.translatable("container.repair"));
        this.name.setCanLoseFocus(false);
        this.name.setTextColor(-1);
        this.name.setTextColorUneditable(-1);
        this.name.setBordered(false);
        this.name.setMaxLength(50);
        this.name.setResponder(this::onNameChanged);
        this.name.setValue("");
        this.addWidget(this.name);
        this.name.setEditable(false);
    }

    @Override
    public void resize(Minecraft pMinecraft, int pWidth, int pHeight) {
        String s = this.name.getValue();
        this.init(pMinecraft, pWidth, pHeight);
        this.name.setValue(s);
    }

    @Override
    public void removed() {
        super.removed();
        //this.minecraft.keyboardHandler.setSendRepeatsToGui(false);
    }

    @Override
    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
        if (pKeyCode == 256) {
            this.minecraft.player.closeContainer();
        }

        return this.name.keyPressed(pKeyCode, pScanCode, pModifiers) || this.name.canConsumeInput() || super.keyPressed(pKeyCode, pScanCode, pModifiers);
    }

    private void onNameChanged(String name) {
        if (!name.isEmpty()) {
            String s = name;
            Slot slot = this.menu.getSlot(0);
            if (slot.hasItem() && !slot.getItem().hasCustomHoverName() && name.equals(slot.getItem().getHoverName().getString())) {
                s = "";
            }

            this.minecraft.player.connection.send(new ServerboundRenameItemPacket(s));
        }
    }

    @Override
    protected void renderBg(GuiGraphics stack, float partialTick, int x, int y) {
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, INV);

        int i = this.leftPos;
        int j = this.topPos;

        stack.blit(INV, i, j, 0, 0, this.imageWidth, this.imageHeight);

        stack.blit(INV, i + 33, j + 14, 0, this.imageHeight + (this.name.isFocused() ? 0 : 16), 110, 16);

    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int pButton) {
        return super.mouseClicked(mouseX, mouseY, pButton);
    }

    private void renderLines(GuiGraphics graphics, int mouseX, int mouseY) {
        graphics.fill(RenderType.guiOverlay(), mouseX, mouseX + 1, mouseY, mouseY + 1,0xff0000);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(graphics, mouseX, mouseY);
        renderLines(graphics, mouseX, mouseY);

        //this.name.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int x, int y) {
        graphics.drawString(font, playerInventoryTitle, inventoryLabelX, inventoryLabelY, 4210752, false);

        this.titleLabelY = (imageWidth - font.width(title)) / 2;

        //graphics.drawString(font, title, titleLabelY, titleLabelX - 2, 4210752, false);
    }
}