import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class Legionellosis {

    private final MultidirGraph graph;
    //[home,maxDistance]
    private final int[] sick;
    private final int numSick;

    public Legionellosis(MultidirGraph graph, int[] sick) {
        this.graph = graph;
        this.sick = sick;
        numSick = sick.length / 2;
    }

    public String res() {
        int[] hits = new int[graph.size()];
        boolean[] processed = new boolean[graph.size()];
        for (int i = 0; i < numSick; i++) {
            Arrays.fill(processed, false);
            explore(hits, processed, sick[i * 2], sick[i * 2 + 1]);
        }
        return formatOutput(hits);
    }

    private void explore(int[] hits, boolean[] processed, int current, int currentLevel) {
        Queue<Integer> q = new LinkedList<>();
        q.add(current);
        int toNextLevel = 1;
        int toNextLevelDec = 0;
        do {
            current = q.remove();
            if (!processed[current]) {
                hits[current]++;
            }
            processed[current] = true;
            if (--toNextLevel == 0) {
                if (--currentLevel < 0)
                    break;
            }
            MultidirGraph.NodeIterator it = graph.iteratorAt(current);
            while (it.hasNext()) {
                int node = it.getNext();
                if (!processed[node]) {
                    toNextLevelDec++;
                    q.add(node);
                }
            }
            if (toNextLevel == 0) {
                toNextLevel = toNextLevelDec;
                toNextLevelDec = 0;
            }
        } while (!q.isEmpty());
    }

    private String formatOutput(int[] hits) {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < hits.length; i++) {
            if (hits[i] == numSick)
                output.append(i + 1).append(' ');
        }

        if (output.length() == 0) {
            return "0";
        } else {
            output.setLength(output.length() - 1);
        }
        return output.toString();
    }

}
