package com.tutor.tutorlab.modules.address.vo;

import lombok.*;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Builder
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@NoArgsConstructor(access = PROTECTED)
@Entity
@Table(name = "address")
public class Address {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name = "state", length = 50)
    private String state;

    @Column(name = "si_gun", length = 50)
    private String siGun;

    @Column(name = "gu", length = 50)
    private String gu;

    @Column(name = "dong_myun_li", length = 50)
    private String dongMyunLi;
}
