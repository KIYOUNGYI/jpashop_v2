package jpabook.jpashop_v2.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRawValue;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProtocolBufMessageDTO {

    private String type;

    private String json;

    private byte[] data;

    public void setJson(String json) {
        this.json = json;
    }

    @JsonRawValue
    public String getJson() {
        return json;
    }

}

