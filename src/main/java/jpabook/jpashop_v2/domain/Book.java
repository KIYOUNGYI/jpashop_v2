package jpabook.jpashop_v2.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("B")
public class Book extends Item
{
    private String author;
    private String isbn;
}
