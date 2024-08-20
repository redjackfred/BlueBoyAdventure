package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {
    public boolean upPressed, downPressed, leftPressed, rightPressed, enterPressed, shotKeyPressed;
    boolean showDebugText = false;
    GamePanel gp;

    public KeyHandler(GamePanel gp){
        this.gp = gp;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        if(gp.gameState == gp.titleState) {
            titleState(code);
        }
        // Play State
        else if(gp.gameState == gp.playState) {
            playState(code);
        }else if(gp.gameState == gp.pauseState){
            pauseState(code);
        }else if(gp.gameState == gp.dialogState){
            dialogueState(code);
        }else if(gp.gameState == gp.characterState){
            characterState(code);
        }else if(gp.gameState == gp.optionState){
            optionState(code);
        }else if(gp.gameState == gp.gameOverState){
            gameOverState(code);
        }else if(gp.gameState == gp.tradeState){
            tradeState(code);
        }
    }

    private void tradeState(int code) {
        if(code == KeyEvent.VK_W) {
            gp.ui.commandNum--;
            if (gp.ui.commandNum < 0) {
                gp.ui.commandNum = 2;
            }
            gp.playSE(9);
        }else if(code == KeyEvent.VK_S){
            gp.ui.commandNum++;
            if (gp.ui.commandNum > 2) {
                gp.ui.commandNum = 0;
            }
            gp.playSE(9);
        }else if(code == KeyEvent.VK_ENTER) {
            enterPressed = true;
        }
        if(gp.ui.subState == 1){
            npcInventory(code);
            if(code == KeyEvent.VK_ESCAPE){
                gp.ui.subState = 0;
            }
        }else if(gp.ui.subState == 2){
            playerInventory(code);
            if(code == KeyEvent.VK_ESCAPE){
                gp.ui.subState = 0;
            }
        }
    }

    private void gameOverState(int code) {
        if(code == KeyEvent.VK_W) {
            gp.ui.commandNum--;
            if (gp.ui.commandNum < 0) {
                gp.ui.commandNum = 1;
            }
            gp.playSE(9);
        }else if(code == KeyEvent.VK_S){
            gp.ui.commandNum++;
            if (gp.ui.commandNum > 1) {
                gp.ui.commandNum = 0;
            }
            gp.playSE(9);
        }else if(code == KeyEvent.VK_ENTER) {
            if (gp.ui.commandNum == 0) {
                gp.gameState = gp.playState;
                gp.retry();
                gp.playMusic(0);
            }else if(gp.ui.commandNum == 1){
                gp.gameState = gp.titleState;
                gp.restart();
            }
        }
    }

    private void optionState(int code) {
        if(code == KeyEvent.VK_ESCAPE){
            gp.gameState = gp.playState;
        }

        if (code == KeyEvent.VK_W) {
            gp.ui.commandNum--;
            gp.playSE(9);
            if (gp.ui.commandNum < 0) {
                gp.ui.commandNum = 4;
            }
        }
        if (code == KeyEvent.VK_S) {
            gp.ui.commandNum++;
            gp.playSE(9);
            if (gp.ui.commandNum > 4) {
                gp.ui.commandNum = 0;
            }
        }
        if (code == KeyEvent.VK_A) {
            if(gp.ui.subState == 0){
                if(gp.ui.commandNum == 0 && gp.music.volumnScale > 0){
                    gp.music.volumnScale--;
                    gp.music.checkVolumn();
                    gp.playSE(9);
                }else if(gp.ui.commandNum == 1 && gp.music.volumnScale > 0){
                    gp.soundEffect.volumnScale--;
                    gp.playSE(9);
                }
            }
        }
        if (code == KeyEvent.VK_D) {
            if(gp.ui.subState == 0){
                if(gp.ui.commandNum == 0 && gp.music.volumnScale < 5){
                    gp.music.volumnScale++;
                    gp.music.checkVolumn();
                    gp.playSE(9);
                }else if(gp.ui.commandNum == 1 && gp.music.volumnScale < 5){
                    gp.soundEffect.volumnScale++;
                    gp.playSE(9);
                }
            }
        }
        if (code == KeyEvent.VK_ENTER) {
            enterPressed = true;
        }

    }

    public void titleState(int code){
        if (gp.ui.titleScreenState == 0) {

            // Title State
            if (gp.gameState == gp.titleState) {
                if (code == KeyEvent.VK_W) {
                    gp.ui.commandNum--;
                    if (gp.ui.commandNum < 0) {
                        gp.ui.commandNum = 2;
                    }
                }
                if (code == KeyEvent.VK_S) {
                    gp.ui.commandNum++;
                    if (gp.ui.commandNum > 2) {
                        gp.ui.commandNum = 0;
                    }
                }
                if (code == KeyEvent.VK_ENTER) {
                    switch (gp.ui.commandNum) {
                        case 0:
                            gp.ui.titleScreenState = 1;
                            break;
                        case 1:
                            // Add later
                            break;
                        case 2:
                            System.exit(0);
                            break;
                    }
                }
            }
        } else if (gp.ui.titleScreenState == 1) {
            if (code == KeyEvent.VK_W) {
                gp.ui.commandNum--;
                if (gp.ui.commandNum < 0) {
                    gp.ui.commandNum = 3;
                }
            }
            if (code == KeyEvent.VK_S) {
                gp.ui.commandNum++;
                if (gp.ui.commandNum > 3) {
                    gp.ui.commandNum = 0;
                }
            }
            if (code == KeyEvent.VK_ENTER) {
                switch (gp.ui.commandNum) {
                    case 0:
                        System.out.println("Do some fighter specific stuff");
                        gp.gameState = gp.playState;
                        gp.playMusic(0);

                        break;
                    case 1:
                        System.out.println("Do some thief specific stuff");
                        gp.gameState = gp.playState;
                        gp.playMusic(0);
                        break;
                    case 2:
                        System.out.println("Do some sorcerer specific stuff");
                        gp.gameState = gp.playState;
                        gp.playMusic(0);
                        break;
                    case 3:
                        gp.ui.titleScreenState = 0;
                        gp.ui.commandNum = 0;
                        break;
                }
                gp.ui.titleScreenState = 0;
            }
        }
    }
    public void playState(int code){
        switch (code) {
            case KeyEvent.VK_W:
                upPressed = true;
                break;
            case KeyEvent.VK_S:
                downPressed = true;
                break;
            case KeyEvent.VK_A:
                leftPressed = true;
                break;
            case KeyEvent.VK_D:
                rightPressed = true;
                break;
            case KeyEvent.VK_C:
                gp.gameState = gp.characterState;
                break;
            case KeyEvent.VK_P:
                if (gp.gameState == gp.playState) {
                    gp.gameState = gp.pauseState;
                }
                break;
            case KeyEvent.VK_T:
                showDebugText = !showDebugText;
                break;
            case KeyEvent.VK_F:
                shotKeyPressed = true;
                break;
            case KeyEvent.VK_ESCAPE:
                gp.gameState = gp.optionState;
                break;
            case KeyEvent.VK_R:
                switch(gp.currentMap){
                    case 0: gp.tileManager.loadMap("maps/worldmap.txt", 0); break;
                    case 1: gp.tileManager.loadMap("maps/indoor01.txt", 1); break;
                }
                break;
            case KeyEvent.VK_ENTER:
                enterPressed = true;
                break;
        }
    }

    public void playerInventory(int code){
        if(code == KeyEvent.VK_W){
            gp.ui.playerSlotRow--;
            gp.playSE(9);
            if(gp.ui.playerSlotRow < 0){
                gp.ui.playerSlotRow = 3;
            }
        }
        if(code == KeyEvent.VK_A){
            gp.ui.playerSlotCol--;
            gp.playSE(9);
            if(gp.ui.playerSlotCol < 0){
                gp.ui.playerSlotCol = 4;
            }
        }
        if(code == KeyEvent.VK_S){
            gp.ui.playerSlotRow++;
            gp.playSE(9);
            if(gp.ui.playerSlotRow > 3){
                gp.ui.playerSlotRow = 0;
            }
        }
        if(code == KeyEvent.VK_D){
            gp.ui.playerSlotCol++;
            gp.playSE(9);
            if(gp.ui.playerSlotCol > 4){
                gp.ui.playerSlotCol = 0;
            }
        }
    }

    public void npcInventory(int code){
        if(code == KeyEvent.VK_W){
            gp.ui.npcSlotRow--;
            gp.playSE(9);
            if(gp.ui.npcSlotRow < 0){
                gp.ui.npcSlotRow = 3;
            }
        }
        if(code == KeyEvent.VK_A){
            gp.ui.npcSlotCol--;
            gp.playSE(9);
            if(gp.ui.npcSlotCol < 0){
                gp.ui.npcSlotCol = 4;
            }
        }
        if(code == KeyEvent.VK_S){
            gp.ui.npcSlotRow++;
            gp.playSE(9);
            if(gp.ui.npcSlotRow > 3){
                gp.ui.npcSlotRow = 0;
            }
        }
        if(code == KeyEvent.VK_D){
            gp.ui.npcSlotCol++;
            gp.playSE(9);
            if(gp.ui.npcSlotCol > 4){
                gp.ui.npcSlotCol = 0;
            }
        }
    }

    public void pauseState(int code){
        if(code == KeyEvent.VK_P){
            gp.gameState = gp.playState;
        }
    }
    public void dialogueState(int code){
        if(code == KeyEvent.VK_ENTER){
            gp.gameState = gp.playState;
        }
    }
    public void characterState(int code){
        if(code == KeyEvent.VK_C){
            gp.gameState = gp.playState;
        }
        playerInventory(code);
        if(code == KeyEvent.VK_ENTER){
            gp.player.selectItem();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        switch(code){
            case KeyEvent.VK_W:
                upPressed = false;
                break;
            case KeyEvent.VK_S:
                downPressed = false;
                break;
            case KeyEvent.VK_A:
                leftPressed = false;
                break;
            case KeyEvent.VK_D:
                rightPressed = false;
                break;
            case KeyEvent.VK_F:
                shotKeyPressed = false;
                break;
            case KeyEvent.VK_ENTER:
                enterPressed = false;
                break;
        }
    }
}
