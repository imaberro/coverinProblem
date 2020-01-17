package mcdp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
//import java.nio.file.Files;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MKPData {

    int knapsac;
    int objects;
    int optim;
    int[] capacities;
    int[] weighs;
    int[][] constraints;

    public MKPData(String fileName, String subFolderProblem) throws FileNotFoundException, IOException {
        
        System.out.println("/problems/" + subFolderProblem + "/" + fileName);

        File archivo = new File ("/problems/" + subFolderProblem + "/" + fileName);
        FileReader fr = new FileReader (archivo);
        BufferedReader br = new BufferedReader(fr);
        
        String linea;
        int i,j,cantlinea;
        String[] data;
        
        linea=br.readLine();
        data=linea.split(" ");
        
        
        knapsac=Integer.parseInt(data[0]);
        objects=Integer.parseInt(data[1]);
        
        System.out.println("Mochilas = "+knapsac+", Objetos= "+objects);
        
        capacities=new int[knapsac];
        weighs=new int[objects];
        constraints=new int[knapsac][objects];
        
        cantlinea=0;
        
        System.out.println("Pesos");
        
        while(cantlinea<objects){
        
            linea=br.readLine();
            data=linea.split(" ");
            for(i = 0; i<data.length;i++){
                weighs[i]=Integer.parseInt(data[i]);
                System.out.print(weighs[i]+" ");
            }
            cantlinea+=data.length;
        }
        
        cantlinea=0;
        
        System.out.println("\nDimensiones");
        
        while(cantlinea<knapsac){
        
            linea=br.readLine();
            data=linea.split(" ");

            for(i=0; i<data.length;i++){
                capacities[i]=Integer.parseInt(data[i]);
                System.out.print(capacities[i]+" ");
            }
            cantlinea+=data.length;
        }
        
        System.out.println("\nConstantes");
        
        for(i=0;i<knapsac;i++){
            
            System.out.println("C1");
            cantlinea=0;
            while(cantlinea<objects){
                linea=br.readLine();
                data=linea.split(" ");
                for(j =0; j<data.length; j++){
                    constraints[i][j]=Integer.parseInt(data[i]);
                    System.out.print(constraints[i][j]+" ");
                }
                cantlinea+=data.length;
            }
        }
        linea=br.readLine();
        data=linea.split(" ");
        optim=Integer.parseInt(data[0]);
        
        
    }

    

}
