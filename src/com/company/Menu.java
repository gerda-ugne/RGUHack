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
        System.out.println();
        System.out.println();
        System.out.println("`" +
                " MMMMMMMb.                                                                           `MM                 \n" +
                " MM    `Mb                                                                            MM                 \n" +
                " MM     MM ___  __   ____      ___   ___  __    __         ___  __   ____      ___    MM ___  __    __   \n" +
                " MM     MM `MM 6MM  6MMMMb   6MMMMb  `MM 6MMb  6MMb        `MM 6MM  6MMMMb   6MMMMb   MM `MM 6MMb  6MMb  \n" +
                " MM     MM  MM69 \" 6M'  `Mb 8M'  `Mb  MM69 `MM69 `Mb        MM69 \" 6M'  `Mb 8M'  `Mb  MM  MM69 `MM69 `Mb \n" +
                " MM     MM  MM'    MM    MM     ,oMM  MM'   MM'   MM        MM'    MM    MM     ,oMM  MM  MM'   MM'   MM \n" +
                " MM     MM  MM     MMMMMMMM ,6MM9'MM  MM    MM    MM        MM     MMMMMMMM ,6MM9'MM  MM  MM    MM    MM \n" +
                " MM     MM  MM     MM       MM'   MM  MM    MM    MM        MM     MM       MM'   MM  MM  MM    MM    MM \n" +
                " MM    .M9  MM     YM    d9 MM.  ,MM  MM    MM    MM        MM     YM    d9 MM.  ,MM  MM  MM    MM    MM \n" +
                "_MMMMMMM9' _MM_     YMMMM9  `YMMM9'Yb_MM_  _MM_  _MM_      _MM_     YMMMM9  `YMMM9'Yb_MM__MM_  _MM_  _MM_\n" +
                "                                                                                                         \n" +
                "                                                                                                         \n" +
                "                                                                                                         ");
        typeSlowest("\nThere is nothing better than a long, careless sleep. So this is what you do - you are asleep." +
                "\nYou are in an amazing place, full of wonders. Lost in the dreamland. But how do you get out of there?" +
                "\nYou cannot stay there forever. You need to wake up, but you find yourself lost. Find the exit. Escape," +
                "\nbefore it's too late.");

        typeSlow("\nYou've entered the dream realm.");
        Scanner s = new Scanner (System.in);
        String input;

        do {

            System.out.println("\n\nPlease choose one of the following options:");
            System.out.println("1. Start a new game");
            System.out.println("2. Load a saved game");
            System.out.println("3. Display a game guide");
            System.out.println("0. Exit");

            input = s.nextLine();

            switch(input)
            {
                case "1":
                {
                    typeSlow("\nYou open your eyes. It is not your room anymore. You are outside, surrounded by hedges and a complete darkness.\n" +
                            "The only source of light is an oil lamp that lies in front of you. You get up and take it.\n");

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

                        if(game.getPlayer().getOil() < 5)
                        {
                            System.out.println("You have run out of oil. The darkness consumes you.");
                            game.gameOver();
                        }
                        game.displayMap();
                        System.out.println("\nChoose your direction of floating:");

                        System.out.println("u   - to go up");
                        System.out.println("d   - to go down");
                        System.out.println("l   - to go left");
                        System.out.println("r   - to go right");

                        System.out.println("\nEach turn consumes oil. You have " + game.getPlayer().getOil() +" oil left. Make sure it " +
                                "doesn't run out, otherwise the nightmares might end you.");

                        System.out.println("0   - to return");

                        choice = s.nextLine();
                        if(choice.equals("0")) break;
                        canMove = game.movePlayer(choice);

                        if(canMove)
                        {
                            game.getPlayer().setOil(game.getPlayer().getOil() - 5);
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
                case "2":
                    saveGame();
                    break;
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

    public void typeSlow(String text)
    {
        String[] txt = text.split("");

        for (String aTxt : txt)
        {
            System.out.print(aTxt);
            sleepMe(7);
        }

        System.out.println();
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
    }

    public void typeSlowest(String text)
    {
        String[] txt = text.split("");

        for (String aTxt : txt)
        {
            System.out.print(aTxt);
            sleepMe(15);
        }

        System.out.println();
    }

    public void sleepMe(int time)
    {
        try
        {
            Thread.sleep(time);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }



}