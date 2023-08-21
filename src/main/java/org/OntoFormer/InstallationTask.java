package org.OntoFormer;

import static org.OntoFormer.FileManagement.*;

public class InstallationTask {
    private static String FuncName;
    private static Boolean bPrint = false;

    public static void main(String[] args) {
        String str = getRootVariable();
        Boolean bool = ExistsPath(str);
        System.out.println(bool);
    }

    public static void InstallSrcFolder(){
        FuncName = "InstallSrcFolder";
        String root = getRootVariable();        root = root + "/OntoFormer";

        if(ExistsPath(root)== false){
            FileManagement.CreateDirectory(root);

            if (bPrint == true) {
                System.out.println("[" + FuncName + "] Created directory: " + root);
            }

        }else{
            if (bPrint == true) {
                System.out.println("[" + FuncName + "] Directory already exists: " + root);
            }
        }
    }

    public static void InstallSrcGraph(){
        FuncName = "InstallSrcGraph";
        String root = getRootVariable();
        root = root + "/OntoFormer/DefaultGraph.rdf";

        if(ExistsPath(root)== false){
            FileManagement.CreateFile(root);

            if (bPrint == true) {
                System.out.println("[" + FuncName + "] Created source graph at: " + root);
            }

        }else{
            if (bPrint == true) {
                System.out.println("[" + FuncName + "] Source graph already exists at: " + root);
            }
        }

    }
    public static String getRootVariable(){
        FuncName = "getRootVariable";
        String user = System.getProperty("user.name");
        String root = "/home/" + user + "/Dokumente";

        if (bPrint == true) {
            System.out.println("[" + FuncName + "] RootVariable: " + root);
        }

        return root;
    }



}
