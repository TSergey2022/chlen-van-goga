package com.example.thirdlab;

import javafx.geometry.Bounds;

public abstract class PhysicsBody2D {

  public abstract void move(double scale, Bounds globalBounds);
  public abstract TransitData getTransit();

}
