
package com.mycompany.apiprototype;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author Sofie Muska
 * CSC 340-01
 * API Prototype Assignment
 * The main class.
 * Last Modified: 9/15/2022
 * I have abided by the UNCG Academic Integrity Policy.
 * Followed the YouTube tutorial "How to Send HTTP Request and Parse JSON
 * Data Using Java" by Coding Master - Programming Tutorials.
 */
public class APIPrototype {
    
    private static HttpURLConnection connection;
    
    /**
     * The main method for establishing a connection with the Open Library API
     * and for retrieving information about authors from it.
     * @param args the command line arguments
     * @throws MalformedURLException when an invalid URL is encountered.
     * @throws IOException when an I/O operation has failed or been interrupted.
     */
    public static void main(String[] args) throws MalformedURLException, IOException {
        
        BufferedReader reader;
        String line;
        StringBuffer responseContent = new StringBuffer();
        
        try {
            URL url = new URL("http://openlibrary.org/search/authors.json?limit=3&q=frank%20herbert");
            connection = (HttpURLConnection) url.openConnection();
        
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
        
            int status = connection.getResponseCode();
            //System.out.println("Code: " + status);
        
            if (status > 299) {
                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                while((line = reader.readLine()) != null) {
                    responseContent.append(line);
                }
                reader.close();
            }
            else {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while((line = reader.readLine()) != null) {
                    responseContent.append(line);
                }   
                reader.close();
            }
        
            //System.out.println(responseContent.toString());
            parse(responseContent.toString());
        }
        catch (MalformedURLException e) {
            e.printStackTrace(System.out);
        }
        catch (IOException e) {
            e.printStackTrace(System.out);
        }
        finally {
            connection.disconnect();
        }
    
    }
    
    /**
     * This method parses the information returned from the 0pen Library API.
     * @param responseBody the information from the API in String format
     * Exception e when a specified String does not exist in responseBody.
     */
    public static void parse(String responseBody) {
        
        String authorString = responseBody.substring(responseBody.indexOf("["));
        
        JSONArray authorArray = new JSONArray(authorString);
        
        for(int i = 0; i < authorArray.length(); i++) {
            JSONObject author = authorArray.getJSONObject(i);
            
            String name = author.getString("name");
            System.out.println("\nAuthor: " + name);
            
            try {
                String birthDate = author.getString("birth_date");
                System.out.println("\nBirth Date: " + birthDate);
            }
            catch(Exception e) {
                System.out.println("\nBirthDate: Unknown");
            }
            
            try {
                String deathDate = author.getString("death_date");
                System.out.println("\nDeath Date: " + deathDate);
            }
            catch(Exception e) {
                System.out.println("\nDeath Date: Unknown/TBD");
            }
            
            try {
                String topWork = author.getString("top_work");
                System.out.println("\nTop Work: " + topWork + "\n\n");
            }
            catch(Exception e) {
                System.out.println("\nTop Work: Unknown\n\n");
            }
            
        }
        
    }
    
}
