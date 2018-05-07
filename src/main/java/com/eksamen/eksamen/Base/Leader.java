package com.eksamen.eksamen.Base;

public class Leader extends Staff{
  @Override
  public int getStaffNiveau() {
    return super.getStaffNiveau() + 2;
  }
}
