package it.polimi.ingsw.client.cli.gamePhases;

import it.polimi.ingsw.client.cli.CLI;
import it.polimi.ingsw.messages.sentByClient.configurationMessagesClient.NickNameMessage;
import it.polimi.ingsw.utils.Constants;

import java.util.Scanner;
/**
 * class able to handle the initialization phase in which the user have to insert
 * the nickname
 */
public class NickNamePhase extends Phase{
    /**
     * method able to handle the nickname insertion and wait for an ack
     * @param cli is client's cli
     */
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
                    wait();
                }
            } catch (InterruptedException e) {
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
