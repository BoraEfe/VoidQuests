package Listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class QuestsGUIListener implements Listener {
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;

        Player p = (Player) event.getWhoClicked();

        if (p.getOpenInventory().getTitle().equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', "&dDaily Quests"))) event.setCancelled(true);
    }
}
