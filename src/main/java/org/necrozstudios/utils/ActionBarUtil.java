package org.necrozstudios.utils;

import org.bukkit.entity.Player;
import org.bukkit.ChatColor;

public class ActionBarUtil {

    public static void sendActionBar(Player player, String message) {
        player.spigot().sendMessage(
                net.md_5.bungee.api.ChatMessageType.ACTION_BAR,
                net.md_5.bungee.api.chat.TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', message))
        );
    }

}
