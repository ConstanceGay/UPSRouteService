package UPSRouteService;

import java.util.ArrayList;
import java.util.List;

public class Path {

    private List<Instruction> instructions = new ArrayList<>();

    public void add(Instruction instruction) {
        instructions.add(instruction);
    }

    public String toString() {
        return instructions.toString();
    }

}
