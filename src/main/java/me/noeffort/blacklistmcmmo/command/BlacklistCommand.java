package me.noeffort.blacklistmcmmo.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.gmail.nossr50.mcMMO;

import me.noeffort.blacklistmcmmo.Main;
import me.noeffort.blacklistmcmmo.Messages;
import me.noeffort.blacklistmcmmo.util.MessageUtil;
import me.noeffort.blacklistmcmmo.util.config.BlacklistConfig;

public class BlacklistCommand implements CommandExecutor {

	Main plugin;
	mcMMO mcMMO;
	BlacklistConfig blacklistConfig;
	
	public BlacklistCommand(Main plugin) {
		this.plugin = plugin;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage(MessageUtil.translate(Messages.player));
			return true;
		}
		
		Player player = (Player) sender;
		
		if(command.getName().equalsIgnoreCase("blacklist")) {
			if(args.length < 1) {
				
				String itemInHand = player.getItemInHand().getType().name();
				
				blacklistConfig = new BlacklistConfig(plugin);
				blacklistConfig.getBlacklistConfigFile();
				blacklistConfig.getBlacklistConfig().set("blacklist.items." + itemInHand, player.getItemInHand().getType().toString());
				blacklistConfig.saveBlacklistConfig();
				blacklistConfig.reloadBlacklistConfig();
				player.sendMessage(MessageUtil.translate(Messages.prefix + "&aThis item has been added to the blacklist as a type."));
				return true;
			} 
			else if(args.length == 1) {
				if(args[0].equalsIgnoreCase("metadata")) {
					
					String itemInHand = player.getItemInHand().getType().toString();
					
					blacklistConfig = new BlacklistConfig(plugin);
					blacklistConfig.getBlacklistConfigFile();
					blacklistConfig.getBlacklistConfig().set("blacklist.items." + itemInHand, player.getItemInHand().getType().toString());
					blacklistConfig.getBlacklistConfig().set("blacklist.items." + itemInHand + ".name", player.getItemInHand().getItemMeta().getDisplayName());
					blacklistConfig.getBlacklistConfig().set("blacklist.items." + itemInHand + ".lore", player.getItemInHand().getItemMeta().getLore());
					blacklistConfig.saveBlacklistConfig();
					blacklistConfig.reloadBlacklistConfig();
					player.sendMessage(MessageUtil.translate(Messages.prefix + "&aThis item has been added to the blacklist with metadata."));
					return true;
				} else {
					player.sendMessage(MessageUtil.translate(Messages.invalid));
					return true;
				}
			} else {
				sender.sendMessage(MessageUtil.translate(Messages.toolittleargs));
				return true;
			}
		}
		
		return false;
	}

}
