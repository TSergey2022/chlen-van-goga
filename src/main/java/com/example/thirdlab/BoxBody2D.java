package com.example.thirdlab;

import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BoxBody2D extends PhysicsBody2D implements Serializable {

  private static final long serialVersionUID = -123123123L;

  public Rectangle shape;
  public Point2D velocity;

  public BoxBody2D(Rectangle shape, Point2D velocity) {
    this.shape = shape;
    this.velocity = velocity == null ? Utils.getRandomVelocity() : velocity;
  }

  public TransitData getTransit() {
    TransitData data = new TransitData();
    data.points = new ArrayList<>(List.of(
        shape.getX(), shape.getY(),
        shape.getWidth(), shape.getHeight()
    ));
    data.velocity = new Double[] { velocity.getX(), velocity.getY() };
    data.fillColor = shape.getFill().toString();
    data.blendMode = shape.getBlendMode();
    return data;
  }

  @Override
  public void move(double scale, Bounds globalBounds) {
    Point2D delta = new Point2D(velocity.getX() * scale, velocity.getY() * scale);
    shape.setX(shape.getX() + delta.getX());
    shape.setY(shape.getY() + delta.getY());
    if (globalBounds != null) {
      Bounds bounds = shape.getBoundsInLocal();
      if (bounds.getMinX() <= globalBounds.getMinX()) {
        velocity = new Point2D(-velocity.getX(), velocity.getY());
        shape.setX(globalBounds.getMinX() + Math.abs(bounds.getMinX() - globalBounds.getMinX()));
      } else if (bounds.getMaxX() >= globalBounds.getMaxX()) {
        velocity = new Point2D(-velocity.getX(), velocity.getY());
        shape.setX(globalBounds.getMaxX() - Math.abs(bounds.getMaxX() - globalBounds.getMaxX()) - bounds.getWidth());
      }
      if (bounds.getMinY() <= globalBounds.getMinY()) {
        velocity = new Point2D(velocity.getX(), -velocity.getY());
        shape.setY(globalBounds.getMinY() + Math.abs(bounds.getMinY() - globalBounds.getMinY()));
      } else if (bounds.getMaxY() >= globalBounds.getMaxY()) {
        velocity = new Point2D(velocity.getX(), -velocity.getY());
        shape.setY(globalBounds.getMaxY() - Math.abs(bounds.getMaxY() - globalBounds.getMaxY()) - bounds.getHeight());
      }
    }
  }

  static public BoxBody2D fromTransit(TransitData data) {
    Double x = data.points.get(0); Double y = data.points.get(1);
    Double width = data.points.get(2); Double height = data.points.get(3);
    Rectangle shape = new Rectangle();
    shape.setX(x); shape.setY(y); shape.setWidth(width); shape.setHeight(height);
    shape.setFill(Paint.valueOf(data.fillColor));
    shape.setBlendMode(data.blendMode);
    return new BoxBody2D(shape, new Point2D(data.velocity[0], data.velocity[1]));
  }

}
