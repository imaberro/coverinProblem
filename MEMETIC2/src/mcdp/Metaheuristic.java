package mcdp;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.jfree.data.xy.XYSeries;

public class Metaheuristic {

    // Parametros de la metaheuristica
    private int numberIndividual;
    private int numberIteration;
    private int numberField;

    private float consultationFactor;
    private int numberOfVariables;
    private int numberOfVariablesToChange;
    private static int nDiscretization = 0;
    private int iteracionMejorFitness = 0;

    private float k1;
    private float k2;

    private Individual bestSolution;		// Mejor Solución

    private ArrayList<Individual> poblation;    //Poblacion General
    

    // Dataset (benchmark)
    private MCDPData data;

    // Estadisticas
    private long numAcceptedMoves;
    private long numRejectedMoves;

    private float rpd;

    public XYSeries seriesIterationFitness = new XYSeries("Iteracion/Fitness");
    
    
    //adaptar la metaheuristica, pasar los metodos de tranforma, discretiza (LISTO) (los cases), y los validar y reparar
    //y el metodo del colum inclution y colum dominator.

//<editor-fold defaultstate="collapsed" desc="Constructor">
    public Metaheuristic(int numberIndividual, int numberIteration, MCDPData data) {

        System.out.println("numberIndividual: "+ numberIndividual+ " num iteraciones: " +numberIteration + " factor de consulta: "+ consultationFactor);
        this.numberIndividual = numberIndividual;
        this.numberIteration = numberIteration;
        this.numberField = numberField;
        this.consultationFactor = consultationFactor;
        this.k1 = k1;
        this.k2 = k2;

        this.data = data;

        this.poblation = new ArrayList<Individual>(this.numberIndividual);
        

        this.bestSolution = new Individual();

        this.numAcceptedMoves = 0;
        this.numRejectedMoves = 0;

    }
//</editor-fold>11

//<editor-fold defaultstate="collapsed" desc="run">
    public void run() throws IOException {
        
        generateInitialPoblation();
        //ordenar despues la generacion de pollos
        ordenaBacterias();
            
        System.out.println("mejor fitness INICIAL: "+poblation.get(0).getFitness());
        

        
        int iteration = 0;
        float mejorFitness = 99999;
        clasificacion();

        while (iteration < this.numberIteration) {
            //System.out.println( " iteracion: "+iteration+"--------------------------------------------------------");    
            
            
            System.out.println("Iteration="+iteration);
            for ( int i = 0 ; i < poblation.size() ; i++ ){
                int [] M;
                if ( poblation.get(i).getChicken() == 1 ){
                    M = movimientoGallo( poblation.get(i).getMachine_cell());
                }
                
                else {
                    if ( poblation.get(i).getChicken() == 2 ){
                        M = movimientoGallina ( poblation.get(i).getMachine_cell(), i);
                    }
                    else{
                        M = movimientoPollo(poblation.get(i).getMachine_cell(),i);
                    }
                }
                
                //se revisa que la nueva matriz cumpla con MCDP y se reemplaza con el riginal si el fitness es mejor
                Individual Pk = new Individual(poblation.get(i).getMachine_cell(), poblation.get(i).getFitness());
                Pk.setMachine_cell(M);
                
                MCDPModel boctorModel= new MCDPModel(data.matriz, data.rows, data.columns, data.cost, Pk.getMachine_cell());
                Boolean constraintOK = boctorModel.checkConstraint();
                if(constraintOK == true){
                    Pk.setFitness(boctorModel.calculateFitness());
                }
                else{
                    Pk.setMachine_cell(poblation.get(i).getMachine_cell());
                    
                }
                if(Pk.getFitness()<poblation.get(i).getFitness()){
                    poblation.get(i).setMachine_cell(Pk.getMachine_cell());
                   
                    poblation.get(i).setFitness(Pk.getFitness());
                }
                
                
            }
            
            ordenaBacterias();
            if (poblation.get(0).getFitness() < mejorFitness) {
                mejorFitness = poblation.get(0).getFitness();
                this.iteracionMejorFitness = iteration;
                System.out.println("mejor fitness: "+mejorFitness+ " iteracion: "+iteration);

            }
            System.out.println("Fitness ciclo "+iteration+" = "+poblation.get(0).getFitness());
            
            for(int i =0; i< poblation.size();i++){
                System.out.print(" "+poblation.get(i).getFitness());
            }
            System.out.print("\n");
            //System.out.println("mejor fitness: "+mejorFitness+ " iteracion: "+iteration);
            //seriesIterationFitness.add(iteration, bestSolution.getFitness());

            iteration++;
        }
   
        //chooseBestSolutionInPoblation();
        //System.out.println("<<<<<<<<<<<<<<<<<<<<<<<END-RUN");

        //Plot.createPlots(this.modifyNumberPoblation, this.modifyNumberIteration, this.modifyConsultationFactor, resultsPath, fileName, this.seriesPoblationFitness, this.seriesIterationFitness, this.seriesConsultationFactorFitness, this.bestFitnessAuthor, this.numberIterationBestFitness, this.consultationFactorBestFitness, this.numberIterationAS);
    
    }
    
    private ArrayList<Individual> Gallos;
    private ArrayList<Individual> Pollitos;
    private ArrayList<Individual> Gallinas;
    
    //Clasifica la cantidad pollos y gallinas que hay en la poblacion.
    public void clasificacion(){
        Gallos = new ArrayList();
        Pollitos = new ArrayList();
        Gallinas = new ArrayList();
        
        poblation.get(0).setChicken(1);    
        int i = 0;
        //Numero de Gallos (Rooster Number)
        int RN = 1;
        //Numero de Pollitos (Chicks Number)
        int CN = 1;
        //Numero de Gallinas (Hens Number)
        int HN = 1;
        //Número de Gallinas Mamás (MN)
        int MN = 1;
        
        
        
        while ( poblation.get(0).getFitness() == poblation.get(i).getFitness()  ){
            poblation.get(i).setChicken(1);
            Gallos.add(poblation.get(i)); 
            RN++;
            i++;
            
        }
        
        
        poblation.get(poblation.size() - 1).setChicken(3);
        i = poblation.size() - 1;
        
        while( poblation.get(i).getFitness() == poblation.get( poblation.size() - 1).getFitness() ){
            poblation.get(i).setChicken(3);
            Pollitos.add(poblation.get(i));
            CN++;
            i--;
        }
        
        for ( i = 0 ; i < poblation.size() ; i++){
            if ( poblation.get(i).getFitness() != poblation.get(0).getFitness() &&
                 poblation.get(i).getFitness() != poblation.get( poblation.size() - 1).getFitness() ){
                    HN++;
                    poblation.get(i).setChicken(2);
                    Gallinas.add(poblation.get(i));
                }
            
        }
        
        Individual gallina;
        for ( i = 0 ; i < Pollitos.size() && i < Gallinas.size() ; i++ ){
            Individual pollito = Pollitos.get(i);
            Random r = new Random();
            //Toma una gallina desde el índice hasta n-1, donde n corresponde al tamaño del arreglo de la
            //gallina.
            do{
                gallina = Gallinas.get(r.nextInt(Gallinas.size()));
            }while(gallina.getParentesco() != null);
            
            gallina.setParentesco(pollito);
            pollito.setParentesco(gallina);
            MN++;
            
        }
        
        
    }
    
//</editor-fold>
    public void ordenaBacterias(){
        boolean orden = false;
        int i = 0;
        Individual aux = null;
        int mayor = 0;
        int indice =0;
        while(i<poblation.size() && orden == false){
            i+=1;
            orden = true;
            for (int j = 0; j < poblation.size()-1; j++) {
                if(poblation.get(j+1).getFitness()<poblation.get(j).getFitness()){
                    orden=false;
                    aux = new Individual(poblation.get(j).getMachine_cell(), 
                          poblation.get(j).getFitness());
                          poblation.get(j).setMachine_cell(poblation.get(j+1).getMachine_cell());
                          poblation.get(j).setFitness(poblation.get(j+1).getFitness()); 
                          poblation.get(j+1).setMachine_cell(aux.getMachine_cell());
                          poblation.get(j+1).setFitness(aux.getFitness()); 
                }
            }
        }
        /*
        System.out.println("ANTES fitness "+poblation.get(0).getFitness());
        aux = new Individual(poblation.get(0).getMachine_cell(), poblation.get(0).getPart_cell(), poblation.get(0).getFitness());
        int[] SumCell = new int[data.C];     
        for (int y = 0; y < data.P; y++){
            mayor=0;indice=0;
            for (int c = 0; c < data.C; c++){
                SumCell[c]=0;
            }
            for (int x = 0; x < data.M; x++){
                if (this.data.A[x][y] == 1){
                    for(int z = 0; z < data.C; z++){
                      if (poblation.get(0).getMachine_cell()[x][z]==1){
                          SumCell[z]+=1;
                      }
                    }         
                }
            }

            for (int c = 0; c < data.C; c++){
                poblation.get(0).getPart_cell()[y][c]=0;
                if (mayor <= SumCell[c]){
                    mayor = SumCell[c];
                    indice=c; 
                }    
            }

            poblation.get(0).getPart_cell()[y][indice]=1;
            
        }
       
        MCDPModel  boctorModel = new MCDPModel(data.A, data.M, data.P, data.C, data.mmax,
                        poblation.get(0).getMachine_cell(),
                        poblation.get(0).getPart_cell());
        boolean constraintOK = boctorModel.checkConstraint();
        if (constraintOK == true) {
                poblation.get(0).setFitness(boctorModel.calculateFitness());
        }
        
        if(aux.getFitness()< poblation.get(0).getFitness()){
            poblation.get(0).setMachine_cell(aux.getMachine_cell());
            poblation.get(0).setPart_cell(aux.getPart_cell());
            poblation.get(0).setFitness(aux.getFitness()); 
        }
        bestSolution.setMachine_cell(poblation.get(0).getMachine_cell());
        bestSolution.setPart_cell(poblation.get(0).getPart_cell());
        bestSolution.setFitness(poblation.get(0).getFitness());
        System.out.println("DESPUES firness "+poblation.get(0).getFitness());*/
        //System.out.println("pocision despues firness "+poblation.get(0).getFitness());
        /*for (int j = 0; j < poblation.size(); j++) {
            System.out.println("pocision "+j+" firness "+poblation.get(j).getFitness());
        }*/
    }
    
    public int[] generaRandom(){
        int[] matriz = new int[data.columns];
        for (int x = 0; x < data.columns; x++){
                matriz[x] = (int)(Math.random()*2);
        }   
        return matriz;
    }
    
    
    public int bin (double x){
        double t = 0;
        double u = Math.random();
        int salida = 0;

            t = Math.tanh(x);
            if(t < 0 )
                t=t*-1;
            //break;
        
        if ( u < t){
            salida = 1;;
        }else{
            salida = 0;
        }
    
    return salida;
    }
    
    public int [] movimientoGallo (int []matriz ){
        for (int i = 0; i < data.columns; i++){
            
            matriz[i] =discretiza(transforma( matriz[i] * bin((1+(int)Math.random()*2))));
        }
        return matriz;
    }
   
    int S1 (int posicion){
        
        return (int)(Math.exp( ( poblation.get(posicion).getFitness() - poblation.get(0).getFitness() ) /
                                ( Math.abs( poblation.get(posicion).getFitness())+ Double.MIN_VALUE) ) );
    }
    
    int S2 (int posicion, int posRandom){
     
        return  (int)( Math.exp(( poblation.get(posRandom).getFitness() - poblation.get(posicion).getFitness() )) );
        
    }
    
    //la matriz debe tener valores entre 0 y 1, entonces se debe transformar y discretizar los valores flotantes y los fuera de rango
    public double transforma(double valor){
        double x= Math.tanh(valor);
        if(x<0){
            x*=-1;
        }
        return x;
    }
    public int discretiza(double x){
        
        int salida=0;
	double u = 0;
        double t=Math.random();
        
        switch(nDiscretization){
            
            case 1: 
                if(t<x){
                    salida = 1;
                }
                else{
                    salida = 0;
                }
                break;
            
            case 2:
                u = Math.random();
                if (u < x){
                    salida = 1;
	        }else{
                    salida = 0;
                    }
                break; 
            
            case 3: 
	        u = Math.random();
                    if (u < x){
			salida = 0;
		    }else{
                        salida = 1;
		    }
	        break;
                
            case 4: 
	        u = Math.random();
                if(x < u){
                    salida =1;
                }else{
                    salida = 0;
		}
	        break;
                
	        case 5: 
	    	int desde = 0;
		int hasta = 1;
				
		Random nRand = null;
		nRand = new Random();		       
		u = nRand.nextInt(hasta - desde + 1) + desde;
				//u = Math.random();
		if(x <= u){
                    salida = 0;
		}else{
		    salida = 1;	
		}
	        break;
        }
        
        return salida;
    }
    //pasar valores del pollo y del gallo para realizar la operacion de la gallina
    public int [] movimientoGallina (int [] matriz, int posicion){
        int[] Matriz = generaRandom();
        int i, j, posRandom;
        Random r = new Random();
        
        do{
            posRandom = r.nextInt(posicion) + 1;
        }while(poblation.get(posicion).getFitness() < poblation.get(posRandom).getFitness());
        
        for ( i = 0 ; i < data.columns ; i++){
                Matriz[i] = discretiza(transforma (matriz[i]+ S1(posicion) + Math.random() 
                        * ( poblation.get(0).getMachine_cell()[i] - matriz[i] )
                        + S2( posicion , posRandom ) * Math.random()
                        * ( poblation.get( posRandom ).getMachine_cell()[i] - matriz[i] )));
            
        }
        
        
       return Matriz; 
        
    }
    
    public int [] movimientoPollo (int []matriz , int posicion){
        int i, j, posRandom;
        int [] Matriz = generaRandom();
        Random r = new Random();
        posRandom = r.nextInt(3);
        
        
        for ( i = 0; i < data.columns ; i++ ){
            
                Matriz [i] = discretiza(transforma(Matriz[i] + posRandom
                                * ( poblation.get(posicion).getParentesco().getMachine_cell()[i] -
                                    matriz[i])));
            
        }
        
        return Matriz;
    }
    
    
    public void limpiaParentesco(){
        for(int i =0;i<poblation.size();i++){
            poblation.get(i).setParentesco(null);
        }
    }

    




//<editor-fold defaultstate="collapsed" desc="generateInitialPoblation">
    private void generateInitialPoblation() {
        for (int i = 0; i < numberIndividual; i++) {
            // Inicialite procedure
            boolean constraintOK = false;
            MCDPRandomSolution randomSolution = new MCDPRandomSolution(data.matriz, data.rows, data.columns, data.cost);
            float randomSolutionFitness = 0;

            // Estoy en el ciclo hasta generar una solución randomica que satisfaga las restricciones
            while (constraintOK == false) {
                
                // Create random solution
                
                randomSolution.createRandomSolution();
                
                // Check constraint
                MCDPModel boctorModel = new MCDPModel(data.matriz, data.rows, data.columns, data.cost,
                                                        randomSolution.getSuperA());
                        
                constraintOK = true;
                        //boctorModel.checkConstraint();
                //System.out.println(constraintOK);

                if (constraintOK == true) {
                    randomSolutionFitness = boctorModel.calculateFitness();
                    this.numAcceptedMoves++;
                    break;
                } else {
                    this.numRejectedMoves++;
                }
            }
            System.out.println("Se creooOoO");
            // Create Solution
            Individual individual = new Individual(randomSolution.getSuperA(), randomSolutionFitness);

            // Add Solution in poblation
            poblation.add(individual);
        }
    }
//</editor-fold>


    public Individual getBestSolution() {
        return bestSolution;
    }

    public void setBestSolution(Individual bestSolution) {
        this.bestSolution = bestSolution;
    }

    public float getRpd() {
        return rpd;
    }

    public void setRpd(float rpd) {
        this.rpd = rpd;
    }


    public int getIteracionMejorFitness() {
        return iteracionMejorFitness;
    }

    public void setIteracionMejorFitness(int iteracionMejorFitness) {
        this.iteracionMejorFitness = iteracionMejorFitness;
    }

}
