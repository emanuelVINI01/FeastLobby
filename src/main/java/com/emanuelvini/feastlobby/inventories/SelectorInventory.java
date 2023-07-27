package com.emanuelvini.feastlobby.inventories;

import com.emanuelvini.feastlobby.FeastLobby;
import com.emanuelvini.feastlobby.configuration.FileValue;
import com.emanuelvini.feastlobby.configuration.MessageValue;
import com.emanuelvini.feastlobby.configuration.SelectorValue;
import com.emanuelvini.feastlobby.model.Server;
import com.emanuelvini.feastlobby.repository.ServerRepository;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import lombok.AllArgsConstructor;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.stream.Collectors;

@AllArgsConstructor
public class SelectorInventory implements InventoryProvider {

    private final ServerRepository repository;

    public static void open(Player player) {
        SmartInventory.builder().
                size(SelectorValue.get(SelectorValue::inventorySize)/9, 9).
                title(SelectorValue.get(SelectorValue::inventoryName)).
                id(String.valueOf(Math.random() * Math.random())).
                provider(new SelectorInventory(FeastLobby.getInstance().getServerRepository())).
                build().open(player);
    }

    private void updateServers(Player player, InventoryContents contents) {
        val section = SelectorValue.servers();
        for (String key : section.getKeys(false)) {
            Server server =  repository.getServer(key);
            if (server == null) {
                FeastLobby.getInstance().getCustomLogger().log(String.format("Há um erro na selector.yml. O Servidor §f%s§c não existe.", key), "c");
            } else {
                val slot = section.getInt(String.format("%s.slot", key));
                val row = Math.round(slot/9);
                val column = Math.round(slot % 9);
                val updatedServerItem = server.getItem().clone();
                if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
                    val meta = updatedServerItem.getItemMeta();
                    if (meta.hasLore())
                        meta.setLore(updatedServerItem.getItemMeta().getLore().stream().map(s -> me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(player, s)).collect(Collectors.toList()));
                    meta.setDisplayName(me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(player, updatedServerItem.getItemMeta().getDisplayName()));
                    updatedServerItem.setItemMeta(meta);
                    contents.set(row, column, ClickableItem.of(updatedServerItem, e -> {
                        Bukkit.getScheduler().runTaskAsynchronously(FeastLobby.getInstance(), () -> {
                            if (server.isMaintenance() && !player.hasPermission("feastlobby.bypass.maintenance")) {
                                player.sendMessage(MessageValue.get(MessageValue::serverIsInMaintenance));
                                return;
                            }
                            if (player.hasPermission(String.format("feastlobby.bypass.file.%s", server.getId()))) {
                                server.sendPlayerTo(player);
                                return;
                            }
                            if (FileValue.get(FileValue::closeInventoryOnJoinLeave)) player.closeInventory();

                            if (server.isInFile(player)) {
                                server.removeFromFile(player);
                            } else {
                                server.joinFile(player);
                            }
                        });
                    }));
                } else {
                    contents.set(row, column, ClickableItem.of(updatedServerItem, e -> {
                        Bukkit.getScheduler().runTaskAsynchronously(FeastLobby.getInstance(), () -> {
                            if (server.isMaintenance() && !player.hasPermission("feastlobby.bypass.maintenance")) {
                                player.sendMessage(MessageValue.get(MessageValue::serverIsInMaintenance));
                                return;
                            }
                            if (player.hasPermission(String.format("feastlobby.bypass.file.%s", server.getId()))) {
                                server.sendPlayerTo(player);
                                return;
                            }
                            if (FileValue.get(FileValue::closeInventoryOnJoinLeave)) player.closeInventory();

                            if (server.isInFile(player)) {
                                server.removeFromFile(player);
                            } else {
                                server.joinFile(player);
                            }
                        });
                    }));
                }
            }
        }
    }

    @Override
    public void init(Player player, InventoryContents inventoryContents) {
        updateServers(player, inventoryContents);
    }

    @Override
    public void update(Player player, InventoryContents inventoryContents) {
        updateServers(player, inventoryContents);
    }
}
