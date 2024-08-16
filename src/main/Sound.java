package main;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.io.IOException;
import java.net.URL;

public class Sound {
    Clip clip;
    URL soundURL[] = new URL[30];
    FloatControl fc;
    int volumnScale = 3;
    float volumn;

    public Sound(){
        soundURL[0] = getClass().getResource("/sound/BlueBoyAdventure.wav");
        soundURL[1] = getClass().getResource("/sound/coin.wav");
        soundURL[2] = getClass().getResource("/sound/powerup.wav");
        soundURL[3] = getClass().getResource("/sound/unlock.wav");
        soundURL[4] = getClass().getResource("/sound/fanfare.wav");
        soundURL[5] = getClass().getResource("/sound/hitmonster.wav");
        soundURL[6] = getClass().getResource("/sound/receivedamage.wav");
        soundURL[8] = getClass().getResource("/sound/levelup.wav");
        soundURL[9] = getClass().getResource("/sound/cursor.wav");
        soundURL[10] = getClass().getResource("/sound/burning.wav");
        soundURL[11] = getClass().getResource("/sound/cuttree.wav");
    }

    public void setFile(int i){
        try{
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundURL[i]);
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            fc = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            checkVolumn();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void play(){
        clip.start();
    }

    public void loop(){
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stop(){
        clip.stop();
    }

    public void checkVolumn(){
        switch (volumnScale){
            case 0: volumn = -80f; break;
            case 1: volumn = -20f; break;
            case 2: volumn = -12f; break;
            case 3: volumn = -5f; break;
            case 4: volumn = 1f; break;
            case 5: volumn = 6f; break;
        }
        fc.setValue(volumn);
    }
}
