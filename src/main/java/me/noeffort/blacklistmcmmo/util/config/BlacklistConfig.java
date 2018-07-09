package me.noeffort.blacklistmcmmo.util.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;

import me.noeffort.blacklistmcmmo.Main;

public class BlacklistConfig implements Listener {

	private static FileConfiguration config = null;
	private static File configFile = null;
	
	//Initiating the Main class
	Main plugin;
	
	//Constructor
	public BlacklistConfig(Main instance) {
		this.plugin = instance;
	}
	
	//Used to create and reload the config file
	public void reloadBlacklistConfig() {
		//Checking for file
		if(configFile == null) {
			//Making new config file
			configFile = new File(plugin.getDataFolder(), "blacklist.yml");
			//Checking for existence
			if(!configFile.exists()) {
				//File not found
				plugin.saveResource("blacklist.yml", false);
				Bukkit.getLogger().log(Level.INFO, "Blacklist.yml config file generated!");
			} else {
				//File found
				saveBlacklistConfig();
				Bukkit.getLogger().log(Level.INFO, "Blacklist.yml file found, no worries!");
			}
		}
		//Setting file
		config = YamlConfiguration.loadConfiguration(configFile);
		
		//Allowing inputs to file
		Reader defaultConfigStream = new InputStreamReader(plugin.getResource("blacklist.yml"));
		if(defaultConfigStream != null) {
			YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(defaultConfigStream);
			defaultConfig.options().copyDefaults(true);
			config.setDefaults(defaultConfig);
		}
	}
	
	//Used to get the custom config file
	public FileConfiguration getBlacklistConfig() {
		//Checking for file
		if(config == null) {
			reloadBlacklistConfig();
		}
		//Returning the file
		return config;
	}
	
	//Used to save the custom file
	public void saveBlacklistConfig() {
		//Checking for file
		if(config == null || configFile == null) {
			return;
		}
		//Saving file
		try {
			getBlacklistConfig().save(configFile);
		} catch (IOException e) {
			//Error boi
			plugin.getLogger().log(Level.SEVERE, "Could not save config to " + configFile, e);
		}
	}
	
	//Saving the defaults of the file, reverting to base settings
	public void saveDefaultBlacklistConfig() {
		//Checking for file
		if(configFile == null) {
			configFile = new File(plugin.getDataFolder(), "blacklist.yml");
			plugin.saveResource("blacklist.yml", false);
			Bukkit.getLogger().log(Level.INFO, "Blacklist.yml config file generated!");
		}
		//Saving defaults
		if(configFile.exists()) {
			plugin.saveResource("blacklist.yml", false);
		}
	}
	
	public File getBlacklistConfigFile() {
		return configFile;
	}
	
}
