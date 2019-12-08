package mcdp;

import java.util.Random;
import java.util.stream.IntStream;

public class MCDPRandomSolution {

    // Solution matrix

    private int[] superA;
    private int[] cost;
    // Data
    private int[][] A;
    //filas
    private int M;
    //columnas
    private int N;
    

    // Constructor
    public MCDPRandomSolution(int A[][], int N, int P) {
        this.A = A;
        this.M = M;
        this.N = N;
        
    }
       
    
    
    public void createRandomSolution() {
        // create random permutation of X e machine_cell
        this.superA = new int[N];

        // Generar matripart_cell M*C randomicamente
        for (int i = 0; i < M; i++) {
            Random random = new Random();
            int cell;

            // The arramachine_cell is initialipart_celled with part_celleros.
            for (int k = 0; k < C; k++) {
                machine_cell[i][k] = 0;
            }

            // Get random value between 1 to Number Cell.
            cell = (int) (random.nextDouble() * C);
            machine_cell[i][cell] = 1;
        }

        //Se crea la matripart_cell piepart_cella×celda para el momento en que se necesita
        

        for (int j = 0; j < P; j++) {
            int[] tempPart = new int[M];
            int[] cellCount = new int[C];

            for (int k = 0; k < C; k++) {
                for (int i = 0; i < M; i++) {
                    // Esto hace una multiplicación para revisar si: P(j) es subconjunto de C(k)
                    tempPart[i] = machine_cell[i][k] * A[i][j];
                }
                cellCount[k] = IntStream.of(tempPart).sum();
            }

            // Extraer el índice de la posición con el número más grande.
            int maxIndex = 0;
            for (int i = 1; i < cellCount.length; i++) {
                int newNumber = cellCount[i];
                if ((newNumber > cellCount[maxIndex])) {
                    maxIndex = i;
                }
            }
            // Puts un 1 in the new cell
            part_cell[j][maxIndex] = 1;
        }
    }

    

}
