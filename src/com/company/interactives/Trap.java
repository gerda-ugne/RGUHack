package com.company.interactives;

/**
 * Trap class represents the
 * traps you can find in game.
 */
public class Trap extends Interactive {

    private boolean visible;

    @Override
    public void setVisibility(boolean visibility) {
        this.visible = visibility;
    }

    @Override
    public boolean isVisible() {
        return visible;
    }
}
