package org.presentation;

import org.businessLayer.Verificator;
import java.io.File;
import java.util.Scanner;

public class FileReader {

    /**
     * Se deschide fisierul primit ca parametru. Se citesc linie
     * cu linie din fisier. Liniile la randul lor se parseaza dupa
     * spatii libere si doua puncte. Comanda "rupta" in cuvinte
     * se da mai departe metodei detectCommand, pentru a detecta
     * ce operatie trebuie executata.
     * @param filePath numele fisierului care contine comenzile
     */
    public FileReader(String filePath) {
        try {
            File file = new File(filePath);
            Scanner scanner = new Scanner(file);

            Verificator verificator = new Verificator();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] strings = line.split(" |: |, ");

                verificator.detectCommand(strings);
            }

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
