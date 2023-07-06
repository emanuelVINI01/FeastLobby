package com.emanuelvini.feastlobby.inventories;

import com.emanuelvini.feastcore.bukkit.api.common.ItemStackBuilder;
import com.emanuelvini.feastlobby.FeastLobby;
import com.emanuelvini.feastlobby.model.Server;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import lombok.AllArgsConstructor;
import lombok.val;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.function.Predicate;

@AllArgsConstructor
public class ServerEditingInventory implements InventoryProvider {

    private final Server server;

    private final FeastLobby plugin = FeastLobby.getInstance();

    private final Predicate<Event> filter;

    public static void open(Player player, Server server) {
        SmartInventory.builder()
                .provider(new ServerEditingInventory(server, e -> {
                    val chatEvent = (AsyncPlayerChatEvent) e;
                    return chatEvent.getPlayer().getName() == player.getName();
                }))
                .size(3, 9)
                .title("§7Menu de Edição")
                .build().open(player);
    }

    private void updateInventoryByServer(InventoryContents contents) {
        contents.set(1, 2, ClickableItem.of(
                new ItemStackBuilder(Material.ITEM_FRAME).
                        withName("§bAlterar nome do servidor").
                        withLore("§r", "§aClique aqui para alterar o nome do servidor.", "§r", "§aNome atual: "+server.getName()).buildStack(),
                e -> {
                    val player = (Player) e.getWhoClicked();
                    player.closeInventory();
                    player.sendMessage("§r\n§aDigite o novo nome do servidor. Exemplo: §f&eRankUP Alguma Coisa");
                    plugin.awaitEventWithFilter(AsyncPlayerChatEvent.class, filter, event -> {
                        val chatEvent = (AsyncPlayerChatEvent) event;
                        server.setName(ChatColor.translateAlternateColorCodes('&',chatEvent.getMessage()));
                        plugin.getServerRepository().updateServer(server);
                        chatEvent.setCancelled(true);
                        player.sendMessage("§aNome do servidor atualizado com sucesso.");
                        open(player, server);
                    });
                }));

    }

    @Override
    public void init(Player player, InventoryContents inventoryContents) {
        updateInventoryByServer(inventoryContents);
    }

    @Override
    public void update(Player player, InventoryContents inventoryContents) {

    }
}
