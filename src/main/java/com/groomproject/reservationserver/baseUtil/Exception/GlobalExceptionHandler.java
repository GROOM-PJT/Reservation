package com.groomproject.reservationserver.baseUtil.Exception;

import com.groomproject.reservationserver.baseUtil.response.dto.CommonResponse;
import com.groomproject.reservationserver.baseUtil.response.service.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Author : Jeeseob
 * @CreateAt : 2022/10/05
 */

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private final ResponseService responseService;

    @ExceptionHandler(BusinessException.class)
    protected CommonResponse globalBusinessExceptionHandler(BusinessException e) {
        return responseService.failResult(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    protected CommonResponse globalExceptionHandler(Exception e) {
        return responseService.failResult(e.getMessage());
    }
}
