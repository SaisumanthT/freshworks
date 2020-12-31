import org.json.simple.JSONObject;

/**
 * This class is the basic sample usage of the CRD operations for a Data store.
 */
public class UsageExample {

  public static void main(String[] args) {

    /**
     * This will create a data store with default location as C:\\Users\\public, which might fail for Mac users, so use
     * the valid path accordingly in the constructor parameter.
     */
    DataStore ds = new DataStore();

    //Can provide a desired path for the datastore as a constructor parameter as shown below.
    // DataStore ds = new DataStore("C:\\Users\\public");

    JSONObject jsonObject1 = new JSONObject();
    jsonObject1.put("firstName", "Jane");
    jsonObject1.put("lastName", "Doe");
    jsonObject1.put("address", "Boston");

    //This will output successful key creation message.
    System.out.println(ds.create("1", jsonObject1, 10));

    JSONObject jsonObject2 = new JSONObject();
    jsonObject2.put("firstName", "John");
    jsonObject2.put("lastName", "Doe");
    jsonObject2.put("address", "Philly");

    //This will output successful key creation message.
    System.out.println(ds.create("2", jsonObject2, 10));

    //This will output key already exists message.
    System.out.println(ds.create("1", jsonObject1, 10));

    //This will output the value for key-1 in the data store.
    System.out.println(ds.read("1"));

    //This will output the value for key-2 in the data store.
    System.out.println(ds.read("2"));

    //This will delete the key-1.
    System.out.println(ds.delete("1"));

    //This will output that key does not exist as it's already deleted previously.
    System.out.println(ds.read("1"));
  }
}
