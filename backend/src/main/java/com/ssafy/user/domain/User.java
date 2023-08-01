package com.ssafy.user.domain;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class User {

    @Id
    @Column(name ="user_id")
    private UUID id;

    @NonNull
    private String name;

    @NonNull
    private String channelId;

    @NonNull
    private String nickname;

    private String email;

    @NonNull
    private String gender;


}
