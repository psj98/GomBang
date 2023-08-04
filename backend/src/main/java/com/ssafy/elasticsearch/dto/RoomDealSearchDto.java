package com.ssafy.elasticsearch.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.GeoPointField;
import org.springframework.data.geo.Point;

@Getter
@NoArgsConstructor
@Document(indexName = "recStation")
public class RoomDealSearchDto {

    @Id
    @Field(type = FieldType.Keyword)
    private Integer roomId;

    @Field(type = FieldType.Text)
    private String address;

    @GeoPointField
    private Point location;

    @Builder
    public RoomDealSearchDto(Integer roomId, String address, Point location) {
        this.roomId = roomId;
        this.address = address;
        this.location = location;
    }

    @Override
    public String toString() {
        return "RoomDealSearchDto{" +
                "roomId=" + roomId +
                ", address='" + address + '\'' +
                ", location=" + location +
                '}';
    }
}
