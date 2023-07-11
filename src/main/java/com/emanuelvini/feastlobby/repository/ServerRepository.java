package com.emanuelvini.feastlobby.repository;


import com.emanuelvini.feastlobby.FeastLobby;
import com.emanuelvini.feastlobby.configuration.ErrorFixerValue;
import com.emanuelvini.feastlobby.model.Server;
import com.emanuelvini.feastlobby.repository.adapter.ServerAdapter;
import com.henryfabio.sqlprovider.executor.SQLExecutor;
import de.tr7zw.nbtapi.NBT;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ServerRepository {


    private final SQLExecutor executor;

    private final HashMap<String, Server> servers = new HashMap<>();

    private final FeastLobby plugin;

    public ServerRepository(FeastLobby plugin) {
        this.plugin = plugin;
        this.executor = new SQLExecutor(plugin.getMySQL());
        if (ErrorFixerValue.get(ErrorFixerValue::deleteTable)) {
            plugin.getCustomLogger().log("Desligando servidor para aplicar removação da tabela...", "e");
            plugin.getCustomLogger().log("§lDESATIVE ESSA OPÇÃO E LIGUE O SERVIDOR NOVAMENTE!", "c");
            executor.updateQuery("DROP TABLE servers");
            Bukkit.getServer().shutdown();
            return;
        }
        createTable();
        reloadServers();
    }

    @SneakyThrows
    public List<Server> getServers() {
        return new ArrayList<>(servers.values());
    }

    @SneakyThrows
    private void createTable() {
        executor.updateQuery("CREATE TABLE IF NOT EXISTS servers (id TEXT, name TEXT, bungee TEXT, address TEXT, item TEXT, maintenance BOOLEAN)");
    }

    @SneakyThrows
    private void reloadServers() {
        servers.clear();
        executor.resultManyQuery("SELECT * FROM servers", k -> {}, ServerAdapter.class).forEach(server -> {
            if (servers.containsKey(server.getId())) {
                plugin.getCustomLogger().log(String.format("Servidor duplicado encontrado!!! ID: §f%s§c NOME: §f%s", server.getId(), server.getName()), "c");
                executor.updateQuery("DELETE FROM servers WHERE id = ? AND bungee = ?", statement -> {
                    statement.set(1, server.getId());
                    statement.set(2, server.getBungee());
                });
                return;
            }
            servers.put(server.getId(), server);
            plugin.getCustomLogger().log(String.format("Servidor §f%s§a carregado com sucesso!", server.getName()), "a");
        });
    }

    @SneakyThrows
    public Server getServer(String id) {
        return servers.get(id);
    }


    @SneakyThrows
    public void saveServer(Server server) {
        executor.updateQuery("INSERT INTO servers VALUES(?,?,?,?,?,?)", statement -> {
            statement.set(1, server.getId());
            statement.set(2, server.getName());
            statement.set(3, server.getBungee());
            statement.set(4, server.getAddress());
            statement.set(5, NBT.itemStackToNBT(server.getItem()).toString());
            statement.set(6, server.isMaintenance());
        });
    }

    @SneakyThrows
    public void updateServer(Server server) {
        executor.updateQuery("DELETE FROM servers WHERE id = ?", statement -> {
            statement.set(1, server.getId());
        });
        saveServer(server);
    }


}
