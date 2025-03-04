import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class Bird {
    private int x, y;
    private int size;
    private double velocity;
    private double gravity;
    private BufferedImage image;

    public Bird(int startX, int startY) {
        this.x = startX;
        this.y = startY;
        this.size = 40; // Bird size
        this.velocity = 0;
        this.gravity = 0.5; // Adjust gravity as needed

        try {
            InputStream stream = getClass().getResourceAsStream("/burger.png");
            if (stream != null) {
                image = ImageIO.read(stream);
            } else {
                System.err.println("Image not found");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        velocity += gravity; // Apply gravity
        y += velocity;      // Update bird's y position
    }

    public void flap() {
        velocity = -10; // Give upward velocity for flap (adjust as needed)
    }

    public void draw(Graphics g) {
        if (image != null) {
            g.drawImage(image, x - size / 2, y - size / 2, size, size, null);
        } else {
            g.setColor(Color.YELLOW); // Bird color
            g.fillRect(x - size / 2, y - size / 2, size, size); // Draw bird as a rectangle
        }
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    public int getSize() {
        return size;
    }

    // You can add methods to get X, Size, etc. if needed later
}