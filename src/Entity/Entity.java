package Entity;

import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Entity {
    GamePanel gp;
    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
    public BufferedImage image, image2, image3;
    public BufferedImage attackUp1, attackUp2, attackDown1, attackDown2, attackLeft1, attackLeft2, attackRight1, attackRight2;
    String[] dialogues = new String[20];
    public int solidAreaDefaultX, solidAreaDefaultY;
    public Rectangle solidArea = new Rectangle(0, 0, 48, 48);
    public Rectangle attackArea = new Rectangle(0, 0, 0, 0);

    // State
    public int worldX, worldY;
    public String direction = "down";
    int dialogueIndex = 0;
    public boolean collisionOn = false;
    public int spriteNum = 1;
    public boolean collision = false;
    public boolean invincible = false;
    public boolean attacking = false;
    public boolean alive = true;
    public boolean dying = false;
    boolean hpBarOn = false;

    // Counter
    public int actionLockCounter = 0;
    public int spriteCounter = 0;
    public int invincibleCounter = 0;
    int dyingCounter = 0;
    int hpBarCounter = 0;
    public int shotAvailableCounter = 0;

    // Character attributes
    public String name;
    public int speed;
    public int maxLife;
    public int mana;
    public int ammo;
    public int maxMana;
    public int life;
    public int level;
    public int strength;
    public int dexterity;
    public int attack;
    public int defense;
    public int exp;
    public int nextLevelExp;
    public int coin;
    public Entity currentWeapon;
    public Entity currentShield;
    public Projectile projectile;

    // Item attributes
    public int attackValue;
    public int defenseValue;
    public String description = "";
    public int useCost;
    public int value;

    // Type
    public int type; // 0 = player, 1 = npc, 2 = monster
    public final int type_player = 0;
    public final int type_npc = 1;
    public final int type_monster = 2;
    public final int type_sword = 3;
    public final int type_axe = 4;
    public final int type_shield = 5;
    public final int type_consumable = 6;
    public final int type_pickUpOnly = 7;

    public Entity(GamePanel gp){
        this.gp = gp;
    }

    public BufferedImage setUp(String imagePath, int width, int height){
        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;
        try{
            image = ImageIO.read(getClass().getClassLoader().getResourceAsStream(imagePath +".png"));
            image = uTool.scaleImage(image, width, height);
        }catch (Exception e){
            e.printStackTrace();
        }
        return image;
    }

    public void setAction(){}
    public void damageReaction(){}
    public void speak(){
        if(dialogues[dialogueIndex] == null){
            dialogueIndex = 0;
        }
        gp.ui.currentDialogue = dialogues[dialogueIndex];
        dialogueIndex++;

        switch(gp.player.direction){
            case "up":
                direction = "down";
                break;
            case "down":
                direction = "up";
                break;
            case "left":
                direction = "right";
                break;
            case "right":
                direction = "left";
                break;
        }
    }

    public void use(Entity entity){}

    public void checkDrop(){}
    public void dropItem(Entity droppedItem){
        for(int i = 0; i < gp.obj.length; i++){
            if(gp.obj[i] == null){
                gp.obj[i] = droppedItem;
                gp.obj[i].worldX = worldX;
                gp.obj[i].worldY = worldY;
                break;
            }
        }
    }

    public Color getParticleColor(){
        Color color = null;
        return color;
    }

    public int getParticleSize(){
        int size = 0;
        return size;
    }

    public int getParticleSpeed(){
        int speed = 0;
        return speed;
    }

    public int getParticleMaxLife(){
        int maxLife = 0;
        return maxLife;
    }

    public void generateParticle(Entity generator, Entity target){
        Color color = generator.getParticleColor();
        int size = generator.getParticleSize();
        int speed = generator.getParticleSpeed();
        int maxLife = generator.getParticleMaxLife();

        Particle p1 = new Particle(gp, target, color, size, speed, maxLife, -2, -1);
        Particle p2 = new Particle(gp, target, color, size, speed, maxLife, 2, -1);
        Particle p3 = new Particle(gp, target, color, size, speed, maxLife, 2, 1);
        Particle p4 = new Particle(gp, target, color, size, speed, maxLife, -2, 1);
        gp.particleList.add(p1);
        gp.particleList.add(p2);
        gp.particleList.add(p3);
        gp.particleList.add(p4);
    }

    public void update(){
        setAction();
        collisionOn = false;
        gp.collisionChecker.checkTile(this);
        gp.collisionChecker.checkObject(this, false);
        gp.collisionChecker.checkEntity(this, gp.npc);
        gp.collisionChecker.checkEntity(this, gp.monster);
        gp.collisionChecker.checkEntity(this, gp.iTile);
        boolean contactPlayer = gp.collisionChecker.checkPlayer(this);
        if(this.type == type_monster && contactPlayer && !gp.player.invincible){
            damagePlayer(attack);
        }

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

        if(invincible){
            invincibleCounter++;
            if(invincibleCounter > 40){
                invincibleCounter = 0;
                invincible = false;
            }
        }
        if(shotAvailableCounter < 30){
            shotAvailableCounter++;
        }
    }

    public void damagePlayer(int attack){
        gp.playSE(6);
        int damage = attack - gp.player.defense;
        if(damage < 0){
            damage = 0;
        }
        gp.player.life -= damage;
        gp.player.invincible = true;
    }

    public void draw(Graphics2D g2){
        BufferedImage image = null;
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        if(screenX >= -gp.tileSize && screenX < gp.getScreenWidth() &&
                screenY >= -gp.tileSize && screenY < gp.getScreenHeight()) {
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

            // Monster HP bar
            if(type == 2 && hpBarOn) {
                double oneScale = (double)gp.tileSize / maxLife;
                double hpBarValue = oneScale * life;
                g2.setColor(new Color(35, 35, 35));
                g2.fillRect(screenX - 2, screenY - 18, gp.tileSize + 4, 14);
                g2.setColor(new Color(255, 0, 30));
                if(hpBarValue < 0){
                    hpBarValue = 0;
                }
                g2.fillRect(screenX, screenY - 15, (int)hpBarValue, 10);

                hpBarCounter++;
                if(hpBarCounter > 600){
                    hpBarCounter = 0;
                    hpBarOn = false;
                }
            }

            if(invincible){
                hpBarOn = true;
                changeAlpha(g2, 0.45F);
            }
            if(dying){
                dyingAnimation(g2);
            }

            g2.drawImage(image, screenX, screenY, null);

            changeAlpha(g2, 1F);
        }
    }

    private void dyingAnimation(Graphics2D g2) {
        dyingCounter++;
        int i = 5;

        if(dyingCounter < i){
            changeAlpha(g2, 0.3F);
        }else if(dyingCounter < i * 2){
            changeAlpha(g2, 1F);
        }else if(dyingCounter < i * 3){
            changeAlpha(g2, 0.25F);
        }else if(dyingCounter < i * 4){
            changeAlpha(g2, 1F);
        }else if(dyingCounter < i * 5){
            changeAlpha(g2, 0.2F);
        }else if(dyingCounter < i * 6){
            changeAlpha(g2, 1F);
        }else if(dyingCounter < i * 7){
            changeAlpha(g2, 0.1F);
        }else if(dyingCounter < i * 8){
            changeAlpha(g2, 1F);
        }else{
            alive = false;
        }
    }

    public void changeAlpha(Graphics2D g2, float alphaValue){
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaValue));
    }
}
