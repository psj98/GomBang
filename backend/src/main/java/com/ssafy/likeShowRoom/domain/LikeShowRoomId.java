package com.ssafy.likeShowRoom.domain;

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
public class LikeShowRoomId implements Serializable {

    private UUID memberId; // 유저 id
    private Integer showRoomId; // 곰방봐 id
}
