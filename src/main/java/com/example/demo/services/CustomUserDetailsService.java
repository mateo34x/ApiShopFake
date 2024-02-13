/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.demo.services;

import com.example.demo.RoleRepository;
import com.example.demo.UserRepository;
import com.example.demo.domain.Role;
import com.example.demo.domain.Tokens;
import com.example.demo.domain.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public Users findUserByEmail(String email) {
        return userRepository.findItemByEmail(email);
    }

    public void saveUser(Users user, Tokens tokens) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setEnabled(false);
        Role userRole = roleRepository.findByRole("ADMIN");
        user.setRoles(new HashSet<>(Arrays.asList(userRole)));
        user.setActivo(false);
        user.setTokens(Arrays.asList(tokens));
        userRepository.save(user);
    }


    public void saveUserGoogle(Users user, Tokens tokens) {
        user.setEnabled(true);
        Role userRole = roleRepository.findByRole("ADMIN");
        user.setRoles(new HashSet<>(Arrays.asList(userRole)));
        user.setTokens(Arrays.asList(tokens));
        userRepository.save(user);
    }


    public void saveSesion(Users user) {
        userRepository.save(user);
    }




    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Users user = userRepository.findItemByEmail(email);
        if (user != null) {
            List<GrantedAuthority> authorities = getUserAuthority(user.getRoles());
            return buildUserForAuthentication(user, authorities);
        } else {
            throw new UsernameNotFoundException("username not found");
        }
    }


    public void addToken(Users user,Tokens tokens) {
        user.agregarToken(tokens);
        userRepository.save(user);
    }

    private List<GrantedAuthority> getUserAuthority(Set<Role> userRoles) {
        Set<GrantedAuthority> roles = new HashSet<>();
        userRoles.forEach((role) -> {
            roles.add(new SimpleGrantedAuthority(role.getRole()));
        });

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>(roles);
        return grantedAuthorities;
    }

    private UserDetails buildUserForAuthentication(Users user, List<GrantedAuthority> authorities) {
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
    }


    public List<Tokens> obtenerTodosLosTokens(String correo) {
        List<Tokens> todosLosTokens = new ArrayList<>();


        Users usuario = userRepository.findItemByEmail(correo);

            if (usuario.getTokens() != null) {
                todosLosTokens.addAll(usuario.getTokens());
            }




        return todosLosTokens;
    }


}
