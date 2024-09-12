package com.ssafy.showRoom.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShowRoomImageInfo {

    @Id
    @GeneratedValue
    private Integer id;

    private Integer showRoomId;

    private String fileUrl;

    public ShowRoomImageInfo(Integer showRoomId, String fileUrl) {
        this.showRoomId = showRoomId;
        this.fileUrl = fileUrl;
    }
}
