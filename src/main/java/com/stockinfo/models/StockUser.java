package com.stockinfo.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
public class StockUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long id;

    @Column(unique = true, updatable = false)
    @Size(max = 30)
    private String username;

    @Column(unique = true)
    @Size(max = 60)
    @Email
    private String email;

    @Size(max = 120)
    private String password;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_companies_subscribed",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "company_symbol"))
    private Set<Company> companies = new HashSet<>();

    public StockUser(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StockUser stockUser = (StockUser) o;
        return id.equals(stockUser.id) &&
                username.equals(stockUser.username) &&
                email.equals(stockUser.email) &&
                password.equals(stockUser.password) &&
                Objects.equals(roles, stockUser.roles) &&
                Objects.equals(companies, stockUser.companies);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, email, password);
    }
}
