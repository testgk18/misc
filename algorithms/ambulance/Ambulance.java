package ambulance;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

/**
 * Class ambulance simulates an ambulance. It is open for a specific range of 
 * time. There are n doctors waiting for patients to come. Patients eventually
 * are send to the xray-doctor for shooting some photoes of their bones, and 
 * then come back for further examination. The duration of examinations and 
 * frequency of incoming patients is defined by the computing constants at the
 * top. There are several policies, how the service can be managed.
 * 
 * @author  till zopppke
 */
public class Ambulance {

	////////////////////////// computing constants ///////////////////////////// 

	private static final int OPEN_TIME = 7 * 60 * 60;
	private static final int CLOSE_TIME = 11 * 60 * 60;
	private static final int INCOMING_MY = 30 * 60;
	private static final int FIRST_EXAMINATION_MIN = 5 * 60;
	private static final int FIRST_EXAMINATION_MAX = 20 * 60;
	private static final int XRAY_EXAMINATION_MIN = 5 * 60;
	private static final int XRAY_EXAMINATION_MAX = 10 * 60;
	private static final int SECOND_EXAMINATION_MIN = 10 * 60;
	private static final int SECOND_EXAMINATION_MAX = 30 * 60;
	private static final double XRAY_PROBABILITY = 0.85;
	private static final int NUMBER_OF_DOCTORS = 2;

	////////////////////////// policy constants ////////////////////////////////

	/**
	 * encoding the policy, where persons coming back from the xray shooting
	 * should see the doctor first, but only can see their preferred doctor. 
	 */
	public static final short XRAY_FIRST = 0;

	/**
	 * Encoding the policy, where people see the doctor in order of appereance.
	 */
	public static final short FIRST_COME = 1;

	/**
	 * all policiy-keys in an array
	 */
	public static final short[] POLICIES =
		new short[] { XRAY_FIRST, FIRST_COME };

	/**
	 * a string for each policy. Used for printout.
	 */
	public static final String[] POLICY_STRINGS =
		new String[] { "back from xray preferred", "first come first serve" };

	//////////////////////////////// fields ////////////////////////////////////

	/**
	 * public timer, counting seconds. This is used for computing a person's 
	 * waiting time. The timer is adjusted whenever a new event occures.
	 */
	public static int time;

	// lists for incoming, for xrayWaiters and for those, who are back from xray.
	private LinkedList _examinationWaiters = null;
	private LinkedList _xrayWaiters = null;
	private LinkedList _backFromXrayWaiters = null;

	// eventqueue
	private EventQueue _eventQueue = null;

	// doctors
	private Person[] _doctors;
	private Person _xrayDoctor;

	// list of gone patients.
	private List _gonePatients;

	// minimum and maximum time for all kinds of examination
	private int[] _examinationMins;
	private int[] _examinationMaxs;

	// flag indicating whether there should be detailed output
	private boolean _verbose = false;

	// current policy 
	private short _policy = XRAY_FIRST;

	///////////////////////////// lifecycle ////////////////////////////////////

	/**
	 * default constructor
	 */
	public Ambulance() {
	}

	/**
	 * initializes this ambulance by resetting all variables. Also the initial
	 * events of incoming patients are created and inserted in the evenQueue.
	 * This method must be called before starting the simulation.
	 */
	public void init() {
		// init lists
		_examinationWaiters = new LinkedList();
		_xrayWaiters = new LinkedList();
		_backFromXrayWaiters = new LinkedList();
		_gonePatients = new Vector();

		// init doctors
		_doctors = new Person[NUMBER_OF_DOCTORS];
		for (int i = 0; i < _doctors.length; ++i) {
			_doctors[i] = new Person("d" + (i + 1));
		}
		_xrayDoctor = new Person("x");

		// init examination time computing helper arrays
		_examinationMins =
			new int[] {
				0,
				FIRST_EXAMINATION_MIN,
				XRAY_EXAMINATION_MIN,
				SECOND_EXAMINATION_MIN };
		_examinationMaxs =
			new int[] {
				0,
				FIRST_EXAMINATION_MAX,
				XRAY_EXAMINATION_MAX,
				SECOND_EXAMINATION_MAX };

		// create enterAmbulanceEvents
		Vector v = new Vector();
		int i = 0;
		for (int t = OPEN_TIME + getPoisson();
			t < CLOSE_TIME;
			t += getPoisson()) {
			v.add(
				new Event(Event.NEW_PATIENT, t, new Person("p" + (++i)), null));
		}

		// init eventQueue
		_eventQueue = new EventQueue(v);

		// init timer
		time = OPEN_TIME;
	}

	/**
	 * Starts the simulation. All doctors are started waiting at OPEN_TIME, then 
	 * events are taken from the eventQueue and handled one by one until no more
	 * left, finally all doctors are stopped waiting.
	 */
	public void start() {
		// doctors start waiting at OPEN_TIME
		for (int i = 0; i < _doctors.length; ++i) {
			_doctors[i].startWaiting();
		}
		_xrayDoctor.startWaiting();
		printTimer();
		print("Start of simulation.");
		print("All doctors start waiting.");

		// recurse on all events
		while (_eventQueue.hasMoreElements()) {
			Event e = (Event) _eventQueue.nextElement();
			processEvent(e);
		}

		// doctors stop waiting at CLOSE_TIME, or when the last patient left.
		time = Math.max(time, CLOSE_TIME);
		for (int i = 0; i < _doctors.length; ++i) {
			_doctors[i].stopWaiting();
		}
		_xrayDoctor.stopWaiting();
		printTimer();
		print("All doctors stop waiting.");
		print("End of simulation.");
	}

	//////////////////////////// public methods ////////////////////////////////

	/**
	 * Sets the verbosity flag to the specified value
	 */
	public void setVerbose(boolean verbose) {
		_verbose = verbose;
	}

	/**
	 * Sets the policy to the specified value
	 * @param policy a <code>short</code> encoding the kind of policy.
	 */
	public void setPolicy(short policy) {
		_policy = policy;
	}

	/**
	 * Returns a list containing all patients that the ambulance finished 
	 * surving.
	 * @return a List of all patients that gone home.
	 */
	public List getGonePatients() {
		return _gonePatients;
	}

	/**
	 * Returns a List containing all Doctors except the xray doctor
	 * @return a List containing all Doctors except the xray doctor
	 */
	public Person[] getDoctors() {
		return _doctors;
	}

	/**
	 * Returns the xray-doctor
	 * @return the xray-doctor
	 */
	public Person getXrayDoctor() {
		return _xrayDoctor;
	}

	//////////////////////////// private methods ///////////////////////////////

	// handles all events. The timer is updated to the event's timestamp.
	// person's status is adjusted, waiting-lists are updated and checked.
	// follow-up events are created.
	private void processEvent(Event e) {
		// adjust timer
		time = e.getTime();
		printTimer();

		// get related patient and doctor. Start them waiting. Get preferredDoctor
		Person p = e.getPatient();
		p.startWaiting();
		Person d = e.getDoctor();
		if (d != null) {
			d.startWaiting();
		}
		Person preferredDoctor = p.getPreferredDoctor();

		// switch on the event type
		switch (e.getType()) {
			case Event.NEW_PATIENT :
				// new patient entered ambulance. Add him to incoming waiters
				// and check if doctors are waiting
				print(
					"New patient: " + p + ", add to examination-waiting-list");
				_examinationWaiters.addLast(p);
				checkExamination();
				break;

			case Event.FIRST_EXAMINATION :
				// end of 1st examination
				print("1st examination end: " + p + ", " + d);
				if (needXray()) {
					// if xray needed, add patient to the xray waiters
					// and check if xray-doctor is waiting
					print("Xray needed. Add " + p + " to xray-waiting-list");
					p.setPreferredDoctor(d);
					_xrayWaiters.addLast(p);
					checkXRay();
				} else {
					// if no xray needed, then patient can go home.
					print("No xray needed. Can go home now: " + p);
					_gonePatients.add(p);
				}
				// check if incoming patients are waiting
				checkExamination();
				break;

			case Event.XRAY_SHOOTING :
				// end of xray-examination. Add Patient to waiters.
				// Check if any xray waiters and if doctors are waiting
				print("Xray-shooting end: " + p);
				if (_policy == XRAY_FIRST) {
					print("Add to back-from-xray-waiters: " + p);
					_backFromXrayWaiters.addLast(p);
				} else {
					print("Add to examination-waiting-list: " + p);
					_examinationWaiters.addLast(p);
				}
				checkExamination();
				checkXRay();
				break;

			case Event.SECOND_EXAMINATION :
				// end of second examination. Patient can go home now.
				// check if incoming patients are waiting.
				print("2nd examination end: " + p + ", " + d);
				print("Can go home: " + p);
				_gonePatients.add(p);
				checkExamination();
				break;
		}
	}

	// checks, whether the xray-doctor is free and a patient is waiting for him.
	// In this case a xray-shooting is started and the related event added to
	// the event queue.
	private void checkXRay() {
		print("Checking xray-waiting-list...");
		if (_xrayDoctor.isWaiting() && _xrayWaiters.size() > 0) {
			print("Xray-shooting starts: " + _xrayWaiters.getFirst());
			int t = computeExaminationTime(Event.XRAY_SHOOTING);
			Person p = (Person) _xrayWaiters.removeFirst();
			_eventQueue.insert(
				new Event(Event.XRAY_SHOOTING, time + t, p, _xrayDoctor));
			_xrayDoctor.stopWaiting();
			p.stopWaiting();
		}
	}

	// Checks, whether any doctor is free and can take one of the waiting
	// patients for examination, if there is any. According to current policy
	// back-from-xray-Waiters might be preferred.
	private void checkExamination() {
		print("Checking examination-waiting-list...");
		List l = getWaitingDoctors();
		for (int i = 0; i < l.size(); ++i) {
			Person d = (Person) l.get(i);
			boolean done = false;
			print("Checking if doctor " + d + " can take a patient...");

			// check backFromXrayWaiters first
			for (int j = 0; !done && j < _backFromXrayWaiters.size(); ++j) {
				Person p = (Person) _backFromXrayWaiters.get(j);
				print(
					"Back from xray-shooting: "
						+ p
						+ ", preferred doctor: "
						+ p.getPreferredDoctor()
						+ "...");
				if (p.getPreferredDoctor() == d) {
					// preferred doctor is waiting. 
					int t = computeExaminationTime(Event.SECOND_EXAMINATION);
					_eventQueue.insert(
						new Event(Event.SECOND_EXAMINATION, time + t, p, d));
					_backFromXrayWaiters.remove(j);
					p.stopWaiting();
					d.stopWaiting();
					done = true;
					print("2nd examination start: " + p + ", " + d);
				}
			}
			// none of the xrayWaiters could be served by this doctor.
			// so take the first of the incoming, if there is any
			if (!done && _examinationWaiters.size() > 0) {
				Person p = (Person) _examinationWaiters.removeFirst();
				print(
					"waiting for examination: "
						+ p
						+ ", preferred doctor: "
						+ p.getPreferredDoctor()
						+ "...");
				if (p.getPreferredDoctor() == null) {
					// 1st examination.  
					print("1st examination start: " + p + ", " + d);
					int t = computeExaminationTime(Event.FIRST_EXAMINATION);
					_eventQueue.insert(
						new Event(Event.FIRST_EXAMINATION, time + t, p, d));
				} else {
					// 2nd examination
					print("2nd examination start: " + p + ", " + d);
					int t = computeExaminationTime(Event.SECOND_EXAMINATION);
					_eventQueue.insert(
						new Event(Event.SECOND_EXAMINATION, time + t, p, d));
				}
				p.stopWaiting();
				d.stopWaiting();
			}
		}
	}

	// computes the examination time according to type of examination.
	private int computeExaminationTime(short type) {
		return (int)
			(_examinationMins[type]
				+ (_examinationMaxs[type] - _examinationMins[type])
					* Math.random());
	}

	// returns a randomly shuffled list of all doctors currently not examining
	// a patient.
	private List getWaitingDoctors() {
		Vector v = new Vector();
		for (int i = 0; i < _doctors.length; ++i) {
			if (_doctors[i].isWaiting()) {
				v.add(_doctors[i]);
			}
		}
		Collections.shuffle(v);
		return v;
	}

	// computes the time between two incoming events given as poisson process
	private int getPoisson() {
		return (int) (-INCOMING_MY * Math.log(Math.random()));
	}

	// computes whether an xray-shooting is necessary.
	private boolean needXray() {
		return Math.random() < XRAY_PROBABILITY;
	}

	// prints the given string if verbosity flag is set. 
	private void print(String s) {
		if (_verbose) {
			System.out.println(s);
		}
	}

	// prints the current timer
	private void printTimer() {
		if (_verbose) {
			System.out.println("");
			System.out.println("-- " + getTimeString() + " --");
		}
	}

	////////////////////////////// static methods //////////////////////////////

	/**
	 * returns a string representation of the current timer
	 */
	public static String getTimeString() {
		return getTimeString(time);
	}

	/**
	 * returns a string representation of the specified time.
	 */
	public static String getTimeString(int t) {
		int hours = t / 3600;
		int minutes = (t / 60) % 60;
		int seconds = t % 60;
		return (hours < 10 ? "0" : "")
			+ hours
			+ (minutes < 10 ? ":0" : ":")
			+ minutes
			+ (seconds < 10 ? ":0" : ":")
			+ seconds;
	}

	/**
	 * Returns an array containing keys for all policies
	 * @return an array containing keys for all policies
	 */
	public static short[] getPolicies() {
		return POLICIES;
	}

	/**
	 * returns a string representation of a policy
	 * @param policy a <code>short</code> specifying a key for a policy 
	 * @return a string representation of a policy
	 */
	public static String getPolicyString(short policy) {
		return POLICY_STRINGS[policy];
	}
}
