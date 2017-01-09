package view;

import controller.AppInfo;
import controller.ApplicationStatus;
import controller.SequenceManager;
import controller.StringVerifier;
import model.ReadWriteException;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.Observable;
import java.util.Observer;

import static java.awt.GridBagConstraints.EAST;
import static java.awt.GridBagConstraints.WEST;

/**
 * Diese Klasse enthält die Start_Maske User Interface. Das Design entstand mit einem Gridbaglayout. Um zu starten muss die
 * Methode fenster_erstellen() ausgeführt werden.
 * Created by Moser on 31.12.2016.
 *
 * @author Moser
 */
public class Start_Maske extends JPanel implements Observer {

    // Alle constraint Felder
    private int gridx, gridy, gridwidth, gridheight, fill, anchor, ipadx, ipady;
    private double weightx, weighty;
    private Insets insets;
    //Instanz Variablem
    private JTextField kurzZeichen, konfigSchritt, applStatus, applNr;
    private JLabel fieldMaschNr;
    private JButton start_konfiguration;
    private JCheckBoxMenuItem item;
    private  boolean kurzZeichenOK = false;
    private SequenceManager manager = null;

    //Konstruktor
    public Start_Maske() {
        manager = new SequenceManager();
        manager.addObserver(this);
        setLayout(new GridBagLayout());
        initComponent();
    }

    //Statische Methode um ein Fenster zu erzeugen. Konstruktor von Start_Maske wir hier aufgerufen
    private static JFrame fensterErstellen() {
        JFrame frame = new JFrame("IB_Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(750, 440);
        frame.setMinimumSize(new Dimension(700, 400));
        frame.setLocationRelativeTo(null);
        frame.setContentPane(new Start_Maske());
        frame.setVisible(true);

        return frame;
    }

    //Komponenten hinzufuegen
    private void initComponent() {
        //JLabel für Maschinenummer erzeugt
        addGB(new JLabel("Maschinenummer"), gridx = 0, gridy = 0);
        addGB(fieldMaschNr = new JLabel(), gridx = 1, gridy = 0);
        fieldMaschNr.setPreferredSize(new Dimension(150, 30));

        //Maschinenummer wird uebergeben
        initMaschinenummer();

        //Checkbox zur ueberpruefung der Maschinenummer
        addGB(item = new JCheckBoxMenuItem("Maschinenummer I.O."), gridx = 2, gridy = 0);
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(kurzZeichenOK && item.getState()){
                    start_konfiguration.setEnabled(true);
                }
                else{
                    start_konfiguration.setEnabled(false);
                }
            }
        });

        //Kurzzeichen Beschriftung und Textfeld zum eintragen des Kurzzeichens
        // AktionListener hinzugefuegt um start_konfiguration Button frei zu geben
        addGB(new JLabel("Ihr Kurzzeichen (Mit Enter bestaetigen)  "), gridx = 0, gridy = 3);
        addGB(kurzZeichen = new JTextField(), gridx = 1, gridy = 3);
        kurzZeichen.setPreferredSize(new Dimension(100, 30));
        kurzZeichen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Kurzzeichen auf richtiges Format pruefen
                try {
                    MaskFormatter mf = new MaskFormatter("UUUU###LL");
                    String valueToString = mf.valueToString(kurzZeichen.getText());
                    kurzZeichen.setText(mf.valueToString(valueToString));
                    kurzZeichenOK = true;
                    if(item.getState()){
                    start_konfiguration.setEnabled(true);}
                } catch (ParseException e1) {
                    JOptionPane.showMessageDialog(null,"Kurzzeichen pruefen: " );
                    kurzZeichen.selectAll();
                }
            }
        });

        //Anzeigebereich welche Applikation im Moment laeuft
        addGB(new JLabel("Applikations Name "), gridx = 0, gridy = 4);
        addGB(konfigSchritt = new JTextField(), gridx = 1, gridy = 4);
        konfigSchritt.setPreferredSize(new Dimension(190, 30));
        konfigSchritt.setEnabled(false);

        //Anzeigebereich welchen Status die Applikation hat
        addGB(new JLabel("Status  "), gridx = 2, gridy = 4,gridwidth = 1,gridheight = 1);
        addGB(applStatus = new JTextField(), gridx = 3, gridy = 4);
        applStatus.setPreferredSize(new Dimension(85, 30));
        applStatus.setEnabled(false);

        //Anzeigebereich welche ApplikationsNummer gerade laeuft
        addGB(new JLabel("Applikations Nummer"), gridx = 0, gridy = 5);
        addGB(applNr = new JTextField(), gridx = 1, gridy = 5);
        applNr.setPreferredSize(new Dimension(30, 30));
        applNr.setEnabled(false);

        //Start Button mit AktionListener verbunden
        addGB(start_konfiguration = new JButton("Start Konfiguration"), gridx = 1, gridy = 6, weightx = 0, weighty = 3);
        start_konfiguration.setEnabled(false);
        start_konfiguration.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (item.getState() && kurzZeichenOK) {
                    manager.addMessageToProtocol("Kurzzeichen: " + kurzZeichen.getText());
                    new Thread(manager).start();
                    applNr.setEnabled(true);
                    applStatus.setEnabled(true);
                    konfigSchritt.setEnabled(true);
                } else if ((item.getState()) == false) {
                    JOptionPane.showMessageDialog(null, "Bitte Maschinenummer pruefen");
                } else if (kurzZeichenOK == false) {
                    JOptionPane.showMessageDialog(null, "Bitte Kurzzeichen pruefen");
                }
            }
        });
    }

    /**
     * Methode zum eintragen der Maschinenummer
     */
    private void initMaschinenummer() {
        try {
            fieldMaschNr.setText(StringVerifier.getVerifiedMachineNumber());
        } catch (ReadWriteException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }

    //Methoden zum Hinzufuegen von Komponenten mit den benoetigten GridBagConstraints
    private void addGB(Component component, int gridx, int gridy) {
        addGB(component, gridx, gridy, 1, 1, EAST, 0.0, 0.0, WEST, new Insets(30, 0, 0, 0), 0, 0);
    }

    private void addGB(Component component, int gridx, int gridy, int gridwidth, int gridheight) {
        addGB(component, gridx, gridy, gridwidth, gridheight, WEST, 0.0, 0.0, EAST, new Insets(30, 0, 0, 0), 0, 0);
    }

    private void addGB(Component component, int gridx, int gridy, double weightx, double weighty) {
        addGB(component, gridx, gridy, 1, 1, WEST, weightx, weighty, EAST, new Insets(30, 0, 0, 0), 0, 0);
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

    /**
     * Update Methode von Observable ueberschrieben um vom SequenzManager den aktuellen Status der externen Applikationen zu erhalten
     *
     * @param o
     * @param arg
     */
    @Override
    public void update(Observable o, Object arg) {
        ApplicationStatus status = (ApplicationStatus) arg;

        if (status.getActState() == AppInfo.ERROR) {
            int choice = JOptionPane.showOptionDialog(null, status.getApplName(), "Fehlermeldung", JOptionPane.OK_OPTION, JOptionPane.ERROR_MESSAGE, null, null, null);
            if (choice == JOptionPane.YES_OPTION) {
                System.exit(0);
            }

        } else if (status.getActState() == AppInfo.TERMINATED) {
            konfigSchritt.setText(status.getApplName());
            applStatus.setText(status.getActState().toString());
            applNr.setText("");
            //Ausstieg aus Programm
            if (status.getActState()== AppInfo.TERMINATED){
                JOptionPane.showMessageDialog(null, "Die Konfiguration wurde erfolgreich beendet");
                System.exit(0);
            }

        } else {
            konfigSchritt.setText(status.getApplName());
            applStatus.setText(status.getActState().toString());
            applNr.setText("" + (status.getNumber()+1));
        }
    }
}
