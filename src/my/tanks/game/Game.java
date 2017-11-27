package my.tanks.game;

import my.tanks.display.Display;
import my.tanks.game.level.Level;
import my.tanks.graphics.Sprite;
import my.tanks.graphics.SpriteSheet;
import my.tanks.graphics.TextureAtlas;
import my.tanks.io.Input;
import my.tanks.utils.Time;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Game implements Runnable {

    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    public static final String TITLE = "Tanks";
    public static final int CLEAR_COLOR = 0xff000000;
    public static final int NUM_BUFFERS = 3;

    public static final float UPDATE_RATE = 60.0f;
    public static final float UPDATE_INTERVAL = Time.SECOND / UPDATE_RATE;
    public static final long IDLE_TIME = 1;
    public static final String FILE_NAME = "texture_atlas.png";

    private boolean running;
    private Thread gameThread;
    private Graphics2D graphics;

    private Input input;
    private TextureAtlas atlas;
    private Player player;
    private Level lvl;


    public Game() {
        running = false;
        Display.create(WIDTH, HEIGHT, TITLE, CLEAR_COLOR, NUM_BUFFERS);
        graphics = Display.getGraphics();
        input = new Input();
        Display.addInputListener(input);
        atlas = new TextureAtlas(FILE_NAME);
        player = new Player(300, 300, 2, 3, atlas);
        lvl = new Level(atlas);
    }

    public synchronized void start() {
        if (running) return;
        running = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    public synchronized void stop() {
        if (!running) return;

        running = false;


        try {
            gameThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        cleanUp();
    }

    private void update() {
            player.update(input);
            lvl.update();
    }

    private void render() {
        Display.clear();

        lvl.render(graphics);
        player.render(graphics);
        lvl.renderGrass(graphics);

        Display.swapBuffers();
    }

    private void cleanUp() {
        Display.destroy();
    }


    @Override
    public void run() {
        int fpsCount = 0;                     // counters
        int updateCount = 0;                  // counters
        int updateLoopCount = 0;              // counters

        long count = 0;                       // counters


        float delta = 0;
        long lastTime = Time.get();

        while (running) {
            long now = Time.get();
            long elapsedTime = now - lastTime;
            lastTime = now;

            count += elapsedTime;

            boolean render = false;
            delta += (elapsedTime / UPDATE_INTERVAL);
            while (delta > 1) {
                update();
                updateCount++;
                delta--;
                if (render) {
                    updateLoopCount++;
                } else render = true;
            }

            if (render) {
                render();
                fpsCount++;
            } else {
                try {
                    Thread.sleep(IDLE_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (count >= Time.SECOND){
                Display.setTitle(TITLE + " || Fps: " + fpsCount + " | Upd: " + updateCount + " | UpdL: " + updateLoopCount);
                fpsCount = 0;
                updateCount = 0;
                updateLoopCount = 0;
                count = 0;
            }
        }

    }






}
