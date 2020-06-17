package com.chen.stencil.controller;


import com.chen.stencil.annotation.JwtIgnore;
import com.chen.stencil.common.response.Result;
import com.chen.stencil.pojo.Audience;
import com.chen.stencil.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author chenweibo
 * @since 2020-06-17
 */
@RestController
@RequestMapping("/api")
public class UsersController {

    @Autowired
    private Audience audience;

    @PostMapping("/login")
    @JwtIgnore
    public Result adminLogin(HttpServletResponse response, String username, String password) {
        // 这里模拟测试, 默认登录成功，返回用户ID和角色信息
        String userId = UUID.randomUUID().toString();
        String role = "admin";

        // 创建token
        String token = JwtTokenUtil.createJWT(userId, username, role, audience);
        //log.info("### 登录成功, token={} ###", token);

        // 将token放在响应头
        response.setHeader(JwtTokenUtil.AUTH_HEADER_KEY, JwtTokenUtil.TOKEN_PREFIX + token);
        // 将token响应给客户端
        JSONObject result = new JSONObject();
        result.put("token", token);
        return Result.SUCCESS(result);

    }

    @GetMapping("/me")
    public Result getUser() {
        //log.info("### 查询所有用户列表 ###");

        return Result.SUCCESS();
    }
}
