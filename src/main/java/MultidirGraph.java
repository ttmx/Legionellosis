public class MultidirGraph {

    private static class Node {

        private Node next;
        private final int id;

        private Node(int id) {
            this.id = id;
        }

        private void setNext(Node node) {
            next = node;
        }

        private Node getNext() {
            return next;
        }

        private int getId() {
            return id;
        }
    }

    public static class NodeIterator {

        private Node n;

        public NodeIterator(Node node) {
            n = node;
        }

        public boolean hasNext() {
            return n != null;
        }

        public int getNext() {
            Node ret = n;
            n = n.getNext();
            return ret.getId();
        }
    }

    private final Node[] firstConnection;

    public MultidirGraph(int size) {
        firstConnection = new Node[size];
    }

    public void addConnection(int a, int b) {
        setPair(a, b);
        setPair(b, a);
    }

    private void setPair(int a, int b) {
        Node n = new Node(b);
        n.setNext(firstConnection[a]);
        firstConnection[a] = n;
    }

    public NodeIterator iteratorAt(int a) {
        return new NodeIterator(firstConnection[a]);
    }

    public int size() {
        return firstConnection.length;
    }
}
