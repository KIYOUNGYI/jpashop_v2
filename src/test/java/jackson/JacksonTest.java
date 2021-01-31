package jackson;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

public class JacksonTest {

    static class Value {
        private String string;
        private String emptyString;
        private Object nullValue;
        private int number;
        private int zero;
        private List<String> list;
        private List<String> emptyList;
        private Date date;
        private Date zeroDate;
        private Optional<String> optional;
        private Optional<String> emptyOptional;

        public Value() {
            this.string = "민수";
            this.emptyString = "";
            this.nullValue = null;
            this.number = 100;
            this.zero = 0;
            this.list = asList("민수", "원우");
            this.emptyList = emptyList();
            date = new Date();
            zeroDate = new Date(0L);
            this.optional = Optional.of("민수");
            this.emptyOptional = Optional.empty();
        }
    }

    @Test
    public void always() throws JsonProcessingException {
        System.out.println(new Value());
        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
//        String result = objectMapper.writeValueAsString(new Value());
//
//        System.out.println(result);
    }


}