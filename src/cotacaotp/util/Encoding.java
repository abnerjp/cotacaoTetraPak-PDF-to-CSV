package cotacaotp.util;

import java.text.Normalizer;

/**
 *
 * @author abnerjp
 */
public class Encoding {

    public static String convertUTF8toISO(String str) {
        String ret;
        try {

            ret = new String(str.getBytes("windows-1252"), "UTF-8");
        } catch (java.io.UnsupportedEncodingException e) {
            ret = "";
        }
        return ret;
    }

    public static String removeAccents(String str) {
        str = Normalizer.normalize(str, Normalizer.Form.NFD);
        str = str.replaceAll("[^\\p{ASCII}]", "");
        return str;
    }

    public static String removeEspacoInterno(String str) {
        String strAux;
        try {
            strAux = str.replaceAll(" ", "");
        } catch (NullPointerException e) {
            strAux = "";
        }
        return strAux;
    }
}
