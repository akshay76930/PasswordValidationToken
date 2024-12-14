package com.talenttrack.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.talenttrack.entity.Role;
import com.talenttrack.entity.UserRole;
import com.talenttrack.repository.UserRoleRepository;

@Service
public class CustomUserAuthenticationService implements UserDetailsService {

	@Autowired
	private UserRoleRepository userRoleRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		UserRole userRole = userRoleRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));

		
		Set<GrantedAuthority> authorities = mapRolesToAuthorities(userRole.getRoles());

		return new org.springframework.security.core.userdetails.User(userRole.getUsername(), userRole.getPassword(),
				authorities);
	}

	private Set<GrantedAuthority> mapRolesToAuthorities(Set<Role> roles) {
		Set<GrantedAuthority> authorities = new HashSet<>();
		for (Role role : roles) {
			authorities.add(new SimpleGrantedAuthority(role.name())); 
		}
		return authorities;
	}
}
