package facs.edu.runner;

import facs.edu.entities.AnaliseLexica;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Compiler {
    public static void main(String[] args) {
        try {
            AnaliseLexica analiseLexica = new AnaliseLexica("/home/ygorpgama/Documentos/faculdade.txt");
            analiseLexica.run();
            System.out.println(analiseLexica);
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
