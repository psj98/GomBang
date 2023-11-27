package com.ssafy.notification.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Getter
@Embeddable
@NoArgsConstructor
public class RelatedUrl {

    @NotNull
    private String url;

    public RelatedUrl(String url){
        this.url = url;
    }
}
