package com.ssafy.showRoom.domain;

import com.ssafy.member.domain.Member;
import com.ssafy.roomDeal.domain.DealStatus;
import com.ssafy.roomDeal.domain.RoomDeal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Date;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShowRoom {

    @Id
    @GeneratedValue
    @Column(name="show_room_id")
    private Integer id;

    @OneToOne
    @JoinColumn(name="room_deal_id")
    private RoomDeal roomDeal;

    @OneToOne
    @JoinColumn(name="member_id")
    private Member member;

    @NotNull
    private String roadAddress;

    @NotNull
    private String jibunAddress;

    private String thumbnail;

    private String station;

    private String univ;

    private Date registerTime;

    private DealStatus dealStatus;

    public ShowRoom saveThumbnail(String fileUrl) {
        this.thumbnail = fileUrl;

        return this;
    }

}
