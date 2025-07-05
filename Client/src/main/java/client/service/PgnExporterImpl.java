package client.service;

import client.interfaces.PgnExporter;
import shared.dtos.PgnContent;
import shared.dtos.SetStoredMatchIdRequest;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class PgnExporterImpl implements PgnExporter {
    private final ObjectInputStream in;
//    private final ObjectOutputStream out;

    public PgnExporterImpl(ObjectInputStream in, ObjectOutputStream out) {
        this.in = in;
//        this.out = out;
    }

    @Override
    public PgnContent export() {
//        SetStoredMatchIdRequest request = new SetStoredMatchIdRequest(gameId);


        Object object = in.getObjectInputFilter();
        if (object instanceof PgnContent) {
            return (PgnContent) object;
        }
        return null;
    }
}
