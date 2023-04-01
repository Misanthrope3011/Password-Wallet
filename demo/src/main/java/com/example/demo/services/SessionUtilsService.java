package com.example.demo.services;

import com.example.demo.config.Roles;
import com.example.demo.exceptions.ExceptionHandler;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.management.relation.Role;
import java.util.ArrayList;
import java.util.List;


public class SessionUtilsService {

	public static void updateUserPermsToRO() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		List<GrantedAuthority> updatedAuthorities = new ArrayList<>();
		updatedAuthorities.add(new SimpleGrantedAuthority(Roles.USER_RO.name()));
		updateSecurityContext(auth, updatedAuthorities);
	}

	public static void updateUserPermsToRW() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		List<GrantedAuthority> updatedAuthorities = new ArrayList<>();
		updatedAuthorities.add(new SimpleGrantedAuthority(Roles.USER_RO.name()));
		updatedAuthorities.add(new SimpleGrantedAuthority(Roles.USER_RW.name()));
		updateSecurityContext(auth, updatedAuthorities);
	}

	private static void updateSecurityContext(Authentication auth, List<GrantedAuthority> updatedAuthorities) {
		Authentication newAuth = new UsernamePasswordAuthenticationToken(auth.getPrincipal(), auth.getCredentials(), updatedAuthorities);
		SecurityContextHolder.getContext().setAuthentication(newAuth);
	}

	public static String getSessionUserName() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return authentication.getName();
	}

}
