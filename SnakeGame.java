import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

public class SnakeGame extends JPanel implements ActionListener {
    private static final int WIDTH = 400;
    private static final int HEIGHT = 400;
    private static final int UNIT_SIZE = 10;
    private static final int DELAY = 200;
    private ArrayList<Point> snake;
    private Point apple;
    private boolean isGameOver;
    private boolean isMovingRight;
    private boolean isMovingLeft;
    private boolean isMovingUp;
    private boolean isMovingDown;
    private Timer timer;

    public SnakeGame() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.black);
        setFocusable(true);
        addKeyListener(new GameKeyListener());
        initializeGame();
    }

    private void initializeGame() {
        snake = new ArrayList<>();
        snake.add(new Point(WIDTH / 2, HEIGHT / 2));
        generateApple();

        isGameOver = false;
        isMovingRight = true;
        isMovingLeft = false;
        isMovingUp = false;
        isMovingDown = false;

        timer = new Timer(DELAY, this);
        timer.start();
    }

    private void generateApple() {
        int maxX = (WIDTH / UNIT_SIZE) - 1;
        int maxY = (HEIGHT / UNIT_SIZE) - 1;
        Random random = new Random();
        int x = random.nextInt(maxX) * UNIT_SIZE;
        int y = random.nextInt(maxY) * UNIT_SIZE;
        apple = new Point(x, y);
    }

    private void move() {
        Point head = snake.get(0);

        if (isMovingRight) {
            head = new Point(head.x + UNIT_SIZE, head.y);
        } else if (isMovingLeft) {
            head = new Point(head.x - UNIT_SIZE, head.y);
        } else if (isMovingUp) {
            head = new Point(head.x, head.y - UNIT_SIZE);
        } else if (isMovingDown) {
            head = new Point(head.x, head.y + UNIT_SIZE);
        }

        snake.add(0, head);

        if (head.equals(apple)) {
            generateApple();
        } else {
            snake.remove(snake.size() - 1);
        }
    }

    private void checkCollision() {
        Point head = snake.get(0);

        if (head.x < 0 || head.x >= WIDTH || head.y < 0 || head.y >= HEIGHT) {
            isGameOver = true;
            return;
        }

        for (int i = 1; i < snake.size(); i++) {
            if (head.equals(snake.get(i))) {
                isGameOver = true;
                return;
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!isGameOver) {
            move();
            checkCollision();
            repaint();
        }
    }

    private class GameKeyListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();

            if (key == KeyEvent.VK_RIGHT && !isMovingLeft) {
                isMovingRight = true;
                isMovingUp = false;
                isMovingDown = false;
            } else if (key == KeyEvent.VK_LEFT && !isMovingRight) {
                isMovingLeft = true;
                isMovingUp = false;
                isMovingDown = false;
            } else if (key == KeyEvent.VK_UP && !isMovingDown) {
                isMovingUp = true;
                isMovingRight = false;
                isMovingLeft = false;
            } else if (key == KeyEvent.VK_DOWN && !isMovingUp) {
                isMovingDown = true;
                isMovingRight = false;
                isMovingLeft = false;
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (!isGameOver) {
            // Draw the snake
            g.setColor(Color.green);
            for (Point point : snake) {
                g.fillRect(point.x, point.y, UNIT_SIZE, UNIT_SIZE);
            }

            // Draw the apple
            g.setColor(Color.red);
            g.fillRect(apple.x, apple.y, UNIT_SIZE, UNIT_SIZE);
        } else {
            // Game over message
            g.setColor(Color.white);
            g.setFont(new Font("SansSerif", Font.BOLD, 24));
            g.drawString("Game Over", WIDTH / 2 - 75, HEIGHT / 2 - 12);
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake Game");
        SnakeGame game = new SnakeGame();
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
    
}