package com.example;

import com.example.data.DataEntry;
import com.example.data.DataManager;
import com.example.regex.RegexUtil;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * The {@code MainApp} class sets up the JavaFX user interface and
 * orchestrates interactions between the regex utilities and the data
 * management components.  It contains two tabs: one for regular
 * expression search/replace operations and another for managing a
 * collection of key–value pairs.  Each tab provides controls for
 * entering data, executing actions, and displaying results to the
 * user.
 */
public class MainApp extends Application {
    /**
     * Manages the collection of data entries used by the data tab.
     */
    private final DataManager dataManager = new DataManager();

    @Override
    public void start(Stage primaryStage) {
        // Create a tab pane to separate regex functionality from data management.
        TabPane tabPane = new TabPane();

        Tab regexTab = new Tab("Regex Operations");
        regexTab.setContent(createRegexPane());
        regexTab.setClosable(false);

        Tab dataTab = new Tab("Data Management");
        dataTab.setContent(createDataPane());
        dataTab.setClosable(false);

        tabPane.getTabs().addAll(regexTab, dataTab);

        // Set up the stage and display the application.
        Scene scene = new Scene(tabPane, 800, 600);
        primaryStage.setTitle("Text Processing Tool");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Constructs the pane that handles regular expression search and
     * replace operations.  Users can enter text and regex patterns,
     * search for matches, and replace occurrences with a replacement
     * string.  Results are displayed in a list view and a separate
     * text area.
     *
     * @return a node representing the regex tab content
     */
    private Node createRegexPane() {
        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        // Input area for the text on which regex operations will be performed.
        Label textLabel = new Label("Enter Text:");
        TextArea textArea = new TextArea();
        textArea.setWrapText(true);
        textArea.setPrefRowCount(6);

        // Input for the regex pattern.  This field accepts any valid Java
        // regular expression.  If the pattern is invalid, an error will
        // be displayed in the results area.
        Label patternLabel = new Label("Enter Regex Pattern:");
        TextField patternField = new TextField();
        patternField.setPromptText("e.g., \\d+ for digits");

        // Replacement field for substitution operations.  If left
        // empty, matches will be removed when the Replace button is
        // clicked.
        Label replacementLabel = new Label("Replacement (optional):");
        TextField replacementField = new TextField();
        replacementField.setPromptText("Replacement text");

        // Buttons to trigger match search and replacement operations.
        Button findButton = new Button("Find Matches");
        Button replaceButton = new Button("Replace");

        // List view to display all matches found in the text.  Each
        // entry represents one occurrence of the pattern.
        ListView<String> matchListView = new ListView<>();
        Label matchLabel = new Label("Matches:");

        // Area to display the result of the replacement operation.  It
        // shows the original text with all matches replaced by the
        // replacement string.  This area is read‑only to avoid
        // confusion with the input text area.
        Label replacedLabel = new Label("Replaced Text:");
        TextArea replacedArea = new TextArea();
        replacedArea.setWrapText(true);
        replacedArea.setEditable(false);
        replacedArea.setPrefRowCount(4);

        // Event handler for the Find Matches button.  Compiles the
        // provided pattern and searches the input text for matches.
        findButton.setOnAction(e -> {
            String text = textArea.getText();
            String pattern = patternField.getText();
            matchListView.getItems().clear();
            if (pattern == null || pattern.isEmpty() || text == null) {
                return;
            }
            try {
                for (String match : RegexUtil.findMatches(pattern, text)) {
                    matchListView.getItems().add(match);
                }
                if (matchListView.getItems().isEmpty()) {
                    matchListView.getItems().add("No matches found");
                }
            } catch (Exception ex) {
                matchListView.getItems().add("Invalid pattern: " + ex.getMessage());
            }
        });

        // Event handler for the Replace button.  Performs a global
        // replacement on the input text using the provided pattern
        // and replacement string.
        replaceButton.setOnAction(e -> {
            String text = textArea.getText();
            String pattern = patternField.getText();
            String replacement = replacementField.getText();
            if (pattern == null || pattern.isEmpty() || text == null) {
                return;
            }
            try {
                String replaced = RegexUtil.replace(pattern,
                        replacement == null ? "" : replacement,
                        text);
                replacedArea.setText(replaced);
            } catch (Exception ex) {
                replacedArea.setText("Invalid pattern: " + ex.getMessage());
            }
        });

        // Arrange buttons horizontally for a cleaner look.
        HBox buttonBox = new HBox(10, findButton, replaceButton);

        // Assemble all components into the vertical layout.
        root.getChildren().addAll(
                textLabel, textArea,
                patternLabel, patternField,
                replacementLabel, replacementField,
                buttonBox,
                matchLabel, matchListView,
                replacedLabel, replacedArea);

        // Wrap the VBox in a scroll pane so that content remains accessible
        // when the window is resized.
        ScrollPane scrollPane = new ScrollPane(root);
        scrollPane.setFitToWidth(true);
        return scrollPane;
    }

    /**
     * Constructs the pane that manages a collection of key–value pairs.
     * Users can add new entries, update existing ones, and delete
     * entries by key.  The collection uses Java's Map under the hood
     * and demonstrates proper use of equals and hashCode for custom
     * objects.
     *
     * @return a node representing the data management tab content
     */
    private Node createDataPane() {
        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        // Table to display the entries.  It shows the key and value
        // columns and binds to an observable list that updates when
        // modifications occur.
        TableView<DataEntry> table = new TableView<>();
        TableColumn<DataEntry, String> keyCol = new TableColumn<>("Key");
        keyCol.setCellValueFactory(new PropertyValueFactory<>("key"));
        keyCol.setPrefWidth(200);

        TableColumn<DataEntry, String> valueCol = new TableColumn<>("Value");
        valueCol.setCellValueFactory(new PropertyValueFactory<>("value"));
        valueCol.setPrefWidth(300);

        table.getColumns().addAll(keyCol, valueCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setItems(dataManager.getObservableList());

        // Input fields for the key and value.  Keys are used to
        // uniquely identify entries in the collection.  Values may be
        // blank.
        TextField keyField = new TextField();
        keyField.setPromptText("Key");
        TextField valueField = new TextField();
        valueField.setPromptText("Value");

        // Button that adds a new entry or updates an existing entry
        // with the same key.  If the key already exists, the value is
        // replaced; otherwise a new entry is inserted.
        Button addUpdateButton = new Button("Add/Update");
        addUpdateButton.setOnAction(e -> {
            String key = keyField.getText();
            String value = valueField.getText();
            if (key == null || key.isEmpty()) {
                return;
            }
            if (dataManager.addOrUpdateEntry(key, value)) {
                // Clearing the fields after a successful add/update makes
                // repeated operations more convenient.
                keyField.clear();
                valueField.clear();
                table.refresh();
            }
        });

        // Button that deletes an entry based on its key.  If the key
        // does not exist in the collection, nothing happens.
        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(e -> {
            String key = keyField.getText();
            if (key == null || key.isEmpty()) {
                return;
            }
            if (dataManager.deleteEntry(key)) {
                keyField.clear();
                valueField.clear();
                table.refresh();
            }
        });

        // Arrange input fields and buttons horizontally.
        HBox inputBox = new HBox(10, keyField, valueField, addUpdateButton, deleteButton);
        inputBox.setAlignment(Pos.CENTER_LEFT);

        // Add table and input controls to the layout.
        root.getChildren().addAll(table, inputBox);
        return root;
    }

    public static void main(String[] args) {
        launch(args);
    }
}