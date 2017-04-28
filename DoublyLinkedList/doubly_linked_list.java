import java.util.NoSuchElementException;

/**
 * Implements a DoublyLinkedList using a sentinel node.
 *
 * @param <T>
 */
public class DoublyLinkedList<T>
{
  /** Type of the nodes in the list */
  public static class Node<S>
  {
    /** Points at the next node in the list */
    private Node<S> next;
    /** Points at the previous node in the list */
    private Node<S> prev;
    /** Value stored in the node */
    private S value;
    
    private Node(S value, Node<S> prev, Node<S> next)
    {
      this.value = value;
      this.prev = prev;
      this.next = next;
    }
    
    /** Gets the value in the node */
    public S getValue() { return value; }
    /** Sets the value in the node */
    public void setValue(S s) { value = s; }
    /** Gets the next node in the list */
    public Node<S> getNext() { return next; }
    /** Gets the previous node in the list */
    public Node<S> getPrev() { return prev; }
  }
  
  /** Points at the sentinel node */
  private Node<T> sentinel;
  /** Stores the size of the list */
  private int size;
  
  /**
   * Constructs an empty DoublyLinkedList with its sentinel node.
   */
  public DoublyLinkedList() 
  {
    sentinel = new Node<T>(null,null,null);
    sentinel.next = sentinel.prev = sentinel;
    size = 0;
  }
    
  /**
   * Returns the sentinel node.  This allows users of the class to know when to stop iterating. 
   * @return the sentinel node
   */
  public Node<T> getSentinel() {
    return sentinel; 
  }
  
  /**
   * Returns the number of Nodes in the list. 
   * @return the number of Nodes in the list
   */
  public int size() { 
    return size; 
  }
  
  /**
   * Returns if the list is empty 
   * @return if the list is empty
   */
  public boolean isEmpty() {
    return size == 0; 
  }

  /**
   * Inserts a new value in the list after the given Node.  For example,
   * if the list is 1,2,3,4, and listNode is 2 then addAfter(listNode,9) should give
   * 1,2,9,3,4.
   * @param listNode the new value should be inserted after this node  
   * @param t the value to insert
   * @return the newly created Node
   */
  public Node<T> addAfter(Node<T> listNode, T t) 
  {
    Node<T> afterNewNode = listNode.next;
    Node<T> newNode = new Node<T>(t, listNode, afterNewNode);
    listNode.next = newNode;
    afterNewNode.prev = newNode;
    size++;
    return newNode;
  }

  /**
   * Inserts a new value in the list before the given Node.  For example,
   * if the list is 1,2,3,4, and listNode is 2 then addBefore(listNode,9) should give
   * 1,9,2,3,4.
   * @param listNode the new value should be inserted before this node 
   * @param t the value to insert
   * @return the newly created Node
   */
  public Node<T> addBefore(Node<T> listNode, T t) 
  {
    Node<T> beforeNewNode = listNode.prev;
    Node<T> newNode = new Node<T>(t, beforeNewNode, listNode);
    beforeNewNode.next = newNode;
    listNode.prev = newNode;
    size++;
    return newNode;
  }
  
  /**
   * Removes the given Node from the list. 
   * @param listNode the node to remove from the list
   * @return the value in the removed node
   * @throws IllegalArgumentException if listNode is the sentinel
   */
  public T removeNode(Node<T> listNode) 
  {
    if (listNode.equals(sentinel)) throw new IllegalArgumentException();
    T valListNode = listNode.getValue();
    Node<T> prevNode = listNode.prev;
    Node<T> nextNode = listNode.next;
    prevNode.next = nextNode;
    nextNode.prev = prevNode;
    listNode = null;
    size--;
    return valListNode;
  }

  /** 
   * Adds a value to the front of the list.
   * @param t value to be added
   */
  public Node<T> addFirst(T t) { 
    Node<T> currFirst = sentinel.next;
    Node<T> newNode = new Node<T>(t, sentinel, currFirst);
    sentinel.next = newNode;
    currFirst.prev = newNode;
    size++;
    return newNode; 
  }

  /** 
   * Adds a value to the end of the list.
   * @param t value to be added
   * @return the created Node at the end of the list
   */
  public Node<T> addLast(T t) { 
    Node<T> currLast = sentinel.prev;
    Node<T> newNode = new Node<T>(t, currLast, sentinel);
    currLast.next = newNode;
    sentinel.prev = newNode;
    size++;
    return newNode;
  }
  
  /**
   * Removes the value at the front of the list.
   * @return the value that was removed.
   * @throws NoSuchElementException if the list is empty
   */
  public T removeFirst() 
  {
    if (isEmpty()) throw new NoSuchElementException();
    Node<T> currFirst = sentinel.next;
    Node<T> toBeFirst = currFirst.next;
    T currFirstVal = currFirst.getValue();
    toBeFirst.prev = sentinel;
    sentinel.next = toBeFirst;
    currFirst = null;
    size--;
    return currFirstVal;
  }

  /**
   * Removes the value at the end of the list.
   * @return the value that was removed.
   * @throws NoSuchElementException if the list is empty
   */
  public T removeLast() 
  {
    if (isEmpty()) throw new NoSuchElementException();
    Node<T> currLast = sentinel.prev;
    Node<T> toBeLast = currLast.prev;
    T currLastVal = currLast.getValue();
    toBeLast.next = sentinel;
    sentinel.prev = toBeLast;
    currLast = null;
    size--;
    return currLastVal;
  }

  /**
   * Gets the Node at the front of the list.  If empty return the sentinel.
   * @return the Node at the front of the list
   */
  public Node<T> getFirst() 
  {
    if (isEmpty()) return sentinel;
    return sentinel.next;
  }

  /**
   * Gets the Node at the end of the list. If empty return the sentinel.
   * @return the Node at the end of the list
   */
  public Node<T> getLast() 
  {
    if (isEmpty()) return sentinel;
    return sentinel.prev;
  } 
  
  /**
   * Returns the Node at index i (0-based).
   * @param i the index of the Node to retrieve
   * @return the node at index i
   * @throws IndexOutOfBoundException if the i is an invalid index
   */
  public Node<T> getNode(int i) 
  {
    if (i < 0 || i >= size()) throw new IndexOutOfBoundsException();
    Node<T> node = sentinel;
    for (int j = 0; j <= i; j++){
      node = node.getNext();
    }
    return node;
  }
  
  /**
   * Returns false if o is not a (descendent of) DoublyLinkedList or is null.  Otherwise, returns true
   * if the two lists contain the same values in the same order.  In other words, the two lists must have
   * the same size, and each corresponding element must have values that are .equals to each other (do not
   * use == when comparing the values in the nodes).  Users may put nulls in the list, so make sure to compare
   * values of null properly.  Should have runtime Theta(n).
   * @return true if the lists store the same values in the same order, false otherwise
   */
  @Override
  public boolean equals(Object o) 
  {
    if (o instanceof DoublyLinkedList<?>){
      DoublyLinkedList<?> dll = (DoublyLinkedList<?>) o;
      if (dll.size() != size) return false;
      Node<T> curr = getFirst();
      Node<?> ocurr = dll.getFirst();
      for(int i = 0; i < size; i++){
        if (curr.getValue() == null){
          if (ocurr.getValue() != null) return false;
        }
        else {
          if (!curr.getValue().equals(ocurr.getValue())) return false;
        }
        curr = curr.getNext();
        ocurr = ocurr.getNext();
      }
      return true;
    }
    else return false;
  }
    
  /**
   * Inserts the entire given otherList after the given listNode.  This should remove all nodes from
   * otherList leaving it empty.  Make sure otherList is still in a valid (empty) state after this operation.
   * The operation must be Theta(1) to receive credit (i.e., no loops, no recursion).
   * For example, if the current list is 1,2,3,4, otherList is 7,8,9 and the listNode points at 2
   * then spliceAfter will produce 1,2,7,8,9,3,4.
   * @param listNode the Node after which otherList must be inserted
   * @param otherList the list that will be inserted
   */
  public void spliceAfter(Node<T> listNode, DoublyLinkedList<T> otherList) 
  {
    if (otherList.size == 1){
      addAfter(listNode,otherList.getFirst().getValue());
    }
    else if (otherList.size > 1){
      Node<T> oFirst = otherList.getFirst();
      Node<T> oLast = otherList.getLast();
      Node<T> currListNodeNext = listNode.next;
      currListNodeNext.prev = oLast;
      oLast.next = currListNodeNext;
      oFirst.prev = listNode;
      listNode.next = oFirst;
      size = otherList.size + size;
    }
    
    //Making otherList an empty DLL
    otherList.sentinel.next = otherList.getSentinel();
    otherList.sentinel.prev = otherList.getSentinel();
    otherList.size = 0;
    
  }
}
