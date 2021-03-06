import net.jqwik.api.*;
import org.assertj.core.api.Assertions;

import java.util.*;
import java.util.stream.IntStream;

public class LegionellosisTest {

    @Property(tries = 1000)
    String matchesOldImplementation(@ForAll("data") Tuple.Tuple3<Tuple.Tuple3<Integer, Integer, Integer>, Tuple.Tuple2<Integer, Integer>[], Tuple.Tuple2<Integer, Integer>[]> data) {
        Tuple.Tuple3<Integer, Integer, Integer> SLC = data.get1();
        int L = SLC.get2();
        @SuppressWarnings("unchecked") List<Short>[] graph = (List<Short>[]) new List[L];
        for (int i = 0; i < graph.length; i++) {
            graph[i] = new LinkedList<>();
        }
        MultidirGraphOracle graphOracle = new MultidirGraphOracle(L);
        int C = SLC.get3();
        int[] connections = Arrays.stream(data.get2()).flatMapToInt(l1l2 -> IntStream.of(l1l2.get1(), l1l2.get2())).toArray();
        for (int i = 0; i < C; i++) {
            int l1 = connections[i * 2] - 1;
            int l2 = connections[i * 2 + 1] - 1;
            graph[l1].add((short) l2);
            graph[l2].add((short) l1);
            graphOracle.addConnection(l1, l2);
        }
        int S = SLC.get1();
        int[] interviewsData = Arrays.stream(data.get3()).flatMapToInt(hd -> IntStream.of(hd.get1(), hd.get2())).toArray();
        int[] interviews = new int[2 * S];
        for (int i = 0; i < S; i++) {
            interviews[i * 2] = interviewsData[i * 2] - 1;
            interviews[i * 2 + 1] = interviewsData[i * 2 + 1];
        }
        short[] interviews2 = new short[2 * S];
        for (int i = 0; i < S; i++) {
            interviews2[i * 2] = (short) (interviewsData[i * 2] - 1);
            interviews2[i * 2 + 1] = (short) interviewsData[i * 2 + 1];
        }
        LegionellosisOracle original = new LegionellosisOracle(graphOracle, Arrays.copyOf(interviews, interviews.length));
        Legionellosis current = new Legionellosis(graph, interviews2);
        String currentResult = current.perilousLocations();
        Assertions.assertThat(currentResult).isEqualTo(original.res());
        return currentResult;
    }

    @Provide
    @SuppressWarnings("unchecked")
    Arbitrary<Tuple.Tuple3<Tuple.Tuple3<Integer, Integer, Integer>, Tuple.Tuple2<Integer, Integer>[], Tuple.Tuple2<Integer, Integer>[]>> data() {
        return Arbitraries.integers().between(1, 20)
            .flatMap(S -> Arbitraries.integers().between(Math.max(2, S), 10000)
            .flatMap(L -> Arbitraries.integers().between(L - 1, Math.min(17500, (L * (L - 1)) / 2))
            .flatMap(C -> Combinators.combine(
                Arbitraries.just(Tuple.of(S, L, C)),
                Arbitraries.randomValue(random -> {
                    int[] l1s = new int[C];
                    for (int i = 0; i < L - 1; i++) {
                        l1s[i] = i + 1;
                    }
                    for (int i = L - 1; i < C; i++) {
                        l1s[i] = random.nextInt(L) + 1;
                    }
                    Map<Integer, Set<Integer>> antiDup = new HashMap<>(C);
                    for (int i = 1; i <= L; i++) {
                        antiDup.put(i, new HashSet<>());
                    }
                    int[] l2s = new int[C];
                    for (int i = 0; i < C; i++) {
                        int l2;
                        do {
                            l2 = random.nextInt(L) + 1;
                        } while (l2 == l1s[i]
                                && antiDup.get(l2).contains(l1s[i])
                                && antiDup.get(l1s[i]).contains(l2));
                        l2s[i] = l2;
                    }
                    if (Arrays.stream(l1s).noneMatch(l1 -> l1 == L) && Arrays.stream(l2s).noneMatch(l2 -> l2 == L)) {
                        l2s[random.nextInt(C)] = L;
                    }
                    Tuple.Tuple2<Integer, Integer>[] connections = new Tuple.Tuple2[C];
                    for (int i = 0; i < C; i++) {
                        connections[i] = Tuple.of(l1s[i], l2s[i]);
                    }
                    return connections;
                }),
                Combinators.combine(
                    Arbitraries.integers().between(1, L)
                        .array(Integer[].class)
                        .uniqueElements()
                        .ofSize(S),
                    Arbitraries.integers().between(1, L - 1)
                        .array(Integer[].class)
                        .ofSize(S)
                ).flatAs((h, d) -> {
                    Tuple.Tuple2<Integer, Integer>[] hd = new Tuple.Tuple2[h.length];
                    for (int i = 0; i < h.length; i++) {
                        hd[i] = Tuple.of(h[i], d[i]);
                    }
                    return Arbitraries.just(hd);
                })
            ).as(Tuple::of))));
    }

    static class LegionellosisOracle {

        private final MultidirGraphOracle graph;
        //[home,maxDistance]
        private final int[] sick;

        public LegionellosisOracle(MultidirGraphOracle graph, int[] sick) {
            this.graph = graph;
            this.sick = sick;
        }

        public String res() {
            int[] hits = new int[graph.size()];
            for (int i = 0; i < sick.length / 2; i++) {
                boolean[] processed = new boolean[graph.size()];
                explore(sick[i * 2], processed, sick[i * 2 + 1]);
                for (int b = 0; b < processed.length; b++) {
                    if (processed[b])
                        hits[b]++;
                }
            }
            StringBuilder output = new StringBuilder();
            for (int i = 0; i < graph.size(); i++) {
                if (hits[i] == sick.length / 2)
                    output.append(i + 1).append(" ");
            }

            if (output.length() == 0) {
                return "0";
            } else {
                output.setLength(output.length() - 1);
            }
            return output.toString();
        }

        private void explore(int current, boolean[] processed, int currentLevel) {
            Queue<Integer> q = new LinkedList<>();
            q.add(current);
            processed[current] = true;
            int toNextLevel = 1;
            int toNextLevelDec = 0;
            while (!q.isEmpty()) {
                current = q.poll();
                processed[current] = true;
                MultidirGraphOracle.NodeIterator iter = graph.iteratorAt(current);
                while (iter.hasNext()) {
                    int a = iter.getNext();
                    if (!processed[a]) {
                        toNextLevelDec++;
                        q.add(a);
                    }
                }
                if (--toNextLevel == 0) {
                    if (--currentLevel < 0) break;
                    toNextLevel = toNextLevelDec;
                    toNextLevelDec = 0;
                }
            }
        }
    }

    static class MultidirGraphOracle {

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

        public MultidirGraphOracle(int size) {
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

}
