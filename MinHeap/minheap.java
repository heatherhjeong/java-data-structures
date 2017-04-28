import java.util.ArrayList;
import java.util.Comparator;

/**
 * Implements a (binary) min heap allowing for Comparable items or a specified Comparator.
 * If no Comparator is supplied, the added items are assumed to be comparable.  If not a 
 * ClassCastException may occur.  Null values should never be added to the min heap. 
 *
 * @param <T>
 */
public class MinHeap<T>
{ 
  private Comparator<? super T> comp;
  private ArrayList<T> index;

  /**
   * Constructs a min heap of Comparable values.
   */
  public MinHeap()
  {
    this(null);
  }

  /**
   * Constructs a min heap using the specified Comparator as the ordering.
   */
  public MinHeap(Comparator<? super T> c)
  {
    index = new ArrayList<>();
    comp = c;
  }

  /**
   * Compares a and b using the appropriate method.
   * @param a
   * @param b
   * @return < 0 if a comes before b, > 0 if b comes before a, or =0 if equal with
   * respect to the given ordering
   */
  private int compare(T a, T b)
  {
    if (comp == null)
    {
      @SuppressWarnings("unchecked")
      Comparable<? super T> ca = (Comparable<? super T>)a;
      return ca.compareTo(b);
    }
    return comp.compare(a,b);
  }

  /**
   * Returns the number of elements in the min heap.
   * @return the number of elements in the min heap
   */
  public int size() { return index.size(); }

  /**
   * Returns (but does not remove) the smallest element of the heap.
   * @return the smallest element of the heap
   */
  public T getMin() { return index.get(0); }

  /**
   * Returns if the heap is empty.
   * @return if the heap is empty
   */
  public boolean isEmpty() { return index.isEmpty(); }
  
  /**
   * Returns and removes the smallest element of the heap.
   * @return the smallest element of the heap
   */
  public T removeMin() { 
    T min = index.get(0);
    swap(0,index.size()-1);
    index.remove(index.size()-1);
    int k = 0;
    int left = 2*k+1;
    int right = 2*k+2;
    boolean ok = left < index.size();
    while (ok){
      if (compare(index.get(k), index.get(left)) > 0){
        if (right < index.size()){
          if (compare(index.get(left),index.get(right)) > 0){
            swap(k,right);
            k = right;
          }
          else {
            swap(left,k);
            k = left;
          }
        }
        else {
          swap(left,k);
          k =left;
        }
      }
      else if (right < index.size()){
        if (compare(index.get(k), index.get(right)) > 0){
          swap(k,right);
          k = right;
        }
        else break;
      }
      else {
        break;
      }
      right = 2*k+2;
      left = 2*k+1;
      ok = left < index.size();
    }
    return min;
  }
  
  public void swap(int index1, int index2){
    T temp = index.get(index1);
    index.set(index1, index.get(index2));
    index.set(index2, temp);
  }
  
  /**
   * Adds the given element to the heap.
   * @param t
   */
  public void add(T t) {
    index.add(t);
    int k = index.size()-1;
    int parent = (k-1)/2;
    boolean ok = k != 0;
    while (ok){
      if (compare(index.get(parent), index.get(k)) > 0){
        swap(parent,k);
        k = parent;
      }
      else break;
      parent = (k-1)/2;
      ok = k != 0;
    }
  }
  
  /**
   * Returns whether the heap contains the given value.
   * @param t value to find
   * @return whether the heap contains the given value
   */
  public boolean contains(T t) { return index.contains(t); }  
}
