package org.github.testutils.model.source;

public class ValueProperty {
    private String value;

    public ValueProperty() {
    }

    public ValueProperty(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}