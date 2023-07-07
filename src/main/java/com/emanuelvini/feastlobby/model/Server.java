package com.emanuelvini.feastlobby.model;

import com.emanuelvini.feastcore.bukkit.api.common.ActionBarUtil;
import com.emanuelvini.feastlobby.FeastLobby;
import com.emanuelvini.feastlobby.configuration.ErrorFixerValue;
import com.emanuelvini.feastlobby.configuration.FileValue;
import com.emanuelvini.feastlobby.configuration.MessageValue;
import lombok.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;


@AllArgsConstructor
@Builder
@Getter
@Setter
public class Server {

    private String id;

    private String name;

    private String bungee;

    private String address;

    private ItemStack item;

    private boolean maintenance;

    private final List<String> file = new ArrayList<>();

    public boolean isInFile(Player player) {
        if (ErrorFixerValue.get(ErrorFixerValue::debug)) FeastLobby.getInstance().getCustomLogger().
                log(String.format("[DEBUG] O jogador §f%s§6 foi verificado sobre seu status na fila.", player.getName()), "6");
        return file.contains(player.getName());
    }

    public void removeFromFile(Player player) {
        if (ErrorFixerValue.get(ErrorFixerValue::debug)) FeastLobby.getInstance().getCustomLogger().
                log(String.format("[DEBUG] O jogador §f%s§6 saiu da fila.", player.getName()), "6");
        player.sendMessage(MessageValue.get(MessageValue::leaveFile).replace("{server_name}", name));
        file.remove(player.getName());
        updateFile();
    }

    public void joinFile(Player player) {
        if (ErrorFixerValue.get(ErrorFixerValue::debug)) FeastLobby.getInstance().getCustomLogger().
                log(String.format("[DEBUG] O jogador §f%s§6 entrou na fila.", player.getName()), "6");
        player.sendMessage(MessageValue.get(MessageValue::joinedInFile).replace("{server_name}", name).
                replace("{position}", String.valueOf(file.size() + 1)));
        file.add(player.getName());
    }

    public void clearFile() {
        if (ErrorFixerValue.get(ErrorFixerValue::debug)) FeastLobby.getInstance().getCustomLogger().
                log("[DEBUG] A fila acabou de ser limpa.", "6");
        file.clear();
    }

    public void updateFile() {
        if (ErrorFixerValue.get(ErrorFixerValue::debug)) FeastLobby.getInstance().getCustomLogger().
                log("[DEBUG] A fila está sendo atualizada...", "6");
        if (file.size() != 0) {
            file.forEach(name -> {
                val player = Bukkit.getPlayer(name);
                if (player == null) {
                    file.remove(name);
                    return;
                }
                val position = file.indexOf(name) - 1;
                if (ErrorFixerValue.get(ErrorFixerValue::debug)) FeastLobby.getInstance().getCustomLogger().
                        log(String.format("[DEBUG] O jogador §f%s§6 está na posição %d da fila.", player.getName(), position), "6");
                val message = MessageValue.get(MessageValue::positionInFile).
                        replace("{server_name}", name).
                        replace("{position}", String.valueOf(position));
                if (ErrorFixerValue.get(ErrorFixerValue::debug)) FeastLobby.getInstance().getCustomLogger().
                        log(String.format("[DEBUG] Mensagem gerada da fila: %s", message), "6");
                if (FileValue.get(FileValue::enableActionbarMessage)) {
                    ActionBarUtil.sendActionBar(player, message);
                }
                if (FileValue.get(FileValue::enableChatMessage)) {
                    player.sendMessage(message);
                }
            });

            val name = file.get(0);
            val player = Bukkit.getPlayer(name);
            if (player != null) {
                if (isMaintenance() && !player.hasPermission("feastlobby.bypass.maintenance")) {
                    player.sendMessage(MessageValue.get(MessageValue::serverIsInMaintenance));
                } else {
                    if (ErrorFixerValue.get(ErrorFixerValue::debug)) FeastLobby.getInstance().getCustomLogger().
                            log(String.format("[DEBUG] O jogador §f%s§6 foi enviado ao servidor %s.", player.getName(), getName()), "6");
                    sendPlayerTo(player);
                }
            }
            file.remove(name);

        }
    }

    public void sendPlayerTo(Player player) {
        FeastLobby.getInstance().getBungeeCordChannel().sendPlayerToServer(player,bungee);
    }


}
