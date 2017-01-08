package controller;

import model.ProtocolWriteException;
import model.ProtocolWriter;
import model.ReadWriteException;
import model.XmlHandler;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * Die Klasse beschreibt den sequenziellen Ablauf der Applikation.
 * Als Observable benachrichtigt sie die GUI/Konsole über den Zustand der
 * aktuell ausgeführten externen Applikation
 *
 * Created by Nett on 29.12.2016.
 * @author Nett
 */
public class SequenceManager extends Observable implements Runnable{

    private String stepConfigApplName = null;
    private List<String> applNameList = null;

    public SequenceManager() {
        applNameList = new ArrayList<>();
    }

    /**
     * Ist die Einstiegsmethode in die Applikation und wird durch die GUI/Konsole durch Thread.start()
     * aufgerufen. Sie iteriert in iterateOverStepConditions() über alle Steps, informiert sofern kein
     * Fehler auftritt die registrierten Observer über die erfolgreiche Ausführung und stösst die Erzeugung des Prokokolls an.
     *
     * Wirft eine ReadWriteException an die GUI/Konsole damit sie da behandelt werden kann
     *
     * @throws ReadWriteException
     */
    public void startIbManager(){
        String sucess = "IB-Manager erfolgfreich beendet!";
        try {
            iterateOverStepConditions();
            informObserver(createApplStatus(sucess,0,AppInfo.TERMINATED));
            //Protokoll als erfolgreich beendet markieren
            addMessageToProtocol(sucess);
        }catch (ReadWriteException ex){
            //Error an die Observer weitergeben
            informObserver(createApplStatus(ex.getMessage(),0,AppInfo.ERROR));
        }finally {
            try {
                //Protokoll erzeugen
                ProtocolWriter.createPdf(applNameList);
            }catch (ProtocolWriteException e){
                informObserver(createApplStatus(e.getMessage(),0,AppInfo.ERROR));
            }
        }
    }

    /**
     * Liest die NodeList der StepConfigurations.xml und iteriert über dessen Step-Elemente.
     * In Abhängigkeit des Bedingungstyp wird die im StepConfigurations.xml definierte externe
     * Applikation aufgerufen
     *
     * @throws ReadWriteException
     */
    private void iterateOverStepConditions() throws  ReadWriteException{
        //NodeList aus StepConfigurations.xml einlesen
        NodeList nListStepConf = XmlHandler.getNodeListStepConfigurations();
        for (int i = 0; i < nListStepConf.getLength(); i++) {
            Node nodeStepConf = nListStepConf.item(i);
            if (nodeStepConf.getNodeType() == Node.ELEMENT_NODE) {
                Element stepConfigElement = (Element) nodeStepConf;
                //CharactName stepConfigCharactName der Bedingung aus StepConfigurations.xml lesen
                String stepConfigCharactName = stepConfigElement.getElementsByTagName("CharactName").item(0).getTextContent();
                //Mit stepConfigCharactName des entsprechende Element aus dem Betriebsauftrag holen
                Element orderElement = XmlHandler.getElementFromOrder(stepConfigCharactName);
                //Den Bedingungstyp StepConfigurations.xml lesen BA = Betriebsauftrag EX = ExistFile
                String stepConfigCondType = stepConfigElement.getElementsByTagName("CondType").item(0).getTextContent();
                if (stepConfigCondType.equals("BA")) {
                    if (checkBaStepCondition(stepConfigElement, orderElement)) {
                        //Erstellt die Info betreffend Pfad und Befehlszeilenargumente
                        List<String> sArrayList = createAppPathInfo(stepConfigElement);
                        //Aufruf der externen Applikation
                        runExternalApplication(sArrayList, i);
                    }
                }
                else if (stepConfigCondType.equals("EX")) {
                    addMessageToProtocol(stepConfigApplName);
                }
            }
        }
    }


    /**
     * Liest den Namen, den Pfad und die Befehlszeilenargumente der aktuellen
     * externen Applikation und erstellt daraus eine List
     *
     * @param stepConfigElement
     * @return
     */
    private List createAppPathInfo(Element stepConfigElement) {

        List<String> sArrayList = new ArrayList<>();
        //Optionale Pre-Befehlszeilenargumente lesen
        String stepConfigPreArgs = stepConfigElement.getElementsByTagName("PreArgs").item(0).getTextContent();
        //Pre-Befehlszeilenargumente bei Leerschlag splitten und der Liste hinzufügen
        String[]preArgs = stepConfigPreArgs.split(" ");
        for(String arg : preArgs){
            if(!arg.isEmpty()){
                sArrayList.add(arg);
            }
        }
        //Name und Pfad der Applikation lesen und der Liste hinzufügen
        stepConfigApplName = stepConfigElement.getElementsByTagName("ApplName").item(0).getTextContent();
        sArrayList.add(stepConfigElement.getElementsByTagName("ApplPath").item(0).getTextContent() + stepConfigApplName);
        //Optionale Post-Befehlszeilenargumente lesen
        String stepConfigPostArgs = stepConfigElement.getElementsByTagName("PostArgs").item(0).getTextContent();
        //Post-Befehlszeilenargumente bei Leerschlag splitten und der Liste hinzufügen
        String []postArgs = stepConfigPostArgs.split(" ");
        for(String arg : postArgs){
            if(!arg.isEmpty()){
                sArrayList.add(arg);
            }
        }
        return sArrayList;
    }


    /**
     * Vergleicht den Wert der im StepConfigurations.xml definierten Bedingunge mit
     * dem entsprechenden Element im Betriebsauftrag
     *
     * @param stepConfigElement
     * @param orderElement
     */
    private boolean checkBaStepCondition(Element stepConfigElement, Element orderElement) {
        //Den Wert orderCharactValue der Bedingung aus StepConfigurations.xml lesen
        String orderCharactValue = orderElement.getElementsByTagName("CharactValue").item(0).getTextContent();
        //Den Wert stepConfigCharactValue der Bedingung aus dem Betriebsauftrag lesen
        String stepConfigCharactValue = stepConfigElement.getElementsByTagName("CharactValue").item(0).getTextContent();
        //Die Werte der beiden Bedingungen vergleichen
        if (stepConfigCharactValue.equals(orderCharactValue)) {
            return true;
        }
        return false;
    }

    /**
     * Führt eine externe Applikation mit optionalen Befehlszeilenargumenten aus
     * und übergibt der Methode informObserver() die Informationen der aktuell gestarteten externen Applikation
     * Wirft eine ReadWriteException falls die Applikation nicht gestartet werden kann
     *
     * @param sArrayList
     * @param number
     * @throws ReadWriteException
     */
    private void runExternalApplication(List<String> sArrayList, int number)throws ReadWriteException{

        List<String> cmdArgs = sArrayList;

        ProcessBuilder pb = new ProcessBuilder (cmdArgs);
        Process p = null;
        try {
            p = pb.start();
            informObserver(createApplStatus(stepConfigApplName, number, AppInfo.RUNNING));
            int ok = p.waitFor();
            if(ok == 0) {
                informObserver(createApplStatus(stepConfigApplName, number, AppInfo.FINISH));
                //Name der Applikation wird als FINISH in die Liste zur Protokoll-Erzeugung eingetragen
                addMessageToProtocol(stepConfigApplName + "   " + AppInfo.FINISH);
            }else{
                //Name der Applikation wird als ERROR in die Liste zur Protokoll-Erzeugung eingetragen
                addMessageToProtocol(stepConfigApplName + "   " + AppInfo.ERROR);
                ReadWriteException rwEx = new ReadWriteException("Fehler beim Ausführen von " + stepConfigApplName);
                throw rwEx;
            }
        } catch (IOException e) {
            ReadWriteException rwEx = new ReadWriteException("Fehler beim Ausführen von " + e.getMessage());
            throw rwEx;
        } catch (InterruptedException e) {
            ReadWriteException rwEx = new ReadWriteException("Fehler beim Ausführen von " + e.getMessage());
            throw rwEx;
        }
    }

    /**
     * Informiert die Observers und übergibt den aktuellen Status der momentan ausgeführten Applikation
     *
     * @param appState aktueller Status der momentan ausgeführten Applikation
     */
    private void informObserver(ApplicationStatus appState){
        setChanged();
        notifyObservers(appState);
    }

    /**
     * Erzeugt eine Instanz der Klasse ApplicationStatus mit den aktuellen Informationen
     *
     * @param name
     * @param number
     * @param actState
     * @return
     */
    private ApplicationStatus createApplStatus(String name, int number, AppInfo actState){

        return new ApplicationStatus(name, number, actState);
    }

    /**
     * Fügt dem Protokoll eine Meldung hinzu
     *
     * @param message
     */
    public void addMessageToProtocol(String message){
        applNameList.add(message);
    }


    @Override
    public void run() {
        startIbManager();
    }
}
