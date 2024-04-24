package com.example.thirdlab;

import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

import java.util.random.RandomGenerator;

public final class Utils {

  static public Point2D getRandomPoint(Pane root) {
    return new Point2D(
        RandomGenerator.getDefault().nextDouble(0, root.getWidth()),
        RandomGenerator.getDefault().nextDouble(0, root.getHeight())
    );
  }

  static public Rectangle getFig1() {
    Rectangle rect = new Rectangle();
    rect.setWidth(50);
    rect.setHeight(50);
    rect.setFill(Paint.valueOf(Color.AQUA.toString()));
    return rect;
  }

  static public Circle getFig2() {
    Circle circle = new Circle();
    circle.setRadius(25);
    circle.setFill(Paint.valueOf(Color.DARKORANGE.toString()));
    return circle;
  }

  static public Polygon getFig3() {
    Polygon tri = new Polygon();
    tri.getPoints().addAll(0.0, -30.0, -25.0, 20.0, 25.0, 20.0);
    tri.setFill(Paint.valueOf(Color.DARKOLIVEGREEN.toString()));
    return tri;
  }

  static public Point2D getRandomVelocity() {
    int random = RandomGenerator.getDefault().nextInt(0, 4);
    switch (random) {
      case 0: return new Point2D(100, 100);
      case 1: return new Point2D(100, -100);
      case 2: return new Point2D(-100, -100);
      case 3: return new Point2D(-100, 100);
      default: throw new RuntimeException();
    }
  }

  static public void movePolygon(Polygon polygon, Point2D velocity) {
    for (int i = 0; i < polygon.getPoints().size(); i += 2) {
      double newX = polygon.getPoints().get(i) + velocity.getX();
      double newY = polygon.getPoints().get(i + 1) + velocity.getY();
      polygon.getPoints().set(i, newX);
      polygon.getPoints().set(i + 1, newY);
    }
  }

}
