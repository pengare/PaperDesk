package hml.paperdeskandroid;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.view.MotionEvent;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

public class PosOverLay extends Overlay {
	// 定义该PosOverLay所绘制的位图
	Bitmap posBitmap;
	// 定义该PosOverLay绘制位图的位置
	GeoPoint gp;
	
	//Base context(map view), used to show toast
	MapMasterActivity context;
	
	public PosOverLay(GeoPoint gp, Bitmap posBitmap, MapMasterActivity context)
	{
		super();
		this.gp = gp;
		this.posBitmap = posBitmap;
		this.context = context;
	}

	@Override
	public void draw(Canvas canvas, MapView mapView
		, boolean shadow)
	{
		if (!shadow)
		{
			// 获取MapView的Projection对象
			Projection proj = mapView.getProjection();
			Point p = new Point();
			// 将真实的地理坐标转化为屏幕上的坐标
			proj.toPixels(gp, p);
			// 在指定位置绘制图片
			canvas.drawBitmap(posBitmap, p.x - posBitmap.getWidth() / 2
				, p.y - posBitmap.getHeight(), null);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent e, MapView mapView) {
		// TODO Auto-generated method stub
		//return super.onTouchEvent(e, mapView);
		
		if(e.getAction() == 1)
		{
			//GeoPoint p = mapView.getProjection()e;
			Toast.makeText(context, "x: "+ e.getX() + "\ny: "+e.getY(), Toast.LENGTH_SHORT).show();
			context.bCommandChanged = true;
		}
		return false;
	}
	
}
