package it.polimi.ingsw.client.gamePhases.myTurnPhases;

import it.polimi.ingsw.client.CLI.CLI;
import it.polimi.ingsw.client.gamePhases.Phase;
import it.polimi.ingsw.messages.sentByClient.actionMessages.BuyEvolutionCardMessage;

import java.util.Scanner;

public class BuyEvolutiuonCardPhase extends Phase {
    @Override
    public void makeAction(CLI cli) {
        Scanner scanner = new Scanner(System.in);
        cli.printEvolutionSection();

        int row, col, pos;

        do{
            System.out.println("Inserisci la riga e la colonna della carta da comprare");
            System.out.print("riga > ");
            row = scanner.nextInt();
            System.out.print("colonna > ");
            col = scanner.nextInt();
            System.out.println("inserisci in quale production zone inserire la carta: (0,1,2)");
            System.out.print("> ");
            pos = scanner.nextInt();
        }while ((row<0 || row > 2) || (col <0 || col>3) || (pos != 0 && pos != 1 && pos != 2));

        cli.getClientSocket().send(new BuyEvolutionCardMessage("buy evolution card", row, col, pos));
        System.out.println("Message sent");
        synchronized (this){
            try {
                System.out.println("Waiting for ack/nack");
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if(cli.isAckArrived()){
            System.out.println("Carta inserita correttamente");
            cli.setIsAckArrived(false);
            cli.setActionBeenDone(true);
        }else{
            System.out.println("Errore durante l'acquisto della carta dalla evolution section");
            cli.setIsNackArrived(false);
        }

        cli.setGamePhase(new MyTurnPhase());
        new Thread(cli).start();
    }
}
