package models;

public class User {
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
    public void setId(String s){ this.id=s; }
    public void setName(String s){ this.name=s; }
    public void setEmail(String s){ this.email=s; }
    public void setPassword(String s){ this.password=s; }
    public void setRole(String s){ this.role=s; }
}
