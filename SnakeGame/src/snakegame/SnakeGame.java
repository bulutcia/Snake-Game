package snakegame;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

public class SnakeGame extends JFrame implements ActionListener, KeyListener {
    private static final int GRID_SIZE = 20;
    private static final int CELL_SIZE = 30;

    private int snakeLength = 3;
    private int[] snakeX, snakeY;
    private int direction; // 0: sağ, 1: sol, 2: yukarı, 3: aşağı

    private int foodX, foodY;

    private Timer timer;

    public SnakeGame() {
        setTitle("Snake Game");
        setSize(GRID_SIZE * CELL_SIZE, GRID_SIZE * CELL_SIZE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());
        addKeyListener(this);

        snakeX = new int[GRID_SIZE * GRID_SIZE];
        snakeY = new int[GRID_SIZE * GRID_SIZE];

        direction = 0;

        spawnSnake();
        spawnFood();

        timer = new Timer(200, this);
        timer.start();
    }

    private void spawnSnake() {
        for (int i = 0; i < snakeLength; i++) {
            snakeX[i] = GRID_SIZE / 2 - i;
            snakeY[i] = GRID_SIZE / 2;
        }
    }

    private void spawnFood() {
        Random rand = new Random();
        foodX = rand.nextInt(GRID_SIZE);
        foodY = rand.nextInt(GRID_SIZE);
    }

    private void move() {
        for (int i = snakeLength - 1; i > 0; i--) {
            snakeX[i] = snakeX[i - 1];
            snakeY[i] = snakeY[i - 1];
        }

        switch (direction) {
            case 0:
                snakeX[0]++;
                break;
            case 1:
                snakeX[0]--;
                break;
            case 2:
                snakeY[0]--;
                break;
            case 3:
                snakeY[0]++;
                break;
        }

        checkCollision();
        checkFood();
    }

    private void checkCollision() {
        if (snakeX[0] < 0 || snakeX[0] >= GRID_SIZE || snakeY[0] < 0 || snakeY[0] >= GRID_SIZE) {
            gameOver();
        }

        for (int i = 1; i < snakeLength; i++) {
            if (snakeX[0] == snakeX[i] && snakeY[0] == snakeY[i]) {
                gameOver();
            }
        }
    }

    private void checkFood() {
        if (snakeX[0] == foodX && snakeY[0] == foodY) {
            snakeLength++;
            spawnFood();
        }
    }

    private void gameOver() {
        timer.stop();
        JOptionPane.showMessageDialog(this, "Game Over! Your score: " + (snakeLength - 3));
        System.exit(0);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        g.setColor(Color.RED);
        g.fillRect(foodX * CELL_SIZE, foodY * CELL_SIZE, CELL_SIZE, CELL_SIZE);

        g.setColor(Color.GREEN);
        for (int i = 0; i < snakeLength; i++) {
            g.fillRect(snakeX[i] * CELL_SIZE, snakeY[i] * CELL_SIZE, CELL_SIZE, CELL_SIZE);
        }

        Toolkit.getDefaultToolkit().sync();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if ((key == KeyEvent.VK_RIGHT) && direction != 1) {
            direction = 0;
        } else if ((key == KeyEvent.VK_LEFT) && direction != 0) {
            direction = 1;
        } else if ((key == KeyEvent.VK_UP) && direction != 3) {
            direction = 2;
        } else if ((key == KeyEvent.VK_DOWN) && direction != 2) {
            direction = 3;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SnakeGame game = new SnakeGame();
            game.setVisible(true);
        });
    }
}
