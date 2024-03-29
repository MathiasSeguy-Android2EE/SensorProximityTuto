/**<ul>
 * <li>SensorProximityTuto</li>
 * <li>com.android2ee.android.tuto.sensor.proximity</li>
 * <li>25 nov. 2011</li>
 * 
 * <li>======================================================</li>
 *
 * <li>Projet : Mathias Seguy Project</li>
 * <li>Produit par MSE.</li>
 *
 /**
 * <ul>
 * Android Tutorial, An <strong>Android2EE</strong>'s project.</br> 
 * Produced by <strong>Dr. Mathias SEGUY</strong>.</br>
 * Delivered by <strong>http://android2ee.com/</strong></br>
 *  Belongs to <strong>Mathias Seguy</strong></br>
 ****************************************************************************************************************</br>
 * This code is free for any usage but can't be distribute.</br>
 * The distribution is reserved to the site <strong>http://android2ee.com</strong>.</br>
 * The intelectual property belongs to <strong>Mathias Seguy</strong>.</br>
 * <em>http://mathias-seguy.developpez.com/</em></br> </br>
 * 
 * *****************************************************************************************************************</br>
 *  Ce code est libre de toute utilisation mais n'est pas distribuable.</br>
 *  Sa distribution est reservée au site <strong>http://android2ee.com</strong>.</br> 
 *  Sa propriété intellectuelle appartient à <strong>Mathias Seguy</strong>.</br>
 *  <em>http://mathias-seguy.developpez.com/</em></br> </br>
 * *****************************************************************************************************************</br>
 */
package com.android2ee.android.tuto.sensor.proximity;


import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

/**
 * @author Mathias Seguy (Android2EE)
 * @goals
 * This class aims to:
 * <ul><li></li></ul>
 */
public class SensorProximityTutoActivity extends Activity implements SensorEventListener {
	// see :http://developer.android.com/reference/android/hardware/SensorEvent.html
	// see also:http://developer.android.com/reference/android/hardware/SensorManager.html
	/**
	 * The Tag for the Log
	 */
	private static final String LOG_TAG = "SensorsProximity";

	/******************************************************************************************/
	/** Current sensor value **************************************************************************/
	/******************************************************************************************/
	/**
	 * Current value of the light
	 */
	float proximityValue;
	/**
	 * Max range of the sensor
	 */
	float maxRange;
	/******************************************************************************************/
	/** View **************************************************************************/
	/******************************************************************************************/
	/**
	 * The view that draw the graphic
	 */
	TextView txvProximity;
	/******************************************************************************************/
	/** Sensors and co **************************************************************************/
	/******************************************************************************************/
	/** * The sensor manager */
	SensorManager sensorManager;
	/**
	 * The light
	 */
	Sensor proximity;

	/******************************************************************************************/
	/** Manage life cycle **************************************************************************/
	/******************************************************************************************/
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// build the GUI
		setContentView(R.layout.main);
		// Then manage the sensors and listen for changes
		// Instantiate the SensorManager
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		// Instantiate the light and its max range
		proximity = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
		maxRange = proximity.getMaximumRange();
		// Then build the GUI:
		// find the TextView
		txvProximity = (TextView) findViewById(R.id.txtView);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onPause()
	 */
	@Override
	protected void onPause() {
		// unregister every body
		sensorManager.unregisterListener(this, proximity);
		// and don't forget to pause the thread that redraw the xyAccelerationView
		super.onPause();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		/*
		 * Register the sensor
		 */
		sensorManager.registerListener(this, proximity, SensorManager.SENSOR_DELAY_FASTEST);
		super.onResume();
	}

	/******************************************************************************************/
	/** Drawing method **************************************************************************/
	/******************************************************************************************/

	/**
	 * The method to redraw the view
	 */
	private void redraw() {
		String message = String.format(getString(R.string.proximity_value), ( (int)(proximityValue)));
		txvProximity.setText(message);
	}

	/******************************************************************************************/
	/** SensorEventListener **************************************************************************/
	/******************************************************************************************/

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.hardware.SensorEventListener#onAccuracyChanged(android.hardware.Sensor, int)
	 */
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// Nothing to do
		if (sensor.getType() == Sensor.TYPE_PROXIMITY) {
			// do a log (for the fun, ok don't do a log...)
			String accuracyStr;
			if(SensorManager.SENSOR_STATUS_ACCURACY_HIGH==accuracy) {
				accuracyStr="SENSOR_STATUS_ACCURACY_HIGH";
			}else if(SensorManager.SENSOR_STATUS_ACCURACY_LOW==accuracy) {
				accuracyStr="SENSOR_STATUS_ACCURACY_LOW";
			}else if(SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM==accuracy) {
				accuracyStr="SENSOR_STATUS_ACCURACY_MEDIUM";
			}else{
				accuracyStr="SENSOR_STATUS_UNRELIABLE";
			}
			 Log.d(LOG_TAG, "Sensor's values (" + proximityValue + ") and accuracy : " + accuracyStr);
			// then redraw
			redraw();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.hardware.SensorEventListener#onSensorChanged(android.hardware.SensorEvent)
	 */
	@Override
	public void onSensorChanged(SensorEvent event) {
		// update only when your are in the right case:
		if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
			// the light value
			proximityValue = event.values[0];
			// do a log (for the fun, ok don't do a log...)
			 Log.d(LOG_TAG, "Sensor's values (" + proximityValue + ") and maxRange : " + maxRange);
			// then redraw
			redraw();
		}

	}
}