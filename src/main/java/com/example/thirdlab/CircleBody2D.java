package com.example.thirdlab;

import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class CircleBody2D extends PhysicsBody2D {

  public Circle shape;
  private Point2D velocity;

  public CircleBody2D(Circle shape, Point2D velocity) {
    this.shape = shape;
    this.velocity = velocity == null ? Utils.getRandomVelocity() : velocity;
  }

  public TransitData getTransit() {
    TransitData data = new TransitData();
    data.points = new ArrayList<>(List.of(
        shape.getCenterX(), shape.getCenterY(),
        shape.getRadius(), shape.getRadius()
    ));
    data.velocity = new Double[] { velocity.getX(), velocity.getY() };
    data.fillColor = shape.getFill().toString();
    data.blendMode = shape.getBlendMode();
    return data;
  }

  @Override
  public void move(double scale, Bounds globalBounds) {
    Point2D delta = new Point2D(velocity.getX() * scale, velocity.getY() * scale);
    shape.setCenterX(shape.getCenterX() + delta.getX());
    shape.setCenterY(shape.getCenterY() + delta.getY());
    if (globalBounds != null) {
      Bounds bounds = shape.getBoundsInLocal();
      if (bounds.getMinX() <= globalBounds.getMinX()) {
        velocity = new Point2D(-velocity.getX(), velocity.getY());
        shape.setCenterX(globalBounds.getMinX() + Math.abs(bounds.getMinX() - globalBounds.getMinX()) + bounds.getWidth() / 2);
      } else if (bounds.getMaxX() >= globalBounds.getMaxX()) {
        velocity = new Point2D(-velocity.getX(), velocity.getY());
        shape.setCenterX(globalBounds.getMaxX() - Math.abs(bounds.getMaxX() - globalBounds.getMaxX()) - bounds.getWidth() / 2);
      }
      if (bounds.getMinY() <= globalBounds.getMinY()) {
        velocity = new Point2D(velocity.getX(), -velocity.getY());
        shape.setCenterY(globalBounds.getMinY() + Math.abs(bounds.getMinY() - globalBounds.getMinY()) + bounds.getWidth() / 2);
      } else if (bounds.getMaxY() >= globalBounds.getMaxY()) {
        velocity = new Point2D(velocity.getX(), -velocity.getY());
        shape.setCenterY(globalBounds.getMaxY() - Math.abs(bounds.getMaxY() - globalBounds.getMaxY()) - bounds.getWidth() / 2);
      }
    }
  }

  static public CircleBody2D fromTransit(TransitData data) {
    Double x = data.points.get(0); Double y = data.points.get(1);
    Double radius = data.points.get(2);
    Circle shape = new Circle();
    shape.setCenterX(x); shape.setCenterX(y); shape.setRadius(radius);
    shape.setFill(Paint.valueOf(data.fillColor));
    shape.setBlendMode(data.blendMode);
    return new CircleBody2D(shape, new Point2D(data.velocity[0], data.velocity[1]));
  }

}
