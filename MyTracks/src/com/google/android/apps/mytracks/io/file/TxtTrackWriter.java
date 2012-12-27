package com.google.android.apps.mytracks.io.file;

import com.google.android.apps.mytracks.content.MyTracksLocation;
import com.google.android.apps.mytracks.content.Track;
import com.google.android.apps.mytracks.content.Waypoint;
import com.google.android.apps.mytracks.io.file.TrackWriterFactory.TrackFileFormat;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.util.Calendar;

/**
 * txt文件格式
 * <p>
 * x,y
 * </p>
 * 
 * @author zhanghanguo
 */
public class TxtTrackWriter implements TrackFormatWriter {
  
  private static final String TAG = TxtTrackWriter.class.getSimpleName();
  
  private static final NumberFormat SHORT_FORMAT = NumberFormat.getInstance();

  static {
    SHORT_FORMAT.setMinimumFractionDigits(6);
    SHORT_FORMAT.setMaximumFractionDigits(6);
  }

  private final Context mContext;
  private Track mTrack;
  private PrintWriter printWriter;
  private long mLastTime;

  public TxtTrackWriter(Context context) {
    this.mContext = context;
  }

  @Override
  public String getExtension() {
    return TrackFileFormat.TXT.getExtension();
  }

  @Override
  public void prepare(Track track, OutputStream outputStream) {
    this.mTrack = track;
    printWriter = new PrintWriter(outputStream);
  }

  @Override
  public void close() {
    if (printWriter != null) {
      printWriter.close();
      printWriter = null;
    }
  }

  @Override
  public void writeHeader() {
    // TODO Auto-generated method stub

  }

  @Override
  public void writeFooter() {
    // TODO Auto-generated method stub

  }

  @Override
  public void writeBeginWaypoints() {
    // TODO Auto-generated method stub

  }

  @Override
  public void writeEndWaypoints() {
    // TODO Auto-generated method stub

  }

  @Override
  public void writeWaypoint(Waypoint waypoint) {
//    Location location = waypoint.getLocation();
//    writeLocation(location);
  }

  @Override
  public void writeBeginTrack(Location firstLocation) {
    // TODO Auto-generated method stub

  }

  @Override
  public void writeEndTrack(Location lastLocation) {
    // TODO Auto-generated method stub

  }

  @Override
  public void writeOpenSegment() {
    // TODO Auto-generated method stub

  }

  @Override
  public void writeCloseSegment() {
    // TODO Auto-generated method stub

  }
  
  private static final int GPS_UPDATE_TYPE_INVALID  = 0;          /**< 无效 */
  private static final int GPS_UPDATE_TYPE_CONNECT_STATUS = 1;        /**< 连接状态改变 */
  private static final int GPS_UPDATE_TYPE_POS = 2;               /**< 位置改变 */

  @Override
  public void writeLocation(Location location) {
    if(location == null){
      Log.w(TAG, "location param is null!");
      return;
    }
    StringBuilder line = new StringBuilder();
    if(location.getLatitude() == 0 && location.getLongitude() == 0){
      //GPS_UPDATE_TYPE_CONNECT_STATUS
      if(location instanceof MyTracksLocation){
        MyTracksLocation myTracksLocation = (MyTracksLocation)location;
        line.append(GPS_UPDATE_TYPE_CONNECT_STATUS);
        line.append(",");
        line.append(myTracksLocation.getGpsStatus());
      }
    }else{
      line.append(GPS_UPDATE_TYPE_POS);
      line.append(",");
      line.append(SHORT_FORMAT.format(location.getLongitude()));//经度
      line.append(",");
      line.append(SHORT_FORMAT.format(location.getLatitude()));//纬度
      line.append(",");
      line.append(SHORT_FORMAT.format(location.getSpeed()));
      line.append(",");
      line.append(SHORT_FORMAT.format(location.getBearing()));
      line.append(",");
      line.append(SHORT_FORMAT.format(location.getAccuracy()));
    }
    long time = location.getTime();
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(time);
    line.append(",");
    line.append(calendar.get(Calendar.YEAR)).append(":");
    line.append(calendar.get(Calendar.MONTH)+1).append(":");
    line.append(calendar.get(Calendar.DAY_OF_MONTH)).append(":");
    line.append(calendar.get(Calendar.HOUR_OF_DAY)).append(":");
    line.append(calendar.get(Calendar.MINUTE)).append(":");
    line.append(calendar.get(Calendar.SECOND));
    if(mLastTime == 0){
      line.append(",").append(0);
    }else{
      line.append(",").append(time - mLastTime);
    }
    mLastTime = time;
    line.append("\r");
    Log.i(TAG, line.toString());
    printWriter.println(line.toString());
  }

}
