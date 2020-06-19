package com.chen.stencil.validator;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
public class UserLoginValidator {

    @NotEmpty(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;

    @NotEmpty(message = "密码不能为空")
    @Length(min = 6, max = 18, message = "密码长度为6-18")
    private String password;
}
