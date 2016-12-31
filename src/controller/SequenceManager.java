package controller;

import model.ReadWriteException;
import model.XmlHandler;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.Observable;

/**
 * Die Klasse beschreibt den sequenziellen Ablauf der Applikation.
 * Als Observable benachrichtigt sie die GUI über den Zustand der
 * aktuell ausgeführten externen Applikation
 *
 * Created by Nett on 29.12.2016.
 * @author Nett
 */
public class SequenceManager extends Observable {

    /**
     * Ist die Einstiegsmethode in die Applikation und wird durch die GUI/Konsole aufgerufen
     * Sie beschreibt den sequenziellen Ablauf der Anwendung
     *
     * Wirft eine ReadWriteException an die GUI/Konsole damit sie da behandelt werden kann
     *
     * @throws ReadWriteException
     */
    public void start() throws ReadWriteException{

        checkStepConditions();

    }

    private void checkStepConditions() {
        NodeList nListStepConf = XmlHandler.getNodeListStepConfigurations();
        for (int i = 0; i < nListStepConf.getLength(); i++) {
            Node nodeStepConf = nListStepConf.item(i);
            if (nodeStepConf.getNodeType() == Node.ELEMENT_NODE) {
                Element stepConfigElement = (Element) nodeStepConf;
                //CharactName stepConfigCharactName der Bedingung aus StepConfig.xml lesen
                String stepConfigCharactName = stepConfigElement.getElementsByTagName("CharactName").item(0).getTextContent();
                //Mit stepConfigCharactName des entsprechende Element aus dem Betriebsauftrag holen
                Element orderElement = XmlHandler.getElementFromOrder(stepConfigCharactName);
                //Den Bedingungstyp StepConfig.xml lesen BA = Betriebsauftrag EX = ExistFile
                String stepConfigCondType = stepConfigElement.getElementsByTagName("CondType").item(0).getTextContent();
                if(stepConfigCondType.equals("BA")) {
                    //Den Wert orderCharactValue der Bedingung aus StepConfig.xml lesen
                    String orderCharactValue = orderElement.getElementsByTagName("CharactValue").item(0).getTextContent();
                    //Den Wert stepConfigCharactValue der Bedingung aus dem Betriebsauftrag lesen
                    String stepConfigCharactValue = stepConfigElement.getElementsByTagName("CharactValue").item(0).getTextContent();
                    //Die Werte der beiden Bedingungen vergleichen
                    if (stepConfigCharactValue.equals(orderCharactValue)) {
                        //Pfad der auszuführenden Applikation lesen
                        String stepConfigApplPath = stepConfigElement.getElementsByTagName("ApplPath").item(0).getTextContent();
                        System.out.println(stepConfigApplPath + " wird ausgeführt!");
                    }
                }
            }
        }
    }

}
