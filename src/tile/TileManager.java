package tile;

import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;

public class TileManager {
    GamePanel gamePanel;
    public Tile[] tiles;
    public int[][] mapTileNum;

    public TileManager(GamePanel gamePanel){
        this.gamePanel = gamePanel;
        tiles = new Tile[39];
        mapTileNum = new int[gamePanel.getMaxWorldRow()][gamePanel.getMaxWorldCol()];
        getTileImage();
        loadMap("maps/worldmap.txt");
    }

    public void loadMap(String filePath){
        try{
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            int col = 0;
            int row = 0;
            while(row < gamePanel.getMaxWorldRow()){
                String line = bufferedReader.readLine();
                String numbers[] = line.split(" ");
                while(col < gamePanel.getMaxWorldCol()) {
                    int num = Integer.parseInt(numbers[col]);
                    mapTileNum[row][col] = num;
                    col++;
                }
                col = 0;
                row++;
            }
            bufferedReader.close();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void getTileImage() {
        boolean collision = false;
        for(int i = 0; i < 38; i++){
            if(i >= 16){
                collision = true;
            }
            setUp(i, String.format("%03d", i), collision);
        }
    }

    public void setUp(int index, String imageName, boolean collision){
        UtilityTool uTool = new UtilityTool();
        try{
            tiles[index] = new Tile();
            tiles[index].image = ImageIO.read(getClass().getClassLoader().getResourceAsStream("tiles/" + imageName + ".png"));
            tiles[index].image = uTool.scaleImage(tiles[index].image, gamePanel.tileSize, gamePanel.tileSize);
            tiles[index].collision = collision;
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2){
        int worldCol = 0;
        int worldRow = 0;


        while(worldCol < gamePanel.getMaxWorldCol() && worldRow < gamePanel.getMaxWorldRow()){
            int tileNum = mapTileNum[worldRow][worldCol];
            int worldX = worldCol * gamePanel.tileSize;
            int worldY = worldRow * gamePanel.tileSize;
            int screenX = worldX - gamePanel.player.worldX + gamePanel.player.screenX;
            int screenY = worldY - gamePanel.player.worldY + gamePanel.player.screenY;

            if(screenX >= -gamePanel.tileSize && screenX < gamePanel.getScreenWidth() &&
                    screenY >= -gamePanel.tileSize && screenY < gamePanel.getScreenHeight()) {
                g2.drawImage(tiles[tileNum].image, screenX, screenY, null);
            }
            worldCol++;

            if(worldCol == gamePanel.getMaxWorldCol()){
                worldCol = 0;
                worldRow++;
            }
        }
    }
}
