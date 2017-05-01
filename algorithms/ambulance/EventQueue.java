package ambulance;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Vector;

/**
 * Class EventQueue is a priority Queue for Events implemented as a heap.
 * 
 * @author  till zopppke
 */
public class EventQueue implements Enumeration {

	// vector containing discreteElements maintaining a heap property
	private Vector _elements;

	///////////////////////////// constructors /////////////////////////////////

	/**
	 * Constructs an initially empty discreteEventQueue
	 */
	public EventQueue() {
		_elements = new Vector();
	}

	/**
	 * Constructs a discreteEventQueue initially containing given elements
	 * @param elements a <code>collection</code> of elements
	 */
	public EventQueue(Collection elements) {
		_elements = new Vector(elements);
		createHeap();
	}

	////////////////////////// private methods /////////////////////////////////

	// compares two elements by calling compareTo
	private int compare(int i, int j) {
		return ((Comparable) _elements.get(i)).compareTo(_elements.get(j));
	}

	// increases the last element
	private void increase() {
		increase(_elements.size() - 1);
	}

	// increases the specified element so that the heap property will maintained
	private void increase(int i) {
		int parent = (i - 1) / 2;
		while (i > 0 && compare(parent, i) > 0) {
			swap(i, parent);
			i = parent;
			parent = (i - 1) / 2;
		}
	}

	// creates the heap property for the elements Vector
	private void createHeap() {
		for (int i = _elements.size() - 1; i >= 0; --i) {
			reHeap(i);
		}
	}

	// lets the first element sink;
	private void reHeap() {
		reHeap(0);
	}

	// lets the specified element sink
	private void reHeap(int i) {
		int j = 2 * i + 1;
		boolean done = false;
		while (j < _elements.size() && !done) {
			// compare left child to right child
			if (j + 1 < _elements.size() && compare(j, j + 1) > 0) {
				j++;
			}
			// compare parent to smaller child
			if (compare(i, j) > 0) {
				swap(i, j);
				i = j;
				j = 2 * j + 1;
			} else {
				done = true;
			}
		}
	}

	// swaps two elements in the elements Vector
	private void swap(int i, int j) {
		Object temp = _elements.get(i);
		_elements.set(i, _elements.get(j));
		_elements.set(j, temp);
	}

	/////////////////////////// public methods /////////////////////////////////

	/**
	 * Checks whether this discreteEventQueue contains more events
	 * @see java.util.Enumeration#hasMoreElements()
	 */
	public boolean hasMoreElements() {
		return _elements.size() > 0;
	}

	/**
	 * Returns the next event (that will happen in the nearest future)
	 * @see java.util.Enumeration#nextElement()
	 */
	public Object nextElement() {
		Object retour = _elements.firstElement();
		_elements.set(0, _elements.lastElement());
		_elements.remove(_elements.size() - 1);
		reHeap();
		return retour;
	}
	
	/**
	 * inserts a discreteEvent by maintaining the heap property
	 * @param e the Element to be inserted
	 */
	public void insert(Event e) {
		_elements.add(e);
		increase();
	}
}
