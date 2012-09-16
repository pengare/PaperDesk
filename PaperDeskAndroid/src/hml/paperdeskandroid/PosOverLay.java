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
	// �����PosOverLay�����Ƶ�λͼ
	Bitmap posBitmap;
	// �����PosOverLay����λͼ��λ��
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
			// ��ȡMapView��Projection����
			Projection proj = mapView.getProjection();
			Point p = new Point();
			// ����ʵ�ĵ�������ת��Ϊ��Ļ�ϵ�����
			for(int i = 0; i < gp.length; ++i)
			{
				proj.toPixels(gp[i], p);
				// ��ָ��λ�û���ͼƬ
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
