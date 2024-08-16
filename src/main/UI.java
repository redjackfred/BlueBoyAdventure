package main;

import Entity.Entity;
import object.OBJ_Heart;
import object.OBJ_ManaCrystal;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class UI {
    GamePanel gamePanel;
    Graphics2D g2;
    BufferedImage heart_full, heart_half, heart_blank, crystal_full, crystal_blank;
    public boolean messageOn = false;
    ArrayList<String> message = new ArrayList<>();
    ArrayList<Integer> messageCounter = new ArrayList<>();
    public boolean gameFinished = false;
    public String currentDialogue = "";
    Font maruMonica, purisaB;
    public int commandNum = 0;
    public int titleScreenState = 0; // 0: the first screen, 1: the second screen
    int slotRow = 0;
    int slotCol = 0;
    int subState = 0;


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
        Entity heart = new OBJ_Heart(gamePanel);
        heart_full = heart.image;
        heart_half = heart.image2;
        heart_blank = heart.image3;

        Entity crystal = new OBJ_ManaCrystal(gamePanel);
        crystal_full = crystal.image;
        crystal_blank = crystal.image2;
    }

    public void addMessage(String text){
        message.add(text);
        messageCounter.add(0);
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
            drawMessage();
            drawPlayerLife();
            drawPlayScreen();
        }else if(gamePanel.gameState == gamePanel.pauseState){
            drawPlayerLife();
            drawPauseScreen();
        }else if(gamePanel.gameState == gamePanel.dialogState){
            drawPlayerLife();
            drawDialogueScreen();
        }else if(gamePanel.gameState == gamePanel.characterState){
            drawCharacterScreen();
            drawInventory();
        }else if(gamePanel.gameState == gamePanel.optionState){
            drawOptionsScreen();
        }
    }

    private void drawOptionsScreen() {
        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(20F));

        // Sub window
        int frameX = gamePanel.tileSize * 6;
        int frameY = gamePanel.tileSize;
        int frameWidth = gamePanel.tileSize * 8;
        int frameHeight = gamePanel.tileSize * 10;
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        switch (subState){
            case 0: options_top(frameX, frameY); break;
            case 1: option_control(frameX, frameY); break;
            case 2: option_endGameConfirmation(frameX, frameY); break;
        }
    }

    private void option_endGameConfirmation(int frameX, int frameY) {
        int textX = frameX + gamePanel.tileSize;
        int textY = frameY + gamePanel.tileSize * 3;

        currentDialogue = "Quit the game and \nreturn to the title screen?";

        for(String line: currentDialogue.split("\n")){
            g2.drawString(line, textX, textY);
            textY += 40;
        }

        // Yes
        String text = "Yes";
        textX = getXforCenteredText(text);
        textY += gamePanel.tileSize * 3;
        g2.drawString(text, textX, textY);
        if(commandNum == 0){
            g2.drawString(">", textX - 25, textY);
            if(gamePanel.keyHandler.enterPressed){
                subState = 0;
                gamePanel.gameState = gamePanel.titleState;
                gamePanel.keyHandler.enterPressed = false;
            }
        }

        // No
        text = "No";
        textX = getXforCenteredText(text);
        textY += gamePanel.tileSize;
        g2.drawString(text, textX, textY);
        if(commandNum == 1){
            g2.drawString(">", textX - 25, textY);
            if(gamePanel.keyHandler.enterPressed){
                subState = 0;
                commandNum = 3;
                gamePanel.keyHandler.enterPressed = false;
            }
        }
    }

    public void options_top(int frameX, int frameY){
        int textX;
        int textY;

        // Title
        String text = "Options";
        textX = getXforCenteredText(text);
        textY = frameY + gamePanel.tileSize;
        g2.drawString(text, textX, textY);

        // Music
        textY += gamePanel.tileSize;
        g2.drawString("Music", textX, textY);
        if(commandNum == 0){
            g2.drawString(">", textX - 25, textY);
        }

        // SE
        textY += gamePanel.tileSize;
        g2.drawString("SE", textX, textY);
        if(commandNum == 1){
            g2.drawString(">", textX - 25, textY);
        }

        // Control
        textY += gamePanel.tileSize;
        g2.drawString("Control", textX, textY);
        if(commandNum == 2){
            g2.drawString(">", textX - 25, textY);
            if(gamePanel.keyHandler.enterPressed){
                subState = 1;
                commandNum = 0;
                gamePanel.keyHandler.enterPressed = false;
            }
        }

        // End Game
        textY += gamePanel.tileSize;
        g2.drawString("End Game", textX, textY);
        if(commandNum == 3){
            g2.drawString(">", textX - 25, textY);
            if(gamePanel.keyHandler.enterPressed){
                subState = 2;
                commandNum = 0;
                gamePanel.keyHandler.enterPressed = false;
            }
        }

        // Back
        textY += gamePanel.tileSize * 2;
        g2.drawString("Back", textX, textY);
        if(commandNum == 4){
            g2.drawString(">", textX - 25, textY);
            if(gamePanel.keyHandler.enterPressed){
                gamePanel.gameState = gamePanel.playState;
                commandNum = 0;
            }
        }

        // Music Volumn
        textX = frameX + (int)(gamePanel.tileSize * 4.5);
        textY = frameY + gamePanel.tileSize + 24;
        g2.drawRect(textX, textY, 120, 24);
        int volumnWidth = 24 * gamePanel.music.volumnScale;
        g2.fillRect(textX, textY, volumnWidth, 24);

        // SE Volumn
        textY += gamePanel.tileSize;
        g2.drawRect(textX, textY, 120, 24);
        volumnWidth = 24 * gamePanel.soundEffect.volumnScale;
        g2.fillRect(textX, textY, volumnWidth, 24);


    }

    private void drawInventory() {
        // Frame
        int frameX = gamePanel.tileSize * 9;
        int frameY = gamePanel.tileSize;
        int frameWidth = gamePanel.tileSize * 6;
        int frameHeight = gamePanel.tileSize * 5;
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        // Slots
        final int slotXStart = frameX + 20;
        final int slotYStart = frameY + 20;
        int slotX = slotXStart;
        int slotY = slotYStart;
        int slotSize = gamePanel.tileSize + 3;

        // Draw player's items
        for(int i = 0; i < gamePanel.player.inventory.size(); i++){
            // Equip cursor
            if(gamePanel.player.inventory.get(i) == gamePanel.player.currentWeapon ||
                    gamePanel.player.inventory.get(i) == gamePanel.player.currentShield){
                g2.setColor(new Color(240, 190, 90));
                g2.fillRoundRect(slotX, slotY, gamePanel.tileSize, gamePanel.tileSize, 10, 10);
            }

            g2.drawImage(gamePanel.player.inventory.get(i).down1, slotX, slotY, null);
            slotX += slotSize;
            if(i % 5 == 4){
                slotX = slotXStart;
                slotY += slotSize;
            }
        }

        // Cursor
        int cursorX = slotXStart + slotSize * slotCol;
        int cursorY = slotYStart + slotSize * slotRow;
        int cursorWidth = gamePanel.tileSize;
        int cursorHeight = gamePanel.tileSize;
        // Draw cursor
        g2.setColor(Color.white);
        g2.setStroke(new BasicStroke(4));
        g2.drawRoundRect(cursorX, cursorY, cursorWidth, cursorHeight, 10, 10);

        // Description frame
        int dFrameX = frameX;
        int dFrameY = frameY + frameHeight;
        int dFrameWidth = frameWidth;
        int dFrameHeight = gamePanel.tileSize * 3;

        // Draw description text
        int textX = dFrameX + 20;
        int textY = dFrameY + gamePanel.tileSize;
        g2.setFont(g2.getFont().deriveFont(20F));
        int itemIndex = getItemIndexOnSlot();
        if(itemIndex < gamePanel.player.inventory.size()){
            drawSubWindow(dFrameX,dFrameY,dFrameWidth,dFrameHeight);
            for(String line: gamePanel.player.inventory.get(itemIndex).description.split("\n")){
                g2.drawString(line, textX, textY);
                textY += 32;
            }

        }
    }

    public void option_control(int frameX, int frameY){
        int textX;
        int textY;

        // Title
        String text = "Control";
        textX = getXforCenteredText(text);
        textY = frameY + gamePanel.tileSize;
        g2.drawString(text, textX, textY);

        textX = frameX + gamePanel.tileSize;
        textY += gamePanel.tileSize;
        g2.drawString("Move", textX, textY); textY += gamePanel.tileSize;
        g2.drawString("Confirm/Attack", textX, textY); textY += gamePanel.tileSize;
        g2.drawString("Shoot/Cast", textX, textY); textY += gamePanel.tileSize;
        g2.drawString("Character Screen", textX, textY); textY += gamePanel.tileSize;
        g2.drawString("Pause", textX, textY); textY += gamePanel.tileSize;
        g2.drawString("Option", textX, textY); textY += gamePanel.tileSize;

        textX = frameX + gamePanel.tileSize * 6;
        textY = frameY + gamePanel.tileSize * 2;
        g2.drawString("WASD", textX, textY); textY += gamePanel.tileSize;
        g2.drawString("ENTER", textX, textY); textY += gamePanel.tileSize;
        g2.drawString("F", textX, textY); textY += gamePanel.tileSize;
        g2.drawString("C", textX, textY); textY += gamePanel.tileSize;
        g2.drawString("P", textX, textY); textY += gamePanel.tileSize;
        g2.drawString("ESC", textX, textY); textY += gamePanel.tileSize;

        // BACK
        textX = frameX + gamePanel.tileSize;
        textY = gamePanel.tileSize * 9;
        g2.drawString("Back", textX, textY);
        if(commandNum == 0){
            g2.drawString(">", textX - 25, textY);
            if(gamePanel.keyHandler.enterPressed){
                subState = 0;
                commandNum = 2;
                gamePanel.keyHandler.enterPressed = false;
            }
        }
    }

    public int getItemIndexOnSlot(){
        int itemIndex = slotCol + slotRow * 5;
        return itemIndex;
    }

    private void drawMessage() {
        int messageX = gamePanel.tileSize;
        int messageY = gamePanel.tileSize * 4;
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 20F));

        for(int i = 0; i < message.size(); i++){
            if(message.get(i) != null){
                g2.setColor(Color.black);
                g2.drawString(message.get(i), messageX + 2, messageY + 2);
                g2.setColor(Color.white);
                g2.drawString(message.get(i), messageX, messageY);

                // Set the counter to the array
                int counter = messageCounter.get(i) + 1;
                messageCounter.set(i, counter);
                messageY += 50;

                if(messageCounter.get(i) > 180){
                    message.remove(i);
                    messageCounter.remove(i);
                }
            }
        }
    }

    private void drawCharacterScreen() {
        // Create a frame
        final int frameX = gamePanel.tileSize;
        final int frameY = gamePanel.tileSize;
        final int frameWidth = gamePanel.tileSize * 5;
        final int frameHeight = gamePanel.tileSize * 10;
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        // Text
        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(20F));

        int textX = frameX + gamePanel.tileSize / 2;
        int textY = frameY + gamePanel.tileSize;
        final int lineHeight = 33;

        // Names
        g2.drawString("Level", textX, textY);
        textY += lineHeight;
        g2.drawString("Life", textX, textY);
        textY += lineHeight;
        g2.drawString("Mana", textX, textY);
        textY += lineHeight;
        g2.drawString("Strength", textX, textY);
        textY += lineHeight;
        g2.drawString("Dexterity", textX, textY);
        textY += lineHeight;
        g2.drawString("Attack", textX, textY);
        textY += lineHeight;
        g2.drawString("Defense", textX, textY);
        textY += lineHeight;
        g2.drawString("Exp", textX, textY);
        textY += lineHeight;
        g2.drawString("Next Level", textX, textY);
        textY += lineHeight;
        g2.drawString("Coin", textX, textY);
        textY += lineHeight + 20;
        g2.drawString("Weapon", textX, textY);
        textY += lineHeight + 15;
        g2.drawString("Shield", textX, textY);

        // Values
        int tailX = frameX + frameWidth - 30;
        // Reset textY
        textY = frameY + gamePanel.tileSize;
        String value;

        value = String.valueOf(gamePanel.player.level);
        textX = getXforAlignToRIghtText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;
        value = String.valueOf(gamePanel.player.life + "/" + gamePanel.player.maxLife);
        textX = getXforAlignToRIghtText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;
        value = String.valueOf(gamePanel.player.mana + "/" + gamePanel.player.maxMana);
        textX = getXforAlignToRIghtText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;
        value = String.valueOf(gamePanel.player.strength);
        textX = getXforAlignToRIghtText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;
        value = String.valueOf(gamePanel.player.dexterity);
        textX = getXforAlignToRIghtText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;
        value = String.valueOf(gamePanel.player.attack);
        textX = getXforAlignToRIghtText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;
        value = String.valueOf(gamePanel.player.defense);
        textX = getXforAlignToRIghtText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;
        value = String.valueOf(gamePanel.player.exp);
        textX = getXforAlignToRIghtText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;
        value = String.valueOf(gamePanel.player.nextLevelExp);
        textX = getXforAlignToRIghtText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;
        value = String.valueOf(gamePanel.player.coin);
        textX = getXforAlignToRIghtText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight + 5;

        // Draw weapon image
        g2.drawImage(gamePanel.player.currentWeapon.down1, tailX - gamePanel.tileSize, textY - 15, null);
        textY += gamePanel.tileSize;
        g2.drawImage(gamePanel.player.currentShield.down1, tailX - gamePanel.tileSize, textY - 15, null);
    }

    public void drawPlayerLife(){
        int x = gamePanel.tileSize / 2;
        int y = gamePanel.tileSize / 2;
        int i = 0;
        // Draw Max Life
        while(i < gamePanel.player.maxLife / 2){
            g2.drawImage(heart_blank, x, y, null);
            i++;
            x += gamePanel.tileSize;
        }

        // Reset
        x = gamePanel.tileSize / 2;
        y = gamePanel.tileSize / 2;
        i = 0;

        // Draw current life
        while(i < gamePanel.player.life){
            g2.drawImage(heart_half, x, y, null);
            i++;
            if(i < gamePanel.player.life){
                g2.drawImage(heart_full, x, y, null);
            }
            i++;
            x += gamePanel.tileSize;
        }

        // Draw max mana
        x = gamePanel.tileSize /2 - 5;
        y = (int)(gamePanel.tileSize * 1.5);
        i = 0;
        while(i < gamePanel.player.maxMana){
            g2.drawImage(crystal_blank, x, y, null);
            i++;
            x += 35;
        }
        // Draw current mana
        x = gamePanel.tileSize /2 - 5;
        i = 0;
        while(i < gamePanel.player.mana){
            g2.drawImage(crystal_full, x, y, null);
            i++;
            x += 35;
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

    private int getXforAlignToRIghtText(String text, int tailX){
        int textLength = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = tailX - textLength;

        return x;
    }
    public void drawPlayScreen(){

    }
}
