package com.emanuelvini.feastlobby.inventories;

import com.emanuelvini.feastcore.bukkit.api.common.ItemStackBuilder;
import com.emanuelvini.feastlobby.FeastLobby;
import com.emanuelvini.feastlobby.model.Server;
import com.emanuelvini.feastlobby.repository.ServerRepository;
import de.tr7zw.nbtapi.NBT;
import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTItem;
import de.tr7zw.nbtapi.NBTListCompound;
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

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Predicate;

@AllArgsConstructor
public class ServerItemEditingInventory implements InventoryProvider {

    private final Server server;

    private final ServerRepository repository;

    private final FeastLobby plugin = FeastLobby.getInstance();

    private final Predicate<Event> filter;

    public static void open(Player player, Server server) {
        SmartInventory.builder().provider(new ServerItemEditingInventory(server, FeastLobby.getInstance().getServerRepository(), e -> {
            val chatEvent = (AsyncPlayerChatEvent) e;
            return chatEvent.getPlayer().getName().equals(player.getName());
        })).size(4, 9).title("§7Menu de Edição").build().open(player);
    }

    private void customSkullEditing(Player player, InventoryContents contents) {

        contents.set(0, 0, ClickableItem.of(new ItemStackBuilder(Material.ARROW).withName("§cVoltar").buildStack(), e -> {
            player.closeInventory();
            ServerEditingInventory.open(player, server);
        }));

        contents.set(1, 2, ClickableItem.of(new ItemStackBuilder(Material.NAME_TAG).withName("§aAlterar nome").withLore("§r", String.format("§aNome atual: %s", server.getItem().getItemMeta().getDisplayName())).buildStack(), e -> {
            player.closeInventory();
            player.sendMessage("§r\n§aDigite o novo nome para o item.\n§r");
            plugin.awaitEventWithFilter(AsyncPlayerChatEvent.class, filter, event -> {
                val chatEvent = (AsyncPlayerChatEvent) event;
                chatEvent.setCancelled(true);
                NBT.modify(server.getItem(), nbt -> {
                    nbt.modifyMeta((roNBT, meta) -> { // do not modify the nbt while modifying the meta!
                        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', chatEvent.getMessage()));
                    });
                });
                repository.updateServer(server);
                player.sendMessage("§a§lSUCESSO! §aO nome do item foi atualizado.");
                open(player, server);
            });
        }));

        val lines = new ArrayList<>(Arrays.asList("§r", "§aDescrição atual: "));
        if (server.getItem().getItemMeta().hasLore()) {
            lines.addAll(server.getItem().getItemMeta().getLore());
        }

        contents.set(1, 3, ClickableItem.of(new ItemStackBuilder(Material.BED).withName("§aAlterar descrição").
                withLore(lines).buildStack(), e -> {
            player.closeInventory();
            player.sendMessage("§r\n§aDigite a nova descrição, para uma nova linha envie.\n§aPara redefinir, use: §fredefinir§r\n§aPara remover uma linha use: §fremover 1");
            val lore = new ArrayList<String>();
            if (server.getItem().getItemMeta().hasLore()) {
                lore.addAll(server.getItem().getItemMeta().getLore());
            }
            plugin.awaitEventWithFilter(AsyncPlayerChatEvent.class, filter, event -> {
                val chatEvent = (AsyncPlayerChatEvent) event;
                chatEvent.setCancelled(true);
                if (chatEvent.getMessage().equalsIgnoreCase("redefinir")) {
                    NBT.modify(server.getItem(), nbt -> {
                        nbt.modifyMeta((roNBT, meta) -> { // do not modify the nbt while modifying the meta!
                            meta.setLore(Collections.emptyList());
                        });
                    });
                    return;
                }
                if (chatEvent.getMessage().startsWith("remover ")) {
                    try {
                       int line = Integer.parseInt(chatEvent.getMessage().replace("remover ", ""));
                       lore.remove(line);
                        NBT.modify(server.getItem(), nbt -> {
                            nbt.modifyMeta((roNBT, meta) -> { // do not modify the nbt while modifying the meta!
                                meta.setLore(lore);
                            });
                        });
                       player.sendMessage("§aLinha atualizada com sucesso.");
                    } catch (Exception ex) {
                        player.sendMessage("§c§lERRO! §cEssa linha é inválida.");
                        return;
                    }
                    return;
                }
                lore.add(ChatColor.translateAlternateColorCodes('&', chatEvent.getMessage()));
                NBT.modify(server.getItem(), nbt -> {
                    nbt.modifyMeta((roNBT, meta) -> { // do not modify the nbt while modifying the meta!
                        meta.setLore(lore);
                    });
                });
                repository.updateServer(server);
                player.sendMessage("§a§lSUCESSO! §aA descrição do item foi atualizada com sucesso.");
                open(player, server);
            });
        }));

        contents.set(1, 4, ClickableItem.empty(server.getItem()));

        contents.set(1, 5, ClickableItem.of(new ItemStackBuilder(Material.SKULL_ITEM).withName("§aAlterar URL da Cabeça").withLore("§r").buildStack(), e -> {
            player.closeInventory();
            player.sendMessage("§r\n§aDigite a nova URL da cabeça do item.\n§r");
            plugin.awaitEventWithFilter(AsyncPlayerChatEvent.class, filter, event -> {
                val chatEvent = (AsyncPlayerChatEvent) event;
                chatEvent.setCancelled(true);
                NBTItem nbti = new NBTItem(server.getItem());
                NBTCompound skullNBT = nbti.getOrCreateCompound("SkullOwner");
                skullNBT.setString("Name", "Dragao banana");
                skullNBT.setString("Id", String.valueOf(UUID.randomUUID()));
                NBTListCompound textureCompound = skullNBT.getOrCreateCompound("Properties").getCompoundList("textures").addCompound();
                textureCompound.setString("Value", Base64.getEncoder().encodeToString(String.format("{\"textures\":{\"SKIN\":{\"url\":\"%s\"}}}", "http://textures.minecraft.net/texture/" + chatEvent.getMessage()).getBytes(StandardCharsets.UTF_8)));
                server.setItem(nbti.getItem());
                repository.updateServer(server);
                player.sendMessage("§a§lSUCESSO! §aA URL da cabeça do item foi atualizada.");
                open(player, server);
            });
        }));
        contents.set(1, 6, ClickableItem.of(new ItemStackBuilder().withName("§cDesabilitar cabeça customizada").toSkullBuilder().withTexture("http://textures.minecraft.net/texture/61856c7b378d350262143843d1f9fbb21911a71983ba7b39a4d4ba5b66bedc6").buildSkull(), e -> {
            player.sendMessage("§a§lSUCESSO! §aA cabeça customizada foi desativada com sucesso.");
            server.setItem(new ItemStackBuilder(Material.STONE).withName(server.getItem().getItemMeta().getDisplayName()).withLore(server.getItem().getItemMeta().getLore()).buildStack());
            repository.updateServer(server);
        }));

    }

    private void itemEditing(Player player, InventoryContents contents) {
        contents.set(0, 0, ClickableItem.of(new ItemStackBuilder(Material.ARROW).withName("§cVoltar").buildStack(), e -> {
            player.closeInventory();
            ServerEditingInventory.open(player, server);
        }));

        contents.set(1, 2, ClickableItem.of(new ItemStackBuilder(Material.NAME_TAG).withName("§aAlterar nome").withLore("§r", String.format("§aNome atual: %s", server.getItem().getItemMeta().getDisplayName())).buildStack(), e -> {
            player.closeInventory();
            player.sendMessage("§r\n§aDigite o novo nome para o item.\n§r");
            plugin.awaitEventWithFilter(AsyncPlayerChatEvent.class, filter, event -> {
                val chatEvent = (AsyncPlayerChatEvent) event;
                chatEvent.setCancelled(true);
                NBT.modify(server.getItem(), nbt -> {
                    nbt.modifyMeta((roNBT, meta) -> { // do not modify the nbt while modifying the meta!
                        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', chatEvent.getMessage()));
                    });
                });
                repository.updateServer(server);
                player.sendMessage("§a§lSUCESSO! §aO nome do item foi atualizado.");
                open(player, server);
            });
        }));

        contents.set(1, 3, ClickableItem.of(new ItemStackBuilder(Material.BED).withName("§aAlterar descrição").withLore("§r", "§aDescrição atual:", String.join("<n>", server.getItem().getItemMeta().getLore())).buildStack(), e -> {
            player.closeInventory();
            player.sendMessage("§r\n§aDigite a nova descrição, use <n> para uma nova linha.\n§r");
            plugin.awaitEventWithFilter(AsyncPlayerChatEvent.class, filter, event -> {
                val chatEvent = (AsyncPlayerChatEvent) event;
                chatEvent.setCancelled(true);
                NBT.modify(server.getItem(), nbt -> {
                    nbt.modifyMeta((roNBT, meta) -> { // do not modify the nbt while modifying the meta!
                        meta.setLore(Arrays.asList(ChatColor.translateAlternateColorCodes('&', chatEvent.getMessage()).split("<n>")));
                    });
                });
                repository.updateServer(server);
                player.sendMessage("§a§lSUCESSO! §aA descrição do item foi atualizada com sucesso.");
                open(player, server);
            });
        }));

        contents.set(1, 4, ClickableItem.empty(server.getItem()));


        contents.set(1, 5, ClickableItem.of(new ItemStackBuilder(Material.ITEM_FRAME).withName("§aDefinir item com o da sua mão").withLore("§r", String.format("§aItem atual: §e%s", server.getItem().getType().toString())).buildStack(), e -> {
            if (player.getItemInHand() == null) {
                player.sendMessage("§c§lERRO! §cVocê não tem nenhum item em sua mão.");
                return;
            }
            val item = new ItemStackBuilder(player.getItemInHand().getType()).withName(server.getItem().getItemMeta().getDisplayName()).withLore(server.getItem().getItemMeta().getLore());
            server.setItem(item.buildStack());
            repository.updateServer(server);
            player.sendMessage("§a§lSUCESSO! §aO item foi definido com sucesso.");
        }));
        contents.set(1, 6, ClickableItem.of(new ItemStackBuilder().withName("§aHabilitar cabeça customizada").toSkullBuilder().withTexture("http://textures.minecraft.net/texture/4b599c618e914c25a37d69f541a22bebbf751615263756f2561fab4cfa39e").buildSkull(), e -> {
            player.sendMessage("§a§lSUCESSO! §aA cabeça customizada foi habilitada com sucesso.");
            server.setItem(new ItemStackBuilder(Material.STONE).withName(server.getItem().getItemMeta().getDisplayName()).withLore(server.getItem().getItemMeta().getLore()).toSkullBuilder().withTexture("http://textures.minecraft.net/texture/244393ed6e152bcc060ccad13e8e9c013a65e44c7b70264b5b2282efd2246ae3").buildSkull());
            repository.updateServer(server);
        }));
    }

    private void updateInventory(Player player, InventoryContents contents) {
        if (server.getItem().getType().toString().contains("SKULL")) {
            customSkullEditing(player, contents);
        } else {
            itemEditing(player, contents);
        }
    }


    @Override
    public void init(Player player, InventoryContents inventoryContents) {
        updateInventory(player, inventoryContents);
    }

    @Override
    public void update(Player player, InventoryContents inventoryContents) {
        updateInventory(player, inventoryContents);
    }
}
