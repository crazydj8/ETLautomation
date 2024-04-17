package com.ooadteam5.etlautomation.miniporject.model;

import tech.tablesaw.api.NumericColumn;
import tech.tablesaw.api.Table;
import tech.tablesaw.columns.Column;
import tech.tablesaw.api.ColumnType;

public class DataProcessor {

    public Table handleMissingValues(Table data) {
        // Iterate over each column in the table
        for (Column<?> column : data.columns()) {
            // Count the number of missing values in the column
            long missingValuesCount = column.countMissing();
            // Print out the column name and the number of missing values
            System.out.println("Column: " + column.name() + ", Missing values: " + missingValuesCount);
        }

        // Remove rows with any missing values
        return data.dropRowsWithMissingValues();
    }

    public Table handleOutliers(Table data) {
        Table cleanedData = data.copy();

        for (String columnName : cleanedData.columnNames()) {
            if (cleanedData.column(columnName).type() == ColumnType.DOUBLE) {
                NumericColumn<?> numericColumn = cleanedData.numberColumn(columnName);

                double q1 = numericColumn.quartile1();
                double q3 = numericColumn.quartile3();
                double iqr = q3 - q1;

                // Iterate over all rows
                for (int i = 0; i < numericColumn.size(); i++) {
                    double value = numericColumn.getDouble(i);

                    // Check if the value is an outlier
                    if (value < q1 - 1.5 * iqr || value > q3 + 1.5 * iqr) {
                        // If it is, set all values in the row to null
                        for (String colName : cleanedData.columnNames()) {
                            cleanedData.column(colName).set(i, null);
                        }
                    }
                }
            }
        }
        return cleanedData;
    }

    public Table removeDuplicates(Table data) {
        // Get the number of rows before removing duplicates
        int before = data.rowCount();

        // Remove duplicates
        data = data.dropDuplicateRows();

        // Get the number of rows after removing duplicates
        int after = data.rowCount();

        // Print out the number of duplicates removed
        System.out.println("Duplicates removed: " + (before - after));

        return data;
    }
}