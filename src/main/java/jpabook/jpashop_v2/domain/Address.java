package jpabook.jpashop_v2.domain;

import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
public class Address
{
    private String street;
    private String address;
    private String zipcode;

    protected Address()
    {
    }

    public Address(String street, String address, String zipcode)
    {
        this.street = street;
        this.address = address;
        this.zipcode = zipcode;
    }
}
