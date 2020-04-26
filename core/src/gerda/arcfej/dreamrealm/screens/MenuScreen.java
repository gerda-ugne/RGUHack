package gerda.arcfej.dreamrealm.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import gerda.arcfej.dreamrealm.GameCore;
import gerda.arcfej.dreamrealm.screens.GameScreen;

import java.util.*;

/**
 * The main class for the project Dream Realm.
 * It contains a menu with options, as well as an introduction to the game.
 *
 */
public class MenuScreen extends ScreenAdapter {

    /**
     * The controller of the game
     */
    private GameCore game;

    /**
     * The stage to display the menu on.
     */
    private Stage stage;

    /**
     * Default constructor of the class.
     *
     * @param game The controller of the game
     */
    public MenuScreen(GameCore game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());

        VerticalGroup menu = new VerticalGroup();
        menu.setFillParent(true);
        stage.addActor(menu);

        Label title = new Label("Dream Realm", game.skin);
        title.setAlignment(Align.center);
        menu.addActor(title);

        TextButton newGame = new TextButton("New Game", game.skin);
        newGame.setX(menu.getWidth() / 2 - newGame.getWidth() / 2);
        newGame.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                game.startNewGame();
            }
        });
        menu.addActor(newGame);
    }

    @Override
    public void dispose() {
        super.dispose();
        stage.dispose();
    }

    @Override
    public void show() {
        super.show();
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        // Clear the screen to black
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
    }

    //    /**
//     * Introduction to the game.
//     * Prompts to start a new game or load a saved game
//     */
//    public void introduction()
//    {
//        //Insert introduction and ascii art here
//        System.out.println();
//        System.out.println();
//        System.out.println("`" +
//                " MMMMMMMb.                                                                           `MM                 \n" +
//                " MM    `Mb                                                                            MM                 \n" +
//                " MM     MM ___  __   ____      ___   ___  __    __         ___  __   ____      ___    MM ___  __    __   \n" +
//                " MM     MM `MM 6MM  6MMMMb   6MMMMb  `MM 6MMb  6MMb        `MM 6MM  6MMMMb   6MMMMb   MM `MM 6MMb  6MMb  \n" +
//                " MM     MM  MM69 \" 6M'  `Mb 8M'  `Mb  MM69 `MM69 `Mb        MM69 \" 6M'  `Mb 8M'  `Mb  MM  MM69 `MM69 `Mb \n" +
//                " MM     MM  MM'    MM    MM     ,oMM  MM'   MM'   MM        MM'    MM    MM     ,oMM  MM  MM'   MM'   MM \n" +
//                " MM     MM  MM     MMMMMMMM ,6MM9'MM  MM    MM    MM        MM     MMMMMMMM ,6MM9'MM  MM  MM    MM    MM \n" +
//                " MM     MM  MM     MM       MM'   MM  MM    MM    MM        MM     MM       MM'   MM  MM  MM    MM    MM \n" +
//                " MM    .M9  MM     YM    d9 MM.  ,MM  MM    MM    MM        MM     YM    d9 MM.  ,MM  MM  MM    MM    MM \n" +
//                "_MMMMMMM9' _MM_     YMMMM9  `YMMM9'Yb_MM_  _MM_  _MM_      _MM_     YMMMM9  `YMMM9'Yb_MM__MM_  _MM_  _MM_\n" +
//                "                                                                                                         \n" +
//                "                                                                                                         \n" +
//                "                                                                                                         ");
//        typeSlowest("\nThere is nothing better than a long, careless sleep. So this is what you do - you are asleep." +
//                "\nYou are in an amazing place, full of wonders. Lost in the dreamland. But how do you get out of there?" +
//                "\nYou cannot stay there forever. You need to wake up, but you find yourself lost. Find the exit. Escape," +
//                "\nbefore it's too late.");
//
//        typeSlow("\nYou've entered the dream realm.");
//        Scanner s = new Scanner (System.in);
//        String input;
//
//        do {
//
//            System.out.println("\n\nPlease choose one of the following options:");
//            System.out.println("1. Start a new game");
//            System.out.println("2. Load a saved game");
//            System.out.println("3. Display a game guide");
//            System.out.println("0. Exit");
//
//            input = s.nextLine();
//
//            switch(input)
//            {
//                case "1":
//                {
//                    typeSlow("\nYou open your eyes. It is not your room anymore. You are outside, surrounded by hedges and a complete darkness.\n" +
//                            "The only source of light is an oil lamp that lies in front of you. You get up and take it.\n");
//
//                    gameScreen.displayMap();
//                    gameMenu();
//                } break;
//                case "2": loadGame(); break;
//                case "0": System.out.println("Thank you for playing! See you soon."); System.exit(1);
//                default: System.out.println("Your input is invalid! Please enter an integer from 0 to 2.");
//            }
//
//        } while (!(input.equals("0")));
//    }
//
//    /**
//     * Game menu, which is displayed after loading a game or starting a new one
//     */
//    public void gameMenu()
//    {
//        Scanner s = new Scanner(System.in);
//        String input;
//
//        do {
//
//            System.out.println("\nPlease choose one of the following options:");
//            System.out.println("1. Make a move");
//            System.out.println("2. Save the game progress");
//            System.out.println("3. Check your health status");
//            System.out.println("4. Check your inventory");
//            System.out.println("5. Check for traps");
//            System.out.println("0. Exit the game");
//
//            input = s.nextLine();
//
//            switch(input)
//            {
//                case "1":
//                {
//                    boolean canMove = false;
//                    Scanner sc = new Scanner(System.in);
//                    String choice;
//
//
//                    do {
//
//                        if(gameScreen.getPlayer().getOil() < 5)
//                        {
//                            System.out.println("You have run out of oil. The darkness consumes you.");
//                            gameScreen.gameOver();
//                        }
//                        gameScreen.displayMap();
//                        System.out.println("\nChoose your direction of floating:");
//
//                        System.out.println("u   - to go up");
//                        System.out.println("d   - to go down");
//                        System.out.println("l   - to go left");
//                        System.out.println("r   - to go right");
//
//                        System.out.println("\nEach turn consumes oil. You have " + gameScreen.getPlayer().getOil() +" oil left. Make sure it " +
//                                "doesn't run out, otherwise the nightmares might end you.");
//
//                        System.out.println("0   - to return");
//
//                        choice = s.nextLine();
//                        if(choice.equals("0")) break;
//                        canMove = gameScreen.movePlayer(choice);
//
//                        if(canMove)
//                        {
//                            gameScreen.getPlayer().setOil(gameScreen.getPlayer().getOil() - 5);
//                            System.out.println("You have moved.\n");
//                            gameScreen.displayMap();
//                        }
//                        else
//                        {
//                            System.out.println("Moving in that direction is forbidden!");
//                        }
//
//                    } while (!canMove);
//
//                    break;
//                }
//                case "2":
//                    saveGame();
//                    break;
//                 case "3":
//                 {
//                     System.out.println("You closely focus on your heartbeat.\n");
//                     System.out.println("Your status:");
//
//                    gameScreen.getPlayer().checkStatus();
//                    break;
//                 }
//               case "4":
//               {
//                   System.out.println("\nHere is your inventory. Defeat your nightmares or trade to acquire more items.\n");
//                   gameScreen.getPlayer().getInv().showInventory();break;
//               }
//
//                case "5":
//                {
//
//
//                    boolean hasRock = gameScreen.getPlayer().getInv().findInInventory("Rock");
//                    boolean trapExists = false;
//                    if(hasRock)
//                    {
//                        gameScreen.disableTrap();
//                        trapExists = gameScreen.getPlayer().getInv().remove("Rock");
//
//                        if(trapExists)
//                        {
//                            System.out.println("Good job! You have successfully disabled a deadly trap.");
//
//                        }
//                        else
//                        {
//                            System.out.println("There were no traps found. However, you have wasted your rock.");
//                        }
//
//                    }
//                    else
//                    {
//                        System.out.println("You don't have enough rocks to perform this action.");
//                    }
//                    break;
//                }
//
//                case "0": System.out.println("Thank you for playing! See you soon."); System.exit(1);
//                default: System.out.println("Your input is invalid! Please enter an integer from 0 to 5.");
//            }
//
//
//        } while(!(input.equals("0")));
//    }
//
//    /**
//     * Saves the game progress
//     */
//    public void saveGame()
//    {
//
//    }
//
//    /**
//     * Loads the game file
//     */
//    public void loadGame()
//    {
//
//    }
//
//    /**
//     * Slowly types the text to give it
//     * a game-like feeling.
//     *
//     * @param text - text to type
//     */
//    public void typeSlow(String text)
//    {
//        String[] txt = text.split("");
//
//        for (String aTxt : txt)
//        {
//            System.out.print(aTxt);
//            sleepMe(7);
//        }
//
//        System.out.println();
//        Scanner scanner = new Scanner(System.in);
//        scanner.nextLine();
//    }
//
//    /**
//     * Slowly types the text to give it a
//     * game - like feeling.
//     *
//     * This method is slower than typeSlow method.
//     *
//     * @param text - text to type
//     */
//    public void typeSlowest(String text)
//    {
//        String[] txt = text.split("");
//
//        for (String aTxt : txt)
//        {
//            System.out.print(aTxt);
//            sleepMe(15);
//        }
//
//        System.out.println();
//    }
//
//    /**
//     * Sleeps a thread.
//     * @param time
//     */
//    public void sleepMe(int time)
//    {
//        try
//        {
//            Thread.sleep(time);
//        }
//        catch (InterruptedException e)
//        {
//            e.printStackTrace();
//        }
//    }
}
