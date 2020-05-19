package com.Main;
import com.Main.MaxMinHeap.MaxMinHeap;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;


/**
 * This class is just used as the main "wrapper" of the MaxMin heap data structure that I've built.
 * This class uses the MaxMinHeap implementation under com.Main.MaxMinHeap.MaxMinHeap.
 * This is just a class provided to the tester for easy verification of the logic,
 *
 */

public class Main {

    // Enums that describe the possible menu options
    public enum MenuOption {
        BUILD,
        EXTRACT_MAX,
        EXTRACT_MIN,
        INSERT,
        DELETE,
        HEAPIFY,
        BAD_INPUT,
        EXIT
    }

    public static void main(String[] args) throws IOException {

        /*
        This is the main wrapping method used to run and play with the implemented MaxMinHeap data structure
         */

        MenuOption userInput;
        MaxMinHeap mmHeap = new MaxMinHeap();
        Scanner inputScanner = new Scanner(System.in);  // Create a Scanner object for user input
        boolean heapWasBuilt = false;
        boolean userExit = false;

        // Keep showing the menu as long as the user did not choose the Exit command
        while (!userExit) {
            userInput = showUserMenuAndGetUserInput(heapWasBuilt);

            System.out.println("User Input - " + userInput);
            System.out.println("\n\n############################# - " + userInput + " - #############################");
            switch (userInput){
                case BUILD:
                    System.out.print("Enter the full path to the heap input file: ");
                    String inputFile = inputScanner.nextLine();
                    mmHeap = new MaxMinHeap(inputFile);
                    mmHeap.buildHeap();
                    heapWasBuilt = true;
                    System.out.println("#############################################################################");
                    break;
                case INSERT:
                    // Read user input for insert
                    System.out.print("Enter the value you want to insert to the heap: ");
                    int valueToInsert = Integer.parseInt(inputScanner.nextLine());
                    mmHeap.heapInsert(valueToInsert);
                    System.out.println("#############################################################################");
                    break;
                case DELETE:
                    System.out.print("Enter the index in the heap array that you want to delete (notice that indices start from 0):");
                    int indexTodelete = Integer.parseInt(inputScanner.nextLine());
                    mmHeap.heapDelete(indexTodelete);
                    System.out.println("#############################################################################");
                    break;
                case EXTRACT_MAX:
                    int removedMax = mmHeap.heapExtractMax();
                    System.out.println("Removed max value - " + removedMax);
                    System.out.print("New Heap (after removed max value) -" );
                    mmHeap.printHeap();
                    System.out.println("#############################################################################");
                    break;
                case EXTRACT_MIN:
                    int removedMin = mmHeap.heapExtractMin();
                    System.out.println("Removed min value - " + removedMin);
                    System.out.print("New Heap (after removed min value) -" );
                    mmHeap.printHeap();
                    System.out.println("#############################################################################");
                    break;
                case HEAPIFY:
                    System.out.print("Enter the index of the heap array you want to heapify: ");
                    int valueToHeapify = Integer.parseInt(inputScanner.nextLine());
                    mmHeap.heapify(valueToHeapify);
                    System.out.println("#############################################################################");
                    break;
                case EXIT:
                    userExit = true;
                    break;
                default:
                    System.out.println(userInput);
                    System.out.println("");
            }
        }
        System.out.println("Goodbye!");
    }

    public static MenuOption showUserMenuAndGetUserInput(boolean heapAlreadyBuilt){
        Scanner inputScanner = new Scanner(System.in);  // Create a Scanner object for user input
        String selectedOption;

        if (heapAlreadyBuilt) {
            System.out.println("\nOptions:\n1.Build-Heap\n2.Heap-Extract-Max\n3.Heap-Extract-Min\n4.Heap-Insert\n5.Heap-Delete\n6.Heapify\n7.Exit");
            System.out.print("Enter selection: ");
            selectedOption = inputScanner.nextLine();  // Read user input
            switch (selectedOption) {
                case "1":
                    return MenuOption.BUILD;
                case "2":
                    return MenuOption.EXTRACT_MAX;
                case "3":
                    return MenuOption.EXTRACT_MIN;
                case "4":
                    return MenuOption.INSERT;
                case "5":
                    return MenuOption.DELETE;
                case "6":
                    return MenuOption.HEAPIFY;
                case "7":
                    return MenuOption.EXIT;
                default:
                    System.out.println("Bad user input - '" + selectedOption + "', try again.");
                    return MenuOption.BAD_INPUT;
            }
        }

        System.out.println("\nOptions (other options will be available after you build your heap):\n1.Build-Heap\n2.Exit");
        System.out.print("Enter selection: ");
        selectedOption = inputScanner.nextLine();  // Read user input

        switch (selectedOption) {
            case "1":
                return MenuOption.BUILD;
            case "2":
                return MenuOption.EXIT;
            default:
                System.out.println("Bad user input - '" + selectedOption + "'");
                return MenuOption.BAD_INPUT;
        }
    }
}
