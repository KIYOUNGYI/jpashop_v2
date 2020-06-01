//package jpabook.jpashop_v2.config;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.PropertySource;
//import springfox.documentation.builders.ApiInfoBuilder;
//import springfox.documentation.builders.ParameterBuilder;
//import springfox.documentation.builders.PathSelectors;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.schema.ModelRef;
//import springfox.documentation.service.ApiInfo;
//import springfox.documentation.service.Parameter;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spring.web.plugins.Docket;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//@Configuration
//@EnableSwagger2
//@PropertySource(value={"classpatrh:${spring.profiles.active}/application.properties"})
//public class SwaggerConfig
//{
//    @Value("${server.port}")
//    private String port;
//
//    @Value("${spring.profile.value}")
//    private String profile;
//
//    @Bean
//    public Docket api() {
//
//        System.out.println(String.format("PROFILE : %s", profile));
//
//        HostEnum hostEnum = Arrays.stream(HostEnum.values()).filter(e -> e.getProfile().equals(profile)).findAny().get();
//
//        String host = hostEnum.getHost();
//        if (hostEnum.isHasPort()) {
//            host = String.format("%s:%s", host, port);
//        }
//
//        System.out.println(String.format("HOST : %s", host));
//
//        List<Parameter> opParameters = new ArrayList<>();
//
//        opParameters.add(new ParameterBuilder().name(HttpHeadersCustom.AUTHORIZATION)
//                .modelRef(new ModelRef("string"))
//                .parameterType("header")
//                .required(false)
//                .build());
//
//        opParameters.add(new ParameterBuilder().name(HttpHeadersCustom.GLOWPICK_UUID)
//                .modelRef(new ModelRef("string"))
//                .parameterType("header")
//                .required(false)
//                .build());
//
//        opParameters.add(new ParameterBuilder().name(HttpHeadersCustom.GLOWPICK_USERID)
//                .modelRef(new ModelRef("long"))
//                .parameterType("header")
//                .required(false)
//                .build());
//
//        opParameters.add(new ParameterBuilder().name(HttpHeadersCustom.GLOWPICK_REQUEST_KEY)
//                .modelRef(new ModelRef("string"))
//                .parameterType("header")
//                .required(false)
//                .build());
//
//        opParameters.add(new ParameterBuilder().name(HttpHeadersCustom.GLOWPICK_OS)
//                .modelRef(new ModelRef("string"))
//                .parameterType("header")
//                .required(false)
//                .build());
//
//        return new Docket(DocumentationType.SWAGGER_2)
//                .host(host)
//                .apiInfo(apiInfo())
//                .select()
//                .apis(RequestHandlerSelectors.basePackage("app.controller"))
//                .paths(PathSelectors.any())
//                .build()
//                .globalOperationParameters(opParameters);
//
//    }
//
//    private ApiInfo apiInfo() {
//        return new ApiInfoBuilder()
//                .title("글로우픽 서비스(Java) API")
//                .build();
//    }
//
//}
