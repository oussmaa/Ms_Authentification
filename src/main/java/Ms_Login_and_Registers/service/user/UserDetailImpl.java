package Ms_Login_and_Registers.service.user;

import Ms_Login_and_Registers.models.Permissions;
import Ms_Login_and_Registers.models.Roles;
import Ms_Login_and_Registers.models.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Objects;

public class UserDetailImpl implements UserDetails {
    private static final long serialVersionUID = 1L;


    private Long id;

    private String username;

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Long getThemeid() {
        return themeid;
    }

    public void setThemeid(Long themeid) {
        this.themeid = themeid;
    }



    private String email;

    @JsonIgnore
    private String password;

    private String name;

    private Collection<? extends GrantedAuthority> authorities;
    private boolean locked;
    private String phone;
    private Long themeid;
    private String userrole;
    private Permissions permissions;
    public UserDetailImpl(Long id, String username, String email, String password, String name, boolean locked, String phone, Long themeid) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.name = name;
        this.locked = locked;
        this.phone = phone;
        this.themeid = themeid;
        this.userrole = userrole;

    }

    public static UserDetailImpl build(User user) {


        return new UserDetailImpl(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                user.getName(),
                user.isLocked(),
                user.getPhone(),
                user.getThemeid());


    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserDetailImpl user = (UserDetailImpl) o;
        return Objects.equals(id, user.id);
    }
}
