package QuestManager;

import org.bukkit.entity.Player;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static javax.management.remote.JMXConnectorFactory.connect;


public class VoidQuestsDatabase {
    private static final String DATABASE_FOLDER = "plugins/VoidQuests";
    private static final String DATABASE_NAME = "quests.db";
    private static Connection connection;

    public static void initializeDatabase(){

        File folder = new File(DATABASE_FOLDER);
        if (!folder.exists()){
            folder.mkdirs();
        }

        File databaseFile = new File(folder, DATABASE_NAME);
        try{
            if(!databaseFile.exists()){
                databaseFile.createNewFile();
            }
            connection = DriverManager.getConnection("jdbc:sqlite:" + databaseFile.getAbsolutePath());
            createTables();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public static Connection getConnection(){
        if (connection == null){
            synchronized (VoidQuestsDatabase.class){
                if(connection == null){
                    initializeDatabase();
                }
            }
        }
        return connection;
    }

    private static void createTables(){

        String activeQuestsTable = "CREATE TABLE IF NOT EXISTS active_quests(" +
                                    "id INTEGER NOT NULL, " +
                                    "name TEXT NOT NULL, " +
                                    "description TEXT, " +
                                    "goal INTEGER NOT NULL, " +
                                    "reward TEXT NOT NULL);" ;


        String allQuestsTable = "CREATE TABLE IF NOT EXISTS quests (" +
                                   "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                   "name TEXT NOT NULL, " +
                                   "description TEXT, " +
                                   "goal INTEGER NOT NULL, " +
                                   "reward TEXT NOT NULL);" ;

        String createPlayerQuestsTable = "CREATE TABLE IF NOT EXISTS player_quests (" +
                                         "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                         "player_uuid TEXT NOT NULL, " +
                                         "player_name TEXT NOT NULL, " +
                                         "quest_id INTEGER DEFAULT NULL, " +
                                         "blocks_mined INTEGER DEFAULT 0, " +
                                         "fish_caught INTEGER DEFAULT 0, " +
                                         "online_time INTEGER DEFAULT 0, " +
                                         "progress INTEGER DEFAULT 0, " +
                                         "completed BOOLEAN DEFAULT 0);" ;
        try {
            connection.createStatement().execute(allQuestsTable);
            connection.createStatement().execute(createPlayerQuestsTable);
            connection.createStatement().execute(activeQuestsTable);
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    public static void addQuests(String name, String description, int goal, String reward){
        String sql = "INSERT INTO quests (name, description, goal, reward) VALUES (? ,? ,? ,? )";
        try(var stmt  = connection.prepareStatement(sql)){
            stmt.setString(1, name);
            stmt.setString(2, description);
            stmt.setInt(3, goal);
            stmt.setString(4, reward);
            stmt.executeUpdate();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    public static void addPlayerToDatabase(Player p){
        String player_uuid = p.getUniqueId().toString();
        String player_name = p.getName();

        System.out.println("Player UUID: " + player_uuid + ", Player Name: " + player_name);

        String sql = "INSERT OR IGNORE INTO player_quests (player_uuid, player_name) VALUES (?, ?)";

        try(var stmt  = connection.prepareStatement(sql)){
            stmt.setString(1, player_uuid);
            stmt.setString(2, player_name);
            stmt.executeUpdate();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    public static void closeConnection(){
        try{
            if(connection != null && !connection.isClosed()){
                connection.close();
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }
}
