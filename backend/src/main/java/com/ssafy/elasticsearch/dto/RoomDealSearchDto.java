package com.ssafy.elasticsearch.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;
import org.springframework.data.geo.Point;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "recStation")
public class RoomDealSearchDto {

    @Id
    @Field(type = FieldType.Keyword)
    private Integer roomId;

    @Field(type = FieldType.Text)
    private String address;

    @GeoPointField
    private Point location;

}
