import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.Group; 
import javafx.scene.layout.BorderPane; 
import javafx.scene.layout.HBox; 
import javafx.scene.paint.Color; 
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * A graphical view of the simulation grid. The view displays a rectangle for
 * each location. Colors for each type of life form can be defined using the
 * setColor method.
 *
 * @author David J. Barnes, Michael Kölling & Jeffery Raphael
 * @version 2024.02.03
 */

public class SimulatorView extends Application {
   
    public static final int WIN_WIDTH = 850; //650;
    public static final int WIN_HEIGHT = 850; //650;  

    private final String GENERATION_PREFIX = "Generation: ";
    private final String POPULATION_PREFIX = "Population: ";

    private Label genLabel, population, infoLabel;

    private FieldCanvas fieldCanvas;
    private Simulator simulator;

    /**
     * Create a view of the given width and height.
     * @param Stage The window for the JavaFX application.
     */
    @Override
    public void start(Stage stage) {
        
        fieldCanvas = new FieldCanvas(WIN_WIDTH - 50, WIN_HEIGHT - 50);
        simulator = new Simulator();
        fieldCanvas.setScale(Simulator.GRID_HEIGHT, Simulator.GRID_WIDTH); 

        Group root = new Group();
        
        genLabel = new Label(GENERATION_PREFIX);
        infoLabel = new Label("  ");
        population = new Label(POPULATION_PREFIX);

        BorderPane bPane = new BorderPane(); 
        HBox infoPane = new HBox();
        HBox popPane = new HBox();
        

        infoPane.setSpacing(10);
        infoPane.getChildren().addAll(genLabel, infoLabel);       
        popPane.getChildren().addAll(population); 
        
        bPane.setTop(infoPane);
        bPane.setCenter(fieldCanvas);
        bPane.setBottom(population);
        
        root.getChildren().add(bPane);
        Scene scene = new Scene(root, WIN_WIDTH, WIN_HEIGHT); 
        
        stage.setScene(scene);          
        stage.setTitle("Life Simulation");
        updateCanvas(simulator.getGeneration());
        
        stage.show();     
    }

    /**
     * Display a short information label at the top of the window.
     * @param text The information label to display.
     */
    private void setInfoText(String text) {
        infoLabel.setText(text);
    }

    /**
     * Show the current status of the field.
     * @param generation The current generation.
     */
    private void updateCanvas(int generation) {
        genLabel.setText(GENERATION_PREFIX + generation);
        String populationDetails;
        populationDetails = fieldCanvas.update(simulator.getField());
        population.setText(POPULATION_PREFIX + populationDetails);
    }

    /**
     * Run the simulation from its current state for the given number of
     * generations.
     * @param numGenerations The number of generations to run for.
     */
    public void simulate(int numGenerations) {
        new Thread(() -> {
           
            for (int gen = 1; gen <= numGenerations; gen++) {
                simulator.simOneGeneration();    
                simulator.delay(1000);
                Platform.runLater(() -> {
                    updateCanvas(simulator.getGeneration());
                });
            }
            
        }).start();
    }

    /**
     * Reset the simulation to a starting position.
     */
    public void reset() {
        simulator.reset();
        updateCanvas(simulator.getGeneration());
    }
    
    public static void main(String args[]){           
        launch(args);      
   } 
}
