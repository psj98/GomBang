package com.ssafy.roomDeal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.awt.*;
import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomDealRegisterRequestDto {

    @NotNull
    private String roomType;

    @NotNull
    private Double roomSize;

    @NotNull
    private Integer roomCount;

    private String oneroomType;

    @NotNull
    private Integer bathroomCount;

    @NotNull
    private String roadAddress;

    @NotNull
    private String jibunAddress;

    @NotNull
    private Integer monthlyFee;

    @NotNull
    private Integer deposit;

    @NotNull
    private Integer managementFee;

    @NotNull
    private Date usageDate;

    @NotNull
    private Date moveInDate;

    @NotNull
    private Date expirationDate;

    @NotNull
    private Integer floor;

    @NotNull
    private Integer totalFloor;

    @NotNull
    private Point position;

    private String content;
}
