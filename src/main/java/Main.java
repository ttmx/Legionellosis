import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        TurboScanner in = new TurboScanner(System.in);
        int L = in.nextInt();
        List<Short>[] graph = newGraph(L);
        int C = in.nextInt();
        for (int i = 0; i < C; i++) {
            int l1 = in.nextInt() - 1;
            int l2 = in.nextInt() - 1;
            graph[l1].add((short) l2);
            graph[l2].add((short) l1);
        }
        int S = in.nextInt();
        short[] interviews = new short[2 * S];
        for (int i = 0; i < S; i++) {
            interviews[i * 2] = (short) (in.nextInt() - 1);
            interviews[i * 2 + 1] = (short) in.nextInt();
        }
        System.out.println(new Legionellosis(graph, interviews).perilousLocations());
    }

    private static List<Short>[] newGraph(int vertices) {
        @SuppressWarnings("unchecked") List<Short>[] graph = (List<Short>[]) new List[vertices];
        for (int i = 0; i < graph.length; i++) {
            graph[i] = new LinkedList<>();
        }
        return graph;
    }

}
