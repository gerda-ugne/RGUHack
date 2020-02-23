package com.company.interactives;

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
