package ti.camview;

/**
 * Component of the FilmMaker-GUI. Input for the filter-option.
 * Filter-option means: if you selct e.g. 15 minutes, the Camera.getImage()-method is
 * only looking for pictures, that are shot at most these minutes before or after the ideal
 * point of time.
 * Creation date: (05.07.00 14:42:24)
 * @author: 
 */
public class FilterManager extends java.awt.Panel {
	private java.awt.CheckboxGroup cbg;
	private java.awt.Checkbox filterBox;

	private class filterListener implements java.awt.event.ItemListener {
		private filterListener() {}
		public void itemStateChanged(java.awt.event.ItemEvent ev)
		{
			// enabling the input for the filter-amount if selected.
			intervalManager.setVisible(filterBox.getState());
		}
	}
	private IntervalManager intervalManager;
	private java.awt.Checkbox noFilterBox;
	private java.awt.GridBagConstraints constraints;
	private java.awt.GridBagLayout gridbag;
/**
 * Constructs a new FilterManager-Object. You may choose a color.
 * Creation date: (05.07.00 15:31:55)
 * @param bgc java.awt.Color
 */
public FilterManager(java.awt.Color bgc) {
	super();
	setBackground(bgc);
	initialize();
	
}
/**
 * returns the amount of the filter in minutes, if selected.
 * returns Integer.MAX_VALUE, if not selected.
 * Creation date: (05.07.00 15:19:45)
 * @return int
 */
public int getFilter() {
	if (filterBox.getState())
		return intervalManager.getInterval();
	else
		return Integer.MAX_VALUE;

}
/**
 * Insert the method's description here.
 * Creation date: (05.07.00 14:46:51)
 */
private void initialize() {

	setLayout(null);
	
	cbg = new java.awt.CheckboxGroup();
	filterBox = new java.awt.Checkbox("Filter", cbg, false);
	noFilterBox = new java.awt.Checkbox("no filter", cbg, true);
	intervalManager = new IntervalManager(getBackground());
	filterBox.addItemListener(new filterListener());
	noFilterBox.addItemListener(new filterListener());	
	
	add(noFilterBox);
	noFilterBox.setBounds (5, 3, 80, 21);
	noFilterBox.setVisible(true);

	add(filterBox);
	filterBox.setBounds (5, 23, 80, 21);
	filterBox.setVisible(true);
	
	add(intervalManager);
	intervalManager.setBounds(130, 15, 110, 28);
	intervalManager.setVisible(false);
}
}
