package org.necrozstudios.commands;

import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.necrozstudios.apis.ItemAPI;
import org.necrozstudios.necrozatp;

import java.util.Arrays;

public class GiveCustomPickaxeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Verifica se o comando foi executado por um jogador
        if (sender instanceof Player) {
            Player player = (Player) sender;

            // Verifica se o jogador tem a permissão necessária
            if (!player.hasPermission("necroz.givepickaxe")) {
                player.sendMessage(ChatColor.RED + "Você não tem permissão para usar este comando.");
                return true;
            }

            NBTItem picareta = new NBTItem(new ItemAPI(Material.DIAMOND_PICKAXE).setDisplayName("&6&lPicareta Lendária").setLore("Uma ferramenta forjada pelos deuses.","Indestrutível e cheia de poder.").addEnchantmentnorestriction(Enchantment.DURABILITY, 10).build());
                picareta.setInteger("picareta.bonus", 0);

            // Adiciona a picareta ao inventário do jogador
            player.getInventory().addItem(picareta.getItem());

            // Envia uma mensagem para o jogador como feedback
            String givepickaxeMessage = necrozatp.getCustomConfig().getString("messages.pickaxelore", "Você recebeu a Picareta Lendária!");
            // Envia a mensagem ao jogador, com a cor configurada
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', givepickaxeMessage));

            return true;
        } else {
            // Envia uma mensagem se o comando for executado no console
            sender.sendMessage("Este comando só pode ser usado por jogadores.");

            return false;
        }
    }
}
