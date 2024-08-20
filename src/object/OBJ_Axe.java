package object;

import Entity.Entity;
import main.GamePanel;

public class OBJ_Axe extends Entity {
    public OBJ_Axe(GamePanel gp){
        super(gp);

        type = type_axe;
        name = "Woodcutter's Axe";
        down1 = setUp("objects/axe", gp.tileSize, gp.tileSize);
        attackValue = 2;
        attackArea.width = 30;
        attackArea.height = 30;
        price = 75;
        description = "[" + name + "]\nA bit rusty but still\ncan cut some trees";
    }
}
