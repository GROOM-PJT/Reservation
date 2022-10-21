package com.groomproject.reservationserver.reservation.dto;

import lombok.*;

import java.time.LocalDateTime;

/**
 * @Author : Jeeseob
 * @CreateAt : 2022/10/18
 */

@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ReservationRequsetToSend {
    private Long restaurantId;
    // 유저정보를 body에 넣을지, jwt에 넣어서 파싱할지 선택해야함.
    private String username;

    // 인원
    private int numberOfReservations;

    // 예약 추가 요청 사항
    private String comment;

    // 예약 시간
    private LocalDateTime reservationTime;

    // 예약 신청 시간
    private LocalDateTime createAt;
}



