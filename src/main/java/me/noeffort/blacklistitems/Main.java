package me.noeffort.blacklistitems;

import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;

import com.gmail.nossr50.mcMMO;

import me.noeffort.blacklistitems.command.BlacklistCommand;
import me.noeffort.blacklistitems.util.AnvilListener;
import me.noeffort.blacklistitems.util.McMMOListener;
import me.noeffort.blacklistitems.util.config.BlacklistConfig;

public class Main extends JavaPlugin {

	//Making instances of the Main class
	private static Main instance;
	
	//Getting the mobConfig file
	private BlacklistConfig config;
	
	//Logger and mcMMO input
	private static mcMMO mcMMO = null;
	private static final Logger log = Logger.getLogger("Minecraft");
	
	@Override
	public void onEnable() {
		
		instance = this;
		
		registerListeners();
		registerCommands();
		loadConfigFiles();
		
		//Checks if mcMMO exists
		if(!setupmcMMO()) {
			log.severe("mcMMO not found!");
			getServer().getPluginManager().disablePlugin(this);
			return;
		}
	}
	
	public void registerListeners() {
		getServer().getPluginManager().registerEvents(new McMMOListener(), this);
		getServer().getPluginManager().registerEvents(new AnvilListener(), this);
	}
	
	public void registerCommands() {
		this.getCommand("blacklist").setExecutor(new BlacklistCommand());
	}
	
	//Setting up mcMMO
	private boolean setupmcMMO() {
        if (getServer().getPluginManager().getPlugin("mcMMO") == null) {
            return false;
        } else {
        	return true;
        }
    }
	
	private void loadConfigFiles() {
		config = new BlacklistConfig();
		config.createFile();
	}
    
    public static mcMMO getmcMMO() {
    	return mcMMO;
    }
    
    public BlacklistConfig getBlacklistConfig() {
    	return config;
    }
    
    //Getter for the Main class
    public static Main get() {
    	return instance;
    }
	
}
