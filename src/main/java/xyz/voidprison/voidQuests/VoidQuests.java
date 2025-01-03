package xyz.voidprison.voidQuests;

import Commands.QuestsCommand;
import Listeners.QuestsListeners;
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
        getServer().getPluginManager().registerEvents(new QuestsListeners(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
