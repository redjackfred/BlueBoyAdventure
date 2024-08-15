package Entity;
import main.GamePanel;
import main.KeyHandler;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Player extends Entity{
    KeyHandler keyHandler;

    public final int screenX;
    public final int screenY;
    int standCounter = 0;

    public Player(GamePanel gamePanel, KeyHandler keyHandler){
        super(gamePanel);
        this.keyHandler = keyHandler;

        screenX = gamePanel.getScreenWidth() / 2 - gamePanel.tileSize / 2;
        screenY = gamePanel.getScreenHeight() / 2 - gamePanel.tileSize / 2;

        solidArea = new Rectangle();
        solidArea.x = 8;
        solidArea.y = 16;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 32;
        solidArea.height = 32;

        setDefaultValues();
        getPlayerImage();
    }

    public void setDefaultValues(){
        // Starting position
        worldX = gp.tileSize * 23 ;
        worldY = gp.tileSize * 21;
        speed = 4;
        direction = "down";

        // Player status
        maxLife = 6;
        life = maxLife;
    }

    public void getPlayerImage(){
        up1 = setUp("player/boy_up_1");
        up2 = setUp("player/boy_up_2");
        down1 = setUp("player/boy_down_1");
        down2 = setUp("player/boy_down_2");
        left1 = setUp("player/boy_left_1");
        left2 = setUp("player/boy_left_2");
        right1 = setUp("player/boy_right_1");
        right2 = setUp("player/boy_right_2");
    }
    public void update(){
        if(keyHandler.upPressed || keyHandler.downPressed || keyHandler.leftPressed || keyHandler.rightPressed) {
            if (keyHandler.upPressed) {
                direction = "up";
            }
            if (keyHandler.downPressed) {
                direction = "down";
            }
            if (keyHandler.leftPressed) {
                direction = "left";
            }
            if (keyHandler.rightPressed) {
                direction = "right";
            }

            // Check Tile Collision
            collisionOn = false;
            gp.collisionChecker.checkTile(this);

            // Check object collision
            int objIndex = gp.collisionChecker.checkObject(this, true);
            pickUpObject(objIndex);

            // Check NPC collision
            int npcIndex = gp.collisionChecker.checkEntity(this, gp.npc);
            interactNPC(npcIndex);

            // If collision is false, player can move
            if(!collisionOn){
                switch (direction){
                    case "up":
                        worldY -= speed;
                        break;
                    case "down":
                        worldY += speed;
                        break;
                    case "left":
                        worldX -= speed;
                        break;
                    case "right":
                        worldX += speed;
                        break;
                }
            }

            spriteCounter++;
            if (spriteCounter > 15) {
                if (spriteNum == 1) {
                    spriteNum = 2;
                } else if (spriteNum == 2) {
                    spriteNum = 1;
                }
                spriteCounter = 0;
            }
        }else{
            standCounter++;

            if(standCounter == 20) {
                spriteNum = 1;
                standCounter = 0;
            }
        }
    }

    public void pickUpObject(int objIndex){
        if(objIndex != -1) {

        }
    }

    public void interactNPC(int index){
        // When Player hits a NPC
        if(index != -1 && gp.keyHandler.enterPressed) {
            gp.gameState = gp.dialogState;
            gp.npc[index].speak();
            gp.keyHandler.enterPressed = false;
        }
    }
    public void draw(Graphics2D g2){
        BufferedImage image = null;
        switch (direction){
            case "up":
                if(spriteNum == 1) {
                    image = up1;
                }else if(spriteNum == 2){
                    image = up2;
                }
                break;
            case "down":
                if(spriteNum == 1) {
                    image = down1;
                }else if(spriteNum == 2){
                    image = down2;
                }
                break;
            case "left":
                if(spriteNum == 1) {
                    image = left1;
                }else if(spriteNum == 2){
                    image = left2;
                }
                break;
            case "right":
                if(spriteNum == 1) {
                    image = right1;
                }else if(spriteNum == 2){
                    image = right2;
                }
                break;
        }
        g2.drawImage(image, screenX, screenY, null);
        // Collision box
//        g2.setColor(Color.red);
//        g2.drawRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);
    }
}
