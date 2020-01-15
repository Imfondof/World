package com.imfondof.world.mvvm.bean;

/**
 * Imfondof on 2020/1/3 10:09
 * description:
 */
public class Account {
    private String name;
    private int level;

    public Account(String name, int level) {
        this.name = name;
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
