package org.presentation;

import org.connection.ConnectionFactory;

import java.io.File;
import java.sql.Statement;
import java.util.Scanner;

public class DumpFileReader {

    /**
     * Se deschide fisierul "dump.txt", care contine instructiunile pentru crearea
     * bazei de date cu tabelele corespunzatoare claselor din model.
     * Instructiunile se citesc si se excut pe rand.
     */
    public DumpFileReader() {
        try {
            File file = new File("dump.txt");
            Scanner scanner = new Scanner(file);

            StringBuilder text = new StringBuilder();
            while (scanner.hasNextLine()) { // se verifica daca mai sunt linii in fisier
                text.append(scanner.nextLine());
            }
            String[] commands = text.toString().split(";");

            Statement statement = ConnectionFactory.getConnection().createStatement();
            for (String s : commands) {
                statement.execute(s);
            }

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
