package view;

import controller.AppInfo;
import controller.ApplicationStatus;
import controller.SequenceManager;
import controller.StringVerifier;
import model.ReadWriteException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import static java.awt.GridBagConstraints.WEST;

/**
 * Diese Klasse enthält die Start_Maske User Interface. Das Design entstand mit einem Gridbaglayout. Um zu starten muss die
 * Methode fenster_erstellen() ausgeführt werden.
 * Created by Moser on 31.12.2016.
 * @author Moser
 */
public class Start_Maske extends JPanel implements Observer{

    // Alle constraint Felder
    private int gridx, gridy, gridwidth, gridheight, fill, anchor, ipadx, ipady;
    private double weightx, weighty;
    private Insets insets;
    private JTextField kurzZeichen, konfigSchritt, applStatus;
    private JLabel fieldMaschNr;
    private JButton start_konfiguration;
    private JCheckBoxMenuItem item;
    //private JOptionPane pane;
    private SequenceManager manager = null;
    private StringVerifier verivier = null;

    //Konstruktor
    public Start_Maske() {
        manager = new SequenceManager();
        manager.addObserver(this);
        verivier = new StringVerifier();
        setLayout(new GridBagLayout());
        example();
    }

    //Statische Methode um ein Fenster zu erzeugen. Konstruktor von Start_Maske wir hier aufgerufen
    private static JFrame fensterErstellen(){
        JFrame frame = new JFrame("IB_Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(750,440);
        frame.setMinimumSize(new Dimension(700, 400));
        frame.setLocationRelativeTo(null);
        frame.setContentPane(new Start_Maske());
        frame.setVisible(true);

        return frame;
    }

    //Komponenten hinzufügen
    private void example() {
        //JLabel für Maschinenummer erzeugt
        addGB(new JLabel("Maschinenummer"), gridx = 0, gridy = 0);
        addGB(fieldMaschNr =new JLabel(), gridx = 1, gridy = 0);
        fieldMaschNr.setPreferredSize(new Dimension(150,30));

        //Maschinenummer wird uebergeben
        initMaschinenummer();

        //Checkbox zur ueberpruefung der Maschinenummer
        addGB(item = new JCheckBoxMenuItem("Maschinenummer I.O."), gridx = 2, gridy = 0);

        //Kurzzeichen beschriftung und Textfeld zum eintragen
        addGB(new JLabel("Ihr_Kurz_Zeichen"), gridx = 0, gridy = 3);
        addGB(kurzZeichen = new JTextField(), gridx = 1, gridy = 3);
        kurzZeichen.setPreferredSize(new Dimension(150,30));

        //Anzeigebereich welche Applikation im Moment läuft
        addGB(new JLabel("Aktueller_Konfigurations_Schritt"), gridx = 0, gridy = 4);
        addGB(konfigSchritt = new JTextField(), gridx = 1, gridy = 4);
        konfigSchritt.setPreferredSize(new Dimension(150,30));

        //Anzeigebereich welchen Status die Applikation hat
        addGB(applStatus = new JTextField(), gridx = 3, gridy = 4);
        applStatus.setPreferredSize(new Dimension(150,30));

        //Start Button mit AktionListener verbunden
        addGB(start_konfiguration = new JButton("Start_Konfiguration"), gridx = 1, gridy = 5, weightx = 0, weighty = 3);
        start_konfiguration.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (item.getState()){
                    manager.addMessageToProtocol("Kurzzeichen: " + kurzZeichen.getText());
                    new Thread(manager).start();
                }
                else{
                    JOptionPane.showMessageDialog(null,"Bitte Maschinenummer prüfen");

                }
            }
        });
    }

    /**
     * Methode zum eintragen der Maschinenummer
     */
    private void initMaschinenummer(){
        try {
            fieldMaschNr.setText(verivier.getVerifiedMachineNumber());
        }catch (ReadWriteException ex){
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }

    //Methoden zum Hinzufügen von Komponenten mit den benötigten GridBagConstraints
    private void addGB(Component component, int gridx, int gridy) {
        addGB(component, gridx, gridy, 1, 1, WEST, 0.0, 0.0, WEST, new Insets(0, 0, 0, 0), 0, 0);
    }
    private void addGB(Component component, int gridx, int gridy, int gridwidth) {
        addGB(component, gridx, gridy, gridwidth, 1, WEST, 0.0, 0.0, WEST, new Insets(0, 0, 0, 0), 0, 0);
    }
    private void addGB(Component component, int gridx, int gridy, int gridwidth, int gridheight) {
        addGB(component, gridx, gridy, gridwidth, gridheight, WEST, 0.0, 0.0, WEST, new Insets(0, 0, 0, 0), 0, 0);
    }
    private void addGB(Component component, int gridx, int gridy, int gridwidth, int gridheight, int fill) {
        addGB(component, gridx, gridy, gridwidth, gridheight, fill, 0.0, 0.0, WEST, new Insets(0, 0, 0, 0), 0, 0);
    }
    private void addGB(Component component, int gridx, int gridy, double weightx, double weighty) {
        addGB(component, gridx, gridy, 1, 1, WEST, weightx, weighty, WEST, new Insets(0, 0, 0, 0), 0, 0);
    }
    private void addGB(Component component, int gridx, int gridy, double weightx, double weighty, int ipadx, int ipady) {
        addGB(component, gridx, gridy, 1, 1, WEST, weightx, weighty, WEST, new Insets(0, 0, 0, 0), ipadx, ipady);
    }
    private void addGB(Component component, int gridx, int gridy, double weightx, double weighty, int anchor) {
        addGB(component, gridx, gridy, 1, 1, WEST, weightx, weighty, anchor, new Insets(0, 0, 0, 0), 0, 0);
    }
    private void addGB(Component component, int gridx, int gridy, double weightx, double weighty, int anchor, Insets insets) {
        addGB(component, gridx, gridy, 1, 1, WEST, weightx, weighty, anchor, insets, 0, 0);
    }

    private void addGB(Component component, int gridx, int gridy, int gridwidth, int gridheight,
                       int fill, double weightx, double weighty, int anchor, Insets insets,
                       int ipadx, int ipady) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = gridx;
        constraints.gridy = gridy;
        constraints.gridwidth = gridwidth;
        constraints.gridheight = gridheight;
        constraints.fill = fill;
        constraints.weightx = weightx;
        constraints.weighty = weighty;
        constraints.anchor = anchor;
        constraints.insets = insets;
        constraints.ipadx = ipadx;
        constraints.ipady = ipady;
        add(component, constraints);
    }
    //Start Methode
    public static void main(String[] args) {
        fensterErstellen();
    }

    @Override
    public void update(Observable o, Object arg) {
        ApplicationStatus status = (ApplicationStatus)arg;

        if(status.getActState() == AppInfo.ERROR){
            int choice = JOptionPane.showOptionDialog(null,status.getApplName(),"Fehlermeldung",JOptionPane.OK_OPTION,JOptionPane.ERROR_MESSAGE,null,null,null);
            if (choice == JOptionPane.YES_OPTION)
            {
                System.exit(0);
            }
        } else if(status.getActState() == AppInfo.TERMINATED){

            //fieldMaschNr.setText("");
            konfigSchritt.setText(status.getApplName());
            applStatus.setText(status.getActState().toString());
            //appStatus.setText("");
        }else {
            //appNumber.setText("" + status.getNumber());
            konfigSchritt.setText(status.getApplName());
            applStatus.setText(status.getActState().toString());
            //appStatus.setText(status.getActState().toString());
        }

    }
}
