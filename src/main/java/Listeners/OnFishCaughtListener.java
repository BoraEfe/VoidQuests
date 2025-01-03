package Listeners;

import QuestManager.VoidQuestsDatabase;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class OnFishCaughtListener implements Listener {
    @EventHandler
    public void onFishCatch(PlayerFishEvent event){

        if(event.getState() == PlayerFishEvent.State.CAUGHT_FISH){
            Player player = event.getPlayer();
            String player_uuid = player.getUniqueId().toString();
            String player_name = player.getDisplayName();

            String sqlUpdate = "UPDATE player_quests SET fish_caught = fish_caught + 1 WHERE player_uuid = ? AND player_name = ?";

            Connection connection = VoidQuestsDatabase.getConnection();

            try(PreparedStatement updateStmt = connection.prepareStatement(sqlUpdate)){

                updateStmt.setString(1, player_uuid);
                updateStmt.setString(2, player_name);

                int rowsUpdated = updateStmt.executeUpdate();

                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&2+1 Fish caught"));

                if(rowsUpdated == 0){
                    player.sendMessage("ERROR CODE 404. Contact Staff!");
                }
            }
            catch (SQLException e){
                e.printStackTrace();
            }
        }
    }
}
