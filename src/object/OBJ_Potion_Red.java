package object;

import Entity.Entity;
import main.GamePanel;

public class OBJ_Potion_Red extends Entity {
    GamePanel gp;
    public OBJ_Potion_Red(GamePanel gp){
        super(gp);
        this.gp = gp;

        type = type_consumable;
        name = "Red Potion";
        down1 = setUp("objects/potion_red", gp.tileSize, gp.tileSize);
        value = 5;
        defenseValue = 2;
        price = 150;
        description = "[" + name + "]\nHeals your life by " + value;
    }

    public void use(Entity entity){
        gp.gameState = gp.dialogState;
        gp.ui.currentDialogue = "You drink the " + name + "!\nYour life has been recovered by " + value + ".";
        entity.life += value;
        if(gp.player.life > gp.player.maxLife){
            gp.player.life = gp.player.maxLife;
        }
        gp.playSE(2);
    }
}
