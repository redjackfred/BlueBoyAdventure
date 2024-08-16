package main;

import java.awt.*;

public class EventHandler {
    GamePanel gp;
    EventRect eventRect[][];

    int previousEventX, previousEventY;
    boolean canTouchEvent = true;

    public EventHandler(GamePanel gp){
        this.gp = gp;
        this.eventRect = new EventRect[gp.maxWorldRow][gp.maxWorldCol];
        int col = 0;
        int row = 0;
        while(col < gp.maxWorldCol && row < gp.maxWorldRow){
            eventRect[row][col] = new EventRect();
            eventRect[row][col].x = 23;
            eventRect[row][col].y = 23;
            eventRect[row][col].width = 2;
            eventRect[row][col].height = 2;
            eventRect[row][col].eventRectDefaultX = eventRect[row][col].x;
            eventRect[row][col].eventRectDefaultY = eventRect[row][col].y;

            col++;
            if(col == gp.maxWorldCol){
                col = 0;
                row++;
            }
        }

    }

    public void checkEvent(){
        // Check if the player character is more than 1 tile away from the last event
        int xDistance = Math.abs(gp.player.worldX - previousEventX);
        int yDistance = Math.abs(gp.player.worldY - previousEventY);
        int distance = Math.max(xDistance, yDistance);
        if(distance > gp.tileSize){
            canTouchEvent = true;
        }

        if(canTouchEvent) {
            if (hit(27, 16, "right")) {
                // event happens
                damagePit(27, 16, gp.dialogState);
//            teleport(gp.dialogState);
            }
            if (hit(23, 19, "any")) {
                // event happens
                damagePit(27, 16, gp.dialogState);
//            teleport(gp.dialogState);
            }
            if (hit(23, 12, "up")) {
                healingPool(23, 12, gp.dialogState);
            }
        }
    }

    public boolean hit(int col, int row, String requiredDirection){
        boolean hit = false;

        gp.player.solidArea.x = gp.player.worldX + gp.player.solidArea.x;
        gp.player.solidArea.y = gp.player.worldY + gp.player.solidArea.y;
        eventRect[row][col].x = col * gp.tileSize + eventRect[row][col].x;
        eventRect[row][col].y = row * gp.tileSize + eventRect[row][col].y;

        if(gp.player.solidArea.intersects(eventRect[row][col]) && !eventRect[row][col].eventDone){
            if(gp.player.direction.contentEquals(requiredDirection)
                    || requiredDirection.contentEquals("any")){
                hit = true;

                previousEventX = gp.player.worldX;
                previousEventY = gp.player.worldY;
            }
        }

        gp.player.solidArea.x = gp.player.solidAreaDefaultX;
        gp.player.solidArea.y = gp.player.solidAreaDefaultY;
        eventRect[row][col].x = eventRect[row][col].eventRectDefaultX;
        eventRect[row][col].y = eventRect[row][col].eventRectDefaultY;

        return hit;
    }

    public void teleport(int gameState){
        gp.gameState = gameState;
        gp.ui.currentDialogue = "Teleport!";
        gp.player.worldX = gp.tileSize * 37;
        gp.player.worldY = gp.tileSize * 10;
    }
    public void healingPool(int col, int row, int gameState){
        if(gp.keyHandler.enterPressed){
            gp.gameState = gameState;
            gp.ui.currentDialogue = "You drink the water.\nYour life has been recovered.";
            gp.player.life = gp.player.maxLife;
            gp.playSE(2);
            gp.player.attackCanceled = true;
            gp.assetSetter.setMonster();
            gp.player.mana = gp.player.maxMana;
        }
    }
    public void damagePit(int col, int row, int gameState){
        gp.gameState = gameState;
        gp.playSE(6);
        gp.ui.currentDialogue = "You fall into a pit!";
        gp.player.life--;
        eventRect[row][col].eventDone = true;
        canTouchEvent = false;
    }
}
