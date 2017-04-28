import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
 
/**
 * Implements a hashtable using chaining.  If the load factor is ever strictly larger than 
 * the prespecified threshold, then the table automatically resizes.  
 * 
 * Unlike the Java API we do not allow null keys.  We also differ from the Java API in 
 * that we use K as the argument for methods like get, containsKey, etc.  The Java API uses
 * Object for the type in those methods for maximum flexibility but here we go for maximum
 * clarity and type safety.
 *
 * @param <K>
 * @param <V>
 */
public class HashtableMap<K, V>
{
  public static class Entry<Key,Value>
  {
    private Key key;
    private Value value;
    public Key getKey() { return key; }
    public Value getValue() { return value; }
    public void setValue(Value newValue) {value = newValue;}
    public Entry(Key k, Value v)
    {
      key = k;
      value = v;
    }
  }
  
  private ArrayList<LinkedList<Entry<K,V>>> table;
  private double threshold;
  private int size;
  
  /**
   * Creates a hashtable with initCap buckets and a threshold of thresh
   * on the load factor.  If the load factor becomes larger than thresh then 
   * the table will be resized (by doubling).
   * 
   * @param initCap
   * @param thresh
   */
  public HashtableMap(int initCap, double thresh)
  {
    table = new ArrayList<>(initCap);
    threshold = thresh;
    size = 0;
    for (int i = 0; i < initCap; i++) {
      LinkedList<Entry<K,V>> empty = new LinkedList<Entry<K,V>>();
      table.add(empty);
      //table.add(null);
    }
  }

  public HashtableMap() { this(10,.75); }
  
  //Constant used by getBucket
  private static final double HASH_MULT = (Math.sqrt(5)-1)/2;
  
  //Given a key and the number of buckets, returns the bucket number using key.hashCode()
  private static int getBucket(Object key, int numBuckets) 
  { 
    int hash = key.hashCode();
    double x = Integer.toUnsignedLong(hash)*HASH_MULT;
    return (int)(numBuckets*(x - Math.floor(x)));
  }
    
  /**
   * If the key is contained in the map, returns the corresponding Entry.  Otherwise, returns null.
   * 
   * @param key the key to look for
   * @return the corresponding Entry, or null if the key is not present in the map.
   * @throws NullPointerException if key is null
   */
  public Entry<K,V> getEntry(K key)
  {
    if (key == null) throw new NullPointerException();
    int bucketWithKey = getBucket(key,buckets());
    LinkedList<Entry<K,V>> listWithKey = table.get(bucketWithKey);
    for (int i = 0; i < listWithKey.size(); i++){
      if (listWithKey.get(i).getKey().equals(key)) return listWithKey.get(i);
    }
    return null;
  }
  
  /**
   * If the key is contained in the map, returns the corresponding value.  Otherwise, returns null.
   * Note that this method could return null even if the key is contained in the map provided
   * the corresponding value is null.
   * 
   * @param key the key to look for
   * @return the corresponding value, or null if the key is not present in the map.
   * @throws NullPointerException if key is null
   */
  public V get(K key) 
  { 
    if (key == null) throw new NullPointerException();
    Entry<K,V> entryWithKey = getEntry(key);
    if (entryWithKey == null) return null;
    return entryWithKey.getValue();
  }
  
  /**
   * Removes the key from the map if present, and returns the corresponding value.  If the
   * key is not present, returns null.
   * 
   * @param key the key to remove
   * @return removes the key if present and returns the corresponding value; returns null otherwise
   * @throws NullPointerException if key is null
   */
  public V remove(K key) 
  {
    if (key == null) throw new NullPointerException();
    Entry<K,V> entryWithKey = getEntry(key);
    if (entryWithKey == null) return null;
    V entryValue = entryWithKey.getValue();
    int bucketWithKey = getBucket(key,buckets());
    LinkedList<Entry<K,V>> listWithKey = table.get(bucketWithKey);
    listWithKey.remove(entryWithKey);
    size--;
    return entryValue;
  }

  /**
   * Returns whether or not the key is present in the map.
   * 
   * @param key to look up
   * @return true if the key is present, false otherwise
   * @throws NullPointerException if key is null
   */
  public boolean containsKey(K key) 
  { 
    if (key == null) throw new NullPointerException();
    return getEntry(key) != null;
  }
  
  /**
   * Returns whether or not the value is present in the map.  Note the value may be null.  
   *  
   * @param value the value to look for
   * @return true if the given value is found in the map, false otherwise
   */
  public boolean containsValue(V value) 
  { 
    if (value == null) {
      for (int i = 0; i < buckets(); i++){
        LinkedList<Entry<K,V>> currList = table.get(i);
        for (int j = 0; j < currList.size(); j++){
          if (currList.get(j).getValue() == null) return true;
        }
      }
    }
    else {
      for (int i = 0; i < buckets(); i++){
        LinkedList<Entry<K,V>> currList = table.get(i);
        for (int j = 0; j < currList.size(); j++)
          if (currList.get(j).getValue().equals(value)) return true;
      }   
      
    }
    return false;
  }
  
  /**
   * If the key is not contained in the map, it adds the key to the map and associates the given value to it.
   * If the key is already contained in the map, it updates the corresponding value.
   * Returns null if the key is not already contained in the map.  Otherwise, returns the old value.
   * After a key is added, this could trigger a table resizing if the load factor threshold is violated.
   * 
   * @param key the key to assign a value to
   * @param value the value for the corresponding key
   * @return the old value associated with the key, or null if the key isn't present in the map
   * @throws NullPointerException if key is null
   */
  public V put(K key, V value) 
  { 
    if (key == null) throw new NullPointerException();
    if (containsKey(key)){
      Entry<K,V> entryWithKey = getEntry(key);
      V oldVal = entryWithKey.getValue();
      entryWithKey.setValue(value);
      return oldVal;
    }
    Entry<K,V> newEntry = new Entry<K,V>(key,value);
    int bucket = getBucket(key,buckets());
    table.get(bucket).addFirst(newEntry);
    size++;
    if ((double)size/buckets() > threshold){
      ArrayList<LinkedList<Entry<K,V>>> newMap = new ArrayList<>(buckets()*2);
      for (int i = 0; i < buckets()*2; i++){
        LinkedList<Entry<K,V>> empty = new LinkedList<>();
        newMap.add(empty);
      }
      for (int i = 0; i < buckets(); i++){
        LinkedList<Entry<K,V>> currList = table.get(i);
        for (int j = 0; j < currList.size(); j++){
          Entry<K,V> currEntry = currList.get(j);
          int newBucket = getBucket(currEntry.getKey(),buckets()*2);
          newMap.get(newBucket).add(currEntry);
        }
      }
      table = newMap;
    }
    return null;
  }
  
  /**
   * Returns the number of keys in the map.
   * @return the number of keys in the map
   */
  public int size() 
  { 
    return size;
  }
  
  /**
   * Returns the number of buckets in the bucket table.
   * @return the number of buckets in the bucket table.
   */
  public int buckets()
  {
    return table.size();
  }
}
