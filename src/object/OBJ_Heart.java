package object;

import Entity.Entity;
import main.GamePanel;

public class OBJ_Heart extends Entity {
    GamePanel gp;

    public OBJ_Heart(GamePanel gp){
        super(gp);
        this.gp = gp;

        type = type_pickUpOnly;
        name = "Heart";
        value = 2;
        price = 100;
        down1 = setUp("objects/heart_full", gp.tileSize, gp.tileSize);
        image = setUp("objects/heart_full", gp.tileSize, gp.tileSize);
        image2 = setUp("objects/heart_half", gp.tileSize, gp.tileSize);
        image3 = setUp("objects/heart_blank", gp.tileSize, gp.tileSize);
    }

    public void use(Entity entity){
        gp.playSE(2);
        gp.ui.addMessage("Life +" + value);
        entity.life += value;
    }
}
