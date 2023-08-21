/*
This module is part of the master thesis "A prototype application for ontology based data acquisition".
Author: Christian Heterle - https://github.com/ChHet
Institution: Friedrich-Alexander-Universität Erlangen-Nürnberg
Chair of Computer Science 6

OntoFormer V.0.1

MAIN - This is for handling the main tasks in OntoFormer
 */
package org.OntoFormer;

import org.apache.jena.rdf.model.Model;
import org.apache.poi.ss.formula.functions.Choose;

import javax.swing.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

// import org.apache.jena.rdf.model.*;

import static org.OntoFormer.FileManagement.*;
import static org.OntoFormer.RDFModel.*;
import static org.OntoFormer.FormGraph.*;


public class Main {
        public static final String IDENT = "identification";            // Relevant constant for dataset identification.
    public static void main(String[] args) {
            boolean devMode = false;
            List<String> filepaths = new ArrayList<>();

            System.out.println(" ------------------------------------------- ");
            System.out.println(" ONTOFORMER API - V.0.1");
            System.out.println(" ------------------------------------------- ");

            if (devMode){
                generateRDFfiles();
                unifyallRDFfiles();
            }else{
                if(args.length == 0) {

                    System.out.println(" NO ARGUMENT WAS ENTERED. TRY RECALLING THE APPLICATION WITH ONE OF THE FOLLOWING ARGUMENTS:");
                    System.out.println(" 'MealCourse'         - ");
                    System.out.println(" 'ExploreOntology'    - ");
                    System.out.println(" 'CompetenceQuestion' - ");
                    System.out.println(" 'Conversion'         - ");
                    System.out.println(" 'UnifyModels'        - ");
                    System.out.println(" ------------------------------------------- ");
                    System.out.println(" Shutting down.");
                    System.out.println(" ------------------------------------------- ");

                    return;
                }

            for (String arg : args) {
                switch (arg) {
                    case "MealCourse":
                        System.out.println(" ARGUMENT: 'MealCourse' ");
                        System.out.println(" Create a preconfigured MealCourse.html-form!");
                        System.out.println(" ------------------------------------------- ");
                        System.out.println(" NOTE: In this version of ONTOFORMER it is not possible to create other forms in the compiled application.");
                        System.out.println("       For further insights consult the FormGraph.java-source code!");
                        System.out.println("       This case is for verification of the associated master thesis.");
                        System.out.println(" ------------------------------------------- ");
                        createMealCourseForm("MealCourse.html");
                        break;
                    case "ExploreOntology":
                        System.out.println(" ARGUMENT: 'MealCourse' ");
                        System.out.println(" Query a ontology file and search for keywords. Attention, if no file is chosen, it throws an exeption!");
                        queryModel("");
                        break;
                    case "CompetenceQuestion":
                        System.out.println(" ARGUMENT: 'MealCourse' ");
                        System.out.println(" Different SPARQL-queries are executed on chosen models and their results are shown.");
                        System.out.println(" ------------------------------------------- ");
                        System.out.println(" First, choose the model [UnifiedModels.rdf] to perform the queries on. Try abort for checking default path.");
                        //Model model = RDFModel.importRDFModel("", false);
                        String path = ChooseFile();
                        if (path == null) {
                            System.out.println(" No valid path was chosen. Checking for the UnifiedModels.rdf file in the specified default path.");
                            path = getPathToModels() + "/UnifiedModels.rdf";
                            if (!ExistsPath(path)) {
                                System.out.println("No valid path was given. Shutting down program.");
                                break;
                            } else {
                                System.out.println("Found path: " + path);
                            }

                        }

                        int userInput = 0;
                        boolean validInput = false;

                        System.out.println(" ------------------------------------------- ");
                        System.out.println(" Input | Description");
                        System.out.println("  1    | Warmup - This query lists all entities and their rdf:type.");
                        System.out.println("  2    | CompQ1 - Which attributes are captured for wine?");
                        System.out.println("  3    | CompQ2 - Which wines are ordered with the food 'Green salad'?.");
                        System.out.println("  4    | CompQ3 - Which entries of 'EdibleThing' are ordered with white wines?");
                        System.out.println("  0    | Abort interactions.");
                        System.out.println(" ------------------------------------------- ");
                        System.out.println(" Please choose the query to execute on the chosen model. ");
                        Scanner scanner = new Scanner(System.in);
                        while (!validInput) {
                            try {
                                userInput = scanner.nextInt();
                                validInput = true;
                            } catch (InputMismatchException e) {
                                System.out.println(" Invalid input. Please reenter an integer for an action.");
                                userInput = scanner.nextInt();         // Clear input buffer
                            }
                        }
                        if (userInput == 0 || userInput > 4 || userInput < 0) {
                            System.out.println(" Closing down the function. Option for abort was chosen or no valid option was provided. ");
                        } else {

                            try {
                                System.out.println("UserInput: " + userInput + ", Path: " + path + ".");
                                // Trying to create an instance of a non-existent class
                                RDFModel.execCompetenceQ(userInput, path);
                                // BeAFool(userInput, path);
                            } catch (NoClassDefFoundError e) {
                                // Catch the exception and print out details
                                System.out.println("NoClassDefFoundError caught:");
                                System.out.println("Class not found: " + e.getMessage());
                                System.out.println("Class name: " + e.getClass());
                                System.out.println("Class PackageName: " + e.getClass().getPackageName());
                                System.out.println("Class simpleName " + e.getClass().getSimpleName());
                                System.out.println("Currently looking at: " + System.getProperty("user.dir"));
                                e.printStackTrace();


                            }

                        }
                        scanner.close();
                        break;

                    case "Conversion":
                        System.out.println(" ARGUMENT: 'Conversion' ");
                        System.out.println(" Choose some triple containing [form].txt-files to convert them to .rdf-files.");
                        System.out.println(" ------------------------------------------- ");
                        filepaths=ChooseFiles();
                        if(filepaths.isEmpty() || filepaths == null){
                            System.out.println("No files were chosen for conversion. Shutting down");
                        }else{
                            System.out.println("(" + filepaths.size() + ") files were chosen for unification.");
                            TxtToRDF(filepaths);
                        }
                        break;

                    case "UnifyModels":
                        System.out.println(" ARGUMENT: 'UnifyModels' ");
                        System.out.println(" Choose some .rdf-files to create one unified model.");
                        System.out.println(" ------------------------------------------- ");
                        filepaths = ChooseFiles();
                        if(filepaths.isEmpty() || filepaths == null){
                            System.out.println(" No files were chosen for unification. Shutting down");
                        }else{
                            System.out.println(" (" + filepaths.size() + ") files were chosen for unification.");
                            UnifyModels(filepaths);
                        }
                        break;

                    default:
                        System.out.println("No valid case presented. Shutting down.");
                        break;
                }
            }
        }
            System.out.println(" ------------------------------------------- ");
            System.out.println(" TASK DONE!");
            System.out.println(" ------------------------------------------- ");


            // createWineForm("wine_form.html");
            // testRecipe("recipe.html");
            // path = "/home/heterle/FAUbox/MASTER S5 Masterarbeit/Ontologies/food.rdf"
            // path = ""

            // ask for CHEBI_2634 in queryModel in rxno.rdf
            // String[] inputs = RDFModel.tripleUserInput();
            // Model model = RDFModel.loadRDFModel();

            // Show case: queryModel
            // String path = "";                   // "" triggers the filedialog
            // RDFModel.queryModel(path);

            // Show case: create Form
            // createMealCourseForm("MealCourse3.html");
            // System.out.println("Done!");
    }

    public static void TxtToRDF(List<String> paths){
        int i = 0;
        String baseOutput = "OutputFile";
        String genericOutput;
        for (String path:paths){
            if (i == 0){ genericOutput = baseOutput + ".rdf";
            }else{
                genericOutput = baseOutput + "("+ i +").rdf";
            }
            textToRDFfile(path,genericOutput);
            i++;
        }
    }

    public static void UnifyModels(List<String> paths) {
        int i = 0;
        String output = "UnifiedModels.rdf";
        List<Model> models = new ArrayList<>();
        Model model;

        for (String path:paths){
            model = loadRDFModel(path);
            if (model != null){
                models.add(model);
            }else{
                System.out.println("No mo1del for path: [" + path + "].");
            }
        }
        model = unifyModels(models);
        writeToFile(model,"UnifiedModels.rdf");
    }
    public static String getPathRDFFiles(){
        String currentDirectory = System.getProperty("user.dir");
        String path = currentDirectory + "/MealCourse_RDF";
        return path;

    }

    public static String getPathTxtFiles(){
        String currentDirectory = System.getProperty("user.dir");
        String path = currentDirectory + "/MealCourse_Text";
        return path;
    }

    public static String getPathToModels(){
        String currentDirectory = System.getProperty("user.dir");
        String path = currentDirectory + "/Models";
        return path;
    }
    public static String getPathToOntologies(){
        String currentDirectory = System.getProperty("user.dir");
        String path = currentDirectory + "/Ontologies";
        return path;
}


    public static void testScanner(){
            Scanner sc= new Scanner(System.in); //System.in is a standard input stream
            System.out.print("Enter a string: ");
            String str= sc.nextLine();              //reads string
            System.out.print("You have entered: "+str + " ");
            sc.close();

    }
    public static void createMealCourseForm(String htmlName){
            String title = "Meal Course Form";
            String header = "Meal Course Form";

            FormGraph formGraph = new FormGraph(title, header);

            String exampleType = "http://thisIsAnExample.com/Entities";
            String exampleRelation = "http://thisIsAnExample.com/Relationship";

            String rdfNamespace = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
            String rdfsNamespace = "http://www.w3.org/2000/01/rdf-schema#";
            String xsdNamespace = "http://www.w3.org/2001/XMLSchema#";

            // food.rdf and wine.rdf - Ontology class embedding for objects
            String typeMealCourse= "http://www.w3.org/TR/2004/REC-owl-guide-20040210/wine#MealCourse";
            String typeWine = "http://www.w3.org/TR/2004/REC-owl-guide-20040210/wine#Wine";
            String typeRegion = "http://www.w3.org/TR/2004/REC-owl-guide-20040210/wine#Region";
            String typeTaste = "http://www.w3.org/TR/2004/REC-owl-guide-20040210/wine#WineTaste";
            String typeMeal = "http://www.w3.org/TR/2004/REC-owl-guide-20040210/wine#Meal";
            String typeEdibleThing = "http://www.w3.org/TR/2004/REC-owl-guide-20040210/wine#EdibleThing";

            // examples - placeholder classes for additional objects, that have no ontology class representation in food.rdf and wine.rdf
            String typeString = xsdNamespace + "string";
            String typeFloat =  xsdNamespace + "decimal";

            // food.rdf and wine.rdf - Ontology class embedding for relationships
            String relHasColor = "http://www.w3.org/TR/2003/PR-owl-guide-20031215/wine#hasColor";
            String relLocatedIn = "http://www.w3.org/TR/2004/REC-owl-guide-20040210/wine#locatedIn";
            String relHasFood = "http://www.w3.org/TR/2004/REC-owl-guide-20040210/wine#hasFood";
            String relHasDrink = "http://www.w3.org/TR/2004/REC-owl-guide-20040210/wine#hasDrink";


            // example - placeholder for "hasAttribute", that has no ontology class representation in food.rdf and wine.rdf
            String relHasAttribute = rdfsNamespace + "Property";

            formGraph.typifyNode("MealCourse", typeMealCourse);

            // Identify dataset -
            formGraph.addSection(IDENT, IDENT, "ID");
            formGraph.addInputField(IDENT,"ident1", IDENT, "baseURI");
            formGraph.addInputField(IDENT,"ident2", IDENT, "Course Number");


            // Wine section
            formGraph.addSection("wine_section", typeWine, "Wine");
            formGraph.addInputField("wine_section", "wine_name", typeString,  "Name:");

            List<String> wineTypes = new ArrayList<>();
            wineTypes.add("");
            wineTypes.add("Red Wine");
            wineTypes.add("White Wine");
            wineTypes.add("Rosé");
            wineTypes.add("Fortified Wine");
            wineTypes.add("Sparkling Wine");
            formGraph.addSelectOptions("wine_section", "wine_type", typeString, "Type:", wineTypes);

            formGraph.addInputField("wine_section", "wine_region", typeRegion,  "Region of Origin:");
            formGraph.addInputField("wine_section", "wine_alcohol", typeFloat,"Degree of Alcohol:", "number");

            List<String> wineTastes = new ArrayList<>();
            wineTastes.add("");
            wineTastes.add("Fruity");
            wineTastes.add("Dry");
            wineTastes.add("Sweet");
            wineTastes.add("Tart");
            wineTastes.add("Tannic");
            wineTastes.add("Oaky");
            wineTastes.add("Earthy");
            wineTastes.add("Spicy");
            wineTastes.add("Floral");
            wineTastes.add("Herbaceous");
            formGraph.addSelectOptions("wine_section", "wine_tastes", typeTaste,"Tastes:", wineTastes, true);


            // Dish section
            formGraph.addSection("dish_section", typeMeal,"Dish");
            formGraph.addInputField("dish_section", "dish_name", typeString, "Name:");

            List<String> dishTastes = new ArrayList<>();
            dishTastes.add("");
            dishTastes.add("Sweet");
            dishTastes.add("Savory");
            dishTastes.add("Spicy");
            dishTastes.add("Sour");
            dishTastes.add("Salty");
            dishTastes.add("Bitter");
            dishTastes.add("Tangy");
            dishTastes.add("Smoky");
            dishTastes.add("Rich");
            dishTastes.add("Herbaceous");
            formGraph.addSelectOptions("dish_section", "dish_tastes", typeString,"Tastes:", dishTastes, true);

            formGraph.addInputField("dish_section", "dish_main_ingredient", typeEdibleThing,"Main Ingredient:");
            formGraph.addInputField("dish_section", "dish_side", typeEdibleThing,"Side:");

            // System.out.println("Pass 3");

            // Establish relationships
            formGraph.addRelation("MealCourse", "wine_section", relHasDrink);
            formGraph.addRelation("wine_section",  "wine_name", relHasAttribute);
            // String relHasAttribute2 = relHasAttribute + "2";    // Testing reasons
            formGraph.addRelation("wine_section", "wine_type", relHasAttribute);
            // Identify type_of relationship
            // formGraph.typifyNode(relHasAttribute2, relHasColor, false);
            // formGraph.typifyNode(relHasAttribute2, relHasColor);
            formGraph.addRelation("wine_section", "wine_region",relLocatedIn); // locatedIn
            formGraph.addRelation("wine_section", "wine_alcohol",relHasAttribute);
            formGraph.addRelation("wine_section", "wine_tastes", relHasAttribute);

            formGraph.addRelation("MealCourse", "dish_section", relHasFood);
            formGraph.addRelation("dish_section", "dish_name",relHasAttribute);
            formGraph.addRelation("dish_section", "dish_tastes",relHasAttribute);
            formGraph.addRelation("dish_section", "dish_main_ingredient",relHasAttribute);
            formGraph.addRelation("dish_section", "dish_side", relHasAttribute);

            // System.out.println("Pass 4");


            formGraph.toHTML(htmlName);
    }

    /*

    public static void createMealCourseForm(String htmlName){
            // fits to V5
            String title = "Meal Course Form";
            String header = "Meal Course Form";

            FormGraph formGraph = new FormGraph(title, header);

            String typeWine = "http://www.w3.org/TR/2004/REC-owl-guide-20040210/wine#Wine";
            String typeMealCourse = "https://www.w3.org/TR/2003/PR-owl-guide-20031215/food#MealCourse";
            String typeDish = "https://www.w3.org/TR/2003/PR-owl-guide-20031215/food#Meal";

            // Wine section
            // Update type
            formGraph.addSection("http://www.w3.org/TR/2004/REC-owl-guide-20040210/wine#Wine", "Wine");
            // Update type
            formGraph.addInputField("http://www.w3.org/TR/2004/REC-owl-guide-20040210/wine#Wine", "http://www.w3.org/TR/2004/REC-owl-guide-20040210/wine#Wine", "Name:");

            List<String> wineTypes = new ArrayList<>();
            wineTypes.add("");
            wineTypes.add("Red Wine");
            wineTypes.add("White Wine");
            wineTypes.add("Rosé");
            wineTypes.add("Fortified Wine");
            wineTypes.add("Sparkling Wine");
            // Update type
            formGraph.addSelectOptions("http://www.w3.org/TR/2004/REC-owl-guide-20040210/wine#Wine", "wine_type", "Type:", wineTypes);

            // Update type
            formGraph.addInputField("http://www.w3.org/TR/2004/REC-owl-guide-20040210/wine#Wine", "wine_region", "Region of Origin:");
            formGraph.addInputField("http://www.w3.org/TR/2004/REC-owl-guide-20040210/wine#Wine", "wine_alcohol", "Degree of Alcohol:", "", "number");

            List<String> wineTastes = new ArrayList<>();
            wineTastes.add("");
            wineTastes.add("Fruity");
            wineTastes.add("Dry");
            wineTastes.add("Sweet");
            wineTastes.add("Tart");
            wineTastes.add("Tannic");
            wineTastes.add("Oaky");
            wineTastes.add("Earthy");
            wineTastes.add("Spicy");
            wineTastes.add("Floral");
            wineTastes.add("Herbaceous");

            // Update type
            formGraph.addSelectOptions("http://www.w3.org/TR/2004/REC-owl-guide-20040210/wine#Wine", "wine_tastes", "" , "Tastes:", wineTastes, true);

            // Dish section
            // Update type
            formGraph.addSection("dish_section", "" ,"Dish");
            // Update type
            formGraph.addInputField("dish_section", "dish_name", "",  "Name:");

            List<String> dishTastes = new ArrayList<>();
            dishTastes.add("");
            dishTastes.add("Sweet");
            dishTastes.add("Savory");
            dishTastes.add("Spicy");
            dishTastes.add("Sour");
            dishTastes.add("Salty");
            dishTastes.add("Bitter");
            dishTastes.add("Tangy");
            dishTastes.add("Smoky");
            dishTastes.add("Rich");
            dishTastes.add("Herbaceous");
            // Update type
            formGraph.addSelectOptions("dish_section", "dish_tastes", "", "Tastes:", dishTastes, true);

            // Update type
            formGraph.addInputField("dish_section", "dish_main_ingredient", "Main Ingredient:");
            formGraph.addInputField("dish_section", "dish_side", "Side:");

            // Establish relationships
            formGraph.addRelation("form", "http://www.w3.org/TR/2004/REC-owl-guide-20040210/wine#Wine");
            formGraph.addRelation("http://www.w3.org/TR/2004/REC-owl-guide-20040210/wine#Wine", "wine_name");
            formGraph.addRelation("http://www.w3.org/TR/2004/REC-owl-guide-20040210/wine#Wine", "wine_type");
            formGraph.addRelation("http://www.w3.org/TR/2004/REC-owl-guide-20040210/wine#Wine", "wine_region");
            formGraph.addRelation("http://www.w3.org/TR/2004/REC-owl-guide-20040210/wine#Wine", "wine_alcohol");
            formGraph.addRelation("http://www.w3.org/TR/2004/REC-owl-guide-20040210/wine#Wine", "wine_tastes");

            formGraph.addRelation("form", "dish_section");
            formGraph.addRelation("dish_section", "dish_name");
            formGraph.addRelation("dish_section", "dish_tastes");
            formGraph.addRelation("dish_section", "dish_main_ingredient");
            formGraph.addRelation("dish_section", "dish_side");

            formGraph.toHTML(htmlName);
        }

     */

/* Only compatible with FormGraph V3!
    public static void createWineForm(String htmlName){

        String title = "Wine Form";
        String header = "Wine Form";

        FormGraph formGraph = new FormGraph(title, header);

        formGraph.addInputField("name", "Name:");

        List<String> wineTypes = new ArrayList<>();
        wineTypes.add("");
        wineTypes.add("Red Wine");
        wineTypes.add("White Wine");
        wineTypes.add("Rosé");
        wineTypes.add("Fortified Wine");
        wineTypes.add("Sparkling Wine");
        formGraph.addSelectOptions("type", "Type:", wineTypes);

        formGraph.addInputField("region", "Region of Origin:");
        formGraph.addInputField("alcohol", "Degree of Alcohol:", "0", "number");

        List<String> wineTastes = new ArrayList<>();
        wineTastes.add("");
        wineTastes.add("Fruity");
        wineTastes.add("Dry");
        wineTastes.add("Sweet");
        wineTastes.add("Tart");
        wineTastes.add("Tannic");
        wineTastes.add("Oaky");
        wineTastes.add("Earthy");
        wineTastes.add("Spicy");
        wineTastes.add("Floral");
        wineTastes.add("Herbaceous");
        formGraph.addSelectOptions("tastes", "Tastes:", wineTastes, true);

        formGraph.addRelation("wine", "name");
        formGraph.addRelation("wine", "type");
        formGraph.addRelation("wine", "region");
        formGraph.addRelation("wine", "alcohol");
        formGraph.addRelation("wine", "tastes");

        formGraph.toHTML(htmlName);

    }

    public static void testRecipe(String htmlName){
        String title = "Recipe Form";
        String header = "Recipe Form";

        FormGraph formGraph = new FormGraph(title, header);

        formGraph.addInputField("recipe_name", "Recipe Name:");

        // Add ingredients section
        //formGraph.addLabel("ingredients_label", "Ingredients");
        formGraph.addInputField("ingredient_name", "Name:");
        formGraph.addInputField("ingredient_quantity", "Quantity:");

        // Add instructions section
        //formGraph.addLabel("instructions_label", "Instructions");
        formGraph.addInputField("instruction_step", "Step:");

        // Establish relationships
        formGraph.addRelation("recipe", "recipe_name");
        formGraph.addRelation("recipe", "ingredients_label");
        formGraph.addRelation("ingredients_label", "ingredient_name");
        formGraph.addRelation("ingredients_label", "ingredient_quantity");
        formGraph.addRelation("recipe", "instructions_label");
        formGraph.addRelation("instructions_label", "instruction_step");

        formGraph.toHTML(htmlName);
    }
    */
}







     /*       FormGraph graph = new FormGraph();

        // Create nodes for attributes of the wine
        graph.addLabel("wine_label", "Wine");

        // Name
        graph.addLabel("name", "Name:");
        graph.addInputField("name", "");

        // Type
        List<String> wineTypes = new ArrayList<>();
        wineTypes.add("");
        wineTypes.add("Red Wine");
        wineTypes.add("White Wine");
        wineTypes.add("Rosé");
        wineTypes.add("Fortified Wine");
        wineTypes.add("Sparkling Wine");

        graph.addLabel("type", "Type:");
        graph.addSelectOptions("type", wineTypes);

        // Region of Origin
        graph.addLabel("region", "Region of Origin:");
        graph.addInputField("region", "");

        // Degree of Alcohol
        graph.addLabel("alcohol", "Degree of Alcohol:");
        graph.addInputField("alcohol", "", "0");

        // Tastes
        List<String> wineTastes = new ArrayList<>();
        wineTastes.add("");
        wineTastes.add("Fruity");
        wineTastes.add("Dry");
        wineTastes.add("Sweet");
        wineTastes.add("Tart");
        wineTastes.add("Tannic");
        wineTastes.add("Oaky");
        wineTastes.add("Earthy");
        wineTastes.add("Spicy");
        wineTastes.add("Floral");
        wineTastes.add("Herbaceous");

        graph.addLabel("tastes", "Tastes:");
        graph.addSelectOptions("tastes", wineTastes, true);

        // Add relations between nodes to form a hierarchy
        graph.addRelation("wine_label", "name");
        graph.addRelation("wine_label", "type");
        graph.addRelation("wine_label", "region");
        graph.addRelation("wine_label", "alcohol");
        graph.addRelation("wine_label", "tastes");

        graph.toHTML("wine_form2.html");*/




        /*FormGraph graph = new FormGraph();

        List<String> options = new ArrayList<>();
        options.add("");
        options.add("Red Wine");
        options.add("White Wine");
        options.add("Rosè");
        options.add("Sparkling Wine");
        options.add("Fortified Wine");

        graph.addLabel("select_options_label", "Wine:");
        graph.addSelectOptions("select_options", options);
        graph.addSelectOptions("multi_select_options", options, false);

        graph.toHTML("FormGraphTest2.html"); */


   /*
    public static void main(String[] args) {
        String s;

        for (int i = 0; i < args.length; i++) {
            s = args[i];

            switch (s) {
                case "one":
                    System.out.println(args[i] + " -- case: one");
                    break;

                case "loadModel":
                    //String path = args[i+1];
                    String path = "/home/heterle/Workspace/Sandbox/ontologies/RXNO/rxno.xml";

                    if (path.equals("")) {
                        System.out.println(args[i] + " No path provided.");
                    } else {
                        System.out.println(args[i] + " -- path: ");
                    }
                    Model model = importRDFModel(path, true);
                    System.out.println("Done.");
                    break;

                case "three":
                    System.out.println(args[i] + " -- case: three");
                    break;

                default:
                    System.out.println("No valid argument given: " + s);
            }
            //}


            //System.out.println("Hello world!");
        }


    }*/
