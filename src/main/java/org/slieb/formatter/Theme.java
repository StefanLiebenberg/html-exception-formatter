package org.slieb.formatter;

public enum Theme {

    EMPTY("empty"),
    GRAY("gray");

    private final String name;

    Theme(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
