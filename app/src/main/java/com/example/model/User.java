package com.example.model;

public class User {
    private String id;
    private String name;
    private String collegeName;
    private String studentIdNumber;
    private String email;
    private String department;
    private double rating;
    private int ridesCompleted;
    private boolean isVerified;
    private String avatarUrl;

    public User(String id, String name, String collegeName, String studentIdNumber, 
                String email, String department, double rating, int ridesCompleted, boolean isVerified) {
        this.id = id;
        this.name = name;
        this.collegeName = collegeName;
        this.studentIdNumber = studentIdNumber;
        this.email = email;
        this.department = department;
        this.rating = rating;
        this.ridesCompleted = ridesCompleted;
        this.isVerified = isVerified;
    }

    // Default constructor (unverified, requiring student ID login)
    public User() {
        this.id = "";
        this.name = "";
        this.collegeName = "";
        this.studentIdNumber = "";
        this.email = "";
        this.department = "";
        this.rating = 5.0;
        this.ridesCompleted = 0;
        this.isVerified = false;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getCollegeName() { return collegeName; }
    public String getStudentIdNumber() { return studentIdNumber; }
    public String getEmail() { return email; }
    public String getDepartment() { return department; }
    public double getRating() { return rating; }
    public int getRidesCompleted() { return ridesCompleted; }
    public boolean isVerified() { return isVerified; }

    public void setName(String name) { this.name = name; }
    public void setCollegeName(String collegeName) { this.collegeName = collegeName; }
    public void setStudentIdNumber(String studentIdNumber) { this.studentIdNumber = studentIdNumber; }
    public void setEmail(String email) { this.email = email; }
    public void setDepartment(String department) { this.department = department; }
    public void setVerified(boolean verified) { isVerified = verified; }
}
