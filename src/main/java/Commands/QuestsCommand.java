package Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class QuestsCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("quests")){
            if (!(sender instanceof Player)){
                sender.sendMessage(ChatColor.RED + "Only players can use this command!");
                return true;
            }

            Player p = (Player) sender;
            String pName = p.getName();

            openQuestInventory(p);
        }
        return true;
    }

    public static void openQuestInventory(Player p){
        Inventory inventory = Bukkit.createInventory(p, 9 * 3, ChatColor.translateAlternateColorCodes('&', "&d&lDaily Quests"));

        ItemStack grayGlass = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        for (int i = 0; i < 27; i++) {
            inventory.setItem(i, grayGlass);
        }

        p.openInventory(inventory);
    }
}
