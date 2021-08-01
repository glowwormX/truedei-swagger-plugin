package com.truedei.swagger.plugin;/**
 * @Description:
 * @author xqw
 * @date 2021/7/29 23:34
 */

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import com.truedei.swagger.plugin.annotation.ApiVersion;
import com.truedei.swagger.plugin.annotation.EnableSwaggerPlugin;
import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
/**
 * @Description:
 * @author xqw
 * @date 2021/7/29 23:34
 */
@SpringBootApplication
@EnableSwaggerPlugin //开启自定义扩展的功能
public class TestApp {
    public static void main(String[] args) {
        SpringApplication.run(TestApp.class, args);
    }

}


/**
 * SwaggerConfig file
 */
@Configuration
@EnableSwagger2
@EnableKnife4j //开启使用第三方UI
@EnableSwaggerPlugin //开启自定义扩展的功能
class Swagger2Config {

    @Bean
    public Docket appApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .useDefaultResponseMessages(false) //不使用swagger自带的response的消息（404,500,200等）
                .groupName("API仓库") //分组名称
                .genericModelSubstitutes(ResponseEntity.class)
                .apiInfo(apiInfo()) //swagger基本信息
                .select()
                //第一种：扫描指定的包
//                .apis(RequestHandlerSelectors.basePackage("com.glodon.demo.mybatis")) //扫描的包
                //第二种：扫描只包含Swagger的注解，这种方式灵活
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .apis(input -> {
                    ApiVersion apiVersion = input.getHandlerMethod().getMethodAnnotation(ApiVersion.class);
                    if(apiVersion==null){
                        return true;
                    }
                    return false;
                })//controller路径
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * 配置Swagger信息
     * @return
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("API仓库接口文档")
                .description("该文档主要提供知识库后端的接口 \r\n\n"
                        + "请求服务:http//IP:Port/doc.html\r\n\n"
                        + "")
                .contact(new Contact("我们是TrueDei团队", "https://www.truedei.com/", "8042965@qq.com"))
                .version("0.0.1")
                .build();
    }

}

/**
 * @description:
 * @author: zhengh
 * @create: 2020-10-23 16:43
 **/
@Configuration
class SwaggerConfiguration extends WebMvcConfigurerAdapter {
    /**
     * 这个地方要重新注入一下资源文件，不然不会注入资源的，也没有注入requestHandlerMappping,相当于xml配置的
     *  <!--swagger资源配置-->
     *  <mvc:resources location="classpath:/META-INF/resources/" mapping="swagger-ui.html"/>
     *  <mvc:resources location="classpath:/META-INF/resources/webjars/" mapping="/webjars/**"/>
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars*")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

}
