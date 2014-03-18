package com.pl.slalom;
import android.app.*;
import android.os.*;
import android.widget.*;
import com.pl.slalom.graphics.*;
import com.pl.slalom.player.*;
import com.pl.slalom.data.*;
import com.pl.slalom.data.race.*;
import android.content.*;
import android.content.res.*;
import com.pl.slalom.Utility.*;

public class GameActivity extends Activity implements ICommandHandler, IMoveCallback
{
	private Game game;
	private SlopeView slopeView;
	private ControlsView controlsView;
	private RunData runData;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.game);
	
		try{
			int rundataId = getIntent().getIntExtra(Constants.Extra.RunData, -1);
			runData = DataManager.getInstance().getRunData(rundataId);
			
			game = new Game(runData.slopeId, runData.skiId, runData.playerId, this);
		
			LinearLayout llSlope = (LinearLayout)findViewById(R.id.llSlope);
			slopeView = new SlopeView(this, game, Constants.controlsOnSlope, this);
			llSlope.addView(slopeView);
		
			LinearLayout llControls = (LinearLayout)findViewById(R.id.llControls);
			controlsView = new ControlsView(this, game, this);
			llControls.addView(controlsView);
		} catch (Exception ex){
			Toast.makeText(this, "Error GA1: " + ex.getMessage(), Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void onMove(int dx, int dy)
	{
		game.makeMove(dx, dy);
		
		slopeView.invalidate();
		controlsView.invalidate();
	}

	@Override
	public void moveComplete()
	{
		checkGameFinished();
	}

	public void checkGameFinished(){
		if (!(game.finished || game.failed)){
			return;
		}
		
		final Activity a = this;
		AlertDialog ad = new AlertDialog.Builder(this).create();
		final RunStatus status = game.finished ? RunStatus.Finished : RunStatus.Failed;
		final int turns = game.route.currentPosition;
		final float time = game.getTime();
		
		Resources r = getResources();
		String msg = String.format(r.getString(R.string.run_complete1), status);
		ad.setTitle(msg);
		String msg2 = "";
		if (status == RunStatus.Finished){
			msg2 += String.format(r.getString(R.string.run_complete3), turns);
			msg2 += "\n" + String.format(r.getString(R.string.run_complete4), time);
		}
		ad.setMessage(msg2);
		ad.setCancelable(false);
		
		ad.setButton(AlertDialog.BUTTON_POSITIVE, 
			r.getString(R.string.run_completeContinue),
			new DialogInterface.OnClickListener(){ 
				public void onClick(DialogInterface dialog, int which){ 
					RunResult rr = new RunResult(turns, time, status);
					runData.resUpdater.updateResult(rr);
					a.finish();
				}
			}
		);
		ad.show();
	}

	@Override
	public void onBackPressed()
	{
		final Activity a = this;
		AlertDialog ad = new AlertDialog.Builder(this).create();

		Resources r = getResources();
		ad.setTitle(r.getString(R.string.run_back));
		ad.setCancelable(true);

		ad.setButton(AlertDialog.BUTTON_POSITIVE, 
			r.getString(R.string.run_backyes),
			new DialogInterface.OnClickListener(){ 
				public void onClick(DialogInterface dialog, int which){ 
					RunResult rr = new RunResult(-1, -1, RunStatus.Failed);
					runData.resUpdater.updateResult(rr);
					a.finish();
				}
			}
		);
		ad.setButton(AlertDialog.BUTTON_NEUTRAL, 
					 r.getString(R.string.run_backno), 
					 new DialogInterface.OnClickListener(){ 
						public void onClick(DialogInterface dialog, int which){ }
					 }
					);
		ad.show();
	}
}
