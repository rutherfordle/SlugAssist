package slug.project.com;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.annotation.SuppressLint;

import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;

import android.os.Bundle;
//import android.util.Log;

import android.widget.LinearLayout;
import android.widget.Toast;


import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.jsambells.directions.RouteAbstract;
import com.jsambells.directions.ParserAbstract.Mode;
import com.jsambells.directions.RouteAbstract.RoutePathSmoothness;
import com.jsambells.directions.google.DirectionsAPI;
import com.jsambells.directions.google.DirectionsAPIRoute;




public class ShowPath extends MapActivity implements com.jsambells.directions.ParserAbstract.IDirectionsListener{

	//final Context context = this;
	protected MapView map;
	//KEY IS NEED TO USE GOOGLE MAPS
	final String mapKey = "0ffItvm2Kq-2srykW31ZHlSBgS0qOyMdOc2yO0w";
	// Create your own key: @see http://code.google.com/android/add-ons/google-apis/mapkey.html#getdebugfingerprint
	GPSTracker gps;
	int currentLon;
	int currentLat;
    int destLat;
    int destLon;
	//private Button button;
	//private String test;

    
	@SuppressLint("NewApi")
	@SuppressWarnings({ "static-access", "deprecation" })
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		//look at xml from layout
		setContentView(R.layout.route);
		gps = new GPSTracker(ShowPath.this);
		
		Bundle extras=getIntent().getExtras();
		//Intent i = getIntent();
		String location =extras.getString("keyword1");
		String Destination= change_location(location);
		//Destination = change_location(location);
		// getting attached intent data
		Toast.makeText(this,"Begin Route: "+ Destination , Toast.LENGTH_LONG).show();


		/*
        btnShowLocation = (Button) findViewById(R.id.btnShowLocation);

        // show location button click event
        btnShowLocation.setOnClickListener(new View.OnClickListener() {


			@Override
			public void onClick(View arg0) {		
				// create class object
		        gps = new GPSTracker(DirectionsExample.this);

				// check if GPS enabled		
		        if(gps.canGetLocation()){
		        	 //gLat = gps.getLatitude();
		        	 //gLon = gps.getLongitude();
		        	double latitude = gps.getLatitude();
		        	double longitude = gps.getLongitude();

		        	// \n is for new line
		        	Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();	
		        }else{
		        	// can't get location
		        	// GPS or Network is not enabled
		        	// Ask user to enable GPS/network in settings
		        	gps.showSettingsAlert();
		        }

			}
		});
		 */




		map = new MapView(this, mapKey);
		map.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT));
		map.setClickable(true);
		map.setEnabled(true);
		map.setBuiltInZoomControls(true);

		LinearLayout main = (LinearLayout)findViewById(R.id.route);
		main.addView(map);

		MapController mc = map.getController();

		////////////////////Converter: Address to geopoint////////////////////////////////////
		Geocoder gc = new Geocoder(this);

		try{
			//isPresent() works for android 2.3.3 and up 
			if(gc.isPresent()){
				if(gps.canGetLocation()){
					currentLat = (int)(gps.getLatitude()* 1E6);
					currentLon = (int)(gps.getLongitude()* 1E6);

					// \n is for new line
					//Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();	

					//DESTINATION IS RETRIVED FROM PREVIOUS ACTIVITY
					List<Address> list = gc.getFromLocationName(Destination, 1);
					Address address = list.get(0);

					destLat = (int)(address.getLatitude()* 1E6);
					destLon = (int)(address.getLongitude()* 1E6);
				}else{
					// can't get location
					// GPS or Network is not enabled
					// Ask user to enable GPS/network in settings
					gps.showSettingsAlert();
				}
			}
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		//////////////////////////////////////////////////////////////////////////////


		GeoPoint currentLocation=new GeoPoint(currentLat,currentLon);
		GeoPoint destLocation= new GeoPoint(destLat, destLon);
		
		//GeoPoint gp = new GeoPoint((int)(gLat * 1E6), (int)(gLon * 1E6));
		mc.animateTo(currentLocation);
		mc.setZoom(19);
		////////////////////// add marker//////////////////////
		
		List<Overlay> mapOverlays = map.getOverlays();
        Drawable drawable = this.getResources().getDrawable(R.drawable.mark_red);
        
        
        AddItemizedOverlay itemizedOverlay = 
             new AddItemizedOverlay(drawable, this);
       
        
        OverlayItem overlayitem = new OverlayItem(currentLocation, "Hello", "Sample Overlay item");
        
        itemizedOverlay.addOverlay(overlayitem);
        mapOverlays.add(itemizedOverlay);
        
		
		
		// Find a route
		List<GeoPoint> waypoints = new ArrayList<GeoPoint>();

		///////////////////////REAPLACE WITH STUDENT CLASSROOMS//////////////////////
		//RANDOM POINTS IN SANTA CRUZ
		//waypoints.add(gp1);
		waypoints.add(currentLocation);
		waypoints.add(destLocation);
		//waypoints.add(new GeoPoint(37000205,-122062300)); 
		//waypoints.add(new GeoPoint(36999080,-122063875));

		DirectionsAPI directions = new DirectionsAPI();
		directions.getDirectionsThruWaypoints(
				waypoints, 
				DirectionsAPI.Mode.WALKING, 
				this
				);




	}
	/*	 MapActivity */

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}



	public void onDirectionsAvailable(RouteAbstract route, Mode mode) {
		// TODO Auto-generated method stub
		// Add it to a map

		// Add a directions overlay to the map.
		// This is just a quick example to draw the line on the map.
		DirectionsOverlay directions = new DirectionsOverlay();
		directions.setRoute((DirectionsAPIRoute)route);
		map.getOverlays().add(directions);
		map.requestLayout();

	}
	public void onDirectionsNotAvailable() {
		// TODO Auto-generated method stub
		// Show an error?
	}

	public class DirectionsOverlay extends Overlay {

		static final String TAG = "DirectionsOverlay";

		// The route to draw
		private DirectionsAPIRoute mRoute;

		// Our Paint
		Paint pathPaint = new Paint();

		public DirectionsOverlay() {
			this.pathPaint.setAntiAlias(true);
		}

		public DirectionsOverlay setRoute( DirectionsAPIRoute route) {
			this.mRoute = route;
			return this;
		}

		// This function does some fancy drawing
		public void draw(android.graphics.Canvas canvas, MapView mapView, boolean shadow) {

			// This method will be called twice. Once in the 
			// shadow phase, skewed and darkened, then again in 
			// the non-shadow phase. 

			if (this.mRoute != null && !shadow) {

				Path thePath = new Path();

				// Reset our paint. 
				this.pathPaint.setStrokeWidth(3);
				this.pathPaint.setARGB(200, 255, 0, 0);
				// holders of mapped coords...
				Point screen = new Point();

				// This drawing code needs some work to filter the path to the portion 
				// * that is on the screen. The FINE line really slows things down



				RoutePathSmoothness smoothenss = RoutePathSmoothness.ROUGH;
				List<GeoPoint> drawPoints = mRoute.getGeoPointPath(smoothenss);

				Iterator<GeoPoint> itr = drawPoints.listIterator();

				if (itr.hasNext()) { 
					// convert the start point.
					mapView.getProjection().toPixels( (GeoPoint)itr.next(), screen );
					thePath.moveTo(screen.x, screen.y);

					while( itr.hasNext() ) {
						GeoPoint p = (GeoPoint)itr.next();
						map.getProjection().toPixels( p, screen);
						thePath.lineTo(screen.x, screen.y);
					}

					this.pathPaint.setStyle(Paint.Style.STROKE);
					canvas.drawPath(thePath, this.pathPaint);
				}

			}

			super.draw(canvas, mapView, shadow);

		}

	}
	
	//This function will basically get the location and change to something more suitable for google maps
	public String change_location(String loc){
		String temp = "";
		//cannot use switch statement on strings so we are going to do this old fashion way
		if(loc.equals("Media Theater"))
			temp = "Media Theater, Santa Cruz, CA";
		else if(loc.equals("Classroom Unit"))
			temp = "Classroom Unit, Santa Cruz, CA";
		else if(loc.equals("Oaks Acad"))
			temp = "Oakes Housing Office, Santa Cruz, CA";
		else if (loc.equals("Baskin Eng1") || loc.equals("Baskin Aud") || loc.equals("Baskin Eng2"))
			temp = "Baskin Engineering, Santa Cruz, CA";
		else if (loc.equals("Thim Lec") || loc.equals("Thim Lab"))
			temp = "Thimann Lecture, Santa Cruz, CA";
		else if (loc.equals("N.Sci Annex"))
			temp = "Natural Science Annex, Santa Cruz, CA";
		else if (loc.equals("Kresge Clm"))
			temp ="Kresge Classroom, Santa Cruz, CA";
		else if (loc.equals("Soc Sci 2"))
			temp ="Social Sciences 2 , Santa Cruz, CA";
		else if (loc.equals("Soc Sci 1"))
			temp ="Social Sciences 1, Santa Cruz, CA";
		else if (loc.equals("Phys Sci"))
			temp ="Physical Sciences, Santa Cruz, CA";
		else if (loc.equals("Merril Acad"))
			temp ="Merill College, Santa Cruz, CA";
		else if (loc.equals("Porter Acad"))
			temp ="Porter College, Santa Cruz, CA";
		
		return temp;
	}
}

