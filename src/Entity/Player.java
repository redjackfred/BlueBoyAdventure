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

        attackArea.width = 36;
        attackArea.height = 36;


        setDefaultValues();
        getPlayerImage();
        getPlayerAttackImage();
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
        up1 = setUp("player/boy_up_1", gp.tileSize, gp.tileSize);
        up2 = setUp("player/boy_up_2", gp.tileSize, gp.tileSize);
        down1 = setUp("player/boy_down_1", gp.tileSize, gp.tileSize);
        down2 = setUp("player/boy_down_2", gp.tileSize, gp.tileSize);
        left1 = setUp("player/boy_left_1", gp.tileSize, gp.tileSize);
        left2 = setUp("player/boy_left_2", gp.tileSize, gp.tileSize);
        right1 = setUp("player/boy_right_1", gp.tileSize, gp.tileSize);
        right2 = setUp("player/boy_right_2", gp.tileSize, gp.tileSize);
    }

    public void getPlayerAttackImage(){
        attackUp1 = setUp("player/boy_attack_up_1", gp.tileSize, gp.tileSize * 2);
        attackUp2 = setUp("player/boy_attack_up_2", gp.tileSize, gp.tileSize * 2);
        attackDown1 = setUp("player/boy_attack_down_1", gp.tileSize, gp.tileSize * 2);
        attackDown2 = setUp("player/boy_attack_down_2", gp.tileSize, gp.tileSize * 2);
        attackLeft1 = setUp("player/boy_attack_left_1", gp.tileSize * 2, gp.tileSize);
        attackLeft2 = setUp("player/boy_attack_left_2", gp.tileSize * 2, gp.tileSize);
        attackRight1 = setUp("player/boy_attack_right_1", gp.tileSize * 2, gp.tileSize);
        attackRight2 = setUp("player/boy_attack_right_2", gp.tileSize * 2, gp.tileSize);
    }

    public void update(){
        if(attacking){
          attacking();
        }
        else if(keyHandler.upPressed || keyHandler.downPressed || keyHandler.leftPressed || keyHandler.rightPressed || keyHandler.enterPressed) {
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

            // Check Monster collision
            int monsterIndex = gp.collisionChecker.checkEntity(this, gp.monster);
            contactMonster(monsterIndex);

            // Check Event
            gp.eventHandler.checkEvent();

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

        // This needs to be outside of key if statement
        if(invincible){
            invincibleCounter++;
            if(invincibleCounter > 60){
                invincibleCounter = 0;
                invincible = false;
            }
        }
    }

    private void contactMonster(int monsterIndex) {
        if(monsterIndex != -1){
            if(!invincible) {
                gp.playSE(6);
                life -= 1;
                invincible = true;
            }
        }
    }

    public void attacking(){
        spriteCounter++;
        if(spriteCounter <= 5){
            spriteNum = 1;
        }else if(spriteCounter <= 25){
            spriteNum = 2;

            // Save the current worldX, worldY, solidArea
            int currentWorldX = worldX;
            int currentWorldY = worldY;
            int solidAreaWidth = solidArea.width;
            int solidAreaHeight = solidArea.height;

            // Adjust player's worldX/Y for the attackArea
            switch(direction){
                case "up": worldY -= attackArea.height; break;
                case "down": worldY += attackArea.height; break;
                case "left": worldX -= attackArea.width; break;
                case "right": worldX += attackArea.width; break;
            }

            // attackArea becomes solidArea
            solidArea.width = attackArea.width;
            solidArea.height = attackArea.height;

            // Check monster collision with the updated worldX, worldY and solidArea
            int monsterIndex = gp.collisionChecker.checkEntity(this, gp.monster);
            damageMonster(monsterIndex);

            // After checking collision, restore the original data
            worldX = currentWorldX;
            worldY = currentWorldY;
            solidArea.width = solidAreaWidth;
            solidArea.height = solidAreaHeight;

        }else{
            spriteNum = 1;
            spriteCounter = 0;
            attacking = false;
        }
    }

    private void damageMonster(int monsterIndex) {
        if(monsterIndex != -1){
           if(!gp.monster[monsterIndex].invincible){
               gp.playSE(5);
               gp.monster[monsterIndex].life -= 1;
               gp.monster[monsterIndex].invincible = true;
               gp.monster[monsterIndex].damageReaction();

               if(gp.monster[monsterIndex].life <= 0){
                   gp.monster[monsterIndex].dying = true;
               }
           }
        }
    }

    public void pickUpObject(int objIndex){
        if(objIndex != -1) {

        }
    }

    public void interactNPC(int index){
        // When Player hits a NPC
        if(gp.keyHandler.enterPressed) {
            if (index != -1) {
                gp.gameState = gp.dialogState;
                gp.npc[index].speak();
                gp.keyHandler.enterPressed = false;
            }else{
                attacking = true;
            }
        }
    }
    public void draw(Graphics2D g2){
        BufferedImage image = null;
        int tempScreenX = screenX;
        int tempScreenY = screenY;
        switch (direction){
            case "up":
                if(!attacking){
                    if(spriteNum == 1) {
                        image = up1;
                    }else if(spriteNum == 2){
                        image = up2;
                    }
                }else{
                    tempScreenY = screenY - gp.tileSize;
                    if(spriteNum == 1) {
                        image = attackUp1;
                    }else if(spriteNum == 2){
                        image = attackUp2;
                    }
                }
                break;
            case "down":
                if(!attacking){
                    if(spriteNum == 1) {
                        image = down1;
                    }else if(spriteNum == 2){
                        image = down2;
                    }
                }else{
                    if(spriteNum == 1) {
                        image = attackDown1;
                    }else if(spriteNum == 2){
                        image = attackDown2;
                    }
                }
                break;
            case "left":
                if(!attacking){
                    if(spriteNum == 1) {
                        image = left1;
                    }else if(spriteNum == 2){
                        image = left2;
                    }
                }else{
                    tempScreenX = screenX - gp.tileSize;
                    if(spriteNum == 1) {
                        image = attackLeft1;
                    }else if(spriteNum == 2){
                        image = attackLeft2;
                    }
                }
                break;
            case "right":
                if(!attacking){
                    if(spriteNum == 1) {
                        image = right1;
                    }else if(spriteNum == 2){
                        image = right2;
                    }
                }else{
                    if(spriteNum == 1) {
                        image = attackRight1;
                    }else if(spriteNum == 2){
                        image = attackRight2;
                    }
                }
                break;
        }

        if(invincible){
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
        }

        g2.drawImage(image, tempScreenX, tempScreenY, null);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

        // Collision box
//        g2.setColor(Color.red);
//        g2.drawRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);
    }
}
