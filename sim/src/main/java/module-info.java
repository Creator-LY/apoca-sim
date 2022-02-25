module com {
    requires transitive javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires de.jensd.fx.glyphs.fontawesome;

    opens com.gui to javafx.fxml;
    exports com.gui;
}
