package com.dkamin.distributedscorecard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.util.Scanner;

@SpringBootApplication
public class Game {

    public static void main(String[] args) {
         SpringApplication.run(Game.class, args);
        // new SpringApplicationBuilder(Game.class).web(false).run(args);
        System.out.print("\n" + "\u001B[32m" + "Hello. BUILD SUCCESS \n" + "\u001B[0m");
        
        Scanner scanner = new Scanner(System.in);
        ZKmethods scoreBoard = null;
        
        while (true) {
            String input = scanner.nextLine();
            String[] commands = input.split(" ");

            if (commands.length < 3) {
                System.out.println("\n" + "Invalid argument size. Please pass enough arguments for desired process.");
			}
			
            String connectionString = commands[1];
            if (commands[0].toLowerCase().equals("watcher")) {
                
                int listSize = 0;
                try {
                    listSize = Integer.parseInt(commands[2]);

                    scoreBoard = new ZKmethods(connectionString, listSize);
                } catch (NumberFormatException ex) {
                    ex.printStackTrace();
                    System.out.println("\nPass list size as third argument");
                }


            } else if (commands[0].toLowerCase().equals("player")) {
                if (commands.length == 3) {

                    String playerName = commands[2];
                    if (Helper.playerExists(playerName, connectionString)) {
                        System.out.println("\nPlayer already exists!");
                    } else {
                        System.out.println("\n Node added - " + playerName);

                        Player newPlayer = new Player(commands[2], connectionString);
                        newPlayer.joinScoreboard();
                    }
                }else if (commands.length == 6) {
                    Helper.postScore(connectionString,commands[2],
                            Integer.parseInt(commands[3]),
                            Float.parseFloat(commands[5]),
                            Float.parseFloat(commands[4])); 
                }
            }  else if(commands[0].toLowerCase().equals("delete")){
                scoreBoard.deleteParentNodes();
            }else{
                System.out.println("\n" + "Not enough number of arguments. Please pass enough arguments for desired process.");
            }
        }

	}

}

