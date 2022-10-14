package com.groomproject.reservationserver.baseUtil.Exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @Author : Jeeseob
 * @CreateAt : 2022/10/11
 */

@Getter
@RequiredArgsConstructor
public enum ExMessage {
    EMAIL_ALREADY_EXIST("이미 존재하는 이메일 입니다")
    ;

    private final String message;
}
