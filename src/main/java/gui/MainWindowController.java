package gui;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import task4.ParseException;
import task4.Parser;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

public class MainWindowController {
    @FXML
    private TextArea code;
    @FXML
    private Parent root;
    @FXML
    private TableColumn<Variable, String> variableColumn;
    @FXML
    private TableColumn<Variable, Double> valueColumn;
    @FXML
    private TableView<Variable> table;
    @FXML
    private TextArea log;
    private ObservableList<Variable> vars;

    @FXML
    private void run()
    {
        String expression = code.getText();
        String logMessage = "";
        vars.remove(0, vars.size());
        try {
            Map<String, Double> varsMap = Parser.compile(expression);
            vars.addAll(varsMap.entrySet().stream().map(stringDoubleEntry -> new Variable(stringDoubleEntry.getKey(), stringDoubleEntry.getValue())).collect(Collectors.toList()));
            logMessage = "Successfully run!";
        } catch (ParseException e)        {
            e.printStackTrace();
            logMessage = e.getMessage()+ " at "+ e.index;
        }
        finally {
            log.setText(logMessage);
        }

    }
    @FXML
    private void initialize()
    {
        vars = FXCollections.observableArrayList();
        variableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        valueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
        table.setItems(vars);
    }


    public Parent getRoot() {
        return root;
    }

    static MainWindowController create() throws IOException {
        FXMLLoader loader = new FXMLLoader(MainWindowController.class.getResource("MainWindow.fxml"));
        loader.load();
        return loader.getController();
    }

    public class Variable
    {
        private SimpleStringProperty name;
        private SimpleDoubleProperty value;
        private Variable(String name, double value)
        {
            this.name = new SimpleStringProperty(name);
            this.value = new SimpleDoubleProperty(value);
        }

        public String getName() {
            return name.get();
        }

        public SimpleStringProperty nameProperty() {
            return name;
        }

        public void setName(String name) {
            this.name.set(name);
        }

        public double getValue() {
            return value.get();
        }

        public SimpleDoubleProperty valueProperty() {
            return value;
        }

        public void setValue(double value) {
            this.value.set(value);
        }
    }
}
