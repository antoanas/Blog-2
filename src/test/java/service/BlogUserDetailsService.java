package service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service("blogUserDetailsService")
public class BlogUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    public BlogUserDetailsService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = this.userRepository.findByEmail(email);

        if (user == null){
            throw new UsernameNotFoundException("Invalid User");
        }else {
            Set<GrantedAuthority> grantedAuthorities =
                    user.getRoles().stream().
                            map(role -> new SimpleGrantedAuthority(role.getName())).
                            collect(Collectors.toSet());
            return new org.springframework.security.core.userdetails.
                    User(user.getEmail(), user.getPassword(), grantedAuthorities);
        }
    }
}