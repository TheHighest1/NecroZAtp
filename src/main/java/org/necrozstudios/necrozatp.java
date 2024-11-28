package org.necrozstudios;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.necrozstudios.events.JoinEvent;
import org.necrozstudios.listeners.PlayerInteractListener;
import org.necrozstudios.listeners.BlockBreakListener;
import org.necrozstudios.listeners.DoubleCoinEnchantListener;
import org.necrozstudios.utils.EconomyUtils;
import org.bukkit.entity.Player;
import org.necrozstudios.commands.GiveCustomPickaxeCommand;
import org.necrozstudios.commands.PluginReload;
import org.necrozstudios.events.JoinEvent;

public final class necrozatp extends JavaPlugin {
    private static FileConfiguration customConfig = null;

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        customConfig = getConfig();  // Carrega o config.yml como customConfig
        EconomyUtils.setupEconomy(this);



        getCommand("picareta").setExecutor(new GiveCustomPickaxeCommand()); // Seta o comando
        getCommand("necrozreload").setExecutor(new PluginReload(this));


        getServer().getPluginManager().registerEvents(new PlayerInteractListener(), this); // registra o evento
        getServer().getPluginManager().registerEvents(new BlockBreakListener(), this); // registra o evento
        getServer().getPluginManager().registerEvents(new JoinEvent(), this);



    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static FileConfiguration getCustomConfig() {
        return customConfig;
    }

}
