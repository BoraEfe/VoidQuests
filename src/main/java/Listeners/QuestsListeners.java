package Listeners;

import QuestManager.QuestsManager;
import QuestManager.VoidQuestsDatabase;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerHarvestBlockEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class QuestsListeners implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        Player p = event.getPlayer();
        VoidQuestsDatabase.addPlayerToDatabase(p);
    }
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;

        Player p = (Player) event.getWhoClicked();
        String pName = p.getName();

        if (p.getOpenInventory().getTitle().equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', "&dDaily Quests"))) event.setCancelled(true);
    }
    @EventHandler
    public void onHarvest(BlockBreakEvent event){

        Player p = event.getPlayer();
        String pName = p.getDisplayName();
        String pUUID = p.getUniqueId().toString();

        if(p.getItemInHand().getType() == Material.DIAMOND_PICKAXE){

            String sqlUpdate = "UPDATE player_quests SET blocks_mined = blocks_mined + 1 WHERE player_uuid = ? AND player_name = ?";

            Connection connection = VoidQuestsDatabase.getConnection();

            try(PreparedStatement updateStmt = connection.prepareStatement(sqlUpdate)){

                updateStmt.setString(1, pUUID);
                updateStmt.setString(2, pName);

                int rowsUpdated = updateStmt.executeUpdate();

                if(rowsUpdated == 0 ){
                    p.sendMessage("ERROR CODE 404. Contact Staff!");
                }
            }
            catch(SQLException e){
                e.printStackTrace();
            }
        }
        else{
            p.sendMessage("WRONG PICKAXE ITEM.");
        }
    }
}
