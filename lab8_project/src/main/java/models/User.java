package models;

public abstract class User {
    protected String id;
    protected String name;
    protected String email;
    protected String password;
    protected String role;

    public User() {}
    public User(String id, String name, String email, String password, String role) {
        this.id=id; this.name=name; this.email=email; this.password=password; this.role=role;
    }
    public String getId(){ return id; }
    public String getName(){ return name; }
    public String getEmail(){ return email; }
    public String getPassword(){ return password; }
    public String getRole(){ return role; }
    public void setName(String n){ this.name=n; }
    public void setPassword(String p){ this.password=p; }
}
