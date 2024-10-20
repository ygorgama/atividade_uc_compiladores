package facs.edu.runner;

import facs.edu.entities.AnaliseLexica;

import java.io.FileNotFoundException;

public class Compiler {
    public static void main(String[] args) {
        try {
            AnaliseLexica analiseLexica = new AnaliseLexica("C:\\out\\faculdade.txt");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
