package com.company;

import java.util.*;
public class Menu
{
    Game game;
    public Menu()
    {
        game = new Game();
    }

    public static void main(String[] args) {
        Menu menu = new Menu();
        // Introduces play to the game
        menu.introduction();

    }

    /**
     * Introduction to the game.
     * Prompts to start a new game or load a saved game
     */
    public void introduction()
    {
        //Insert introduction and ascii art here

        System.out.println("You've entered the dream realm.");
        Scanner s = new Scanner (System.in);
        String input;

        do {

            System.out.println("Please choose one of the following options:");
            System.out.println("1. Start a new game");
            System.out.println("2. Load a saved game");
            System.out.println("3. Display a game guide");
            System.out.println("0. Exit");

            input = s.nextLine();

            switch(input)
            {
                case "1":
                {
                    System.out.println("\nYou open your eyes. It is not your room anymore. You are outside, surrounded by hedges and a complete darkness.");
                    System.out.println("The only source of light is an oil lamp that lies in front of you. You get up and take it.");

                    game.displayMap();
                    gameMenu();
                } break;
                case "2": loadGame(); break;
                case "0": System.out.println("Thank you for playing! See you soon."); System.exit(1);
                default: System.out.println("Your input is invalid! Please enter an integer from 0 to 2.");
            }

        } while (!(input.equals("0")));
    }

    /**
     * Game menu, which is displayed after loading a game or starting a new one
     */
    public void gameMenu()
    {
        Scanner s = new Scanner(System.in);
        String input;

        do {

            System.out.println("\nPlease choose one of the following options:");
            System.out.println("1. Make a move");
            System.out.println("2. Save the game progress");
            System.out.println("3. Check your health status");
            System.out.println("4. Check your inventory");
            System.out.println("5. Check for traps");
            System.out.println("0. Exit the game");

            input = s.nextLine();

            switch(input)
            {
                case "1":
                {
                    boolean canMove = false;
                    Scanner sc = new Scanner(System.in);
                    String choice;


                    do {

                        game.displayMap();
                        System.out.println("\nChoose your direction of floating:");

                        System.out.println("u   - to go up");
                        System.out.println("d   - to go down");
                        System.out.println("l   - to go left");
                        System.out.println("r   - to go right");

                        System.out.println("0   - to return");

                        choice = s.nextLine();
                        if(choice.equals("0")) break;
                        canMove = game.movePlayer(choice);

                        if(canMove)
                        {
                            System.out.println("You have moved.\n");
                            game.displayMap();
                        }
                        else
                        {
                            System.out.println("Moving in that direction is forbidden!");
                        }

                    } while (!canMove);

                    break;
                }
//                case "2": saveGame(); break;
                 case "3":
                 {
                     System.out.println("You closely focus on your heartbeat.\n");
                     System.out.println("Your status:");

                    game.getPlayer().checkStatus();
                    break;
                 }
               case "4":
               {
                   System.out.println("/n Here is your inventory. Defeat your nightmares or trade to acquire more items.\n");
                   game.getPlayer().getInv().showInventory();break;
               }

                case "5":
                {


                    boolean hasRock = game.getPlayer().getInv().findInInventory("Rock");
                    boolean trapExists = false;
                    if(hasRock)
                    {
                        game.disableTrap();
                        trapExists = game.getPlayer().getInv().remove("Rock");

                        if(trapExists)
                        {
                            System.out.println("Good job! You have successfully disabled a deadly trap.");

                        }
                        else
                        {
                            System.out.println("There were no traps found. However, you have wasted your rock.");
                        }

                    }
                    else
                    {
                        System.out.println("You don't have enough rocks to perform this action.");
                    }
                    break;
                }

                case "0": System.out.println("Thank you for playing! See you soon."); System.exit(1);
                default: System.out.println("Your input is invalid! Please enter an integer from 0 to 5.");
            }


        } while(!(input.equals("0")));
    }

    /**
     * Starts the game
     */
    public void  startGame()
    {

    }

    /**
     * Saves the game progress
     */
    public void saveGame()
    {

    }

    /**
     * Loads the game file
     */
    public void loadGame()
    {

    }



}