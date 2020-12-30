package cotacaotp.controller;

import cotacaotp.model.ArquivoCSV;
import cotacaotp.model.Item;
import cotacaotp.util.Alerta;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author abnerjp
 */
public class ItensLidosController implements Initializable {

    Item item = null;
    List<Item> listaItens;
    ArquivoCSV arqCSV = null;

    @FXML
    private Label lblCotacao;
    @FXML
    private TableView<Item> tabCotacao;
    @FXML
    private TableColumn<Item, Integer> colSequencia;
    @FXML
    private TableColumn<Item, String> colCodigoTP;
    @FXML
    private TableColumn<Item, String> colNome;
    @FXML
    private TableColumn<Item, String> colQuantidade;
    @FXML
    private TableColumn<Item, String> colValorUnitario;
    @FXML
    private TableColumn<Item, String> colValorTotal;
    @FXML
    private TableColumn<Item, String> colPercentDescontoContrato;
    @FXML
    private TableColumn<Item, String> colValorDescontoContrato;
    @FXML
    private TableColumn<Item, String> colPercentIPI;
    @FXML
    private TableColumn<Item, String> colValorIPI;
    @FXML
    private TableColumn<Item, String> colPercentICMS;
    @FXML
    private TableColumn<Item, String> colValorICMS;
    @FXML
    private TableColumn<Item, String> colDataEntrega;
    @FXML
    private Button btnSalvarCSV;
    @FXML
    private Button btnCancelar;
    @FXML
    private TextField txtDescontoContrato;
    @FXML
    private TextField txtTotalSemIPI;
    @FXML
    private TextField txtTotalIPI;
    @FXML
    private TextField txtTotalGeral;
    @FXML
    private TextField txtICMS;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @FXML
    private void clkTabView(MouseEvent event) {
        if (event.getClickCount() == 2) {
            //duplo clique
        }
    }

    @FXML
    private void clkSalvarCSV(ActionEvent event) throws IOException {
        boolean sucessoExportar;
        FileChooser telaSave = new FileChooser();

        telaSave.setTitle("Selecione a pasta");
        telaSave.setInitialFileName(arqCSV != null ? arqCSV.getNomeCotacao() : "");

        telaSave.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Valores separados por vírgulas(*.csv*)", "*.csv")
        );

        File f = telaSave.showSaveDialog(null);

        sucessoExportar = arqCSV.gerarArqCSV(f);
        if (f != null) {
            if (sucessoExportar) {
                new Alerta("Arquivo Criado", "Êxito", 
                        "foi gerado com sucesso o arquivo CSV").exibeInformacao(Alert.AlertType.INFORMATION);
            } else {
                new Alerta("Arquivo não Criado", "Erro", "Ocorreu um erro ao tentar gerar o arquivo CSV."
                        + "\n\nContate o administrador.").exibeInformacao(Alert.AlertType.INFORMATION);
            }
        }
    }

    @FXML
    private void clkCancelar(ActionEvent event) {
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }

    public void setCotacao(ArquivoCSV cotacaoEstruturada) {
        this.arqCSV = cotacaoEstruturada == null ? new ArquivoCSV() : cotacaoEstruturada;
        atualizarInterface();
    }

    private void atualizarInterface() {
        txtTotalSemIPI.setText(arqCSV.getVlrTotalSemIPIStr());
        txtTotalIPI.setText(arqCSV.getVlrTotalIPIStr());
        txtICMS.setText(arqCSV.getVlrTotalICMSStr());
        txtDescontoContrato.setText(arqCSV.getVlrDescontoContratoStr());
        txtTotalGeral.setText(arqCSV.getVlrTotalComIPIStr());

        lblCotacao.setText(arqCSV.getNomeCotacao().isEmpty() ? "Cotação" : "Cotação - " + arqCSV.getNomeCotacao());
        carregarTabela(arqCSV.getListaItens());
    }

    private void carregarTabela(List<Item> lista) {
        ObservableList<Item> obsLista;
        tabCotacao.getItems().clear();

        colSequencia.setCellValueFactory(new PropertyValueFactory<>("sequencia"));
        colCodigoTP.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colQuantidade.setCellValueFactory(obj -> new SimpleStringProperty(obj.getValue().getQuantidadeStr()));
        colValorUnitario.setCellValueFactory(obj -> new SimpleStringProperty(obj.getValue().getValorUnitarioStr()));
        colValorTotal.setCellValueFactory(obj -> new SimpleStringProperty(obj.getValue().getValorTotalStr()));
        colPercentDescontoContrato.setCellValueFactory(obj -> new SimpleStringProperty(obj.getValue().getPorcentDescontoContratoStr()));
        colValorDescontoContrato.setCellValueFactory(obj -> new SimpleStringProperty(obj.getValue().getValorDescontoContratoStr()));
        colPercentIPI.setCellValueFactory(obj -> new SimpleStringProperty(obj.getValue().getPorcentIPIStr()));
        colValorIPI.setCellValueFactory(obj -> new SimpleStringProperty(obj.getValue().getValorIPIStr()));
        colPercentICMS.setCellValueFactory(obj -> new SimpleStringProperty(obj.getValue().getPorcentICMSStr()));
        colValorICMS.setCellValueFactory(obj -> new SimpleStringProperty(obj.getValue().getValorICMSStr()));
        colDataEntrega.setCellValueFactory(new PropertyValueFactory<>("dataEntrega"));

        obsLista = FXCollections.observableArrayList(lista);

        tabCotacao.setItems(obsLista);
        //tabCotacao.getSelectionModel().select(-1);
    }
}
