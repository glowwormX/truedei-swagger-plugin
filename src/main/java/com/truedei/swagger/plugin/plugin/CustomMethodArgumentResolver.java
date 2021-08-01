package com.truedei.swagger.plugin.plugin;

import com.truedei.swagger.plugin.annotation.CustomParam;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.Assert;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.AbstractMessageConverterMethodArgumentResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CustomMethodArgumentResolver extends AbstractMessageConverterMethodArgumentResolver {
    private static final Logger log = LoggerFactory.getLogger(OperationPositionBulderPlugin.class);

    private static final String POST = "post";
    private static final String APPLICATION_JSON = "application/json";

    public CustomMethodArgumentResolver(List<HttpMessageConverter<?>> converters) {
        super(converters);
    }

    public CustomMethodArgumentResolver(List<HttpMessageConverter<?>> converters, List<Object> requestResponseBodyAdvice) {
        super(converters, requestResponseBodyAdvice);
    }

    /**
     * 判断是否需要处理该参数
     *
     * @param parameter the method parameter to check
     * @return {@code true} if this resolver supports the supplied parameter;
     * {@code false} otherwise
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        // 只处理带有@CustomParam注解的参数
        return parameter.hasParameterAnnotation(CustomParam.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        String contentType = Objects.requireNonNull(servletRequest).getContentType();

        if (contentType == null || !contentType.contains(APPLICATION_JSON)) {
            log.error("解析参数异常，contentType需为{}", APPLICATION_JSON);
            throw new RuntimeException("解析参数异常，contentType需为application/json");
        }

        if (!POST.equalsIgnoreCase(servletRequest.getMethod())) {
            log.error("解析参数异常，请求类型必须为post");
            throw new RuntimeException("解析参数异常，请求类型必须为post");
        }
        return bindRequestParams(parameter, webRequest);
    }

    private Object bindRequestParams(MethodParameter parameter, NativeWebRequest webRequest) throws Exception {
        CustomParam customParam = parameter.getParameterAnnotation(CustomParam.class);
        String name = StringUtils.isBlank(customParam.value()) ? parameter.getParameterName() : customParam.value();
        Map arg = (Map) readWithMessageConverters(webRequest, parameter, Map.class);
        return arg.get(name);
    }
//    private Object bindRequestParams(MethodParameter parameter, HttpServletRequest servletRequest) {
//        CustomParam customParam = parameter.getParameterAnnotation(CustomParam.class);
//
//        Class<?> parameterType = parameter.getParameterType();
//        String requestBody = getRequestBody(servletRequest);
//        Map<String, Object> params = ObjectMapperUtil.str2Obj(requestBody, new TypeReference<Map<String, Object>>() {
//        });
//
//        params = MapUtils.isEmpty(params) ? new HashMap<>(0) : params;
//        String name = StringUtils.isBlank(customParam.value()) ? parameter.getParameterName() : customParam.value();
//        Object value = params.get(name);
//
//        if (parameterType.equals(String.class)) {
//            if (StringUtils.isBlank((String) value)) {
//                log.error("参数解析异常,String类型参数不能为空");
//                throw new RuntimeException("参数解析异常,String类型参数不能为空");
//            }
//        }
//
//        if (customParam.required()) {
//            if (value == null) {
//                log.error("参数解析异常,require=true,值不能为空");
//                throw new RuntimeException("参数解析异常,require=true,值不能为空");
//            }
//        } else {
//            if (customParam.defaultValue().equals(ValueConstants.DEFAULT_NONE)) {
//                log.error("参数解析异常,require=false,必须指定默认值");
//                throw new RuntimeException("参数解析异常,require=false,必须指定默认值");
//            }
//            if (value == null) {
//                value = customParam.defaultValue();
//            }
//        }
//
//        return ConvertUtils.convert(value, parameterType);
//    }

    /**
     * 获取请求body
     *
     * @param servletRequest request
     * @return 请求body
     */
    private String getRequestBody(HttpServletRequest servletRequest) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            BufferedReader reader = servletRequest.getReader();
            char[] buf = new char[1024];
            int length;
            while ((length = reader.read(buf)) != -1) {
                stringBuilder.append(buf, 0, length);
            }
        } catch (IOException e) {
            log.error("读取流异常", e);
            throw new RuntimeException("读取流异常");
        }
        return stringBuilder.toString();
    }

    protected <T> Object readWithMessageConverters(NativeWebRequest webRequest, MethodParameter parameter,
                                                   Type paramType) throws IOException, HttpMediaTypeNotSupportedException, HttpMessageNotReadableException {

        HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        Assert.state(servletRequest != null, "No HttpServletRequest");
        ServletServerHttpRequest inputMessage = new ServletServerHttpRequest(servletRequest);

        Object arg = readWithMessageConverters(inputMessage, parameter, paramType);
        if (arg == null && checkRequired(parameter)) {
            throw new HttpMessageNotReadableException("Required request body is missing: " +
                    parameter.getExecutable().toGenericString(), inputMessage);
        }
        return arg;
    }

    protected boolean checkRequired(MethodParameter parameter) {
        RequestBody requestBody = parameter.getParameterAnnotation(RequestBody.class);
        return (requestBody != null && requestBody.required() && !parameter.isOptional());
    }
}