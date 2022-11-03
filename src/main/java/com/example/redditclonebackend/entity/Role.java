package com.example.redditclonebackend.entity;

import lombok.*;

import javax.persistence.*;

import java.util.HashSet;
import java.util.Set;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToMany(cascade = ALL, fetch = EAGER)
    @JoinTable(name = "role_authority",
        joinColumns = {
            @JoinColumn(name = "role_id", referencedColumnName = "id")
        },
        inverseJoinColumns = {
            @JoinColumn(name = "authority_id", referencedColumnName = "id")
        }
    )
    private Set<Authority> authorities = new HashSet<>();

    @ManyToMany(mappedBy = "roles")
    private Set<User> users = new HashSet<>();
}
