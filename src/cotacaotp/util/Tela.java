package cotacaotp.util;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

/**
 *
 * @author abnerjp
 */
public class Tela {

    private String caminho;
    private String titulo;
    private boolean maximizado;
    private boolean redimensionavel;
    private boolean barra;
    private Parent root;
    private FXMLLoader loader;

    public Tela(String caminho, String titulo, boolean maximizado, boolean redimensionavel, boolean barra) throws IOException{
        this.caminho = caminho;
        this.titulo = titulo;
        this.maximizado = maximizado;
        this.redimensionavel = redimensionavel;
        this.barra = barra;
        geraLoader();
    }

    public Tela(String caminho, String titulo) throws IOException {
        this(caminho, titulo, false, false, false);
    }

    public Tela(String caminho) throws IOException {
        this(caminho, "");
    }

    /**
     * Exibir tela normal: permite selecionar a tela "pai" enquanto a filha estiver aberta
     *
     * @throws java.io.IOException
     */
    public void exibirTela() throws IOException{
        Stage stage = preparaExibe();
        stage.initModality(Modality.NONE);
        stage.showAndWait();
    }
    
    /**
     * Exibir tela filha: NÃO permite selecionar a tela "pai" enquanto a filha estiver aberta
     * @throws java.io.IOException
     */
    public void exibirTelaFilha() throws IOException {
        Stage stage = preparaExibe();
        stage.initModality(Modality.APPLICATION_MODAL);
        
        stage.showAndWait();
    }
    
    /**
     * Utilizar somente na primeira janela que será aberta no sistema
     * @throws IOException
     */
    public void exibirPrimeiraTela() throws IOException {
        Stage stage = preparaExibe();
        
        stage.initModality(Modality.APPLICATION_MODAL);

        
        // Para a janela que será aberta, fecha todo o sistema ao clicar no "X" (fechar)
        stage.setOnCloseRequest((WindowEvent arg0) -> {
            System.exit(0);
        });

        stage.show();
    }
    
    /**
     * cria scene e stage com dados dos atributos, e retorna stage preparado para ser exibido
     * @return
     * @throws IOException 
     */
    private Stage preparaExibe() throws IOException {
        //Parent root = loader.load();

        Scene scene = new Scene(root);
        Stage stage = new Stage();

        if (!barra) {
            stage.initStyle(StageStyle.UNDECORATED);
        }

        stage.setScene(scene);
        stage.setTitle(titulo);
        stage.setMaximized(maximizado);
        stage.setResizable(redimensionavel);

        return stage;
    }
    
    /**
     * gera objeto FXMLoader, para que seja possível retornar valores das telas
     * @return 
     */
    private void geraLoader () throws IOException {
        loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(caminho));
        root = loader.load(); // só é possivel capturar a classe da janela após esta linha ser executada
    }

    public FXMLLoader getLoader() {
        return loader;
    }
}
