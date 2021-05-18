package it.polimi.ingsw.client.gamePhases;

import it.polimi.ingsw.client.CLI.CLI;
import it.polimi.ingsw.client.GamePhases;
import it.polimi.ingsw.messages.sentByClient.configurationMessagesClient.NickNameMessage;

import java.util.Scanner;

public class NickNamePhase extends Phase{
    @Override
    public void makeAction(CLI cli) {
        String nic;
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your nickname: ");
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

        if(cli.isAckArrived()){
            cli.setNickname(nic);
            cli.setGamePhase(new NumberOfPlayerPhase());
            cli.setIsAckArrived(false);
            new Thread(cli).start();
        }else{
            cli.setIsNackArrived(false);
            System.err.println("The nickname that you have chose is already in use, please pick a different nickname");
        }
    }
}
