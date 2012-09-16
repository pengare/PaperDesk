package hml.paperdeskandroid;

import android.R.integer;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.view.MotionEvent;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

public class PosOverLay extends Overlay {
	// 定义该PosOverLay所绘制的位图
	Bitmap posBitmap;
	// 定义该PosOverLay绘制位图的位置
	GeoPoint[] gp;
	public PosOverLay(GeoPoint[] gp, Bitmap posBitmap)
	{
		super();
		this.gp = gp;
		this.posBitmap = posBitmap;
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
			for(int i = 0; i < gp.length; ++i)
			{
				proj.toPixels(gp[i], p);
				// 在指定位置绘制图片
				canvas.drawBitmap(posBitmap, p.x - posBitmap.getWidth() / 2
						, p.y - posBitmap.getHeight(), null);
			}
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent e, MapView mapView) {
		if(e.getAction() == MotionEvent.ACTION_UP)
		{
			MapMasterActivity.bCommandChanged = true;
		}
		return super.onTouchEvent(e, mapView);
	}
	
	
	
}
