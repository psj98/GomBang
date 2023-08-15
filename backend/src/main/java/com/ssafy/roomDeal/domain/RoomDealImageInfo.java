package com.ssafy.roomDeal.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomDealImageInfo implements Serializable {

    @Id
    @GeneratedValue
    private Integer id;

    private Long roomDealId;

    private String fileUrl;

    public RoomDealImageInfo(Long roomDealId, String fileUrl) {
        this.roomDealId = roomDealId;
        this.fileUrl = fileUrl;
    }

}
