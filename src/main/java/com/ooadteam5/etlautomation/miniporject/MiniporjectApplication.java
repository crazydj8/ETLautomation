package com.ooadteam5.etlautomation.miniporject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

import com.ooadteam5.etlautomation.miniporject.model.DataAnalyzer;
import com.ooadteam5.etlautomation.miniporject.model.DataLoader;
import com.ooadteam5.etlautomation.miniporject.model.DataProcessor;
import com.ooadteam5.etlautomation.miniporject.model.DataVisualizer;

import javafx.stage.Stage;
import tech.tablesaw.api.Table;
import java.util.*;
import javafx.application.Application;

import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@SpringBootApplication
public class MiniporjectApplication {
	public static void main(String[] args){
		Application.launch(DataVisualizer.class, args);
	}

	// public static void main(String[] args) {
	// 	SpringApplication.run(MiniporjectApplication.class, args);
	// }
}
