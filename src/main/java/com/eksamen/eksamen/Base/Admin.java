package com.eksamen.eksamen.Base;

public class Admin extends Staff{

  public Admin() { }

  @Override
  public int getStaffNiveau(){
    return super.getStaffNiveau()+15;
  }
}
