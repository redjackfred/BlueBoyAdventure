package object;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_Heart extends SuperObject{

    public OBJ_Heart(GamePanel gp){
        name = "Heart";
        try{
            image = ImageIO.read(getClass().getClassLoader().getResourceAsStream("objects/heart_full.png"));
            image2 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("objects/heart_half.png"));
            image3 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("objects/heart_blank.png"));
            image = uTool.scaleImage(image, gp.tileSize, gp.tileSize);
            image2 = uTool.scaleImage(image2, gp.tileSize, gp.tileSize);
            image3 = uTool.scaleImage(image3, gp.tileSize, gp.tileSize);
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
