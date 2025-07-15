package com.yatharth.finance_tracker.entity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name="users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true,nullable = false)
    private String username;
    @Column(unique = true,nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    List<Transaction> transactions;

}
