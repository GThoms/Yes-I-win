package com.jhua.assassin;

/**
 * Created by Marcos on 5/9/2015.
 */
public class Friend {
    String name = null;
    boolean selected = false;

    public Friend(String name, boolean selected) {
        this.name = name;
        this.selected = selected;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return selected;
    }
    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
