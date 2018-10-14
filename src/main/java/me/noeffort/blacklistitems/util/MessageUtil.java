package me.noeffort.blacklistitems.util;

import org.bukkit.ChatColor;

public class MessageUtil {

	public static String translate(String string) {
		return ChatColor.translateAlternateColorCodes('&', string);
	}
}
