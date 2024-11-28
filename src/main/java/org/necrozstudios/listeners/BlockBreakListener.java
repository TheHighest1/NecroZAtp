package org.necrozstudios.listeners;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.entity.Player;
import org.necrozstudios.necrozatp;
import org.necrozstudios.utils.EconomyUtils;
import org.necrozstudios.utils.MessageUtils;
import org.necrozstudios.utils.ActionBarUtil;

public class BlockBreakListener implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();

        // Obtém o tipo de bloco quebrado
        String blockType = event.getBlock().getType().name();

        // Verifica se o bloco quebrado é um minério (contém "ORE" no nome)
        if (!blockType.contains("ORE")) {
            return;  // Se não for minério, não faz nada
        }

        // Obtém a seção de blocos configurados no config.yml
        ConfigurationSection blocksSection = necrozatp.getPlugin(necrozatp.class).getConfig().getConfigurationSection("blocks price");
        if (blocksSection == null || !blocksSection.contains(blockType)) {
            return; // Se o bloco não estiver configurado, não faz nada
        }

        // Obtém o valor de recompensa do bloco
        double reward = blocksSection.getDouble(blockType + ".reward", 0);

        // Dá dinheiro ao jogador usando Vault
        EconomyUtils.depositPlayer(player, reward);

        // Evita duplicação de mensagens
        if (!event.isCancelled()) {
            // Verifica se a mensagem no chat está ativada no config.yml
            boolean isMessageEnabled = necrozatp.getPlugin(necrozatp.class).getConfig().getBoolean("messages.money_received_enabled", true);

            // Se a mensagem no chat estiver ativada, envia a mensagem
            if (isMessageEnabled) {
                // Substitui os placeholders %block% e %amount%
                MessageUtils.sendConfigMessage(
                        player,
                        necrozatp.getPlugin(necrozatp.class),
                        "messages.money_received",
                        "§aVocê quebrou um bloco de §e%block%§a e recebeu §6$%amount%§a!",
                        "%player%", player.getName(),
                        "%block%", blockType.replace("_", " "),  // Substitui _ por espaço no nome do bloco
                        "%amount%", String.format("%.2f", reward)
                );
            }

            // Verifica se a mensagem na Action Bar está ativada no config.yml
            boolean isActionBarMessageEnabled = necrozatp.getPlugin(necrozatp.class).getConfig().getBoolean("messages.money_received_actionbar_enabled", true);

            // Se a mensagem na Action Bar estiver ativada, envia a mensagem
            if (isActionBarMessageEnabled) {
                // Mensagem da Action Bar com placeholders para bloco e recompensa
                String actionBarMessage = necrozatp.getPlugin(necrozatp.class).getConfig().getString(
                        "messages.money_received_actionbar",
                        "§aVocê quebrou §e%block%§a e ganhou §6$%amount%§a!"
                );

                // Substitui os placeholders na mensagem da Action Bar
                actionBarMessage = actionBarMessage
                        .replace("%block%", blockType.replace("_", " "))  // Substitui _ por espaço
                        .replace("%amount%", String.format("%.2f", reward));

                // Envia a Action Bar ao jogador
                ActionBarUtil.sendActionBar(player, actionBarMessage);
            }
        }

        // Cancela o drop do bloco
        event.setDropItems(false);  // Impede que o bloco seja dropado no chão
    }
}
