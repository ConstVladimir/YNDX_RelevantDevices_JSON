import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Coincidence {

    private static final String inputFile = "input.txt";
    private static final String outputFile = "output.txt";

    public static void main(String[] args) throws IOException, ParseException, java.text.ParseException {
        JSONParser parser = new JSONParser();
        JSONArray thingsJSON;
        JSONArray relevantThingsJSON = new JSONArray();

        String Name_CONTAINS = new String("");
        Long Price_GREATER_THAN = Long.valueOf(0);
        Long Price_LESS_THAN = Long.valueOf(1000000);
        Long Date_BEFORE = Long.valueOf(0);
        Long Date_AFTER = Long.valueOf(0);
        DateFormat format = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);

        BufferedReader reader = new BufferedReader(new FileReader(inputFile));

        thingsJSON = (JSONArray) parser.parse(reader.readLine());

        while (reader.ready()){
            String  param [] = reader.readLine().split(" ");

            switch (param[0]){
                case ("NAME_CONTAINS"):
                    Name_CONTAINS = param[1];
                    break;
                case ("PRICE_GREATER_THAN"):
                    Price_GREATER_THAN = Long.parseLong(param[1]);
                    break;
                case ("PRICE_LESS_THAN"):
                    Price_LESS_THAN = Long.parseLong(param[1]);
                    break;
                case ("DATE_BEFORE"):
                    Date_BEFORE = format.parse(param[1]).toInstant().toEpochMilli();
                    break;
                case ("DATE_AFTER"):
                    Date_AFTER = format.parse(param[1]).toInstant().toEpochMilli();
                    break;
            }
        }

        for (Iterator<JSONObject> it = thingsJSON.iterator(); it.hasNext();){
            JSONObject i = it.next();
            Long thingDate = format.parse((String) i.get("date")).toInstant().toEpochMilli();
            Long thingPrice = (Long) i.get("price");
            String thingName = (String) i.get("name");

            if ( thingDate > Date_BEFORE) continue;
            if ( thingDate < Date_AFTER) continue;
            if ( thingPrice > Price_LESS_THAN) continue;
            if ( thingPrice < Price_GREATER_THAN) continue;
            if ( thingName.toLowerCase().contains(Name_CONTAINS.toLowerCase())) relevantThingsJSON.add(i);
        }



        relevantThingsJSON.sort(new JSONComparator());

        FileWriter file = new FileWriter(outputFile);
        file.write(relevantThingsJSON.toString());
        file.close();
    }
}

class JSONComparator implements Comparator<JSONObject> {

    public int compare(JSONObject a, JSONObject b){
        Long a1 = (Long)a.get("id");
        Long b1 = (Long)b.get("id");
        return a1.compareTo(b1);
    }
}