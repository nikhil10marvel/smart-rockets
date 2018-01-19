package io.smart.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import io.smart.rockets.Block;
import io.smart.rockets.Pad;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class StageLoader {

    private InputStream inputStream;
    private URL url;
    private BufferedImage img;

    ArrayList<Block> blocks = new ArrayList<>();
    ArrayList<Pad> pads = new ArrayList<>();
    Vector2 start_pos;
    Vector2 target_pos;

    public static final int SCAN_WIDTH = Gdx.graphics.getWidth() / 32;
    public static final int SCAN_HEIGHT = Gdx.graphics.getHeight() / 32;

    public static StageLoader getStage(String file){
        FileHandle handle = Gdx.files.local(file);
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(handle.file());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return new StageLoader(fis);
    }

    StageLoader(InputStream img){
        try {
            inputStream = img;
            url = null;
            this.img = ImageIO.read(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    StageLoader(URL url){
        try {
            this.url = url;
            this.inputStream = null;
            this.img = ImageIO.read(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * An enum to map colours with game objects
     *
     * Block -> Black
     * Start Position(Rocket) -> Blue
     * Target -> Red
     * Bounce plate -> Green
     */
    enum Palette {

        BLOCK(0,0,0),   // Maps to Black
        START(0,0,255), // Maps to Blue
        TARGET(255,0,0),// Maps to Red
        BOUNCE(0,255,0) // Maps to Green
        ;

        Color color;

        Palette(int r, int g, int b){
            color = new Color(r, g, b);
        }

        Palette(Color color){
            this.color = color;
        }

        Palette(int rgb){
            this.color = new Color(rgb);
        }

        boolean equals(Palette palette){
            return color.equals(palette.color);
        }

        boolean equals(Color color1){
            return color.equals(color1);
        }
    }

    public void load(){
        //System.out.println(img.getWidth() + ":" + img.getHeight());
        for(int xx = 0; xx < SCAN_WIDTH; xx++){
            for(int yy = 0; yy < SCAN_HEIGHT; yy++){
                int rgb = img.getRGB(xx, yy);
                Color color = new Color(rgb);
                if (Palette.BLOCK.equals(color)) {
                    Block newblock = new Block(convertVector(xx * 32, yy * 32));
                    blocks.add(newblock);
                } else if (Palette.START.equals(color)){
                    start_pos = convertVector(xx * 32, yy * 32);
                } else if (Palette.TARGET.equals(color)){
                    target_pos = convertVector(xx * 32, yy * 32);
                } else if (Palette.BOUNCE.equals(color)){
                    Pad newpad = new Pad(convertVector(xx * 32, yy * 32));
                    pads.add(newpad);
                }
            }
        }
    }

    public Block[] getLoadedBlocks(){
        Block[] ret = new Block[blocks.size()];
        blocks.toArray(ret);
        return ret;
    }

    public Pad[] getLoaddedPads(){
        Pad[] ret = new Pad[pads.size()];
        pads.toArray(ret);
        return ret;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public URL getUrl() {
        return url;
    }

    public Vector2 getStart_pos() {
        return start_pos;
    }

    public Vector2 getTarget_pos() {
        return target_pos;
    }

    public static Vector2 convertVector(Vector2 screen){
        Vector2 converted = new Vector2();
        converted.x = screen.x;
        converted.y = Gdx.graphics.getHeight() - 1 -screen.y;
        return converted;
    }

    public static Vector2 convertVector(float x, float y){
        Vector2 converted = new Vector2();
        converted.x = x;
        converted.y = Gdx.graphics.getHeight() - 1 -y;
        //System.out.println(converted);
        return converted;
    }

    public void cleanup(){
        try {
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
