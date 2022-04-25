package com.edu.hanu.model;

import lombok.*;

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

import javax.persistence.*;
//import javax.validation.constraints.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user")
public class User implements Serializable {

//    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "email", nullable = false, unique = true)
//    @Email
//    @NotBlank
    private String email;

    @Column(name = "country", nullable = false)
//    @NotNull
    private String country;


    @Column(name = "phone", nullable = false)
//    @NotNull
    private String phone;


    @Column(name = "password", nullable = false)
//    @NotNull
//    @Min(5)
    private String password;


    @Column(name = "full_name", nullable = false)
//    @NotNull
//    @Size(min = 2, max = 30)
    private String fullname;

    @ManyToMany
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Collection<Ticket> ticket;

}

