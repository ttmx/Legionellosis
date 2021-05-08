public class MultidirGraph {
    private final Node[] firstConnection;

    public MultidirGraph(int size){
        firstConnection = new Node[size];
    }

    public void addConnection(int a,int b){
        setPair(a,b);
        setPair(b,a);
    }

    private void setPair(int a, int b){
        Node n = new Node(b);
        n.setNext(firstConnection[a]);
        firstConnection[a] = n;
    }

    public NodeIterator getIterator(int a){
        return new NodeIterator(firstConnection[a]);
    }
    public int size(){
        return firstConnection.length;
    }
}
