package it.simostefi.wedding.manager;

import com.google.gson.Gson;
import it.simostefi.wedding.service.datastore.DatastoreService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class Manager {

    protected Gson gson = new Gson();
    protected DatastoreService datastoreService;

    public Manager() {
        datastoreService = new DatastoreService();
    }

    public enum Status {
        OK,
        ERROR
    }
}
