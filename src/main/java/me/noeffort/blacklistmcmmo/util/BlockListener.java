package me.noeffort.blacklistmcmmo.util;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.gmail.nossr50.mcMMO;

import me.noeffort.blacklistmcmmo.Main;
import me.noeffort.blacklistmcmmo.Messages;
import me.noeffort.blacklistmcmmo.util.config.BlacklistConfig;

public class BlockListener implements Listener {
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onBlockClick(PlayerInteractEvent event) {

		Main plugin = Main.get();
		mcMMO mcMMO;
		BlacklistConfig blacklistConfig;
		Player player = event.getPlayer();
		
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			
			mcMMO = Main.getmcMMO();
			
			if(event.getClickedBlock().getType() == Material.getMaterial(mcMMO.getConfig().getString("Skills.Salvage.Anvil_Material"))) {
				
				blacklistConfig = new BlacklistConfig(plugin);
				List<ItemStack> inConfig = new ArrayList<ItemStack>();
				
				for(String key : blacklistConfig.getBlacklistConfig().getConfigurationSection("blacklist.items").getKeys(false)) {
					ItemStack stack = blacklistConfig.getBlacklistConfig().getItemStack("blacklist.items." + key);
					inConfig.add(stack);
				}
				
				System.out.println(Messages.prefix + inConfig.toString());
				
				for(ItemStack itemStack : inConfig) {
					if(inConfig.contains(itemStack)) {
						if(player.getItemInHand().isSimilar(itemStack)) {
							player.sendMessage(MessageUtil.translate(Messages.prefix + "&cYou cannot repair this item!"));
							event.setCancelled(true);
						}
					} else {
						return;
					}
				}
				
			}
		}
		
	}

}
