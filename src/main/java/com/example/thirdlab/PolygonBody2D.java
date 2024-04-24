package com.example.thirdlab;

import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;

import java.util.ArrayList;
import java.util.List;

public class PolygonBody2D extends PhysicsBody2D {

  public Polygon shape;
  private Point2D velocity;

  public PolygonBody2D(Polygon shape, Point2D velocity) {
    this.shape = shape;
    this.velocity = velocity == null ? Utils.getRandomVelocity() : velocity;
  }

  public TransitData getTransit() {
    TransitData data = new TransitData();
    var bounds = shape.getBoundsInLocal();
    var poly2 = new Polygon();
    poly2.getPoints().addAll(shape.getPoints());
    Utils.movePolygon(poly2, new Point2D(-bounds.getMinX(), -bounds.getMinY()));
    data.points = new ArrayList<>(poly2.getPoints());
    data.velocity = new Double[] { velocity.getX(), velocity.getY() };
    data.fillColor = shape.getFill().toString();
    data.blendMode = shape.getBlendMode();
    return data;
  }

  @Override
  public void move(double scale, Bounds globalBounds) {
    Point2D delta = new Point2D(velocity.getX() * scale, velocity.getY() * scale);
    Utils.movePolygon(shape, delta);
    if (globalBounds != null) {
      Bounds bounds = shape.getBoundsInParent();
      if (bounds.getMinX() <= globalBounds.getMinX()) {
        velocity = new Point2D(-velocity.getX(), velocity.getY());
        delta = new Point2D(Math.abs(bounds.getMinX() - globalBounds.getMinX()), 0);
        Utils.movePolygon(shape, delta);
      } else if (bounds.getMaxX() >= globalBounds.getMaxX()) {
        velocity = new Point2D(-velocity.getX(), velocity.getY());
        delta = new Point2D(-Math.abs(bounds.getMaxX() - globalBounds.getMaxX()), 0);
        Utils.movePolygon(shape, delta);
      }
      if (bounds.getMinY() <= globalBounds.getMinY()) {
        velocity = new Point2D(velocity.getX(), -velocity.getY());
        delta = new Point2D(0, Math.abs(bounds.getMinY() - globalBounds.getMinY()));
        Utils.movePolygon(shape, delta);
      } else if (bounds.getMaxY() >= globalBounds.getMaxY()) {
        velocity = new Point2D(velocity.getX(), -velocity.getY());
        delta = new Point2D(0, -Math.abs(bounds.getMaxY() - globalBounds.getMaxY()));
        Utils.movePolygon(shape, delta);
      }
    }
  }

  static public PolygonBody2D fromTransit(TransitData data) {
    Polygon shape = new Polygon();
    shape.getPoints().addAll(data.points);
    shape.setFill(Paint.valueOf(data.fillColor));
    shape.setBlendMode(data.blendMode);
    return new PolygonBody2D(shape, new Point2D(data.velocity[0], data.velocity[1]));
  }

}
