package com.pl.slalom.graphics;
import android.view.*;
import android.graphics.*;
import android.content.*;
import android.widget.*;
import com.pl.slalom.track.*;
import com.pl.slalom.*;
import com.pl.slalom.player.*;

public class SlopeView extends View
{
	private Context context;
	private Slope slope;
	private Route route;
	private int canvasWidth = 1000;
	private float borderRel = 0.1f;
	private float[] treeOffsets;
	
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
	
	private String textStart;
	private String textFinish;
	private String textGo;
	
	public SlopeView(Context context, Slope slope, Route route){
		super(context);
		this.slope = slope;
		this.route = route;
		this.context = context;
		
		initBorderTrees();

		textStart = getResources().getString(R.string.flag_start);
		textFinish = getResources().getString(R.string.flag_finish);
		textGo = getResources().getString(R.string.flag_go);
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
			drawRoute(canvas);
			drawBorders(canvas);
			drawGates(canvas);
			
		} catch (Exception ex){
			Toast.makeText(context, "Error: " + ex.getMessage(), Toast.LENGTH_LONG).show();
		}
	}
	
	private void init(Canvas canvas){
		float canvasHeight = this.getHeight();
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
}
