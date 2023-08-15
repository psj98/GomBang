package com.ssafy.starRoomDeal.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class StarRoomDealId implements Serializable {

    private UUID memberId;

    private Long roomDealId;
}
