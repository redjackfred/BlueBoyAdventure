package tile_interactive;

import Entity.Entity;
import main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class InteractiveTile extends Entity {
    GamePanel gp;
    public boolean destructible = false;

    public InteractiveTile(GamePanel gp, int col, int row){
        super(gp);
        this.gp = gp;
    }

    public void playSE(){}
    public InteractiveTile getDestroyedForm(){
        InteractiveTile tile = null;
        return tile;
    }

    public boolean isCorrectItem(Entity entity){
        boolean isCorrectItem = false;

        return isCorrectItem;
    }
    public void update(){
        if(invincible){
            invincibleCounter++;
            if(invincibleCounter > 20){
                invincibleCounter = 0;
                invincible = false;
            }
        }
    }

//    public void draw(Graphics2D g2){
//        int screenX = worldX - gp.player.worldX + gp.player.screenX;
//        int screenY = worldY - gp.player.worldY + gp.player.screenY;
//
//        if(screenX >= -gp.tileSize && screenX < gp.getScreenWidth() &&
//                screenY >= -gp.tileSize && screenY < gp.getScreenHeight()) {
//            g2.drawImage(image, screenX, screenY, null);
//        }
//    }
}
