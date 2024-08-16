package main;

import java.io.*;

public class Config {
    GamePanel gp;

    public Config(GamePanel gp){
        this.gp = gp;
    }

    public void saveConfig(){
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("config.txt"));

            // Music volumn
            bw.write(String.valueOf(gp.music.volumnScale));
            bw.newLine();

            // SE volumn
            bw.write(String.valueOf(gp.soundEffect.volumnScale));
            bw.newLine();

            bw.close();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void loadConfig(){
        try {
            BufferedReader br = new BufferedReader(new FileReader("config.txt"));

            // Music volumn
            String s = br.readLine();
            gp.music.volumnScale = Integer.valueOf(s);

            // SE volumn
            s = br.readLine();
            gp.soundEffect.volumnScale = Integer.valueOf(s);

            br.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
