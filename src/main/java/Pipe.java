import java.awt.*;
import java.util.Random;

public class Pipe {
    private int x; // X-coordinate of the pipe
    private int topPipeHeight; // Height of the top pipe
    private int gapHeight; // Gap between top and bottom pipes
    private int width; // Width of the pipe
    private int speed; // How fast the pipe moves to the left
    private Color color; // The pipe color
    private boolean scored;

    public Pipe(int x, int width, int gapHeight, int speed, Color color) {
        this.x = x;
        this.width = width;
        this.gapHeight = gapHeight;
        this.speed = speed;
        this.color = color;
        this.scored = false;

        Random random = new Random();
        this.topPipeHeight = random.nextInt(200) + 100; // Random height between 100 and 300
    }

    public void update() {
        x -= speed; // Move the pipe to the left
    }

    public void draw(Graphics g) {
        g.setColor(color);

        // Draw top pipe
        g.fillRect(x, 0, width, topPipeHeight);

        // Draw bottom pipe
        int bottomPipeY = topPipeHeight + gapHeight;
        int bottomPipeHeight = 600 - bottomPipeY; // Assuming window height is 600
        g.fillRect(x, bottomPipeY, width, bottomPipeHeight);
    }

    public boolean isColliding(Bird bird) {
        // Bird's rectangle
        int birdX = bird.getX();
        int birdY = bird.getY();
        int birdSize = bird.getSize();

        // Top pipe rectangle
        Rectangle topPipeRect = new Rectangle(x, 0, width, topPipeHeight);

        // Bottom pipe rectangle
        Rectangle bottomPipeRect = new Rectangle(x, topPipeHeight + gapHeight, width, 600 - (topPipeHeight + gapHeight));

        Rectangle birdRect = new Rectangle(birdX - birdSize / 2, birdY - birdSize / 2, birdSize, birdSize);

        return birdRect.intersects(topPipeRect) || birdRect.intersects(bottomPipeRect);
    }

    public int getX() {
        return x;
    }

    public int getTopPipeHeight() {
        return topPipeHeight;
    }

    public int getGapHeight() {
        return gapHeight;
    }

    public int getWidth() {
        return width;
    }

    public int getBottomPipeY() {
        return topPipeHeight + gapHeight;
    }

    public int getBottomPipeHeight() {
        return 600 - getBottomPipeY();
    }

    public boolean isScored() {
        return scored;
    }

    public void setScored(boolean scored) {
        this.scored = scored;
    }
}