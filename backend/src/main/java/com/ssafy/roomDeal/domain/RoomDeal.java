package com.ssafy.roomDeal.domain;

import com.ssafy.member.domain.Member;
import com.ssafy.roomDeal.dto.RoomDealRegisterDefaultDto;
import com.ssafy.roomDeal.dto.RoomDealUpdateRequestDto;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.awt.*;
import java.io.Serializable;
import java.sql.Date;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomDeal implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "room_deal_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

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

//    @NotNull
//    private Point position;

    @NotNull
    private DealStatus dealStatus;

    private String thumbnail;

    private String station;

    private Double stationDistance;

    private String univ;

    private Double univDistance;

    private String content;

    @NotNull
    private Date registerTime;

    public RoomDeal(RoomDealRegisterDefaultDto roomDealRegisterDefaultDto, Member member) {
        this.member = member;
        this.roomType = roomDealRegisterDefaultDto.getRoomType();
        this.roomSize = roomDealRegisterDefaultDto.getRoomSize();
        this.roomCount = roomDealRegisterDefaultDto.getRoomCount();
        this.oneroomType = roomDealRegisterDefaultDto.getOneroomType();
        this.bathroomCount = roomDealRegisterDefaultDto.getBathroomCount();
        this.roadAddress = roomDealRegisterDefaultDto.getRoadAddress();
        this.jibunAddress = roomDealRegisterDefaultDto.getJibunAddress();
        this.monthlyFee = roomDealRegisterDefaultDto.getMonthlyFee();
        this.deposit = roomDealRegisterDefaultDto.getDeposit();
        this.managementFee = roomDealRegisterDefaultDto.getManagementFee();
        this.usageDate = roomDealRegisterDefaultDto.getUsageDate();
        this.moveInDate = roomDealRegisterDefaultDto.getMoveInDate();
        this.expirationDate = roomDealRegisterDefaultDto.getExpirationDate();
        this.floor = roomDealRegisterDefaultDto.getFloor();
        this.totalFloor = roomDealRegisterDefaultDto.getTotalFloor();
//        this.position = roomDealDefaultDto.getPosition();
        this.dealStatus = DealStatus.dealable;
        this.thumbnail = roomDealRegisterDefaultDto.getThumbnail();
        this.station = roomDealRegisterDefaultDto.getStation();
        this.stationDistance = roomDealRegisterDefaultDto.getStationDistance();
        this.univ = roomDealRegisterDefaultDto.getUniv();
        this.univDistance = roomDealRegisterDefaultDto.getUnivDistance();
        this.content = roomDealRegisterDefaultDto.getContent();
        this.registerTime = new Date(System.currentTimeMillis());

    }

    // 매물 수정
    public RoomDeal roomDealUpdate(RoomDealUpdateRequestDto roomDealUpdateRequestDto) {
        this.monthlyFee = roomDealUpdateRequestDto.getMonthlyFee();
        this.deposit = roomDealUpdateRequestDto.getDeposit();
        this.managementFee = roomDealUpdateRequestDto.getManagementFee();

        return this;
    }
}
