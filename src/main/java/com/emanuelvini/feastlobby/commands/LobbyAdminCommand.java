package com.emanuelvini.feastlobby.commands;

import com.emanuelvini.feastcore.bukkit.api.common.ItemStackBuilder;
import com.emanuelvini.feastlobby.FeastLobby;
import com.emanuelvini.feastlobby.configuration.MessageValue;
import com.emanuelvini.feastlobby.inventories.ServerEditingInventory;
import com.emanuelvini.feastlobby.model.Server;
import com.emanuelvini.feastlobby.repository.ServerRepository;
import lombok.val;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.command.Context;
import me.saiintbrisson.minecraft.command.target.CommandTarget;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.function.Predicate;

public class LobbyAdminCommand {

    private ServerRepository repository;

    public LobbyAdminCommand() {
        repository = FeastLobby.getInstance().getServerRepository();
    }

    @Command(
            name = "lobbyadmin",
            aliases = {"adminlobby", "lobbyadm", "admlobby", "feastlobby"},
            permission = "feastlobby.admin",
            target = CommandTarget.PLAYER
    )
    public void onHelp(Context<Player> context) {
        context.sendMessage(
                "§d§lFEAST LOBBY\n" +
                "\n" +
                        "§d/lobbyadmin criar §8-§7 Cria um servidor");
    }

    @Command(
            name = "lobbyadmin.editarservidor",
            permission = "feastlobby.admin.editarservidor",
            usage = "§c/lobbyadmin editarservidor <id>"
    )
    public void onEdit(Context<Player> context, String id) {
        val server = repository.getServer(id);
        if (server == null) {
            context.sendMessage(MessageValue.get(MessageValue::notExistsServer));
            return;
        }
        ServerEditingInventory.open(context.getSender(), server);
    }

    @Command(
            name = "lobbyadmin.criar",
            permission = "feastlobby.admin.criar",
            usage = "§cUso: /lobbyadmin criar"
    )
    public void onCreate(Context<Player> context) {
        val player = context.getSender();
        val builder = Server.builder();
        val plugin = FeastLobby.getInstance();

        Predicate<Event> filter = (event) -> {
          val playerEvent = ((AsyncPlayerChatEvent) event) .getPlayer();
          return playerEvent.getName().equals(player.getName());
        };

        //Server ID
        player.sendMessage(
                "§r\n§aDigite o id do servidor que será usado para editar. Exemplo: §erankupalgo\n");
        plugin.awaitEventWithFilter(AsyncPlayerChatEvent.class, filter, event -> {
            val chatEventId = (AsyncPlayerChatEvent) event;
            chatEventId.setCancelled(true);
            if (repository.getServer(chatEventId.getMessage()) != null) {
                player.sendMessage("§cJá existe um servidor com esse ID.");
                return;
            }
            builder.id(chatEventId.getMessage());
            player.sendMessage(
                    "§r\n§aAgora, digite o nome do servidor que irá aparecer quando a pessoa entrar na fila. Exemplo: §e&eRankUP Alguma Coisa\n");
            plugin.awaitEventWithFilter(AsyncPlayerChatEvent.class, filter, eventName -> {
                val chatEventName = (AsyncPlayerChatEvent) eventName;
                chatEventName.setCancelled(true);
                val name = ChatColor.translateAlternateColorCodes('&', chatEventName.getMessage());

                builder.name(name);
                //Bungee Server name
                player.sendMessage(
                        "§r\n§aAgora, digite o nome do servidor que está na configuração do BungeeCord/Velocity. Exemplo: §erankup\n");
                plugin.awaitEventWithFilter(AsyncPlayerChatEvent.class, filter, eventBungee -> {
                    val chatEventBungee = (AsyncPlayerChatEvent) eventBungee;
                    chatEventBungee.setCancelled(true);
                    if (plugin.getServerRepository().getServers().stream().anyMatch(s -> s.getBungee().equals(chatEventBungee.getMessage()))) {
                        player.sendMessage("§cJá existe um servidor com esse identificador BungeeCord.");
                        return;
                    }
                    builder.bungee(chatEventBungee.getMessage());
                    //Adress
                    player.sendMessage(
                            "§r\n§aAgora, digite o endereço do servidor que está na configuração do BungeeCord/Velocity. Exemplo: §e127.0.0.1:25565\n");
                    plugin.awaitEventWithFilter(AsyncPlayerChatEvent.class, filter, eventAddress -> {
                        val chatEventAddress = (AsyncPlayerChatEvent) eventAddress;
                        chatEventAddress.setCancelled(true);
                        builder.address(ChatColor.translateAlternateColorCodes('&', chatEventAddress.getMessage()));

                        builder.item(new ItemStackBuilder(Material.STONE).withName("§aServidor Novo").withLore("§8Uma lore qualquer!!!").buildStack());
                        builder.maintenance(true);
                        val server = builder.build();
                        repository.saveServer(server);
                        ServerEditingInventory.open(player, server);
                    });
                });
            });
        });

    }
}
