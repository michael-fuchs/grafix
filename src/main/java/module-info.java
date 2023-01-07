module mifu.grafix {
  requires javafx.controls;
  requires javafx.fxml;

  requires org.kordamp.ikonli.javafx;
  requires org.kordamp.bootstrapfx.core;

  opens mifu.grafix to javafx.fxml;
  exports mifu.grafix;
}
