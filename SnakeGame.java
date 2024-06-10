import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

public class SnakeGame extends JFrame {
    private final int WIDTH = 800;
    private final int HEIGHT = 600;
    private final int UNIT_SIZE = 20;
    private final int GAME_UNITS = (WIDTH * HEIGHT) / UNIT_SIZE;
    private final int DELAY = 100;
    private ArrayList<Point> snake;
    private Point food;
    private char direction = 'R';
    private boolean running = false;
    private Timer timer;

    public SnakeGame() {
        setTitle("Snake Game");
        setSize(WIDTH, HEIGHT);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        snake = new ArrayList<>();
        snake.add(new Point(UNIT_SIZE, UNIT_SIZE));
        spawnFood();

        timer = new Timer(DELAY, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (running) {
                    move();
                    checkFoodCollision();
                    checkWallCollision();
                    checkSelfCollision();
                    repaint();
                }
            }
        });
        timer.start();

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        if (direction != 'D') direction = 'U';
                        break;
                    case KeyEvent.VK_DOWN:
                        if (direction != 'U') direction = 'D';
                        break;
                    case KeyEvent.VK_LEFT:
                        if (direction != 'R') direction = 'L';
                        break;
                    case KeyEvent.VK_RIGHT:
                        if (direction != 'L') direction = 'R';
                        break;
                }
            }
        });
    }

    private void spawnFood() {
        int x = (int) (Math.random() * (WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        int y = (int) (Math.random() * (HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
        food = new Point(x, y);
    }

    private void move() {
        for (int i = snake.size() - 1; i > 0; i--) {
            snake.set(i, snake.get(i - 1));
        }
        switch (direction) {
            case 'U':
                snake.get(0).y -= UNIT_SIZE;
                break;
            case 'D':
                snake.get(0).y += UNIT_SIZE;
                break;
            case 'L':
                snake.get(0).x -= UNIT_SIZE;
                break;
            case 'R':
                snake.get(0).x += UNIT_SIZE;
                break;
        }
    }

    private void checkFoodCollision() {
        if (snake.get(0).equals(food)) {
            snake.add(new Point(food));
            spawnFood();
        }
    }

    private void checkWallCollision() {
        if (snake.get(0).x < 0 || snake.get(0).x >= WIDTH || snake.get(0).y < 0 || snake.get(0).y >= HEIGHT) {
            gameOver();
        }
    }

    private void checkSelfCollision() {
        for (int i = 1; i < snake.size(); i++) {
            if (snake.get(0).equals(snake.get(i))) {
                gameOver();
            }
        }
    }

    private void gameOver() {
        running = false;
        timer.stop();
        JOptionPane.showMessageDialog(this, "Game Over!", "Snake Game", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        // Draw background
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        // Draw food
        g.setColor(Color.RED);
        g.fillRect(food.x, food.y, UNIT_SIZE, UNIT_SIZE);

        // Draw snake
        for (Point point : snake) {
            g.setColor(Color.GREEN);
            g.fillRect(point.x, point.y, UNIT_SIZE, UNIT_SIZE);
            g.setColor(Color.BLACK);
            g.drawRect(point.x, point.y, UNIT_SIZE, UNIT_SIZE);
        }
    }

    public static void main(String[] args) {
        new SnakeGame();
    }
}
