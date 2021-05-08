import java.io.IOException;

public class Main{
	public static void main(String[] args) throws IOException {
	    TurboScanner in = new TurboScanner(System.in);
	    int locNum = in.nextInt();
		MultidirGraph graph = new MultidirGraph(locNum);
		int conNum = in.nextInt();
		for (int i = 0;i<conNum;i++){
			graph.addConnection(in.nextInt()-1,in.nextInt()-1);
		}
		int sickNum = in.nextInt();
		int[] sick = new int[2*sickNum];
		for (int i = 0;i<sickNum;i++){
			sick[i*2] = in.nextInt()-1;
			sick[i*2+1] = in.nextInt();
		}
		Legionellosis leg = new Legionellosis(graph,sick);
		System.out.println(leg.res());
	}

}
