package com.ssafy.starRoomDeal.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class StarRoomDealId implements Serializable {

    private UUID memberId;

    private Long roomDealId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StarRoomDealId that = (StarRoomDealId) o;
        return Objects.equals(memberId, that.memberId) &&
                Objects.equals(roomDealId, that.roomDealId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(memberId, roomDealId);
    }
}
