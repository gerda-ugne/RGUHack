package com.company.map;

import com.company.interactives.Interactive;
import com.company.interactives.Position;

import java.util.Set;

public class Field extends Position {

    // Used for map generation
    private Set<Field> connected;
    private boolean isVisited;
    // End of map generation fields

    private boolean up;
    private boolean down;
    private boolean right;
    private boolean left;

    private Interactive interactive;

    public Field() {
        this(false, false, false, false, null);
    }

    public Field(boolean up, boolean down, boolean left, boolean right) {
        this(up, down, left, right, null);
    }

    public Field(boolean up, boolean down, boolean left, boolean right, Interactive interactive) {
        this.up = up;
        this.down = down;
        this.right = right;
        this.left = left;
        this.interactive = interactive;
    }

    public Interactive getInteractive() {
        return interactive;
    }

    public void setInteractive(Interactive interactive) {
        this.interactive = interactive;
    }

    public Set<Field> getConnected() {
        return connected;
    }

    public void setConnected(Set<Field> connected) {
        this.connected = connected;
    }

    public boolean isVisited() {
        return isVisited;
    }

    public void setVisited(boolean visited) {
        isVisited = visited;
    }

    public boolean canUp() {
        return up;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public boolean canDown() {
        return down;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

    public boolean canRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public boolean canLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }
}
