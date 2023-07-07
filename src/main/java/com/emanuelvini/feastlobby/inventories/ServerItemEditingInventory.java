package com.emanuelvini.feastlobby.inventories;

import com.emanuelvini.feastcore.bukkit.api.common.ItemStackBuilder;
import com.emanuelvini.feastlobby.FeastLobby;
import com.emanuelvini.feastlobby.model.Server;
import com.emanuelvini.feastlobby.repository.ServerRepository;
import de.tr7zw.nbtapi.NBTItem;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import lombok.AllArgsConstructor;
import lombok.val;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Predicate;

@AllArgsConstructor
public class ServerItemEditingInventory {

    private final Server server;

    private final ServerRepository repository;

    private final FeastLobby plugin = FeastLobby.getInstance();

    private final Predicate<Event> filter;

    public static void open(Player player, Server server) {
        SmartInventory.builder()
                .provider(new ServerEditingInventory(server, FeastLobby.getInstance().getServerRepository(), e -> {
                    val chatEvent = (AsyncPlayerChatEvent) e;
                    return chatEvent.getPlayer().getName().equals(player.getName());
                }))
                .size(4, 9)
                .title("§7Menu de Edição")
                .build().open(player);
    }

    private void customSkullEditing(Player player, InventoryContents contents) {

        contents.set(0,0, ClickableItem.of(new ItemStackBuilder(Material.ARROW).withName("§cVoltar").buildStack(), e-> {
            player.closeInventory();
            ServerEditingInventory.open(player, server);
        }));

        contents.set(1,3, ClickableItem.of(new ItemStackBuilder(Material.NAME_TAG).
                withName("§aAlterar nome").
                withLore("§r", String.format("§aNome atual: %s", server.getItem().getItemMeta().getDisplayName())).
                buildStack()
        ,e -> {
            plugin.awaitEventWithFilter(AsyncPlayerChatEvent.class, filter, event -> {
                val chatEvent = (AsyncPlayerChatEvent) event;
                chatEvent.setCancelled(true);
                val item = new NBTItem(server.getItem());
                
            });
        }));

        contents.set(1,4, ClickableItem.empty(server.getItem()));



    }

    private void updateInventory() {
        if (server.getItem().getType().toString().contains("SKULL")) {

        }
    }


}
