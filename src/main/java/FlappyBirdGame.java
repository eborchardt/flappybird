import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;


public class FlappyBirdGame extends JFrame {

    public FlappyBirdGame() {
        setTitle("Flappy Bird"); // Set the window title
        setSize(800, 600);      // Set window size (width, height)
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close on window close
        setResizable(false);       // Prevent resizing the window

        GamePanel gamePanel = new GamePanel(); // Create a panel to draw on
        add(gamePanel);                      // Add the panel to the frame

        setVisible(true);                      // Make the window visible
    }

    public static void main(String[] args) {
        new FlappyBirdGame(); // Create and run the game
    }
}

class GamePanel extends JPanel implements KeyListener, ActionListener {

    private Bird bird;
    private Timer gameTimer;
    private final int DELAY = 20;

    private List<Pipe> pipes;
    private int pipeSpawnInterval = 100;
    private int pipeSpawnCounter = 0;

    private boolean gameOver;

    private int score;

    public GamePanel() {
        setBackground(Color.CYAN); // Background color
        bird = new Bird(100, 300); // Create bird at starting position
        setFocusable(true); // Panel needs focus to receive key events
        addKeyListener(this); // Listen for key presses

        gameTimer = new Timer(DELAY, this);
        gameTimer.start();
        pipes = new ArrayList<>();
        generatePipe();
        gameOver = false;
    }

    private void checkCollisions() {
        for (Pipe pipe : pipes) {
            if (pipe.isColliding(bird)) {
                gameOver = true;
                gameTimer.stop();
            }
            if (bird.getY() <= 0 || bird.getY() >= getHeight()) {
                gameOver = true;
                gameTimer.stop();
            }
        }
    }

    private void generatePipe() {
        int pipeWidth = 50;
        int gapHeight = 200;
        int pipeSpeed = 5;
        Color pipeColor = Color.GREEN;
        int newPipeX;
        if (getWidth() == 0) {
            newPipeX = 800 + pipeWidth;
        } else {
            newPipeX = getWidth() + pipeWidth;
        }

        pipes.add(new Pipe(newPipeX, pipeWidth, gapHeight, pipeSpeed, pipeColor));
    }

    private void updatePipes()  {
        for (Pipe pipe : pipes) {
            pipe.update();
            if (!pipe.isScored() && pipe.getX() + pipe.getWidth() < bird.getX() - bird.getSize() / 2) {
                score++;
                pipe.setScored(true);
            }
        }
        pipes.removeIf(pipe -> pipe.getX() + pipe.getWidth() < 0);

        pipeSpawnCounter++;
        if (pipeSpawnCounter >= pipeSpawnInterval) {
            generatePipe();
            pipeSpawnCounter = 0;
        }

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(getBackground()); // Fill background color (important for clearing)
        g.fillRect(0, 0, getWidth(), getHeight());

        for (Pipe pipe : pipes) {
            pipe.draw(g);
        }
        bird.draw(g); // Draw the bird

        showScore(g);

        if (gameOver) {
            g.setColor(Color.RED);
            g.setFont(new Font("ComicSans", Font.BOLD, 48));
            String message = "Game Over!";
            FontMetrics metrics = getFontMetrics(g.getFont());
            int x = (getWidth() - metrics.stringWidth(message)) / 2;
            int y = getHeight() / 2;
            g.drawString(message, x, y);
        }
    }

    private void showScore(Graphics g) {
        g.setColor(Color.RED);
        g.setFont(new Font("ComicSans", Font.BOLD, 48));
        String message = "Score: " + score;
        FontMetrics metrics = getFontMetrics(g.getFont());
        int x = (getWidth() - metrics.stringWidth(message)) / 2;
        int y = 50;
        g.drawString(message, x, y);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        bird.update();
        updatePipes();
        repaint();
        checkCollisions();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            if (!gameOver) {
                bird.flap(); // Flap when space is pressed
            } else {
                resetGame();
            }
        }
        repaint();
    }

    private void resetGame() {
        score = 0;
        pipes = new ArrayList<>();
        gameOver = false;
        bird = new Bird(100, 300);
        pipeSpawnCounter = 0;

        pipes.clear();

        if (gameTimer != null) {
            gameTimer.stop();
        }
        gameTimer = new Timer(DELAY, this);
        gameTimer.start();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Currently unused
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Currently unused
    }
}
