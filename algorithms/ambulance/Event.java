package ambulance;

/**
 * A data-class containing all data that is necessary to specify any kind of 
 * event that can occure at the ambulance. 
 * 
 * @author  till zopppke
 */
public class Event implements Comparable {

	///////////////////////////// static keys //////////////////////////////////

	/**
	 * a key encoding the event, that a new patient arrives at the ambulance
	 */
	public static final short NEW_PATIENT = 0;
	
	/**
	 * a key encoding the event, that a first examination is finished
	 */
	public static final short FIRST_EXAMINATION = 1;
	
	/**
	 * a key encoding the event, that an xray-shooting is finished
	 */
	public static final short XRAY_SHOOTING = 2;
	
	/**
	 * a key encoding that a second examination is finished.
	 */
	public static final short SECOND_EXAMINATION = 3;

	//////////////////////////////// fields ////////////////////////////////////

	// time in milliseconds when this event occures
	private int _time;

	// event type of this discreteEvent as encoded by static keys
	private short _type;

	// the patient who is related to this event
	private Person _patient = null;

	// the doctor who is related to this event
	private Person _doctor = null;

	////////////////////////////// constructors ////////////////////////////////

	/**
	 * Constructs a new event. 
	 * @param type        a <code>short</code> specifying the event's type
	 * @param time        an <code>int</code> specifiying the moment in time 
	 *                    when the event occures
	 * @param patient     a <code>person</code> as the patient to whom 
	 *                    this event is related 
	 * @param doctor      a <code>person</code> as the doctor to whom 
	 *                    this event is related
	 */
	public Event(short type, int time, Person patient, Person doctor) {
		_type = type;
		_time = time;
		_patient = patient;
		_doctor = doctor;
	}

	////////////////////////////////// getters /////////////////////////////////

	/**
	 * returns the timestamp of this event
	 */
	public int getTime() {
		return _time;
	}

	/**
	 * returns this event's type
	 * @return this event's type
	 */
	public short getType() {
		return _type;
	}

	/**
	 * returns the patient this event is related to
	 * @return the patient this event is related to
	 */
	public Person getPatient() {
		return _patient;
	}

	/**
	 * returns the doctor this event is related to
	 * @return the doctor this event is related to
	 */
	public Person getDoctor() {
		return _doctor;
	}

	/////////////////////////// implementing comparable //////////////////////// 

	/**
	 * Compares this event to another by comparing their timestamps
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Object o) throws ClassCastException {
		return _time - ((Event) o).getTime();
	}

}
