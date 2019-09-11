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
        put(2, "Tourner immédiatement à gauche");
        put(3, "Tourner immédiatement à droite");
        put(4, "Tourner légèrement à gauche");
        put(5, "Tourner légèrement à droite");
        put(10, "Votre destination est droit devant");
        put(12, "Rester à gauche");
        put(13, "Rester à droite");
    }};

    private static Map<Integer, String> instructionTypeMap = new HashMap<>() {{
        put(0, "Tournez à gauche en direction de ");
        put(1, "Tournez à droite sur ");
        put(2, "Tourner immédiatement à gauche sur ");
        put(3, "Tourner immédiatement à droite sur ");
        put(4, "Tourner légèrement à gauche sur ");
        put(5, "Tourner légèrement à droite sur ");
        put(10, "Votre destination est droit devant sur ");
        put(12, "Rester à gauche sur ");
        put(13, "Rester à droite sur ");
    }};

    Instruction(String name, int type, List<Integer> wayPoints) {
        this.wayPoints = wayPoints;
        instruction = (name.equals("-")) ? instructionTypeNoNameMap.get(type) : instructionTypeMap.get(type) + name;
    }

    Instruction(List<Integer> wayPoints, String instruction) {
        this.wayPoints = wayPoints;
        this.instruction = instruction;
    }

    public List<Integer> getWayPoints() {
        return wayPoints;
    }

    public String toString() {
        return instruction;
    }

}
