class Node {
    int posA;
    int posB;
    int posC;

    boolean visited;

    Node(int a, int b, int c, boolean visited)
    {
        this.posA = a;
        this.posB = b;
        this.posC = c;

        this.visited = visited;
    }
}