/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package alex.dsamotorphinventory;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Scanner;
import java.util.LinkedList;

/**
 * The DSAMotorPhInventory class manages the motor inventory for MotorPH.
 * It allows users to perform various operations like adding new stock, 
 * deleting incorrect stock, sorting stock by brand, and searching for stock 
 * by engine number. The inventory data is stored in a CSV file and is read 
 * through the Csv class.
 *  
 * The class includes a menu system that provides options for users to interact 
 * with the inventory system and perform the necessary actions.
 * 
 * @author Alex Resurreccion
 */
public class DSAMotorPhInventory {
    
     /**
     * A list of inventory items represented as arrays of strings. Each string 
     * array contains the following information: engine number, date entered, 
     * stock label, brand, and status.
     */
    private static LinkedList<String[]> items = new LinkedList<String[]>(); // A map holding the items in the inventory
    private static Scanner scanner = new Scanner(System.in); // A scanner to take user input
    private static String engineNumber; //The engine number entered by the user.
    private static int engineIndex; //The index of engine number in items list.
    private static Csv csv = new Csv(); //A Csv object for reading and writing CSV files.
    
    /**
     * Reads the inventory items from a CSV file and populates the `items` list.
     */
    public static void getCsVItems(){
        items = csv.read();
    }
    
    /**
     * The main method starts the program by displaying the welcome message 
     * and calling the menu() method to provide the user with available options.
     * 
     */
    public static void main(String[] args) {
        getCsVItems();
        System.out.println("    Welcome back User   ");
        System.out.println("***MotorPH Inventory App***\n\n");
        menu();
    }
    
    /**
     * Displays the main menu options for the user to choose an action.
     * The user can select to add, delete, sort, or search inventory items.
     */
    public static void menu () {
        try {
            System.out.println("--Main Menu--\n"
                    + "type \"1\" for Add Stocks\n"
                    + "type \"2\" for Delete Incorrect Stocks\n"
                    + "type \"3\" for Sort Stocks\n"
                    + "type \"4\" for Search Stocks");
            int selection =  scanner.nextInt();
            scanner.nextLine(); // fix bug for new line or entering string

            if (selection == 1){
                addStock();
            } else if (selection == 2){
                deleteStock();
            } else if (selection == 3){
                sortStocks();
           } else if (selection == 4){
               // Trigger the sort stocks functionality
               searchStock();
           } else {
               // If user enters a number outside the range 1-4
               System.out.println("Invalid selection. Please choose a number between 1 and 4.");
               menu();
           }
    //                continueMenu = false;

        } catch (java.util.InputMismatchException e) {
            System.out.println("Error: Please enter a valid number.");
            scanner.nextLine();
            menu();
        } catch (Exception e){
            System.out.println("An unexpected error occured: " + e.getMessage());
        }
    }
    
    /**
     * Prompts the user to search for a stock item by engine number and displays 
     * the corresponding stock details. If no result is found, a message is shown.
     */
    public static void searchStock(){
        String[] item = searchEngine();
        if(item.length == 1){
            System.out.println("No Result Found");
        } else {
            displayStock(item);
        }
        newTransactionPrompt();
    }
    
    /**
     * Prompts the user to input an engine number to search for an item in the 
     * inventory. If a match is found, the corresponding stock item is returned; 
     * otherwise, a message indicating no result is returned.
     * 
     * @return The array containing information of the matching stock item.
     */
    public static String[] searchEngine(){
        System.out.println("Enter engine number: ");
        engineNumber = scanner.nextLine();
        for(int i=0; i<items.size(); i++){
            if(items.get(i)[0].equals(engineNumber)){
                engineIndex = i;
                return items.get(i);
            }
        }
        return new String[] {engineNumber};
    }
    
    /**
     * Displays the details of a stock item, including its engine number, date 
     * entered, stock label, brand, and status.
     * 
     * @param item The array representing the stock item details.
     */
    public static void displayStock(String[] item){
        // Display stock information
        System.out.println("Item info ");
        System.out.println("Engine number: " + item[0]);
        System.out.println("Date entered: " + item[1]);
        System.out.println("Stock label: " + item[2]);
        System.out.println("Brand: " + item[3]);
        System.out.println("Status: " + item[4]);
    }
    
    /**
     * Prompts the user to add a new stock item to the inventory. If the engine 
     * number already exists, the user is given the option to enter a new number 
     * or display the existing stock information.
     */
    public static void addStock(){
        String existChoice = "x";
        do {
            String[] engineNumberOrItem = searchEngine();
            engineNumber = engineNumberOrItem[0];
            if(engineNumberOrItem.length == 1){
                addNewStock();
            } else {
                System.out.println("Item already exist in inventory. "
                        + "Do you want to enter a new engine number (y) or display information (any)?");
                existChoice = scanner.nextLine();
                if(!existChoice.equals("y")){
                    // Display information
                    displayStock(engineNumberOrItem);
                }
            }
        } while(existChoice.equals("y"));
        newTransactionPrompt();
    }
    
    /**
     * Prompts the user to input details for a new stock item and adds it to the 
     * inventory if the information is correct.
     */
    public static void addNewStock(){
        System.out.println("Engine number is not in inventory.");
        String isInfoCorrect;
        Date date = new Date();
        String dateEntered = new SimpleDateFormat("MM/dd/yyyy").format(date);
        do{
            // Prompt user to input stock information
            System.out.println("Enter information for the new item ");
            String stockLabel = "New";
            System.out.println("Brand: ");
            String brand = scanner.nextLine();
            String status = "On-hand";
            String[] newStock = {engineNumber, dateEntered, stockLabel, brand, status};
            displayStock(newStock);
            System.out.println("Are all the information correct? (y/n)");
            isInfoCorrect = scanner.nextLine();
            
            if(isInfoCorrect.equals("y")){
                items.add(newStock);
                csv.add(newStock);
                System.out.println("Successfully saved new item ");
            }
        } while(!isInfoCorrect.equals("y"));
        
        newTransactionPrompt();
    }
    
    /**
     * Prompts the user to delete a stock item if it is marked as "old" and "sold."
     * The user is asked to confirm deletion before the item is permanently removed 
     * from the inventory.
     */
    public static void deleteStock(){
        System.out.println("On delete stock");
        String existChoice = "x";
        do {
            String[] itemInfo = searchEngine();
            engineNumber = itemInfo[0];
            if(itemInfo.length == 1){
                System.out.println("Engine number is not in inventory. "
                        + "Do you want to enter new engine number? (y/n) ");
                existChoice = scanner.nextLine();
            } else {
                displayStock(itemInfo);
                if(itemInfo[2].equals("Old") && itemInfo[4].equals("Sold")){
                    deleteStockDetected();
                } else {
                    System.out.println("Item Stock cannot be deleted");
                }
                existChoice = "x";
            }
        } while(existChoice.equals("y"));
        
        newTransactionPrompt();
    }
    
    /**
     * Prompts the user to confirm the deletion of a stock item if it is marked as 
     * "Old" and "Sold." The user must confirm the deletion by entering the engine 
     * number.
     */
    public static void deleteStockDetected(){
        System.out.println("Item is detected as old and sold. "
                + "Do you want to delete this item (y/n)");
        String deleteItem = scanner.nextLine();
        if(deleteItem.equals("y")){
            System.out.println("Are you sure you want to delete this item? "
                    + "Input the engine number to confirm.");
            String confirmDelete = scanner.nextLine();
            if(confirmDelete.equals(engineNumber)){
                items.remove(engineIndex);
                boolean isDeleted = csv.delete(engineNumber);
                if(isDeleted){
                    System.out.println("Item has been deleted");
                } else {
                    System.out.println("Item deletion failed. Something went wrong.");
                }
                
            } else {
                System.out.println("Item deletion failed. "
                        + "Engine number did not match.");
            }
        }
    }
    
    /**
     * Prompts the user to either exit the program or perform another transaction.
     */
    public static void newTransactionPrompt(){
        System.out.println("Type x to exit or anything to make another transaction");
        String userInput = scanner.nextLine();
        if(!userInput.equals("x")){
            menu();
        }
    }
    
    /**
     * Sorts the inventory items by brand using the merge sort algorithm. The user 
     * can choose whether to sort in ascending order or not.
     */
    public static void sortStocks(){
        System.out.println("Do you want to sort brand in ascending order? (y/n)");
        String sortOrder = scanner.nextLine();
        
        items = mergeSort(items);
        
        if(!sortOrder.equals("y")){
            items = items.reversed();
        }
        
        for (int i=0; i<items.size() ; i++){
            System.out.println(String.join(", ", items.get(i)));
        }
        
        newTransactionPrompt();
    }
    
     /**
     * Recursively sorts a list of stock items using the merge sort algorithm.
     * The list is sorted by the "brand" field (index 3 in the stock item array).
     * 
     * @param list The list of stock items to be sorted.
     * @return The sorted list of stock items.
     */
    public static LinkedList<String[]> mergeSort(LinkedList<String[]> list) {
        // Base case: if the list has 0 or 1 elements, it's already sorted
        if (list.size() <= 1) {
            return list;
        }

        // Split the list in half
        LinkedList<String[]> left = new LinkedList<>();
        LinkedList<String[]> right = new LinkedList<>();
        int middle = list.size() / 2;
        int index = 0;
        
        for (String[] item : list) {
            if (index < middle) {
                left.add(item);
            } else {
                right.add(item);
            }
            index++;
        }

        // Recursively sort each half
        left = mergeSort(left);
        right = mergeSort(right);

        // Merge the sorted halves
        return merge(left, right);
    }

    /**
     * Merges two sorted lists of stock items into one sorted list.
     * The sorting is based on the "brand" field (index 3).
     * 
     * @param left The left sorted list.
     * @param right The right sorted list.
     * @return The merged and sorted list.
     */
    public static LinkedList<String[]> merge(LinkedList<String[]> left, LinkedList<String[]> right) {
        LinkedList<String[]> merged = new LinkedList<>();
        
        while (!left.isEmpty() && !right.isEmpty()) {
            String[] leftItem = left.peek();
            String[] rightItem = right.peek();
            
            // Compare brands (index 3)
            if (leftItem[3].compareTo(rightItem[3]) <= 0) {
                merged.add(left.poll());
            } else {
                merged.add(right.poll());
            }
        }

        // Add any remaining elements
        merged.addAll(left);
        merged.addAll(right);

        return merged;
    }
}
