package com.tutor.tutorlab.modules.address.vo;

import lombok.*;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Getter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor(access = PROTECTED)
@Entity
@Table(name = "address")
public class Address {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(length = 50)
    private String state;

    @Column(length = 50)
    private String siGun;

    @Column(length = 50)
    private String gu;

    @Column(length = 50)
    private String dongMyunLi;

    @Builder(access = PRIVATE)
    public Address(String state, String siGun, String gu, String dongMyunLi) {
        this.state = state;
        this.siGun = siGun;
        this.gu = gu;
        this.dongMyunLi = dongMyunLi;
    }

    public static Address of(String state, String siGun, String gu, String dongMyunLi) {
        return Address.builder()
                .state(state)
                .siGun(siGun)
                .gu(gu)
                .dongMyunLi(dongMyunLi)
                .build();
    }
}
