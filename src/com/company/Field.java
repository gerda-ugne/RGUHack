package com.company;

import com.company.interactives.Interactive;

public class Field {

    public final boolean up;
    public final boolean down;
    public final boolean right;
    public final boolean left;

    private Interactive interactive;

    public Field(boolean up, boolean down, boolean left, boolean right) {
        this.up = up;
        this.down = down;
        this.right = right;
        this.left = left;
    }

    public Field(boolean up, boolean down, boolean right, boolean left, Interactive interactive) {
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
}
