package Interface;

import UPSRouteService.Instruction;
import t2s.son.LecteurTexte;

import javax.swing.*;
import java.util.concurrent.TimeUnit;

public class TextToVoice extends Thread {

    private int mode;
    private String message;
    private DefaultListModel<Instruction> list;

    public TextToVoice(String message){
        this.mode = 1;
        this.message = message;
    }

    public TextToVoice(DefaultListModel<Instruction> list){
        this.mode = 2;
        this.list = list;
    }

    public void run() {
        final LecteurTexte reader = new LecteurTexte();
        switch (this.mode){
            case 1 :

                reader.setTexte(this.message);
                reader.play();
                break;
            case 2:
                for (int ite=0;ite<this.list.getSize();ite++){
                    Instruction step = this.list.getElementAt(ite);
                    if(step.toString() != null){
                        System.out.println(step.toString());
                        reader.setTexte(step.toString());
                        reader.play();
                    }
                }
                break;

        }
    }

}
