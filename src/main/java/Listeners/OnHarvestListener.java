package Listeners;

import QuestManager.VoidQuestsDatabase;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class OnHarvestListener implements Listener {

    @EventHandler
    public void onHarvest(BlockBreakEvent event){

        Player player = event.getPlayer();
        String player_name = player.getDisplayName();
        String player_uuid = player.getUniqueId().toString();

        if(player.getItemInHand().getType() == Material.DIAMOND_PICKAXE){

            String sqlUpdate = "UPDATE player_quests SET blocks_mined = blocks_mined + 1 WHERE player_uuid = ? AND player_name = ?";

            Connection connection = VoidQuestsDatabase.getConnection();

            try(PreparedStatement updateStmt = connection.prepareStatement(sqlUpdate)){

                updateStmt.setString(1, player_uuid);
                updateStmt.setString(2, player_name);

                int rowsUpdated = updateStmt.executeUpdate();

                if(rowsUpdated == 0 ){
                    player.sendMessage("ERROR CODE 404. Contact Staff!");
                }
            }
            catch(SQLException e){
                e.printStackTrace();
            }
        }
        else{
            player.sendMessage("WRONG PICKAXE ITEM.");
        }
    }
}
