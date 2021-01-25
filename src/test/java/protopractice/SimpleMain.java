package protopractice;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import example.complex.Complex.ComplexMessage;
import example.complex.Complex.DummyMessage;
import example.enumerations.EnumExample.DayOfTheWeek;
import example.enumerations.EnumExample.EnumMessage;
import example.simple.Simple;
import example.simple.Simple.SimpleMessage;
import example.simple.Simple.SimpleMessage.Builder;
import java.util.Arrays;

public class SimpleMain {

  public static void main(String[] args) throws InvalidProtocolBufferException {

//    simpleSample();
//    enumSample();
//    complexSample();
//    conversionToJson();
    conversionToJson2();
  }

  private static void simpleSample() {
    Builder builder = SimpleMessage.newBuilder();

    //simple fields
    builder.setId(1)
        .setIsSimple(true)
        .setName("hehe").build();

    //repeated field
    builder.addSampleList(1).addSampleList(2).addSampleList(3).addAllSampleList(Arrays.asList(4, 5, 6)).build();

  }

  private static void enumSample() {
    EnumMessage.Builder builder1 = EnumMessage.newBuilder();

    builder1.setId(345);
    builder1.setDayOfTheWeek(DayOfTheWeek.FRIDAY);

    EnumMessage message = builder1.build();
    System.out.println("message => " + message);
  }

  private static void complexSample() {
    DummyMessage oneDummy = newDummyMessage(55, "one");
    ComplexMessage.Builder builder = ComplexMessage.newBuilder();

    builder.setOneDummy(oneDummy);

    builder.addMultipleDummy(newDummyMessage(66, "second"));
    builder.addMultipleDummy(newDummyMessage(67, "third"));
    builder.addMultipleDummy(newDummyMessage(68, "fourth"));

    builder.addAllMultipleDummy(Arrays.asList(
        newDummyMessage(69, "other dummy"),
        newDummyMessage(70, "second dummy")
    ));

    ComplexMessage message = builder.build();
    System.out.println("message => " + message);

  }

  private static void conversionToJson() throws InvalidProtocolBufferException {
    DummyMessage oneDummy = newDummyMessage(55, "one");
    ComplexMessage.Builder builder = ComplexMessage.newBuilder();

    builder.setOneDummy(oneDummy);

    builder.addMultipleDummy(newDummyMessage(66, "second"));
    builder.addMultipleDummy(newDummyMessage(67, "third"));
    builder.addMultipleDummy(newDummyMessage(68, "fourth"));

    builder.addAllMultipleDummy(Arrays.asList(
        newDummyMessage(69, "other dummy"),
        newDummyMessage(70, "second dummy")
    ));

    ComplexMessage message = builder.build();

    String jsonString = JsonFormat.printer().print(builder);
    System.out.println("jsonString = " + jsonString);


  }

  private static void conversionToJson2() throws InvalidProtocolBufferException {

    Builder builder = SimpleMessage.newBuilder();

    //simple fields
    builder.setId(1)
        .setIsSimple(true)
        .setName("hehe").build();

    //repeated field
    builder.addSampleList(1).addSampleList(2).addSampleList(3).addAllSampleList(Arrays.asList(4, 5, 6)).build();

    String jsonString = JsonFormat.printer()
        .print(builder);
    System.out.println("jsonString = " + jsonString);


    //parse JSON into protoBuf
    Simple.SimpleMessage.Builder builder2 = SimpleMessage.newBuilder();

    JsonFormat.parser().ignoringUnknownFields().merge(jsonString, builder2);

    System.out.println("builder2 = " + builder2);

  }

  private static DummyMessage newDummyMessage(Integer id, String name) {

    DummyMessage.Builder dummyMessageBuilder = DummyMessage.newBuilder();
    DummyMessage message = dummyMessageBuilder.setName(name).setId(id).build();
    return message;
  }
}
