package jpabook.jpashop_v2.domain;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Base64;
import lombok.Builder;
import test.Test;


public class WMember implements Serializable {

  private static final long serialVersionUID = 1117310806869718035L;

  private String name;
  private String email;
  private int age;


  @Builder
  public WMember(String name, String email, int age) {
    this.name = name;
    this.email = email;
    this.age = age;
  }

  @Override
  public String toString() {
    return "WMember{" +
        "name='" + name + '\'' +
        ", email='" + email + '\'' +
        ", age=" + age +
        '}';
  }

  public static void main(String[] args) throws IOException, ClassNotFoundException {

    WMember wMember = new WMember("김배민", "deliverykim@baemin.com", 25);
    byte[] serializedMember;
    try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
      try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
        oos.writeObject(wMember);
        // serializedMember -> 직렬화된 member 객체
        serializedMember = baos.toByteArray();
      }
    }
    // 바이트 배열로 생성된 직렬화 데이터를 base64로 변환
    String s = Base64.getEncoder().encodeToString(serializedMember);
    System.out.println("s = " + s);

    byte[] bytes = Base64.getDecoder().decode(s);
    try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes)) {
      try (ObjectInputStream ois = new ObjectInputStream(bais)) {
        // 역직렬화된 Member 객체를 읽어온다.
        Object objectMember = ois.readObject();
        WMember member = (WMember) objectMember;
        System.out.println(member);
      }
    }

    //데이터 직렬화 방법
    //[1] csv  (,로 구분)
    //[2] Json (오버헤드가 적어서 많이 선호)
    //[3] 프로토콜 버퍼

    Test.WMember wMember1 = Test.WMember.newBuilder().setAge(25).setEmail("g@gmail.com").setName("gg").build();
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    byte[] serializedWMember = baos.toByteArray();
    String s2 = Base64.getEncoder().encodeToString(serializedMember);
    System.out.println("s2 = " + s);

  }
}
