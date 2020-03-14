package com.company.interactives;

/**
 * Abstract class to make fields interactive.
 */
public abstract class Interactive extends Position {

    abstract void setVisibility(boolean visibility);
    abstract boolean isVisible();
}
