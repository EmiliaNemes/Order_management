package org.main;

import org.presentation.DumpFileReader;
import org.presentation.FileReader;

public class Main {

    /**
     * Se instantiaza un dump file, pentru a crea tabelele din baza de date.
     * Se instantiaza un file reader, pentru a citi comenzile din fisierul dat ca argument.
     * @param args numele fisierului cu comenzi se da ca argument programului
     */
    public static void main( String[] args )
    {
        DumpFileReader dumpFileReader = new DumpFileReader();
        FileReader fileReader = new FileReader(args[0]);
    }

}
