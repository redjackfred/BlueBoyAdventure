package object;

import main.GamePanel;
import main.UtilityTool;
import org.w3c.dom.css.Rect;

import java.awt.*;
import java.awt.image.BufferedImage;

public class SuperObject {
    public BufferedImage image, image2, image3;
    public String name;
    public boolean collision = false;
    public int worldX, worldY;
    public Rectangle solidArea = new Rectangle(0, 0, 48, 48);
    public int solidAreaDefaultX = 0;
    public int solidAreaDefaultY = 0;
    UtilityTool uTool = new UtilityTool();

    public void draw(Graphics2D g2, GamePanel gamePanel){
        int screenX = worldX - gamePanel.player.worldX + gamePanel.player.screenX;
        int screenY = worldY - gamePanel.player.worldY + gamePanel.player.screenY;

        if(screenX >= -gamePanel.tileSize && screenX < gamePanel.getScreenWidth() &&
                screenY >= -gamePanel.tileSize && screenY < gamePanel.getScreenHeight()) {
            g2.drawImage(image, screenX, screenY, null);
        }
    }
}
