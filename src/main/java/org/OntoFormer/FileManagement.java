/*
This module is part of the master thesis "A prototype application for ontology based data acquisition".
Author: Christian Heterle
Institution: Friedrich-Alexander-Universität Erlangen-Nürnberg
Chair of Computer Science 6

OntoFormer V.0.1

FILEMANAGEMENT - Handle basic file related tasks.
 */
package org.OntoFormer;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.vocabulary.*;
import org.apache.jena.vocabulary.RDFS;

import javax.swing.*;
import java.io.*;
import java.nio.file.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileManagement {
    public static void main(String[] args) {
        /*
        // Chapter: Result
        generateRDFfiles();
        unifyallRDFfiles();
         */

        System.out.println("Done!");
    }


    public static void generateRDFfiles(){
        // Customized first task of model generation. Chapter: Result
        // create the .rdf files out of the collected data

        //String basePath = "/home/heterle/Workspace/OntoFormer/Test_MealCourse_Texts/";        // Testing
        String basePath = Main.getPathTxtFiles();
        String path = basePath + "/Meal_Course_Form.txt";
        textToRDFfile(path, "MealCourse.rdf");                      // this is the generic method
        System.out.println("Created: MealCourse.rdf");                      // Success message

        for (int i = 1; i < 30; i++){
            // path = "/home/heterle/Downloads/Meal_Course_Form(" + i + ").txt";                // Testing
            path = basePath + "/Meal_Course_Form(" + i + ").txt";
            textToRDFfile(path, "MealCourse" + i + ".rdf");         // Success message
            System.out.println("Created: MealCourse" + i + ".rdf");
        }

    }
    public static void unifyallRDFfiles(){
        // Customized second task. Chapter: Result
        // Unify all RDF MealCourse model into a single model
        // String basePath = "/home/heterle/Workspace/OntoFormer/Test_MealCourse_RDF/";     //Testing
        String basePath = Main.getPathRDFFiles();
        String mealcourse = basePath + "/MealCourse.rdf";
        List<Model> models = new ArrayList<Model>();
        Model mdl = RDFModel.loadRDFModel(mealcourse);
        models.add(mdl);

        for (int i = 1; i < 30; i++){
            mealcourse = basePath + "/MealCourse" + i+ ".rdf";
            mdl = RDFModel.loadRDFModel(mealcourse);
            models.add(mdl);
        }

        Model unifiedModel = RDFModel.unifyModels(models);
        RDFModel.writeToFile(unifiedModel,"UnifiedModels.rdf");
    }


    public static void textToRDFfile(String path, String filename){
        // path is a pointer to the location of the form-data.txt file.
        // filename describes the name of the output file. It is needed to specify the file ending. ".rdf" is recommended
        Model model = ModelFactory.createDefaultModel();
        String nsExample = getNamespace(path);            // extract the URI of the ongoing project.
                        // This method is expecting, that a namespace is defined in the respective .txt file.
                        // This is not generic, it is decided by convention, that a header needs to contain a URI.
                        // In upcoming projects, this might be different. For the proof of concept, this is the state.
                        // TODO: Try-Except part. Throw an exception, if namespace is empty

        if (nsExample == null){
            nsExample = "http://www.DefaultPlaceholder.com/Project#";
        }

        // important namespaces
        // further improvement: make use of the embedded Semantic Web (RDF-/RDFS-/OWL-) vocabulary
        /*
        String rdfNamespace = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
        String rdfType = rdfNamespace + "type";
        String rdfValue = rdfNamespace + "value";
        String rdfProperty = rdfNamespace + "Property";     // sic.!
        String rdfRelation = rdfNamespace + "predicate";
         */

        String rdfType =  String.valueOf(RDF.type);
        String rdfValue = String.valueOf(RDF.value);
        String rdfProperty = String.valueOf(RDF.Property);
        String rdfRelation = String.valueOf(RDF.predicate);

        List<String[]> triples = extractTriples(path);          // extracting all triples from the specified file
        for (String[] triple : triples) {
            String subject = formatString(triple[0]);           // format string to get rid of unwanted whitespaces
            String predicate = formatString(triple[1]);
            String object = formatString(triple[2]);

            // exclude namespace from triple extraction
            if (subject.contains("ns")){
                if (predicate.contains("is")){
                    continue;
                }
            }
            // fit the subject to a namespace
            if (subject.startsWith("http:") == false){
                subject = nsExample + subject;
            }

            // fit the predicate according to the keywords, that are contained in the form-data.txt file
            if (predicate.startsWith("http:") == false){
                if(predicate == "of_type" || predicate.endsWith("of_type")){
                    predicate = rdfType;
                }else if(predicate == "has_value" || predicate.endsWith("has_value")) {
                    predicate = rdfValue;
                }else if(predicate == "hasAttribute" || predicate.endsWith("hasAttribute")) {
                    predicate = rdfProperty;
                }else{
                    predicate = rdfRelation;
                }

            }
            // fit the object to a namespace
            if (object.startsWith("http:") == false){
                object = nsExample + object;
            }

            // inserting triples into the model - subject, predicate and object need to be URIs
            RDFModel.addTripleToModel(model, subject, predicate, object);
            // Success message
            // System.out.println(" Added triple [S:" + subject +"], [P:"+ predicate+ "], [O:" + object + "] to model.");
        }
        // write the configured model to a specified filename. It is recommended to use .rdf-file ending.
        // System.out.println(" Created file [" + filename + "]");
        RDFModel.writeToFile(model, filename);
    }

    public static String getNamespace(String path){
        List<String[]> triples = extractTriples(path);          // extracting all triples from the specified file
        String namespace = "";

        for (String[] triple : triples) {
            String subject = formatString(triple[0]);           // format string to get rid of unwanted whitespaces
            String predicate = formatString(triple[1]);
            String object = formatString(triple[2]);

            // Namespaces are written down in the header of the .txt file - so first line to fulfill the following criteria is identified
            if (subject.contains("ns")){
                if (predicate.contains("is")){
                    namespace = object;
                    break;
                }
            }
        }

        if(namespace == ""){
            namespace = null;
        }

        return namespace;
    }
    public static String formatString(String strToFormat){
        // Used for URI-formatting. If there are whitespace-characters in a URI-string, they need to be replaced by "_".
        // Otherwise, it will throw an exception.
        return strToFormat.replaceAll("\\s", "_");          // \s = wildcard for whitespace
    }

    public static List<String[]> extractTriples(String filePath) {
        // Method to extract all the triples from the form-data.txt file.
        // form-data.txt is achieved through submitting values in a form.
        // filePath is pointing to the destination of the wanted file.
        List<String[]> triples = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\"\\s+\""); // Split by " ", \s = wildcard for whitespace
                for (int i = 0; i < parts.length; i++) {
                    parts[i] = parts[i].replaceAll("\"", ""); // Remove quotes from each part
                }
                if (parts.length == 3) {        // Validation of triples, whether they consist of three parts
                    triples.add(parts);
                } else {
                    System.out.println("Invalid line: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return triples;
    }

        public static String ChooseFile() {
            // Opens up a file dialog that lets the user choose a file and returns the path to it
            final JFileChooser fc = new JFileChooser();
            fc.setCurrentDirectory(new File(System.getProperty("user.home")));
            // sets the source directory dynamically to the home
            int result = fc.showOpenDialog(null);
            String erg;

            if (result == JFileChooser.APPROVE_OPTION) {            // if a file is selected:
                File selectedFile = fc.getSelectedFile();
                System.out.println("Selected file: " + selectedFile.getAbsolutePath());
                erg = selectedFile.getAbsolutePath();
            }else {                                                 // if no file is selected:
                System.out.println("No file selected.");
                erg = null;
            }
            return erg;
        }

        public static List<String> ChooseFiles() {
            // Opens up a file dialog that lets the user choose multiple files and returns the paths to them
            final JFileChooser fc = new JFileChooser();
            fc.setCurrentDirectory(new File(System.getProperty("user.home")));
            fc.setMultiSelectionEnabled(true);                  // Enable multiple file selection
            int result = fc.showOpenDialog(null);
            List<String> selectedFiles = new ArrayList<>();

            if (result == JFileChooser.APPROVE_OPTION) {        // if files are selected:
                File[] files = fc.getSelectedFiles();
                for (File file : files) {
                    System.out.println("Selected file: " + file.getAbsolutePath());
                    selectedFiles.add(file.getAbsolutePath());
                }
            } else {                                            // if no files are selected:
                System.out.println("No files selected.");
            }

            return selectedFiles;
        }

        public static void CreateFile(String filePath) {
            // An auxiliary method to create a file. Be aware of the ending! (e.g. .rdf)
            try{
                Path newFilePath = Paths.get(filePath);
                Files.createFile(newFilePath);
                System.out.println("File " + filePath + " is created!");
            } catch (IOException e){
                System.err.println("Failed to create file" + filePath + ": " + e.getMessage());
            }

        }

        public static void CreateDirectory(String directoryPath){
            // An auxiliary method to create a directory through specifying an access path. No file ending needed.
            try {
                Path path = Paths.get(directoryPath);
                Files.createDirectories(path);
                System.out.println("Directory " + directoryPath + " is created!");

            } catch (IOException e) {
                System.err.println("Failed to create directory" + directoryPath + ": " + e.getMessage());
            }
        }

        public static Boolean ExistsPath(String path){
            // An auxiliary method to check if a file or directory exists under the given path.
            Path file = Paths.get(path);
            return Files.exists(file);
        }

        public static Boolean IsDirectory(String path){
            // An auxiliary method to check if the path is leading to a directory.
            File f = new File(path);
            return f.isDirectory();
        }

        public static Boolean IsFile(String path){
            // An auxiliary method to check if the path is leading to a file.
            File f = new File(path);
            return f.isFile();
        }

        public static void listAllFiles(String directoryPath){
            // An auxiliary method to list all files contained by a directory.
            File directory = new File(directoryPath);
            if(!directory.exists() || !directory.isDirectory()){
                System.out.println("Invalid path!");
            }else{
                File[] files = directory.listFiles();

                if (files != null){
                    for(File file : files){
                        if(file.isFile()){
                            System.out.println("File name:" + file.getName());
                        }
                    }
                }else{
                    System.out.println("The specified directory contains no files.");
                }
            }
        }
    }
