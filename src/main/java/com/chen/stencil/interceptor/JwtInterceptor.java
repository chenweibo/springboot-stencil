package com.chen.stencil.interceptor;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.chen.stencil.annotation.JwtIgnore;
import com.chen.stencil.common.exception.CustomException;
import com.chen.stencil.common.response.ResultCode;
import com.chen.stencil.pojo.Audience;
import com.chen.stencil.utils.JwtTokenUtil;
import com.chen.stencil.utils.RedisUtils;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class JwtInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private Audience audience;
    @Autowired
    RedisUtils redisUtils;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 忽略带JwtIgnore注解的请求, 不做后续token认证校验
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            JwtIgnore jwtIgnore = handlerMethod.getMethodAnnotation(JwtIgnore.class);
            if (jwtIgnore != null) {
                return true;
            }
        }

        if (HttpMethod.OPTIONS.equals(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return true;
        }

        // 获取请求头信息authorization信息
        final String authHeader = request.getHeader(JwtTokenUtil.AUTH_HEADER_KEY);
        //log.info("## authHeader= {}", authHeader);

        if (StringUtils.isBlank(authHeader) || !authHeader.startsWith(JwtTokenUtil.TOKEN_PREFIX)) {
            //log.info("### 用户未登录，请先登录 ###");
            throw new CustomException(ResultCode.USER_NOT_LOGGED_IN);
        }

        // 获取token
        final String token = authHeader.substring(7);

        if (audience == null) {
            BeanFactory factory = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
            audience = (Audience) factory.getBean("audience");
        }


        // 验证token是否有效--无效已做异常抛出，由全局异常处理后返回对应信息
        Claims claims = JwtTokenUtil.parseJWT(token, audience.getBase64Secret());
        request.setAttribute("userid", claims);

        String userId = claims.get("userId", String.class);
        request.setAttribute("userId", userId);
        String hToken = redisUtils.hget("user_token", userId);

        if (!hToken.equals(token)) {
            throw new CustomException(ResultCode.USER_LOGIN_ELSEWHERE);
        }

        return true;
    }

}
