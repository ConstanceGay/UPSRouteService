package Interface;

import UPSRouteService.Instruction;
import t2s.son.LecteurTexte;

import javax.swing.*;

public class TextToVoice implements Runnable {

    private int mode;
    private String message;
    private DefaultListModel<Instruction> list;
    private LecteurTexte reader;

    public TextToVoice(String message, LecteurTexte lt){
        this.mode = 1;
        this.message = message;
        this.reader = lt;
    }

    public TextToVoice(DefaultListModel<Instruction> list, LecteurTexte lt){
        this.mode = 2;
        this.list = list;
        this.reader = lt;
    }

    public void run() {

        switch (this.mode){
            case 1 :

                reader.setTexte(this.message);
                reader.playAll();
                break;
            case 2:
                for (int ite=0;ite<this.list.getSize();ite++){
                    Instruction step = this.list.getElementAt(ite);
                    if(step.toString() != null){
                        System.out.println(step.toString());
                        reader.setTexte(step.toString());
                        reader.playAll();
                    }
                }
                break;

        }
    }

}
