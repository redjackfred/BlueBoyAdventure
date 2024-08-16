package tile_interactive;

import Entity.Entity;
import main.GamePanel;

public class IT_Trunk extends InteractiveTile{
    GamePanel gp;
    public IT_Trunk(GamePanel gp, int col, int row) {
        super(gp, col, row);
        this.gp = gp;

        worldX = gp.tileSize * col;
        worldY = gp.tileSize * row;
        solidArea.x = 0;
        solidArea.y = 0;
        solidArea.width = 0;
        solidArea.height = 0;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;


        down1 = setUp("tiles_interactive/trunk", gp.tileSize, gp.tileSize);
    }
}
