package Interface;

import UPSRouteService.Instruction;
import t2s.son.LecteurTexte;

import javax.swing.*;

public class TextToVoice implements Runnable {

    private String message;
    private DefaultListModel<Instruction> list;
    private LecteurTexte reader;

    public TextToVoice(String message, LecteurTexte lt){
        this.message = message;
        this.reader = lt;
    }

    public void run() {
                reader.setTexte(this.message);
                reader.playAll();
    }

}
