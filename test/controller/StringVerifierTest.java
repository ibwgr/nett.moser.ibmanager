package controller;

import model.MachineNumberReader;
import model.ReadWriteException;
import model.XmlHandler;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Testet die Methode getMachineNumber() und getVerifiedPathFromPathConfig()
 *
 * Created by Nett on 29.12.2016.
 * @author Nett
 */
@RunWith(Enclosed.class)
public class StringVerifierTest {

    @RunWith(PowerMockRunner.class)
    @PrepareForTest({MachineNumberReader.class})
    public static class getVerifiedMachineNumber {

        /**
         * Testet ob eine gültige Maschinennummer wie gewünscht wieder
         * zurückgegeben wird
         */
        @Test
        public void mockitoFake_validMachineNumber_returnsMachNumberIn() {

            String machnumberIn = "A2231E0305";

            PowerMockito.mockStatic(MachineNumberReader.class);
            when(MachineNumberReader.readMachineNumber()).thenReturn(machnumberIn);

            String machNumerResult = StringVerifier.getVerifiedMachineNumber();

            Assert.assertEquals(machNumerResult,machnumberIn);
        }

        /**
         * Testet ob die ReadWriteException geworfen wird
         * wenn der String einen Character zu wenig hat.
         */
        @Test
        public void mockitoFake_machNumberOneDigitToShort_throwsReadWriteException() {
            String machnumberIn = "A2231E030";
            verifyMachNumber(machnumberIn);
        }

        /**
         * Testet ob die ReadWriteException geworfen wird
         * wenn der String einen Character zu viel hat.
         */
        @Test
        public void mockitoFake_machNumberOneDigitToLong_throwsReadWriteException() {
            String machnumberIn = "A2231E03055";
            verifyMachNumber(machnumberIn);
        }

        /**
         * Testet ob die ReadWriteException geworfen wird
         * wenn der String an Index 0 keinen Buchstaben hat.
         */
        @Test
        public void mockitoFake_machNumberNoLetterAtFirstPos_throwsReadWriteException() {
            String machnumberIn = "12231E0305";
            verifyMachNumber(machnumberIn);
        }

        /**
         * Testet ob die ReadWriteException geworfen wird
         * wenn der String an Index 5 keinen Buchstaben hat.
         */
        @Test
        public void mockitoFake_machNumberNoLetterAtPos5_throwsReadWriteException() {
            String machnumberIn = "A223110305";
            verifyMachNumber(machnumberIn);
        }

        /**
         * Testet ob die ReadWriteException geworfen wird
         * wenn der String von Index 1 bis 4 nicht nur aus Zahlen besteht.
         */
        @Test
        public void mockitoFake_machNumberNotOnlyNumbersFromPos1To4_throwsReadWriteException() {
            String machnumberIn = "A2B31E0305";
            verifyMachNumber(machnumberIn);
        }

        /**
         * Testet ob die ReadWriteException geworfen wird
         * wenn der String von Index 6 bis zum Ende nicht nur aus Zahlen besteht.
         */
        @Test
        public void mockitoFake_machNumberNotOnlyNumbersFromPos6ToEnd_throwsReadWriteException() {
            String machnumberIn = "A2231E03B5";
            verifyMachNumber(machnumberIn);
        }

        /**
         * Testet ob die ReadWriteException geworfen wird
         * wenn der String empty ist.
         */
        @Test
        public void mockitoFake_machNumberEmpty_throwsReadWriteException() {
            String machnumberIn = "";
            verifyMachNumber(machnumberIn);
        }

        /**
         * Testet ob die ReadWriteException geworfen wird
         * wenn der String null ist.
         */
        @Test
        public void mockitoFake_machNumberNull_throwsReadWriteException() {
            String machnumberIn = null;
            verifyMachNumber(machnumberIn);
        }

    }

    /**
     * Prüft ob der im jeweiligen Test übergebene fehlerhafte String
     * machnumberIn eine ReadWriteException wirft.
     *
     * @param machnumberIn
     */
    private static void verifyMachNumber(String machnumberIn) {
        try {
            PowerMockito.mockStatic(MachineNumberReader.class);
            when(MachineNumberReader.readMachineNumber()).thenReturn(machnumberIn);

            StringVerifier.getVerifiedMachineNumber();

            Assert.fail("No ReadWriteException was thrown");
        } catch (ReadWriteException ex) {
            Assert.assertEquals(machnumberIn + " ist keine gültige Maschinennnummer", ex.getMessage());
        }
    }

    @RunWith(PowerMockRunner.class)
    @PrepareForTest({XmlHandler.class})
    public static class getVerifiedPathFromPathConfig {

        /**
         * Testet ob ein gültiger Pfad auch wieder zurückgegeben wird
         * Ein Pfad ist gültig wenn er nicht Null oder Empty ist
         */
        @Test
        public void mockitoFake_withValidPathInMaschinennummer_returnsPathIn() {

            String pathIn = "C:\\tcommc\\exe\\machinnr.ini";
            PowerMockito.mockStatic(XmlHandler.class);
            when(XmlHandler.readPathFromPathConfig(Mockito.anyString())).thenReturn(pathIn);

            String pathOut = StringVerifier.getVerfiedPathFromPathConfig(Mockito.anyString());

            Assert.assertEquals(pathIn,pathOut);
        }

        /**
         * Testet ob eine ReadWriteException geworfen wird
         * wenn der Pfad empty ist.
         */
        @Test
        public void mockitoFake_withEmptySearchKeyword_throwsReadWriteException() {

            try {
                String searchKeyword = "";
                PowerMockito.mockStatic(XmlHandler.class);
                when(XmlHandler.readPathFromPathConfig(Mockito.anyString())).thenReturn(searchKeyword);

                StringVerifier.getVerfiedPathFromPathConfig(Mockito.anyString());

                Assert.fail("No ReadWriteException was thrown");
            }catch(ReadWriteException ex){
                Assert.assertEquals("Der im PathConfig.xml mit CharactName  definierte Pfad ist nicht gültig: ",ex.getMessage());
            }
        }

        /**
         * Testet ob eine ReadWriteException geworfen wird
         * wenn der Pfad null ist.
         */
        @Test
        public void mockitoFake_withNullSearchKeyword_throwsReadWriteException() {

            try {
                String searchKeyword = null;
                PowerMockito.mockStatic(XmlHandler.class);
                when(XmlHandler.readPathFromPathConfig(Mockito.anyString())).thenReturn(searchKeyword);

                StringVerifier.getVerfiedPathFromPathConfig(Mockito.anyString());

                Assert.fail("No ReadWriteException was thrown");
            }catch(ReadWriteException ex){
                Assert.assertEquals("Der im PathConfig.xml mit CharactName  definierte Pfad ist nicht gültig: null",ex.getMessage());
            }
        }

    }

}