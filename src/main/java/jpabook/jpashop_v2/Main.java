package jpabook.jpashop_v2;

//import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;

import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Main {

  public static void main(String[] args) {
    SpringApplication app = new SpringApplication(Main.class);
    app.run(args);
  }

  //v1 할 때 필요한 녀석, jsonIgnore 만으로 해결안됨.
  //레이지로 세팅된거 싹 다 쿼리 찔러서 가져오게끔 하는 것이라 보면 된다.
  @Bean
  Hibernate5Module hibernate5Module() {
    Hibernate5Module hibernate5Module = new Hibernate5Module();
//    hibernate5Module.configure(Hibernate5Module.Feature.FORCE_LAZY_LOADING, true); // 이런거 자체를 사실은 쓰면 안되고, v1 관련해서 엔티티 자체를 노출하면 안되요. 방법이 있긴 함,  for loop 돌면서 강제로 뭐좀 해주면 되긴 함.
    return hibernate5Module;
  }

}
