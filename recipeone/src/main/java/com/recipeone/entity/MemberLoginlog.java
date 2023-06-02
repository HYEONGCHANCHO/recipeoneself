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
public class MemberLoginlog extends BaseEntity2 {

    @Id
    private String mid;
//    private String usersex;
//    private String userage;
    private long userlev;
    private LocalDateTime loginlog;


}
