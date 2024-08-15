package object;

import Entity.Entity;
import main.GamePanel;

public class OBJ_Shield_Wood extends Entity {
    public OBJ_Shield_Wood(GamePanel gp){
        super(gp);

        name = "Normal Shield";
        down1 = setUp("objects/shield_wood", gp.tileSize, gp.tileSize);
        defenseValue = 1;
    }
}
