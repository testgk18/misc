package civ.io;

import java.util.Vector;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import civ.world.UnitType;

public class UnitTypeHandler extends DefaultHandler {

	private static final String TYPE = "type", NAME = "name",
			ATTACK = "attack", DEFENSE = "defense", ENERGY = "energy";

	// vector to store unit types
	private Vector<UnitType> _typesVector = new Vector<UnitType>();

	// currently parsing unit type
	private UnitType _tmpType;

	public UnitTypeHandler() {
		// empty
	}

	public UnitType[] getUnitTypes() {
		return (UnitType[]) _typesVector.toArray();
	}

	// //////////////////////// defaultHandler methods ////////////////////////

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attrs) throws SAXException {

		try {
			if (qName.equals(TYPE)) {

				// create new temporary type and parse attributes
				_tmpType = new UnitType();
				_tmpType.setName(attrs.getValue(NAME));
				_tmpType.setAttack(Double.parseDouble(attrs.getValue(ATTACK)));
				_tmpType.setDefense(Double.parseDouble(attrs.getValue(DEFENSE)));
				_tmpType.setEnergy(Double.parseDouble(attrs.getValue(ENERGY)));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new SAXException(e);
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {

		try {
			if (qName.equals(TYPE)) {

				// Store unit type to vector
				_typesVector.add(_tmpType);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new SAXException(e);
		}
	}

}
