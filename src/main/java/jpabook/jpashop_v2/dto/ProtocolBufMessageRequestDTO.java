package jpabook.jpashop_v2.dto;

import lombok.Data;

@Data
public class ProtocolBufMessageRequestDTO {

    private String type;

    private Object data;

    private Boolean isDebug = false;

}
