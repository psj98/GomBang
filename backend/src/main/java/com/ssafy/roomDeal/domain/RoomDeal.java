package com.ssafy.roomDeal.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.awt.*;
import java.sql.Date;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomDeal {

    @Id
    @Column(name = "room_deal_id")
    private UUID id;

    @NonNull
    private String roomType;

    @NonNull
    private Double roomSize;

    @NonNull
    private Integer roomCount;

    private String oneroomType;

    @NonNull
    private Integer bathroomCount;

    @NonNull
    private String address;

    @NonNull
    private String sido;

    @NonNull
    private String gugun;

    @NonNull
    private String dongEupMyonRi;

    @NonNull
    private Integer monthlyFee;

    @NonNull
    private Integer deposit;

    @NonNull
    private Integer managementFee;

    @NonNull
    private Date usageDate;

    @NonNull
    private Date moveInDate;

    @NonNull
    private Date expirationDate;

    @NonNull
    private Integer floor;

    @NonNull
    private Integer totalFloor;

    @NonNull
    private Point position;

    @NonNull
    private DealStatus dealStatus;

    private String thumbnail;

    private String station;

    private Double stationDistance;

    private String univ;

    private Double univDistance;

    private String content;

    @NonNull
    private Date regTime;

    // User -> Member로 변경시 FK로 가져올 것

}
