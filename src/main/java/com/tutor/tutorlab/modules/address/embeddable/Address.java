package com.tutor.tutorlab.modules.address.embeddable;

import lombok.*;

import javax.persistence.Embeddable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Address {

    private String state;
    private String siGunGu;
    private String dongMyunLi;

    @Override
    public String toString() {
        return state + " " + siGunGu + " " + dongMyunLi;
    }
}
