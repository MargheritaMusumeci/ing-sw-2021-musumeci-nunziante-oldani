package it.polimi.ingsw.client.cli.gamePhases;

import it.polimi.ingsw.client.cli.CLI;
import it.polimi.ingsw.messages.sentByClient.configurationMessagesClient.NickNameMessage;
import it.polimi.ingsw.utils.Constants;

import java.util.Scanner;

public class NickNamePhase extends Phase{
    @Override
    public void makeAction(CLI cli) {
        String nic;
        Scanner scanner = new Scanner(System.in);
        do{
            System.out.print(Constants.ANSI_CYAN + "Enter your nickname: " + Constants.ANSI_RESET);
            nic = scanner.next();
            cli.getClientSocket().send(new NickNameMessage(nic));

            try {
                synchronized (this){
                    //nel message handler bisogna mettere che a risveglairsi non deve essere la cli ma la phase della cli
                    //e la sincronizzazione del risveglio deve essere fatta sulla pahse della cli e non più sulla cli
                    wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IllegalMonitorStateException e){
                e.printStackTrace();
            }


        }while(!cli.isAckArrived());


        if(cli.isAckArrived()){
            cli.setNickname(nic);
            cli.setGamePhase(new NumberOfPlayerPhase());
            cli.setIsAckArrived(false);
            new Thread(cli).start();
        }else{
            cli.setIsNackArrived(false);
        }
    }
}
