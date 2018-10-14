package me.noeffort.blacklistitems.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringJoiner;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.noeffort.blacklistitems.Main;
import me.noeffort.blacklistitems.Messages;
import me.noeffort.blacklistitems.util.MessageUtil;
import me.noeffort.blacklistitems.util.config.BlacklistConfig;

public class BlacklistCommand implements CommandExecutor {

	Main plugin = Main.get();
	BlacklistConfig config;
	
	String sectionName = "";
	boolean meta = false;
	boolean type = false;
	boolean countdown = false;
	
	public BlacklistCommand() { }

	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		
		config = plugin.getBlacklistConfig();
		
		if(!(sender instanceof Player)) {
			sender.sendMessage(MessageUtil.translate(Messages.player));
			return true;
		}
		
		Player player = (Player) sender;
		
		if(command.getName().equalsIgnoreCase("blacklist")) {
			if(args.length == 1) {
				if(args[0].equalsIgnoreCase("metadata")) {
					player.sendMessage(MessageUtil.translate(Messages.prefix + "&c[Metadata] Please input a name for this item."));
					return true;
				}
				if(args[0].equalsIgnoreCase("type")) {
					player.sendMessage(MessageUtil.translate(Messages.prefix + "&c[RawType] Please input a name for this item."));
					return true;
				}
				if(args[0].equalsIgnoreCase("confirm")) {	
					
					String name = getSectionName();
					
					if(countdown) {
						if(name == null || name.equals("")) {
							throw new NullPointerException("Configuration Section name undefined!");
						}
						
						if(meta && !type) {
							config.getConfig().set("blacklist.items." + name + ".ignoreMeta", false);
							config.getConfig().set("blacklist.items." + name + ".type", player.getInventory().getItemInMainHand().getType().toString());
							config.getConfig().set("blacklist.items." + name + ".name", player.getInventory().getItemInMainHand().getItemMeta().getDisplayName());
							config.getConfig().set("blacklist.items." + name + ".lore", player.getInventory().getItemInMainHand().getItemMeta().getLore());
							config.saveConfig();
							config.reloadConfig();
							setCountdown(false);
							return true;
						}
						
						if(!meta && type) {
							config.getConfig().set("blacklist.items." + name + ".ignoreMeta", true);
							config.getConfig().set("blacklist.items." + name + ".type", player.getInventory().getItemInMainHand().getType().toString());
							config.saveConfig();
							config.reloadConfig();
							setCountdown(false);
							return true;
						}
					} else {
						setCountdown(false);
						return true;
					}
				}
				if(args[0].equalsIgnoreCase("list")) {
					
					List<Material> inConfig = new ArrayList<Material>();
					
					ConfigurationSection blacklisted = config.getConfig().getConfigurationSection("blacklist.items");
					
					StringJoiner joiner = new StringJoiner(", ", "", "");
					
					try {
						for(String key : blacklisted.getKeys(false)) {
							Material material = Material.getMaterial(config.getConfig().getString("blacklist.items." + key + ".type"));
							inConfig.add(material);
							
							joiner.add(material.toString());
						}
					} catch(NullPointerException ignored) {
						player.sendMessage(MessageUtil.translate(Messages.prefix + "&cNothing to display!"));
						return true;
					}
					
					String joined = joiner.toString();
					
					player.sendMessage(MessageUtil.translate(Messages.prefix + "&b" + joined));
					
					return true;
					
				}
				if(args[0].equalsIgnoreCase("reload")) {
					config.reloadConfig();
					player.sendMessage(MessageUtil.translate(Messages.reload));
					return true;
				}
				else {
					player.sendMessage(MessageUtil.translate(Messages.invalid));
					return true;
				}
			} else if(args.length == 2) {
				
				ItemStack mainhand = player.getInventory().getItemInMainHand();
				
				setSectionName(args[1].toString());
				
				if(mainhand == null || mainhand.getType() == Material.AIR) {
					player.sendMessage(MessageUtil.translate(Messages.prefix + "&cYou must have an item in your hand!"));
					return true;
				}
				if(!args[0].isEmpty() && args[0].equalsIgnoreCase("metadata")) {
					config.reloadConfig();
					if(config.getConfig().isSet("blacklist.items." + args[1].toString())) {
						player.sendMessage(MessageUtil.translate(Messages.prefix + "&7Do you want to override " + args[1].toString() + "?"));
						player.sendMessage(MessageUtil.translate("&7If you would like to, do: &a/bl confirm&7. You have 15 seconds!"));
						meta = true;
						type = false;
						countdown = true;
						startCountdown(player);
						return true;
					}
					if(args[1].toString().equals("null")) {
						throw new IllegalArgumentException(player.getName() + " attempted to name item section null");
					}
					config.getConfig().set("blacklist.items." + args[1].toString() + ".ignoreMeta", false);
					config.getConfig().set("blacklist.items." + args[1].toString() + ".type", player.getInventory().getItemInMainHand().getType().toString());
					config.getConfig().set("blacklist.items." + args[1].toString() + ".name", player.getInventory().getItemInMainHand().getItemMeta().getDisplayName());
					config.getConfig().set("blacklist.items." + args[1].toString() + ".lore", player.getInventory().getItemInMainHand().getItemMeta().getLore());
					config.saveConfig();
					config.reloadConfig();
					player.sendMessage(MessageUtil.translate(Messages.prefix + "&aThis item has been added to the blacklist with metadata."));
					return true;
				}
				if(!args[0].isEmpty() && args[0].equalsIgnoreCase("type")) {
					config.reloadConfig();
					if(config.getConfig().isSet("blacklist.items." + args[1].toString())) {
						player.sendMessage(MessageUtil.translate(Messages.prefix + "&7Do you want to override " + args[1].toString() + "?"));
						player.sendMessage(MessageUtil.translate("&7If you would like to, do: &a/bl confirm&7. You have 15 seconds!"));
						meta = false;
						type = true;
						countdown = true;
						startCountdown(player);
						return true;
					}
					if(args[1].toString().equals("null")) {
						throw new IllegalArgumentException(player.getName() + " attempted to name item section null");
					}
					config.getConfig().set("blacklist.items." + args[1].toString() + ".ignoreMeta", true);
					config.getConfig().set("blacklist.items." + args[1].toString() + ".type", player.getInventory().getItemInMainHand().getType().toString());
					config.saveConfig();
					config.reloadConfig();
					player.sendMessage(MessageUtil.translate(Messages.prefix + "&aThis item has been added to the blacklist as a raw type."));
					return true;
				} else {
					sender.sendMessage(MessageUtil.translate(Messages.invalid));
					return true;
				}
			}
			else {
				sender.sendMessage(MessageUtil.translate(Messages.toolittleargs));
				return true;
			}
		}
		
		return true;
	}
	
	HashMap<UUID, Integer> map = new HashMap<UUID, Integer>();
	int time = 0;
	int taskID = 0;
	
	private void startCountdown(Player player) {
		
		map.put(player.getUniqueId(), 15);
		
		taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			@Override
			public void run() {
				int mapTime = map.get(player.getUniqueId());
				time = mapTime;
				if(!ifCountdown()) {
					Bukkit.getScheduler().cancelTask(taskID);
					setCountdown(false);
					return;
				} else {
					if(mapTime <= 0) {
						Bukkit.getScheduler().cancelTask(taskID);
						setCountdown(false);
						return;
					}
				}
				mapTime--;
				map.put(player.getUniqueId(), mapTime);
			}
		}, 0L, 20L);
	}
	
	public boolean ifCountdown() {
		return countdown;
	}
	
	public void setCountdown(boolean countdown) {
		this.countdown = countdown;
	}
	
	public void setSectionName(String name) {
		this.sectionName = name;
	}
	
	public String getSectionName() {
		return sectionName;
	}

}
