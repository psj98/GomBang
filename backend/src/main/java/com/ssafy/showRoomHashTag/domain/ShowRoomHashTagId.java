package com.ssafy.showRoomHashTag.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class ShowRoomHashTagId implements Serializable {

    private Integer showRoomId; // 곰방봐 id
    private Integer hashTagId; // 해시태그 id
}
