/*
This module is part of the master thesis "A prototype application for ontology based data acquisition".
Author: Christian Heterle
Institution: Friedrich-Alexander-Universität Erlangen-Nürnberg
Chair of Computer Science 6

OntoFormer V.0.1

RDFMODEL - Handling everything RDF related.
 */
package org.OntoFormer;

import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.*;
import org.apache.jena.riot.Lang;
import org.apache.jena.query.*;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;

/*  Unused in working code - used in development phase
import org.apache.jena.sparql.core.Var;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.VCARD;
import org.apache.jena.rdf.model.ResIterator;
import org.apache.jena.rdf.model.SimpleSelector;
*/

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import static org.OntoFormer.FileManagement.*;
import static org.OntoFormer.Main.IDENT;

public class RDFModel {

        public static void main(String[] args) {
            //exploreImportFile();
            //https://jena.apache.org/documentation/io/index.html
            // queryModel("");
            String path = "/home/heterle/Workspace/OntoFormer/UnifiedModels.rdf";
            // String path = "/home/heterle/Workspace/OntoFormer/FirstTry/UnifiedModels_eval1.rdf";
            execCompetenceQ(4,path);
            // execQuery1();
        }

        public static void testLiterals(){
            Model model = ModelFactory.createDefaultModel();
            Resource r = model.createResource();

            r.addProperty(RDFS.label, "11");
            r.addProperty(RDFS.label, model.createLiteral("chat", "en"));
            r.addProperty(RDF.type, model.createResource("http://example.com/Project#Wine"));

            model.write(System.out);


        }

        public static void BeAFool(Integer qNumber, String pathToModel){
            System.out.println("I was entered");
        }
        public static void execCompetenceQ(Integer qNumber, String pathToModel){
            String CompQ;

            switch(qNumber){
                case 1:
                    // SPARQL Query that lists all entities and their rdf:type
                    System.out.println("WARMUP: This query lists all entities and their rdf:type.");
                    CompQ = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"+
                            "SELECT ?entity ?type \n"+
                            "WHERE { \n"+
                            "?entity rdf:type ?type . \n"+
                            "}";
                    break;
                case 2:
                    // SPARQL Query for the question "Which attributes are captured for wine?"
                    System.out.println("COMPETENCE QUESTION 1: SPARQL query for the question 'Which attributes are captured for wine?'.");
                    CompQ = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"+
                            "SELECT ?attribute \n"+
                            "WHERE { \n"+
                            "?entity rdf:type <http://www.w3.org/TR/2004/REC-owl-guide-20040210/wine#Wine>. \n"+
                            "?entity rdf:predicate ?attribute. \n" +
                            "}";
                    break;
                case 3:
                    // SPARQL Query for the question "Which wines are ordered with the food 'Green salad'?"
                    System.out.println("COMPETENCE QUESTION 2: SPARQL query for the question 'Which wines are ordered with the food 'Green salad'?'.");
                    CompQ = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n" +
                            "SELECT ?mealCourse ?food ?wine \n"+
                            "WHERE { \n"+
                            "?entity rdf:type <http://www.w3.org/TR/2004/REC-owl-guide-20040210/wine#EdibleThing>. \n"+
                            "?entity rdf:value ?food. \n" +
                            "FILTER (CONTAINS(STR(?food), 'Green_salad')). \n" +
                            "?foodSection rdf:predicate ?entity. \n" +
                            "?mealCourse rdf:predicate ?foodSection. \n" +
                            "?mealCourse rdf:predicate ?wineSection. \n" +
                            "FILTER (CONTAINS(STR(?wineSection), 'wine_section')). \n" +
                            "?wineSection rdf:predicate ?name. \n"+
                            "FILTER (CONTAINS(STR(?name), 'wine_name')). \n" +
                            "?name rdf:value ?wine."+
                            "}";
                    break;

                case 4:
                    // SPARQL Query for the question "Which entries of 'EdibleThing' are ordered with White Wines?"
                    System.out.println("COMPETENCE QUESTION 3: SPARQL query for the question 'Which entries of 'EdibleThing' are ordered with white wines?'.");
                    CompQ = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n" +

                            // "SELECT ?food ?whiteWine ?name\n"+       // Bonus: also select the names
                            "SELECT ?food ?whiteWine \n"+
                            "WHERE { \n"+
                            "?edible rdf:type <http://www.w3.org/TR/2004/REC-owl-guide-20040210/wine#EdibleThing>. \n"+
                            "?foodSection rdf:predicate ?edible. \n" +
                            "?edible rdf:value ?food. \n" +
                            "?mealCourse rdf:predicate ?foodSection. \n" +
                            "?mealCourse rdf:predicate ?wineSection. \n" +
                            "FILTER (CONTAINS(STR(?wineSection), 'wine_section')). \n" +
                            "?wineSection rdf:predicate ?wineType. \n" +
                            "?wineSection rdf:predicate ?wineName. \n" +
                            "FILTER (CONTAINS(STR(?wineType), 'wine_type')). \n" +
                            "FILTER (CONTAINS(STR(?wineName), 'wine_name')). \n" +
                            "?wineType rdf:value ?whiteWine. \n"+
                            "?wineName rdf:value ?name.\n"+
                            "FILTER (CONTAINS(STR(?whiteWine), 'White_Wine')). \n" +
                            /*
                            //"?wineSection2 rdf:predicate ?wineName. \n" +             // needed for the bonus
                            //
                            // "?wineName rdf:value ?name.\n"+
                            //"?name rdf:type <http://www.w3.org/2001/XMLSchema#String> \n" +
                             */
                            "}";
                    break;
                default:
                    System.out.println("There is no competence question prepared for: " + qNumber);
                    CompQ = null;
            }

            Model model = loadRDFModel(pathToModel);
            if(model != null && CompQ != null){
                executeSPARQLQuery(model, CompQ);
            }else{
                if (model == null){System.out.println("Something went wrong for path [" + pathToModel +"]");}
                if (CompQ == null){System.out.println("You need to choose a valid number for the competence question!");}
            }

        }

        public static void exploreImportFile(){
            // Method to test import file and to test the different ontology formats: .owl, .xrdf, .xml

            Model model;
            String path;

            // String path = "";            // fileDialog option
            //String path = "/home/heterle/Workspace/Sandbox/src/main/java/org.example/rxno.owl";
            //model = importRDFModel(path, true);
            //System.out.println("------");

            //path = "/home/heterle/Workspace/Sandbox/ontologies/RXNO/rxno.xrdf";
            //model = importRDFModel(path, true);
            //System.out.println("------");

            path = "/home/heterle/Workspace/Sandbox/ontologies/RXNO/rxno.xml";
            model = importRDFModel(path, true);
            //Model model = importRDFModel(path, true);
            //printModel(model, true, true, true);
        }


        //  ----------- IMPORT TASK
        public static Model importRDFModel(String path, boolean writeOut){
            // import a RDF model via path or file dialog, if path = ""
            Model model;
            String fileName = "";
            if(path != null && !path.isEmpty()){
                // path is provided
                fileName = path;
            }else{
                // choose file via FileDialog
                fileName = ChooseFile();
            }

            if (fileName != null) {
                model = loadRDFModel(fileName);
                if (writeOut) {
                    writeOutRDF(model, "RDFXML");
                }
            } else {
                System.out.println("No file was chosen. Abort function.");
                model = null;
            }

            return model;
        }

        public static Model loadRDFModel (String inputFileName){
            // Take an inputFileName (filePath) and load it into a model

            // create an empty model
            Model model = ModelFactory.createDefaultModel();

            // use the RDFDataMgr to find the input file. Inputfile must be in the current directory!
            InputStream in = RDFDataMgr.open(inputFileName);
            if (in == null) {
                throw new IllegalArgumentException("File: " + inputFileName + " not found");
            }

            // read the RDF/XML file
            model.read(in, null);

            return model;
        }

        public static void writeToFile(Model model, String filename){
            // Write a model to an .rdf file with the specified filename(.rdf)
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
                model.write(writer);
                System.out.println("Created file [" + filename + "].");
            } catch (IOException e) {
                System.out.println("Error writing to RDF file: " + e.getMessage());
            }
        }

        //  ----------- EXPLORATION TASK

        public static void writeOutRDF (Model model, String form){
            // Method to write out a model in a certain format
            // fixed arguments! XML, DataMgr, NTRIPLES
            switch (form) {
                case "XML" -> model.write(System.out);
                case "RDFXML" -> RDFDataMgr.write(System.out, model, Lang.RDFXML);
                case "NTRIPLES" -> RDFDataMgr.write(System.out, model, Lang.NTRIPLES);
                default -> System.out.println("No valid form given");
            }

        }
        public static void printModel(Model model, Boolean bSubj, Boolean bPred, Boolean bObj){
            // prints all the statements in the model - similar to Method writeOutRDF
            // list the statements in the Model
            StmtIterator iter = model.listStatements();

            boolean printAll;
            if (bSubj && bPred && bObj){
                printAll = true;
            }else{
                printAll = false;
            }

            while (iter.hasNext()){
                Statement stmt = iter.nextStatement();          // get next statement

                Resource subject = stmt.getSubject();           // get the subject
                Property predicate = stmt.getPredicate();       // get the predicate
                RDFNode object = stmt.getObject();              // get the object

                if (printAll) {
                    // print a whole statement
                    System.out.print("S:[" + subject.toString() + "] ");
                    System.out.print("P:[" + predicate.toString()+ "] ");
                    // RDFNode object can be a resource or a literal
                    if (object instanceof Resource){
                        // object is a Resource
                        System.out.print("O:[" + object.toString() + "]\n");
                    }else {
                        // object is a literal
                        System.out.print("O:[\"" + object.toString() + "\"]\n");
                    }

                } else {
                    // print only the desired values
                    if (bSubj == true){System.out.println("S: [" + subject.toString() + "]");}
                    if (bPred == true){System.out.println("P: [" + predicate.toString() + "]");}
                    if (bObj == true){System.out.println("O: [" + object.toString() +"]");}

                }
            }

        }

    //  ----------- QUERYING TASK
    public static void queryModel(String path){
        // method to search certain entities in the specified ontology-file

        // String path = "/home/heterle/FAUbox/MASTER S5 Masterarbeit/food.rdf";           // food
        // String path = "/home/heterle/FAUbox/MASTER S5 Masterarbeit/wine.rdf";        // wine
        // String path = "";               // fileDialog
        System.out.println("Search for a model.");
        Model model = importRDFModel(path, false);
        String searchWord = acquireSearchString();
        extractMatches(model, searchWord);
        // String[] inputs = tripleUserInput();        // get user triplets
        // extractMatches(model, inputs[0], inputs[1], inputs[2]);
    }

    //  ----------- SPARQL-QUERYING

    public static void executeSPARQLQuery(Model model, String queryString){
        try (QueryExecution queryExec = QueryExecutionFactory.create(queryString, model)) {
            ResultSet resultSet = queryExec.execSelect();

            // Print out the query results
            ResultSetFormatter.out(resultSet);
        }
    }

    public static boolean anotherQuery(){

        boolean bAnswer = false;
        System.out.print("Do you want to request another query? (y/n): ");
        Scanner scanner = new Scanner(System.in);
        String userInput = scanner.nextLine();
        userInput = userInput.toLowerCase();

        if (userInput.equals("y") || userInput.equals("yes") || userInput.equals("true")) {
            System.out.println("Requesting another query...");
            bAnswer = true;
        } else if (userInput.equals("n") || userInput.equals("no") || userInput.equals("false")) {
            System.out.println("Exiting query handler.");
            bAnswer = false;
        } else {
            System.out.println("Invalid input. Please enter 'y', 'yes', 'true', 'n', 'no', or 'false'.");
            bAnswer=anotherQuery();
        }

        scanner.close();
        return bAnswer;
    }

    public static String acquireSearchString(){
        Scanner scanner = new Scanner(System.in);

        System.out.print("Please enter a keyword to search for: ");
        String result = scanner.nextLine().trim();
        System.out.print("You entered [" + result + "]");

        scanner.close();

        return result;
    }

    //  ----------- EXPLORE ONTOLOGY TASK

    public static void extractMatches(Model model, String searchString){
        List matches = new ArrayList();
        String Msg = "";

        // if they are NOT null or empty
        if(searchString != null && !searchString.isEmpty()){
            if(searchString.contains(" ")){
                searchString.replaceAll(" ", "_");
            }
            matches = selectStatement(model,  searchString);
            Msg += "[searchString: " + searchString + "] ";
        }

        if (matches != null || matches.size() == 0){
            System.out.println("There are the following matches for the input parameter(s) "+ Msg + ":");
            for(int i = 0; i < matches.size(); i++) System.out.println(matches.get(i));
        } else {
            System.out.println("There were no matches to be found. Input: " + Msg + ".");
        }
    }

    public static List selectStatement(Model model, String hint){
        // Usually, you need URIs to find Statements - selectStatement takes a hint and searches the URIs for them
        // For the OntoFormer application, we need a possibility to find resources or nodes based on search-/ keywords
        // Initially, it was planned to do a triple search, but it turned out, that it only works with getSubject()
        // URI in the model is like: http://example/meal#wine; but a search string would be "wine"

        String URI = "";
        List matches = new ArrayList();

        StmtIterator iter = model.listStatements();
        while(iter.hasNext()) {
            Statement temp = iter.nextStatement();

            URI = temp.getSubject().toString();

            if ((URI.endsWith(hint) == true || URI.contains(hint) == true) && !matches.contains(URI)) {
                // filter out duplicates
                matches.add(URI);
            }

        }

        if (matches.size() == 0){
            return null;
        }else{
            return matches;
        }

    }

    //  ----------- MANAGEMENT TASK
    public static void addTripleToModel(Model model, String Subject, String Predicate, String Object){
        // Args Subject, Predicate and Object need to be URLs.

        // System.out.println("Subject: " + Subject);
        // System.out.println("Predicate: " + Predicate);
        // System.out.println("Object: " + Object);
        Resource subj = model.createResource(Subject);
        Resource obj = model.createResource(Object);
        Property pred = model.createProperty(Predicate);

        model.add(subj, pred, obj);
    }

    public static void addResourceToModel(Model model, String ResourceURI){
        // getResource: return a resource object, if one exists in the given model. Otherwise: create a new one
        Resource resource = model.getResource(ResourceURI);
    }
    public static void addPropertyToResource(Model model, String resourceURI, Property property, String propName){
        Resource resource = model.getResource(resourceURI);
        resource.addProperty(property, propName);
    }


    //  ----------- MODEL UNIFICATION TASK
    public static Model unifyTwoModels(){
        Model mdl1 = importRDFModel("", false);
        Model mdl2 = importRDFModel("", false);

        Model unified = ModelFactory.createUnion(mdl1, mdl2);
        return unified;
    }

    public static Model unifyModels(List<Model> models){
        Model unified = ModelFactory.createDefaultModel();

        for (Model model:models){
            unified.add(model);
        }
        return unified;
    }


}

//  ----------- Deprecated EXTRACT MATCHES
// it turns out, that this way it is only possible to search for a subject. Therefor, it is obsolete to include the triple logic


        /*
        public static void extractMatches(Model model, String Subject, String Predicate, String Object){
            // extract matches
            // Beware: Jena core API supports only a limited query primitive; More powerful: SPARQL

            List matches = new ArrayList();
            String Msg = "";

            // if they are NOT null or empty
            if(Subject != null && !Subject.isEmpty()){
                if(Subject.contains(" ")){
                    Subject.replaceAll(" ", "_");
                }
                matches = selectStatement(model, "Subject", Subject);
                Msg += "[Subject: " + Subject + "] ";
            }

            if(Predicate != null && !Predicate.isEmpty()){
                if(Predicate.contains(" ")){
                    Predicate.replaceAll(" ", "_");
                }
                matches = selectStatement(model, "Predicate", Predicate);
                Msg += "[Predicate: " + Predicate+ "] ";
            }

            if(Object != null && !Object.isEmpty()){
                if(Object.contains(" ")){
                    Object.replaceAll(" ", "_");
                }
                matches = selectStatement(model, "Object", Object);
                Msg += "[Object: " + Object + "] ";
            }

            if (matches != null || matches.size() == 0){
                System.out.println("There are the following matches for the input parameter(s) "+ Msg + ":");
                for(int i = 0; i < matches.size(); i++) System.out.println(matches.get(i));
            } else {
                System.out.println("There were no matches to be found. Input: " + Msg + ".");
            }

        }

        public static List selectStatement(Model model, String type, String hint){
            // Usually, you need URIs to find Statements - selectStatement takes a hint and searches the URIs for them
            // For the OntoFormer application, we need a possibility to find resources or nodes based on search-/ keywords
            // URI in the model is like: http://example/meal#wine; but a search string would be "wine"
            // Also it only works for single types of hints: Subject OR Predicate OR Object

            String URI = "";
            List matches = new ArrayList();

            StmtIterator iter = model.listStatements();
            while(iter.hasNext()) {
                Statement temp = iter.nextStatement();

                if (Objects.equals(type, "Subject")) {
                    URI = temp.getSubject().toString();

                } else if (Objects.equals(type, "Predicate")) {
                    URI = temp.getPredicate().toString();

                } else if (Objects.equals(type, "Object")) {
                    URI = temp.getObject().toString();
                }

                if ((URI.endsWith(hint) == true || URI.contains(hint) == true) && !matches.contains(URI)) {
                    // filter out duplicates
                    matches.add(URI);
                }

            }

            if (matches.size() == 0){
                return null;
            }else{
                return matches;
            }

        }

        public static String[] tripleUserInput(){
            Scanner scanner = new Scanner(System.in);
            String[] result = new String[3];

            System.out.print("Please enter a subject: ");
            String subject = scanner.nextLine().trim();
            if (subject == ""){subject = null;}

            System.out.print("Please enter a predicate: ");
            String predicate = scanner.nextLine().trim();
            if (predicate == ""){predicate = null;}

            System.out.print("Please enter an object: ");
            String object = scanner.nextLine().trim();
            if (object == ""){object = null;}

            result[0]= subject;
            result[1]= predicate;
            result[2]= object;

            System.out.print("Print out the triple [true/false]? ");
            boolean bPrint = scanner.nextBoolean();

            if (bPrint == true){
                // print out the user-provided Subject, Predicate, and Object
                System.out.println("Subject: " + subject);
                System.out.println("Predicate: " + predicate);
                System.out.println("Object: " + object);
            }

            scanner.close();

            return result;
        }

         */


