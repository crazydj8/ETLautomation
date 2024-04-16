
package com.ooadteam5.etlautomation.miniporject.model;

import tech.tablesaw.api.Table;
import tech.tablesaw.io.csv.CsvReadOptions;
import tech.tablesaw.io.csv.CsvReader;

import java.io.IOException;

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
        Table updatedData = existingData.append(newData);
        updatedData.write().csv(filePath);
    }

    // Method to refresh the loaded CSV data
    public Table refreshCSV() throws IOException {
        return loadCSV();
    }
}