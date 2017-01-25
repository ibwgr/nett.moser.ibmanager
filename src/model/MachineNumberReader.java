package model;

import controller.StringVerifier;

import java.io.*;

/**
 * Die Klasse liest die Maschinennummer aus einem im PathConfig.xml definierten File
 *
 * Created by Nett on 28.12.2016.
 * @author Nett
 */
public class MachineNumberReader {

    /**
     * Gibt die Machinennummer zurueck die aus dem File gelesen wird,
     * dessen Pfad im PathConfig.xml unter dem Namen Maschinennummer definiert ist.
     * Wirft bei FileNotFoundException oder IOException eine ReadWriteException
     * mit einer entsprechenden Meldung.
     *
     * @return machNumber
     * @throws ReadWriteException
     */
    public static String readMachineNumber()throws ReadWriteException{
        String path = StringVerifier.getVerfiedPathFromPathConfig("Maschinennummer");
        File datei = new File(path);
        FileReader reader = null;
        BufferedReader bufReader = null;
        String machNumber = null;
        try {
            reader = new FileReader(datei);
            bufReader = new BufferedReader(reader);
            machNumber = bufReader.readLine();
        } catch (FileNotFoundException e) {
            ReadWriteException rwEx = new ReadWriteException("Pfad oder Datei" + path +" konnte nicht gefunden werden!\n"+ e.getMessage());
            throw rwEx;
        } catch (IOException e) {
            ReadWriteException rwEx = new ReadWriteException("Datei " + path + " konnte nicht gelesen werden!\n"+ e.getMessage());
            throw rwEx;
        }finally {
            try {
                if(reader != null) {
                    reader.close();
                }
            } catch (IOException e) {}
            if(bufReader != null){
                try {
                    bufReader.close();
                } catch (IOException e) {}
            }
        }
        return machNumber;
    }

}
