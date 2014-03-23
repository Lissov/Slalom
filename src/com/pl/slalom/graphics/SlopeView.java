package com.pl.slalom.graphics;
import android.view.*;
import android.view.animation.Animation;
import android.graphics.*;
import android.widget.*;
import com.pl.slalom.track.*;
import com.pl.slalom.*;
import com.pl.slalom.player.*;
import java.util.*;
import android.app.*;

public class SlopeView extends View
{
	private Activity context;
	private ICommandHandler cmdHandler;
	private Game game;
	private Slope slope;
	private Route route;
	private int canvasWidth = 1000;
	private float borderRel = 0.1f;
	private float[] treeOffsets;
	
	private boolean drawNextStep;
	
	private float yProgress = -2.5f;
	
	private ICoordsTransform coordsTransform;
	
	private TreeDrawer treeDrawer = new TreeDrawer();
	private GateDrawer gateDrawer = new GateDrawer();
	private FlagDrawer flagDrawer = new FlagDrawer();
	private TramplinDrawer tramplinDrawer = new TramplinDrawer();
	private FieldDrawer fieldDrawer = new FieldDrawer();
	private PlayerDrawer playerDrawer = new PlayerDrawer();
	private RouteDrawer routeDrawer = new RouteDrawer();
	private PassDrawer passDrawer = new PassDrawer();
	private NextMoveDrawer moveDrawer = new NextMoveDrawer();
	private MakingMoveDrawer makeMoveDrawer = new MakingMoveDrawer();
	
	private String textStart;
	private String textFinish;
	private String textGo;
	private Timer updateTimer;
	
	public SlopeView(Activity context, Game game, boolean drawNextStep, ICommandHandler cmdHandler){
		super(context);
		this.slope = game.slope;
		this.route = game.route;
		this.game = game;
		this.context = context;
		this.drawNextStep = drawNextStep;
		this.cmdHandler = cmdHandler;
		
		initBorderTrees();

		textStart = getResources().getString(R.string.flag_start);
		textFinish = getResources().getString(R.string.flag_finish);
		textGo = getResources().getString(R.string.flag_go);
		
		yProgress = -0.065f * game.slope.width;

	}
	
	public void resume(){
		if (updateTimer == null){
			updateTimer = new Timer("sloperefresher");
		}

		updateTimer.schedule(new TimerTask() {
			public void run() {
				redraw();
			}
		}, 1000, 40);		
	}
	
	public void stop(){
		updateTimer.cancel();
	}

	private void redraw(){
		context.runOnUiThread(new Runnable(){ 
				public void run() { 
					updateProgress();	
					invalidate(); 
				} 
			});
	}
	
	private float screenSpeed = 0;
	private long lastUpdate = 0;
	private void updateProgress(){
		if (lastUpdate == 0){
			lastUpdate = System.currentTimeMillis();
			return;
		}
		
		float minY = coordsTransform.getMinYVisible();
		float maxY = coordsTransform.getMaxYVisible();
		float maxF = slope.gates[slope.gates.length - 1].position;
		/*if (minY < 0 && maxY > maxF)
			return;*/
	
		float cp = route.positionsY[route.currentPosition];
		float scrS = (maxY - minY);
		float noMove = scrS / 10;
		if (cp - minY < (noMove)) 
		{
			screenSpeed = 0;
			return;
		}
		
		screenSpeed = (float)Math.pow(3 * (cp - minY - noMove) / scrS, 3);
		if (maxY > maxF){
			screenSpeed *= (maxF + 0.2f*scrS - maxY) / (0.2f*scrS);
		}
		
		long nt = System.currentTimeMillis();
		float dy = screenSpeed * (nt - lastUpdate) / 1000;
		if (dy > 1) dy = 1;
		yProgress += dy;
		lastUpdate = nt;
	}
	
	private void initBorderTrees(){
		treeOffsets = new float[10];
		treeOffsets[0] = 0;
		treeOffsets[1] = 0.01f;
		treeOffsets[2] = 0.01f;
		treeOffsets[3] = 0;
		treeOffsets[4] = -0.01f;
		treeOffsets[5] = 0.05f;
		treeOffsets[6] = 0.02f;
		treeOffsets[7] = -0.02f;
		treeOffsets[8] = 0;
		treeOffsets[9] = 0.02f;
	}
	
	@Override
	protected void onDraw(Canvas canvas)
	{
		try{
			init(canvas);

			drawField(canvas);
			drawTramplins(canvas);
			if (drawNextStep)
				drawMove(canvas);
			drawRoute(canvas);
			drawBorders(canvas);
			drawGates(canvas);
			drawMakingMove(canvas);
			
		} catch (Exception ex){
			Toast.makeText(context, "Error SV2: " + ex.getMessage(), Toast.LENGTH_LONG).show();
		}
	}
	
	private void init(Canvas canvas){
		float canvasHeight = this.getHeight();
		float canvasWidth = this.getWidth();
		float borderAbs = (canvasWidth * borderRel);
		float scale = (canvasWidth - 2 * borderAbs) / slope.width;
		
		coordsTransform = new StraightCoordsTransform(scale, borderAbs, canvasHeight);
		//coordsTransform = new PerspectiveCoordsTransform(scale, borderAbs, canvasHeight, canvasWidth);
		coordsTransform.setYProgress(yProgress);
		
		fieldDrawer.init(context, coordsTransform);
		treeDrawer.init(context, coordsTransform);
		gateDrawer.init(context, coordsTransform);
		flagDrawer.init(context, coordsTransform);
		tramplinDrawer.init(context, coordsTransform);
		playerDrawer.init(context, coordsTransform);
		routeDrawer.init(context, coordsTransform);
		passDrawer.init(context, coordsTransform);
		moveDrawer.init(context, coordsTransform);
		makeMoveDrawer.init(context, coordsTransform);
	}
	
	private void drawMakingMove(Canvas canvas){
		if (game.makingMove){
			float progress = game.getMovePercentage();
			makeMoveDrawer.draw(canvas,
								route.positionsX[route.currentPosition],
								route.positionsY[route.currentPosition],
								game.makingX, game.makingY, progress);
		}
	}
	
	private void drawMove(Canvas canvas)
	{
		boolean[][] posMoves = game.getPossibleMoves();
		int cx = route.positionsX[route.currentPosition];
		int cy = route.positionsY[route.currentPosition];
		Point lm = route.getLastMove();
		moveDrawer.draw(canvas, cx, cy, cx + lm.x, cy + lm.y,
						posMoves);
	}

	private void drawField(Canvas canvas)
	{
		fieldDrawer.drawField(canvas, slope.width);
	}
	
	private void drawGates(Canvas canvas)
	{
		for (int i = 0; i < slope.gates.length; i++){
			Gate g = slope.gates[i];
			switch (g.gateType){
				case Start:
					gateDrawer.draw(canvas, g.leftPos, g.rightPos, g.position, g.gateType, textStart, 0);
					break;
				case Finish:
					gateDrawer.draw(canvas, g.leftPos, g.rightPos, g.position, g.gateType, textFinish, 0);
					break;
				case Banner:
					gateDrawer.draw(canvas, g.leftPos, g.rightPos, g.position, g.gateType, textGo, 0);
					break;
				case SmallBanners:
					gateDrawer.draw(canvas, g.leftPos - 0.3f, g.leftPos, g.position, g.gateType, "", i % 2);
					gateDrawer.draw(canvas, g.rightPos, g.rightPos + 0.3f, g.position, g.gateType, "", i % 2);
					break;
				case Flags:
					flagDrawer.draw(canvas, g.leftPos, g.position, true, i % 2);
					flagDrawer.draw(canvas, g.rightPos, g.position, false, i % 2);
					break;
			}
			
			if (i < route.passedCount)
				passDrawer.drawPass(canvas, g.position, g.leftPos, g.rightPos);
		} 
	}
	
	private void drawTramplins(Canvas canvas){
		for (int i = 0; i < slope.tramplins.length; i++) {
			Tramplin t = slope.tramplins[i];
			tramplinDrawer.draw(canvas, t.position, t.left, t.right, t.power);
		}
	}
		
	private void drawBorders(Canvas canvas){
		float maxY = coordsTransform.getMaxYVisible();
		float minY = coordsTransform.getMinYVisible();
		for (int y = (int)maxY + 1; y >= minY; y--){
			int i = (y % 10 + 10) % 10;
			treeDrawer.draw(canvas, 0 + treeOffsets[i], y);
			treeDrawer.draw(canvas, slope.width - treeOffsets[i], y);
		}
	}
	
	private void drawRoute(Canvas canvas){
		int cp = route.currentPosition;
		
		for (int i = 0; i < cp; i++){
			playerDrawer.draw(canvas, route.positionsX[i], route.positionsY[i], false);
			routeDrawer.draw(canvas, 
							 route.positionsX[i], route.positionsY[i],
							 route.positionsX[i+1], route.positionsY[i+1]);
		}
		
		playerDrawer.draw(canvas, route.positionsX[cp], route.positionsY[cp], true);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		try{
			if (event.getAction() == MotionEvent.ACTION_DOWN){
				Point touched = getTouchedNextMove(event.getX(), event.getY());

				if (touched == null)
					return true;

				cmdHandler.onMove(touched.x, touched.y);

				return true;
			}

			return super.onTouchEvent(event);
		} catch(Exception ex){
			Toast.makeText(context, "Error SV1: " + ex.getMessage(), Toast.LENGTH_LONG).show();
			return false;
		}
	}
	
	private Point getTouchedNextMove(float x, float y){
		PointF point = coordsTransform.toFieldPoint(x, y);
		int xs = Math.round(point.x);
		int ys = Math.round(point.y);
		
		if ((point.x - xs)*(point.x - xs) + (point.y - ys)*(point.y - ys) > Constants.moveClickR2)
		{
			return null;
		}
		
		Point lm = route.getLastMove();
		xs = xs - route.positionsX[route.currentPosition] - lm.x;
		ys = ys - route.positionsY[route.currentPosition] - lm.y;
		
		int m = Constants.MaxPossibleMove;
		if (xs < -m  || xs > m || ys < -m || ys > m)
			return null;

		boolean[][] posMoves = game.getPossibleMoves();
		if (!posMoves[xs + m][ys + m])
			return null;
			
		return new Point(xs + lm.x, ys + lm.y);
	}
}
