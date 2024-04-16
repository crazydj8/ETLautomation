package com.ooadteam5.etlautomation.miniporject.model;

import tech.tablesaw.api.*;
import tech.tablesaw.columns.Column;
import tech.tablesaw.io.csv.CsvReadOptions;
import tech.tablesaw.io.csv.CsvReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DataLoader {
    private String filePath;

    // Constructor
    public DataLoader(String filePath) {
        this.filePath = filePath;
    }

    // Getter and setter for filePath
    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    // Method to load CSV
    public Table loadCSV() throws IOException {
        CsvReadOptions options = CsvReadOptions.builder(filePath).build();
        CsvReader csvReader = new CsvReader();
        return csvReader.read(options);
    }

    // Method to perform update operations (e.g., append data)
    public void updateCSV(Table newData) throws IOException {
        Table existingData = loadCSV();

        // Append new data to existing data
        Table updatedData = existingData.append(newData);
        updatedData.write().csv(filePath);
    }

    // Method to refresh the loaded CSV data
    public Table refreshCSV() throws IOException {
        return loadCSV();
    }

    // Method to dynamically obtain column names, data types, and data values from the user
    public Table createTableFromUserInput() {
        Scanner scanner = new Scanner(System.in);
        List<Column<?>> columns = new ArrayList<>();

        System.out.println("Enter column names (type 'done' to finish):");
        String columnName;
        while (!(columnName = scanner.nextLine()).equalsIgnoreCase("done")) {
            System.out.println("Enter data type for column '" + columnName + "' (int/string/double):");
            String dataType = scanner.nextLine();

            // Translate user input to specific data types
            switch (dataType.toLowerCase()) {
                case "int":
                    IntColumn intColumn = IntColumn.create(columnName);
                    addDataToIntColumn(intColumn, scanner);
                    columns.add(intColumn);
                    break;
                case "string":
                    StringColumn stringColumn = StringColumn.create(columnName);
                    addDataToStringColumn(stringColumn, scanner);
                    columns.add(stringColumn);
                    break;
                case "double":
                    DoubleColumn doubleColumn = DoubleColumn.create(columnName);
                    addDataToDoubleColumn(doubleColumn, scanner);
                    columns.add(doubleColumn);
                    break;
                default:
                    System.out.println("Invalid data type. Defaulting to string.");
                    StringColumn defaultColumn = StringColumn.create(columnName);
                    addDataToStringColumn(defaultColumn, scanner);
                    columns.add(defaultColumn);
            }
        }

        Table table = Table.create("New Data", columns.toArray(new Column<?>[0]));
        return table;
    }

    // Method to add data to an IntColumn
    private void addDataToIntColumn(IntColumn column, Scanner scanner) {
        System.out.println("Enter data for column '" + column.name() + "':");
        while (true) {
            try {
                int value = Integer.parseInt(scanner.nextLine());
                column.append(value);
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter an integer value:");
            }
        }
    }

    // Method to add data to a StringColumn
    private void addDataToStringColumn(StringColumn column, Scanner scanner) {
        System.out.println("Enter data for column '" + column.name() + "':");
        column.append(scanner.nextLine());
    }

    // Method to add data to a DoubleColumn
    private void addDataToDoubleColumn(DoubleColumn column, Scanner scanner) {
        System.out.println("Enter data for column '" + column.name() + "':");
        while (true) {
            try {
                double value = Double.parseDouble(scanner.nextLine());
                column.append(value);
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a double value:");
            }
        }
    }

    // Main function for testing
    public static void main(String[] args) {
        // File path to the CSV file
        String filePath = "C:/Users/ankit/Desktop/SEM_6/OOAD/project/ETLautomation/Datasets/cars1.csv";

        // Create a DataLoader instance
        DataLoader dataLoader = new DataLoader(filePath);

        try {
            // Load CSV data
            System.out.println("Loading CSV data:");
            Table loadedData = dataLoader.loadCSV();
            System.out.println(loadedData);

            // Perform update operation (append data)
            System.out.println("\nUpdating CSV data:");

            // Create a new table with columns based on user input
            System.out.println("Enter details for new columns:");
            Table newData = dataLoader.createTableFromUserInput();

            // Update the CSV with the new data
            dataLoader.updateCSV(newData);
            System.out.println("CSV data updated successfully.");

            // Refresh loaded CSV data
            System.out.println("\nRefreshing CSV data:");
            Table refreshedData = dataLoader.refreshCSV();
            System.out.println(refreshedData);
        } catch (IOException e) {
            System.err.println("An error occurred while processing CSV data: " + e.getMessage());
            e.printStackTrace();
        }
    }
}