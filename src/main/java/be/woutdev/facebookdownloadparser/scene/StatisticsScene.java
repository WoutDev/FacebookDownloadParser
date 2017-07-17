package be.woutdev.facebookdownloadparser.scene;

import be.woutdev.facebookdownloadparser.Parser;
import com.sun.javafx.collections.ObservableListWrapper;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;

/**
 * Created by Wout on 17/07/2017.
 */
public class StatisticsScene
{
    private final Parser parser;
    private final VBox box;
    private final Stage stage;
    private final Scene scene;
    private final TableView<Statistic> view;
    private final ObservableList<Statistic> statistics;

    public StatisticsScene(Parser parser)
    {
        this.parser = parser;
        this.box = new VBox();
        this.stage = new Stage();
        this.statistics = new ObservableListWrapper<Statistic>(new ArrayList<>());

        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(parser.getStage());

        scene = new Scene(box, 600, 300);

        view = new TableView<Statistic>();

        TableColumn<Statistic, String> name = new TableColumn("Statistic");
        TableColumn<Statistic, Object> value = new TableColumn("Data");

        name.setCellValueFactory((a) -> new ReadOnlyObjectWrapper<>(a.getValue().getName()));
        value.setCellValueFactory((a) -> new ReadOnlyObjectWrapper<>(a.getValue().getValue()));

        name.prefWidthProperty().bind(view.widthProperty().divide(2));
        value.prefWidthProperty().bind(view.widthProperty().divide(2));

        view.getColumns().addAll(name, value);

        box.setSpacing(5);
        box.getChildren().add(view);
    }

    public void show()
    {
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Statistics");
        stage.show();
    }

    public void load()
    {
        view.getItems().add(new Statistic("Account's age", "7 years")); // TODO: Extract cool statistics
    }

    public static class Statistic
    {
        private final String name;
        private final Object value;

        public Statistic(String name, Object value)
        {
            this.name = name;
            this.value = value;
        }

        public String getName()
        {
            return name;
        }

        public Object getValue()
        {
            return value;
        }
    }
}