package org.necrozstudios.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.necrozstudios.necrozatp;

public class PluginReload implements CommandExecutor {
    private final JavaPlugin plugin;

    public PluginReload(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("necrozreload")) {
            if (sender instanceof Player && !sender.hasPermission("necrozstudios.reload")) {
                sender.sendMessage("§cVocê não tem permissão para usar este comando.");
                return true;
            }

            // Recarregar a configuração
            plugin.reloadConfig();

            plugin.getLogger().info("O plugin foi recarregado por " + sender.getName());
            String reloadMessage = necrozatp.getCustomConfig().getString("messages.reload_message", "§aO plugin foi recarregado com sucesso!");

            // Verificar se o remetente é um jogador
            if (sender instanceof Player) {
                Player player = (Player) sender;
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', reloadMessage));
            } else {
                // Caso seja console, enviar mensagem para o sender
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', reloadMessage));
            }

            return true;
        }
        return false;
    }
}
