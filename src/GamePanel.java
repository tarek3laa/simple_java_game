import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.lang.Math.abs;


public class GamePanel extends JPanel implements Runnable, KeyListener {


    private int width, height; // width and height of panel
    private Thread thread;
    private boolean running = false;
    private Random random;
    List<Position<Integer, Integer>> minePosition; // list of mine position
    Shape shape, corner1, corner2, corner3, corner4;


    enum Direction {NON, LEFT, RIGHT, UP, DOWN}

    private Direction directions = Direction.NON;
    int x_axis = 200, y_axis = 200; // position of shape

    public GamePanel(int width, int height) {

        this.height = height;
        this.width = width;
        minePosition = new ArrayList<>();
        random = new Random();
        setFocusable(true);
        addKeyListener(this);
        setPreferredSize(new Dimension(width, height)); // set size of panel
        start(); // start game
    }


    /**
     * this method called while game is running
     *
     * @param graphics
     */
    @Override
    public void paint(Graphics graphics) {
        graphics.clearRect(0, 0, width, height); // clear screen
        graphics.setColor(Color.white); // set color to white
        graphics.fillRect(0, 0, width, height); // fill screen with white color

        graphics.setColor(Color.LIGHT_GRAY); // change color LIGHT_GRAY
        for (int i = 0; i < width; i++) { // draw vertical lines
            graphics.drawLine(i * 30, 0, i * 30, height);
        }
        for (int i = 0; i < height / 10; i++) { // draw horizontal lines
            graphics.drawLine(0, i * 30, width, i * 30);
        }

        //draw shape
        shape.draw(graphics, Color.red);
        //draw corners
        corner1.draw(graphics, Color.GREEN);
        corner2.draw(graphics, Color.GREEN);
        corner3.draw(graphics, Color.GREEN);
        corner4.draw(graphics, Color.GREEN);
        if (!running) // if game not running
            for (Position<Integer, Integer> position : minePosition) { // iterate over mines positions
                Shape shape = new Shape(position, 10, 10); // crate new shape with width and height 10px
                shape.draw(graphics, Color.BLACK); // draw this shape
            }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    /**
     * called every time when user clicked any key
     *
     * @param e KeyEvent
     */
    @Override
    public void keyPressed(KeyEvent e) {

        switch (e.getKeyCode()) { // get key code
            case KeyEvent.VK_W:  // w key
                directions = Direction.UP; // set directions to UP
                break;

            case KeyEvent.VK_S: // s key
                directions = Direction.DOWN; // set directions to Down
                break;
            case KeyEvent.VK_D: // d key
                directions = Direction.RIGHT; // set directions to right
                break;
            case KeyEvent.VK_A:// a key
                directions = Direction.LEFT; // set directions to left
                break;

        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    // called while thread is running
    @Override
    public void run() {

        while (running) {
            play();
            repaint();
        }
    }

    /**
     * check witch key is clicked
     * ,redraw the shape with new position
     * and check if the position of this shape equals mines positions
     */

    private void play() {

        switch (directions) {
            case UP: // if directions equals UP decrease y axis by 1
                if (y_axis > 0) {
                    y_axis -= 10;
                }
                break;
            case DOWN: // if directions equals DOWN increase y axis by 1
                if (y_axis < height)
                    y_axis += 10;
                break;
            case LEFT: //if directions equals LEFT decrease x axis by 1
                if (x_axis > 0)
                    x_axis -= 10;

                break;
            case RIGHT: //if directions equals RIGHT increase x axis by 1
                if (x_axis < width)
                    x_axis += 10;


        }

        directions = Direction.NON; // reassign non to directions
        for (Position<Integer, Integer> position : minePosition) { // iterate over mine positions
            if (abs(position.getX_axis() - x_axis) <= 4 && abs(position.getY_axis() - y_axis) <= 4) { // check if shape's position equals any mine's positions then stop the game

                stop();
            }

        }
        if ((x_axis <= 4 && y_axis <= 4) || (x_axis >= 380 && x_axis <= 400 && y_axis <= 5) || (x_axis <= 5 && y_axis >= 380 && y_axis <= 400) || (x_axis >= 380 && y_axis >= 380)) { // check if shape's position equals any corner's position then stop game

            stop();
        }

        shape = new Shape(new Position(x_axis, y_axis), 10, 10);
        corner1 = new Shape(new Position(0, 0), 7, 7);
        corner2 = new Shape(new Position(385, 0), 7, 7);
        corner3 = new Shape(new Position(0, 385), 7, 7);
        corner4 = new Shape(new Position(385, 385), 7, 7);

    }

    /**
     * start game
     */
    private void start() {
        thread = new Thread(this);
        running = true;
        setMinePosition();
        thread.start(); // start the thread
    }

    /**
     * set the mine position
     */
    private void setMinePosition() {
        for (int i = 0; i < 40; i++) { // create 40 mines with random position
            int x = 1 + random.nextInt(350);
            int y = 1 + random.nextInt(350);
            minePosition.add(new Position<>(x, y));
        }
    }

    /**
     * stop game
     */

    public void stop() {
        running = false;
        try {
            thread.join(); // stop thread
        } catch (InterruptedException e) {

            e.printStackTrace();
        }
    }

    public class Position<first, second> {
        private first x_axis;
        private second y_axis;

        public Position(first x_axis, second y_axis) {
            this.x_axis = x_axis;
            this.y_axis = y_axis;
        }

        public first getX_axis() {
            return x_axis;
        }

        public second getY_axis() {
            return y_axis;
        }

    }

}
