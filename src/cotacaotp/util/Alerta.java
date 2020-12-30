/*
 * classe para auxiliar com janeals de dialogos/alertas/etc..
 */
package cotacaotp.util;

import java.util.NoSuchElementException;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.Region;

/**
 *
 * @author abnerjp
 */
public class Alerta {

    private String titulo;
    private String cabecalho;
    private String mensagem;

    public Alerta(String titulo, String cabecalho, String mensagem) {
        this.titulo = titulo;
        this.cabecalho = cabecalho;
        this.mensagem = mensagem;
    }

    /**
     * Caixa de texto
     *
     * @return Valor informado ou null para campo vazio
     */
    public String caixaTexto(String valorPadrao) throws NullPointerException{
        String input;
        TextInputDialog dialog = new TextInputDialog(valorPadrao);
        dialog.setTitle(titulo);
        dialog.setHeaderText(cabecalho);
        dialog.setContentText(mensagem);
        try {
            input = dialog.showAndWait().get();
        } catch (NoSuchElementException e) {
            input = "";
        }
        return input;
    }

    /**
     * exibe informação na tela
     *
     * @param tipo
     */
    public void exibeInformacao(AlertType tipo) {
        Alert alert = new Alert(tipo);

        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.getButtonTypes().clear();
        alert.getButtonTypes().add(ButtonType.OK);

        alert.setTitle(titulo);
        alert.setHeaderText(cabecalho);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    public ButtonType decisaoYesNo() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.getButtonTypes().clear();
        alert.getButtonTypes().add(ButtonType.YES);
        alert.getButtonTypes().add(ButtonType.NO);

        alert.setTitle(titulo);
        alert.setHeaderText(cabecalho);
        alert.setContentText(mensagem);

        return alert.showAndWait().get();
    }

    public void setCabecalho(String cabecalho) {
        this.cabecalho = cabecalho;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public void setInformacao(String cabecalho, String mensagem) {
        this.cabecalho = cabecalho;
        this.mensagem = mensagem;
    }

}
