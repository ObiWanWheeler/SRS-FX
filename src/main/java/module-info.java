module com.obiwanwheeler {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.datatype.jsr310;

    opens com.obiwanwheeler to javafx.fxml;
    opens com.obiwanwheeler.fxmlControllers to javafx.fxml;
    opens com.obiwanwheeler.utilities to com.fasterxml.jackson.databind;

    exports com.obiwanwheeler.creators;
    exports com.obiwanwheeler.interfaces;
    exports com.obiwanwheeler.objects;
    exports com.obiwanwheeler.utilities;
    exports com.obiwanwheeler;
}