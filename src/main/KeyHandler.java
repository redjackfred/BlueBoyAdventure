package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {
    public boolean upPressed, downPressed, leftPressed, rightPressed, enterPressed;
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
            case KeyEvent.VK_R:
                gp.tileManager.loadMap("maps/worldmap.txt");
                break;
            case KeyEvent.VK_ENTER:
                enterPressed = true;
                break;
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
            case KeyEvent.VK_ENTER:
                enterPressed = false;
                break;
        }
    }
}
