package jpabook.jpashop_v2.controller;

import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import sample.Common.Person;

@RestController
public class ProtoController {

  @GetMapping("/person")
  public String getPerson(){

    Person person = Person.newBuilder().setAge(20).setName("뭐야이게도대체").build();

    return person.toString();
  }
}
