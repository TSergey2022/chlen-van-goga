package com.example.thirdlab;

import javafx.geometry.Point2D;
import javafx.scene.effect.BlendMode;

import java.io.Serializable;
import java.util.ArrayList;

public class TransitData implements Serializable {

  public ArrayList<Double> points;
  public Double[] velocity;
  public String fillColor;
  public BlendMode blendMode;

}
