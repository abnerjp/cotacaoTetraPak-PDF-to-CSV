package cotacaotp.model;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author abnerjp
 */
public final class ArquivoTXT {

    private File arquivo;
    private List<String> linhasArquivo;
    private final Charset cs = StandardCharsets.UTF_8;

    public ArquivoTXT(String caminhoArquivo) {
        this.linhasArquivo = new ArrayList<>();
        setCaminhoArquivo(caminhoArquivo);
    }

    public void setCaminhoArquivo(String caminho) {
        this.arquivo = new File(caminho);
        try {
            if (isArquivoValido()) {
                lerArquivoTXT();
            }
        } catch (NullPointerException e) {

        }
    }

    public String getNomeArquivo() {
        return isArquivoValido() ? this.arquivo.getName() : "";
    }
    
    public List<String> getLinhasCotacao() {
        return this.linhasArquivo;
    }

    private boolean isArquivoValido() {
        return this.arquivo != null && this.arquivo.exists();
    }

    private void lerArquivoTXT() {
        if (isArquivoValido()) {
            try {
                this.linhasArquivo = Files.readAllLines(Paths.get(arquivo.getAbsolutePath()), this.cs);
            } catch (IOException ex) {
                System.out.println("erro ao ler o arquivo");
            }
        } else {
            System.out.println("arquivo não é válido");
        }
    }
}
