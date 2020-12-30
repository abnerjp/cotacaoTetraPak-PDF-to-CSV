package cotacaotp.model;

import cotacaotp.util.Encoding;
import java.text.NumberFormat;
import java.util.Locale;

/**
 *
 * @author abnerjp
 */
public class Item {

    private int sequencia;
    private String codigo;
    private String nome;
    private double quantidade;
    private double valorUnitario;
    private String dataEntrega;
    private double porcentDescontoContrato;
    private double valorDescontoContrato;
    private String undMedida;
    private double porcentICMS;
    private double valorICMS;
    private double porcentIPI;
    private double valorIPI;
    private final Locale localBR = new Locale("pt", "BR");
    private final NumberFormat numBR = NumberFormat.getNumberInstance(localBR);
    private final NumberFormat percent = NumberFormat.getPercentInstance(localBR);

    public Item() {
        this.sequencia = 0;
        this.codigo = "";
        this.nome = "";
        this.quantidade = 0;
        this.valorUnitario = 0;
        this.dataEntrega = "";
        this.porcentDescontoContrato = 0;
        this.valorDescontoContrato = 0;
        this.undMedida = "";
        this.porcentICMS = 0;
        this.valorICMS = 0;
        this.porcentIPI = 0;
        this.valorIPI = 0;
    }

    public int getSequencia() {
        return sequencia;
    }

    public void setSequencia(int sequencia) {
        this.sequencia = sequencia;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getQuantidade() {
        return quantidade;
    }

    public String getQuantidadeStr() {
        return String.valueOf(numBR.format(getQuantidade()));
    }

    public void setQuantidade(double quantidade) {
        this.quantidade = quantidade;
    }

    public double getValorUnitario() {
        return valorUnitario;
    }

    public String getValorUnitarioStr() {
        return String.valueOf(numBR.format(getValorUnitario()));
    }

    public void setValorUnitario(double valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

    public double getValorTotal() {
        return getQuantidade() * getValorUnitario();
    }

    public String getValorTotalStr() {
        return String.valueOf(numBR.format(getValorTotal()));
    }

    public String getDataEntrega() {
        return dataEntrega;
    }

    public void setDataEntrega(String dataEntrega) {
        this.dataEntrega = dataEntrega;
    }

    public double getPorcentDescontoContrato() {
        return porcentDescontoContrato;
    }

    public String getPorcentDescontoContratoStr() {
        return String.valueOf(numBR.format(getPorcentDescontoContrato())) + "%";
    }

    public void setPorcentDescontoContrato(double porcentDescontoContrato) {
        this.porcentDescontoContrato = porcentDescontoContrato;
    }

    public double getValorDescontoContrato() {
        return valorDescontoContrato;
    }

    public String getValorDescontoContratoStr() {
        return String.valueOf(numBR.format(getValorDescontoContrato()));
    }

    public void setValorDescontoContrato(double valorDescontoContrato) {
        this.valorDescontoContrato = valorDescontoContrato;
    }

    public String getUndMedida() {
        return undMedida;
    }

    public void setUndMedida(String undMedida) {
        this.undMedida = undMedida;
    }

    public double getPorcentICMS() {
        return porcentICMS;
    }

    public String getPorcentICMSStr() {
        return String.valueOf(numBR.format(getPorcentICMS())) + "%";
    }

    public void setPorcentICMS(double porcentICMS) {
        this.porcentICMS = porcentICMS;
    }

    public double getValorICMS() {
        return valorICMS;
    }

    public String getValorICMSStr() {
        return String.valueOf(numBR.format(getValorICMS()));
    }

    public void setValorICMS(double valorICMS) {
        this.valorICMS = valorICMS;
    }

    public double getPorcentIPI() {
        return porcentIPI;
    }

    public String getPorcentIPIStr() {
        return String.valueOf(numBR.format(getPorcentIPI())) + "%";
    }

    public void setPorcentIPI(double porcentIPI) {
        this.porcentIPI = porcentIPI;
    }

    public double getValorIPI() {
        return valorIPI;
    }

    public String getValorIPIStr() {
        return String.valueOf(numBR.format(getValorIPI()));
    }

    public void setValorIPI(double valorIPI) {
        this.valorIPI = valorIPI;
    }

    public static String cabecalho() {
        return "SEQUENCIA;CODIGO;ITEM;UND;QTDE;VLR_UND;VLR_TOTAL;(%)_DESCONTO_CONTRATO;VLR_DESCONTO_CONTRATO;(%)_IPI;VLR_IPI;(%)_ICMS;VLR_ICMS;DATA_ENTREGA";
    }

    @Override
    public String toString() {
        String texto = getSequencia() + ";" + Encoding.removeEspacoInterno(getCodigo()) + ";" + getNome() + ";" + getUndMedida() + ";" + numBR.format(getQuantidade()) + ";"
                + numBR.format(getValorUnitario()) + ";" + numBR.format(getValorTotal()) + ";"
                + percent.format(getPorcentDescontoContrato() / 100) + ";" + numBR.format(getValorDescontoContrato())
                + ";" + percent.format(getPorcentIPI() / 100) + ";" + numBR.format(getValorIPI()) + ";"
                + percent.format(getPorcentICMS() / 100) + ";" + numBR.format(getValorICMS()) + ";" + getDataEntrega().replace(".", "/");

        return Encoding.removeAccents(texto);
    }

}
