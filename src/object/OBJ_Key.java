package object;

import Entity.Entity;
import main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_Key extends Entity {
    public OBJ_Key(GamePanel gp){
        super(gp);
        down1 = setUp("objects/key", gp.tileSize, gp.tileSize);
        name = "Key";
        price = 100;
        description = "[" + name + "]\nIt opens a door";
    }
}
