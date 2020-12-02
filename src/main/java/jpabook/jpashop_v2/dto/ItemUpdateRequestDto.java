package jpabook.jpashop_v2.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemUpdateRequestDto {

  private String name;
  private Integer price;
  private Integer stockQuantity;
}
