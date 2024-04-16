package com.ooadteam5.etlautomation.miniporject.model;

import tech.tablesaw.api.DoubleColumn;
import tech.tablesaw.api.Table;
import tech.tablesaw.columns.Column;

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
        // Iterate over each column in the table
        for (Column<?> column : data.columns()) {
            // Only process numeric columns
            if (column instanceof DoubleColumn) {
                DoubleColumn doubleColumn = (DoubleColumn) column;

                // Calculate Q1, Q3, and IQR
                double q1 = doubleColumn.percentile(0.25);
                double q3 = doubleColumn.percentile(0.75);
                double iqr = q3 - q1;

                // Identify outliers
                DoubleColumn outliers = doubleColumn
                        .where(doubleColumn.isLessThan(q1 - 1.5 * iqr).or(doubleColumn.isGreaterThan(q3 + 1.5 * iqr)));

                // Print out the column name and the number of outliers
                System.out.println("Column: " + column.name() + ", Outliers: " + outliers.size());

                // Remove outliers
                data.removeColumns(column.name());
                data.addColumns(doubleColumn.where(doubleColumn.isGreaterThanOrEqualTo(q1 - 1.5 * iqr)
                        .and(doubleColumn.isLessThanOrEqualTo(q3 + 1.5 * iqr))));
            }
        }

        return data;
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