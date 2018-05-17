package com.eksamen.eksamen.Base;

public class Worker extends Staff{

  public Worker(){}

  @Override
  public int getStaffNiveau(){
    return super.getStaffNiveau()+17;
  }
}
