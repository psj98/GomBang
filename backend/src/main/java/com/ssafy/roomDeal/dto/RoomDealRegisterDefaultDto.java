package com.ssafy.roomDeal.dto;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.util.List;
import java.util.UUID;

@Getter
public class RoomDealRegisterDefaultDto {

    @NotNull
    private UUID id;

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
    private String lat;

    @NotNull
    private String lon;

    private String station;

    private String univ;

    private String content;
}
