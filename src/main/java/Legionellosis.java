import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Legionellosis {

    private final List<Short>[] graph;
    //[home,maxDistance]
    private final short[] interviews;
    private final byte numSick;

    public Legionellosis(List<Short>[] graph, short[] interviews) {
        this.graph = graph;
        this.interviews = interviews;
        numSick = (byte) (interviews.length / 2);
    }

    public String perilousLocations() {
        byte[] hits = new byte[graph.length];
        boolean[] processed = new boolean[graph.length];
        for (short i = 0; i < numSick; i++) {
            Arrays.fill(processed, false);
            if (!bfs(hits, processed, interviews[i * 2], interviews[i * 2 + 1], (short) (i + 1))) {
                return "0";
            }
        }
        return formatOutput(hits);
    }

    private boolean bfs(byte[] hits, boolean[] processed, short current, short depthLimit, short minHits) {
        Queue<Short> border = new LinkedList<>();
        boolean valid = false;
        border.add(current);
        processed[current] = true;
        if (++hits[current] == minHits) {
            valid = true;
        }
        int lastBorderSize = 1;
        short currentDepth = 0;
        do {
            current = border.remove();
            if (--lastBorderSize == 0) {
                currentDepth++;
            }
            for (Short node : graph[current]) {
                if (!processed[node]) {
                    processed[node] = true;
                    if (++hits[node] == minHits) {
                        valid = true;
                    }
                    if (currentDepth < depthLimit) {
                        border.add(node);
                    }
                }
            }
            if (lastBorderSize == 0) {
                lastBorderSize = border.size();
            }
        } while (!border.isEmpty() && currentDepth < depthLimit);
        return valid;
    }

    private String formatOutput(byte[] hits) {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < hits.length; i++) {
            if (hits[i] == numSick) {
                output.append(i + 1).append(' ');
            }
        }
        if (output.length() == 0) {
            return "0";
        } else {
            output.setLength(output.length() - 1);
        }
        return output.toString();
    }

}
