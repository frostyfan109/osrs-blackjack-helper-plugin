package net.runelite.client.plugins.thievinghelper;

import net.runelite.api.Client;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.components.PanelComponent;

import javax.inject.Inject;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ThievingHelperOverlay extends Overlay {
    private final static int MENU_HEIGHT = 16;
    private final static Color[] colors = new Color[]{
            Color.GREEN,
            Color.MAGENTA,
            Color.BLUE
    };

    private final ThievingHelperPlugin plugin;
    private final PanelComponent panelComponent = new PanelComponent();

    @Inject
    private Client client;

    @Inject
    public ThievingHelperOverlay(ThievingHelperPlugin plugin) {
        this.plugin = plugin;

        setPosition(OverlayPosition.DETACHED);
        setLayer(OverlayLayer.ALWAYS_ON_TOP);
        setPriority(OverlayPriority.HIGH);
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        panelComponent.setPreferredSize(new Dimension(145, 0));
        panelComponent.getChildren().clear();

        // Menu entries remain allocated regardless of if they are rendered
        if (client.isMenuOpen()) {
            MenuEntry[] menuEntries = client.getMenuEntries();

            HashMap<Integer, ArrayList<MenuAction>> highlighted = plugin.getHighlighted();
            for (Map.Entry<Integer, ArrayList<MenuAction>> entry : highlighted.entrySet()) {
                Integer npcID = entry.getKey();
                ArrayList<MenuAction> highlightedOpcodes = entry.getValue();
                for (int i = 0; i < menuEntries.length; i++) {
                    MenuEntry menuEntry = menuEntries[i];
                    if (menuEntry.getIdentifier() == npcID && highlightedOpcodes.contains(menuEntry.getMenuAction())) {
                        highlightEntry(graphics, menuEntries.length - i - 1);
                    }
                }
            }
        }
        return panelComponent.render(graphics);
    }

    private void highlightEntry(Graphics2D graphics, int index) {
        int x = client.getMenuX();
        // Index + 1 to account for fake "Choose option" entry
        int y = client.getMenuY() + (MENU_HEIGHT * (index + 1));
        int width = client.getMenuWidth();
        int height = MENU_HEIGHT;

        int failStreak = plugin.getFailStreak();
        // Cycle through available colors - reusing colors is perfectly acceptable as it just needs to
        // change colors from the previous failure
        Color color = colors[failStreak % colors.length];
        graphics.setColor(color);
        graphics.drawRect(
                x,
                y,
                width,
                height
        );
    }
}
