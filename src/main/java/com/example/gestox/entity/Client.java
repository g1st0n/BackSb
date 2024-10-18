package com.example.gestox.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idClient;

    private String fullName;
    private String clientType;
    private String email;
    private String address;
    private String telephone ;

    @OneToMany(mappedBy = "client")
    private List<CustomerOrder> orders;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
