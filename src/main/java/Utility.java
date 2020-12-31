import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.management.ManagementFactory;
import java.util.Date;
import java.util.HashMap;

/**
 * This class is an utility which interacts with the data store and has implementations of CRD operations with
 * the data store.
 */
public class Utility {

  /**
   * To get the current processName, which will be used to define the store name.
   *
   * @return the process name as a string
   */
  public static String getStoreName() {
    return "store" + ManagementFactory.getRuntimeMXBean().getName();
  }

  /**
   * Validate if length of the key is less than 32
   *
   * @param key is the key of the data store.
   * @return if it is a valid key or not.
   */
  public static boolean isValidKey(String key) {
    return key.length() <= 32;
  }

  /**
   * Validate if the size of the value object is less than 16KB.
   * @param value is the value of a particular key in the data store.
   * @return if it is a valid value or not.
   */
  public  static  boolean isValidValue(JSONObject value){
      return value.toJSONString().length()/1024 <=16;
  }

  /**
   * This method checks if a particular key exists in a data store. It also takes into consideration of the
   * expiry time for a particular key.
   * @param key
   * @param location
   * @return true/false based on the existence of the key.
   */
  public static boolean isKeyExists(String key, String location) {

    boolean isKeyExists = false;
    HashMap<String, Data> dataMap = null;
    try {
      File file = new File(location);
      if(file.exists()){
        dataMap =getDataMap(file);

        if(dataMap.containsKey(key)){
          isKeyExists = true;
        }
      }

      if(isKeyExists){
         if(checkExpiry(dataMap,key,file)){
           isKeyExists = false;
         }
      }
    }catch (Exception e){
      e.printStackTrace();
    }
    return  isKeyExists;
  }

  /**
   * This method is used to write a particular data in the data store.
   * @param data
   * @param location
   * @return true/false based on successful write of the data in a particular location.
   */
  public static boolean write(Data data, String location){

    HashMap<String, Data> dataMap = null;
    try {
      File file = new File(location);
      if (file.exists()) {
        dataMap = getDataMap(file);
      }else {
        dataMap = new HashMap<String, Data>();
      }

      dataMap.put(data.getKey(), data);
      writeDataMap(file,dataMap);

    }catch (Exception e){
      return  false;
    }
    return true;
  }

  /**
   * This method is used to read the data for a particular key in a data store.
   * @param key
   * @param location
   * @return The data for that particular key.
   */
  public static Data read(String key, String location) {
    HashMap<String, Data> dataMap = null;
    try {
      File file = new File(location);
      if (file.exists()) {
        dataMap = getDataMap(file);
      }else {
        return null;
      }
    } catch (Exception e){
      e.printStackTrace();
    }
    return dataMap.get(key);
  }

  /**
   * This method is used to delete data for a particular key.
   * @param key
   * @param location
   * @return true/false based on the successful deletion of the data.
   */
  public static boolean delete(String key, String location) {
    HashMap<String, Data> dataMap = null;
    try {
      File file = new File(location);
      if (file.exists()) {
        dataMap = getDataMap(file);
        dataMap.remove(key);
        writeDataMap(file,dataMap);
        return true;
      }else {
        return false;
      }

    }catch (Exception e){
      e.printStackTrace();
      return false;
    }
  }

  /**
   * This is the private helper method which reads the data from a particular file and converts it to a HashMap.
   * @param file
   * @return HashMap of the file.
   * @throws IOException
   * @throws ClassNotFoundException
   */
  private static HashMap<String,Data> getDataMap(File file) throws IOException, ClassNotFoundException {
    FileInputStream fileInputStream =  new FileInputStream(file);
    ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
    HashMap<String,Data> dataMap =  (HashMap<String, Data>) objectInputStream.readObject();
    fileInputStream.close();
    objectInputStream.close();
    return  dataMap;
  }

  /**
   * This is a private helper method with writes the HashMap data to a particular file.
   * @param file
   * @param dataMap
   * @throws IOException
   */
  private static void writeDataMap(File file, HashMap<String,Data> dataMap) throws IOException {
    FileOutputStream fileOutputStream = new FileOutputStream(file);
    ObjectOutputStream objectOutputStream = new ObjectOutputStream(
            fileOutputStream);
    objectOutputStream.writeObject(dataMap);

    fileOutputStream.close();
    objectOutputStream.close();
  }

  /**
   * This is a private helper method which will verify if a key is expired or not.
   * @param dataMap
   * @param key
   * @param file
   * @return true/false based on the expiry of the key.
   * @throws IOException
   */
  private static boolean checkExpiry(HashMap<String,Data> dataMap,String key, File file) throws IOException {
    Data data = dataMap.get(key);

    //If the key is expired then remove it from the datastore and return expiry as true.
    if (data.getExpiryTime() > 0 && isKeyExpired(data)){
      dataMap.remove(key);
      writeDataMap(file,dataMap);
      return  true;
    }
    return false;
  }

  /**
   * This is a private helper which calculates the expiry for a key
   * @param data
   * @return true/false based on the key expiry.
   */
  private static  boolean isKeyExpired(Data data){
    long currentTimeStamp = new Date().getTime();
    return (currentTimeStamp - data
            .getTimestamp()) >= (data
            .getExpiryTime() * 1000);
  }

}
