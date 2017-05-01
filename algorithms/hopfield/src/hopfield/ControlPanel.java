package hopfield;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ControlPanel extends JPanel
		implements
			ControlListener,
			HopfieldListener {

	private static boolean[][] buttonAbilities = new boolean[][]{
			{false, false, true, false, true, true},
			{false, false, false, true, true, true},
			{true, true, false, true, true, true},};

	// references to data objects
	Control control = null;
	private Hopfield hopfield = null;

	// textfields for statistics
	private JTextField[] statFields = new JTextField[Hopfield.statisticsStrings.length];

	// action buttons
	private HButton[] hbuttons = new HButton[6];

	// small panels for grouping the gui elements
	private JPanel trainingPanel = new JPanel();
	private JPanel actionPanel = new JPanel();

	public ControlPanel() {
		// nop
	}

	public void init() {
		this.setLayout(new GridBagLayout());

		// ////////////////////////////// statistics /////////////////////////

		this.trainingPanel.setLayout(new GridBagLayout());
		this.trainingPanel.setBorder(BorderFactory
				.createTitledBorder("statistics"));

		// iterate over 5 textFields
		for (short i = 0; i < Hopfield.statisticsStrings.length; ++i) {
			this.statFields[i] = new JTextField(7);
			this.statFields[i].setText(Integer.toString(this.hopfield
					.getStatistics(i)));
			this.statFields[i].setEditable(false);
			this.trainingPanel.add(new JLabel(Hopfield.statisticsStrings[i]),
					new GridBagConstraints(0, i, 1, 1, 1.0, 0.0,
							GridBagConstraints.EAST,
							GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0,
									5), 0, 0));
			this.trainingPanel.add(this.statFields[i],
					new GridBagConstraints(1, i, 1, 1, 1.0, 0.0,
							GridBagConstraints.WEST,
							GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0,
									0), 0, 0));
		}

		// /////////////////////////// actionPanel ////////////////////////////

		this.actionPanel.setLayout(new GridBagLayout());
		this.actionPanel.setBorder(BorderFactory.createTitledBorder("actions"));

		// construct and add buttons
		for (short i = 0; i < Control.commandStrings.length; ++i) {
			this.hbuttons[i] = new HButton(i);
			this.hbuttons[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					short command = ((HButton) e.getSource()).command;
					ControlPanel.this.control.doCommand(command);
				}
			});
			this.actionPanel.add(this.hbuttons[i],
					new GridBagConstraints(i % 2, i / 2, 1, 1, 1.0, 0.0,
							GridBagConstraints.EAST,
							GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0,
									5), 0, 0));
		}

		// init button states
		this.statusChanged(Control.STOPPED);

		// add panels
		this.add(this.trainingPanel, new GridBagConstraints(0, 0, 1, 1, 1.0,
				1.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH,
				new Insets(5, 5, 5, 5), 0, 0));
		this.add(this.actionPanel, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.SOUTH, GridBagConstraints.BOTH, new Insets(
						5, 5, 5, 5), 0, 0));

		// add this as controlListener
		this.control.addControlListener(this);
		this.hopfield.addHopfieldListener(this);
	}

	// ////////////////////// controlListener methods /////////////////////////

	public void statusChanged(short status) {

		// enable or disable buttons
		for (int i = 0; i < this.hbuttons.length; ++i) {
			this.hbuttons[i].setEnabled(buttonAbilities[status][i]);
		}

		// set status message
	}

	public void hopfieldChanged() {
		for (int i = 0; i < this.statFields.length; ++i) {
			this.statFields[i].setText(Integer
					.toString(this.hopfield.statistics[i]));
		}
	}

	// ////////////////////// inner class HopfieldButton //////////////////////

	class HButton extends JButton {
		short command;

		HButton(short command) {
			super(Control.commandStrings[command]);
			this.command = command;
		}
	}

	public void setControl(Control control) {
		this.control = control;
	}

	public void setHopfield(QueensHopfield hopfield) {
		this.hopfield = hopfield;
	}
}
