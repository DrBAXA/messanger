package ua.bentleytek.messenger.entity;

import org.hibernate.validator.constraints.Email;
import ua.bentleytek.messenger.validator.Unique;
import ua.bentleytek.messenger.validator.UniqueField;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name="users")
public class User implements Serializable{

    @Id
    @GeneratedValue
    private int id;

    @Column
    @NotNull(message = "Поле логін не може бути пустим!")
    @Size(min = 4, message = "Логін повинен складатись не менш ніж з 4 символів!")
    @Pattern(regexp = "^(\\w){3,}$", message = "Логін повинен складатись з латинських літер!")
    @Unique(field = UniqueField.NAME, message = "Користувач з таким іменем вже зареєстрований")
    private String name;

    @Column
    @NotNull
    @Email(message = "Така адреса електронної пошти не існує!")
    @Unique(field = UniqueField.EMAIL, message = "Користувач з такою адресою електронної пошти вже зареєстрований")
    private String email;

    @Column
    @NotNull
    @Size(min = 6, message = "Пароль повинен містити не менше 6 символів!")
    private String password;

    @Column
    private String photo;

    @Column(name = "role")
    private String role;

    @Column(name="enabled")
    private int enabled;

    @Column
    private Timestamp lastCheck;

    @Transient
    private boolean online;

    @ManyToMany
    @JoinTable( name = "friends",
                joinColumns =  {@JoinColumn(name = "user_id", referencedColumnName = "id")},
                inverseJoinColumns = {@JoinColumn(name = "friend_id", referencedColumnName = "id")})
    private List<User> friends;

    public Timestamp getLastCheck() {
        return lastCheck;
    }

    public void setLastCheck(Timestamp lastCheck) {
        this.lastCheck = lastCheck;
    }

    public boolean isOnline() {
        return online;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public User() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public int getEnabled() {
        return enabled;
    }

    public void setEnabled(int enabled) {
        this.enabled = enabled;
    }

    public List<User> getFriends() {
        return friends;
    }

    public void setFriends(List<User> friends) {
        this.friends = friends;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
