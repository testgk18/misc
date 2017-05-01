package ambulance;

/**
 * Class person is a data class containing all data we need to track about a
 * patient and a doctor.
 * 
 * @author  till zopppke
 */
public class Person {

	// if person is waiting, this is the point in time it started
	private int _startedWaitingAt = 0;
	
	// total waiting time, as needed for statistic purpose
	private int _totalWaitingTime = 0;
	
	// flag indicating whether the person is currently waiting or not.
	private boolean _isWaiting = true;
	
	// reference to preferred doctor. There is one policy, where a person must
	// return to the same doctor for 2nd examination as he had for the 1st exam.
	private Person _preferredDoctor = null;
	
	// name of this person. It is nice to have a name, and also useful for print.
	private String _name;

	/**
	 * Creates a new person
	 * @param name a String as the person's name
	 */
	public Person(String name) {
		_name = name;
	}

	/**
	 * Causes this person to stop waiting. The waiting status and total waiting
	 * time is adjusted.
	 */
	public void stopWaiting() {
		_isWaiting = false;
		_totalWaitingTime =
			_totalWaitingTime + Ambulance.time - _startedWaitingAt;
	}

	/**
	 * causes this person to start waiting. Waiting status is adjusted and 
	 * the current point in time is saved.
	 */
	public void startWaiting() {
		_isWaiting = true;
		_startedWaitingAt = Ambulance.time;
	}

	/**
	 * Returns the total waiting time of this person
	 * @return the total waiting time of this person
	 */
	public int getWaitingTime() {
		return _totalWaitingTime;
	}

	/**
	 * indicates whether this person is Waiting or not
	 * @return a <code>boolean</code> as the person's waiting status
	 */
	public boolean isWaiting() {
		return _isWaiting;
	}

	/**
	 * returns the person's preferred doctor if there is any. A patient gets a 
	 * preferred doctor when going to the xray-shooting.
	 * @return a reference to the person's preferred doctor
	 */
	public Person getPreferredDoctor() {
		return _preferredDoctor;
	}

	/**
	 * Sets the person's preferred doctor to the specified reference.
	 * @param doctor a reference to the person's preferred doctor
	 */
	public void setPreferredDoctor(Person doctor) {
		_preferredDoctor = doctor;
	}

	/**
	 * Returns the name of this person.
	 * @return the name of this person.
	 */
	public String getName() {
		return _name;
	}

	/**
	 * Returns the name of this person.
	 */
	public String toString() {
		return _name;
	}
}
