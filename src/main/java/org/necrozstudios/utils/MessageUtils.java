package org.necrozstudios.utils;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class MessageUtils {

    /**
     * Envia uma mensagem ao jogador se estiver ativada no config.yml.
     *
     * @param player    O jogador que receberá a mensagem.
     * @param plugin    A instância do plugin para acessar o config.yml.
     * @param path      O caminho da mensagem no config.yml.
     * @param defaultMsg Mensagem padrão caso o valor não esteja no config.yml.
     * @param placeholders Placeholders e seus valores para substituição (pares de chave e valor).
     */
    public static void sendConfigMessage(Player player, JavaPlugin plugin, String path, String defaultMsg, Object... placeholders) {
        FileConfiguration config = plugin.getConfig();

        // Verifica se a mensagem está ativada
        boolean isMessageEnabled = config.getBoolean(path + "_enabled", true);
        if (!isMessageEnabled) {
            return;
        }

        // Obtém a mensagem configurada ou usa a mensagem padrão
        String message = config.getString(path, defaultMsg);

        // Substitui os placeholders fornecidos
        for (int i = 0; i < placeholders.length; i += 2) {
            if (i + 1 < placeholders.length) {
                String key = placeholders[i].toString();
                String value = placeholders[i + 1].toString();
                message = message.replace(key, value);
            }
        }

        // Envia a mensagem ao jogador com cores traduzidas
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }
}
