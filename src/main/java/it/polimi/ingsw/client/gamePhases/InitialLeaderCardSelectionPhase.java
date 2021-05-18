package it.polimi.ingsw.client.gamePhases;

import it.polimi.ingsw.client.CLI.CLI;
import it.polimi.ingsw.client.GamePhases;
import it.polimi.ingsw.messages.sentByClient.configurationMessagesClient.LeaderCardChoiceMessage;

import java.util.ArrayList;
import java.util.Scanner;

public class InitialLeaderCardSelectionPhase extends Phase{
    @Override
    public void makeAction(CLI cli) {
        Scanner scanner = new Scanner(System.in);
        while(cli.getLeaderCards() == null){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        int index = 0;
        ArrayList<Integer> lCards = new ArrayList<>();

        System.out.println("Chose 2 cards");
        System.out.println();
        for(int i = 0 ; i < cli.getLeaderCards().size() ; i++){
            System.out.println("Card " + i + ": " + cli.getLeaderCards().get(i).getRequiresForActiveLeaderCards() + " , " + cli.getLeaderCards().get(i).getAbilityType() + "\n");
        }
        for(int i = 0; i < 2; i++){
            index = scanner.nextInt();
            if(index < cli.getLeaderCards().size() && index >= 0){
                lCards.add(index);
            }
            else{
                i--;
                System.out.println("Carta scelta non valida");
            }
        }
        cli.getClientSocket().send(new LeaderCardChoiceMessage("Leader card scelte" , lCards));

        try {
            synchronized (this){
                wait();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IllegalMonitorStateException e){
            e.printStackTrace();
        }

        if(cli.isAckArrived()){
            cli.setGamePhase(new InitialResourcesSelection());
            cli.setIsAckArrived(false);
            new Thread(cli).start();
        }else{
            cli.setIsNackArrived(false);
            System.err.println("Error while setting the initial leader card, retry");
        }
    }
}
