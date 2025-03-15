package com.talenttrack.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.talenttrack.entity.Role;
import com.talenttrack.entity.UserRole;
import com.talenttrack.repository.UserRoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@Primary
public class CustomUserAuthenticationService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserAuthenticationService.class);

    @Autowired
    private UserRoleRepository userRoleRepository;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Fetch user from database
        UserRole userRole = userRoleRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        // Log successful user retrieval
        logger.info("User found with username: {}", username);

        // Map roles to authorities
        Set<GrantedAuthority> authorities = mapRolesToAuthorities(userRole.getRoles());

        // Return Spring Security UserDetails object
        return new User(userRole.getUsername(), userRole.getPassword(), authorities);
    }

    private Set<GrantedAuthority> mapRolesToAuthorities(Set<Role> roles) {
        Set<GrantedAuthority> authorities = new HashSet<>();
        
        if (roles == null || roles.isEmpty()) {
            logger.warn("No roles found for user");
        }
        
        // Map each role to an authority with the prefix ROLE_
        roles.forEach(role -> authorities.add(new SimpleGrantedAuthority("ROLE_" + role.name())));
        
        return authorities;
    }
}
