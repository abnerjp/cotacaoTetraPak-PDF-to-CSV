package cotacaotp.model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 *
 * @author abnerjp
 */
public class ArquivoCSV {

    private final String nomeCotacao;
    private final List<Item> itens;
    private final Locale localBR = new Locale("pt", "BR");
    private final NumberFormat numBR = NumberFormat.getCurrencyInstance(localBR);

    public ArquivoCSV(String nomeCotacao) {
        this.nomeCotacao = nomeCotacao;
        itens = new ArrayList<>();
    }

    public ArquivoCSV() {
        this("");
    }

    public String getNomeCotacao() {
        return (this.nomeCotacao != null && !this.nomeCotacao.isEmpty()) ? this.nomeCotacao : "";
    }

    public List<Item> getListaItens() {
        return !isEmpty() ? this.itens : new ArrayList<>();
    }

    public boolean EstruturarArquivo(List<String> cotacao) {
        Estruturar(cotacao);
        return !isEmpty();
    }

    public Double getVlrTotalIPI() {
        Double total = 0.0;
        if (!isEmpty()) {
            for (Item item : itens) {
                total += item.getValorIPI();
            }
        }
        return total;
    }

    public String getVlrTotalIPIStr() {
        return String.valueOf(numBR.format(getVlrTotalIPI()));
    }

    public Double getVlrTotalSemIPI() {
        Double total = 0.0;
        if (!isEmpty()) {
            total = itens.stream().map((item) -> item.getValorTotal()).reduce(total, (accumulator, _item) -> accumulator + _item);
        }
        return total;
    }

    public String getVlrTotalSemIPIStr() {
        return String.valueOf(numBR.format(getVlrTotalSemIPI()));
    }

    public Double getVlrTotalComIPI() {
        return getVlrTotalSemIPI() + getVlrTotalIPI();
    }

    public String getVlrTotalComIPIStr() {
        return String.valueOf(numBR.format(getVlrTotalComIPI()));
    }

    public Double getVlrDescontoContrato() {
        Double total = 0.0;
        if (!isEmpty()) {
            total = this.itens.stream().map((item) -> item.getValorDescontoContrato()).reduce(total, (accumulator, _item) -> accumulator + _item);
        }
        return total;
    }

    public String getVlrDescontoContratoStr() {
        return String.valueOf(numBR.format(getVlrDescontoContrato()));
    }

    public Double getVlrTotalICMS() {
        Double total = 0.0;
        if (!isEmpty()) {
            total = this.itens.stream().map((item) -> item.getValorICMS()).reduce(total, (accumulator, _item) -> accumulator + _item);
        }
        return total;
    }

    public String getVlrTotalICMSStr() {
        return String.valueOf(numBR.format(getVlrTotalICMS()));
    }

    private int getInicio(List<String> cotacao) {
        int posLinha;
        String linha;
        boolean inicio = false;
        char sequenciaInicio = 2;
        String pontoDePartida = "___________________________________________________";

        for (posLinha = 0; posLinha < cotacao.size() && !inicio; posLinha++) {
            linha = cotacao.get(posLinha).toLowerCase();
            if (!inicio && linha.contains(pontoDePartida)) {
                sequenciaInicio--;
                if (sequenciaInicio == 0) {
                    inicio = true;
                }
            }
        }
        return inicio ? posLinha : -1;
    }

    private void Estruturar(List<String> cotacao) {
        String linha;
        boolean fimItens = false;
        boolean sequenciaIniciada = false;
        boolean isLinhaDaSequencia;
        int i = getInicio(cotacao);
        Item item = new Item();

        if (i != -1) {
            /*encontrou o inicio dos itens*/
            while (i < cotacao.size() && !fimItens) {
                linha = cotacao.get(i).toLowerCase();
                try {
                    String[] verificaSequencia = linha.substring(0, 6).split(" ");
                    if (verificaSequencia.length > 1 && verificaSequencia[1].trim().charAt(0) < 58 && verificaSequencia[1].trim().charAt(0) > 47) {
                        int sequencia = Integer.valueOf(verificaSequencia[0]);
                        if (sequenciaIniciada) {
                            if (item.getSequencia() > 0) {
                                this.itens.add(item);
                            }
                        }
                        isLinhaDaSequencia = true;
                        item = new Item();
                        item.setSequencia(sequencia);
                        sequenciaIniciada = true;
                        System.out.println("tam vet: "+ verificaSequencia.length +", seq >>> " + linha);
                    } else {
                        isLinhaDaSequencia = false;
                    }
                } catch (IndexOutOfBoundsException | NumberFormatException e) {
                    /*não é a primeira linha (linha da sequencia)*/
                    isLinhaDaSequencia = false;
                }
                if (isLinhaDaSequencia) {
                    /*é a primeira linha (linha da sequencia)*/
                    //código tetra pak
                    try {
                        int posInicial = String.valueOf(item.getSequencia()).length();
                        int posFinal = getPosicao(linha, 58);

                        if (posFinal > posInicial) {
                            String codigoTP = linha.substring(posInicial, posFinal).trim();
                            item.setCodigo(codigoTP);
                        }
                    } catch (IndexOutOfBoundsException e) {
                    }

                    //nome item
                    try {
                        String[] nomeItem = linha.substring(linha.indexOf(item.getCodigo()) + item.getCodigo().length()).split(" ");
                        String nome = "";
                        for (int pos = 0; pos < nomeItem.length - 5; pos++) {
                            nome += " " + nomeItem[pos];
                        }
                        nome += "...";
                        nome = nome.trim();

                        item.setNome(nome);
                    } catch (IndexOutOfBoundsException e) {
                        item.setNome("item " + String.valueOf(item.getSequencia()));
                    }

                    String[] valores = linha.split(" ");
                    int tamVetor = valores.length;
                    if (tamVetor > 0) {
                        try {
                            String dataEntrega = valores[tamVetor - 1].trim();
                            //System.out.println("    dt entrega>>> " + valores[tamVetor - 1].trim());
                            item.setDataEntrega(dataEntrega);

                            String valorUnd = valores[tamVetor - 3].replace(".", "").replace(",", ".").trim();
                            //System.out.println("    vl unitario>>> " + valores[tamVetor - 3].replace(".", "").replace(",", ".").trim());
                            item.setValorUnitario(Double.valueOf(valorUnd));

                            String undMedida = valores[tamVetor - 4].trim();
                            //System.out.println("    und medida>>>  " + valores[tamVetor - 4].trim());
                            item.setUndMedida(undMedida);

                            String qtde = valores[tamVetor - 5].replace(".", "").replace(",", ".").trim();
                            //System.out.println("    quantidade>>>  " + valores[tamVetor - 5].replace(".", "").replace(",", ".").trim());
                            item.setQuantidade(Double.valueOf(qtde));

                            //} catch (IndexOutOfBoundsException e) {
                        } catch (IndexOutOfBoundsException | NumberFormatException e) {
                            //erro ao ler
                            item = new Item();
                        }
                    }
                } else {
                    /*não é a primeira linha (linha da sequencia)*/
                    if (!(linha.replace(" ", "").contains("valortotal") || linha.replace(" ", "").contains("valortotal"))) {
                        if (linha.contains("desconto - contrato")) {
                            String[] quebra = linha.split(" ");
                            try {
                                double vlr;

                                vlr = Double.valueOf(quebra[quebra.length - 1].replace("-", "").replace(".", "").replace(",", ".").trim());
                                item.setValorDescontoContrato(vlr);

                                vlr = Double.valueOf(quebra[quebra.length - 3].replace("-", "").replace(".", "").replace(",", ".").trim());
                                item.setPorcentDescontoContrato(vlr);
                            } catch (NumberFormatException | IndexOutOfBoundsException e) {

                            }
                        } else if (linha.contains("ipi ")) {
                            String[] quebra = linha.split(" ");
                            try {
                                double vlr;

                                vlr = Double.valueOf(quebra[quebra.length - 1].replace(".", "").replace(",", ".").trim());
                                item.setValorIPI(vlr);

                                vlr = Double.valueOf(quebra[quebra.length - 3].replace(".", "").replace(",", ".").trim());
                                item.setPorcentIPI(vlr);
                            } catch (NumberFormatException | IndexOutOfBoundsException e) {

                            }

                        } else if (linha.contains("icms ")) {
                            String[] quebra = linha.split(" ");
                            try {
                                double vlr;

                                vlr = Double.valueOf(quebra[quebra.length - 1].replace(".", "").replace(",", ".").trim());
                                item.setValorICMS(vlr);

                                vlr = Double.valueOf(quebra[quebra.length - 3].replace(".", "").replace(",", ".").trim());
                                item.setPorcentICMS(vlr);
                            } catch (NumberFormatException | IndexOutOfBoundsException e) {

                            }
                        }
                    }
                }
                i++;
                try {
                    String linhaFinal = cotacao.get(i).toLowerCase().replace(" ", "");
                    if (linhaFinal.contains("descontoparapedidoplanejado(%)") || linhaFinal.contains("valortotaldoicms(%)")
                            || linhaFinal.contains("valortotaldoipi") || linhaFinal.contains("valortotaldosprodutos") || linhaFinal.contains("valortotaldoicms(%)")) {
                        fimItens = true;
                    }

                    if (fimItens) {
                        //System.out.println("PAROUUUUUU---" + cotacao.get(i).toLowerCase().replace(" ", ""));
                        if (item.getSequencia() > 0) {
                            this.itens.add(item);
                        }
                    }
                } catch (IndexOutOfBoundsException e) {
                    // tentativa de acesso com posicao de "i" maior que a cotação
                }
            }
        }
    }

    private int getPosicao(String texto, int valorASCII) {
        boolean encontrou = false;
        int i = 0;
        while (i < texto.length() && texto.charAt(i) < valorASCII) {
            i++;
            encontrou = true;
        }
        return encontrou ? i : -1;
    }

    private String extrairData(String texto) {
        String data;
        try {
            data = texto.substring(texto.length() - 10).trim();
        } catch (IndexOutOfBoundsException e) {
            data = "";
        }
        return data;
    }

    public void exibeArquivo() {
        if (!isEmpty()) {
            this.itens.forEach((it) -> {
                System.out.println(it);
            });
        }
    }

    public boolean isEmpty() {
        return itens == null || itens.isEmpty();
    }

    public boolean gerarArqCSV(File f) {
        boolean exportou = false;
        if (f != null && !isEmpty()) {
            try {
                String nomeArquivo = f.getCanonicalPath().toLowerCase();
                try (FileOutputStream arquivo = new FileOutputStream(nomeArquivo)) {
                    try (PrintWriter pw = new PrintWriter(arquivo)) {
                        pw.println(Item.cabecalho());
                        this.itens.forEach((item) -> {
                            pw.println(item);
                        });
                        exportou = true;
                    }
                }
            } catch (IOException | SecurityException e) {
                exportou = false;
            }
        }
        return exportou;
    }
}
