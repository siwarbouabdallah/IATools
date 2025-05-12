package com.example.iatools;
import org.jpmml.evaluator.*;
import org.jpmml.model.PMMLUtil;

import java.io.File;
import java.io.FileInputStream;
import org.dmg.pmml.FieldName;
import org.dmg.pmml.PMML;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication
public class IaToolsApplication {

    public static void main(String[] args) {
        SpringApplication.run(IaToolsApplication.class, args);

        // ---------- 1) Charger le fichier PMML ----------
        String pmmlPath = "src/main/resources/your-model.pmml";
        if (!Files.exists(Paths.get(pmmlPath))) {
            System.err.println("Fichier PMML introuvable : " + pmmlPath);
            return;
        }

        try (FileInputStream fis = new FileInputStream(pmmlPath)) {
            PMML pmml = PMMLUtil.unmarshal(fis);
            System.out.println("PMML loaded successfully");

            // ---------- 2) Construire l'évaluateur ----------
            Evaluator evaluator = new LoadingModelEvaluatorBuilder()
                                        .load(new File("src/main/resources/your-model.pmml"))
                                        // optionnel : active les optimisations tableaux
                                        .build();

            // Vérifier que tout est OK
            evaluator.verify();

            // ---------- 3) Afficher les champs attendus ----------
            List<InputField> inputFields = evaluator.getInputFields();
            System.out.println("Input fields:");
            inputFields.forEach(f ->
                System.out.println(" - " + f.getName() + " (" + f.getDataType() + ", " + f.getOpType() + ")")
            );

            // ---------- 4) Préparer les arguments ----------
            Map<FieldName, FieldValue> arguments = new LinkedHashMap<>();
            for (InputField inputField : inputFields) {
                FieldName name   = inputField.getName();   // ici « input1 »
                Object    rawVal = 3.0;                    // valeur de test
                FieldValue val   = inputField.prepare(rawVal);
                arguments.put(name, val);
            }
            System.out.println("Arguments: " + arguments);

            // ---------- 5) Évaluer ----------
            Map<FieldName, ?> results = evaluator.evaluate(arguments);

            // ---------- 6) Extraire la prédiction ----------
            FieldName targetName = evaluator.getTargetFields().get(0).getName(); // « output »
            Object prediction     = results.get(targetName);

            System.out.println("Prediction = " + prediction); // attendu : 11.0

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
