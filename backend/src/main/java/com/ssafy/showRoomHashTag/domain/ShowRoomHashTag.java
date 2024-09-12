package com.ssafy.showRoomHashTag.domain;

import com.ssafy.hashTag.domain.HashTag;
import com.ssafy.showRoom.domain.ShowRoom;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShowRoomHashTag implements Serializable {

    // 복합키 매핑
    @EmbeddedId
    private ShowRoomHashTagId id;

    @MapsId("showRoomId")
    @ManyToOne
    @JoinColumn(name = "show_room_id")
    private ShowRoom showRoom; // 곰방봐

    @MapsId("hashTagId")
    @ManyToOne
    @JoinColumn(name = "hash_tag_id")
    private HashTag hashTag; // 해시태그
}
