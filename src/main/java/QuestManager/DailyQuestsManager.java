package QuestManager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.*;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

public class DailyQuestsManager {

    public static void startDailyQuestsAssignment(){
        ZoneId newYorkZone = ZoneId.of("America/New_York");
        LocalTime targetTime = LocalTime.of(12,0);

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                ZonedDateTime now = ZonedDateTime.now(newYorkZone);
                LocalTime currentTime = now.toLocalTime();

                if (currentTime.equals(targetTime)){
                    List<Map<String, Object>> quests = getRandomQuests();

                    for(Player player : Bukkit.getOnlinePlayers()){
                        for(Map<String, Object> quest : quests){
                            int id = (int) quest.get("id");
                            String name = (String) quest.get("name");
                            String description = (String) quest.get("description");
                            int goal = (int) quest.get("goal");
                            String reward = (String) quest.get("reward");

                            addQuestToPlayer(player, id);
                            addQuestsToActiveQuestsTable(id, name, description, goal, reward);
                        }
                    }
                }
            }
        }, 0, 60000);
    }

    public static List<Map<String, Object>> getRandomQuests(){
        List<Map<String, Object>> quests = new ArrayList<>();
        String sql = "SELECT id, name, description, goal, reward FROM quests";

        try(Connection connection = VoidQuestsDatabase.getConnection();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql)){

            while(rs.next()){
                Map<String, Object> quest = new HashMap<>();
                quest.put("id", rs.getInt("id"));
                quest.put("name", rs.getString("name"));
                quest.put("description", rs.getString("description"));
                quest.put("goal", rs.getInt("goal"));
                quest.put("reward", rs.getString("reward"));

                quests.add(quest);
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }

        //shuffles the list and chooses the first 3
        Collections.shuffle(quests);
        return quests.subList(0, Math.min(3, quests.size()));
    }

    private static void addQuestToPlayer(Player player, int questId){
        String playerUuid = player.getUniqueId().toString();
        String sql = "INSERT INTO player_quests (player_uuid, quest_id, progress, completed) VALUES (?, ?, 0, 0)";

        try(Connection connection = VoidQuestsDatabase.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql)){

            stmt.setString(1, playerUuid);
            stmt.setInt(2, questId);

            stmt.executeUpdate();
        }

        catch (SQLException e){
            e.printStackTrace();
        }
    }
    private static void addQuestsToActiveQuestsTable(int questId, String name, String description, int goal, String reward){
        String sql = "INSERT INTO active_quests (id, name, description, goal, reward) VALUES (?, ?, ?, ?)";

        try (Connection connection = VoidQuestsDatabase.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)){

            stmt.setInt(1, questId);
            stmt.setString(2, name);
            stmt.setString(3, description);
            stmt.setInt(4, goal);
            stmt.setString(5, reward);

            stmt.executeUpdate();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }
}
