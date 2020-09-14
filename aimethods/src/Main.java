import java.util.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    //ArrayList used to hold Node objects
    //All nodes are stored in here
    private static ArrayList<Node> toOutput = new ArrayList<>();

    //ArrayList used to hold neighbor nodes to a Node object
    //Neighbor nodes stored in the form of integer arrays
    private static ArrayList<int[]> neighbors = new ArrayList<>();

    //Stack of Node objects to loop through
    private static Stack<Node> stack = new Stack<>();

    public static void main(String args[]) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        //Define variables for maximum jug capacities
        int maxA;
        int maxB;
        int maxC;

        //Get the user input value for A
        System.out.print("Enter value of A:");
        maxA = Integer.parseInt(br.readLine());

        //Get the user input value for B
        System.out.print("Enter value of B:");
        maxB = Integer.parseInt(br.readLine());

        //Get the user input value for C
        System.out.print("Enter value of C:");
        maxC = Integer.parseInt(br.readLine());

        System.out.println("Started.");

        //Record the start time
        final long startTime = System.currentTimeMillis();

        //Call the algorithm function using the user specified maximum values
        depthFirstSearch(maxA, maxB, maxC);

        //Record the finish time
        final long finishTime = System.currentTimeMillis();

        //Output the total time taken in ms
        System.out.println("Total execution time: " + (finishTime - startTime) + "ms");
    }

    private static void depthFirstSearch(int maxA, int maxB, int maxC)
    {
        //Define our starting node object of (0,0,0)
        Node n = new Node(0, 0, 0, false);

        int counter = 0;

        //Add it to the top of the stack
        stack.push(n);

        //Mark it as visited
        n.visited = true;

        //Add it to the list of outputs
        toOutput.add(n);

        //Loop so long as there is something in the stack
        while (!stack.empty())
        {
            counter ++;

            //Clear the ArrayList of neighbor objects to a node
            neighbors.clear();

            //Get the Node object that is at the top of the stack
            Node curNode = stack.peek();

            System.out.println("Node #" + counter + " - Current node: (" + curNode.posA + "," + curNode.posB + "," + curNode.posC + ")");

            //Pop it from the top of the stack
            stack.pop();

            //Calculate possible neighbor nodes and call the "addToNeighbors" function
            addToNeighbors(new int[]{0, curNode.posB, curNode.posC}); //Value from emptying position A
            addToNeighbors(new int[]{curNode.posA, 0, curNode.posC}); //Value from emptying position B
            addToNeighbors(new int[]{curNode.posA, curNode.posB, 0}); //Value from emptying position C

            addToNeighbors(new int[]{maxA, curNode.posB, curNode.posC}); //Value from filling position A
            addToNeighbors(new int[]{curNode.posA, maxB, curNode.posC}); //Value from filling position B
            addToNeighbors(new int[]{curNode.posA, curNode.posB, maxC}); //Value from filling position C

            int toAdd = maxB - curNode.posB; //Value from transferring water from jug A to jug B
            if (toAdd != 0)
                addToNeighbors(toAdd <= curNode.posA ? new int[]{curNode.posA - toAdd, curNode.posB + toAdd, curNode.posC} : new int[]{0, curNode.posB + curNode.posA, curNode.posC});

            toAdd = maxC - curNode.posC; //Value from transferring water from jug A to jug C
            if (toAdd != 0)
                addToNeighbors(toAdd <= curNode.posA ? new int[]{curNode.posA - toAdd, curNode.posB, curNode.posC + toAdd} : new int[]{0, curNode.posB, curNode.posC + curNode.posA});

            toAdd = maxA - curNode.posA; //Value from transferring water from jug B to jug A
            if (toAdd != 0)
                addToNeighbors(toAdd <= curNode.posB ? new int[]{curNode.posA + toAdd, curNode.posB - toAdd, curNode.posC} : new int[]{curNode.posA + curNode.posB, 0, curNode.posC});

            toAdd = maxC - curNode.posC; //Value from transferring water from jug B to jug C
            if (toAdd != 0)
                addToNeighbors(toAdd <= curNode.posB ? new int[]{curNode.posA, curNode.posB - toAdd, curNode.posC + toAdd} : new int[]{curNode.posA, 0, curNode.posC + curNode.posB});

            toAdd = maxA - curNode.posA; //Value from transferring water from jug C to jug A
            if (toAdd != 0)
                addToNeighbors(toAdd <= curNode.posC ? new int[]{curNode.posA + toAdd, curNode.posB, curNode.posC - toAdd} : new int[]{curNode.posA + curNode.posC, curNode.posB, 0});

            toAdd = maxB - curNode.posB; //Value from transferring water from jug C to jug B
            if (toAdd != 0)
                addToNeighbors(toAdd <= curNode.posC ? new int[]{curNode.posA, curNode.posB + toAdd, curNode.posC - toAdd} : new int[]{curNode.posA, curNode.posB + curNode.posC, 0});

            //By this point in the program's execution, the ArrayList "neighbors" should
            //have a collection of all non-duplicate neighbors
            //e.g. an array of (0,1,0) will not appear twice
            //This is to help with general speed of execution as it is not running the same thing repeatedly

            //Loop through all neighbor positions for the Node
            for (int[] neighbor : neighbors) {

                //If a node with the given node position already exists in the output array, ignore this neighbor
                if (!existsInOutputs(neighbor))
                {
                    //If it doesn't exist, create a new Node object with the supplied integer array values
                    Node c = new Node(neighbor[0], neighbor[1], neighbor[2], true);

                    //Push it to the top of the stack
                    stack.push(c);

                    //Add it to the outputs list
                    toOutput.add(c);
                }
            }
        }

        //Signal the function has finished
        System.out.println("Finished");

        //Output the total number of distinct states found
        System.out.println("Outputs - " + toOutput.size());
    }

    //Takes an integer array as a parameter and returns a boolean
    //Used to check whether or not a node already exists in the outputs list with the given parameter values
    private static boolean existsInOutputs(int[] toCheck)
    {
        //Loop all Nodes in the outputs array
        for (Node n : toOutput)
        {
            //If there is a match then return true
            if (((toCheck[0] == n.posA) && (toCheck[1] == n.posB) && (toCheck[2] == n.posC))) {
                return true;
            }
        }
        //If we've reached this point then a match wasn't located - return false
        return false;
    }

    //Takes an integer array as a parameter and returns nothing
    //Used to make sure no duplicate integer arrays are added to the list of neighbor nodes
    private static void addToNeighbors(int[] toAdd)
    {
        //If the ArrayList is empty then assume it's fine to add in the neighbor
        if (neighbors.isEmpty())
        {
            neighbors.add(toAdd);
            return; //No need to run past this if it's the first neighbor being added
        }

        //Loop the ArrayList of neighbor nodes
        for (int[] neighbor: neighbors)
        {
            //Compare the parameter's values to the current loop array's values
            if (((neighbor[0] != toAdd[0]) || (neighbor[1] != toAdd[1]) || (neighbor[2] != toAdd[2]))) {
                //If there's a match then we don't need to add it because the neighbor already exists
                continue;
            }
            return;
        }

        //Add to the list of neighbors
        neighbors.add(toAdd);
    }
}