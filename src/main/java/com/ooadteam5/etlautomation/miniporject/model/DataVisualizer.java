package com.ooadteam5.etlautomation.miniporject.model;

import javafx.application.Application;
import javafx.scene.Scene; 
import javafx.stage.Stage;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import tech.tablesaw.api.DoubleColumn;
import tech.tablesaw.api.StringColumn;
import tech.tablesaw.api.Table;
import javafx.scene.layout.FlowPane;

public class DataVisualizer extends Application { 
   @Override 
   public void start(Stage stage) {
      Table table = Table.read().csv("csv_file_name.csv");
      // table.structure();
      // table.first(3);

      //Choose all numeric columns
      var numColumnName = table.numericColumns();
      var catColumnName = table.categoricalColumns();

      //Convert to string arrays
      String[] numColumns = numColumnName.toArray(new String[0]);
      String[] catColumns = catColumnName.toArray(new String[0]);

      //Creating a FlowPane object
      FlowPane root = new FlowPane();

      for (int i = 0; i < numColumnName.size() - 1; i++)
      {
         //Defining the x axis             
         NumberAxis xAxis = new NumberAxis(); 
         xAxis.setLabel(numColumns[i]);

         //Get column values for the ith column
         DoubleColumn column_i = table.doubleColumn(numColumns[i]);

         for (int j = i + 1; j < numColumnName.size(); j++)
         {
            //Defining the y axis   
            NumberAxis yAxis = new NumberAxis(); 
            yAxis.setLabel(numColumns[j]);

            //Creating the scatter chart 
            ScatterChart<Number, Number> scatterchart = new ScatterChart<>(xAxis, yAxis);

            //Prepare XYChart.Series objects by setting data 
            XYChart.Series<Number, Number> series = new ScatterChart.Series<>();

            //Get column values for the jth column
            DoubleColumn column_j = table.doubleColumn(numColumns[j]);
            
            for (int k = 0; k < column_i.size(); k++)
            {
               series.getData().add(new XYChart.Data(column_i.get(k), column_j.get(k)));
            }

            //Setting the data to scatter chart
            scatterchart.getData().add(series);
            
            //Adding the graphs to FlowPane
            root.getChildren().add(scatterchart); 
         }
      }

      for (int i = 0; i < catColumnName.size(); i++)
      {
         //Defining the x axis             
         CategoryAxis xAxis = new CategoryAxis(); 
         xAxis.setLabel(catColumns[i]);

         //Defining the y axis             
         NumberAxis yAxis = new NumberAxis(); 
         xAxis.setLabel("Frequency");

         //Creating the bar chart
         BarChart barchart = new BarChart<>(null, null);

         //Prepare XYChart.Series objects by setting data 
         XYChart.Series series = new XYChart.Series<>();

         //Get the sorted column values for the ith column
         Table sorted = table.sortOn(catColumns[i]);
         StringColumn column_i = sorted.stringColumn(catColumns[i]);

         //Initialize number of occurences of each observation in the ith column
         int num = 1;

         int k = 0;
         for (k = 0; k < column_i.size(); k++)
         {
            if (column_i.get(k).equals(column_i.get(k - 1)))
            {
               num++;
            }
            else
            {
               series.getData().add(new XYChart.Data(column_i.get(k - 1), num));
               num = 1;
            }
         }
         series.getData().add(new XYChart.Data(column_i.get(k), num));

         //Setting the data to bar chart
         barchart.getData().add(series);
            
         //Adding the graphs to FlowPane
         root.getChildren().add(barchart);
      }

      //Creating a scene object 
      Scene scene = new Scene(root, 600, 400);

      //Setting title to the Stage 
      stage.setTitle("Scatter Chart"); 
               
      //Adding scene to the stage 
      stage.setScene(scene);
      
      //Displaying the contents of the stage 
      stage.show();
       
   } 
    public static void main(String args[]){ 
      launch(args); 
    } 
 }
