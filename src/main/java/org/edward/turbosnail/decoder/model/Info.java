package org.edward.turbosnail.decoder.model;

import java.util.HashMap;

public class Info extends HashMap<String, Object> {
    private static final int DEFAULT_SIZE = 10;

    public Info() {
        super(DEFAULT_SIZE);
    }

    public Info(int size) {
        super(size);
    }
}