/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package alex.dsamotorphinventory;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The {@code CsvReader} class provides methods for reading and processing CSV files.
 * 
 * @author Alex Resurreccion
 */
public class Csv {
    private String fileName = "resources/MotorPH Inventory Data - March 2023 Inventory Data.csv";
    /**
     * Reads a CSV file and returns its content as an ArrayList of strings.
     * 
     * @param filePath The path to the CSV file.
     * @return An ArrayList containing the lines of the CSV file.
     */
    public LinkedList read() {
        System.out.println("reading: " + fileName);
        LinkedList<String[]> data = new LinkedList<>();
        int index = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            
            while ((line = reader.readLine()) != null) {
                // Ignore first and second row of csv
                if(index < 2){
                    index++;
                } else {
                    // Split the line by comma (assuming the CSV file is comma-separated)
                    data.add(splitStringByComma(line));
                }
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("error: " + e);
        }
        return data;
    }
    
    public boolean delete(String engineNumberToDelete){
        File inputFile = new File(fileName);
        File tempFile = new File("myTempFile.csv");
        
        try {
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

            String currentLine;

            while((currentLine = reader.readLine()) != null) {
                // trim newline when comparing with lineToRemove
                String trimmedLine = currentLine.trim().split(",")[0];
                if(trimmedLine.equals(engineNumberToDelete)){
                    continue;
                }
                writer.write(currentLine + System.getProperty("line.separator"));
            }
            writer.close(); 
            reader.close(); 
        } catch (IOException e) {
            System.out.println("error: " + e);
        }
        
        boolean isDeleted = false;
        if(inputFile.delete()){
            isDeleted = tempFile.renameTo(inputFile);
        }
        return isDeleted;
    }
    
    public void add(String[] itemInfo){
        BufferedWriter writer;
        try {
            writer = new BufferedWriter(new FileWriter(fileName, true));
            writer.write(String.join(", ", itemInfo) + System.getProperty("line.separator"));
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(Csv.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Splits a string by comma, handling cases where commas are within double quotes.
     * 
     * @param data The string to be split.
     * @return An array of strings resulting from the split.
     */
    public String[] splitStringByComma(String data){
        return data.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
    }
}

