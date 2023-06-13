package com.recipeone.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "MEMBERPAGELOG")
@SequenceGenerator(
        name = "MEMBERPAGELOG_SEQ_GENERATOR",
        sequenceName = "MEMBERPAGELOG_SEQ",
        initialValue = 1,
        allocationSize = 1
)

public class Memberpagelog {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "MEMBERPAGELOG_SEQ_GENERATOR")
    @Column(name = "no")
    private Long no;
    private String mid;
    private String page;
    private String duration;
    private Integer userlev;
    private String useryear;
    private String usergender;

}
