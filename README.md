Semesterarbeit NDK HF Grundlagen Software-Entwicklung 

Hans-Jürg Nett und Harry Moser

IB-Manager

Folgende Ressourcen sind notwendig um den IB_Manager ausführen zu können, und können auf zwei Arten istalliert werden:

1.Möglichkeit

Untenstehende Files die im Module rsrc des Projekts zur Verfügung liegen, manuell in die erwähnten Verzeichnisse kopieren.

Config-Files:

C:\temp\Configuration_A2231E0305.xml

C:\tcommc\exe\machinnr.ini

Im Root-Pfad des IB_Manager bei IntelliJ z.B:

C:\Users\Nett\Documents\IBW\IB_Manager\out\production\StepConfigurations.xml

C:\Users\Nett\Documents\IBW\IB_Manager\out\production\PathConfig.xml

Externe Applikationen zu Testzwecken:

C:\temp\IbManagerApp.exe

C:\temp\Uhr.jar

C:\temp\IbBatch.bat

2.Möglichkeit

Für Windows Betriebssysteme besteht auch die Möglichkeit das IB-Manager-Setup auszuführen, 
welches die Files in die entsprechenden Verzeichnisse kopiert und einen Shortcut zur Applikation auf dem Desktop anlegt.
Die Setup-Datei befindet sich ebenfalls im Module rsrc des Projekts.

Wenn die Applikation durch das Setup installiert wird, müssen für die Ausführung aus IntelliJ wie oben erwähnt StepConfigurations.xml und PathConfig.xml manuell ins Root-Verzeichnis kopiert werden.

Im Modul doc ist die aus IntelliJ erstellte Java-Doc abgelegt. Die Dokumentation kann über die Datei index.html gestartet werden.
