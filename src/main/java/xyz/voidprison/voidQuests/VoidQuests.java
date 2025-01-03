package xyz.voidprison.voidQuests;

import Commands.QuestsCommand;
import Listeners.*;
import QuestManager.DailyQuestsManager;
import QuestManager.VoidQuestsDatabase;
import org.bukkit.plugin.java.JavaPlugin;

import static QuestManager.VoidQuestsDatabase.initializeDatabase;
import static QuestManager.QuestsManager.addDefaultQuests;

public final class VoidQuests extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        VoidQuestsDatabase.getConnection();
        initializeDatabase();
        addDefaultQuests();
        DailyQuestsManager.startDailyQuestsAssignment();

        getCommand("quests").setExecutor(new QuestsCommand());

        getServer().getPluginManager().registerEvents(new QuestsGUIListener(), this);
        getServer().getPluginManager().registerEvents(new OnHarvestListener(),this);
        getServer().getPluginManager().registerEvents(new OnFishCaughtListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
