package com.chen.stencil.controller;


import cn.hutool.core.net.multipart.UploadFile;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.chen.stencil.annotation.JwtIgnore;
import com.chen.stencil.common.response.Result;
import com.chen.stencil.common.response.ResultCode;
import com.chen.stencil.entity.Users;
import com.chen.stencil.pojo.Audience;
import com.chen.stencil.service.FileService;
import com.chen.stencil.service.impl.UsersServiceImpl;
import com.chen.stencil.utils.JwtTokenUtil;
import com.chen.stencil.utils.RedisUtils;
import com.chen.stencil.validator.RegisterValidator;
import com.chen.stencil.validator.UserLoginValidator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.fastjson.JSONObject;
import org.springframework.util.ResourceUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpServletRequest;


/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author chenweibo
 * @since 2020-06-17
 */
@RestController
@Api(value = "用户授权", tags = {"用户操作接口"})
@RequestMapping("/api")
public class UsersController {

    @Autowired
    private Audience audience;
    @Autowired
    RedisUtils redisUtils;
    @Autowired
    UsersServiceImpl usersService;
    @Autowired
    FileService fileService;

    @ApiOperation(value = "用户登录", tags = {""}, notes = "")
    @PostMapping("/login")
    @JwtIgnore
    public Result adminLogin(@Validated @RequestBody UserLoginValidator userloginValidator) {

        Users one = usersService.getOne(Wrappers.<Users>lambdaQuery().eq(Users::getEmail, userloginValidator.getEmail()), false);

        if (one == null) {
            return new Result(ResultCode.USER_NOT_EXIST);
        }
        if (!one.getPassword().equals(Base64.encodeBase64String(userloginValidator.getPassword().getBytes()))) {
            return new Result(ResultCode.USER_LOGIN_ERROR);
        }

        String token = JwtTokenUtil.createJWT(one.getId().toString(), userloginValidator.getEmail(), "user", audience);
        redisUtils.hset("user_token", one.getId().toString(), token);

        JSONObject result = new JSONObject();
        result.put("token", token);
        return Result.SUCCESS(result);

    }

    @ApiOperation(value = "用户注册", tags = {""}, notes = "")
    @JwtIgnore
    @RequestMapping(value = "/register", method = RequestMethod.POST, produces = "application/json")
    public Result register(@Validated @RequestBody RegisterValidator registerValidator) {

        String email = registerValidator.getEmail();

        Users one = usersService.getOne(Wrappers.<Users>lambdaQuery().eq(Users::getEmail, email), false);
        if (one == null) {
            Users users = new Users();
            users.setIsAdmin(0);
            users.setName(registerValidator.getName());
            users.setEmail(registerValidator.getEmail());
            users.setPassword(Base64.encodeBase64String(registerValidator.getPassword().getBytes()));
            boolean status = usersService.save(users);
            return Result.SUCCESS();

        }
        return new Result(ResultCode.USER_HAS_EXISTED);
    }

    @ApiOperation(value = "获取个人信息", tags = {""}, notes = "")
    @GetMapping("/me")
    public Result getUser(HttpServletRequest request) {
        Users users = usersService.getById(request.getAttribute("userId").toString());
        users.setPassword(null);
        return Result.SUCCESS(users);
    }

    @ApiOperation(value = "登出", tags = {""}, notes = "")
    @GetMapping("/loginOut")
    public Result loginOut(HttpServletRequest request) {

        String token = request.getAttribute("token").toString();
        String userId = request.getAttribute("userId").toString();
        String hToken = redisUtils.hget("user_token", userId);
        if (hToken.equals(token)) {
            redisUtils.hdel("user_token", request.getAttribute("userId").toString());
            return Result.SUCCESS();
        }

        return Result.SUCCESS();
    }

    @ApiOperation(value = "文件上传", tags = {""}, notes = "")
    @PostMapping("/upload")
    public Result upload(@RequestParam("file") MultipartFile file) throws java.io.IOException {

        return fileService.upload(file);
    }


}
