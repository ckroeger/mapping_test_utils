package org.github.testutils.model.target;


import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class TargetAddress {

    @XmlElement(name = "Street")
    String street;

    @XmlElement(name = "HouseNumber")
    String houseNumber;

    @XmlElement(name = "PoBox")
    String poBox;

    @XmlElement(name = "City")
    String city;

    @XmlElement(name = "ZipCode")
    String zipCode;

    @XmlElement(name = "Country")
    Country country;

    @XmlElement(name = "AddressType")
    AddressType addressType;

    @XmlElement(name = "AdditionalInformation")
    String additionalInformation;

    @XmlElement(name = "Province")
    Province province;

    public TargetAddress() {
    }

    public TargetAddress(String street, String houseNumber, String poBox, String city, String zipCode, Country country, AddressType addressType, String additionalInformation,
                          Province province) {
        this.street = street;
        this.houseNumber = houseNumber;
        this.poBox = poBox;
        this.city = city;
        this.zipCode = zipCode;
        this.country = country;
        this.addressType = addressType;
        this.additionalInformation = additionalInformation;
        this.province = province;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }


    public String getPoBox() {
        return poBox;
    }

    public void setPoBox(String poBox) {
        this.poBox = poBox;
    }


    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public AddressType getAddressType() {
        return addressType;
    }

    public void setAddressType(AddressType addressType) {
        this.addressType = addressType;
    }

    public String getAdditionalInformation() {
        return additionalInformation;
    }

    public void setAdditionalInformation(String additionalInformation) {
        this.additionalInformation = additionalInformation;
    }

    public Province getProvince() {
        return province;
    }

    public void setProvince(Province province) {
        this.province = province;
    }
}
