package com.eksamen.eksamen.Base;

public class Leader extends Staff{

  public Leader(){}

  @Override
  public int getStaffNiveau(){
    return super.getStaffNiveau()+16;
  }
}
