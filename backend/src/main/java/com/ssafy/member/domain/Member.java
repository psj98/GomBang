package com.ssafy.member.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.UUID;

@Builder
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Member implements Serializable {

    @Id
    @Column(name ="member_id", columnDefinition = "BINARY(16)")
    private UUID id;

    @Builder.Default
    @NotNull
    private String name = "사용자";

    @NotNull
    private String channelId;

    @NotNull
    private String nickname;

    @NotNull
    private String email;

    @NotNull
    @Enumerated(EnumType.ORDINAL)
    private Gender gender;

}
