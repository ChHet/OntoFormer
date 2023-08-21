/*
This module is part of the master thesis "A prototype application for ontology based data acquisition".
Author: Christian Heterle
Institution: Friedrich-Alexander-Universität Erlangen-Nürnberg
Chair of Computer Science 6

OntoFormer V.0.1

FORMGRAPH - Configuring and printing HTML-forms.
 */
package org.OntoFormer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.OntoFormer.Main.IDENT;        // import the constant keyword.


public class FormGraph {
    private String title;
    private String header;
    private Map<String, Node> nodes;
    private boolean submitButtonAdded;
    private List<Relation> relations;

    public FormGraph(String title, String header) {
        this.title = title;                 // Define HTML document title
        this.header = header;               // Define HTML document header
        nodes = new HashMap<>();            // Contains all nodes of the graph
        submitButtonAdded = false;
        relations = new ArrayList<>();
    }

    /* Meaning of all the following constructor arguments:
    sectionId =
    id =
     */
    public void addInputField(String sectionId, String id, String datatype, String label, String type) {
        addInputField(sectionId, id, datatype, label, type, "");
    }

    public void addInputField(String sectionId, String id, String datatype, String label) {
        addInputField(sectionId, id, datatype, label, "text", "");
    }

    public void addInputField(String sectionId, String id, String datatype, String label, String type, String value) {
        // datatype = ontologic class
        // type = type of HTML-input element
        InputFieldNode inputFieldNode = new InputFieldNode(id, datatype, label, type, value);
        addNode(sectionId, inputFieldNode);
    }

    public void addSelectOptions(String sectionId, String id, String type, String label, List<String> options) {
        addSelectOptions(sectionId, id, type, label, options, false);
    }

    public void addSelectOptions(String sectionId, String id, String type, String label, List<String> options, boolean multiple) {
        SelectOptionsNode selectOptionsNode = new SelectOptionsNode(id, type, label, options, multiple);
        addNode(sectionId, selectOptionsNode);
    }

    public void typifyNode(String id, String datatype){
        InputFieldNode inputFieldNode = new InputFieldNode(id, datatype, "", "hidden", datatype);
        nodes.put(id, inputFieldNode);
    }

    public void addSection(String id, String type, String label) {
        SectionNode sectionNode = new SectionNode(id, type, label);
        nodes.put(id, sectionNode);
    }

    public void addRelation(String subject, String object, String type) {
        addRelation(subject, "has_attribute", object, type);
    }

    public void addRelation(String subject, String predicate, String object, String type) {
        relations.add(new Relation(subject, predicate, object, type));
    }

    public void toHTML(String filename) {
        // "Print" a HTML form
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
                // Priortize ID-SectionNode! Identification attributes are always attached to the top of the form.
                if (node instanceof SectionNode && ((SectionNode) node).datatype.equals(IDENT)){
                    node.toHTML(writer);
                }
            }

            for (Node node : nodes.values()) {
                // Call toHTML for every node, that is not for identification!
                if (node instanceof SectionNode && ((SectionNode) node).datatype.equals(IDENT)){
                    // skip: In the current state, identification attributes are not carrying values!
                }else{
                    node.toHTML(writer);
                }
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
            writer.write("var identification = ''; \n");
            // Handle identification
            writer.write("for (var pair of formData.entries()) {\n");
            writer.write("var inputField = document.getElementById(pair[0]);\n");
            writer.write("var dataTypeAttribute = inputField.getAttribute('data-type');\n");
            writer.write("if (dataTypeAttribute === 'identification') {\n");
            writer.write("identification += pair[1];\n");
            writer.write("if (!identification.endsWith('/')) {\n");
            writer.write("identification += '/';\n");
            writer.write("}\n");
            writer.write("}\n");
            writer.write("}\n");
            writer.write("if (identification.endsWith('/')) {\n");
            writer.write("identification = identification.substring(0, identification.length - 1) + '#';\n");
            writer.write("}\n");
            writer.write("content += '\"ns\" \"is\" \"' + identification + '\"\\n';\n");
            // Handle all the other elements
            writer.write("for (var pair of formData.entries()) {\n");
            writer.write("var inputField = document.getElementById(pair[0]);\n");
            writer.write("if (inputField && inputField.type !== 'hidden') {\n");
            writer.write("var values = pair[1].split(',');\n");     // Handle multiple values separated by comma (e.g. in InputFields)
            writer.write("for (var i = 0; i < values.length; i++) {\n");
            writer.write("dataTypeAttribute = document.getElementById(pair[0]).getAttribute('data-type'); \n");
            writer.write("if (dataTypeAttribute !== 'identification') {\n");
            writer.write("content += '\"' + pair[0] + '\" \"has_value\" \"' + values[i].trim() + '\"\\n';\n");
            writer.write("}\n");
            writer.write("}\n");
            writer.write("}\n");
            writer.write("}\n");

            for (Object relation : relations) {         // handling simple relations
                if (relation instanceof String) {
                    writer.write("content += `" + relation + "` + \"\\n\";\n");
                } else if (relation instanceof Relation) {
                    Relation rel = (Relation) relation;
                    // formatting relation
                    String formattedSubject = "\"" + rel.getSubject() + "\"";
                    String formattedPredicate = "\"" + rel.getPredicate() + "\"";
                    String formattedObject = "\"" + rel.getObject() + "\"";
                    String formattedType = "\"" + rel.getType() + "\"";
                    String formattedAttr = " \"of_type\" ";
                    String relationStr = formattedSubject + " " + formattedPredicate + " " + formattedObject;
                    String rdfType = formattedPredicate + formattedAttr + formattedType;
                    writer.write("content += `" + relationStr + "` + \"\\n\";\n");
                    writer.write("content += `" + rdfType + "` + \"\\n\";\n");
                }
            }

            // Additional handling for "of_type" relation for InputField and SelectOptions elements
            for (Node node : nodes.values()) {
                exploreNode(node,writer);
            }

            writer.write("var blob = new Blob([content], { type: \"text/plain\" });\n");
            writer.write("var anchor = document.createElement(\"a\");\n");
            //writer.write("anchor.download = \"form-data.txt\";\n");
            writer.write("anchor.download = \"" + FileManagement.formatString(title)+ ".txt\";\n");
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

    private void exploreNode(Node node, BufferedWriter writer) throws IOException {
        /*  This method is exploring a graph and classifying its nodes when the HTML-document is rendered.
            It is recursive for every neighbor node, to reach all nodes in the graph.
         */
        if (node instanceof InputFieldNode) {
            // System.out.println("InputField: " + node.id);
            InputFieldNode inputFieldNode = (InputFieldNode) node;
            String id = inputFieldNode.id;
            String type = inputFieldNode.datatype;
            if (type != "identification") {          // ignore identification
                writer.write("content += `\"" + id + "\" \"of_type\" \"" + type + "\"\\n`;\n");
            }

        } else if (node instanceof SelectOptionsNode) {
            SelectOptionsNode selectOptionsNode = (SelectOptionsNode) node;
            String id = selectOptionsNode.id;
            String type = selectOptionsNode.datatype;
            if (type != "identification"){          // ignore identification
                writer.write("content += `\"" + id + "\" \"of_type\" \"" + type + "\"\\n`;\n");
            }

        } else if (node instanceof SectionNode){
            SectionNode sectionNode = (SectionNode) node;
            String id = sectionNode.id;
            String type = sectionNode.datatype;
            if (type != "identification"){          // ignore identification
                writer.write("content += `\"" + id + "\" \"of_type\" \"" + type + "\"\\n`;\n");
            }
        }

        for (Node neighbor : node.neighbors) {
            exploreNode(neighbor, writer);
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

    public void removeNode(String id) {
        Node nodeToRemove = nodes.get(id);
        if (nodeToRemove != null) {
            // Recursively remove orphaned children
            removeOrphanedChildren(nodeToRemove);

            // Remove the node from the nodes map
            nodes.remove(id);

            // Remove the node from any parent's neighbors
            for (Node parent : nodeToRemove.getParents(nodeToRemove.getId())) {
                parent.removeNeighbor(nodeToRemove);
            }
        }
    }

    private void removeOrphanedChildren(Node node) {
        if (node.hasChildren()) {
            for (Node child : new ArrayList<>(node.getChildren(node.getId()))) {
                if (child.getParents(node.getId()).size() == 1) {
                    node.removeNeighbor(child);
                    nodes.remove(child.getId());
                    removeOrphanedChildren(child);
                }
            }
        }
    }

    public void listGraph() {
        System.out.println("Current Status of the Graph:");
        System.out.println("---------------------------");

        for (Node node : nodes.values()) {
            System.out.println("Node ID: " + node.getId());
            System.out.println("Node Class: " + node.getClass().toString());
            System.out.println("Node Data type: " + node.getDatatype());

            System.out.println("Parents:");
            for (Node parent : node.getParents(node.getId())) {
                System.out.println("- Parent ID: " + parent.getId());
            }

            System.out.println("Children:");
            for (Node child : node.getChildren(node.getId())) {
                System.out.println("- Child ID: " + child.getId());
            }

            System.out.println("Relations:");
            for (Relation relation : relations) {
                System.out.println("- Subject: " + relation.getSubject());
                System.out.println("- Predicate: " + relation.getPredicate());
                System.out.println("- Object: " + relation.getObject());
            }

            System.out.println("---------------------------");
        }
    }


    private abstract class Node {
        // Blueprint for InputFieldNode, SelectOptionsNode, SectionsNode; Not meant to be instantiated directly.
        // Also, for toHTML, every node needs its own logic.

        protected String id;
        protected List<Node> neighbors;
        protected String datatype;                  // ONTOLOGICAL attribute! Resembles RDF:type

        public Node(String id, String datatype) {
            this.id = id;
            this.datatype = datatype;               // Type resembles to RDF:type or RDFS:type
            neighbors = new ArrayList<>();
        }

        public String getId() {
            return id;
        }

        public String getDatatype(){
            return datatype;                // Ontologic rdf:type!
        }

        public boolean hasChildren() {
            return !getChildren(this.getId()).isEmpty();
        }

        public List<Node> getParents(String currentSectionId) {
            List<Node> parents = new ArrayList<>();
            for (Node neighbor : neighbors) {
                if (neighbor instanceof SectionNode && ((SectionNode) neighbor).getId().equals(currentSectionId)) {
                    parents.add(neighbor);
                }
            }
            return parents;
        }

        public List<Node> getChildren(String currentSectionId) {
            List<Node> children = new ArrayList<>();
            for (Node neighbor : neighbors) {
                if (neighbor instanceof InputFieldNode || neighbor instanceof SelectOptionsNode) {
                    if (neighbor.getId().startsWith(currentSectionId)) {
                        children.add(neighbor);
                    }
                }
            }
            return children;
        }

        public void addNeighbor(Node neighbor) {
            neighbors.add(neighbor);
        }

        public void removeNeighbor(Node neighbor) {
            neighbors.remove(neighbor);
        }

        public abstract void toHTML(BufferedWriter writer) throws IOException;
    }

    private class Relation {
        // Since RDF statements are triples, this class is implemented to describe relations between nodes.
        private String subject;
        private String predicate;
        private String object;
        private String datatype;

        public Relation(String subject, String predicate, String object, String datatype) {
            this.subject = subject;
            this.predicate = predicate;
            this.object = object;
            this.datatype = datatype;           // Ontologic rdf:type!
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
            return datatype;                    // Ontologic rdf:type!
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
            writer.write(" data-type=\"" + datatype + "\"><br><br>\n");         // Ontologic rdf:type!

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
            writer.write(" data-type=\"" + datatype + "\">\n");             // Ontologic rdf:type!

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
            writer.write("<fieldset data-type=\"" + datatype + "\">\n");                // Ontologic rdf:type!
            writer.write("<legend>" + label + "</legend>\n");

            for (Node neighbor : neighbors) {
                neighbor.toHTML(writer);
            }

            writer.write("</fieldset>\n");
        }
    }
}




