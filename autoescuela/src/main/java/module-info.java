module com.najocar.autoescuela {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.xml.bind;
    requires java.sql;

    opens com.najocar.autoescuela to javafx.fxml;
    exports com.najocar.autoescuela;
    exports com.najocar.autoescuela.controllers;
    opens com.najocar.autoescuela.controllers to javafx.fxml;
    opens com.najocar.autoescuela.model.connections to java.xml.bind;
    opens com.najocar.autoescuela.model.domain to javafx.base;
    exports com.najocar.autoescuela.utils;
    opens com.najocar.autoescuela.utils to javafx.fxml;
    exports com.najocar.autoescuela.model.dao;
    exports com.najocar.autoescuela.model.domain;

}