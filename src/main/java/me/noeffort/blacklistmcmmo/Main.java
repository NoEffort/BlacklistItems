package me.noeffort.blacklistmcmmo;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.gmail.nossr50.mcMMO;

import me.noeffort.blacklistmcmmo.command.BlacklistCommand;
import me.noeffort.blacklistmcmmo.util.MessageUtil;
import me.noeffort.blacklistmcmmo.util.config.BlacklistConfig;

public class Main extends JavaPlugin {

	//Making instances of the Main class
	private static Main instance;
	
	//Getting the mobConfig file
	private static BlacklistConfig blacklistConfig;
	
	//Logger and mcMMO input
	private static mcMMO mcMMO = null;
	private static final Logger log = Logger.getLogger("Minecraft");
	
	@Override
	public void onEnable() {
		//Checks if mcMMO exists
		if(!setupmcMMO()) {
			log.severe("mcMMO not found!");
			getServer().getPluginManager().disablePlugin(this);
			return;
		}
		
		instance = this;
		//Registering the command(s)
		registerCommands();
		
		blacklistConfig = new BlacklistConfig(instance);
		
		blacklistConfig.getBlacklistConfigFile();
		blacklistConfig.reloadBlacklistConfig();
	}
	
	public void registerCommands() {
		this.getCommand("blacklist").setExecutor(new BlacklistCommand(this));
	}
	
	//Setting up mcMMO
	private boolean setupmcMMO() {
        if (getServer().getPluginManager().getPlugin("mcMMO") == null) {
            return false;
        } else {
        	return true;
        }
    }
	
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
    	//Checks for player
        if(!(sender instanceof Player)) {
        	sender.sendMessage(MessageUtil.translate(Messages.player));
        	return true;
        }
        
        //Sets the player
        Player player = (Player) sender;
        
        //Looks for this command
        if(command.getName().equalsIgnoreCase("reloadmmoblacklist")) {
        	//Checking for op
        	if(player.isOp()) {
        		//Reload message
				player.sendMessage(MessageUtil.translate(Messages.reload));
				
				//Checking for existance of file
				if(!blacklistConfig.getBlacklistConfigFile().exists()) {
					player.sendMessage(MessageUtil.translate(Messages.missingfile));
					saveResource("blacklist.yml", false);
					player.sendMessage(MessageUtil.translate(Messages.filefound));
					Bukkit.getLogger().log(Level.INFO, "Config files generated!");
					return true;
				} else {
					//Getting and reloading the config
					blacklistConfig.getBlacklistConfigFile();
					blacklistConfig.reloadBlacklistConfig();
				}
				return true;
			} else {
				//Invalid permissions
				player.sendMessage(MessageUtil.translate(Messages.permissions));
			}
        } else {
        	return true;
        }
		return true;
    }
    
    public static mcMMO getmcMMO() {
    	return mcMMO;
    }
    
    //Getter for the Main class
    public static Main get() {
    	return instance;
    }
	
}
