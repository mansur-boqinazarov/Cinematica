package uz.pdp.config.security;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import uz.pdp.daos.UserDao;
import uz.pdp.model.AuthUser;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserDao userDao;

    public CustomUserDetailsService(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<AuthUser> optional = userDao.getByUsername(username);
        AuthUser authUser = optional.orElseThrow(() -> new BadCredentialsException("Username or password incorrect"));
        String[] roles = authUser.getRole().split(","); // ADMIN,USER
        return User.withUsername(authUser.getUsername())
                .password(authUser.getPassword())
                .roles(roles)
                .build();
    }
}
