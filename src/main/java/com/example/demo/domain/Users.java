/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.demo.domain;

import com.example.demo.domain.Role;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Document(collection = "user")
public class Users {

    @Id
    private String id;
    @Indexed(unique = true, direction = IndexDirection.DESCENDING)
    private String email;
    private String password;
    private String fullname;

    private boolean Google;
    @DBRef
    private Set<Role> roles;

    private List<Tokens> tokens;





    public void agregarToken(Tokens nuevoToken) {
        if (tokens == null) {
            tokens = new ArrayList<>();
        }
        tokens.add(nuevoToken);

    }

    public Tokens obtenerUltimoToken() {
        if (tokens != null && !tokens.isEmpty()) {
            return tokens.get(tokens.size() - 1);
        } else {
            return null; // Manejar el caso donde no hay tokens
        }
    }




    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Tokens> getTokens() {
        return tokens;
    }

    public void setTokens(List<Tokens> tokens) {
        this.tokens = tokens;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public boolean isEnabled() {
        return Google;
    }

    public void setEnabled(boolean google) {
        this.Google = google;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
    
    
    
}
