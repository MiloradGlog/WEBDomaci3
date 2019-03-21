package com.company;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class QuoteGenerator {

    private ArrayList<Quote> izreke;

    public QuoteGenerator(){
        izreke = new ArrayList<>();
        izreke.add(new Quote("Ako zelis pobediti, ne smes izgubiti", "Milorad"));
        izreke.add(new Quote("Ko rano rani, dve srece grabi", "Zile"));
        izreke.add(new Quote("Nije mala nije mala tri put' ratovala", "Junak"));
        izreke.add(new Quote("Karadzicu vodi srbe svoje", "Radovan"));
        izreke.add(new Quote("Ko drugome jamu kopa sam u nju upada", "Mare"));
    }

    public Quote generate(){
        return izreke.get(((int)(Math.random()*100))%izreke.size());
    }

    public Quote fetch(){

        Quote izreka = null;

        try {
            URL url = new URL("https://quotes.rest/qod");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");

            int status = con.getResponseCode();
            System.out.println("Response status = "+ status);

            if (status == 429) {
                System.err.println("TOO MANY REQUESTS TO https://quotes.rest/qod, returning a generated quote");
                izreka = generate();
                return izreka;
            }

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();

            JsonParser parser = new JsonParser();
            JsonObject obj = (JsonObject)parser.parse(content.toString());
            JsonObject quoteObj = (JsonObject)(((JsonArray)((JsonObject)obj.get("contents")).get("quotes")).get(0));

            izreka = new Quote(quoteObj.get("quote").getAsString(), quoteObj.get("author").getAsString());

            System.out.println("fetched saying:"+ izreka.getText() + " author: " + izreka.getAuthor());

            con.disconnect();

        } catch (Exception e){
            e.printStackTrace();
        }

        if (izreka == null){
            System.err.println("FETCHED QUOTE IS NULL");
        }
        return izreka;
    }

}
