package com.pl.slalom.graphics;
import android.view.*;
import android.graphics.*;
import com.pl.slalom.player.*;
import com.pl.slalom.*;
import com.pl.slalom.graphics.controls.*;
import java.util.*;
import android.app.*;

public class ControlsView extends View
{
	private Activity context;
	private Game game;
	ICommandHandler cmdHandler;
	private float btnSize;
	private float baseX;
	private float baseY;
	private ButtonMoveDrawer buttonMoveDrawer = new ButtonMoveDrawer();
	private LastMoveDrawer lastMoveDrawer = new LastMoveDrawer();
	
	private float textSize;
	private Paint paintTextT = new Paint();
	private Paint paintTextN = new Paint();
	private Paint paintTextG = new Paint();
	private Paint paintTextB = new Paint();
	String textStart;
	String textRace;
	String textFinished;
	String textFailed;
	String textGates;
	String textTurns;
	String textTime;
	
	public ControlsView(Activity context, Game game, ICommandHandler handler){
		super(context);
		this.context = context;
		this.game = game;
		this.cmdHandler = handler;
		
		Timer t = new Timer("refresher");
		t.schedule(new TimerTask(){
			public void run() { redraw(); }
		}, 1000, 100);
	}
	
	private void redraw(){
		context.runOnUiThread(new Runnable(){ 
			public void run() { invalidate(); } });
	}
	
	@Override
	public void draw(Canvas canvas)
	{
		if (!inited)
			init(canvas);
			
		drawMoves(canvas);
		drawStatus(canvas);
	}

	private void drawStatus(Canvas canvas){
		if (game.route.currentPosition == 0)
			canvas.drawText(textStart, textSize * 0.5f, textSize * 1.5f, paintTextN);
		else{
			if (game.failed)		
				canvas.drawText(textFailed, textSize * 0.5f, textSize * 1.5f, paintTextB);
			else{
				if (game.finished)		
					canvas.drawText(textFinished, textSize * 0.5f, textSize * 1.5f, paintTextG);
				else
				{
					canvas.drawText(textGates, textSize * 0.5f, textSize * 1.5f, paintTextT);
					canvas.drawText("" + (game.route.passedCount - 1), textSize * 7f, textSize * 1.5f, paintTextG);
				}
			}
		}
		
		canvas.drawText(textTurns, textSize * 0.5f, textSize * 3f, paintTextT);
		canvas.drawText(textTime, textSize * 0.5f, textSize * 4f, paintTextT);
		
		canvas.drawText("" + game.route.currentPosition, textSize * 4f, textSize * 3f, paintTextN);
		canvas.drawText("" + game.getTime(), textSize * 4f, textSize * 4f, paintTextN);		
	}
	
	private void drawMoves(Canvas canvas){ 	
		int mm = Constants.MaxPossibleMove;

		Point lastM = game.route.getLastMove();
		if (game.route.currentPosition > 0){
			lastMoveDrawer.draw(canvas, 
								getControlX(-lastM.x), getControlY(-lastM.y),
								getControlX(0), getControlY(0));
		}
							
		boolean[][] posMoves = game.getPossibleMoves();
		
		int maxX = lastM.x > 0 ? lastM.x + mm : -lastM.x + mm;
		int maxY = lastM.y > 0 ? lastM.y + mm : -lastM.y + mm;
		int maxM = maxX > maxY ? maxX : maxY;
		
		for (int x = -mm; x <= mm; x++)
			for (int y = -mm; y <= mm; y++)
			{
				if (!isInMoveBounds(x,y))
					continue;
					
				buttonMoveDrawer.draw(canvas,
									  getControlX(x), getControlY(y),
									  posMoves[x + mm][y + mm],
									  lastM.x + x, lastM.y + y, maxM);
			}
	}
	
	private float getControlX(int x){
		return baseX + (x + Constants.MaxPossibleMove + 0.5f) * btnSize;
	}
	
	private float getControlY(int y){
		return baseY - (y + Constants.MaxPossibleMove + 0.5f) * btnSize;
	}
	
	private boolean isInMoveBounds(int x, int y){
		if (Math.abs(x) + Math.abs(y)
				<= Constants.MaxPossibleMove)
			return true;
			
		return false;
	}
	
	private boolean inited = false;
	private void init(Canvas canvas){
		float w = this.getWidth();
		float h = this.getHeight();
		float s = w > h ? h : w;

		int mc = 2 * Constants.MaxPossibleMove + 1;
		btnSize = s / (mc);
		baseX = (w - s) / 2;
		baseY = (h + s) / 2;

		buttonMoveDrawer.initialize(context, btnSize);
		lastMoveDrawer.initialize(context, btnSize);
		
		float textA = s/4f + (w>h?w:h);
		if (textA > w) textA = w;
		if (textA > h) textA = textA - s/4f;
		textSize = textA / 20f;
		paintTextT.setColor(getResources().getColor(R.color.controlStatusText));
		paintTextN.setColor(getResources().getColor(R.color.controlStatusNeutral));
		paintTextG.setColor(getResources().getColor(R.color.controlStatusGood));
		paintTextB.setColor(getResources().getColor(R.color.controlStatusBad));
		paintTextT.setTextSize(textSize);
		paintTextN.setTextSize(textSize);
		paintTextG.setTextSize(textSize);
		paintTextB.setTextSize(textSize);
		
		textStart = getResources().getString(R.string.status_start);
		textRace = getResources().getString(R.string.status_race);
		textFailed = getResources().getString(R.string.status_failed);
		textFinished = getResources().getString(R.string.status_finished);
		textGates = getResources().getString(R.string.status_gates);
		textTurns = getResources().getString(R.string.status_turns);
		textTime = getResources().getString(R.string.status_time);
		inited = true;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		if (event.getAction() == MotionEvent.ACTION_DOWN){
			Point touched = getTouchedControl(event.getX(), event.getY());
			
			if (touched == null)
				 return true;
				 
			int m = Constants.MaxPossibleMove;
			boolean[][] posMoves = game.getPossibleMoves();
			if (!posMoves[touched.x + m][touched.y + m])
				return true;
			
			Point lm = game.route.getLastMove();
			cmdHandler.onMove(touched.x + lm.x, touched.y + lm.y);
			
			return true;
		}

		return super.onTouchEvent(event);
	}

	private Point getTouchedControl(float x, float y){
		
		int mc = Constants.MaxPossibleMove;
		
		for (int i = -mc; i <= mc; i++)
			for (int j = -mc; j <= mc; j++)
			{
				if (!isInMoveBounds(i,j))
					continue;

				float cx = getControlX(i);
				float cy = getControlY(j);

				if ((cx - x) * (cx - x) + (cy - y) * (cy - y) < 0.5f * btnSize * btnSize)
				{
					return new Point(i, j);
				}
			}

		return null;
	}
}
