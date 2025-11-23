package models;

public class Instructor extends User {
    public Instructor(){ this.role="Instructor"; }
    public Instructor(String id,String name,String email,String password){ super(id,name,email,password,"Instructor"); }
}
