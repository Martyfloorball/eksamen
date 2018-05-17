package com.eksamen.eksamen.Base;

public class Admin extends Staff implements inta{

  public Admin() { }

  @Override
  public void setStaffNiveau(){
    super.setStaffNiveau(15);
  }
}
