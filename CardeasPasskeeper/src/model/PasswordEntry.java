package model;

public class PasswordEntry {
    private String service;
    private String username;
    private String password;

    public PasswordEntry(String service, String username, String password) {
        this.service = service;
        this.username = username;
        this.password = password;
    }

    public String getService() {
        return service;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return service + "," + username + "," + password;
    }
}
