import controller.GameController;
import model.GameModel;
import view.GameView;

public class Main {
    public static void main(String[] args) {
        // Initialize game model (stores game state)
        GameModel model = new GameModel();
        // Initialize game view (UI components)
        GameView view = new GameView(model);
        // Initialize controller to handle interactions
        new GameController(model, view);
    }
}