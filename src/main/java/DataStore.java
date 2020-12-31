
import org.json.simple.JSONObject;

import java.util.Date;


/**
 * This class builds a file-based key value data store that supports basic CRD (create, delete and read)
 * operations.
 */
public final class DataStore {

  private String storeLocation = "";
  private String storeName = "";

  /**
   * DataStore initialization with default storage location
   */
  public DataStore() {
      storeLocation = "C:\\Users\\Public";
      storeName = Utility.getStoreName();
  }

  /**
   * DataStore initialization with user provided storage location
   */
  public DataStore(String storeLocation) {
      this.storeLocation = storeLocation;
      storeName = Utility.getStoreName();
  }

  /**
   * This method is used to create a key-value entry for a data store.
   * @param key
   * @param value
   * @return a message which state the failure or success of key creation.
   */
  public synchronized String create(String key, JSONObject value) {
    try {
      return create(key, value, 0);
    } catch (Exception exception) {
      return "Create operation failure.";
    }
  }

  /**
   * This method is used to create a key-value entry with a particular expiryTime for that key.
   * @param key
   * @param value
   * @param expiryTime
   * @return a message for the creation of the key-value entry.
   */
  public synchronized String create(String key, JSONObject value, int expiryTime) {

    try {
      if(!Utility.isValidKey(key)){
        return "Key length exceeded.";
      }

      if(!Utility.isValidValue(value)){
        return "Size of the value exceeded 16KB.";
      }

      String fileStoreLocation = this.storeLocation + "/" + this.storeName;

      if(Utility.isKeyExists(key,fileStoreLocation)){
        return "Key already exists in the data store.";
      }

      Data data = new Data();
      data.setKey(key);
      data.setExpiryTime(expiryTime);
      data.setValue(value);
      data.setTimestamp(new Date().getTime());

      if(Utility.write(data,fileStoreLocation)){
        return "Data Store entry creation successful.";
      }

    }catch (Exception e){
      return "Data Store entry creation failure.";
    }


    return "Data Store entry creation failure.";
  }

  /**
   * This method is used to read the value for an particular key from a data store.
   * @param key
   * @return A value for that particular key is the key exists in the data store.
   */
  public synchronized Object read(String key) {

    try {
      String fileStoreLocation = this.storeLocation + "/" + this.storeName;

      if(!Utility.isValidKey(key)){
        return "Key length exceeded.";
      }

      if(!Utility.isKeyExists(key,fileStoreLocation)){
        return "Key does not exists in data store";
      }

      Data data = Utility.read(key,fileStoreLocation);

      if(data != null){
        return data.getValue();
      }
    }catch (Exception e){
      return "Data Store read failure.";
    }
    return "Data Store read failure.";
  }

  /**
   * This method deletes the key-value pair based on the input key.
   * @param key
   * @return the message about the delete operation.
   */
  public synchronized String delete(String key) {
    try {

      String fileStoreLocation = this.storeLocation + "/" + this.storeName;

      if(!Utility.isValidKey(key)){
        return "Key length exceeded.";
      }

      if(!Utility.isKeyExists(key,fileStoreLocation)){
        return "Key does not exists in data store";
      }

      if(Utility.delete(key,fileStoreLocation)){
        return "Key deleted.";
      }

    }catch (Exception e){
      return  "Data Store delete failure.";
    }
    return  "Data Store delete failure.";
  }
}
