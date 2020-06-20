package com.chen.stencil.common.exception.handler;

import com.chen.stencil.common.exception.CustomException;
import com.chen.stencil.common.response.Result;
import com.chen.stencil.common.response.ResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.PoolException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 处理自定义异常
     */
    @ExceptionHandler(CustomException.class)
    public Result handleException(CustomException e) {
        // 打印异常信息
       // log.error("### 异常信息:{} ###", e.getMessage());
        return new Result(e.getResultCode());
    }

    /**
     * 参数错误异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result parameterExceptionHandler(MethodArgumentNotValidException e) {

        // 获取异常信息
        BindingResult exceptions = e.getBindingResult();
        // 判断异常中是否有错误信息，如果存在就使用异常中的消息，否则使用默认消息
        if (exceptions.hasErrors()) {
            List<ObjectError> errors = exceptions.getAllErrors();
            if (!errors.isEmpty()) {
                // 这里列出了全部错误参数，按正常逻辑，只需要第一条错误即可
                FieldError fieldError = (FieldError) errors.get(0);
                return Result.ValidFAIL(fieldError.getDefaultMessage());
            }
        }
        return new Result(ResultCode.PARAM_IS_INVALID);
    }

    /**
     * 处理所有不可知的异常
     */
    @ExceptionHandler(Exception.class)
    public Result handleOtherException(Exception e) {
        //打印异常堆栈信息
        e.printStackTrace();
        // 打印异常信息
        log.error("### 不可知的异常:{} ###", e.getMessage());
        return new Result(ResultCode.SYSTEM_INNER_ERROR);
    }

    /**
     * redis 连接异常
     */
    @ExceptionHandler(PoolException.class)
    public Result handleRedisConnectException(Exception e) {
        //打印异常堆栈信息
        e.printStackTrace();
        // 打印异常信息
        //log.error("### 不可知的异常:{} ###", e.getMessage());
        return new Result(ResultCode.SYSTEM_REDIS_CONNECT);
    }

    /**
     * Required request body is missing
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result HttpMessageNotReadableException(Exception e) {
        //打印异常堆栈信息
        // e.printStackTrace();
        // 打印异常信息
        //log.error("### 不可知的异常:{} ###", e.getMessage());
        return new Result(ResultCode.PARAM_NOT_COMPLETE);
    }

    /**
     * Required request body is missing
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Result HttpRequestMethodNotSupportedException(Exception e) {
        //打印异常堆栈信息
        // e.printStackTrace();
        // 打印异常信息
        //log.error("### 不可知的异常:{} ###", e.getMessage());
        return new Result(ResultCode.METHOD_NOT_SUPPORT);
    }

}
