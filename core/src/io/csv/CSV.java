package io.csv;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class CSV<T> {

    BufferedReader reader;
    BufferedWriter writer;

    HashMap<String,T> internal = new HashMap<>();   // Internal HashMap for loading values.

    CSV(InputStream inputStream){
        reader = new BufferedReader(new InputStreamReader(inputStream));
    }

    CSV(OutputStream outputStream){
        writer = new BufferedWriter(new OutputStreamWriter(outputStream));
    }

    public void store(boolean newline){
        for(Map.Entry<String, T> entry : internal.entrySet()){
            try {
                writer.write(entry.getKey() + ":" + entry.getValue().toString());
                writer.write(';');
                if(newline) writer.newLine();
             } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
