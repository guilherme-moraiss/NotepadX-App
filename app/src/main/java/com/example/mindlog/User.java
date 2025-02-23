package com.example.mindlog;

public class User {

    //region fields
    private String username;
    private String email;
    private String nationality;
    //endregion

    //region constructor
    public User(String username, String email, String nationality) {
        this.username = username;
        this.email = email;
        this.nationality = nationality;
    }
    //endregion

    //region getters

    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getNationality() { return nationality; }
    //endregion
}
