package me.staek.springsecuritypractice.account;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Account {

    @Id @GeneratedValue
    private Integer id;

    @Column(unique = true)
    private String username;

    private String password;

    private String role;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    // TODO {noop}"originalpassword"
    // TODO spring-security 는 encode 규칙을 주어야하는데, 아무것도 없을 경우 {noop} 로 작성한다,
    public void encodePassword() {
        this.password = "{noop}" + this.password;
    }
}
