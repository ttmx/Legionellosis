public class NodeIterator {
    Node n;
    public NodeIterator(Node node){
        n = node;
    }
    public boolean hasNext(){
        return n != null;
    }
    public Node getNext(){
        Node ret = n;
        n = n.getNext();
        return ret;
    }
}
