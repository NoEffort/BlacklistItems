package me.noeffort.blacklistitems.util.config;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.noeffort.blacklistitems.Main;

public class BlacklistConfig {

	Main plugin = Main.get();
	
	ConsoleCommandSender sender = Bukkit.getServer().getConsoleSender();
	
	private File file = null;
	private FileConfiguration config = null;
	
	public FileConfiguration getConfig() {
		if(config == null) {
			reloadConfig();
		}
		return config;
	}
	
	public File getFile() {
		return file;
	}
	
	public void createFile() {
		if(file == null) {
			if(!plugin.getDataFolder().exists()) {
				plugin.getDataFolder().mkdir();
				sender.sendMessage(ChatColor.GREEN + "Plugin folder created!");
			}
			
			file = new File(plugin.getDataFolder(), "blacklist.yml");
			
			if(!file.exists()) {
				try {
					file.createNewFile();
					sender.sendMessage(ChatColor.GREEN + "Blacklist file created!");
				} catch (IOException e) {
					e.printStackTrace();
					sender.sendMessage(ChatColor.RED + "Error creating Blacklist file!");
				}
			}
		}
		
		config = YamlConfiguration.loadConfiguration(file);
		
		loadDefaultConfiguration();
	}
	
	private void loadDefaultConfiguration() {
		if(config.getConfigurationSection("blacklist.items") == null) {
			config.createSection("blacklist.items");
		}
		config.options().copyDefaults(true);
		
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void reloadConfig() {
		config = YamlConfiguration.loadConfiguration(file);
	}
	
	public void saveConfig() {
		if(config == null || file == null) {
			return;
		}
		
		try {
			config.save(file);
		} catch(IOException ignored) {}
	}
	
	public void saveDefaultConfig() {
		if(file == null) {
			file = new File(plugin.getDataFolder(), "blacklist.yml");
		}
		
		if(!file.exists()) {
			plugin.saveResource("blacklist.yml", false);
		}
	}
	
	public Material getMaterial(String item) {
		return Material.getMaterial(getConfig().getString("blacklist.items." + item + ".type"));
	}
	
	public String getDisplayName(String item) {
		return getConfig().getString("blacklist.items." + item + ".name");
	}
	
	public List<String> getLore(String item) {
		return getConfig().getStringList("blacklist.items." + item + ".lore");
	}
	
	public boolean ignoreMeta(String item) {
		return getConfig().getBoolean("blacklist.items." + item + ".ignoreMeta");
	}
	
	public boolean hasName(String item) {
		return getConfig().contains("blacklist.items." + item + ".name");
	}
	
	public boolean hasLore(String item) {
		return getConfig().contains("blacklist.items." + item + ".lore");
	}
}
