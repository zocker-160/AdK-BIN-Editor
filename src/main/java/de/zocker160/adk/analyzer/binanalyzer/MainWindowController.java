package de.zocker160.adk.analyzer.binanalyzer;

import de.zocker160.adk.analyzer.binanalyzer.utils.ListFile;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.effect.ImageInput;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.FilenameFilter;

public class MainWindowController {
    @FXML private Label filenameLabel;
    @FXML private Label messageLabel;

    // Global ---

    @FXML protected TextField value1;
    @FXML protected TextField value2;
    @FXML protected TextField value3;
    @FXML protected TextField value4;

    @FXML protected TextField fogStart;
    @FXML protected TextField fogEnd;

    @FXML private ListView<ListFile> filesList;
    @FXML private Label directoryLocation;

    @FXML private GridPane dataGrid;
    @FXML private Slider testSlider;

    @FXML protected ColorPicker fogColor;
    @FXML protected ColorPicker ambientColor;
    @FXML protected ColorPicker lightColor;

    @FXML protected VBox dataBox;

    // Zone ---
    private int currentZone = 0;
    protected int maxZone = 0;

    @FXML private GridPane zoneGrid;

    @FXML protected Label zoneLabel;
    @FXML private Button prevZoneButton;
    @FXML private Button nextZoneButton;

    @FXML protected ColorPicker fogColorZone;
    @FXML protected ColorPicker ambientColorZone;
    @FXML protected ColorPicker lightColorZone;

    @FXML protected TextField shadowDensityZone;

    @FXML protected TextField fogStartZone;
    @FXML protected TextField fogEndZone;

    @FXML protected TextField posXZone;
    @FXML protected TextField posYZone;

    @FXML protected TextField radiusStartZone;
    @FXML protected TextField radiusEndZone;

    @FXML
    protected void openFile() {
        try {
            String filename = MainWindow.openFile();
            filenameLabel.setText(filename);

            dataGrid.setDisable(false);
            filesList.getItems().clear();
            directoryLocation.setText("<no folder selected>");

        } catch (NullPointerException ignore) {}
    }

    @FXML
    protected void openFileFromList() {
        ListFile file = filesList.getSelectionModel().getSelectedItem();

        try {
            String filename = MainWindow.openFile(file);
            filenameLabel.setText(filename);
            dataGrid.setDisable(false);

        } catch (NullPointerException ignore) {}
    }

    @FXML
    protected void openFolder() {
        var dirChooser = new DirectoryChooser();
        var filter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".bin");
            }
        };

        dirChooser.setTitle("Select directory containing BIN files");
        File binDir = dirChooser.showDialog(MainWindow.getStage());

        try {
            filesList.getItems().clear();

            for (File file : binDir.listFiles(filter)) {
                System.out.println(file);
                filesList.getItems().add(new ListFile(file));
            }
            directoryLocation.setText(binDir.getName());

        } catch (NullPointerException ignore) {
            return;
        }
    }

    @FXML
    protected void saveFile() {
        try {
            MainWindow.saveToFile();
        } catch (NullPointerException ignore) {}
    }

    @FXML
    protected void saveFileAs() {
        var fileChooser = new FileChooser();
        fileChooser.setTitle("Save file...");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("AdK BIN file", "*.bin"),
                new FileChooser.ExtensionFilter("All files", "*.*")
        );

        File file = fileChooser.showSaveDialog(MainWindow.getStage());
        try {
            MainWindow.saveToFile(file);
        } catch (NullPointerException ignore) {}
    }

    public void saveAndSetZone(int i) {
        MainWindow.saveZone(currentZone);
        setZone(i);
    }

    public void setZone(int i) {
        prevZoneButton.setDisable(false);
        nextZoneButton.setDisable(false);
        zoneGrid.setDisable(true);

        if (i <= 0)
            prevZoneButton.setDisable(true);

        if (i >= maxZone)
            nextZoneButton.setDisable(true);

        if (maxZone < 0) {
            zoneLabel.setText("no Zone");
            return;
        }

        if (i < 0 || i > maxZone)
            return;

        currentZone = i;
        MainWindow.loadZone(currentZone);

        zoneLabel.setText("Zone "+(currentZone+1)+"/"+(maxZone+1));
        zoneGrid.setDisable(false);
    }

    protected int getCurrentZone() {
        return currentZone;
    }

    @FXML
    protected void selectPreviousZone() {
        saveAndSetZone(currentZone-1);
    }
    @FXML
    protected void selectNextZone() {
        saveAndSetZone(currentZone+1);
    }
}