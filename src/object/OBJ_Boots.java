package object;

import Entity.Entity;
import main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_Boots extends Entity {
    public OBJ_Boots(GamePanel gp){
        super(gp);
        name = "Boots";
        price = 500;
        down1 = setUp("objects/boots", gp.tileSize, gp.tileSize);
    }
}
