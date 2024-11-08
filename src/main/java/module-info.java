module es.guillearana.examendein1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens es.guillearana.examendein1 to javafx.fxml;
    exports es.guillearana.examendein1;
    exports es.guillearana.examendein1.controllers;
    opens es.guillearana.examendein1.controllers to javafx.fxml;
}