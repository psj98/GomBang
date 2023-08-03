package com.ssafy.roomDeal.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QRoomDeal is a Querydsl query type for RoomDeal
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QRoomDeal extends EntityPathBase<RoomDeal> {

    private static final long serialVersionUID = 2019083793L;

    public static final QRoomDeal roomDeal = new QRoomDeal("roomDeal");

    public final StringPath address = createString("address");

    public final NumberPath<Integer> bathroomCount = createNumber("bathroomCount", Integer.class);

    public final StringPath content = createString("content");

    public final EnumPath<DealStatus> dealStatus = createEnum("dealStatus", DealStatus.class);

    public final NumberPath<Integer> deposit = createNumber("deposit", Integer.class);

    public final StringPath dongEupMyonRi = createString("dongEupMyonRi");

    public final DatePath<java.sql.Date> expirationDate = createDate("expirationDate", java.sql.Date.class);

    public final NumberPath<Integer> floor = createNumber("floor", Integer.class);

    public final StringPath gugun = createString("gugun");

    public final ComparablePath<java.util.UUID> id = createComparable("id", java.util.UUID.class);

    public final NumberPath<Integer> managementFee = createNumber("managementFee", Integer.class);

    public final NumberPath<Integer> monthlyFee = createNumber("monthlyFee", Integer.class);

    public final DatePath<java.sql.Date> moveInDate = createDate("moveInDate", java.sql.Date.class);

    public final StringPath oneroomType = createString("oneroomType");

    public final SimplePath<java.awt.Point> position = createSimple("position", java.awt.Point.class);

    public final DatePath<java.sql.Date> regTime = createDate("regTime", java.sql.Date.class);

    public final NumberPath<Integer> roomCount = createNumber("roomCount", Integer.class);

    public final NumberPath<Double> roomSize = createNumber("roomSize", Double.class);

    public final StringPath roomType = createString("roomType");

    public final StringPath sido = createString("sido");

    public final StringPath station = createString("station");

    public final NumberPath<Double> stationDistance = createNumber("stationDistance", Double.class);

    public final StringPath thumbnail = createString("thumbnail");

    public final NumberPath<Integer> totalFloor = createNumber("totalFloor", Integer.class);

    public final StringPath univ = createString("univ");

    public final NumberPath<Double> univDistance = createNumber("univDistance", Double.class);

    public final DatePath<java.sql.Date> usageDate = createDate("usageDate", java.sql.Date.class);

    public QRoomDeal(String variable) {
        super(RoomDeal.class, forVariable(variable));
    }

    public QRoomDeal(Path<? extends RoomDeal> path) {
        super(path.getType(), path.getMetadata());
    }

    public QRoomDeal(PathMetadata metadata) {
        super(RoomDeal.class, metadata);
    }

}

