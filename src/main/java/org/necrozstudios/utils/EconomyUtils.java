package org.necrozstudios.utils;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.necrozstudios.necrozatp;

public class EconomyUtils {

    private static Economy economy = null;

    public static void setupEconomy(JavaPlugin plugin) {
        if (plugin.getServer().getPluginManager().getPlugin("Vault") == null) {
            plugin.getLogger().severe("Vault não encontrado! A economia não foi configurada.");
            return;
        }

        RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            plugin.getLogger().severe("Economia não foi configurada corretamente.");
            return;
        }

        economy = rsp.getProvider();
        plugin.getLogger().info("Sistema de economia configurado com sucesso!");
    }

    /**
     * Retorna a instância do sistema de economia.
     *
     * @return Instância de Economy ou null se não estiver configurado.
     */
    public static Economy getEconomy() {
        return economy;
    }

    /**
     * Deposita um valor na conta de um jogador e exibe uma mensagem configurável.
     *
     * @param player O jogador que receberá o depósito.
     * @param amount O valor a ser depositado.
     */
    public static void depositPlayer(Player player, double amount) {
        if (economy == null) {
            player.sendMessage("§cO sistema de economia não está configurado. Por favor, contate o administrador.");
            return;
        }

        if (amount <= 0) {
            player.sendMessage("§cO valor a ser depositado deve ser maior que zero.");
            return;
        }

        // Deposita o dinheiro no jogador
        economy.depositPlayer(player, amount);

    }
}
