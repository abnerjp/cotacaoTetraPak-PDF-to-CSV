package cotacaotp.controller;

import cotacaotp.model.ArquivoCSV;
import cotacaotp.model.ArquivoPDF;
import cotacaotp.util.Alerta;
import cotacaotp.util.Tela;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

/**
 * FXML Controller class
 *
 * @author abnerjp
 */
public class ConversorPDFController implements Initializable {

    String text = "";
    @FXML
    private TextField txtCaminho;
    @FXML
    private Button btnBuscar;
    @FXML
    private Button btnProcessarTXT;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    private void salvarTXT(String text) {
        try (FileOutputStream arquivo = new FileOutputStream("cotacaoemtexto1.txt")) {
            try (PrintWriter pw = new PrintWriter(arquivo)) {
                pw.println(text);
            }
        } catch (IOException ex) {
            System.out.println("Erro ao escrever no arquivo: " + ex.getMessage());
        }
    }

    private File carregarArquivo() {
        FileChooser telaBusca = new FileChooser();

        telaBusca.setTitle("Carregar cotação *.pdf");
        telaBusca.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Cotação Tetra Pak (*.pdf*)", "*.pdf*")
        );

        File f = telaBusca.showOpenDialog(btnProcessarTXT.getScene().getWindow());

        return f;
    }

    @FXML
    private void clkProcurar(ActionEvent event) {
        File f = carregarArquivo();
        try {
            txtCaminho.setText(f.getAbsolutePath());
        } catch (NullPointerException | SecurityException e) {
            txtCaminho.setText("");
        }
    }

    @FXML
    private void clkProcessar(ActionEvent event) throws IOException {
        String caminhoPDF = txtCaminho.getText();
        File f = new File(caminhoPDF);
        ArquivoPDF arqPDF = new ArquivoPDF(f);
        ArquivoCSV arqCSV;
        List<String> linhasArq;
        String nomeCotacao = "";
        
        try {
            // capturar nome do arquivo e retirar extensão
            nomeCotacao = f.getName().substring(0, f.getName().lastIndexOf("."));
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            System.out.println("não foi possível extrair o nome do arquivo selecionado.");
        }

        if (!nomeCotacao.isEmpty()) {
            arqCSV = new ArquivoCSV(nomeCotacao);
            linhasArq = arqPDF.ConvertPDFtoTXT();
            boolean deuCerto = arqCSV.EstruturarArquivo(linhasArq);
            if (deuCerto) {
                nomeCotacao = "Cotação" + (nomeCotacao.isEmpty() ? "" : " - " + nomeCotacao);

                Tela tl = new Tela("/cotacaotp/view/ItensLidos.fxml", nomeCotacao, false, true, true);
                ItensLidosController controller = tl.getLoader().getController();
                controller.setCotacao(arqCSV);
                tl.exibirTela();
            } else {
                new Alerta("Ler Cotação TetraPak", "Erro leitura", "Não consegui ler o arquivo que você selecionou.\n\n"
                        + "Verifique se você está carregando um arquivo PDF de cotação da TetraPak").exibeInformacao(Alert.AlertType.INFORMATION);
        
            }
        } else {
            new Alerta("Ler Cotação TetraPak", "Erro arquivo", 
                    "não foi possível abrir o arquivo selecionado, verifique e tente novamente").exibeInformacao(Alert.AlertType.INFORMATION);
        }
    }

}
