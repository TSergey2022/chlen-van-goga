package com.example.thirdlab;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.effect.BlendMode;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;

public class MyApplication extends Application {

  private final DataFormat dfBb2d = new DataFormat("java/BoxBody2D");
  private final DataFormat dfCb2d = new DataFormat("java/CircleBody2D");
  private final DataFormat dfPb2d = new DataFormat("java/PolygonBody2D");

  final private ArrayList<PhysicsBody2D> bodies = new ArrayList<>();
  final private ArrayList<PhysicsBody2D> staticBodies = new ArrayList<>();

  @Override
  public void start(Stage stage) {
    openFirstWindow();
    openSecondWindow();
  }

  private void openFirstWindow() {
    Stage stage = new Stage();
    stage.setOnCloseRequest(event -> Platform.exit());

    AnchorPane root = new AnchorPane();
    HBox hBox = new HBox();
    hBox.setSpacing(10);
    hBox.setPadding(new Insets(20));
    Button quadButton = new Button("", Utils.getFig1());
    quadButton.setOnAction(event -> {
      addQuad(root, bodies, null, null);
    });
    Button circleButton = new Button("", Utils.getFig2());
    circleButton.setOnAction(event -> {
      addCircle(root, bodies, null, null);
    });
    Button starButton = new Button("", Utils.getFig3());
    starButton.setOnAction(event -> {
      addPolygon(root, bodies, null, null);
    });

    hBox.getChildren().addAll(quadButton, circleButton, starButton);
    root.getChildren().add(hBox);

    Scene scene = new Scene(root, 1, 1);

    scene.setOnDragOver(event -> {
      Dragboard db = event.getDragboard();
      if (db.hasContent(dfBb2d) || db.hasContent(dfCb2d) || db.hasContent(dfPb2d)) {
        event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
      }
      event.consume();
    });
    scene.setOnDragDropped(event -> {
      Dragboard db = event.getDragboard();
      boolean success = false;
      if (db.hasContent(dfBb2d)) {
        success = true;
        var body = BoxBody2D.fromTransit((TransitData) db.getContent(dfBb2d));
        body.shape.setX(event.getSceneX());
        body.shape.setY(event.getSceneY());
        addQuad(root, bodies, body.shape, body);
      } else if (db.hasContent(dfCb2d)) {
        success = true;
        var body = CircleBody2D.fromTransit((TransitData) db.getContent(dfCb2d));
        body.shape.setCenterX(event.getSceneX());
        body.shape.setCenterY(event.getSceneY());
        addCircle(root, bodies, body.shape, body);
      } else if (db.hasContent(dfPb2d)) {
        success = true;
        var body = PolygonBody2D.fromTransit((TransitData) db.getContent(dfPb2d));
        Utils.movePolygon(body.shape, new Point2D(
            body.shape.getBoundsInLocal().getMinX(),
            body.shape.getBoundsInLocal().getMinY()
        ));
        Utils.movePolygon(body.shape, new Point2D(
            event.getSceneX(),
            event.getSceneY()
        ));
        addPolygon(root, bodies, body.shape, body);
      }
      event.setDropCompleted(success);
    });

    stage.setResizable(false);
    stage.setMinWidth(600);
    stage.setMinHeight(500);
    stage.setScene(scene);
    stage.setTitle("First Window");
    stage.show();

    double deltaTime = 1.0 / 48.0;
    Timeline timer = new Timeline(new KeyFrame(
        Duration.seconds(deltaTime),
        event -> bodies.forEach((body) -> body.move(0.5 * deltaTime, root.getBoundsInLocal()))
    ));
    timer.setCycleCount(Timeline.INDEFINITE);
    timer.play();
  }

  private void openSecondWindow() {
    Stage stage = new Stage();
    stage.setOnCloseRequest(event -> Platform.exit());

    BorderPane root = new BorderPane();

    Scene scene = new Scene(root, 100, 100);
    scene.setOnDragOver(event -> {
      Dragboard db = event.getDragboard();
      if (db.hasContent(dfBb2d) || db.hasContent(dfCb2d) || db.hasContent(dfPb2d)) {
        event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
      }
      event.consume();
    });
    scene.setOnDragDropped(event -> {
      Dragboard db = event.getDragboard();
      boolean success = false;
      if (db.hasContent(dfBb2d)) {
        success = true;
        var body = BoxBody2D.fromTransit((TransitData) db.getContent(dfBb2d));
        body.shape.setX(event.getSceneX());
        body.shape.setY(event.getSceneY());
        addQuad(root, staticBodies, body.shape, body);
      } else if (db.hasContent(dfCb2d)) {
        success = true;
        var body = CircleBody2D.fromTransit((TransitData) db.getContent(dfCb2d));
        body.shape.setCenterX(event.getSceneX());
        body.shape.setCenterY(event.getSceneY());
        addCircle(root, staticBodies, body.shape, body);
      } else if (db.hasContent(dfPb2d)) {
        success = true;
        var body = PolygonBody2D.fromTransit((TransitData) db.getContent(dfPb2d));
        Utils.movePolygon(body.shape, new Point2D(
            event.getSceneX(),
            event.getSceneY()
        ));
        addPolygon(root, staticBodies, body.shape, body);
      }
      event.setDropCompleted(success);
    });
    stage.setScene(scene);
    stage.setResizable(false);
    stage.setMinWidth(300);
    stage.setMinHeight(300);
    stage.setTitle("Second Window");
    stage.show();
  }

  private ContextMenu createContextMenu(Pane root, Shape shape, PhysicsBody2D body) {
    ContextMenu contextMenu = new ContextMenu();
    MenuItem item1 = new MenuItem("Delete shape");
    contextMenu.getItems().add(item1);
    item1.setOnAction(event2 -> {
      root.getChildren().remove(shape);
      bodies.remove(body);
    });
    return contextMenu;
  }

  private void addQuad(Pane root, ArrayList<PhysicsBody2D> list,  Rectangle rect, BoxBody2D bb) {
    Rectangle shape;
    if (rect == null) {
      shape = Utils.getFig1();
      Point2D point2D = Utils.getRandomPoint(root);
      shape.setX(point2D.getX());
      shape.setY(point2D.getY());
    } else {
      shape = rect;
    }
    root.getChildren().addFirst(shape);
    BoxBody2D body = bb == null ? new BoxBody2D(shape, null) : bb;
    list.add(body);
    shape.setOnDragDetected(event -> {
      Dragboard db = shape.startDragAndDrop(TransferMode.COPY_OR_MOVE);
      ClipboardContent content = new ClipboardContent();
      content.put(dfBb2d, body.getTransit());
      db.setContent(content);
    });
    shape.setOnDragDone(event -> {
      if (event.isAccepted()) {
        root.getChildren().remove(shape);
        list.remove(body);
      } else {
        System.out.println("Nou");
      }
    });
    shape.setOnContextMenuRequested(event1 -> {
      ContextMenu contextMenu = createContextMenu(root, shape, body);
      contextMenu.show(shape, event1.getScreenX(), event1.getScreenY());
    });
  }

  private void addCircle(Pane root, ArrayList<PhysicsBody2D> list,  Circle circle, CircleBody2D cb) {
    Circle shape;
    if (circle == null) {
      shape = Utils.getFig2();
      Point2D point2D = Utils.getRandomPoint(root);
      shape.setBlendMode(BlendMode.MULTIPLY);
      shape.setCenterX(point2D.getX());
      shape.setCenterY(point2D.getY());
    } else {
      shape = circle;
    }
    root.getChildren().addFirst(shape);
    CircleBody2D body = cb == null ? new CircleBody2D(shape, null) : cb;
    list.add(body);
    shape.setOnDragDetected(event -> {
      Dragboard db = shape.startDragAndDrop(TransferMode.COPY_OR_MOVE);
      ClipboardContent content = new ClipboardContent();
      content.put(dfCb2d, body.getTransit());
      db.setContent(content);
    });
    shape.setOnDragDone(event -> {
      if (event.isAccepted()) {
        root.getChildren().remove(shape);
        list.remove(body);
      } else {
        System.out.println("Nou");
      }
    });
    shape.setOnContextMenuRequested(event1 -> {
      ContextMenu contextMenu = createContextMenu(root, shape, body);
      contextMenu.show(shape, event1.getScreenX(), event1.getScreenY());
    });
  }

  private void addPolygon(Pane root, ArrayList<PhysicsBody2D> list,  Polygon poly, PolygonBody2D pb) {
    Polygon shape;
    if (poly == null) {
      shape = Utils.getFig3();
      Point2D point2D = Utils.getRandomPoint(root);
      shape.setBlendMode(BlendMode.MULTIPLY);
      Utils.movePolygon(shape, point2D);
    } else {
      shape = poly;
    }
    root.getChildren().addFirst(shape);
    PolygonBody2D body = pb == null ? new PolygonBody2D(shape, null) : pb;
    list.add(body);
    shape.setOnDragDetected(event -> {
      Dragboard db = shape.startDragAndDrop(TransferMode.COPY_OR_MOVE);
      ClipboardContent content = new ClipboardContent();
      content.put(dfPb2d, body.getTransit());
      db.setContent(content);
    });
    shape.setOnDragDone(event -> {
      if (event.isAccepted()) {
        root.getChildren().remove(shape);
        list.remove(body);
      } else {
        System.out.println("Nou");
      }
    });
    shape.setOnContextMenuRequested(event1 -> {
      ContextMenu contextMenu = createContextMenu(root, shape, body);
      contextMenu.show(shape, event1.getScreenX(), event1.getScreenY());
    });
  }

}