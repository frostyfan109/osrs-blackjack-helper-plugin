package net.runelite.client.plugins.thievinghelper;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.*;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.menus.MenuManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import org.pf4j.Extension;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.HashMap;

@Extension
@PluginDescriptor(
        name = "Thieving Helper",
        description = "Thieving mouse indicator",
        enabledByDefault = false
)
@Singleton
@Slf4j
public class ThievingHelperPlugin extends Plugin {
    // There is a 2 tick window to correct a failed blackjack
    private final static int ACTION_WINDOW = 4;
    private static final String SUCCESS_BLACKJACK = "You smack the bandit over the head and render them unconscious.";
    private static final String FAILED_BLACKJACK = "Your blow only glances off the bandit's head.";
    private static final String PICKPOCKET_ATTEMPT = "You attempt to pick the Menaphite's pocket.";

    @Inject
    private Client client;

    @Inject
    private ThievingHelperOverlay overlay;

    @Inject
    private OverlayManager overlayManager;

    @Inject
    private MenuManager menuManager;

    private int windowRemaining = 0;
    private int targetMenaphite = -1;
    // Tracks number of consecutively failed blackjacks
    // Overlay changes color accordingly to clearly indicate that you have failed again
    @Getter(AccessLevel.PACKAGE)
    private int failStreak = 0;

    @Override
    protected void startUp() {
        overlayManager.add(overlay);
    }

    @Override
    protected void shutDown() {
        resetWindow();

        overlayManager.remove(overlay);
    }

    @Subscribe
    private void onChatMessage(ChatMessage event) {
        String msg = event.getMessage();

        if (event.getType() == ChatMessageType.SPAM) {
            if (msg.equals(FAILED_BLACKJACK)) {
                if (windowRemaining > 0) {
                    failStreak++;
                }
                windowRemaining = ACTION_WINDOW;
            }
            else if (windowRemaining > 0 && msg.equals(PICKPOCKET_ATTEMPT)) {
                resetWindow();
            }
        }
    }

    @Subscribe
    private void onAnimationChanged(AnimationChanged event) {
        Player player = client.getLocalPlayer();
        if (windowRemaining > 0 && windowRemaining != ACTION_WINDOW && event.getActor() == player && player.getAnimation() == 401) {
            resetWindow();
        }
    }

    @Subscribe
    private void onGameTick(GameTick event) {
        if (client.getGameState() == GameState.LOGGED_IN) {
            if (windowRemaining > 1) {
                windowRemaining--;
                if (targetMenaphite == -1) targetMenaphite = client.getLocalPlayer().getRSInteracting();
            }
            else if (windowRemaining == 1) resetWindow();
        }
    }


    protected HashMap getHighlighted() {
        HashMap<Integer, ArrayList<MenuAction>> highlighted = new HashMap();

        if (windowRemaining > 0) {
            highlighted.put(
                targetMenaphite,
                new ArrayList<MenuAction>() {{ add(MenuAction.NPC_FIFTH_OPTION); }}
            );
        }

        return highlighted;
    }

    private void resetWindow() {
        windowRemaining = 0;
        targetMenaphite = -1;
        failStreak = 0;
    }
}
