package UPSRouteService;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Path implements Iterable<Instruction> {

    private List<Instruction> instructions = new ArrayList<>();

    void add(Instruction instruction) {
        instructions.add(instruction);
    }

    public List<Instruction> getInstructions(){ return instructions; }

    public String toString() {
        return instructions.toString();
    }

    @Override
    public Iterator<Instruction> iterator() { return instructions.iterator();
    }

}
