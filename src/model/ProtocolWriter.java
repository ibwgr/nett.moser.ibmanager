package model;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import controller.StringVerifier;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Erstellt das Protokoll mit Maschinennummer, Datum, Kurzzeichen des Prüfers.
 * Pro und ausgeführte Anwendung wird ein Eintrag mit Statusmeldung gemacht.
 * Das Protokoll wird in dem im PathConfig.xml definierten Pfad gespeichert.
 *
 * Created by Nett on 01.01.2017.
 * @author Nett
 */
public class ProtocolWriter {

    private StringVerifier verifier = null;
    private String machineNumer = null;

    public ProtocolWriter() {
        verifier = new StringVerifier();
    }

    /**
     * Erstellt den Speicherpfad des Protokolls
     *
     * @return
     * @throws ReadWriteException
     */
    private String createProtocolPath()throws ReadWriteException{
        machineNumer = verifier.getVerifiedMachineNumber();
        return verifier.getVerfiedPathFromPathConfig("Protokoll") + machineNumer + ".pdf";
    }


    /**
     * Erstellt das Prüfprotokoll aus dem Inhalt der Liste applNameList
     *
     * @param applNameList
     * @throws ProtocolWriteException
     */
    public void createPdf(List<String> applNameList) throws ProtocolWriteException {
        Document document = new Document();
        String path = null;

        try {
            path = createProtocolPath();
            PdfWriter writer = PdfWriter.getInstance(document,
                    new FileOutputStream(path));
            document.open();
            PdfContentByte cb = writer.getDirectContent();
            BaseFont bf = BaseFont.createFont();
            cb.beginText();
            cb.setFontAndSize(bf, 12);
            cb.moveText(20, PageSize.A4.getHeight() - 50);
            cb.showText("Schaltschrank-IB:");
            cb.moveText(120, 0);
            cb.showText(machineNumer);
            cb.moveText(240, 0);
            cb.showText(getDateTime());
            cb.moveText(-360, -50);

            for (String applName : applNameList) {
                cb.newlineShowText(applName);
                cb.moveText(0, -50);
            }
            cb.endText();
        } catch (IOException ex) {
            ProtocolWriteException rwEx = new ProtocolWriteException("Fehler beim Erstellen des Protokolls: " + path + "\n" + ex.getMessage());
            throw rwEx;
        } catch (DocumentException ex) {
            ProtocolWriteException rwEx = new ProtocolWriteException("Fehler beim Erstellen des Protokolls: " + path + "\n" + ex.getMessage());
            throw rwEx;
        } catch (ReadWriteException ex) {
            ProtocolWriteException rwEx = new ProtocolWriteException("Fehler beim Erstellen des Protokoll-Pfad: " + path + "\n" + ex.getMessage());
            throw rwEx;
        } finally {
            document.close();
        }
    }

    private String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

}
