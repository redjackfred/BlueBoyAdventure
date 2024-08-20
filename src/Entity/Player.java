package Entity;
import main.GamePanel;
import main.KeyHandler;
import main.UtilityTool;
import object.OBJ_FireBall;
import object.OBJ_Key;
import object.OBJ_Shield_Wood;
import object.OBJ_Sword_Normal;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class Player extends Entity{
    KeyHandler keyHandler;

    public final int screenX;
    public final int screenY;
    int standCounter = 0;
    public boolean attackCanceled = false;

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
        getPlayerAttackImage();
        setItems();
    }

    public void setDefaultValues(){
        // Starting position
        gp.currentMap = 0;
        worldX = gp.tileSize * 23 ;
        worldY = gp.tileSize * 21;
        speed = 4;
        direction = "down";

        // Player status
        level = 1;
        maxLife = 6;
        life = maxLife;
        maxMana = 4;
        mana = maxMana;
        ammo = 10;
        strength = 1; // The more strength he has, the more damage he gives.
        dexterity = 1; // The more dexterity he has, the less he receives.
        exp = 0;
        nextLevelExp = 5;
        coin = 50;
        currentWeapon = new OBJ_Sword_Normal(gp);
        currentShield = new OBJ_Shield_Wood(gp);
        projectile = new OBJ_FireBall(gp);
        attack = getAttack(); // The total attack value is decided by strength and weapon
        defense = getDefense();  // The total defense value is decided by dexterity and shield
    }

    public void setItems(){
        inventory.clear();
        inventory.add(currentWeapon);
        inventory.add(currentShield);
        inventory.add(new OBJ_Key(gp));
    }

    private int getAttack() {
        return attack = strength * currentWeapon.attackValue;
    }

    private int getDefense() {
        attackArea = currentWeapon.attackArea;
        return defense = dexterity * currentWeapon.defenseValue;
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
        if(currentWeapon.type == type_sword) {
            attackUp1 = setUp("player/boy_attack_up_1", gp.tileSize, gp.tileSize * 2);
            attackUp2 = setUp("player/boy_attack_up_2", gp.tileSize, gp.tileSize * 2);
            attackDown1 = setUp("player/boy_attack_down_1", gp.tileSize, gp.tileSize * 2);
            attackDown2 = setUp("player/boy_attack_down_2", gp.tileSize, gp.tileSize * 2);
            attackLeft1 = setUp("player/boy_attack_left_1", gp.tileSize * 2, gp.tileSize);
            attackLeft2 = setUp("player/boy_attack_left_2", gp.tileSize * 2, gp.tileSize);
            attackRight1 = setUp("player/boy_attack_right_1", gp.tileSize * 2, gp.tileSize);
            attackRight2 = setUp("player/boy_attack_right_2", gp.tileSize * 2, gp.tileSize);
        }else if(currentWeapon.type == type_axe) {
            attackUp1 = setUp("player/boy_axe_up_1", gp.tileSize, gp.tileSize * 2);
            attackUp2 = setUp("player/boy_axe_up_2", gp.tileSize, gp.tileSize * 2);
            attackDown1 = setUp("player/boy_axe_down_1", gp.tileSize, gp.tileSize * 2);
            attackDown2 = setUp("player/boy_axe_down_2", gp.tileSize, gp.tileSize * 2);
            attackLeft1 = setUp("player/boy_axe_left_1", gp.tileSize * 2, gp.tileSize);
            attackLeft2 = setUp("player/boy_axe_left_2", gp.tileSize * 2, gp.tileSize);
            attackRight1 = setUp("player/boy_axe_right_1", gp.tileSize * 2, gp.tileSize);
            attackRight2 = setUp("player/boy_axe_right_2", gp.tileSize * 2, gp.tileSize);
        }

    }

    public void setDefaultPositions(){
        worldX = gp.tileSize * 23;
        worldY = gp.tileSize * 21;
        direction = "down";
    }

    public void restoreLifeAndMana(){
        life = maxLife;
        mana = maxMana;
        invincible = false;
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

            // Check interactive tile collision
            int iTileIndex = gp.collisionChecker.checkEntity(this, gp.iTile);

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

            if(keyHandler.enterPressed && !attackCanceled){
                attacking = true;
                spriteCounter = 0;
            }

            attackCanceled = false;
            gp.keyHandler.enterPressed = false;

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

        if(gp.keyHandler.shotKeyPressed && !projectile.alive && shotAvailableCounter == 30 && projectile.haveResource(this)){
            // Set default coordinates, direction, and user
            projectile.set(worldX, worldY, direction, true, this);

            // Subtract the cost (mana)
            projectile.substractResource(this);

            // Add it to the list
            gp.projectileList.add(projectile);
            gp.playSE(10);

            shotAvailableCounter = 0;
        }

        // This needs to be outside of key if statement
        if(invincible){
            invincibleCounter++;
            if(invincibleCounter > 60){
                invincibleCounter = 0;
                invincible = false;
            }
        }

        if(shotAvailableCounter < 30){
            shotAvailableCounter++;
        }

        if(life > maxLife){
            life = maxLife;
        }
        if(mana > maxMana){
            mana = maxMana;
        }

        if(life <= 0){
            gp.gameState = gp.gameOverState;
            gp.ui.commandNum = -1;
            gp.stopMusic();
            gp.playSE(12);
        }
    }

    private void contactMonster(int monsterIndex) {
        if(monsterIndex != -1){
            if(!invincible && !gp.monster[gp.currentMap][monsterIndex].dying) {
                gp.playSE(6);
                int damage = gp.monster[gp.currentMap][monsterIndex].attack - defense;
                if(damage < 0){
                    damage = 0;
                }
                life -= damage;
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
            damageMonster(monsterIndex, attack);

            int iTileIndex = gp.collisionChecker.checkEntity(this, gp.iTile);
            damageInteractiveTile(iTileIndex);

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

    private void damageInteractiveTile(int iTileIndex) {
        if(iTileIndex != -1 && gp.iTile[gp.currentMap][iTileIndex].destructible &&
                gp.iTile[gp.currentMap][iTileIndex].isCorrectItem(this) && !gp.iTile[gp.currentMap][iTileIndex].invincible){
            gp.iTile[gp.currentMap][iTileIndex].playSE();
            gp.iTile[gp.currentMap][iTileIndex].life--;
            gp.iTile[gp.currentMap][iTileIndex].invincible = true;
            generateParticle(gp.iTile[gp.currentMap][iTileIndex], gp.iTile[gp.currentMap][iTileIndex]);

            if(gp.iTile[gp.currentMap][iTileIndex].life == 0) {
                gp.iTile[gp.currentMap][iTileIndex] = gp.iTile[gp.currentMap][iTileIndex].getDestroyedForm();
            }
        }
    }

    public void damageMonster(int monsterIndex, int attack) {
        if(monsterIndex != -1){
           if(!gp.monster[gp.currentMap][monsterIndex].invincible){
               gp.playSE(5);
               int damage = attack - gp.monster[gp.currentMap][monsterIndex].defense;
               if(damage < 0){
                   damage = 0;
               }
               gp.monster[gp.currentMap][monsterIndex].life -= damage;
               gp.ui.addMessage(damage + " damage!");

               gp.monster[gp.currentMap][monsterIndex].invincible = true;
               gp.monster[gp.currentMap][monsterIndex].damageReaction();

               if(gp.monster[gp.currentMap][monsterIndex].life <= 0){
                   gp.monster[gp.currentMap][monsterIndex].dying = true;
                   gp.ui.addMessage("Killed the " + gp.monster[gp.currentMap][monsterIndex].name + "!");
                   gp.ui.addMessage("Exp " + gp.monster[gp.currentMap][monsterIndex].exp);
                   exp += gp.monster[gp.currentMap][monsterIndex].exp;
                   checkLevelUp();
               }
           }
        }
    }

    private void checkLevelUp() {
        if(exp >= nextLevelExp){
            level++;
            nextLevelExp *= 2;
            maxLife += 2;
            strength++;
            dexterity++;
            attack = getAttack();
            defense = getDefense();
            gp.playSE(8);
            gp.gameState = gp.dialogState;
            gp.ui.currentDialogue = "You are level " + level + " now!\n" + "You feel stronger!";
        }
    }

    public void selectItem(){
        int itemIndex = gp.ui.getItemIndexOnSlot(gp.ui.playerSlotCol, gp.ui.playerSlotRow);
        if(itemIndex < inventory.size()){
            Entity selectedItem = inventory.get(itemIndex);
            if(selectedItem.type == type_sword || selectedItem.type == type_axe){
                currentWeapon = selectedItem;
                attack = getAttack();
                getPlayerAttackImage();
            }
            if(selectedItem.type == type_shield){
                currentShield = selectedItem;
                defense = getDefense();
            }
            if(selectedItem.type == type_consumable){
                selectedItem.use(this);
                inventory.remove(itemIndex);
            }
        }
    }

    public void pickUpObject(int objIndex){
        if (objIndex != -1) {
            // Pickup only items
            if(gp.obj[gp.currentMap][objIndex].type == type_pickUpOnly){
                gp.obj[gp.currentMap][objIndex].use(this);
                gp.obj[gp.currentMap][objIndex] = null;
            }
            // Inventory items
            else {
                String text;
                if (inventory.size() != maxInventorySize) {
                    inventory.add(gp.obj[gp.currentMap][objIndex]);
                    gp.playSE(1);
                    text = "Got a " + gp.obj[gp.currentMap][objIndex].name + "!";
                    gp.obj[gp.currentMap][objIndex] = null;
                } else {
                    text = "You cannot carry any more!";
                }
                gp.ui.addMessage(text);
            }
        }
    }

    public void interactNPC(int index){
        // When Player hits a NPC
        if(gp.keyHandler.enterPressed) {
            if (index != -1) {
                attackCanceled = true;
                gp.gameState = gp.dialogState;
                gp.npc[gp.currentMap][index].speak();
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
