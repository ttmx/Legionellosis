import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.Tuple;
import org.assertj.core.api.Assertions;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class LegionellosisTestGenerator extends LegionellosisTest {

    private int testId = 0;

    @Property(tries = 100)
    void writeToFile(@ForAll("data") Tuple.Tuple3<Tuple.Tuple3<Integer, Integer, Integer>, Tuple.Tuple2<Integer, Integer>[], Tuple.Tuple2<Integer, Integer>[]> data) {
        StringBuilder sb = new StringBuilder();
        Tuple.Tuple3<Integer, Integer, Integer> SLC = data.get1();
        int L = SLC.get2();
        int C = SLC.get3();
        sb.append(L).append(' ').append(C).append('\n');
        for (Tuple.Tuple2<Integer, Integer> l1l2 : data.get2()) {
            sb.append(l1l2.get1()).append(' ').append(l1l2.get2()).append('\n');
        }
        int S = SLC.get1();
        sb.append(S).append('\n');
        for (Tuple.Tuple2<Integer, Integer> hd : data.get3()) {
            sb.append(hd.get1()).append(' ').append(hd.get2()).append('\n');
        }
        sb.setLength(sb.length() - 1);
        testId++;
        try {
            Files.createDirectories(Paths.get(System.getProperty("user.dir") + "/tests/input/"));
            Files.createDirectories(Paths.get(System.getProperty("user.dir") + "/tests/output/"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (FileWriter writer = new FileWriter(System.getProperty("user.dir") + "/tests/input/" + testId + "_input.txt")) {
            writer.write(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (FileWriter writer = new FileWriter(System.getProperty("user.dir") + "/tests/output/" + testId + "_output.txt")) {
            writer.write(matchesOldImplementation(data) + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
