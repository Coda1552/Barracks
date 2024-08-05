package codyhuh.barracks.client.screen;

import codyhuh.barracks.Barracks;
import com.google.common.collect.ImmutableList;
import net.minecraft.ChatFormatting;
import net.minecraft.client.GameNarrator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.BookViewScreen;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.WrittenBookItem;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.IntFunction;

public class CritterpediaScreen extends Screen {
    public static final CritterpediaScreen.BookAccess EMPTY_ACCESS = new CritterpediaScreen.BookAccess() {
        public int getPageCount() {
            return 0;
        }

        public FormattedText getPageRaw(int p_98306_) {
            return FormattedText.EMPTY;
        }
    };
    public static final ResourceLocation BOOK_LOCATION = new ResourceLocation(Barracks.MOD_ID, "textures/gui/critterpedia.png");
    private CritterpediaScreen.BookAccess bookAccess;
    private int currentPage;
    private List<FormattedCharSequence> cachedPageComponents = Collections.emptyList();
    private int cachedPage = -1;
    private Component pageMsg = CommonComponents.EMPTY;
    private CritterpediaButton forwardButton;
    private CritterpediaButton backButton;
    private final boolean playTurnSound;

    public CritterpediaScreen(CritterpediaScreen.BookAccess pBookAccess) {
        this(pBookAccess, true);
    }

    public CritterpediaScreen() {
        this(EMPTY_ACCESS, false);
    }

    private CritterpediaScreen(CritterpediaScreen.BookAccess pBookAccess, boolean pPlayTurnSound) {
        super(GameNarrator.NO_TITLE);
        this.bookAccess = pBookAccess;
        this.playTurnSound = pPlayTurnSound;
    }

    public void setBookAccess(CritterpediaScreen.BookAccess pBookAccess) {
        this.bookAccess = pBookAccess;
        this.currentPage = Mth.clamp(this.currentPage, 0, pBookAccess.getPageCount());
        this.updateButtonVisibility();
        this.cachedPage = -1;
    }

    public boolean setPage(int pPageNum) {
        int i = Mth.clamp(pPageNum, 0, this.bookAccess.getPageCount() - 1);
        if (i != this.currentPage) {
            this.currentPage = i;
            this.updateButtonVisibility();
            this.cachedPage = -1;
            return true;
        } else {
            return false;
        }
    }

    protected void init() {
        //this.createMenuControls();
        this.createPageControlButtons();
    }

    protected void createMenuControls() {
        this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, (p_289629_) -> {
            this.onClose();
        }).bounds(this.width / 2 - 100, 196, 200, 20).build());
    }

    protected void createPageControlButtons() {
        int i = (this.width - 320) / 2;
        int j = 2;
        this.forwardButton = this.addRenderableWidget(new CritterpediaButton(i + 265, 230, true, (p_98297_) -> {
            this.pageForward();
        }, this.playTurnSound));
        this.backButton = this.addRenderableWidget(new CritterpediaButton(i + 31, 230, false, (p_98287_) -> {
            this.pageBack();
        }, this.playTurnSound));
        this.updateButtonVisibility();
    }

    private int getNumPages() {
        return this.bookAccess.getPageCount();
    }

    protected void pageBack() {
        if (this.currentPage > 0) {
            --this.currentPage;
        }

        this.updateButtonVisibility();
    }

    protected void pageForward() {
        if (this.currentPage < this.getNumPages() - 1) {
            ++this.currentPage;
        }

        this.updateButtonVisibility();
    }

    private void updateButtonVisibility() {
        this.forwardButton.visible = this.currentPage < this.getNumPages() - 1;
        this.backButton.visible = this.currentPage > 0;
    }

    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
        if (super.keyPressed(pKeyCode, pScanCode, pModifiers)) {
            return true;
        } else {
            switch (pKeyCode) {
                case 266:
                    this.backButton.onPress();
                    return true;
                case 267:
                    this.forwardButton.onPress();
                    return true;
                default:
                    return false;
            }
        }
    }

    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics);
        int i = (this.width - 320) / 2;
        int j = (this.height - 192) / 2;
        graphics.blit(BOOK_LOCATION, i, j, 0, 0, 320, 192, 512, 512);
        if (this.cachedPage != this.currentPage) {
            FormattedText formattedtext = this.bookAccess.getPage(this.currentPage);
            this.cachedPageComponents = this.font.split(formattedtext, 114);
            this.pageMsg = Component.translatable("book.pageIndicator", this.currentPage + 1, Math.max(this.getNumPages(), 1));
        }

        this.cachedPage = this.currentPage;
        int i1 = this.font.width(this.pageMsg);
        //graphics.drawString(this.font, this.pageMsg, i - i1 + 192 - 44, 18, 0, false);
        int k = Math.min(128 / 9, this.cachedPageComponents.size());

        for(int l = 0; l < k; ++l) {
            FormattedCharSequence formattedcharsequence = this.cachedPageComponents.get(l);
            graphics.drawString(this.font, formattedcharsequence, i + 36, 32 + l * 9, 0, false);
        }

        Style style = this.getClickedComponentStyleAt(mouseX, mouseY);
        if (style != null) {
            graphics.renderComponentHoverEffect(this.font, style, mouseX, mouseY);
        }

        super.render(graphics, mouseX, mouseY, partialTick);
    }

    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        if (pButton == 0) {
            Style style = this.getClickedComponentStyleAt(pMouseX, pMouseY);
            if (style != null && this.handleComponentClicked(style)) {
                return true;
            }
        }

        return super.mouseClicked(pMouseX, pMouseY, pButton);
    }

    public boolean handleComponentClicked(Style pStyle) {
        ClickEvent clickevent = pStyle.getClickEvent();
        if (clickevent == null) {
            return false;
        } else if (clickevent.getAction() == ClickEvent.Action.CHANGE_PAGE) {
            String s = clickevent.getValue();

            try {
                int i = Integer.parseInt(s) - 1;
                return this.setPage(i);
            } catch (Exception exception) {
                return false;
            }
        } else {
            boolean flag = super.handleComponentClicked(pStyle);
            if (flag && clickevent.getAction() == ClickEvent.Action.RUN_COMMAND) {
                this.closeScreen();
            }

            return flag;
        }
    }

    protected void closeScreen() {
        this.minecraft.setScreen(null);
    }

    @Nullable
    public Style getClickedComponentStyleAt(double pMouseX, double pMouseY) {
        if (this.cachedPageComponents.isEmpty()) {
            return null;
        } else {
            int i = Mth.floor(pMouseX - (double)((this.width - 320) / 2) - 36.0D);
            int j = Mth.floor(pMouseY - 2.0D - 30.0D);
            if (i >= 0 && j >= 0) {
                int k = Math.min(128 / 9, this.cachedPageComponents.size());
                if (i <= 114 && j < 9 * k + k) {
                    int l = j / 9;
                    if (l >= 0 && l < this.cachedPageComponents.size()) {
                        FormattedCharSequence formattedcharsequence = this.cachedPageComponents.get(l);
                        return this.minecraft.font.getSplitter().componentStyleAtWidth(formattedcharsequence, i);
                    } else {
                        return null;
                    }
                } else {
                    return null;
                }
            } else {
                return null;
            }
        }
    }

    static List<String> loadPages(Set<Map.Entry<ResourceKey<EntityType<?>>, EntityType<?>>> entries) {
        ImmutableList.Builder<String> builder = ImmutableList.builder();
        loadPages(entries, builder::add);
        return builder.build();
    }

    public static void loadPages(Set<Map.Entry<ResourceKey<EntityType<?>>, EntityType<?>>> entries, Consumer<String> pConsumer) {
    }

    public interface BookAccess {

        int getPageCount();

        FormattedText getPageRaw(int pIndex);

        default FormattedText getPage(int pPage) {
            return pPage >= 0 && pPage < this.getPageCount() ? this.getPageRaw(pPage) : FormattedText.EMPTY;
        }
    }

    public static class WrittenBookAccess implements BookViewScreen.BookAccess {
        private final List<String> pages;

        public WrittenBookAccess() {
            this.pages = readPages();
        }

        private static List<String> readPages() {
            Set<Map.Entry<ResourceKey<EntityType<?>>, EntityType<?>>> entities = ForgeRegistries.ENTITY_TYPES.getEntries();

            return CritterpediaScreen.loadPages(entities);
        }

        public int getPageCount() {
            return this.pages.size();
        }

        public FormattedText getPageRaw(int pIndex) {
            String s = this.pages.get(pIndex);

            try {
                FormattedText formattedtext = Component.Serializer.fromJson(s);
                if (formattedtext != null) {
                    return formattedtext;
                }
            } catch (Exception exception) {
            }

            return FormattedText.of(s);
        }
    }
}