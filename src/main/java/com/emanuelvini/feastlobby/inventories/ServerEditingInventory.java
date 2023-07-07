package com.emanuelvini.feastlobby.inventories;

import com.emanuelvini.feastcore.bukkit.api.common.ItemStackBuilder;
import com.emanuelvini.feastlobby.FeastLobby;
import com.emanuelvini.feastlobby.model.Server;
import com.emanuelvini.feastlobby.repository.ServerRepository;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import lombok.AllArgsConstructor;
import lombok.val;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.function.Predicate;

@AllArgsConstructor
public class ServerEditingInventory implements InventoryProvider {

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

    private void updateInventoryByServer(InventoryContents contents) {
        contents.set(1, 2, ClickableItem.of(
                new ItemStackBuilder(Material.NAME_TAG).
                        withName("§bAlterar nome do servidor").
                        withLore("§r", "§aClique aqui para alterar o nome do servidor.", "§r", "§aNome atual: "+server.getName()).buildStack(),
                e -> {
                    val player = (Player) e.getWhoClicked();
                    player.closeInventory();
                    player.sendMessage("§r\n§aDigite o novo nome do servidor. Exemplo: §f&eRankUP Alguma Coisa");
                    plugin.awaitEventWithFilter(AsyncPlayerChatEvent.class, filter, event -> {
                        val chatEvent = (AsyncPlayerChatEvent) event;
                        server.setName(ChatColor.translateAlternateColorCodes('&',chatEvent.getMessage()));
                        repository.updateServer(server);
                        chatEvent.setCancelled(true);
                        player.sendMessage("§a§lSUCESSO! §aNome do servidor atualizado.");
                        open(player, server);
                    });
                }));
        contents.set(1, 3, ClickableItem.of(
                new ItemStackBuilder(Material.ITEM_FRAME).
                        withName("§bAlterar icone do servidor").
                        withLore("§r", "§aClique aqui para alterar o icone do servidor (no menu).", "§r").buildStack(),
                e -> {
                    val player = (Player) e.getWhoClicked();
                    player.closeInventory();
                    player.sendMessage("§cEssa função está em desenvolvimento...");

                }));
        contents.set(1, 4, ClickableItem.empty(
                new ItemStackBuilder(server.getItem().getType()).
                        withName("§eInformações do servidor").
                        withLore(
                                "§r",
                                String.format("§aID: %s", server.getId()),
                                String.format("§aNome atual: %s.", server.getName()),
                                String.format("§aServidor atual: §e%s", server.getBungee()),
                                String.format("§aEndereço IP: %s", server.getAddress()),
                                String.format("§aStatus: %s", server.isMaintenance() ? "§cManutenção§a" :
                                        PlaceholderAPI.setPlaceholders(null, String.format("§aOnline | %%bungee_%s%%", server.getBungee()))),
                                "§r").
                        buildStack()));
        contents.set(1, 5, ClickableItem.of(
                new ItemStackBuilder(Material.ITEM_FRAME).
                        withName(String.format("§c%s manutenção", server.isMaintenance() ? "Desativar" : "Ativar")).
                        withLore("§r", String.format("§aClique aqui para %s a manutenção.", server.isMaintenance() ? "§cdesativar§a" : "§cativar§a"), "§r").
                        toSkullBuilder().
                        withTexture(server.isMaintenance() ? "http://textures.minecraft.net/texture/61856c7b378d350262143843d1f9fbb21911a71983ba7b39a4d4ba5b66bedc6" : "http://textures.minecraft.net/texture/4b599c618e914c25a37d69f541a22bebbf751615263756f2561fab4cfa39e").
                        buildSkull(),
                e -> {
                    val player = (Player) e.getWhoClicked();
                    val maintenance = !server.isMaintenance();
                    server.setMaintenance(maintenance);
                    repository.updateServer(server);
                    server.clearFile();
                    player.sendMessage("§a§lSUCESSO! Manutenção atualizada.");

                }));
        contents.set(1, 7, ClickableItem.of(
                new ItemStackBuilder(Material.WEB).
                        withName("§bAlterar servidor BungeeCord").
                        withLore("§r", "§aClique aqui para alterar o servidor BungeeCord.", String.format("§aServidor atual: §e%s", server.getBungee()) , "§r").buildStack(),
                e -> {
                    val player = (Player) e.getWhoClicked();
                    player.closeInventory();
                    player.sendMessage("§r\n§aDigite o novo servidor do bungee. Exemplo: §erankup");
                    plugin.awaitEventWithFilter(AsyncPlayerChatEvent.class, filter, event -> {
                        val chatEvent = (AsyncPlayerChatEvent) event;
                        server.setBungee(chatEvent.getMessage());
                        repository.updateServer(server);
                        chatEvent.setCancelled(true);
                        player.sendMessage("§a§lSUCESSO! §aServidor bungeecord atualizado.");
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
        updateInventoryByServer(inventoryContents);
    }
}
