
import org.json.simple.JSONObject;

import java.io.Serializable;


/**
 * This class represents the data which will be stored in the data store.
 */
public class Data implements Serializable {

  private static final long uid = 1L;
  private String key;
  private int expiryTime;
  private JSONObject value;
  private long timestamp;

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public int getExpiryTime() {
    return expiryTime;
  }

  public void setExpiryTime(int expiryTime) {
    this.expiryTime = expiryTime;
  }

  public JSONObject getValue() {
    return value;
  }

  public void setValue(JSONObject value) {
    this.value = value;
  }

  public long getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(long timestamp) {
    this.timestamp = timestamp;
  }
}
