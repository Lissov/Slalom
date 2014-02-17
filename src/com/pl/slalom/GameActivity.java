package com.pl.slalom;
import android.app.*;
import android.os.*;
import android.widget.*;
import com.pl.slalom.graphics.*;
import com.pl.slalom.player.*;
import android.view.*;
import android.graphics.*;

public class GameActivity extends Activity implements ICommandHandler
{
	private Game game;
	private SlopeView slopeView;
	private ControlsView controlsView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.game);
		
		int levelnum = getIntent().getIntExtra(Constants.Extra_LevelNumber, 1);
		int skinum = getIntent().getIntExtra(Constants.Extra_SkiNumber, 0);
		
		game = new Game(levelnum, skinum);
		
		LinearLayout llSlope = (LinearLayout)findViewById(R.id.llSlope);
		slopeView = new SlopeView(this, game, Constants.controlsOnSlope, this);
		llSlope.addView(slopeView);
		
		LinearLayout llControls = (LinearLayout)findViewById(R.id.llControls);
		controlsView = new ControlsView(this, game, this);
		llControls.addView(controlsView);
	}

	@Override
	public void onMove(int dx, int dy)
	{
		game.makeMove(dx, dy);
		
		slopeView.invalidate();
		controlsView.invalidate();
	}
}
