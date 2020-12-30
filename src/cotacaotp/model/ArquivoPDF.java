package cotacaotp.model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

/**
 *
 * @author abnerjp
 * 
 * classe para ler o arquivo PDF (cotação da Tetra Pak)
 */
public class ArquivoPDF {

    private File arquivo;
    String text;
    private final List<String> linhasArquivo;

    public ArquivoPDF(File arq) {
        this.text = "";
        this.linhasArquivo = new ArrayList<>();
        setCaminhoArquivo(arq);
        lerArquivoPDF();
    }

    private void setCaminhoArquivo(File caminho) {
        this.arquivo = caminho;
    }

    private boolean isArquivoValido() {
        return this.arquivo != null && this.arquivo.exists();
    }

    private void lerArquivoPDF() {
        if (isArquivoValido()) {
            try {
                try (PDDocument document = PDDocument.load(this.arquivo)) { // ao fim é executado o método document.close()
                    PDFTextStripper pdfStripper = new PDFTextStripper();
                    text = pdfStripper.getText(document);
                }
            } catch (NullPointerException | SecurityException | IOException e) {
                text = "";
            }
        }
    }

    public String getTextPDF() {
        return text;
    }

    public List<String> ConvertPDFtoTXT() {
        if (!this.text.isEmpty()) {
            if (!this.text.isEmpty()) {
                String[] vetTexto = this.text.split("\n");
                int i = 0;
                while (i < vetTexto.length) {
                    linhasArquivo.add(vetTexto[i]);
                    i++;
                }
            }
        }
        return linhasArquivo;
    }
    
    public void Exibir() {
        linhasArquivo.forEach((string) -> {
            System.out.println(string);
        });
    }
}
