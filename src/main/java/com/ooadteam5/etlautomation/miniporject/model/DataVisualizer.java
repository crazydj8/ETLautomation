package com.ooadteam5.etlautomation.miniporject.model;

import com.ooadteam5.etlautomation.miniporject.MiniporjectApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.support.GenericApplicationContext;

import com.ooadteam5.etlautomation.miniporject.model.DataAnalyzer;
import com.ooadteam5.etlautomation.miniporject.model.DataLoader;
import com.ooadteam5.etlautomation.miniporject.model.DataProcessor;

import javafx.application.Application;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.scene.Scene; 
import javafx.stage.Stage;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ScrollPane;
import tech.tablesaw.api.DoubleColumn;
import tech.tablesaw.api.FloatColumn;
import tech.tablesaw.api.IntColumn;
import tech.tablesaw.api.LongColumn;
import tech.tablesaw.api.NumberColumn;
import tech.tablesaw.api.ShortColumn;
import tech.tablesaw.api.StringColumn;
import tech.tablesaw.api.Table;
import javafx.scene.layout.FlowPane;
import java.util.*;

public class DataVisualizer extends Application { 
   static Table table;
   private ConfigurableApplicationContext applicationContext;
   // DataVisualizer() {
   //    DataAnalyzer analyzer = new DataAnalyzer();
   //    table = analyzer.normalize();
   // }

   @Override 
   public void start(Stage stage) {

      //Table table = Table.read().csv("csv_file_name.csv");
      // table.structure();
      // table.first(3);

      Table dataTable;
		String filePath = "Datasets/titanic.csv";

        // Create a DataLoader instance
        DataLoader dataLoader = new DataLoader(filePath);

        // Load CSV data
        System.out.println("Loading CSV data:");
        dataTable = dataLoader.loadCSV();
        System.out.println(dataTable);

        // Perform update operation (append data)
        System.out.println("\nUpdating CSV data:");

        // Create a new table with columns based on user input
        System.out.println("Enter details for new columns:");
        Table newData = dataLoader.createTableFromUserInput(dataTable);

        // Update the CSV with the new data
        dataLoader.updateCSV(newData);
        System.out.println("CSV data updated successfully.");

        // Refresh loaded CSV data
        System.out.println("\nRefreshing CSV data:");
        Table refreshedData = dataLoader.refreshCSV();
        System.out.println(refreshedData);

		DataProcessor dataProcessor = new DataProcessor();
		dataTable = dataProcessor.handleOutliers(dataTable);
		dataTable = dataProcessor.handleMissingValues(dataTable);
		dataTable = dataProcessor.removeDuplicates(dataTable);
		DataAnalyzer dataAnalyzer = new DataAnalyzer(dataTable);
		Table normTable = dataAnalyzer.normalize();
		double[][] correlation = dataAnalyzer.correlation(normTable);
		Dictionary<String, Double> skew = dataAnalyzer.getSkewness(normTable);
      Dictionary<String, Double> kurtosis = dataAnalyzer.getKurtosis(normTable);
		System.out.println("\n" + dataAnalyzer.summarize());
		System.out.println("\n" + normTable.first(3) + "\n");
		for(int i = 0; i < normTable.columnCount()-dataTable.columnCount(); i++)
        {
            for(int j = 0; j < normTable.columnCount()-dataTable.columnCount(); j++)
                System.out.print(correlation[i][j] + " ");
            System.out.println();
        }
		System.out.println();
		Enumeration<String> skew_key = skew.keys();
        while(skew_key.hasMoreElements()){
            String key = skew_key.nextElement();
            System.out.println(key + ": " + skew.get(key));
        }
		System.out.println();
        Enumeration<String> kurtosis_key = kurtosis.keys();
        while(kurtosis_key.hasMoreElements()){
            String key = kurtosis_key.nextElement();
            System.out.println(key + ": " + kurtosis.get(key));
        }

      Table table = dataTable;
      //Choose all numeric columns
      var numColumnName = table.numericColumns();
      var catColumnName = table.stringColumns();

      //Convert to string arrays
      //String[] numColumns = numColumnName.toArray(new String[0]);
      //String[] catColumns = catColumnName.toArray(new String[0]);

      //Creating a FlowPane object
      FlowPane root = new FlowPane();
      ScrollPane scroll = new ScrollPane();
      scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);    // Horizontal scroll bar
      scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);    // Vertical scroll bar
      scroll.setContent(root);

      for (int i = 0; i < numColumnName.size() - 1; i++)
      {
         //Defining the x axis             
         NumberAxis xAxis = new NumberAxis(); 
         xAxis.setLabel(numColumnName.get(i).toString());

         //Get column values for the ith column
         DoubleColumn column_i = null;
         //System.out.println(numColumnName.get(i).name() + " " + numColumnName.get(i).type());
         if (table.column(numColumnName.get(i).name().toString()) instanceof DoubleColumn)
         {
            column_i = table.doubleColumn(numColumnName.get(i).name().toString());
            //System.out.println("I: " + column_i.get(45));
         }
         else if (table.column(numColumnName.get(i).name().toString()) instanceof IntColumn)
         {
            column_i = table.intColumn(numColumnName.get(i).name().toString()).asDoubleColumn();
            //System.out.println("I: " + column_i.get(45));
         }
         else if (table.column(numColumnName.get(i).name().toString()) instanceof FloatColumn)
         {
            column_i = table.floatColumn(numColumnName.get(i).name().toString()).asDoubleColumn();
         }
         else if (table.column(numColumnName.get(i).name().toString()) instanceof ShortColumn)
         {
            column_i = table.shortColumn(numColumnName.get(i).name().toString()).asDoubleColumn();
         }
         else if (table.column(numColumnName.get(i).name().toString()) instanceof NumberColumn)
         {
            column_i = table.numberColumn(numColumnName.get(i).name().toString()).asDoubleColumn();
         }
         else if (table.column(numColumnName.get(i).name().toString()) instanceof LongColumn)
         {
            column_i = table.longColumn(numColumnName.get(i).name().toString()).asDoubleColumn();
         }
         
         for (int j = i + 1; j < numColumnName.size(); j++)
         {
            //Defining the y axis   
            NumberAxis yAxis = new NumberAxis(); 
            yAxis.setLabel(numColumnName.get(j).toString());

            //Creating the scatter chart 
            ScatterChart<Number, Number> scatterchart = new ScatterChart<>(xAxis, yAxis);

            //Prepare XYChart.Series objects by setting data 
            XYChart.Series<Number, Number> series = new ScatterChart.Series<>();

            //Get column values for the jth column
            DoubleColumn column_j = null;
            if (table.column(numColumnName.get(j).name().toString()) instanceof DoubleColumn)
            {
               column_j = table.doubleColumn(numColumnName.get(j).name().toString());
               //System.out.println("J: " + column_j.size());
            }
            else if (table.column(numColumnName.get(j).name().toString()) instanceof IntColumn)
            {
               column_j = table.intColumn(numColumnName.get(j).name().toString()).asDoubleColumn();
               //System.out.println("J: " + column_j.size());
            }
            else if (table.column(numColumnName.get(j).name().toString()) instanceof FloatColumn)
            {
               column_j = table.floatColumn(numColumnName.get(j).name().toString()).asDoubleColumn();
            }
            else if (table.column(numColumnName.get(j).name().toString()) instanceof ShortColumn)
            {
               column_j = table.shortColumn(numColumnName.get(j).name().toString()).asDoubleColumn();
            }
            else if (table.column(numColumnName.get(j).name().toString()) instanceof NumberColumn)
            {
               column_j = table.numberColumn(numColumnName.get(j).name().toString()).asDoubleColumn();
            }
            else if (table.column(numColumnName.get(j).name().toString()) instanceof LongColumn)
            {
               column_j = table.longColumn(numColumnName.get(j).name().toString()).asDoubleColumn();
            }
            
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
      
      for (int i = 0; i < catColumnName.length; i++)
      {
         
         //Defining the x axis             
         CategoryAxis xAxis = new CategoryAxis(); 
         xAxis.setLabel(catColumnName[i].name().toString());

         //Defining the y axis             
         NumberAxis yAxis = new NumberAxis(); 
         xAxis.setLabel("Frequency");

         //Creating the bar chart
         BarChart barchart = new BarChart<>(xAxis, yAxis);

         //Prepare XYChart.Series objects by setting data 
         XYChart.Series series = new XYChart.Series<>();

         //Get the sorted column values for the ith column
         Table sorted = table.sortOn(catColumnName[i].name().toString());
         StringColumn column_i = sorted.stringColumn(catColumnName[i].name());

         //System.out.println(column_i.get(0) + " " + column_i.get(1) + " " + column_i.get(10));

         //Initialize number of occurences of each observation in the ith column
         
         int num = 1;

         int k = 1;
         while (k < column_i.size())
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
            k++;
         }
         series.getData().add(new XYChart.Data(column_i.get(k - 1), num));

         //Setting the data to bar chart
         barchart.getData().add(series);
            
         //Adding the graphs to FlowPane
         root.getChildren().add(barchart);
      }

      //Creating a scene object 
      Scene scene = new Scene(scroll, 600, 400); // root

      //Setting title to the Stage 
      stage.setTitle("Scatter Chart"); 
               
      //Adding scene to the stage 
      stage.setScene(scene);
      
      //Displaying the contents of the stage 
      stage.show();
       
   } 

   @Override
   public void init() {
       //logger.debug("Initializing Spring ApplicationContext");

       applicationContext = new SpringApplicationBuilder(MiniporjectApplication.class)
           .sources(MiniporjectApplication.class)
           .initializers(initializers())
           .run(getParameters().getRaw().toArray(new String[0]));
   }
   
   @Override
    public void stop() throws Exception {
        //logger.debug("Stopping GsProConnectApplication");
        
        applicationContext.close();
        Platform.exit();
    }  

   ApplicationContextInitializer<GenericApplicationContext> initializers() { 
      return ac -> {
          ac.registerBean(Application.class, () -> DataVisualizer.this);
          ac.registerBean(Parameters.class, this::getParameters);
          ac.registerBean(HostServices.class, this::getHostServices);
      };
  }
   //  public static void main(String args[]){ 
   //    launch(args);
   //  } 
 }
