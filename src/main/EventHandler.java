package main;

import Entity.Entity;

public class EventHandler {
    GamePanel gp;
    EventRect eventRect[][][];
    int tempMap, tempRow, tempCol;

    int previousEventX, previousEventY;
    boolean canTouchEvent = true;

    public EventHandler(GamePanel gp){
        this.gp = gp;
        this.eventRect = new EventRect[gp.maxMap][gp.maxWorldRow][gp.maxWorldCol];

        int map = 0;
        int col = 0;
        int row = 0;
        while(map < gp.maxMap && col < gp.maxWorldCol && row < gp.maxWorldRow){
            eventRect[map][row][col] = new EventRect();
            eventRect[map][row][col].x = 23;
            eventRect[map][row][col].y = 23;
            eventRect[map][row][col].width = 2;
            eventRect[map][row][col].height = 2;
            eventRect[map][row][col].eventRectDefaultX = eventRect[map][row][col].x;
            eventRect[map][row][col].eventRectDefaultY = eventRect[map][row][col].y;

            col++;
            if(col == gp.maxWorldCol){
                col = 0;
                row++;

                if(row == gp.maxWorldRow){
                    row = 0;
                    map++;
                }
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
            if (hit(0, 27, 16, "right")) {
                // event happens
                damagePit(gp.dialogState);
//            teleport(gp.dialogState);
            }
            else if (hit(0, 23, 19, "any")) {
                // event happens
                damagePit(gp.dialogState);
//            teleport(gp.dialogState);
            }
            else if (hit(0, 23, 12, "up")) {
                healingPool(gp.dialogState);
            }
            else if (hit(0, 10, 39, "any")){
                teleport(1, 12, 12, gp.dialogState);
            }
            else if (hit(1, 12, 13, "any")){
                teleport(0, 10, 40, gp.dialogState);
            }else if (hit(1, 12, 9, "up")){
                speak(gp.npc[1][0]);
            }

        }
    }

    public boolean hit(int map, int col, int row, String requiredDirection){
        boolean hit = false;

        if(map == gp.currentMap) {
            gp.player.solidArea.x = gp.player.worldX + gp.player.solidArea.x;
            gp.player.solidArea.y = gp.player.worldY + gp.player.solidArea.y;
            eventRect[map][row][col].x = col * gp.tileSize + eventRect[map][row][col].x;
            eventRect[map][row][col].y = row * gp.tileSize + eventRect[map][row][col].y;

            if (gp.player.solidArea.intersects(eventRect[map][row][col]) && !eventRect[map][row][col].eventDone) {
                if (gp.player.direction.contentEquals(requiredDirection)
                        || requiredDirection.contentEquals("any")) {
                    hit = true;

                    previousEventX = gp.player.worldX;
                    previousEventY = gp.player.worldY;
                }
            }

            gp.player.solidArea.x = gp.player.solidAreaDefaultX;
            gp.player.solidArea.y = gp.player.solidAreaDefaultY;
            eventRect[map][row][col].x = eventRect[map][row][col].eventRectDefaultX;
            eventRect[map][row][col].y = eventRect[map][row][col].eventRectDefaultY;
        }

        return hit;
    }

    public void teleport(int map, int col, int row, int gameState){
//        gp.gameState = gameState;
//        gp.ui.currentDialogue = "Teleport!";
        gp.gameState = gp.transitionState;
        tempMap = map;
        tempCol = col;
        tempRow = row;
        canTouchEvent = false;
        gp.playSE(13);
    }
    public void healingPool(int gameState){
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
    public void damagePit(int gameState){
        gp.gameState = gameState;
        gp.playSE(6);
        gp.ui.currentDialogue = "You fall into a pit!";
        gp.player.life--;
        canTouchEvent = false;
    }

    public void speak(Entity entity){
        if(gp.keyHandler.enterPressed){
            gp.gameState = gp.dialogState;
            gp.player.attackCanceled = true;
            entity.speak();
        }
    }
}
