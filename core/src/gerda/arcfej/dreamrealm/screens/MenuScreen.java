package gerda.arcfej.dreamrealm.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.utils.Align;
import gerda.arcfej.dreamrealm.GameCore;

/**
 * The main class for the project Dream Realm.
 * It contains a menu with options, as well as an introduction to the game.
 *
 */
public class MenuScreen extends AbstractFixSizedScreen {

    /**
     * Default constructor of the class.
     *
     * @param game The controller of the game
     */
    public MenuScreen(GameCore game, SpriteBatch batch) {
        super(game, batch);

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
                game.startGame();
            }
        });
        menu.addActor(newGame);

        TextButton loadGame = new TextButton("Load Game", game.skin);
        loadGame.setX(menu.getWidth() / 2 - loadGame.getWidth() / 2);
        loadGame.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                game.loadGame();
            }
        });
        menu.addActor(loadGame);

        TextButton howToPlay = new TextButton("How To Play", game.skin);
        howToPlay.setX(menu.getWidth() / 2 - howToPlay.getWidth() / 2);
        howToPlay.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                game.showHowToPlay();
            }
        });
        menu.addActor(howToPlay);

        TextButton exit = new TextButton("Exit", game.skin);
        exit.setX(menu.getWidth() / 2 - exit.getWidth() / 2);
        exit.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.exit();
            }
        });
        menu.addActor(exit);
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

    /*
    public void introduction()
    {
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

            input = s.nextLine();

            switch(input)
            {
                case "1":
                {
                    typeSlow("\nYou open your eyes. It is not your room anymore. You are outside, surrounded by hedges and a complete darkness.\n" +
                            "The only source of light is an oil lamp that lies in front of you. You get up and take it.\n");

                    gameScreen.displayMap();
                    gameMenu();
                } break;
                case "2": loadGame(); break;
                case "0": System.out.println("Thank you for playing! See you soon."); System.exit(1);
                default: System.out.println("Your input is invalid! Please enter an integer from 0 to 2.");
            }

        } while (!(input.equals("0")));
    }
    */
}
