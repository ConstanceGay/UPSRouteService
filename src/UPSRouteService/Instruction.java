package UPSRouteService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Instruction {

    private String instruction;
    private List<Integer> wayPoints;
    private static Map<Integer, String> instructionTypeNoNameMap = new HashMap<>() {{
        put(0, "Tourner à gauche");
        put(1, "Tourner à droite");
        put(4, "Tourner légerement à gauche");
        put(10, "Votre destination est sur la gauche");
        put(11, "Prendre la direction nord-est");
    }};
    private static Map<Integer, String> instructionTypeMap = new HashMap<>() {{
        put(0, "Tournez à gauche en direction de ");
        put(1, "Tournez à droite sur ");
        put(4, "Tourner légerement à gauche sur ");
        put(10, "Votre destination est sur la gauche sur ");
        put(11, "Prendre la direction nord-est sur ");
    }};

    Instruction(String name, int type, List<Integer> wayPoints) {
        this.wayPoints = wayPoints;
        instruction = (name.equals("-")) ? instructionTypeNoNameMap.get(type) : instructionTypeMap.get(type) + name;
    }

    Instruction(List<Integer> wayPoints, String instruction) {
        this.wayPoints = wayPoints;
        this.instruction = instruction;
    }

    public String getInstruction() {
        return instruction;
    }

    public List<Integer> getWayPoints() {
        return wayPoints;
    }

    public String toString() {
        return instruction;
    }

}
