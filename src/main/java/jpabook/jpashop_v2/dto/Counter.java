package jpabook.jpashop_v2.dto;

import java.util.concurrent.atomic.AtomicLong;
import lombok.Data;

@Data
public class Counter {

  private final AtomicLong count;
  private final Long time;
}

