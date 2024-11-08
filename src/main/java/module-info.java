module es.guillearana.examendein1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.base;  // Requiere funcionalidades básicas de JavaFX


    opens es.guillearana.examendein1 to javafx.graphics, javafx.fxml;
    opens es.guillearana.examendein1.model to javafx.base;  // Modelos de datos
    opens es.guillearana.examendein1.dao to javafx.base;  // Acceso a datos (DAO)
    opens es.guillearana.examendein1.controllers to javafx.graphics, javafx.fxml;

    exports es.guillearana.examendein1;
    exports es.guillearana.examendein1.controllers;
    exports es.guillearana.examendein1.model;  // Exporta los modelos de datos
    exports es.guillearana.examendein1.dao;  // Exporta los DAOs para acceso a base de datos
}