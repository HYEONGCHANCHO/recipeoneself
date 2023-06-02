package com.recipeone.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "PERSISTENT_LOGINS")
public class PersistentLogins {

    @Id
    private String series;
    private String username;
    private String token;
    private String lastUsed;

}