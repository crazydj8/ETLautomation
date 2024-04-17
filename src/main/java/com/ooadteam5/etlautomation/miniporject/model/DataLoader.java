package com.ooadteam5.etlautomation.miniporject.model;

import tech.tablesaw.api.*;
import tech.tablesaw.columns.Column;
import tech.tablesaw.io.csv.CsvReadOptions;
import tech.tablesaw.io.csv.CsvReader;

//import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DataLoader {
    private String filePath;
    public static Table dataTable;

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
    public Table loadCSV() {
        CsvReadOptions options = CsvReadOptions.builder(filePath).build();
        CsvReader csvReader = new CsvReader();
        return csvReader.read(options);
    }

    // Method to perform update operations (e.g., append data)
    public void updateCSV(Table newData) {
        Table existingData = loadCSV();

        // Append new data to existing data
        Table updatedData = existingData.append(newData);
        updatedData.write().csv(filePath);
    }

    // Method to refresh the loaded CSV data
    public Table refreshCSV() {
        return loadCSV();
    }

    // Method to dynamically obtain column names, data types, and data values from the user
    public Table createTableFromUserInput(Table dataTable) {
        Scanner br = new Scanner(System.in);
        System.out.println("Enter row data as comma-separated values(type 'done' to finish): ");
        String input = br.nextLine();
        List<Column<?>> tabColumns = dataTable.columns();
        if(!(input.equalsIgnoreCase("done"))){
                String[] inputArray = input.split(",");
                for(int i = 0; i < tabColumns.size(); i++)
                        tabColumns.get(i).appendCell(inputArray[i]);
        }
        br.close();
        return dataTable;
    }
}