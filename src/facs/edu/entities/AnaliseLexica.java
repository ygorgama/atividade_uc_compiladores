package facs.edu.entities;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class AnaliseLexica {
    private final Map<String, String> tokensReserveds = new LinkedHashMap<>(){{
        put("INTEGER", "digInt");
        put("DOUBLE", "digDouble");
        put("STRING", "letString");
        put("FOR", "to");
        put("WHILE", "loop");
        put("IF", "this");
        put("ELSE", "or");
        put("END_LINE", ";");
        put("WRITE", "PRINT");
        put("ATTRIBUTION", "=");
    }};


    protected BufferedReader reader;

    public AnaliseLexica(String fileDirectory) throws FileNotFoundException {
        this.reader = new BufferedReader(new FileReader(fileDirectory));
    }

    public void run() throws IOException {
        String line = reader.readLine();

        while (line != null){
            line = reader.readLine();
        }

    }

}
