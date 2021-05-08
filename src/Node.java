public class Node {
    private Node next;
    private int id;
    public Node(int id){
        this.id = id;
    }
    public void setNext(Node node){
        next = node;
    }
    public Node getNext(){
        return next;
    }
    public int getId(){
        return id;
    }
}
