package jpabook.jpashop_v2.func;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.util.JsonFormat;
import com.querydsl.core.Tuple;
import jpabook.jpashop_v2.dto.ProtocolBufMessageDTO;
import jpabook.jpashop_v2.dto.ProtocolBufMessageRequestDTO;
import okio.ByteString;
import org.json.JSONObject;

import java.util.Map;

public class ProtoBufFunc {

    /*
     * Tuple to protoBuf convert
     * */
    public static ProtocolBufMessageDTO getTupleToProtoBuf(Tuple tuple, Message.Builder builder, Boolean isDebug, String type) {

        try {
            JSONObject parameters = new JSONObject(tuple);
            String json = parameters.toString();

            byte[] protobufBase64 = convertJsonToProto(builder, json).toByteArray();

            ProtocolBufMessageDTO resultProtoBuf = new ProtocolBufMessageDTO();
            resultProtoBuf.setType(type);
            resultProtoBuf.setData(protobufBase64);

            if (isDebug == true) {
                resultProtoBuf.setJson(json);
            }

            return resultProtoBuf;
        } catch (Exception e) {
            e.printStackTrace();

            return null;

        }
    }

    /*
     * map to protoBuf convert
     * */
    public static ProtocolBufMessageDTO getMapToProtoBuf(Map<String, Object> map, Message.Builder builder, Boolean isDebug, String type) {

        try {
            JSONObject parameters = new JSONObject(map);
            String json = parameters.toString();

            byte[] protobufBase64 = convertJsonToProto(builder, json).toByteArray();

            ProtocolBufMessageDTO resultProtoBuf = new ProtocolBufMessageDTO();
            resultProtoBuf.setType(type);
            resultProtoBuf.setData(protobufBase64);

            if (isDebug == true) {
                resultProtoBuf.setJson(json);
            }

            return resultProtoBuf;
        } catch (Exception e) {
            e.printStackTrace();

            return null;

        }
    }

    /*
     * DTO to protoBuf convert
     * */
    public static ProtocolBufMessageDTO getObjectToProtoBuf(Object obj, Message.Builder builder, Boolean isDebug, String type) {

        try {
            Gson gson = new Gson();

            String json = gson.toJson(obj);
            JsonObject parameters = gson.fromJson(json, JsonObject.class);
            json = parameters.toString();

            byte[] protobufBase64 = convertJsonToProto(builder, json).toByteArray();

            ProtocolBufMessageDTO resultProtoBuf = new ProtocolBufMessageDTO();
            resultProtoBuf.setType(type);
            resultProtoBuf.setData(protobufBase64);

            if (isDebug == true) {
                resultProtoBuf.setJson(json);
            }

            return resultProtoBuf;
        } catch (Exception e) {
            e.printStackTrace();

            return null;

        }

    }

    /*
     * Json to protoBuf convert
     * */
    public static Message convertJsonToProto(Message.Builder builder, String json) {
        try {
            JsonFormat.parser().merge(json, builder);
            return builder.build();
        } catch (InvalidProtocolBufferException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /*
     * Json to protoBuf convert
     * */
    public static Message convertJsonToProto(Message.Builder builder, Object data) {
        try {
            Gson gson = new Gson();
            String json = gson.toJson(data);
            JsonFormat.parser().merge(json, builder);
            return builder.build();
        } catch (InvalidProtocolBufferException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /*
     * 디버깅 모드: 기존 ProtoBuf 형식에 json 형식 추가하여 return
     * */
    public static ProtocolBufMessageDTO protoBufIsDebug(Message.Builder builder, ProtocolBufMessageDTO protocolBufMessageDTO) {

        try {

            String json = JsonFormat.printer().print(builder.build());

            Gson gson = new Gson();

            JsonObject parameters = gson.fromJson(json, JsonObject.class);
            json = parameters.toString();

            protocolBufMessageDTO.setJson(json);

        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }

        return protocolBufMessageDTO;
    }

    /**
     * 프로토버퍼 메시지 빌더 함수 추가
     *
     * @param builder
     * @param protoReq
     * @return
     */
    public static Message.Builder convertMessageBuilder(Message.Builder builder, ProtocolBufMessageRequestDTO protoReq) {

        try {


            //디버깅 모드이면
            if (protoReq.getIsDebug()) {

                ProtoBufFunc.convertJsonToProto(builder, protoReq.getData());

                return builder;

            } else {

                ByteString temp = ByteString.decodeBase64(String.valueOf(protoReq.getData()));
                byte[] byteArrays = temp.toByteArray();

                builder.mergeFrom(byteArrays);

                return builder;

            }

        } catch (Exception e) {

            e.printStackTrace();

            return null;

        }

    }
}
