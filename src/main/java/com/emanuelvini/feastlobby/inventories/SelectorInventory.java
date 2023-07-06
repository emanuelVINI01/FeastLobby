package com.emanuelvini.feastlobby.inventories;

import com.emanuelvini.feastlobby.FeastLobby;
import com.emanuelvini.feastlobby.configuration.SelectorValue;
import com.emanuelvini.feastlobby.model.Server;
import com.emanuelvini.feastlobby.repository.ServerRepository;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import lombok.AllArgsConstructor;
import lombok.val;
import org.bukkit.entity.Player;

@AllArgsConstructor
public class SelectorInventory implements InventoryProvider {

    private final ServerRepository repository;

    public static void open(Player player) {
        SmartInventory.builder().
                size(SelectorValue.get(SelectorValue::inventorySize)/9, 9).
                title(SelectorValue.get(SelectorValue::inventoryName)).
                provider(new SelectorInventory(FeastLobby.getInstance().getServerRepository())).
                build().open(player);
    }

    private void updateServers(InventoryContents contents) {
        val section = SelectorValue.servers();
        for (String key : section.getKeys(false)) {
            Server server =  repository.getServer(key);
            if (server == null) {
                FeastLobby.getInstance().getCustomLogger().log(String.format("Há um erro na selector.yml. O Servidor §f%s§c não existe.", key), "c");
            } else {
                val slot = section.getInt(String.format("%s.slot", key));
                val row = Math.round(slot/9);
                val column = Math.round(slot % 9);
                contents.set(row, column, ClickableItem.of(server.getItem(), e -> {
                    e.getWhoClicked().sendMessage(server.getName());
                }));
            }
        }
    }

    @Override
    public void init(Player player, InventoryContents inventoryContents) {
        updateServers(inventoryContents);
    }

    @Override
    public void update(Player player, InventoryContents inventoryContents) {
        updateServers(inventoryContents);
    }
}
