package io.github.ckroeger.mapping_test_utils.model.source;

public class SourceAddress {

    private ValueProperty id;
    private ValueWithMaxPropery street;
    private ValueWithMaxPropery houseNumber;
    private ValueWithMaxPropery city;
    private ValueWithMaxPropery postalCode;
    private ValueWithMaxPropery country;
    private ValueWithMaxPropery poBox;

    public SourceAddress() {
    }

    public SourceAddress(ValueProperty id, ValueWithMaxPropery street, ValueWithMaxPropery houseNumber, ValueWithMaxPropery city, ValueWithMaxPropery postalCode, ValueWithMaxPropery country,
                         ValueWithMaxPropery poBox) {
        this.id = id;
        this.street = street;
        this.houseNumber = houseNumber;
        this.city = city;
        this.postalCode = postalCode;
        this.country = country;
        this.poBox = poBox;
    }

    public ValueProperty getId() {
        return id;
    }

    public void setId(ValueProperty id) {
        this.id = id;
    }

    public ValueWithMaxPropery getStreet() {
        return street;
    }

    public void setStreet(ValueWithMaxPropery street) {
        this.street = street;
    }

    public ValueWithMaxPropery getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(ValueWithMaxPropery houseNumber) {
        this.houseNumber = houseNumber;
    }

    public ValueWithMaxPropery getCity() {
        return city;
    }

    public void setCity(ValueWithMaxPropery city) {
        this.city = city;
    }

    public ValueWithMaxPropery getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(ValueWithMaxPropery postalCode) {
        this.postalCode = postalCode;
    }

    public ValueWithMaxPropery getCountry() {
        return country;
    }

    public void setCountry(ValueWithMaxPropery country) {
        this.country = country;
    }

    public ValueWithMaxPropery getPoBox() {
        return poBox;
    }

    public void setPoBox(ValueWithMaxPropery poBox) {
        this.poBox = poBox;
    }

}
