package codyhuh.barracks.client.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.BookViewScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.sounds.SoundEvents;

public class CritterpediaButton extends Button {
    private final boolean isForward;
    private final boolean playTurnSound;

    public CritterpediaButton(int pX, int pY, boolean pIsForward, Button.OnPress pOnPress, boolean pPlayTurnSound) {
        super(pX, pY, 23, 13, CommonComponents.EMPTY, pOnPress, DEFAULT_NARRATION);
        this.isForward = pIsForward;
        this.playTurnSound = pPlayTurnSound;
    }

    public void renderWidget(GuiGraphics p_283468_, int p_282922_, int p_283637_, float p_282459_) {
        int i = 0;
        int j = 192;
        if (this.isHoveredOrFocused()) {
            i += 23;
        }

        if (!this.isForward) {
            j += 13;
        }

        p_283468_.blit(CritterpediaScreen.BOOK_LOCATION, this.getX(), this.getY(), i, j, 23, 13, 512, 512);
    }

    public void playDownSound(SoundManager pHandler) {
        if (this.playTurnSound) {
            pHandler.play(SimpleSoundInstance.forUI(SoundEvents.BOOK_PAGE_TURN, 1.0F));
        }

    }
}