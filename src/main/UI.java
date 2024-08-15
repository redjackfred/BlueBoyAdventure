package main;

import object.OBJ_Heart;
import object.OBJ_Key;
import object.SuperObject;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;

public class UI {
    GamePanel gamePanel;
    Graphics2D g2;
    BufferedImage heart_full, heart_half, heart_blank;
    public boolean messageOn = false;
    public String message = "";
    int messageCounter = 0;
    public boolean gameFinished = false;
    public String currentDialogue = "";
    Font maruMonica, purisaB;
    public int commandNum = 0;
    public int titleScreenState = 0; // 0: the first screen, 1: the second screen


    public UI(GamePanel gamePanel){
        this.gamePanel = gamePanel;
        try {
            InputStream is = getClass().getResourceAsStream("/font/x12y16pxMaruMonica.ttf");
            maruMonica = Font.createFont(Font.TRUETYPE_FONT, is);
            is = getClass().getResourceAsStream("/font/Purisa Bold.ttf");
            purisaB = Font.createFont(Font.TRUETYPE_FONT, is);
        }catch (IOException e){
            e.printStackTrace();
        }catch (FontFormatException e){
            e.printStackTrace();
        }

        // Create HUD object
        SuperObject heart = new OBJ_Heart(gamePanel);
        heart_full = heart.image;
        heart_half = heart.image2;
        heart_blank = heart.image3;
    }

    public void showMessage(String text){
        message = text;
        messageOn = true;
    }
    public void draw(Graphics2D g2){
        this.g2 = g2;
        g2.setFont(purisaB);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setColor(Color.white);

        if(gamePanel.gameState == gamePanel.titleState){
            drawTitleScreen();
        }else if(gamePanel.gameState == gamePanel.playState){
            // Do playState stuff later
            drawPlayerLife();
            drawPlayScreen();
        }else if(gamePanel.gameState == gamePanel.pauseState){
            drawPlayerLife();
            drawPauseScreen();
        }else if(gamePanel.gameState == gamePanel.dialogState){
            drawPlayerLife();
            drawDialogueScreen();
        }
    }

    public void drawPlayerLife(){
        int x = gamePanel.tileSize / 2;
        int y = gamePanel.tileSize / 2;
        int i = 0;
        while(i < gamePanel.player.maxLife / 2){
            g2.drawImage(heart_blank, x, y, null);
            i++;
            x += gamePanel.tileSize;
        }

    }

    public void drawTitleScreen(){
        if(titleScreenState == 0) {
            g2.setColor(new Color(70, 120, 80));
            g2.fillRect(0, 0, gamePanel.screenWidth, gamePanel.screenHeight);

            // Title Name
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 48F));
            String text = "Blue Boy Adventure";
            int x = getXforCenteredText(text);
            int y = gamePanel.tileSize * 3;

            // Shadow
            g2.setColor(Color.black);
            g2.drawString(text, x + 5, y + 5);

            // Main Color
            g2.setColor(Color.white);
            g2.drawString(text, x, y);

            // Blue boy image
            x = gamePanel.screenWidth / 2 - (gamePanel.tileSize * 2) / 2;
            y += gamePanel.tileSize * 2;
            g2.drawImage(gamePanel.player.down1, x, y, gamePanel.tileSize * 2, gamePanel.tileSize * 2, null);

            // Menu
            g2.setFont(g2.getFontMetrics().getFont().deriveFont(Font.BOLD, 24F));

            text = "New Game ";
            x = getXforCenteredText(text);
            y += gamePanel.tileSize * 4;
            g2.drawString(text, x, y);
            if (commandNum == 0) {
                g2.drawString(">", x - gamePanel.tileSize, y);
            }

            text = "Load Game";
            x = getXforCenteredText(text);
            y += gamePanel.tileSize;
            g2.drawString(text, x, y);
            if (commandNum == 1) {
                g2.drawString(">", x - gamePanel.tileSize, y);
            }

            text = "Quit";
            x = getXforCenteredText(text);
            y += gamePanel.tileSize;
            g2.drawString(text, x, y);
            if (commandNum == 2) {
                g2.drawString(">", x - gamePanel.tileSize, y);
            }
        }else if(titleScreenState == 1){
            // Class selection screen
            g2.setColor(Color.white);
            g2.setFont(g2.getFont().deriveFont(21F));

            String text = "Select your class!";
            int x = getXforCenteredText(text);
            int y = gamePanel.tileSize * 3;
            g2.drawString(text, x, y);

            text = "Fighter";
            x = getXforCenteredText(text);
            y += gamePanel.tileSize * 3;
            g2.drawString(text, x, y);
            if(commandNum == 0){
                g2.drawString(">", x - gamePanel.tileSize, y);
            }

            text = "Thief";
            x = getXforCenteredText(text);
            y += gamePanel.tileSize;
            g2.drawString(text, x, y);
            if(commandNum == 1){
                g2.drawString(">", x - gamePanel.tileSize, y);
            }

            text = "Sorcerer";
            x = getXforCenteredText(text);
            y += gamePanel.tileSize;
            g2.drawString(text, x, y);
            if(commandNum == 2){
                g2.drawString(">", x - gamePanel.tileSize, y);
            }

            text = "Back";
            x = getXforCenteredText(text);
            y += gamePanel.tileSize * 2;
            g2.drawString(text, x, y);
            if(commandNum == 3){
                g2.drawString(">", x - gamePanel.tileSize, y);
            }
        }
    }

    public void drawDialogueScreen(){
        // Dialogue Window
        int x = gamePanel.tileSize * 2;
        int y = gamePanel.tileSize /2 ;
        int width = gamePanel.getScreenWidth() - gamePanel.tileSize * 4;
        int height = gamePanel.tileSize * 4;
        drawSubWindow(x, y, width, height);

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 18F));
        x += gamePanel.tileSize;
        y += gamePanel.tileSize;
        for(String line: currentDialogue.split("\n")){
            g2.drawString(line, x, y);
            y += 40;
        }

    }

    public void drawSubWindow(int x, int y, int width, int height){
        Color color = new Color(0, 0, 0, 200);
        g2.setColor(color);
        g2.fillRoundRect(x, y, width, height, 35, 35);

        color = new Color(255, 255, 255);
        g2.setColor(color);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x + 5, y + 5, width - 10, height - 10, 25, 25);
    }

    public void drawPauseScreen(){
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 80F));
        String text = "PAUSED";
        int x = getXforCenteredText(text);
        int y = gamePanel.screenHeight / 2;

        g2.drawString(text, x, y);
    }

    private int getXforCenteredText(String text){
        int textLength = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = gamePanel.screenWidth/2 - textLength/2;

        return x;
    }
    public void drawPlayScreen(){

    }
}
