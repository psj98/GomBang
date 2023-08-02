package com.ssafy.member.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class Member {

    @Id
    @Column(name ="member_id")
    private UUID id;

    @NonNull
    private String name;

    @NonNull
    private String channelId;

    @NonNull
    private String nickname;

    @NonNull
    private String email;

    @NonNull
    private String gender;


}
