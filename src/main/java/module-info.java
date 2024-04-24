module com.example.thirdlab {
  requires javafx.controls;
  requires javafx.fxml;
  requires java.desktop;
  requires javafx.swing;


  opens com.example.thirdlab to javafx.fxml;
  exports com.example.thirdlab;
  exports com.example.thirdlab.img;
  opens com.example.thirdlab.twodee to javafx.fxml;
  opens com.example.thirdlab.threedee to javafx.fxml;
  opens com.example.thirdlab.img to javafx.fxml;

}