package object;

import Entity.Entity;
import main.GamePanel;

public class OBJ_ManaCrystal extends Entity {
    GamePanel gp;

    public OBJ_ManaCrystal(GamePanel gp){
        super(gp);
        this.gp = gp;

        type = type_pickUpOnly;
        name = "Mana Crystal";
        value = 1;
        price = 100;
        down1 = setUp("objects/manacrystal_full", gp.tileSize, gp.tileSize);
        image = setUp("objects/manacrystal_full", gp.tileSize, gp.tileSize);
        image2 = setUp("objects/manacrystal_blank", gp.tileSize, gp.tileSize);
    }

    public void use(Entity entity){
        gp.playSE(2);
        gp.ui.addMessage("Mana +" + value);
        entity.mana += value;
    }
}
