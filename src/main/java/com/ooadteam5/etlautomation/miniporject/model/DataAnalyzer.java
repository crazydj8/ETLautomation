package com.ooadteam5.etlautomation.miniporject.model;
import java.util.*;

import tech.tablesaw.api.NumericColumn;
import tech.tablesaw.api.Table;

public class DataAnalyzer {
    static Table dataTable;
    public DataAnalyzer(Table data)
    {
        dataTable = data;
    }
    public Table summarize()
    {
        //Returns a table containing column summaries for the entire table
        Table data = dataTable.summary();
        return data;
    }
    public Table normalize()
    {
        Table data = dataTable.copy();
        try{
            //Extracts numeric columns
            List<NumericColumn<?>> numcols = dataTable.numericColumns();
            //Min-Max normalizes numeric columns
            for (NumericColumn<?> numericColumn : numcols){
                double min = numericColumn.min();
                double max = numericColumn.max();
                data.addColumns(numericColumn.subtract(min).divide(max-min).multiply(10-1).add(1).setName("Norm_"+numericColumn.name()));
            }
            return data;
        }
        catch(Exception e){
            return data;
        }
    }
    public double[][] correlation(Table data)
    {
        try{
            //Extracting numeric columns in original table
            List<NumericColumn<?>> num = dataTable.numericColumns();
            //Extracting numeric columns in normalized table
            List<NumericColumn<?>> norm = data.numericColumns();
            //Extracting only normalized columns
            List<NumericColumn<?>> normcols = norm.subList(num.size(), norm.size());
            //Calculating Pearson's correlation
            double correlation[][] = new double[normcols.size()][normcols.size()];
            for(int i = 0; i < normcols.size(); i++){
                for(int j = 0; j < normcols.size(); j++){
                    correlation[i][j] = normcols.get(i).pearsons(normcols.get(j));
                }
            }
            return correlation;
        }
        catch(Exception e){
            double error[][] = new double[0][0];
            return error;
        }
    }
    public Dictionary<String, Double> getSkewness(Table data)
    {
        Dictionary<String, Double> skew = new Hashtable<>();
        try{
            //Extracting numeric columns in original table
            List<NumericColumn<?>> num = dataTable.numericColumns();
            //Extracting numeric columns in normalized table
            List<NumericColumn<?>> norm = data.numericColumns();
            //Extracting only normalized columns
            List<NumericColumn<?>> normcols = norm.subList(num.size(), norm.size());
            //Calculating skewness values per column
            for (NumericColumn<?> numericColumn : normcols) {
                skew.put(numericColumn.name(), numericColumn.skewness());
            }
            return skew;
        }
        catch(Exception e){
            return skew;
        }
    }
    public Dictionary<String, Double> getKurtosis(Table data)
    {
        Dictionary<String, Double> kurtosis = new Hashtable<>();
        try{
            //Extracting numeric columns in original table
            List<NumericColumn<?>> num = dataTable.numericColumns();
            //Extracting numeric columns in normalized table
            List<NumericColumn<?>> norm = data.numericColumns();
             //Extracting only normalized columns
            List<NumericColumn<?>> normcols = norm.subList(num.size(), norm.size());
            //Calculating kurtosis values per column
            for (NumericColumn<?> numericColumn : normcols) {
                kurtosis.put(numericColumn.name(), numericColumn.kurtosis());
            }
            return kurtosis;
        }
        catch(Exception e){
            return kurtosis;
        }
    }
    
//     public static void main(String[] args) {
//         DataAnalyzer dataAnalyzer = new DataAnalyzer();
//         Table summaries = dataAnalyzer.summarize();
//         Table normalized = dataAnalyzer.normalize();
//         double correlation[][] = dataAnalyzer.correlation(normalized);
//         Dictionary<String, Double> skew = dataAnalyzer.getSkewness(normalized);
//         Dictionary<String, Double> kurtosis = dataAnalyzer.getKurtosis(normalized);
//         System.out.println(summaries);
//         System.out.println(normalized.first(3));
//         System.out.println();
//         for(int i = 0; i < normalized.columnCount()-dataTable.columnCount(); i++)
//         {
//             for(int j = 0; j < normalized.columnCount()-dataTable.columnCount(); j++)
//                 System.out.print(correlation[i][j] + " ");
//             System.out.println();
//         }
//         System.out.println();
//         Enumeration<String> i = skew.keys();
//         while(i.hasMoreElements()){
//             String key = i.nextElement();
//             System.out.println(key + ": " + skew.get(key));
//         }
//         System.out.println();
//         Enumeration<String> j = kurtosis.keys();
//         while(j.hasMoreElements()){
//             String key = j.nextElement();
//             System.out.println(key + ": " + kurtosis.get(key));
//         }
//     }
}
