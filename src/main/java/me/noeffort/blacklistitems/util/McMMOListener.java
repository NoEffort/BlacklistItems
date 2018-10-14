package me.noeffort.blacklistitems.util;

import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.gmail.nossr50.datatypes.player.McMMOPlayer;
import com.gmail.nossr50.events.skills.repair.McMMOPlayerRepairCheckEvent;
import com.gmail.nossr50.util.player.UserManager;

import me.noeffort.blacklistitems.Main;
import me.noeffort.blacklistitems.Messages;
import me.noeffort.blacklistitems.util.config.BlacklistConfig;

public class McMMOListener implements Listener {
	
	Main plugin = Main.get();
	
	//Constructor
	public McMMOListener() { }
	
	@EventHandler
	public void onItemRepair(McMMOPlayerRepairCheckEvent event) {
		
		BlacklistConfig config = plugin.getBlacklistConfig();
		Player player = event.getPlayer();
		
		if(player == null) {
			event.setCancelled(true);
			return;
		}
		
		@SuppressWarnings("unused")
		McMMOPlayer mcMMOPlayer = UserManager.getPlayer(player);
		
		HashMap<String, Material> keys = new HashMap<String, Material>();
		
		ConfigurationSection blacklisted = config.getConfig().getConfigurationSection("blacklist.items");
		
		for(String key : blacklisted.getKeys(false)) {
			keys.put(key, config.getMaterial(key));
		}
		
		for(Entry<String, Material> entry : keys.entrySet()) {
			try {
						
				boolean ignoreMeta = config.ignoreMeta(entry.getKey());
				ItemStack configItem = new ItemStack(entry.getValue());
				
				ItemStack mainhand = event.getRepairedObject();
				
				if(mainhand.hasItemMeta()) {
					if(ignoreMeta) {
						if(isTypeEqual(mainhand, configItem)) {
							event.setCancelled(true);
							player.sendMessage(MessageUtil.translate(Messages.prefix + "&cYou cannot repair this item!"));
							return;
						}
					} else {
						ItemMeta meta = configItem.getItemMeta();
						meta.setDisplayName(config.getDisplayName(entry.getKey()));
						meta.setLore(config.getLore(entry.getKey()));
						configItem.setItemMeta(meta);
						
						if(isEqual(mainhand, configItem)) {
							event.setCancelled(true);
							player.sendMessage(MessageUtil.translate(Messages.prefix + "&cYou cannot repair this item!"));
							return;
						}
					}
				} else {
					if(ignoreMeta) {
						if(isTypeEqual(mainhand, configItem)) {
							event.setCancelled(true);
							player.sendMessage(MessageUtil.translate(Messages.prefix + "&cYou cannot repair this item!"));
							return;
						}
					} else {
						ItemMeta meta = configItem.getItemMeta();
						meta.setDisplayName(config.getDisplayName(entry.getKey()));
						meta.setLore(config.getLore(entry.getKey()));
						configItem.setItemMeta(meta);
						
						if(isEqual(mainhand, configItem)) {
							event.setCancelled(true);
							player.sendMessage(MessageUtil.translate(Messages.prefix + "&cYou cannot repair this item!"));
							return;
						}
					}
				}	
				
			} catch (NullPointerException e) {
				return;
			}
		}
	}
	
	private boolean isEqual(ItemStack a, ItemStack b) {
		
		if(a == null || b == null) {
			return false;
		}
		
		boolean type = (a.getType() == b.getType());
		boolean hasItemMeta = (a.hasItemMeta() == b.hasItemMeta());
		boolean itemMeta = true;
		
		if(hasItemMeta) {
			itemMeta = Bukkit.getItemFactory().equals(a.getItemMeta(), b.getItemMeta());
		}
		
		if(type && hasItemMeta && itemMeta) {
			return true;
		}
		
		return false;
	}
	
	private boolean isTypeEqual(ItemStack a, ItemStack b) {
		
		if(a == null || b == null) {
			return false;
		}
		
		boolean type = (a.getType() == b.getType());
		
		if(type) {
			return true;
		}
		
		return false;
	}
}

/*
RepairManager manager = new RepairManager(mcMMOPlayer);
manager.handleRepair(new ItemStack(Material.getMaterial(mainhand.toString())));
*/
