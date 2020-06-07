package jpabook.jpashop_v2.study;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

/**
 * SpEL
 * https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#expressions-language-ref
 */
@SpringBootTest
public class SpELTest
{

    @Value("#{1+1}")
    int value;

    @Value("#{'hello '+'world'}")
    String strValue;

    @Value("#{1 eq 1}")
    boolean boolValue;

    @Value("${my.value}")
    int myValue;

    @Test
    public void f1()
    {
        System.out.println("value:"+value);
        System.out.println("strValue:"+strValue);
        System.out.println("boolValue:"+boolValue);
        System.out.println("myValue:"+myValue);

        ExpressionParser parser = new SpelExpressionParser();
        Expression expression = parser.parseExpression("2+100");
        Integer value = expression.getValue(Integer.class);
        System.out.println("expression Language:"+ value);
    }
}
