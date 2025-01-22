package com.aelithron.nonameadditions;

import org.bukkit.Location;

public class Nuke {
    private int strength;
    private int amount;
    private Location location;
    private boolean fake;

    public Nuke(int strength, int amount, Location location, boolean fake) {
        this.strength = strength;
        this.amount = amount;
        this.location = location;
        this.fake = fake;
    }

    public int getStrength() {
        if (fake) { return 0; }
        else { return strength; }
    }

    public int getAmount() {
        return amount;
    }

    public Location getLocation() {
        return location;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setFake(boolean fake) {
        this.fake = fake;
    }
}
