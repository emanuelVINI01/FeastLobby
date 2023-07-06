package com.emanuelvini.feastlobby.repository.adapter;

import com.emanuelvini.feastlobby.model.Server;
import com.henryfabio.sqlprovider.executor.adapter.SQLResultAdapter;
import com.henryfabio.sqlprovider.executor.result.SimpleResultSet;
import de.tr7zw.nbtapi.NBT;

public class ServerAdapter implements SQLResultAdapter<Server> {
    @Override
    public Server adaptResult(SimpleResultSet simpleResultSet) {
        return new Server(
                simpleResultSet.get("id"),
                simpleResultSet.get("name"),
                simpleResultSet.get("bungee"),
                simpleResultSet.get("address"),
                NBT.itemStackFromNBT(NBT.parseNBT(simpleResultSet.get("item"))),
                simpleResultSet.get("maintenance")
        );
    }
}
