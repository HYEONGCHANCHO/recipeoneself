package com.recipeone.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "MEMBERLOGINLOG")
@SequenceGenerator(
        name = "MEMBERLOGINLOG_SEQ_GENERATOR",
        sequenceName = "MEMBERLOGINLOG_SEQ",
        initialValue = 1,
        allocationSize = 1
)

public class MemberLoginlog {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "MEMBERLOGINLOG_SEQ_GENERATOR")
    @Column(name = "id")
    private Long id;
    private String mid;
//    private String usersex;
//    private String userage;
//    private long userlev;
    private LocalDateTime loginlog;





}
