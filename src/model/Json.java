package model;

import java.io.*;
import java.util.StringJoiner;

public class Json {

    public static String arrayToJson(int[] array) {
        StringJoiner json = new StringJoiner(",", "[", "]");
        for (int num : array) {
            json.add(String.valueOf(num));
        }
        writeToJson(json.toString());
        return json.toString();
    }

    public static int[] jsonToArray(String json) {
        String trimmedJson = json.substring(1, json.length() - 1); // Remove the square brackets
        String[] parts = trimmedJson.split(",");
        int[] array = new int[parts.length];
        for (int i = 0; i < parts.length; i++) {
            array[i] = Integer.parseInt(parts[i].trim());
        }
        return array;
    }

    public static void writeToJson(String string) {
        try {
            FileWriter writer = new FileWriter("src/serverCapacities.json");
            writer.write(string);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static int[] readJsonFile() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("src/serverCapacities.json"));
            StringBuilder json = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                json.append(line);
            }
            reader.close();
            return Json.jsonToArray(json.toString());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException a) {
            a.printStackTrace();
        }
        return null;
    }


}
