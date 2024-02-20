package io.github.ckroeger.mapping_test_utils.model.source;

public class ValueWithMaxPropery {

    private String value;
    private Number maxLength;

    public ValueWithMaxPropery() {
    }

    public ValueWithMaxPropery(String value, Number maxLength) {
        this.value = value;
        this.maxLength = maxLength;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Number getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(Number maxLength) {
        this.maxLength = maxLength;
    }
}