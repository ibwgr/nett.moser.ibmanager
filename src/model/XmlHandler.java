package model;

import controller.StringVerifier;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.CodeSource;

/**
 * Dient zu Lesen von XML-Dateien
 *
 * Created by Nett on 29.12.2016.
 * @author Nett
 */
public class XmlHandler {

    private File xmlFile = null;
    private DocumentBuilderFactory docBuilderFactory = null;
    private DocumentBuilder docBuilder = null;
    private Document doc = null;

    /**
     * Gibt den mit dem Suchbegriff searchPath definierten Pfad
     * aus der Datei PathConfig.xml zurück.
     * Wirft im Fehlerfall oder wenn der Suchbegriff nicht gefunden wurde eine ReadWriteException
     *
     * @param searchPath Suchbegriff in PathConfig.xml
     * @return foundPath Pfad der im PathConfig.xml gefunden wird
     * @throws ReadWriteException
     */
    public static String readPathFromPathConfig(String searchPath)throws ReadWriteException{

        String foundPath = null;
        String root = getApplicatonPath() + "\\PathConfig.xml";
        File xmlFile = new File(root);
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();

        try {
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("Item");

            for (int i = 0; i < nList.getLength(); i++) {
                Node nNode = nList.item(i);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;

                    String pathName = eElement.getElementsByTagName("CharactName").item(0).getTextContent();
                    if(pathName.equals(searchPath)){
                        foundPath = eElement.getElementsByTagName("CharactValue").item(0).getTextContent();
                    }
                }
            }
        } catch (ParserConfigurationException e) {
            ReadWriteException rwEx = new ReadWriteException("Fehler beim Lesen von " + root + "\n" + e.getMessage());
            throw rwEx;
        } catch (SAXException e) {
            ReadWriteException rwEx = new ReadWriteException("Fehler beim Lesen von " + root + "\n" + e.getMessage());
            throw rwEx;
        } catch (IOException e) {
            ReadWriteException rwEx = new ReadWriteException("Fehler beim Lesen von " + root + "\n" + e.getMessage());
            throw rwEx;
        }
        if(foundPath == null){
            ReadWriteException rwEx = new ReadWriteException("Der CharactName " + searchPath + " wurde im " + root + " nicht gefunden");
            throw rwEx;
        }
        return foundPath;
    }

    /**
     *Gibt eine Liste aller Steps-Elemente zurück,
     *die in StepConfigurations.xml definiert wurden
     *Wirft im Fehlerfall eine ReadWriteException
     *
     * @return nList Liste aller Steps-Elemente in StepConfigurations.xml
     * @throws ReadWriteException
     */
    public static NodeList getNodeListStepConfigurations()throws ReadWriteException{

        NodeList nList = null;
        String root = getApplicatonPath() + "\\StepConfigurations.xml";
        File xmlFile = new File(root);
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();
            nList = doc.getElementsByTagName("Step");
        } catch (ParserConfigurationException e) {
            ReadWriteException rwEx = new ReadWriteException("Fehler beim Lesen von " + root + "\n" + e.getMessage());
            throw rwEx;
        } catch (SAXException e) {
            ReadWriteException rwEx = new ReadWriteException("Fehler beim Lesen von " + root + "\n" + e.getMessage());
            throw rwEx;
        } catch (IOException e) {
            ReadWriteException rwEx = new ReadWriteException("Fehler beim Lesen von " + root + "\n" + e.getMessage());
            throw rwEx;
        }
        return nList;
    }

    /**
     * Gibt das Element des Suchbegriffs searchElement aus dem Betriebsauftrag
     * in der Bsp-Form Configuration_A2231E0305.xml der aktuellen Maschinennummer zurück.
     * Wirft im Fehlerfall oder wenn das Element nicht gefunden wird eine ReadWriteException
     *
     * @return baElement das Element das mit dem Suchbegriff gefunden wurde
     * @throws ReadWriteException
     */
    public static Element getElementFromOrder(String searchElement)throws ReadWriteException{

        StringVerifier verifier = new StringVerifier();
        String root = verifier.getVerfiedPathFromPathConfig("Betriebsauftrag") + "Configuration_" + verifier.getVerifiedMachineNumber() + ".xml";
        Element baElement = null;

        File xmlFile = new File(root);
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("Configuration");

            for (int i = 0; i < nList.getLength(); i++) {
                Node nNode = nList.item(i);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    String charactName = eElement.getElementsByTagName("CharactName").item(0).getTextContent();
                    if(charactName.equals(searchElement)){
                        baElement = eElement;
                        break;
                    }
                }
            }
        } catch (ParserConfigurationException e) {
            ReadWriteException rwEx = new ReadWriteException("Fehler beim Lesen von " + root + "\n" + e.getMessage());
            throw rwEx;
        } catch (SAXException e) {
            ReadWriteException rwEx = new ReadWriteException("Fehler beim Lesen von " + root + "\n" + e.getMessage());
            throw rwEx;
        } catch (IOException e) {
            ReadWriteException rwEx = new ReadWriteException("Fehler beim Lesen von " + root + "\n" + e.getMessage());
            throw rwEx;
        }
        if(baElement == null){
            ReadWriteException rwEx = new ReadWriteException("Der CharactName " + searchElement + " wurde im Betriebsauftrag " +
                    root +" nicht gefunden");
            throw rwEx;
        }
        return baElement;
    }

    /**
     * Gibt den Root-Pfad zurück von dem aus die Applikation ausgeführt wird
     * Wirft eine ReadWriteException wenn der Pfad nicht erstellt werden konnte
     *
     * @return
     * @throws ReadWriteException
     */
    public static String getApplicatonPath()throws  ReadWriteException{
        CodeSource codeSource = XmlHandler.class.getProtectionDomain().getCodeSource();
        File rootPath = null;
        try {
            rootPath = new File(codeSource.getLocation().toURI().getPath());
        } catch (URISyntaxException e) {
            ReadWriteException rwEx = new ReadWriteException("Der Rootpfad der Applikation konnte nicht erstellt werden");
            throw rwEx;
        }
        return rootPath.getParentFile().getPath();
    }
}
