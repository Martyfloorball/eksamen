package com.eksamen.eksamen.Base;

public class Admin extends Staff{
  @Override
  public int getStaffNiveau() {
    return super.getStaffNiveau() + 15;
  }
}
