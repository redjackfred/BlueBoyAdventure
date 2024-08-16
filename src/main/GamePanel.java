package main;

import Entity.Entity;
import Entity.Player;
import tile.TileManager;
import tile_interactive.InteractiveTile;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class GamePanel extends JPanel implements Runnable{
    // Screen settings
    final int originalTileSize = 16; // 16x16 tile
    final int scale = 3;

    public final int tileSize = originalTileSize * scale;
    final int maxScreenCol = 16;
    final int maxScreenRow = 12;
    final int screenWidth = tileSize * maxScreenCol; // 768 pixels
    final int screenHeight = tileSize * maxScreenRow; // 576 pixels

    // World settings
    final int maxWorldCol = 50;
    final int maxWorldRow = 50;

    int FPS = 60;


    TileManager tileManager = new TileManager(this);
    public EventHandler eventHandler = new EventHandler(this);
    Thread gameThread;
    public KeyHandler keyHandler = new KeyHandler(this);
    public UI ui = new UI(this);
    Sound music = new Sound();
    Sound soundEffect = new Sound();
    public AssetSetter assetSetter= new AssetSetter(this);
    public CollisionChecker collisionChecker = new CollisionChecker(this);
    public Player player = new Player(this, keyHandler);
    public Entity[] obj = new Entity[20];
    public Entity[] npc = new Entity[10];
    public Entity[] monster = new Entity[20];
    public InteractiveTile[] iTile = new InteractiveTile[50];
    ArrayList<Entity> entityList = new ArrayList<>();
    public ArrayList<Entity> projectileList = new ArrayList<>();
    public ArrayList<Entity> particleList = new ArrayList<>();

    // Game State
    public int gameState;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int dialogState = 3;
    public final int titleState = 0;
    public final int characterState = 4;
    public final int optionState = 5;


    public int getMaxScreenCol() {
        return maxScreenCol;
    }

    public int getMaxScreenRow() {
        return maxScreenRow;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public int getMaxWorldCol() {
        return maxWorldCol;
    }

    public int getMaxWorldRow() {
        return maxWorldRow;
    }

    public GamePanel(){
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.setFocusable(true);
    }

    public void setUpGame(){
        assetSetter.setObject();
        assetSetter.setNPC();
        assetSetter.setMonster();
        assetSetter.setInteractiveTile();
//        playMusic(0);
        gameState = titleState;
    }

    public void startGameThread(){
        gameThread = new Thread(this);
        gameThread.start();
    }

    // (Sleep method)
//    @Override
//    public void run() {
//        double drawInterval = 1E9 / FPS;  // 0.01666 seconds
//        double nextDrawTime = System.nanoTime() + drawInterval;
//
//        while(gameThread != null){
//            // 1. Update information such as character position
//            update();
//
//            // 2. Draw the screen with the updated information
//            repaint();
//
//            // Pause Game loop until next frame (Sleep method)
//            double remainingTime = nextDrawTime - System.nanoTime();
//            // Change to millisecond for Thread.sleep method
//            remainingTime = remainingTime / 1e6;
//            // In case that there is no more remaining time
//            if(remainingTime < 0){
//                remainingTime = 0;
//            }
//            try {
//                Thread.sleep((long)remainingTime);
//                nextDrawTime += drawInterval;
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//        }
//    }

    @Override
    public void run(){
        double drawInterval = 1e9 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;

        while(gameThread != null){
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;
            if(delta >= 1){
                update();
                repaint();
                delta--;
                drawCount++;
            }

            if(timer >= 1e9){
                System.out.println("FPS:" + drawCount);
                drawCount = 0;
                timer = 0;
            }
        }
    }

    public void update(){
        if(gameState == playState) {
            player.update();
            for(int i = 0; i < npc.length; i++){
                if(npc[i] != null){
                    npc[i].update();
                }
            }
            for(int i = 0; i < monster.length; i++){
                if(monster[i] != null){
                    if(monster[i].alive){
                        monster[i].update();
                    }else{
                        monster[i].checkDrop();
                        monster[i] = null;
                    }

                }
            }
            for(int i = 0; i < projectileList.size(); i++){
                if(projectileList.get(i) != null){
                    if(projectileList.get(i).alive){
                        projectileList.get(i).update();
                    }else{
                        projectileList.remove(i);
                    }

                }
            }
            for(int i = 0; i < particleList.size(); i++){
                if(particleList.get(i) != null){
                    if(particleList.get(i).alive){
                        particleList.get(i).update();
                    }else{
                        particleList.remove(i);
                    }

                }
            }
            for(int i = 0; i < iTile.length; i++){
                if(iTile[i] != null){
                    iTile[i].update();
                }
            }
        }
        if(gameState == pauseState){
            // nothing
        }
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Debug
        long drawStart = 0;
        if(keyHandler.showDebugText) {
            drawStart = System.nanoTime();
        }

        // Title Screen
        if(gameState == titleState){
            ui.draw(g2);
        }else {
            // Tile
            tileManager.draw(g2);

            // Interactive tiles
            for(int i = 0; i < iTile.length; i++){
                if(iTile[i] != null){
                    iTile[i].draw(g2);
                }
            }

            // Add all Entities to the list
            entityList.add(player);
            for(int i = 0; i < npc.length; i++){
                if(npc[i] != null){
                    entityList.add(npc[i]);
                }
            }
            for(int i = 0; i < obj.length; i++){
                if(obj[i] != null){
                    entityList.add(obj[i]);
                }
            }
            for(int i = 0; i < monster.length; i++){
                if(monster[i] != null){
                    entityList.add(monster[i]);
                }
            }
            for(int i = 0; i < projectileList.size(); i++){
                if(projectileList.get(i) != null){
                    entityList.add(projectileList.get(i));
                }
            }
            for(int i = 0; i < particleList.size(); i++){
                if(particleList.get(i) != null){
                    entityList.add(particleList.get(i));
                }
            }


            // Sort
            Collections.sort(entityList, new Comparator<Entity>(){
                @Override
                public int compare(Entity o1, Entity o2){
                    int result = Integer.compare(o1.worldY, o2.worldY);
                    return result;
                }
            });

            // Draw Entities
            for(int i = 0; i < entityList.size(); i++){
                entityList.get(i).draw(g2);
            }

            entityList.clear();

            // UI
            ui.draw(g2);
        }

        if(keyHandler.showDebugText) {
            long drawEnd = System.nanoTime();
            long passedTime = drawEnd - drawStart;

            g2.setFont(new Font("Arial", Font.PLAIN, 20));
            g2.setColor(Color.white);
            int x = 10;
            int y = 400;
            int lineHeight = 20;

            g2.drawString("WorldX: " + player.worldX, x, y);
            y += lineHeight;
            g2.drawString("WorldY: " + player.worldY, x, y);
            y += lineHeight;
            g2.drawString("Col: " + (player.worldX + player.solidArea.x) / tileSize, x, y);
            y += lineHeight;
            g2.drawString("Row: " + (player.worldY + player.solidArea.y) / tileSize, x, y);
            y += lineHeight;
            g2.drawString("Draw Time: " + passedTime, x, y);

        }

        // To save some memory usage
        g2.dispose();
    }

    public void playMusic(int i){
        music.setFile(i);
        music.play();
        music.loop();
    }

    public void stopMusic(){
        music.stop();
    }

    public void playSE(int i){
        soundEffect.setFile(i);
        soundEffect.play();
    }
}
