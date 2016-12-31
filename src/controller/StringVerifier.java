package controller;

import model.MachineNumberReader;
import model.ReadWriteException;
import model.XmlHandler;

/**
 * Verifiziert einen String ob er den Anforderungen gemäss Beschreibung entspricht
 *
 * Created by Nett on 29.12.2016.
 * @author Nett
 */
public class StringVerifier {

    private MachineNumberReader reader = null;

    public StringVerifier() {
        reader = new MachineNumberReader();

    }

    // Konstruktor um zum Testen einen Fake-Reader zu injizieren
    public StringVerifier(MachineNumberReader reader) {
        this.reader = reader;
    }

    /**
     * Gibt die Maschinennummer zurück wenn sie gültig ist,
     * sonst wir eine ReadWriteException geworfen.
     *
     * @return machNumber
     * @throws ReadWriteException
     */
    public String getVerifiedMachineNumber()throws ReadWriteException {
        String machNumber = reader.readMachineNumber();
        if(!isMachineNumberValid(machNumber)){
            ReadWriteException rwEx = new ReadWriteException(machNumber + " ist keine gültige Maschinennnummer");
            throw rwEx;
        }
        return machNumber;
    }

    /**
     * Prüft den von der Methode XmlHandler.readPathFromPathConfig(searchPath)
     * zurückgegebene String auf null oder Empty und löst im Fehlerfall eine
     * ReadWriteException zurück
     *
     * @param searchPath
     * @return
     * @throws ReadWriteException
     */
    public String getVerfiedPathFromPathConfig(String searchPath)throws ReadWriteException{
        String path = XmlHandler.readPathFromPathConfig(searchPath);
        if(isStringNullOrEmpty(path)){
            ReadWriteException rwEx = new ReadWriteException("Der im PathConfig.xml mit CharactName " + searchPath + " definierte Pfad ist nicht gültig: " + path);
            throw rwEx;
        }
        return path;
    }

    /**
     * Verifiziert einen String ober er folgenden Anforderungen entspricht.
     *
     * Die Maschinennummer darf weder Null noch Empty sein. Die Länge ist
     * genau 10, und an der Stelle 0 und 5 muss ein Buchstabe sein.
     * Alle anderen Ziffern müssen Zahlen sein.
     *
     * @param machNumber
     * @return true wenn valid
     */
    private boolean isMachineNumberValid(String machNumber){
        if(isStringNullOrEmpty(machNumber)){
            return false;
        }
        if(machNumber.length() != 10){
            return false;
        }
        if(!Character.isLetter(machNumber.charAt(0))
                || !Character.isLetter(machNumber.charAt(5))){
            return false;
        }
        if(!hasSubstringNumbersOnly(machNumber,1,4)){
            return false;
        }
        if(!hasSubstringNumbersOnly(machNumber,6,machNumber.length())){
            return false;
        }
        return true;
    }

    /**
     * Prüft ob der String Null oder Empty ist.
     *
     * @param stringIn
     * @return true wenn null oder Empty
     */
    private boolean isStringNullOrEmpty(String stringIn){
        if(stringIn == null){
            return true;
        }
        if(stringIn.isEmpty()){
            return true;
        }
        return false;
    }

    /**
     * Prüft einen String ob er vom Index start bis end nur aus Zahlen besteht.
     *
     * @param machNumber zu prüfender String
     * @param start
     * @param end
     * @return true wenn der String vom Index start bis end nur aus Zahlen besteht
     */
    private boolean hasSubstringNumbersOnly(String machNumber, int start, int end){
        String shortName = machNumber.substring(start, end);
        for (char c : shortName.toCharArray()){
            if(!Character.isDigit(c)){
                return  false;
            }
        }
        return true;
    }

}
