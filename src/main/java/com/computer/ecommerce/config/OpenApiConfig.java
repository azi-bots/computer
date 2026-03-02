package com.computer.ecommerce.config;

import com.computer.ecommerce.common.ResultCode;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme.In;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * OpenAPI/Swagger配置类
 */
@Configuration
@SecurityScheme(
    name = "JWT",
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "JWT",
    description = "JWT认证令牌，格式: Bearer {token}"
)
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("计算机电商平台 API")
                .description("""
                    计算机配件电商平台后端API文档

                    ## 功能模块
                    - 用户管理：用户注册、登录、信息管理
                    - 商品管理：商品分类、商品信息管理
                    - 购物车管理：商品添加、删除、修改数量
                    - 订单管理：订单创建、支付、发货、售后
                    - 库存管理：供应商库存管理
                    - 支付管理：支付接口集成

                    ## 认证方式
                    使用JWT认证，需要在请求头中添加：`Authorization: Bearer {token}`

                    ## 响应格式
                    所有接口返回统一格式：
                    ```json
                    {
                      "success": true,
                      "code": 200,
                      "message": "成功",
                      "data": {}
                    }
                    ```

                    ## 错误码
                    - 200: 成功
                    - 400: 请求参数错误
                    - 401: 未授权
                    - 403: 权限不足
                    - 404: 资源不存在
                    - 500: 服务器内部错误
                    """)
                .version("1.0.0")
                .contact(new Contact()
                    .name("技术支持")
                    .email("support@computer.com")
                    .url("https://www.computer.com"))
                .license(new License()
                    .name("Apache 2.0")
                    .url("https://www.apache.org/licenses/LICENSE-2.0.html")))
            .components(new Components()
                .addSecuritySchemes("JWT", new io.swagger.v3.oas.models.security.SecurityScheme()
                    .type(Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT")
                    .description("JWT认证令牌")))
            .addSecurityItem(new SecurityRequirement().addList("JWT"));
    }

    @Bean
    public OpenApiCustomizer openApiCustomizer() {
        return openApi -> {
            // 为所有操作添加默认响应
            openApi.getPaths().values().forEach(pathItem ->
                pathItem.readOperations().forEach(operation -> {
                    ApiResponses responses = operation.getResponses();
                    if (responses == null) {
                        responses = new ApiResponses();
                        operation.setResponses(responses);
                    }

                    // 添加200成功响应
                    if (!responses.containsKey("200")) {
                        responses.addApiResponse("200", new ApiResponse()
                            .description("成功")
                            .content(new Content().addMediaType("application/json",
                                new MediaType().schema(new Schema<Map<String, Object>>()
                                    .example(Map.of(
                                        "success", true,
                                        "code", 200,
                                        "message", "成功",
                                        "data", new HashMap<>()
                                    ))))));
                    }

                    // 添加400错误响应
                    if (!responses.containsKey("400")) {
                        responses.addApiResponse("400", new ApiResponse()
                            .description("请求参数错误")
                            .content(new Content().addMediaType("application/json",
                                new MediaType().schema(new Schema<Map<String, Object>>()
                                    .example(Map.of(
                                        "success", false,
                                        "code", 400,
                                        "message", "请求参数错误",
                                        "data", new HashMap<>()
                                    ))))));
                    }

                    // 添加401错误响应
                    if (!responses.containsKey("401")) {
                        responses.addApiResponse("401", new ApiResponse()
                            .description("未授权")
                            .content(new Content().addMediaType("application/json",
                                new MediaType().schema(new Schema<Map<String, Object>>()
                                    .example(Map.of(
                                        "success", false,
                                        "code", 401,
                                        "message", "未授权",
                                        "data", new HashMap<>()
                                    ))))));
                    }

                    // 添加403错误响应
                    if (!responses.containsKey("403")) {
                        responses.addApiResponse("403", new ApiResponse()
                            .description("权限不足")
                            .content(new Content().addMediaType("application/json",
                                new MediaType().schema(new Schema<Map<String, Object>>()
                                    .example(Map.of(
                                        "success", false,
                                        "code", 403,
                                        "message", "权限不足",
                                        "data", new HashMap<>()
                                    ))))));
                    }

                    // 添加500错误响应
                    if (!responses.containsKey("500")) {
                        responses.addApiResponse("500", new ApiResponse()
                            .description("服务器内部错误")
                            .content(new Content().addMediaType("application/json",
                                new MediaType().schema(new Schema<Map<String, Object>>()
                                    .example(Map.of(
                                        "success", false,
                                        "code", 500,
                                        "message", "服务器内部错误",
                                        "data", new HashMap<>()
                                    ))))));
                    }
                }));

            // 添加全局错误码说明
            openApi.getInfo().setDescription(openApi.getInfo().getDescription() + "\n\n" +
                "## 详细错误码说明\n" +
                Arrays.stream(ResultCode.values())
                    .filter(rc -> rc.getCode() >= 10000) // 只显示业务错误码
                    .map(rc -> String.format("- %d: %s", rc.getCode(), rc.getMessage()))
                    .reduce((a, b) -> a + "\n" + b)
                    .orElse("暂无业务错误码"));
        };
    }
}