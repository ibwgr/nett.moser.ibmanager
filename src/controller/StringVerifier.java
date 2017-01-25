package controller;

import model.MachineNumberReader;
import model.ReadWriteException;
import model.XmlHandler;

/**
 * Verifiziert einen String ob er den Anforderungen gemaess Beschreibung entspricht
 *
 * Created by Nett on 29.12.2016.
 * @author Nett
 */
public class StringVerifier {

    /**
     * Gibt die Maschinennummer zurueck wenn sie gueltig ist,
     * sonst wird eine ReadWriteException mit entsprechender Meldung geworfen.
     *
     * @return machNumber
     * @throws ReadWriteException
     */
    public static String getVerifiedMachineNumber()throws ReadWriteException {
        String machNumber = MachineNumberReader.readMachineNumber();
        if(!isMachineNumberValid(machNumber)){
            ReadWriteException rwEx = new ReadWriteException(machNumber + " ist keine gültige Maschinennnummer");
            throw rwEx;
        }
        return machNumber;
    }

    /**
     * Prueft den von der Methode XmlHandler.readPathFromPathConfig(searchPath)
     * zurueckgegebene String auf null oder Empty und wirft im Fehlerfall eine
     * ReadWriteException
     *
     * @param searchPath
     * @return
     * @throws ReadWriteException
     */
    public static String getVerfiedPathFromPathConfig(String searchPath)throws ReadWriteException{
        String path = XmlHandler.readPathFromPathConfig(searchPath);
        if(isStringNullOrEmpty(path)){
            ReadWriteException rwEx = new ReadWriteException("Der im PathConfig.xml mit CharactName " + searchPath + " definierte Pfad ist nicht gültig: " + path);
            throw rwEx;
        }
        return path;
    }

    /**
     * Verifiziert den String machNumber ober er folgenden Anforderungen entspricht.
     *
     * Die Maschinennummer darf weder Null noch Empty sein. Die Laenge ist
     * genau 10, und an der Stelle 0 und 5 muss ein Buchstabe sein.
     * Alle anderen Ziffern muessen Zahlen sein.
     *
     * @param machNumber Maschinennummer
     * @return true wenn valid
     */
    private static boolean isMachineNumberValid(String machNumber){
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
     * Prueft ob der String Null oder Empty ist.
     *
     * @param stringIn
     * @return true wenn null oder Empty
     */
    private static boolean isStringNullOrEmpty(String stringIn){
        if(stringIn == null){
            return true;
        }
        if(stringIn.isEmpty()){
            return true;
        }
        return false;
    }

    /**
     * Prueft einen String ob er vom Index start bis end nur aus Zahlen besteht.
     *
     * @param machNumber zu pruefender String
     * @param start
     * @param end
     * @return true wenn der String vom Index start bis end nur aus Zahlen besteht
     */
    private static boolean hasSubstringNumbersOnly(String machNumber, int start, int end){
        String shortName = machNumber.substring(start, end);
        for (char c : shortName.toCharArray()){
            if(!Character.isDigit(c)){
                return  false;
            }
        }
        return true;
    }

}
