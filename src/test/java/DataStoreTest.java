
import org.json.simple.JSONObject;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DataStoreTest {


  @Test
  public void createKeyWithoutExpiry() {
    DataStore ds = new DataStore();
    JSONObject jsonObject1 = new JSONObject();
    jsonObject1.put("firstName", "Jane");
    jsonObject1.put("lastName", "Doe");
    jsonObject1.put("address", "Boston");

    assertEquals(ds.create("1", jsonObject1),"Data Store entry creation successful.");
  }

  @Test
  public void createKeyWithExpiry() {
    DataStore ds = new DataStore();
    JSONObject jsonObject1 = new JSONObject();
    jsonObject1.put("firstName", "Jane");
    jsonObject1.put("lastName", "Doe");
    jsonObject1.put("address", "Boston");

    assertEquals(ds.create("3", jsonObject1,10),"Data Store entry creation successful.");
  }

  @Test
  public void createInvalidKey() {
    DataStore ds = new DataStore();
    JSONObject jsonObject1 = new JSONObject();
    jsonObject1.put("firstName", "Jane");
    jsonObject1.put("lastName", "Doe");
    jsonObject1.put("address", "Boston");

    assertEquals(ds.create("1saaaaaaaaaa" +
            "324444444444444444444444ddddddddddddd" +
            "ddddddd33333333333333333" +
            "sssssssssssss" +
            "ddddddddddddddddddd", jsonObject1,10),"Key length exceeded.");
  }

  @Test
  public void createKeyAlreadyExists() {
    DataStore ds = new DataStore();
    JSONObject jsonObject1 = new JSONObject();
    jsonObject1.put("firstName", "Jane");
    jsonObject1.put("lastName", "Doe");
    jsonObject1.put("address", "Boston");

    ds.create("1", jsonObject1,10);

    assertEquals(ds.create("1", jsonObject1,10),"Key already exists in the data store.");
  }

  @Test
  public void verifyKeyExpiry() throws InterruptedException {
    DataStore ds = new DataStore();
    JSONObject jsonObject1 = new JSONObject();
    jsonObject1.put("firstName", "Jane");
    jsonObject1.put("lastName", "Doe");
    jsonObject1.put("address", "Boston");
    ds.create("1", jsonObject1,2);
    Thread.sleep(3000);

    assertEquals(ds.read("1"),"Key does not exists in data store");
  }

  @Test
  public void readValueForKey() {
    DataStore ds = new DataStore();
    JSONObject jsonObject1 = new JSONObject();
    jsonObject1.put("firstName", "Jane");
    jsonObject1.put("lastName", "Doe");
    jsonObject1.put("address", "Boston");
    ds.create("1", jsonObject1,10);
    assertEquals(ds.read("1"),jsonObject1);
  }

  @Test
  public void readValueForKeyDoesNotExist() {
    DataStore ds = new DataStore();
    JSONObject jsonObject1 = new JSONObject();
    jsonObject1.put("firstName", "Jane");
    jsonObject1.put("lastName", "Doe");
    jsonObject1.put("address", "Boston");
    ds.create("1", jsonObject1,10);

    assertEquals(ds.read("2"),"Key does not exists in data store");
  }


  @Test
  public void deleteKey() {
    DataStore ds = new DataStore();
    JSONObject jsonObject1 = new JSONObject();
    jsonObject1.put("firstName", "Jane");
    jsonObject1.put("lastName", "Doe");
    jsonObject1.put("address", "Boston");
    ds.create("1", jsonObject1,10);

    assertEquals(ds.delete("1"),"Key deleted.");
    assertEquals(ds.read("1"),"Key does not exists in data store");
  }

  @Test
  public void deleteKeyDoesNotExist() {
    DataStore ds = new DataStore();

    assertEquals(ds.delete("2"),"Key does not exists in data store");
  }












}
