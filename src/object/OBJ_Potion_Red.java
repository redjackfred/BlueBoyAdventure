package object;

import Entity.Entity;
import main.GamePanel;

public class OBJ_Potion_Red extends Entity {
    GamePanel gp;
    int healingValue = 5;
    public OBJ_Potion_Red(GamePanel gp){
        super(gp);
        this.gp = gp;

        type = type_consumable;
        name = "Red Potion";
        down1 = setUp("objects/potion_red", gp.tileSize, gp.tileSize);
        defenseValue = 2;
        description = "[" + name + "]\nHeals your life by " + healingValue;
    }

    public void use(Entity entity){
        gp.gameState = gp.dialogState;
        gp.ui.currentDialogue = "You drink the " + name + "!\nYour life has been recovered by " + healingValue + ".";
        entity.life += healingValue;
        if(gp.player.life > gp.player.maxLife){
            gp.player.life = gp.player.maxLife;
        }
        gp.playSE(2);
    }
}
