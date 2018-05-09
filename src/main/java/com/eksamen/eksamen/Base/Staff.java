package com.eksamen.eksamen.Base;

public class Staff {
  private int staffId;
  private String firstName;
  private String lastName;
  private String email;
  private String password; //skal nok fjernes
  private int phonenumber;
  private int staffNiveau = 0;

  public Staff() { }

  public Staff(int staffId, String firstName, String lastName, String email, String password, int phonenumber, int staffNiveau) {
    this.staffId = staffId;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.password = password;
    this.phonenumber = phonenumber;
    this.staffNiveau = staffNiveau;
  }

  public Staff(String firstName, String lastName, String password, int phonenumber, String email, int staffNiveau) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.password = password;
    this.phonenumber = phonenumber;
    this.staffNiveau = staffNiveau;
  }

  @Override
  public String toString() {
    return "Staff{" +
        "staffId=" + staffId +
        ", firstName='" + firstName + '\'' +
        ", lastName='" + lastName + '\'' +
        ", email='" + email + '\'' +
        ", password='" + password + '\'' +
        ", phonenumber=" + phonenumber +
        ", staffNiveau=" + staffNiveau +
        '}';
  }

  public int getStaffId() {
    return staffId;
  }

  public void setStaffId(int staffId) {
    this.staffId = staffId;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public int getPhonenumber() {
    return phonenumber;
  }

  public void setPhonenumber(int phonenumber) {
    this.phonenumber = phonenumber;
  }

  public int getStaffNiveau() {
    return staffNiveau;
  }

  public void setStaffNiveau(int staffNiveau) {
    this.staffNiveau = staffNiveau;
  }
}
