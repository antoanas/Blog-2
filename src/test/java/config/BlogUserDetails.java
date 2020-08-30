package config;

import com.example.demo.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;


import java.util.ArrayList;
import java.util.Collection;

public abstract class BlogUserDetails extends User implements UserDetails{
    @Override
    public boolean isAccountNonExpired(){
        return true;
    }
    @Override
    public boolean isAccountNonLocked(){
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired(){
        return true;
    }
    @Override
    public boolean isEnabled(){
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        String userRoles = StringUtils.collectionToCommaDelimitedString(this.roles);
        return AuthorityUtils.commaSeparatedStringToAuthorityList(userRoles);
    }

    private final ArrayList<String> roles;
    private final User user;

    public BlogUserDetails(User user, ArrayList<String> roles){
        super(user.getEmail(), user.getFullName(), user.getPassword());

        this.roles = roles;
        this.user = user;
    }

    public User getUser(){
        return this.user;
    }
}