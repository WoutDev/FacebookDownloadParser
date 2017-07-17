package be.woutdev.facebookdownloadparser;

import javafx.scene.control.*;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created by Wout on 17/07/2017.
 */
public class ParserMenu
{
    private final Parser stage;

    public ParserMenu(Parser stage)
    {
        this.stage = stage;
    }

    public MenuBar getMenu()
    {
        MenuBar menu = new MenuBar();

        Menu file = new Menu("File");
        Menu util = new Menu("Util");

        MenuItem open = new MenuItem("Open...");
        MenuItem close = new MenuItem("Close");

        MenuItem stats = new MenuItem("Statistics");
        MenuItem openInBrowser = new MenuItem("Open in browser");

        stats.setDisable(stage.getData().size() == 0);
        openInBrowser.setDisable(stage.getData().size() == 0);

        open.setOnAction((e) -> openAction());
        close.setOnAction((e) -> closeAction());

        stats.setOnAction((e) -> statsAction());
        openInBrowser.setOnAction((e) -> openInBrowserAction());

        file.getItems().addAll(open, new SeparatorMenuItem(), close);

        util.getItems().addAll(stats, openInBrowser);

        menu.getMenus().addAll(file, util);

        return menu;
    }

    private void openInBrowserAction()
    {
        try
        {
            Desktop.getDesktop().browse(Paths.get(stage.getTemp().toAbsolutePath().toString(), "index.htm").toUri());
        }
        catch (IOException e)
        {
            new Alert(Alert.AlertType.ERROR, "Failed to open the index!");
            e.printStackTrace();
        }
    }

    private void statsAction()
    {
        stage.getStatisticsScene().show();
    }

    private void loadAll()
    {
        stage.getStatisticsScene().load();
        // TODO: Load in main scene data
    }

    private void closeAction()
    {
        System.exit(0);
    }

    private void openAction()
    {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open Downloaded Facebook Zip");

        FileChooser.ExtensionFilter extFilter =
                new FileChooser.ExtensionFilter("ZIP files (*.zip)", "*.zip");
        chooser.getExtensionFilters().add(extFilter);

        File file = chooser.showOpenDialog(stage.getStage());

        if (file != null)
        {
            try
            {
                ZipFile zipFile = new ZipFile(file);

                Enumeration<? extends ZipEntry> entries = zipFile.entries();

                while (entries.hasMoreElements())
                {
                    ZipEntry entry = entries.nextElement();

                    if (entry.isDirectory())
                    {
                        Files.createDirectory(Paths.get(stage.getTemp().toAbsolutePath().toString(), entry.getName()));
                    }

                    if ((entry.getName().startsWith("html") || entry.getName().startsWith("index")) &&
                        entry.getName().endsWith(".htm"))
                    {
                        InputStream is = zipFile.getInputStream(entry);

                        String shortenedName = entry.getName().contains("/") ? entry.getName()
                                                                                    .split("/")[1] : entry.getName();

                        Files.copy(is, Paths.get(stage.getTemp().toAbsolutePath().toString(), entry.getName()));

                        is.close();

                        stage.getData().put(shortenedName.substring(0, shortenedName.length() - 4),
                                            Files.readAllLines(Paths.get(stage.getTemp().toAbsolutePath().toString(),
                                                                         entry.getName())));
                    }
                    else if (entry.getName().contains("style.css"))
                    {
                        InputStream is = zipFile.getInputStream(entry);

                        Files.copy(is, Paths.get(stage.getTemp().toAbsolutePath().toString(), entry.getName()));

                        is.close();
                    }
                }

                loadAll();

                ((BorderPane) stage.getStage().getScene().getRoot()).setTop(getMenu());
            }
            catch (Exception e)
            {
                new Alert(Alert.AlertType.ERROR, "Failed to open selected file!");
            }
        }
    }
}
