import java.util.Arrays;
import java.util.LinkedList;
import java.io.File;
import java.util.Scanner;

public class DijkstrasShortestPath {
    private static int INFINITY = 10000; //represents infinity. should be larger than every number in distance_matrix.txt
    
    public static void main(String[] args) {
        String[] codes = new String[0];
        int[][] distanceMatrix = new int[0][0];
        String startV = "";
        String endV = "";
        File file;
        Scanner sc;

        //assigning codes from airport_codes.txt
        try {
            file = new File("airport_codes.txt");
            sc = new Scanner(file);
            LinkedList<String> tempCodes = new LinkedList<String>();

            while (sc.hasNext()) {
                tempCodes.add(sc.next());
            }
            codes = tempCodes.toArray((new String[tempCodes.size()]));
        } catch (Exception e) {
            System.out.println(e);
            return;
        }

        //assigning distanceMatrix from distance_matrix.txt
        try {
            file = new File("distance_matrix.txt");
            sc = new Scanner(file);
            LinkedList<Integer> tempMatrix = new LinkedList<>();
            
            while (sc.hasNext()) {
                tempMatrix.add(sc.nextInt());
            }
            double matrixSize = Math.sqrt(tempMatrix.size()); //matrix must be a perfect square

            if (matrixSize % 1 == 0 || tempMatrix.size() == 2) {
                distanceMatrix = new int[(int)matrixSize][(int)matrixSize];
                for (int i = 0; i < matrixSize; i++) {
                    for (int j = 0; j < matrixSize; j++) {
                        distanceMatrix[i][j] = tempMatrix.getFirst();
                        tempMatrix.removeFirst();
                    }
                }
            }
            else {
                System.out.println("Invalid matrix");
                sc.close();
                return;
            }
        }
        catch(Exception e) {
            System.out.println(e);
            sc.close();
            return;
        }

        //making sure that the number of airport codes matches given matrix
        if (distanceMatrix.length != codes.length) {
            System.out.println("distance matrix size does not match number of airport codes.");
            sc.close();
            return;
        }

        //takes user input for start and end locations
        sc = new Scanner(System.in);
        System.out.print("Start: ");
        startV = sc.next();

        System.out.print("End: ");
        endV = sc.next();

        sc.close();

        //runs the shortest path algorithm
        ShortestPath(codes, distanceMatrix, startV, endV);
    }

    private static void ShortestPath(String[] codes, int[][] distance, String startV, String endV) {
        int[] distanceFS = new int[codes.length];
        String[] pred = new String[codes.length];
        LinkedList<Integer> queue = new LinkedList<>();
        int currentV = -1;

        //setting the starting values for every variable
        Arrays.fill(distanceFS, INFINITY); 
        Arrays.fill(pred, "");
        for (int i = 0; i < codes.length; i++) {
            queue.add(i); //stores the location of variables in the codes array rather than the variables themselves in order to more easily refrence them in for loops
            if (codes[i].equals(startV)){ 
                distanceFS[i] = 0; 
                currentV = i;
            }
        }
        if (currentV == -1) {
            System.out.println("Chosen starting location not found.");
            return;
        }

        //calculates shortest distance from start and predecessor and stores them in distanceFS and pred respectivly 
        while (!queue.isEmpty()) {
            for (int i = 0; i < codes.length; i++) {
                if (distanceFS[currentV] + distance[currentV][i] < distanceFS[i] && codes[i]!= codes[currentV] && distance[currentV][i] != 10000) {
                    distanceFS[i] = distanceFS[currentV] + distance[currentV][i];
                    pred[i] = codes[currentV];
                }
            }
            queue.remove(Integer.valueOf(currentV));
            
            if (!queue.isEmpty()) {
                currentV = queue.getFirst();
                
                //selects next closest value
                for (int i = 0; i < queue.size(); i++) {
                    if (distanceFS[queue.get(i)] < distanceFS[currentV]) {
                        currentV = queue.get(i);
                    }
                }
            }
        }
        queue.clear();

        //prints results
        for (int i = 0; i < codes.length; i++) {
            if (codes[i].equals(endV)) {
                System.out.println("The shortest distance between " + startV + " and " + endV + " is " + distanceFS[i] + " miles.");
                System.out.println("The shortest route between " + startV + " and " + endV + " is:");

                currentV = i;
                String result = codes[i];
                while (!pred[currentV].equals("")) {
                    result = pred[currentV] + " --> " + result;
                    for (int j = 0; j < codes.length; j++) {
                        if (pred[currentV].equals(codes[j])) {
                            currentV = j;
                            break;
                        }
                    }
                }
                System.out.println(result);
                return;
            } 
        }
        System.out.println("Chosen end location not found");
    }
}