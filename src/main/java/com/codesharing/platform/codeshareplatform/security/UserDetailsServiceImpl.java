package com.codesharing.platform.codeshareplatform.security;

import com.codesharing.platform.codeshareplatform.exception.UserNotFoundException;
import com.codesharing.platform.codeshareplatform.model.Privilege;
import com.codesharing.platform.codeshareplatform.model.Role;
import com.codesharing.platform.codeshareplatform.model.Users;
import com.codesharing.platform.codeshareplatform.repository.RoleRepository;
import com.codesharing.platform.codeshareplatform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Autowired
    PasswordConfig passwordConfig;

    @Autowired
    RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Users> optionalUser = Optional.ofNullable(userService.findUserByEmail(email));
        System.out.println(email);
        System.out.println(email);
        System.out.println(email);

        if (optionalUser.isPresent()) {
            Users user = optionalUser.get();

            List<String> roleList = new ArrayList<>();
            for (Role role : user.getRoles()) {
                roleList.add(role.getRoleName());
            }


            UserDetails userDetails = User.builder()
                    .username(user.getUsername())
                    .password(passwordConfig.passwordEncoder().encode(user.getPassword()))
                    .disabled(user.isEnabled())
                    .accountExpired(user.isAccountNonExpired())
                    .accountLocked(user.isAccountNonLocked())
                    .credentialsExpired(user.isCredentialsNonExpired())
                    .roles(String.valueOf(Set.of("ROLE_USER")))
                    .build();
            return userDetails;
        } else {
            throw new UserNotFoundException(String.format("User of email: %s, not found!", email));
        }
    }

    private Collection<? extends GrantedAuthority> getAuthorities(Collection<Role> roles) {
        return getGrantedAuthorities(getPrivileges(roles));
    }

    private List<String> getPrivileges(Collection<Role> roles) {
        List<String> privileges = new ArrayList<>();
        List<Privilege> collection = new ArrayList<>();

        for (Role role : roles) {
            collection.addAll(role.getPrivileges());
        }
        for (Privilege item : collection) {
            privileges.add(item.getName());
        }
        return privileges;
    }

    private List<GrantedAuthority> getGrantedAuthorities(List<String> privileges) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String privilege : privileges) {
            authorities.add(new SimpleGrantedAuthority(privilege));
        }
        return authorities;
    }
}
