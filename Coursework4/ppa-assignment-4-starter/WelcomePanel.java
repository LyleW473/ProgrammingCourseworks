import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * The Welcome Panel is the first page that the user sees when loading up the application.
 * It provides brief instructions on how to navigate through the application and what it contains.
 */
public class WelcomePanel extends Panel{
    
    Font font1 = Font.font("Times New Roman", FontWeight.LIGHT, 20);
    Font font2 = Font.font("Nunito", FontWeight.EXTRA_LIGHT, 18);
    Font font3 = Font.font("Nunito", FontWeight.EXTRA_LIGHT, 16);
    
    public WelcomePanel() 
    {    
        // Title Label
        Label titleLabel = new Label("༺ Welcome to Covid Viewer ༻");
        titleLabel.setFont(Font.font("Times New Roman", FontWeight.BOLD, 40));
        
        // Decoration Labels
        Label decorLabel1 = new Label("•   °    +   °   • ");
        decorLabel1.setFont(font1);
        
        Label decorLabel2 = new Label("•   °    +   °   • ");
        decorLabel2.setFont(font1);
        
        // Information Labels 
        Label infoLabel1 = new Label("Covid Viewer is a visual exploration tool for COVID-19 data in London.");
        infoLabel1.setAlignment(Pos.CENTER);
        infoLabel1.setPrefWidth(700);
        infoLabel1.setFont(font2);
        
        Label infoLabel2 = new Label("This application contains a map which displays death rates across London boroughs : ");
        infoLabel2.setAlignment(Pos.CENTER);
        infoLabel2.setPrefWidth(900);
        infoLabel2.setFont(font3);
        
        Label infoLabel3 = new Label("↠ A lighter shade indicates a lower death rate and a darker shade represents a higher rate. \n↠ To see more borough-specific information, simply click on the desired borough.");
        infoLabel3.setAlignment(Pos.CENTER);
        infoLabel3.setPrefWidth(900);
        infoLabel3.setFont(font3);
        
        Label infoLabel4 = new Label("Additionally, Covid Viewer calculates various statistics within the selected date range.");
        infoLabel4.setAlignment(Pos.CENTER);
        infoLabel4.setPrefWidth(900);
        infoLabel4.setFont(font3);
        
        // Instruction Labels
        Label instrLabel1 = new Label("To start, please select a date range of your liking.");
        instrLabel1.setAlignment(Pos.CENTER);
        instrLabel1.setPrefWidth(900);
        instrLabel1.setFont(font2);
        
        Label instrLabel2 = new Label("The buttons found near the bottom are used to navigate through the application. ");
        instrLabel2.setAlignment(Pos.CENTER);
        instrLabel2.setPrefWidth(900);
        instrLabel2.setFont(font2);
        
        // VBox to contain the Information and Instruction Labels
        VBox infoBox = new VBox(5);
        infoBox.getChildren().addAll(decorLabel1, infoLabel1, instrLabel1, instrLabel2, infoLabel2, infoLabel3, infoLabel4);
        infoBox.setAlignment(Pos.TOP_CENTER);
        VBox.setMargin(decorLabel1, new Insets(50, 0, 60, 0));
        VBox.setMargin(instrLabel2, new Insets(0, 0, 30, 0));
        
        // Borderpane that makes up the entire Welcome Panel
        this.pane = new BorderPane(infoBox, titleLabel, null, decorLabel2, null);
        BorderPane.setAlignment(titleLabel, Pos.CENTER);
        BorderPane.setMargin(titleLabel, new Insets(70, 0, 0, 0));
        BorderPane.setAlignment(decorLabel2, Pos.CENTER);
        BorderPane.setMargin(decorLabel2, new Insets(30));
        this.pane.setPrefSize(1000,720); 
    }

    public void update()
    {}
}

