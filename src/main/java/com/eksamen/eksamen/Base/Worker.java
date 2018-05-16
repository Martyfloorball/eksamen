package com.eksamen.eksamen.Base;

public class Worker extends Staff{
  @Override
  public int getStaffNiveau() {
    return super.getStaffNiveau() + 17;
  }
}
