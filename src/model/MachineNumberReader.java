package model;

import java.io.*;

/**
 * Die Klasse liest die Maschinennummer aus dem File das in der Variable path
 * definiert ist die im Konstruktor übergeben wird.
 *
 * Created by Nett on 28.12.2016.
 * @author Nett
 */
public class MachineNumberReader {

    private String path;

    public MachineNumberReader(String path) {
        this.path = path;
    }

    /**
     * Gibt die Machinennummer zurück die aus dem File datei ausgelsen wird zurück.
     * Wirft bei FileNotFoundException oder IOException eine ReadWriteException
     * mit einer entsprechenden Meldung.
     *
     * @return machNumber
     * @throws ReadWriteException
     */
    public String readMachineNumber()throws ReadWriteException{
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
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(bufReader != null){
                try {
                    bufReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return machNumber;
    }

}
