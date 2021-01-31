package jpabook.jpashop_v2.controller;

import common.Common;
import jpabook.jpashop_v2.dto.ProtocolBufMessageDTO;
import jpabook.jpashop_v2.dto.ProtocolBufMessageRequestDTO;
import jpabook.jpashop_v2.func.ProtoBufFunc;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ProtobufController {

    @PostMapping(value = "/test", consumes = "application/x-protobuf", produces = "application/json")
    public ResponseEntity<ProtocolBufMessageDTO> test(@RequestBody Common.Brand brand) {

        ProtocolBufMessageDTO protocolBufMessageDTO = new ProtocolBufMessageDTO();

        protocolBufMessageDTO.setType("test");

        return ResponseEntity.ok(protocolBufMessageDTO);

    }

    @GetMapping(value = "/test2")
    public ResponseEntity<ProtocolBufMessageDTO> test2(@RequestBody ProtocolBufMessageRequestDTO protoReq) {

        Common.Brand.Builder temp = Common.Brand.newBuilder();

        ProtoBufFunc.convertMessageBuilder(temp, protoReq);

        ProtocolBufMessageDTO protocolBufMessageDTO = new ProtocolBufMessageDTO();

        byte[] protobufBase64 = temp.build().toByteArray();

        protocolBufMessageDTO.setType("test");
        protocolBufMessageDTO.setData(protobufBase64);

        return ResponseEntity.ok(protocolBufMessageDTO);

    }
}
