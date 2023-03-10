package ws.askin.files.model;

import ws.askin.files.enums.UserRole;

import javax.persistence.*;

@Entity
@Table(name = "users")
public class User {

    private @Id
    @GeneratedValue Long id;

    @Column(unique=true)
    private String userName;
    private String fullName;

    @Column(unique=true)
    private String email;
    private UserRole role;

    private boolean isDeleted;

    public User() {}

    public User(String userName, String fullName, String email, UserRole role) {
        this.userName = userName;
        this.fullName = fullName;
        this.email = email;
        this.role = role;
        this.isDeleted = false;
    }

    public User(String userName, String fullName, String email, UserRole role, boolean isDeleted) {
        this.userName = userName;
        this.fullName = fullName;
        this.email = email;
        this.role = role;
        this.isDeleted = isDeleted;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                ", isDeleted=" + isDeleted +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
