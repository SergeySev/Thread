package org.example.paper;

import java.util.*;
import java.util.concurrent.Exchanger;

public class EXCHANGER {
    static Random random = new Random();
    static Action[] arrayActions = Action.values();
    static Exchanger<Action> exchanger = new Exchanger<>();

    public static void main(String[] args) {


        //Написать метод, который будет генерить Action и
        //передавать его в конструктор. Те
        //каждый раз будут выигрывать разные игроки

        //Написать метод, который принимает в качестве
        //значения кол-во игроков, которые должны создаться
        //и сыграть друг с другом на победу play(7)

        int countPlayers = 4;
        int countRound = 2;
        generatePlayer(countPlayers, countRound);

    }

    public static void generatePlayer(int count, int countAction) {
        if (count % 2 != 0) {
            System.out.println("We have a tournament with pair. Please create even number");
            return;
        }
        for (int i = 0; i < count; i++) {
            new Player("Player " + i, generateAction(countAction), exchanger);
        }
    }

    public static List<Action> generateAction(int count) {
        List<Action> listAction = new LinkedList<>();
        for (int i = 0; i < count; i++) {
            listAction.add(arrayActions[random.nextInt(0, arrayActions.length)]);
        }
        return listAction;
    }


}

enum Action {
    STONE,
    PAPER,
    SCISSORS
}

class Player extends Thread {
    private final String name;
    private final List<Action> actionList;

    private final Exchanger<Action> exchanger;

    private final Action[] actions = Action.values();

    public Player(String name, List<Action> actionList, Exchanger<Action> exchanger) {
        this.name = name;
        this.actionList = actionList;
        this.exchanger = exchanger;
        this.start();
    }

    private int giveIndex(Action action) {
        for (int i = 0; i < actions.length; i++) {
            if (actions[i] == action) {
                return i;
            }
        }
        return -1;
    }


    private void getWinner(Action my, Action op) {
        int myAction = giveIndex(my);
        int opAction = giveIndex(op);

        if (myAction - opAction == 0)
            System.out.println(name + " have a " + my + " --> TIE!" + "\nOpponent have a " + op + "\n");
        else if (myAction - opAction == -2 || myAction - opAction == 1)
            System.out.println(name + " have a " + my + " --> WINNER!" + "\nOpponent have a " + op + "\n");
//        else if (myAction - opAction == 2 || myAction - opAction == -1) System.out.println(name + " have a " + my + " --> Lose!" + "\nOpponent have a " + op + "\n");
    }

    @Override
    public void run() {
        Action reply;
        for (Action action : actionList) {
            try {
                reply = exchanger.exchange(action);
                getWinner(action, reply);
                Thread.sleep(2500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}