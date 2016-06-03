package com.app.raghavbabu.collabeditor.client.Objects;

/**
 * Created by raghavbabu on 4/24/16.
 */
public class Model {

    private String name;
    private boolean selected;

    public Model(String name) {
        this.name = name;
        selected = false;
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

    public void toggleChecked() {
        selected = !selected ;
    }

}
