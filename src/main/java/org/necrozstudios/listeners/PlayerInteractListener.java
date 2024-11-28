package org.necrozstudios.listeners;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.necrozstudios.apis.ItemAPI;
import org.necrozstudios.necrozatp;

import org.necrozstudios.utils.EconomyUtils;


import java.util.Arrays;

public class PlayerInteractListener implements Listener {

    public static ItemStack EfficiencyUpgrade = new ItemStack(Material.NETHER_STAR);
    public static ItemStack FortuneUpgrade = new ItemStack(Material.EMERALD);
    public static ItemStack SpeedUpgrade = new ItemStack(Material.FEATHER);
    public static ItemStack CloseMenuItem = new ItemStack(Material.ARROW);

    public static void OpenInventory(Player p) {
        ItemStack heldItem = p.getItemInHand();
        ItemMeta heldItemMeta = heldItem.getItemMeta();

        double effiupgPrice = necrozatp.getCustomConfig().getDouble("upgrades.efficiencyUpgrade");
        double fortupgPrice = necrozatp.getCustomConfig().getDouble("upgrades.fortuneUpgrade");

        // Obtenha o nível dos encantamentos
        int enchantLeveleffi = heldItemMeta.getEnchantLevel(Enchantment.DIG_SPEED);
        int enchantLevelfort = heldItemMeta.getEnchantLevel(Enchantment.LOOT_BONUS_BLOCKS);


        // Calcula os preços dos upgrades
        double priceeffi = effiupgPrice * Math.pow(2, enchantLeveleffi);
        double pricefort = fortupgPrice * Math.pow(2, enchantLevelfort);

        Inventory inv = Bukkit.createInventory(null, 9 * 6, "Upgrade Menu");

        // Cria os itens de upgrade com cores personalizadas
        ItemStack efficiencyUpgrade = new ItemAPI(EfficiencyUpgrade.getType())
                .setDisplayName("§6§lUpgrade Efficiency")
                .setLore("", "  §6§lNivel " + enchantLeveleffi + " / 10", "", "  §l➥Preço: " + priceeffi)
                .build();
        ItemStack fortuneUpgrade = new ItemAPI(FortuneUpgrade.getType())
                .setDisplayName("§6§lUpgrade Fortune")
                .setLore("", "  §6§lNivel " + enchantLevelfort + " / 10", "  §l➥Preço: " + pricefort)
                .build();
        ItemStack speedUpgrade = new ItemAPI(SpeedUpgrade.getType())
                .setDisplayName("§6§lUpgrade Speed")
                .setLore("", "  §6§lNivel 0 / 2", "  §l➥Preço: -----")
                .build();

        // Cria o item para fechar o menu
        ItemMeta closeMeta = CloseMenuItem.getItemMeta();
        closeMeta.setDisplayName("§c§lFechar Menu");
        CloseMenuItem.setItemMeta(closeMeta);

        inv.setItem(10, efficiencyUpgrade);
        inv.setItem(13, fortuneUpgrade);
        inv.setItem(16, speedUpgrade);
        inv.setItem(49, CloseMenuItem);

        p.openInventory(inv);
    }

    @EventHandler
    public void onPickaxeInteract(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR) {
            if (e.getPlayer().getItemInHand().getType() == Material.DIAMOND_PICKAXE) {
                Player p = e.getPlayer();
                OpenInventory(p);
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        // Verifica se o inventário é o "Upgrade Menu"
        if (e.getView().getTitle().equals("Upgrade Menu")) {
            e.setCancelled(true); // Previne ações padrão no inventário

            Player p = (Player) e.getWhoClicked();
            ItemStack clickedItem = e.getCurrentItem();
            if (clickedItem == null || clickedItem.getType() == Material.AIR) return;

            ItemStack heldItem = p.getItemInHand();
            ItemMeta heldItemMeta = heldItem.getItemMeta();

            double effiupgPrice = necrozatp.getCustomConfig().getDouble("upgrades.efficiencyUpgrade");
            double fortupgPrice = necrozatp.getCustomConfig().getDouble("upgrades.fortuneUpgrade");

            // Obtenha o nível dos encantamentos
            int enchantLeveleffi = heldItemMeta.getEnchantLevel(Enchantment.DIG_SPEED);
            int enchantLevelfort = heldItemMeta.getEnchantLevel(Enchantment.LOOT_BONUS_BLOCKS);

            // Calcula os preços dos upgrades
            double priceeffi = effiupgPrice * Math.pow(2, enchantLeveleffi);
            double pricefort = fortupgPrice * Math.pow(2, enchantLevelfort);

            ItemStack efficiencyUpgrade = new ItemAPI(EfficiencyUpgrade.getType())
                    .setDisplayName("§6§lUpgrade Efficiency")
                    .setLore("", "  §6§lNivel " + enchantLeveleffi + " / 10", "", "  §l➥Preço: " + priceeffi)
                    .build();

            if (clickedItem.isSimilar(efficiencyUpgrade)) {
                if (EconomyUtils.getEconomy().getBalance(p) >= priceeffi) {
                    ItemStack efficiency = p.getInventory().getItemInHand();
                    ItemMeta upeff = efficiency.getItemMeta();

                    if (upeff.getEnchantLevel(Enchantment.DIG_SPEED) < 10) {
                        upeff.addEnchant(Enchantment.DIG_SPEED, upeff.getEnchantLevel(Enchantment.DIG_SPEED) + 1, true);
                        efficiency.setItemMeta(upeff);
                        p.getInventory().setItem(p.getInventory().getHeldItemSlot(), efficiency);

                        // Alterando o nome e a lore após o upgrade de Eficiência
                        upeff.setDisplayName("§6§lPickaxe of Efficiency (Lvl " + (upeff.getEnchantLevel(Enchantment.DIG_SPEED)) + ")");
                        upeff.setLore(Arrays.asList("§aEficiência no nível " + (upeff.getEnchantLevel(Enchantment.DIG_SPEED)) + "!", "§l➥Preço: " + priceeffi));
                        efficiency.setItemMeta(upeff);
                        OpenInventory(p);
                        p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
                        EconomyUtils.getEconomy().withdrawPlayer(p, priceeffi);

                        // Mensagem personalizada
                        String upgleveleffi = necrozatp.getCustomConfig().getString("messages.pickaxeupgleveleffi", "§aVocê aplicou o upgrade de Eficiência! Custo: §e{price}§a");
                        upgleveleffi = upgleveleffi.replace("{price}", String.valueOf(priceeffi));
                        // Envia a mensagem ao jogador, com a cor configurada
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', upgleveleffi));
                    } else {
                        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
                        String maxleveleffi = necrozatp.getCustomConfig().getString("messages.pickaxemaxleveleffi", "§cEste item já está no nível máximo de Eficiência.");
                        // Envia a mensagem ao jogador, com a cor configurada
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', maxleveleffi));
                    }
                } else {
                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
                    String insufmoney = necrozatp.getCustomConfig().getString("messages.moneyinsuficiente", "Você não tem dinheiro suficiente para este upgrade!");

                    // Envia a mensagem ao jogador, com a cor configurada
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', insufmoney));
                }
            }

            ItemStack fortuneUpgrade = new ItemAPI(FortuneUpgrade.getType())
                    .setDisplayName("§6§lUpgrade Fortune")
                    .setLore("", "  §6§lNivel " + enchantLevelfort + " / 10", "  §l➥Preço: " + pricefort)
                    .build();

            if (clickedItem.isSimilar(fortuneUpgrade)) {
                if (EconomyUtils.getEconomy().getBalance(p) >= pricefort) {
                    ItemStack fortune = p.getInventory().getItemInHand();
                    ItemMeta upfort = fortune.getItemMeta();

                    if (upfort.getEnchantLevel(Enchantment.LOOT_BONUS_BLOCKS) < 10) {
                        upfort.addEnchant(Enchantment.LOOT_BONUS_BLOCKS, upfort.getEnchantLevel(Enchantment.LOOT_BONUS_BLOCKS) + 1, true);
                        fortune.setItemMeta(upfort);
                        p.getInventory().setItem(p.getInventory().getHeldItemSlot(), fortune);

                        // Alterando o nome e a lore após o upgrade de Fortuna
                        upfort.setDisplayName("§6§lPickaxe of Fortune (Lvl " + (upfort.getEnchantLevel(Enchantment.LOOT_BONUS_BLOCKS)) + ")");
                        upfort.setLore(Arrays.asList("§aFortuna no nível " + (upfort.getEnchantLevel(Enchantment.LOOT_BONUS_BLOCKS)) + "!", "§l➥Preço: " + pricefort));
                        fortune.setItemMeta(upfort);
                        OpenInventory(p);
                        p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
                        EconomyUtils.getEconomy().withdrawPlayer(p, pricefort);

                        // Mensagem personalizada
                        String upglevelfort = necrozatp.getCustomConfig().getString("messages.pickaxeupglevelfort", "§aVocê aplicou o upgrade de Fortuna! Custo: §e{price}§a");
                        upglevelfort = upglevelfort.replace("{price}", String.valueOf(pricefort));
                        // Envia a mensagem ao jogador, com a cor configurada
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', upglevelfort));
                    } else {
                        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
                        String maxlevelfort = necrozatp.getCustomConfig().getString("messages.pickaxemaxlevelfort", "§cEste item já está no nível máximo de Fortuna.");

                        // Envia a mensagem ao jogador, com a cor configurada
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', maxlevelfort));
                    }
                } else {
                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
                    String insufmoney = necrozatp.getCustomConfig().getString("messages.moneyinsuficiente", "Você não tem dinheiro suficiente para este upgrade!");

                    // Envia a mensagem ao jogador, com a cor configurada
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', insufmoney));
                }
            }



            // Verifica se o jogador clicou na flecha para fechar
            if (clickedItem.isSimilar(CloseMenuItem)) {
                e.getWhoClicked().closeInventory();
            }

        }
    }

}
