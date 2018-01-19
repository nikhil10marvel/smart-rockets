package io.smart.rockets;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import io.csv.BIn;
import io.genetic.guess.Utils;
import io.smart.genetic.RocketPopulation;
import io.smart.util.StageLoader;

public class Game extends ApplicationAdapter {
	private ShapeRenderer batch;
	private RocketPopulation rocketPopulation;
	private Vector2 target;
	static Block[] blocks;
	static Pad[] pads;
	private StageLoader loader;
    //BitmapFont font;

	public static String file_name_local;

	@Override
	public void create () {
		batch = new ShapeRenderer();
        loader = StageLoader.getStage(file_name_local);
        loader.load();
        blocks = loader.getLoadedBlocks();
        pads = loader.getLoaddedPads();
        target = loader.getTarget_pos();
		//target = new Vector2(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()-32);
        //font = new BitmapFont();
		Rocket.setTarget(target);
		rocketPopulation = new RocketPopulation(target, loader.getStart_pos(),25, 0.01f);
		Utils.createdump();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		batch.setAutoShapeType(true);
		batch.begin();
		//font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), Gdx.graphics.getWidth(), Gdx.graphics.getHeight() - 5);
        batch.set(ShapeRenderer.ShapeType.Filled);
        batch.setColor(Color.SKY);
		batch.ellipse(target.x, target.y, 16, 16);
		rocketPopulation.render(batch);
		for(Block block : blocks){
		    block.render(batch);
        }
        for(Pad pad : pads){
            pad.render(batch);
        }
		batch.end();
		rocketPopulation.tick(Gdx.graphics.getDeltaTime());
		Gdx.graphics.setTitle("Smart Rockets  FPS: " + Gdx.graphics.getFramesPerSecond());
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		Utils.closeDump();
		loader.cleanup();
		BIn.getSerializer(Gdx.files.local("fitness.map.bin"), RocketPopulation.maxfitness, false);
	}
}
