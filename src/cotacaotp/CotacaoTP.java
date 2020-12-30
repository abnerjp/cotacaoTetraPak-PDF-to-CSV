package cotacaotp;

import cotacaotp.util.Tela;
import java.io.IOException;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 *
 * @author abnerjp
 */
public class CotacaoTP extends Application {
    
    @Override
    public void start(Stage primaryStage) throws IOException {
        Tela tl = new Tela("/cotacaotp/view/ConversorPDF.fxml", "Tetra Pak - PDF to CSV", false, false, true);
        try {
            tl.exibirPrimeiraTela();
        } catch (IOException ex) {
            System.out.println("erro ao exibir janela");
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
