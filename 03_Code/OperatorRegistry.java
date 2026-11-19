package chocan;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Stores the operators who may log in to the operator terminal, loaded
 * from data/operators.txt. On the first run it creates one sample operator
 * (number 111111111).
 *
 * @author Jacob Wilson
 */
public class OperatorRegistry {
    private final Path fileName;
    private final Map<Integer, Operator> operators = new LinkedHashMap<>();

    public OperatorRegistry(Path dataDir) {
        this.fileName = dataDir.resolve("operators.txt");
        load();
    }

    public Operator findOperator(int operatorNumber) {
        return operators.get(operatorNumber);
    }

    private void load() {
        if (!RecordFile.exists(fileName)) {
            operators.put(111111111, new Operator(111111111, "Olivia Ortiz"));
            List<String> lines = new ArrayList<>();
            for (Operator o : operators.values()) {
                lines.add(RecordFile.join(o.getNumber(), o.getName()));
            }
            RecordFile.write(fileName, lines);
            return;
        }
        for (String[] f : RecordFile.read(fileName, 2)) {
            Operator o = new Operator(Integer.parseInt(f[0]), f[1]);
            operators.put(o.getNumber(), o);
        }
    }
}
