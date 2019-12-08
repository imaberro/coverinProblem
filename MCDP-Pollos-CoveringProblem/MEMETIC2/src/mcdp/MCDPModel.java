package mcdp;

import java.util.Arrays;

public class MCDPModel {
    
    //mismos datos que el data
    private int[][] A;
    private int M;
    private int N;
    private int C;
    private int Mmax;

    // Solution individuo
    private int[] superA;
    private int[] cost;

    //CREAR LAS RESTRICCION DEL PAPER D:
    
    public MCDPModel(int A[][], int M, int N, int C, int Mmax, int[] superA) {
        this.A = A;
        //rows
        this.M = M;
        //Columns
        this.N = N;
        //idfk
        this.C = C;
        
        this.Mmax = Mmax;
        this.superA = superA;
    }

    /**
     @return true si esta correta la restriccion, en caso contrario retorna falso.
     */
    public boolean Constraint_1() {
        float sum = 0;
        for (int i = 0; i < this.M; i++) {
            sum = 0;
            for ( int j = 0; i < this.N; j++){
                sum = this.A[i][j] * cost[j];
            }
            if ( sum < 1 ){
            return false;
            }
        }
        return true;
    }

    public boolean checkConstraint() {
        boolean c1 = Constraint_1();
        

        return c1;
              
    }

    // Objective function 
    //TODO: cambiar
    public float calculateFitness() {
        float mul = 0;
       
        for ( int i = 0; i < N; i++ ){
            mul += cost[i] * superA[i];
        }
        
        return mul;
    }

}
