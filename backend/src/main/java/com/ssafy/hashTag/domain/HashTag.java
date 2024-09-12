package com.ssafy.hashTag.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HashTag {

    @Id
    @GeneratedValue
    @Column(name = "hash_tag_id")
    private Integer id;

    @NotNull
    private String hashTagName;
}
