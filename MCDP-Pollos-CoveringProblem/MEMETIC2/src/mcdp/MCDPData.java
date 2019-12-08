package mcdp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
//import java.nio.file.Files;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.util.IOUtils;

public class MCDPData {

    //pagina web, leer formato de entrega de datos.
    
    int rows;
    int columns;
    float[] cost;
    int[][] matriz;
    
    
    //leer los datos, cortarlos, y marcar la posicion de los datos con 1, rellenar previamente la matriz de puros 0.
    

    public MCDPData(File archivo) throws FileNotFoundException, IOException {


        FileReader fr = new FileReader(archivo);
        BufferedReader br = new BufferedReader(fr);
        
        String linea;
        int i,j,cantlinea;
        int d = 0;
        String[] data;
        
        linea=br.readLine();
        
        System.out.println(linea);
        data=linea.split(" ");
        
        
        rows=Integer.parseInt(data[0]);
        columns=Integer.parseInt(data[1]);
        
        System.out.println("Filas = "+rows+", Columnas= "+columns);
        //Creo el arreglo del coste de las columnas y después la matriz de filas/columnas.
        cost=new float[columns];
        matriz=new int[rows][columns];
        
        cantlinea=0;
        
        System.out.println("Costos de la columnas: ");
        
        
        
        while(cantlinea<columns){
        
            linea=br.readLine();
            data=linea.trim().split(" ");
            
            for(i = 0; i<data.length;i++){
                
                cost[i+cantlinea]=Float.parseFloat(data[i]);
                //Para revisar hasta donde llegan los datos, usar println.
                //Por comodidad, se dejaron los datos en un solo arreglo hacia la derecha y listo.
                System.out.print(cost[i+cantlinea]+" ");
                d++;
                if ( d % 12 == 0 ){
                    System.out.println();
                }
                //System.out.print(cost[i+cantlinea]+" ");
            }
            cantlinea+=data.length;
        }
        
        cantlinea=0;
       
        
        //Rellenaremos la matriz de puros 0.
        System.out.println();
        for( i = 0; i < rows; i++){
            for ( j = 0; j < columns; j++){
                matriz[i][j] = 0;
            }
        }
        
        int maxPerColum = 0;
        
        
        for( i = 0; i < columns; i++ ){
            linea=br.readLine();
            data=linea.trim().split(" ");
            maxPerColum = Integer.parseInt(data[0]);
            d = 0;
            
            while( cantlinea < maxPerColum ){
                linea=br.readLine();
                data=linea.trim().split(" ");

                for( j = 0; j < data.length; j++){
                    cantlinea=data.length;
                    matriz[i][Integer.parseInt(data[j]) - 1] = 1;
                    System.out.print(" pos:"+data[j]+"- "+matriz[i][Integer.parseInt(data[j]) - 1]);
                    d++;
                    if ( d % 12 == 0){
                        
                        System.out.println();
                    }
                }
            //System.out.println("Max = "+maxPerColum);

            }
        }  
    }
 }
