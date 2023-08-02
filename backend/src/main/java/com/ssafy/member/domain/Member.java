package com.ssafy.member.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Builder
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Member {

    @Id
    @Column(name ="member_id")
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
    private String gender;


}
