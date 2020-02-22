package com.company;

import java.util.Scanner;

public class Game {

    private Player player;
    private Enemy enemy;
    private NPC npc;

    public Game()
    {
        player = new Player();
        enemy = new Enemy();
        npc = new NPC();


    }

    public void tradeLog() {
        System.out.println("You have opened the trade log. You can buy valuable items that will aid you in your journey");
        System.out.println("You can also sell items from your inventory. However, note that you cannot re-buy them.\n");

        System.out.println("Trade log open!\n");

        Scanner s = new Scanner(System.in);
        String input;

        //Looped until user exits
        do {
            System.out.println("Your inventory:\n");
            player.showInventory();

            System.out.println(npc.getName() + "'s inventory:");
            npc.showInventory();

            System.out.println("Do you want to buy or sell items?\n");
            System.out.println("1. Buy");
            System.out.println("2. Sell");
            System.out.println("0. Leave");


            System.out.print("Please choose your option:");
            //Input is scanned
            input = s.nextLine();

            switch (input) {
                case "1":
                    buyItems();
                    break;
                case "2":
                    sellItems();
                    break;
                case "0":
                    System.out.println("You have opted to return to your adventure.");
                    return;
                default:
                    System.out.println("Your choice is not valid. Please try again!");
            }

        } while (!(input == "0"));

    }

    public void sellItems()
    {
        System.out.println("Please state what item you want to sell:");
        player.showInventory();

        Scanner s = new Scanner(System.in);
        String input;

            input = s.nextLine();
            switch(input)
            {

            }

    }

    public void buyItems()
    {

    }

}