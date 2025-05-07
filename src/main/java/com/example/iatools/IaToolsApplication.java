package com.example.iatools;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import org.dmg.pmml.PMML;
import org.jpmml.evaluator.*;
import org.jpmml.model.PMMLUtil;
import org.dmg.pmml.*;

import java.io.FileInputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import org.dmg.pmml.PMML;
import org.jpmml.evaluator.ModelEvaluator;
import org.jpmml.evaluator.ModelEvaluatorFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.jpmml.evaluator.ModelEvaluator;
import org.jpmml.evaluator.ModelEvaluatorFactory;
import org.jpmml.model.PMMLUtil;
import org.dmg.pmml.PMML;
import org.dmg.pmml.Model;

import java.io.File;
import java.io.FileInputStream;


@SpringBootApplication
public class IaToolsApplication {

	public static void main(String[] args) throws Exception {
		FileInputStream fis = new FileInputStream("src/main/resources/test-model.pmml");
	PMML pmml = PMMLUtil.unmarshal(fis);

	// Extraire le premier modèle
	Model model = pmml.getModels().get(0);

	// Créer un évaluateur
	ModelEvaluator<?> evaluator = ModelEvaluatorFactory.newInstance().newModelEvaluator(pmml, model);
	evaluator.verify();

	// Créer les features d’entrée
/*	Map<FieldName, FieldValue> arguments = new LinkedHashMap<>();
	FieldName inputField = new FieldName("input1");

	FieldValue inputValue = evaluator.prepare(inputField, 3.0);
	arguments.put(inputField, inputValue);

	// Évaluer le modèle
	Map<FieldName, ?> results = evaluator.evaluate(arguments);

	// Récupérer la sortie
	FieldName targetField = evaluator.getTargetFields().get(0).getName();
	Object predictedValue = results.get(targetField);

	System.out.println("Input: 3.0 → Predicted output: " + predictedValue); */
}
}