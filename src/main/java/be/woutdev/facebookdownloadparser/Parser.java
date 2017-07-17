package be.woutdev.facebookdownloadparser;

import be.woutdev.facebookdownloadparser.scene.StatisticsScene;
import be.woutdev.facebookdownloadparser.thread.ShutdownHook;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Wout on 17/07/2017.
 */
public class Parser extends Application
{
    private Stage stage;
    private HashMap<String, List<String>> data;
    private ParserMenu parserMenu;
    private StatisticsScene statisticsScene;
    private Path temp;

    @Override
    public void start(Stage stage) throws Exception
    {
        this.stage = stage;
        this.data = new HashMap<>();
        this.parserMenu = new ParserMenu(this);
        this.statisticsScene = new StatisticsScene(this);
        this.temp = Files.createTempDirectory("fb-tmp");

        Runtime.getRuntime().addShutdownHook(new ShutdownHook(this));

        MenuBar menu = parserMenu.getMenu();

        BorderPane root = new BorderPane();
        root.setTop(menu);

        Scene scene = new Scene(root, 1500, 900);

        stage.setScene(scene);
        stage.setTitle("Facebook Download Parser");
        stage.setResizable(false);
        stage.show();
    }

    public Stage getStage()
    {
        return stage;
    }

    public HashMap<String, List<String>> getData()
    {
        return data;
    }

    public Path getTemp()
    {
        return temp;
    }

    public ParserMenu getParserMenu()
    {
        return parserMenu;
    }

    public StatisticsScene getStatisticsScene()
    {
        return statisticsScene;
    }
}
