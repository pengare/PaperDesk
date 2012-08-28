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
	// �����PosOverLay�����Ƶ�λͼ
	Bitmap posBitmap;
	// �����PosOverLay����λͼ��λ��
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
			// ��ȡMapView��Projection����
			Projection proj = mapView.getProjection();
			Point p = new Point();
			// ����ʵ�ĵ�������ת��Ϊ��Ļ�ϵ�����
			proj.toPixels(gp, p);
			// ��ָ��λ�û���ͼƬ
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
