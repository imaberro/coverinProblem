package mcdp;

import java.util.Random;
import java.util.stream.IntStream;

public class MCDPRandomSolution {

    // Solution matrix

    private int[] superA;
    private float[] cost;
    // Data
    private int[][] A;
    //filas
    private int M;
    //columnas
    private int N;
    

    // Constructor
    public MCDPRandomSolution(int A[][], int N, int M, float Cost[]) {
        this.A = A;
        //rows
        this.M = M;
        //Columns
        this.N = N;
        this.cost = Cost;
        
        
    }
       
    
    
    public void createRandomSolution() {
        // create random permutation of X e machine_cell
        this.superA = new int[M];
        
        // Generar matripart_cell M*C randomicamente
        for (int i = 0; i < M; i++) {
            this.superA[i] = 0;
        }
        
        Random random = new Random();
        int cell;
        boolean created = false;
        
        do{
            cell = (int) (random.nextDouble() * M );
            this.superA[cell] = 1;
            MCDPModel model = new MCDPModel(this.A, this.M, this.N, this.cost, this.superA);
            created = model.checkConstraint();
        }while(created == false);
    }

    public int[] getSuperA(){
        return superA;
    }
}
