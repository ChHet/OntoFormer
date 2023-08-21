package org.OntoFormer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Graveyard {
}

    /* to embed in the ToHTML-method

        writer.write("} else if (inputField.value === 'ROOT') {\n");
        writer.write("var values = pair[1].split(',');\n");
        writer.write("for (var i = 0; i < values.length; i++) {\n");
        writer.write("content += '\"' + pair[0] + '\" \"is\" \"' + values[i].trim() + '\"\\n';\n");
        writer.write("}\n");
        writer.write("}\n");

     */

    /* Method for FormGraph for identifying a node as roots
    public void typifyNode(String id, String datatype, boolean root){
        //String sectionId, String id, String datatype, String label, String type, String value
        if(root == true){
            InputFieldNode inputFieldNode = new InputFieldNode(id, datatype, "", "hidden", "ROOT");
            nodes.put(id, inputFieldNode);
        }else{
            InputFieldNode inputFieldNode = new InputFieldNode(id, datatype, "", "hidden", datatype);
            nodes.put(id, inputFieldNode);
        }
    }
     */

/* FileManagement with root-conception // deprecated but maybe makes sense later on
    public static void textToRDFfile(String path, String filename){
        Model model = ModelFactory.createDefaultModel();

        String nsExample = getNamespace(path);      // the URI of the ongoing project
        // important namespaces
        String rdfNamespace = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
        String rdfType = rdfNamespace + "type";
        String rdfValue = rdfNamespace + "value";
        String rdfProperty = rdfNamespace + "Property";
        String rdfRelation = rdfNamespace + "predicate";

        /*
        // first: Handle root
        List<String[]> rootTriples = getRoot(path);
        for (String[] triple : rootTriples) {
            String subject = formatString(triple[0]);
            String predicate = formatString(triple[1]);
            String object =  formatString(triple[2]);

            RDFModel.addTripleToModel(model, subject, predicate, object);
            System.out.println("Root: [S:" + subject +"], [P:"+ predicate+ "], [O:" + object + "] added to model.");
        }


    List<String[]> triples = extractTriples(path);
        for (String[] triple : triples) {
                String subject = formatString(triple[0]);
                String predicate = formatString(triple[1]);
                String object = formatString(triple[2]);

                // exclude namespace
                if (subject.contains("ns")){
                if (predicate.contains("is")){
                continue;
                }
                }

            // exclude root
            if (predicate.contains("is")){
                if (object.contains("ROOT")){           // ROOT constant
                    continue;
                }
            }


                if (subject.startsWith("http:") == false){
                subject = nsExample + subject;
                }


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

                if (object.startsWith("http:") == false){
                object = nsExample + object;
                }

                // inserting triples into the model
                RDFModel.addTripleToModel(model, subject, predicate, object);
                // Success message
                System.out.println("Added triple [S:" + subject +"], [P:"+ predicate+ "], [O:" + object + "] to model.");


                }

                RDFModel.writeToFile(model, filename);
                }

    /*
    public static List<String[]> getRoot(String filePath) {
        List<String[]> triples = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\"\\s+\""); // Split by " ", \s = wildcard for whitespace
                for (int i = 0; i < parts.length; i++) {
                    parts[i] = parts[i].replaceAll("\"", ""); // Remove quotes from each part
                }

                if (parts.length == 3) {
                    if(parts[1].contains("is") && parts[2].contains("ROOT")){
                        triples.add(parts);
                    }
                } else {
                    System.out.println("Invalid line: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return triples;
    }
    /*
    public static String[] getRoot(String filePath) throws IOException {
        List<String> triples = new ArrayList<>();

        // Read the file and extract triples
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                triples.add(line.trim());
            }
        }

        // Define the regular expression pattern to find "variable is ROOT" triples
        Pattern pattern = Pattern.compile("(.+)\\s+is\\s+ROOT", Pattern.CASE_INSENSITIVE);

        // Search for the pattern in the triples and return the root
        for (String triple : triples) {
            Matcher matcher = pattern.matcher(triple);
            if (matcher.find()) {
                String root = matcher.group(1).trim();
                return new String[]{root};
            }
        }

        // If no root is found, return null
        return null;
    }

     */

// ---------------------
/*
 private abstract class Node {
        // V6 - Node!
        protected String id;
        protected List<Node> neighbors;
        protected String datatype; // Add type attribute

        public Node(String id, String datatype) { // Update constructor to accept type
            this.id = id;
            this.datatype = datatype;               // Type resembles to RDF:type or RDFS:type
            neighbors = new ArrayList<>();
        }

        public abstract void toHTML(BufferedWriter writer) throws IOException;

        public void addNeighbor(Node neighbor) {
            neighbors.add(neighbor);
        }
    }

    private class Relation {
        private String subject;
        private String predicate;
        private String object;
        private String datatype;

        public Relation(String subject, String predicate, String object, String datatype) {
            this.subject = subject;
            this.predicate = predicate;
            this.object = object;
            this.datatype = datatype;
        }

        public String getSubject() {
            return subject;
        }

        public String getPredicate() {
            return predicate;
        }

        public String getObject() {
            return object;
        }

        public String getType() {
            return datatype;
        }
    }

    private class InputFieldNode extends FormGraph.Node {
        private String label;
        private String value;
        private String type;

        public InputFieldNode(String id, String datatype, String label,String type, String value) {
            super(id, datatype);
            this.label = label;
            this.type = type;
            this.value = value;
        }

        @Override
        public void toHTML(BufferedWriter writer) throws IOException {
            writer.write("<label for=\"" + id + "\">" + label + "</label><br>\n");

            writer.write("<input type=\"" + type + "\" id=\"" + id + "\" name=\"" + id + "\"");
            if (value != null) {
                writer.write(" value=\"" + value + "\"");
            }
            writer.write(" data-type=\"" + datatype + "\"><br><br>\n");

            for (Node neighbor : neighbors) {
                neighbor.toHTML(writer);
            }
        }
    }

    private class SelectOptionsNode extends FormGraph.Node {
        private String label;
        private List<String> options;
        private boolean multiple;

        public SelectOptionsNode(String id, String datatype, String label, List<String> options, boolean multiple) {
            super(id, datatype);
            this.label = label;
            this.options = options;
            this.multiple = multiple;
        }

        @Override
        public void toHTML(BufferedWriter writer) throws IOException {
            writer.write("<label for=\"" + id + "\">" + label + "</label><br>\n");

            writer.write("<select id=\"" + id + "\" name=\"" + id + "\"");
            if (multiple) {
                writer.write(" multiple");
            }
            writer.write(" data-type=\"" + datatype + "\">\n");

            for (String option : options) {
                writer.write("<option value=\"" + option + "\">" + option + "</option>\n");
            }

            writer.write("</select><br><br>\n");

            for (Node neighbor : neighbors) {
                neighbor.toHTML(writer);
            }
        }
    }

    private class SectionNode extends FormGraph.Node {
        private String label;

        public SectionNode(String id, String datatype, String label) {
            super(id, datatype);
            this.label = label;
        }

        @Override
        public void toHTML(BufferedWriter writer) throws IOException {
            writer.write("<fieldset data-type=\"" + datatype + "\">\n");
            writer.write("<legend>" + label + "</legend>\n");

            for (Node neighbor : neighbors) {
                neighbor.toHTML(writer);
            }

            writer.write("</fieldset>\n");
        }
    }

 */



/*
public class FormGraph {
    // V5
    private String title;
    private String header;
    private Map<String, Node> nodes;
    private boolean submitButtonAdded;
    private List<String> relations;

    public FormGraph(String title, String header) {
        this.title = title;
        this.header = header;
        nodes = new HashMap<>();
        submitButtonAdded = false;
        relations = new ArrayList<>();
    }

    public void addInputField(String sectionId, String id, String label) {
        addInputField(sectionId, id, label, null, "text");
    }

    public void addInputField(String sectionId, String id, String label, String value) {
        addInputField(sectionId, id, label, value, "text");
    }

    public void addInputField(String sectionId, String id, String label, String value, String type) {
        InputFieldNode inputFieldNode = new InputFieldNode(id, label, value, type);
        addNode(sectionId, inputFieldNode);
    }

    public void addSelectOptions(String sectionId, String id, String label, List<String> options) {
        addSelectOptions(sectionId, id, label, options, false);
    }

    public void addSelectOptions(String sectionId, String id, String label, List<String> options, boolean multiple) {
        SelectOptionsNode selectOptionsNode = new SelectOptionsNode(id, label, options, multiple);
        addNode(sectionId, selectOptionsNode);
    }

    public void addSection(String id, String label) {
        SectionNode sectionNode = new SectionNode(id, label);
        nodes.put(id, sectionNode);
    }

    public void addRelation(String subject, String object) {
        addRelation(subject, "has_attribute", object);
    }

    public void addRelation(String subject, String predicate, String object) {
        String formattedSubject = "\"" + subject + "\"";
        String formattedPredicate = "\"" + predicate + "\"";
        String formattedObject = "\"" + object + "\"";
        String relation = formattedSubject + " " + formattedPredicate + " " + formattedObject;
        relations.add(relation);
        System.out.println("Relation: " + relation);
    }

    public void toHTML(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write("<html>\n");
            writer.write("<head>\n");
            writer.write("<title>" + title + "</title>\n");
            writer.write("<meta charset=\"UTF-8\">\n");
            writer.write("</head>\n");
            writer.write("<body>\n");
            writer.write("<h1>" + header + "</h1>\n");
            writer.write("<form id=\"form\">\n");
            for (Node node : nodes.values()) {
                node.toHTML(writer);
            }
            if (!submitButtonAdded) {
                writer.write("<button type=\"button\" onclick=\"handleSubmit()\">Submit</button>\n");
            }
            writer.write("</form>\n");
            writer.write("<script>\n");
            writer.write("function handleSubmit() {\n");
            writer.write("var form = document.getElementById(\"form\");\n");
            writer.write("var formData = new FormData(form);\n");
            writer.write("var content = '';\n");
            writer.write("for (var pair of formData.entries()) {\n");
            writer.write("content += '\"' + pair[0] + '\" \"has_value\" \"' + pair[1] + '\"\\n';\n");
            writer.write("}\n");

            for (String relation : relations) {
                System.out.println("Submitted relation: " +  relation);
                // -- writer.write("content += " + relation + " + \"\\n\";\n");
                // writer.write("content += `\"" + relation + "\"` + \"\\n\";\n");
                writer.write("content += `" + relation + "` + \"\\n\";\n");             // status quo

            }

            //
            for (String relation : relations) {
                writer.write("content += " + relation.replace("\" \"", "\" + \"") + " + \"\\n\";\n");
            }
            //

            writer.write("var blob = new Blob([content], { type: \"text/plain\" });\n");
                    writer.write("var anchor = document.createElement(\"a\");\n");
                    writer.write("anchor.download = \"form-data.txt\";\n");
                    writer.write("anchor.href = URL.createObjectURL(blob);\n");
                    writer.write("anchor.click();\n");
                    writer.write("}\n");
                    writer.write("</script>\n");
                    writer.write("</body>\n");
                    writer.write("</html>");
                    } catch (IOException e) {
                    System.out.println("An error occurred while writing to the file.");
                    }
                    }

private void addNode(String sectionId, Node node) {
        if (nodes.containsKey(sectionId)) {
        Node sectionNode = nodes.get(sectionId);
        sectionNode.addNeighbor(node);
        } else {
        System.out.println("Section with ID \"" + sectionId + "\" does not exist.");
        }
        }

private abstract class Node {
    protected String id;
    protected List<FormGraph.Node> neighbors;

    public Node(String id) {
        this.id = id;
        neighbors = new ArrayList<>();
    }

    public abstract void toHTML(BufferedWriter writer) throws IOException;

    public void addNeighbor(FormGraph.Node neighbor) {
        neighbors.add(neighbor);
    }
}

private class InputFieldNode extends FormGraph.Node {
    private String label;
    private String value;
    private String type;

    public InputFieldNode(String id, String label, String value, String type) {
        super(id);
        this.label = label;
        this.value = value;
        this.type = type;
    }

    @Override
    public void toHTML(BufferedWriter writer) throws IOException {
        writer.write("<label for=\"" + id + "\">" + label + "</label><br>\n");

        writer.write("<input type=\"" + type + "\" id=\"" + id + "\" name=\"" + id + "\"");
        if (value != null) {
            writer.write(" value=\"" + value + "\"");
        }
        writer.write("><br><br>\n");

        for (FormGraph.Node neighbor : neighbors) {
            neighbor.toHTML(writer);
        }
    }
}

private class SelectOptionsNode extends FormGraph.Node {
    private String label;
    private List<String> options;
    private boolean multiple;

    public SelectOptionsNode(String id, String label, List<String> options, boolean multiple) {
        super(id);
        this.label = label;
        this.options = options;
        this.multiple = multiple;
    }

    @Override
    public void toHTML(BufferedWriter writer) throws IOException {
        writer.write("<label for=\"" + id + "\">" + label + "</label><br>\n");

        writer.write("<select id=\"" + id + "\" name=\"" + id + "\"");
        if (multiple) {
            writer.write(" multiple");
        }
        writer.write(">\n");

        for (String option : options) {
            writer.write("<option value=\"" + option + "\">" + option + "</option>\n");
        }

        writer.write("</select><br><br>\n");

        for (FormGraph.Node neighbor : neighbors) {
            neighbor.toHTML(writer);
        }
    }
}

private class SectionNode extends FormGraph.Node {
    private String label;

    public SectionNode(String id, String label) {
        super(id);
        this.label = label;
    }

    @Override
    public void toHTML(BufferedWriter writer) throws IOException {
        writer.write("<fieldset>\n");
        writer.write("<legend>" + label + "</legend>\n");

        for (FormGraph.Node neighbor : neighbors) {
            neighbor.toHTML(writer);
        }

        writer.write("</fieldset>\n");
    }
}
}

*/

/*
public class FormGraph {
    // V4
    private Map<String, Node> nodes;
    private boolean submitButtonAdded;
    private String title;
    private String header;

    public FormGraph(String title, String header) {
        nodes = new HashMap<>();
        submitButtonAdded = false;
        this.title = title;
        this.header = header;
    }

    public void addInputField(String sectionId, String id, String label) {
        addInputField(sectionId, id, label, null, "text");
    }

    public void addInputField(String sectionId, String id, String label, String value) {
        addInputField(sectionId, id, label, value, "text");
    }

    public void addInputField(String sectionId, String id, String label, String value, String type) {
        InputFieldNode inputFieldNode = new InputFieldNode(id, label, value, type);
        addNode(sectionId, inputFieldNode);
    }

    public void addSelectOptions(String sectionId, String id, String label, List<String> options) {
        addSelectOptions(sectionId, id, label, options, false);
    }

    public void addSelectOptions(String sectionId, String id, String label, List<String> options, boolean multiple) {
        SelectOptionsNode selectOptionsNode = new SelectOptionsNode(id, label, options, multiple);
        addNode(sectionId, selectOptionsNode);
    }

    public void addSection(String id, String label) {
        SectionNode sectionNode = new SectionNode(id, label);
        nodes.put(id, sectionNode);
    }

    public void addRelation(String fromId, String toId) {
        if (nodes.containsKey(fromId) && nodes.containsKey(toId)) {
            Node fromNode = nodes.get(fromId);
            Node toNode = nodes.get(toId);
            fromNode.addNeighbor(toNode);
        }
    }


    public void toHTML(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write("<html>\n");
            writer.write("<head>\n");
            writer.write("<title>" + title + "</title>\n");
            writer.write("</head>\n");
            writer.write("<body>\n");
            writer.write("<h1>" + header + "</h1>\n");
            writer.write("<form id=\"form\">\n");
            for (Node node : nodes.values()) {
                node.toHTML(writer);
            }
            if (!submitButtonAdded) {
                writer.write("<button type=\"button\" onclick=\"handleSubmit()\">Submit</button>\n");
            }
            writer.write("</form>\n");
            writer.write("<script>\n");
            writer.write("function handleSubmit() {\n");
            writer.write("var form = document.getElementById(\"form\");\n");
            writer.write("var formData = new FormData(form);\n");
            writer.write("var content = '';\n");
            writer.write("for (var pair of formData.entries()) {\n");
            writer.write("content += pair[0] + \": \" + pair[1] + \",\\n\";\n");
            writer.write("}\n");
            writer.write("var blob = new Blob([content], { type: \"text/plain\" });\n");
            writer.write("var anchor = document.createElement(\"a\");\n");
            writer.write("anchor.download = \"form-data.txt\";\n");
            writer.write("anchor.href = URL.createObjectURL(blob);\n");
            writer.write("anchor.click();\n");
            writer.write("}\n");
            writer.write("</script>\n");
            writer.write("</body>\n");
            writer.write("</html>");
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
        }
    }



    private void addNode(String sectionId, Node node) {
        if (nodes.containsKey(sectionId)) {
            Node sectionNode = nodes.get(sectionId);
            sectionNode.addNeighbor(node);
        } else {
            System.out.println("Section with ID \"" + sectionId + "\" does not exist.");
        }
    }

    private abstract class Node {
        protected String id;
        protected List<Node> neighbors;

        public Node(String id) {
            this.id = id;
            neighbors = new ArrayList<>();
        }

        public abstract void toHTML(BufferedWriter writer) throws IOException;

        public void addNeighbor(Node neighbor) {
            neighbors.add(neighbor);
        }
    }

    private class InputFieldNode extends Node {
        private String label;
        private String value;
        private String type;

        public InputFieldNode(String id, String label, String value, String type) {
            super(id);
            this.label = label;
            this.value = value;
            this.type = type;
        }

        @Override
        public void toHTML(BufferedWriter writer) throws IOException {
            writer.write("<label for=\"" + id + "\">" + label + "</label><br>\n");

            writer.write("<input type=\"" + type + "\" id=\"" + id + "\" name=\"" + id + "\"");
            if (value != null) {
                writer.write(" value=\"" + value + "\"");
            }
            writer.write("><br><br>\n");

            for (Node neighbor : neighbors) {
                neighbor.toHTML(writer);
            }
        }
    }

    private class SelectOptionsNode extends Node {
        private String label;
        private List<String> options;
        private boolean multiple;

        public SelectOptionsNode(String id, String label, List<String> options, boolean multiple) {
            super(id);
            this.label = label;
            this.options = options;
            this.multiple = multiple;
        }

        @Override
        public void toHTML(BufferedWriter writer) throws IOException {
            writer.write("<label for=\"" + id + "\">" + label + "</label><br>\n");

            writer.write("<select id=\"" + id + "\" name=\"" + id + "\"");
            if (multiple) {
                writer.write(" multiple");
            }
            writer.write(">\n");

            for (String option : options) {
                writer.write("<option value=\"" + option + "\">" + option + "</option>\n");
            }

            writer.write("</select><br><br>\n");

            for (Node neighbor : neighbors) {
                neighbor.toHTML(writer);
            }
        }
    }

    private class SectionNode extends Node {
        private String label;

        public SectionNode(String id, String label) {
            super(id);
            this.label = label;
        }

        @Override
        public void toHTML(BufferedWriter writer) throws IOException {
            writer.write("<fieldset>\n");
            writer.write("<legend>" + label + "</legend>\n");

            for (Node neighbor : neighbors) {
                neighbor.toHTML(writer);
            }

            writer.write("</fieldset>\n");
        }
    }
}

 */





/*
public class FormGraph {
    // V3
    private Map<String, Node> nodes;
    private boolean submitButtonAdded;
    private String title;
    private String header;

    public FormGraph(String title, String header) {
        nodes = new HashMap<>();
        submitButtonAdded = false;
        this.title = title;
        this.header = header;
    }

    public void addInputField(String id, String label) {
        addInputField(id, label, null, "text");
    }

    public void addInputField(String id, String label, String value) {
        addInputField(id, label, value, "text");
    }

    public void addInputField(String id, String label, String value, String type) {
        InputFieldNode inputFieldNode = new InputFieldNode(id, label, value, type);
        nodes.put(id, inputFieldNode);
    }

    public void addLabel(String id, String text) {
        LabelNode labelNode = new LabelNode(id, text);
        nodes.put(id, labelNode);
    }

    public void addSelectOptions(String id, String label, List<String> options) {
        addSelectOptions(id, label, options, false);
    }

    public void addSelectOptions(String id, String label, List<String> options, boolean multiple) {
        SelectOptionsNode selectOptionsNode = new SelectOptionsNode(id, label, options, multiple);
        nodes.put(id, selectOptionsNode);
    }

    public void addRelation(String fromId, String toId) {
        if (nodes.containsKey(fromId) && nodes.containsKey(toId)) {
            Node fromNode = nodes.get(fromId);
            Node toNode = nodes.get(toId);
            fromNode.addNeighbor(toNode);
        }
    }

    public void toHTML(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write("<html>\n");
            writer.write("<head>\n");
            writer.write("<title>" + title + "</title>\n");
            writer.write("</head>\n");
            writer.write("<body>\n");
            writer.write("<h1>" + header + "</h1>\n");
            for (Node node : nodes.values()) {
                node.toHTML(writer);
            }
            if (!submitButtonAdded) {
                writer.write("<button type=\"button\" onclick=\"handleSubmit()\">Submit</button>\n");
            }
            writer.write("<script>\n");
            writer.write("function handleSubmit() {\n");
            writer.write("var inputs = document.getElementsByTagName(\"input\");\n");
            writer.write("var selects = document.getElementsByTagName(\"select\");\n");
            writer.write("var content = \"\";\n");
            writer.write("for (var i = 0; i < inputs.length; i++) {\n");
            writer.write("content += inputs[i].id + \", \" + inputs[i].value + \",\\n\";\n");
            writer.write("}\n");
            writer.write("for (var j = 0; j < selects.length; j++) {\n");
            writer.write("var selectedOptions = [];\n");
            writer.write("var options = selects[j].options;\n");
            writer.write("for (var k = 0; k < options.length; k++) {\n");
            writer.write("if (options[k].selected) {\n");
            writer.write("selectedOptions.push(options[k].value);\n");
            writer.write("}\n");
            writer.write("}\n");
            writer.write("content += selects[j].id + \", \" + selectedOptions.join(\", \") + \",\\n\";\n");
            writer.write("}\n");
            writer.write("var blob = new Blob([content], { type: \"text/plain\" });\n");
            writer.write("var anchor = document.createElement(\"a\");\n");
            writer.write("anchor.download = \"form-data.txt\";\n");
            writer.write("anchor.href = URL.createObjectURL(blob);\n");
            writer.write("anchor.click();\n");
            writer.write("}\n");
            writer.write("</script>\n");
            writer.write("</body>\n");
            writer.write("</html>");
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
        }
    }

    private abstract class Node {
        protected String id;
        protected List<Node> neighbors;

        public Node(String id) {
            this.id = id;
            neighbors = new ArrayList<>();
        }

        public abstract void toHTML(BufferedWriter writer) throws IOException;

        public void addNeighbor(Node neighbor) {
            neighbors.add(neighbor);
        }
    }

    private class InputFieldNode extends Node {
        private String label;
        private String value;
        private String type;

        public InputFieldNode(String id, String label, String value, String type) {
            super(id);
            this.label = label;
            this.value = value;
            this.type = type;
        }

        @Override
        public void toHTML(BufferedWriter writer) throws IOException {
            writer.write("<label for=\"" + id + "\">" + label + "</label><br>\n");

            writer.write("<input type=\"" + type + "\" id=\"" + id + "\" name=\"" + id + "\"");
            if (value != null) {
                writer.write(" value=\"" + value + "\"");
            }
            writer.write("><br><br>\n");

            for (Node neighbor : neighbors) {
                neighbor.toHTML(writer);
            }
        }
    }

    private class LabelNode extends Node {
        private String text;

        public LabelNode(String id, String text) {
            super(id);
            this.text = text;
        }

        @Override
        public void toHTML(BufferedWriter writer) throws IOException {
            writer.write("<label>" + text + "</label><br><br>\n");

            for (Node neighbor : neighbors) {
                neighbor.toHTML(writer);
            }
        }
    }

    private class SelectOptionsNode extends Node {
        private String label;
        private List<String> options;
        private boolean multiple;

        public SelectOptionsNode(String id, String label, List<String> options, boolean multiple) {
            super(id);
            this.label = label;
            this.options = options;
            this.multiple = multiple;
        }

        @Override
        public void toHTML(BufferedWriter writer) throws IOException {
            writer.write("<label for=\"" + id + "\">" + label + "</label><br>\n");

            writer.write("<select id=\"" + id + "\" name=\"" + id + "\"");
            if (multiple) {
                writer.write(" multiple");
            }
            writer.write(">\n");

            for (String option : options) {
                writer.write("<option value=\"" + option + "\">" + option + "</option>\n");
            }

            writer.write("</select><br><br>\n");

            for (Node neighbor : neighbors) {
                neighbor.toHTML(writer);
            }
        }
    }
}


 */

/*
public class FormGraph {
    // V2
    private Map<String, Node> nodes;
    private boolean submitButtonAdded;

    public FormGraph() {
        nodes = new HashMap<>();
        submitButtonAdded = false;
    }

    public void addInputField(String id, String label) {
        addInputField(id, label, null);
    }

    public void addInputField(String id, String label, String value) {
        InputFieldNode inputFieldNode = new InputFieldNode(id, label, value);
        nodes.put(id, inputFieldNode);
    }

    public void addLabel(String id, String text) {
        LabelNode labelNode = new LabelNode(id, text);
        nodes.put(id, labelNode);
    }

    public void addSelectOptions(String id, List<String> options) {
        addSelectOptions(id, options, false);
    }

    public void addSelectOptions(String id, List<String> options, boolean multiple) {
        SelectOptionsNode selectOptionsNode = new SelectOptionsNode(id, options, multiple);
        nodes.put(id, selectOptionsNode);
    }

    public void addRelation(String fromId, String toId) {
        if (nodes.containsKey(fromId) && nodes.containsKey(toId)) {
            Node fromNode = nodes.get(fromId);
            Node toNode = nodes.get(toId);
            fromNode.addNeighbor(toNode);
        }
    }

    public void toHTML(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write("<html>\n");
            writer.write("<body>\n");
            for (Node node : nodes.values()) {
                node.toHTML(writer);
            }
            if (!submitButtonAdded) {
                writer.write("<button type=\"button\" onclick=\"handleSubmit()\">Submit</button>\n");
            }
            writer.write("<script>\n");
            writer.write("function handleSubmit() {\n");
            writer.write("var inputs = document.getElementsByTagName(\"input\");\n");
            writer.write("var selects = document.getElementsByTagName(\"select\");\n");
            writer.write("var content = \"\";\n");
            writer.write("for (var i = 0; i < inputs.length; i++) {\n");
            writer.write("content += inputs[i].id + \", \" + inputs[i].value + \",\\n\";\n");
            writer.write("}\n");
            writer.write("for (var j = 0; j < selects.length; j++) {\n");
            writer.write("var selectedOptions = [];\n");
            writer.write("var options = selects[j].options;\n");
            writer.write("for (var k = 0; k < options.length; k++) {\n");
            writer.write("if (options[k].selected) {\n");
            writer.write("selectedOptions.push(options[k].value);\n");
            writer.write("}\n");
            writer.write("}\n");
            writer.write("content += selects[j].id + \", \" + selectedOptions.join(\", \") + \",\\n\";\n");
            writer.write("}\n");
            writer.write("var blob = new Blob([content], { type: \"text/plain\" });\n");
            writer.write("var anchor = document.createElement(\"a\");\n");
            writer.write("anchor.download = \"form-data.txt\";\n");
            writer.write("anchor.href = URL.createObjectURL(blob);\n");
            writer.write("anchor.click();\n");
            writer.write("}\n");
            writer.write("</script>\n");
            writer.write("</body>\n");
            writer.write("</html>");
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
        }
    }

    private abstract class Node {
        protected String id;
        protected List<Node> neighbors;

        public Node(String id) {
            this.id = id;
            neighbors = new ArrayList<>();
        }

        public abstract void toHTML(BufferedWriter writer) throws IOException;

        public void addNeighbor(Node neighbor) {
            neighbors.add(neighbor);
        }
    }

    private class InputFieldNode extends Node {
        private String label;
        private String value;

        public InputFieldNode(String id, String label, String value) {
            super(id);
            this.label = label;
            this.value = value;
        }

        @Override
        public void toHTML(BufferedWriter writer) throws IOException {
            writer.write("<label for=\"" + id + "\">" + label + "</label><br>\n");

            writer.write("<input type=\"text\" id=\"" + id + "\" name=\"" + id + "\"");
            if (value != null) {
                writer.write(" value=\"" + value + "\"");
            }
            writer.write("><br><br>\n");

            for (Node neighbor : neighbors) {
                neighbor.toHTML(writer);
            }
        }
    }

    private class LabelNode extends Node {
        private String text;

        public LabelNode(String id, String text) {
            super(id);
            this.text = text;
        }

        @Override
        public void toHTML(BufferedWriter writer) throws IOException {
            writer.write("<label>" + text + "</label><br><br>\n");

            for (Node neighbor : neighbors) {
                neighbor.toHTML(writer);
            }
        }
    }

    private class SelectOptionsNode extends Node {
        private List<String> options;
        private boolean multiple;

        public SelectOptionsNode(String id, List<String> options, boolean multiple) {
            super(id);
            this.options = options;
            this.multiple = multiple;
        }

        @Override
        public void toHTML(BufferedWriter writer) throws IOException {
            writer.write("<label for=\"" + id + "\">Select an option:</label><br>\n");

            writer.write("<select id=\"" + id + "\" name=\"" + id + "\"");
            if (multiple) {
                writer.write(" multiple");
            }
            writer.write(">\n");

            for (String option : options) {
                writer.write("<option value=\"" + option + "\">" + option + "</option>\n");
            }

            writer.write("</select><br><br>\n");

            for (Node neighbor : neighbors) {
                neighbor.toHTML(writer);
            }
        }
    }
}


public class FormGraph {
    // V1
    private Map<String, Node> nodes;

    public FormGraph() {
        nodes = new HashMap<>();
    }

    public void addInputField(String id, String label) {
        addInputField(id, label, null);
    }

    public void addInputField(String id, String label, String value) {
        InputFieldNode inputFieldNode = new InputFieldNode(id, label, value);
        nodes.put(id, inputFieldNode);
    }

    public void addLabel(String id, String text) {
        LabelNode labelNode = new LabelNode(id, text);
        nodes.put(id, labelNode);
    }

    public void addSelectOptions(String id, List<String> options) {
        addSelectOptions(id, options, false);
    }

    public void addSelectOptions(String id, List<String> options, boolean multiple) {
        SelectOptionsNode selectOptionsNode = new SelectOptionsNode(id, options, multiple);
        nodes.put(id, selectOptionsNode);
    }

    public void addRelation(String fromId, String toId) {
        if (nodes.containsKey(fromId) && nodes.containsKey(toId)) {
            Node fromNode = nodes.get(fromId);
            Node toNode = nodes.get(toId);
            fromNode.addNeighbor(toNode);
        }
    }

    public void toHTML(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write("<html>\n");
            writer.write("<body>\n");
            for (Node node : nodes.values()) {
                node.toHTML(writer);
            }
            writer.write("</body>\n");
            writer.write("</html>");
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
        }
    }

    private abstract class Node {
        protected String id;
        protected List<Node> neighbors;

        public Node(String id) {
            this.id = id;
            neighbors = new ArrayList<>();
        }

        public abstract void toHTML(BufferedWriter writer) throws IOException;

        public void addNeighbor(Node neighbor) {
            neighbors.add(neighbor);
        }
    }

    private class InputFieldNode extends Node {
        private String label;
        private String value;

        public InputFieldNode(String id, String label, String value) {
            super(id);
            this.label = label;
            this.value = value;
        }

        @Override
        public void toHTML(BufferedWriter writer) throws IOException {
            writer.write("<label for=\"" + id + "\">" + label + "</label><br>\n");

            writer.write("<input type=\"text\" id=\"" + id + "\" name=\"" + id + "\"");
            if (value != null) {
                writer.write(" value=\"" + value + "\"");
            }
            writer.write("><br><br>\n");

            for (Node neighbor : neighbors) {
                neighbor.toHTML(writer);
            }
        }
    }

    private class LabelNode extends Node {
        private String text;

        public LabelNode(String id, String text) {
            super(id);
            this.text = text;
        }

        @Override
        public void toHTML(BufferedWriter writer) throws IOException {
            writer.write("<label>" + text + "</label><br><br>\n");

            for (Node neighbor : neighbors) {
                neighbor.toHTML(writer);
            }
        }
    }

    private class SelectOptionsNode extends Node {
        private List<String> options;
        private boolean multiple;

        public SelectOptionsNode(String id, List<String> options, boolean multiple) {
            super(id);
            this.options = options;
            this.multiple = multiple;
        }

        @Override
        public void toHTML(BufferedWriter writer) throws IOException {
            writer.write("<label for=\"" + id + "\">Select an option:</label><br>\n");

            writer.write("<select id=\"" + id + "\" name=\"" + id + "\"");
            if (multiple) {
                writer.write(" multiple");
            }
            writer.write(">\n");

            for (String option : options) {
                writer.write("<option value=\"" + option + "\">" + option + "</option>\n");
            }

            writer.write("</select><br><br>\n");

            for (Node neighbor : neighbors) {
                neighbor.toHTML(writer);
            }
        }
    }
}


// ----------------

public class Form {
    // V0
    static String title;        // title, header for the HTML

    class inputField(){

    }

    class selectField(){

    }

    class label(){

    }
}

     */

