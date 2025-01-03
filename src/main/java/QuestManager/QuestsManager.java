package QuestManager;

import org.bukkit.entity.Player;

public class QuestsManager {
    public static void addDefaultQuests(){
        VoidQuestsDatabase.addQuests("miner", "Mine 1 000 Blocks", 1000, "1000 Stars");
        VoidQuestsDatabase.addQuests("Bigger miner", "Mine 10 000 Blocks", 10000, "10000 Stars");
        VoidQuestsDatabase.addQuests("Biggest miner", "Mine 100 000 Blocks", 100000, "100000 Stars");
    }

}
