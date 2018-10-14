package me.noeffort.blacklistitems.util;

import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.noeffort.blacklistitems.Main;
import me.noeffort.blacklistitems.Messages;
import me.noeffort.blacklistitems.util.config.BlacklistConfig;

public class AnvilListener implements Listener {

	Main plugin = Main.get();
	
	public AnvilListener() {}
	
	@EventHandler
	public void onAnvilRepair(PrepareAnvilEvent event) {
		
		BlacklistConfig config;
		HumanEntity player = event.getView().getPlayer();
		
		config = plugin.getBlacklistConfig();
		
		HashMap<String, Material> keys = new HashMap<String, Material>();
		
		ConfigurationSection blacklisted = config.getConfig().getConfigurationSection("blacklist.items");
		
		for(String key : blacklisted.getKeys(false)) {
			keys.put(key, config.getMaterial(key));
		}
		
		AnvilInventory anvil = event.getInventory();
		
		ItemStack[] content = anvil.getContents();
		
		for(Entry<String, Material> entry : keys.entrySet()) {
			if(!(player instanceof Player)) {
				return;
			} else {
				if(!(anvil instanceof AnvilInventory)) {
					return;
				} else {
					
					ItemStack left = content[0];
					ItemStack right = content[1];
					
					try {
						
						boolean ignoreMeta = config.ignoreMeta(entry.getKey());
						ItemStack configItem = new ItemStack(entry.getValue());

						if(left.hasItemMeta()) {
							if(ignoreMeta) {
								if(isTypeEqual(left, configItem) || isTypeEqual(right, configItem)) {
									player.closeInventory();
									player.sendMessage(MessageUtil.translate(Messages.prefix + "&cYou cannot repair this item!"));
									return;
								}
							} else {
								ItemMeta meta = configItem.getItemMeta();
								meta.setDisplayName(config.getDisplayName(entry.getKey()));
								meta.setLore(config.getLore(entry.getKey()));
								configItem.setItemMeta(meta);
								if(isEqual(left, configItem) || isEqual(right, configItem)) {
									player.closeInventory();
									player.sendMessage(MessageUtil.translate(Messages.prefix + "&cYou cannot repair this item!"));
									return;
								}
							}
						} else {
							if(ignoreMeta) {
								if(isTypeEqual(left, configItem) || isTypeEqual(right, configItem)) {
									player.closeInventory();
									player.sendMessage(MessageUtil.translate(Messages.prefix + "&cYou cannot repair this item!"));
									return;
								}
							} else {
								ItemMeta meta = configItem.getItemMeta();
								meta.setDisplayName(config.getDisplayName(entry.getKey()));
								meta.setLore(config.getLore(entry.getKey()));
								configItem.setItemMeta(meta);
								if(isEqual(left, configItem) || isEqual(right, configItem)) {
									player.closeInventory();
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
