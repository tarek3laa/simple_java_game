import java.awt.*;

public class Shape {

    private int Xax, Yax, width, height;

    public Shape(GamePanel.Position position, int width, int height) {
        this.Xax = (int) position.getX_axis();
        this.Yax = (int) position.getY_axis();
        this.width = width;
        this.height = height;
    }

    public void draw(Graphics g, Color color) { // draw shape
        g.setColor(color);
        g.fillRect(Xax, Yax, 2 * width, 2 * height);
    }
}
