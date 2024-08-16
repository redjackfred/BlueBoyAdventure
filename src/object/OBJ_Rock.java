package object;

import Entity.Entity;
import Entity.Projectile;
import main.GamePanel;

import java.awt.*;

public class OBJ_Rock extends Projectile {
    GamePanel gp;

    public OBJ_Rock(GamePanel gp) {
        super(gp);
        this.gp = gp;

        name = "Rock";
        speed = 8;
        maxLife = 80;
        life = maxLife;
        attack = 2;
        useCost = 1;
        alive = false;
        getImage();
    }

    private void getImage() {
        up1 = setUp("projectile/rock_down_1", gp.tileSize, gp.tileSize);
        up2 = setUp("projectile/rock_down_1", gp.tileSize, gp.tileSize);
        down1 = setUp("projectile/rock_down_1", gp.tileSize, gp.tileSize);
        down2 = setUp("projectile/rock_down_1", gp.tileSize, gp.tileSize);
        left1 = setUp("projectile/rock_down_1", gp.tileSize, gp.tileSize);
        left2 = setUp("projectile/rock_down_1", gp.tileSize, gp.tileSize);
        right1 = setUp("projectile/rock_down_1", gp.tileSize, gp.tileSize);
        right2 = setUp("projectile/rock_down_1", gp.tileSize, gp.tileSize);
    }

    public boolean haveResource(Entity user){
        boolean haveResource = false;
        if(user.ammo >= useCost){
            haveResource = true;
        }
        return haveResource;
    }

    public void substractResource(Entity user){
        user.ammo -= useCost;
    }

    public Color getParticleColor(){
        return new Color(40, 50, 0);
    }

    public int getParticleSize(){
        int size = 10;
        return size;
    }

    public int getParticleSpeed(){
        int speed = 1;
        return speed;
    }

    public int getParticleMaxLife(){
        int maxLife = 20;
        return maxLife;
    }
}
