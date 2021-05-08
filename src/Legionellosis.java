import java.util.LinkedList;
import java.util.Queue;

public class Legionellosis {
    private final MultidirGraph graph;
    //[home,maxDistance]
    private final int[] sick;
    private int max;
    public Legionellosis(MultidirGraph graph,int[] sick){
        this.graph = graph;
        this.sick = sick;
        max = 0;
    }
    public String res(){
        int[] hits = new int[graph.size()];
        for(int i = 0;i<sick.length/2;i++){
            boolean[] processed = new boolean[graph.size()];
            explore(sick[i*2],processed,sick[i*2+1]);
            System.out.println("---");
            for (int b = 0;b< processed.length;b++){
                if (processed[b])
                    hits[b]++;
            }
        }
        for(int i = 0;i< graph.size();i++){
            if(hits[i]>max)
                max = hits[i];
        }
        StringBuilder output = new StringBuilder();
        for(int i = 0;i< graph.size();i++){
            if(hits[i]==max)
                output.append(i+1).append(" ");
        }

        if (output.isEmpty()){
            return "0";
        }else{
            output.setLength(output.length()-1);
        }
        return output.toString();
    }
    private void explore(int current,boolean[] processed,int currentLevel){
        Queue<Integer> q = new LinkedList<>();
        q.add(current);
        processed[current] = true;
        int toNextLevel = 1;
        int toNextLevelDec = 0;
        while(!q.isEmpty()){
            current = q.poll();
            processed[current] = true;
            System.out.println((currentLevel) + " "+ toNextLevel + " " + (current+1));
            NodeIterator iter = graph.getIterator(current);
            while (iter.hasNext()) {
                int a = iter.getNext().getId();
                if(!processed[a]) {
                    toNextLevelDec++;
                    q.add(a);
                }
            }
            if (--toNextLevel == 0) {
                if(--currentLevel<0) break;
                toNextLevel = toNextLevelDec;
                toNextLevelDec = 0;
            }
        }

    }
}
