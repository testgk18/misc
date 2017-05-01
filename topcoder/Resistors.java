// Passed System test
/*
 * Problem Statement
     
 A simple network of resistors can be defined recursively as:
 simple network ::= a simple network in parallel with another simple network
 simple network ::= a simple network in series with another simple network
 simple network ::= a single resistor
 If two networks are in parallel with each other and have resistances r1 and r2, then the total resistance is 1/(1/r1+1/r2). If two networks are in series with each other and have resistances r1 and r2, then the total resistance is r1+r2. If a network is a single resistor, then its resistance is defined as the resistance of that single resistor.
 You will be given a simple network of resistors represented as a String[], resistors. Each element of resistors will be either "P", "S", or a decimal number. If an element is a number, then it represents a single resistor with resistance defined by that number. If an element is "P", it defines a simple network whose two component simple networks consist of the next two simple networks in resistors. Similarly, if an element is "S", then the next two simple networks in resistors are in series. Thus, the total number of elements that are numbers will be equal to the total number of elements that are "S" or "P" plus 1. For example, {"S","5.3","P","40","60"} represents a simple network where a single resistor with resistance 5.3 is in series with two resistors in parallel with resistances 40 and 60:
 +----- 40 ----+
 |             |
 ---- 5.3 ---+             +----
 |             |
 +----- 60 ----+
 Your task is to compute the total resistance of the network, and return the result as a double. Small rounding errors will be ignored when examining your result for correctness. As long as your result has a relative or absolute error of less than 1e-9, it will be judged correct.
 Definition
     
 Class:
 Resistors
 Method:
 getResistance
 Parameters:
 String[]
 Returns:
 double
 Method signature:
 double getResistance(String[] resistors)
 (be sure your method is public)
     

 Constraints
 -
 Each element of resistors will be either "P", "S", or a decimal number.
 -
 resistors will contain between 1 and 50 elements, inclusive.
 -
 Each decimal number in resistors will be between 1 and 1000000, inclusive, and will be represented by between 1 and 50 characters, inclusive.
 -
 resistors will represent a valid network, such that for every "P" or "S" both of the subnetworks exist and there are no extraneous elements.
 -
 Each decimal number in resistors will be formatted as a sequence of digits ('0'-'9') with an optional decimal point, which may not be the first or last character of the number.
 Examples
 0)

     
 {"S","5.3","P","40","60"}
 Returns: 29.3
 This is the network above. The resistance of the two resistors in parallel is 1/(1/60 + 1/40) = 24. This resistance, in series with 5.3 gives 29.3 total. Note that your solution need not return exactly 29.3.
 1)

     
 {"S","P","P","1.65","7.59","S","75","1.2","10"}
 Returns: 11.331670926296885
 This represents the following network:
 +--- 1.65 ---+
 |            |
 +----+            +--------+
 |    |            |        |
 |    +--- 7.59 ---+        |
 |                          |
 ----+                          +---- 10 ----
 |                          |
 |                          |
 |                          |
 |                          |
 +-------- 75 ----- 1.2 ----+



 The total resistance is 10 + 1 / (1 / (75 + 1.2) + 1 / 1.65 + 1 / 7.59).
 2)

     
 {"032.350"}
 Returns: 32.35

 This problem statement is the exclusive and proprietary property of TopCoder, Inc. Any unauthorized use or reproduction of this information without the prior written consent of TopCoder, Inc. is strictly prohibited. (c)2003, TopCoder, Inc. All rights reserved.
 */

import java.util.Stack;

public class Resistors {

    public double getResistance(String[] resistors) {

        return makeResistor(resistors).getRes();
    }

    Resistor makeResistor(String[] resistors) {
        Stack s = new Stack();
        Resistor root = null;

        if (resistors.length == 1) {
            return new BasicRes(Double.parseDouble(resistors[0]));
        }

        for (int i = 0; i < resistors.length; ++i) {
            if (resistors[i].equals("P")) {
                Resistor r = new ParaRes();
                s.push(r);
            } else if (resistors[i].equals("S")) {
                Resistor r = new SerialRes();
                s.push(r);
            } else {
                Resistor r = (Resistor) s.peek();
                double d = Double.parseDouble(resistors[i]);
                if (r == null) {
                    return new BasicRes(d);
                }
                if (r instanceof ParaRes) {
                    ParaRes p = (ParaRes) r;
                    if (p.r1 == null) {
                        p.r1 = new BasicRes(d);
                    } else {
                        p.r2 = new BasicRes(d);
                        normalize(s);
                    }
                } else {

                    SerialRes p = (SerialRes) r;
                    if (p.r1 == null) {
                        p.r1 = new BasicRes(d);
                    } else {
                        p.r2 = new BasicRes(d);
                        normalize(s);
                    }
                }
            }
            if (i == 0) {
                root = (Resistor) s.peek();
            }
        }
        return root;

    }

    void normalize(Stack s) {
        if (s.isEmpty()) {
            return;
        }

        Resistor q = (Resistor) s.pop();
        if (s.isEmpty()) {
            return;
        }
        Resistor o = (Resistor) s.peek();

        if (o instanceof ParaRes) {
            ParaRes p = (ParaRes) o;
            if (p.r1 == null) {
                p.r1 = q;
            } else {
                p.r2 = q;
                normalize(s);
            }
        } else {

            SerialRes p = (SerialRes) o;
            if (p.r1 == null) {
                p.r1 = q;
            } else {
                p.r2 = q;
                normalize(s);
            }
        }
    }

    abstract class Resistor {

        abstract double getRes();
    }

    class BasicRes extends Resistor {

        double res;

        BasicRes(double res) {
            this.res = res;
        }

        double getRes() {
            return res;
        }

    }

    class SerialRes extends Resistor {

        Resistor r1;

        Resistor r2;

        SerialRes() {
        }

        SerialRes(Resistor r1, Resistor r2) {
            this.r1 = r1;
            this.r2 = r2;
        }

        double getRes() {
            return r1.getRes() + r2.getRes();
        }

    }

    class ParaRes extends Resistor {
        Resistor r1;

        Resistor r2;

        ParaRes() {
        }

        ParaRes(Resistor r1, Resistor r2) {
            this.r1 = r1;
            this.r2 = r2;
        }

        double getRes() {
            return 1 / (1 / r1.getRes() + 1 / r2.getRes());
        }
    }

}