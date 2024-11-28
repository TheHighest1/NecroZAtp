package org.necrozstudios.apis;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;
import java.util.List;

public class ItemAPI {

    private ItemStack itemStack;
    private ItemMeta itemMeta;

    public ItemAPI(ItemStack itemStack) {
        this.itemStack = itemStack;
        this.itemMeta = itemStack.getItemMeta();
    }

    public ItemAPI(Material material) {
        this.itemStack = new ItemStack(material);
        this.itemMeta = itemStack.getItemMeta();
    }

    public ItemAPI(Material material, int id) {
        this.itemStack = new ItemStack(material, 1, (short) id);
        this.itemMeta = itemStack.getItemMeta();
    }

    public ItemAPI setDisplayName(String name) {
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        return this;
    }

    public ItemAPI setMetaID(byte metaID) {
        itemStack.getData().setData(metaID);
        return this;
    }

    public ItemAPI setOwner(String name) {
        SkullMeta m = (SkullMeta) itemMeta;
        m.setOwner(name);
        itemStack.setItemMeta(m);
        return this;
    }

    public ItemAPI setAmount(int amount) {
        this.itemStack.setAmount(amount);
        return this;
    }

    public ItemAPI setDurability(short durability) {
        this.itemStack.setDurability(durability);
        return this;
    }

    public ItemAPI addEnchantment(Enchantment enchantment, int lvl) {
        this.itemMeta.addEnchant(enchantment, lvl, false);
        return this;
    }

    public ItemAPI addEnchantmentnorestriction(Enchantment enchantment, int lvl) {
        this.itemMeta.addEnchant(enchantment, lvl, true);
        return this;
    }

    public ItemAPI clearEnchantments() {
        this.itemMeta.getEnchants().forEach((enchantment, integer) -> this.itemMeta.removeEnchant(enchantment));
        return this;
    }

    public ItemAPI removeEnchantment(Enchantment enchantment) {
        if (this.itemMeta.getEnchants().containsKey(enchantment))
            this.itemMeta.removeEnchant(enchantment);
        return this;
    }

    public ItemAPI setLore(List<String> lines) {
        this.itemMeta.setLore(lines);
        return this;
    }

    public ItemAPI setLore(String... lines) {
        this.itemMeta.setLore(Arrays.asList(lines));
        return this;
    }

    public ItemAPI resetLore() {
        this.itemMeta.getLore().clear();
        return this;
    }

    public ItemStack build() {
        itemStack.setItemMeta(itemMeta);
        return itemStack;

    }


    public static void removeItems(Inventory inventory, ItemStack item, int amount) {
        if (amount <= 0) return;
        int size = inventory.getSize();
        for (int slot = 0; slot < size; slot++) {
            ItemStack is = inventory.getItem(slot);
            if (is == null) continue;
            if (item.isSimilar(is)) {
                int newAmount = is.getAmount() - amount;
                if (newAmount > 0) {
                    is.setAmount(newAmount);
                    break;
                } else {
                    inventory.clear(slot);
                    amount = -newAmount;
                    //inventory.setItem(slot,is);
                    if (amount == 0) break;
                }
            }
        }
    }
}
