package org.edward.turbosnail;

import org.edward.turbosnail.xml.model.Protocol;

import java.util.HashMap;

public class Papers extends HashMap<String, Protocol> {
    private static final int DEFAULT_SIZE = 10;

    public Papers() {
        super(DEFAULT_SIZE);
    }

    public Papers(int size) {
        super(size);
    }
}