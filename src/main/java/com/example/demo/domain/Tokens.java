package com.example.demo.domain;

public class Tokens {

    private String id;
    private String name;
    private String access;
    private String create;
    private String expiration;

    private long createL;
    private long expirationL;

    private String token;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    public String getCreate() {
        return create;
    }

    public void setCreate(String create) {
        this.create = create;
    }

    public String getExpiration() {
        return expiration;
    }

    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }

    public long getCreateL() {
        return createL;
    }

    public void setCreateL(long createL) {
        this.createL = createL;
    }

    public long getExpirationL() {
        return expirationL;
    }

    public void setExpirationL(long expirationL) {
        this.expirationL = expirationL;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
