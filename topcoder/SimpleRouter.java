// passed system test
/*
 * Problem Statement      A router's job is to route packets of information to
 * the correct computer. In addition, a router may throw out some packets, or
 * handle the packets on its own. In this problem, you are to implement the
 * software for a simple, rule-based router. Each rule in the router will take
 * one of the following forms (quotes and angle brackets for clarity only):
 * "ACCEPT <IP_RANGE> <PORT_RANGE>" "REJECT <IP_RANGE> <PORT_RANGE>" "FORWARD
 * <IP_RANGE> <PORT_RANGE> <DESTINATION> ( <PORT>)" Each <IP_RANGE> is a string
 * of exactly four <NUMBER_RANGE>s, separated by periods, and each <PORT_RANGE>
 * consists of a single <NUMBER_RANGE>. A <NUMBER_RANGE> can take one of three
 * forms. It may be a single integer, a range of integers (in the form "
 * <LOWER_LIMIT>- <UPPER_LIMIT>", where both limits are inclusive), or an
 * asterisk. <DESTINATION> consists of exactly 4 integers, with 3 periods
 * separating them (an IP address). If a FORWARD rule has a <PORT_RANGE> with
 * only a single integer, then the <DESTINATION> may optionally be followed by a
 * single integer, <PORT>. Each rule tells the router what to do with a packet
 * of information if that packet comes from an IP in the rule's <IP_RANGE> and
 * to a port in the rule's <PORT_RANGE>. An IP is in the <IP_RANGE> if each
 * <NUMBER_RANGE> in the <IP_RANGE> matches the corresponding number in the IP.
 * A <NUMBER_RANGE> matches a number, N, if the <NUMBER_RANGE> is an asterisk,
 * if it is a single number that is the same as N, or if it is a range and N
 * falls within the range, inclusive. The rules for matching a <PORT_RANGE> are
 * the same. If a rule tells the router to forward the packet, then it should be
 * forwarded to <DESTINATION>. If no <PORT> is specified, the packet should be
 * forwarded to the same port it was received on. Otherwise, it should be
 * forwarded to the specified port. If multiple rules apply to a packet, you
 * should use the one that comes last in the input. If no rules apply, REJECT
 * the packet. You will be given a String[], rules, representing a number of
 * rules that the router is to follow. You will also be given a String[],
 * packets, each of whose elements represents a packet of data in the form "
 * <SOURCE_IP> <PORT>" ( <SOURCE_IP> is formatted the same as <DESTINATION>).
 * You should return a String[] with one element per packet, specifying what to
 * do with the packet with the same index in the input as the return. Each
 * element of the return should be either "ACCEPT", "REJECT", or " <IP>:
 * <PORT>", where <IP> and <PORT> represent the location to which the packet
 * should be forwarded. Definition      Class: SimpleRouter Method: route
 * Parameters: String[], String[] Returns: String[] Method signature: String[]
 * route(String[] rules, String[] packets) (be sure your method is public)     
 * 
 * Notes - While the input may have extraneous leading zeros, your return should
 * not. Constraints - rules will contain between 1 and 50 elements, inclusive. -
 * Each element of rules will be formatted as described in the problem
 * statement. - packets will contain between 1 and 50 elements, inclusive. -
 * Each element of packets will be formatted as described in the problem
 * statement. - Each of the four numbers in an IP address, or a number range in
 * an IP address will be between 0 and 255, inclusive. - Each port or number in
 * a port range will be between 1 and 65535 inclusive. - In any <NUMBER_RANGE>
 * with two numbers, <LOWER_LIMIT> will be less than or equal to <UPPER_LIMIT>.
 * Examples 0)
 * 
 *      {"FORWARD 192.168.000.* 001-100 192.168.0.10", "FORWARD 192.168.0.1 80
 * 10.10.95.184 8080", "ACCEPT 192.168.*.* 25", "REJECT 192.168.5.38 *"}
 * {"192.168.0.43 80", "00192.00168.000.001 00080", "192.168.0.1 110",
 * "192.168.1.73 80", "192.168.1.73 25", "206.26.210.5 53", "192.168.5.38 25" }
 * Returns: { "192.168.0.10:80", "10.10.95.184:8080", "REJECT", "REJECT",
 * "ACCEPT", "REJECT", "REJECT" } Packet 0 matches rule 0, and gets forwarded
 * according to that rule. Packet 1 matches both rules 0 and 1, so rule 1 is
 * applied. Packets 2, 3, and 5 don't match any rules, so they are rejected.
 * Packet 4 matches rule 2, and is therefore accepted. Packet 6 matches rules 2
 * and 3, so it gets rejected (rule 3 is applied). 1)
 * 
 *      {"FORWARD *.*.*.* * 192.168.0.1"} {"213.148.161.82 9484",
 * "172.230.108.145 16627", "122.141.122.130 46874", "241.145.145.77 26390",
 * "139.97.106.125 35305", "244.131.151.77 26390"} Returns: {
 * "192.168.0.1:9484", "192.168.0.1:16627", "192.168.0.1:46874",
 * "192.168.0.1:26390", "192.168.0.1:35305", "192.168.0.1:26390" }
 * 
 * 2)
 * 
 *      {"REJECT *.20-252.114-157.36-91 13171-54085", "ACCEPT *.*.73-180.* *",
 * "FORWARD 55.63.173.239 * 168.154.33.25", "REJECT *.72-73.*.48-191 *", "REJECT
 * 20.51.*.* 4579", "ACCEPT 70-166.*.*.86-182 *", "REJECT 88-190.*.119-157.*
 * 3316-27844", "FORWARD *.52-221.134-250.66-207 * 116.94.120.82"}
 * {"203.11.104.45 44072", "154.92.128.87 30085", "20.51.68.55 4579",
 * "177.73.138.69 14319", "112.65.145.82 26287", "55.63.173.239 45899"} Returns: {
 * "ACCEPT", "ACCEPT", "REJECT", "116.94.120.82:14319", "116.94.120.82:26287",
 * "168.154.33.25:45899" }
 * 
 * This problem statement is the exclusive and proprietary property of TopCoder,
 * Inc. Any unauthorized use or reproduction of this information without the
 * prior written consent of TopCoder, Inc. is strictly prohibited. (c)2003,
 * TopCoder, Inc. All rights reserved.
 */

import java.util.StringTokenizer;

public class SimpleRouter {

    public String[] route(String[] rules, String[] packets) {
        Rule[] myRules = new Rule[rules.length];
        for (int i = 0; i < rules.length; ++i) {
            myRules[i] = new Rule(rules[i]);
        }
        String[] retour = new String[packets.length];
        for (int i = 0; i < packets.length; ++i) {
            int[] ips = new int[4];
            StringTokenizer tt = new StringTokenizer(packets[i]);
            StringTokenizer tt2 = new StringTokenizer(tt.nextToken(), ".");
            int c = 0;
            while (tt2.hasMoreTokens()) {
                ips[c++] = Integer.parseInt(tt2.nextToken());
            }
            int port = Integer.parseInt(tt.nextToken());
            boolean done = false;
            for (int j = rules.length - 1; j >= 0 && !done; --j) {
                String s = myRules[j].process(ips, port);
                if (s != null) {
                    retour[i] = s;
                    done = true;
                }
            }
            if (!done) {
                retour[i] = "REJECT";
            }
        }
        return retour;
    }

    class Rule {

        String kind;

        int[] iprange1 = new int[4];

        int[] iprange2 = new int[4];

        int port1;

        int port2;

        String forward, forwardport;

        Rule(String r) {
            StringTokenizer tt = new StringTokenizer(r);
            kind = tt.nextToken();
            StringTokenizer tt2 = new StringTokenizer(tt.nextToken(), ".");
            int c = 0;
            while (tt2.hasMoreTokens()) {
                String s = tt2.nextToken();
                if (s.equals("*")) {
                    iprange1[c] = 0;
                    iprange2[c] = 255;
                } else if (s.indexOf('-') >= 0) {
                    StringTokenizer tt3 = new StringTokenizer(s, "-");
                    iprange1[c] = Integer.parseInt(tt3.nextToken());
                    iprange2[c] = Integer.parseInt(tt3.nextToken());
                } else {
                    iprange1[c] = Integer.parseInt(s);
                    iprange2[c] = Integer.parseInt(s);
                }
                ++c;
            }
            String s = tt.nextToken();
            if (s.equals("*")) {
                port1 = 0;
                port2 = 33333333;
            } else if (s.indexOf('-') >= 0) {
                StringTokenizer tt3 = new StringTokenizer(s, "-");
                port1 = Integer.parseInt(tt3.nextToken());
                port2 = Integer.parseInt(tt3.nextToken());
            } else {
                port1 = Integer.parseInt(s);
                port2 = Integer.parseInt(s);
            }
            if (tt.hasMoreTokens()) {
                StringTokenizer tt4 = new StringTokenizer(tt.nextToken(), ".");
                forward = "" + Integer.parseInt(tt4.nextToken());
                forward += ".";
                forward += "" + Integer.parseInt(tt4.nextToken());
                forward += ".";
                forward += "" + Integer.parseInt(tt4.nextToken());
                forward += ".";
                forward += "" + Integer.parseInt(tt4.nextToken());
            }
            if (tt.hasMoreTokens()) {
                forwardport = tt.nextToken();
            }
        }

        String process(int[] ip, int port) {
            boolean b = true;
            for (int i = 0; i < 4; ++i) {
                if (ip[i] < iprange1[i] || ip[i] > iprange2[i]) {
                    b = false;
                }
            }
            if (port1 > port || port2 < port) {
                b = false;
            }
            if (!b) {
                return null;
            }
            if (kind.equals("ACCEPT")) {
                return "ACCEPT";
            } else if (kind.equals("REJECT")) {
                return "REJECT";
            } else {
                if (forwardport != null) {
                    return forward + ":" + Integer.parseInt(forwardport);
                } else {
                    return forward + ":" + port;
                }
            }
        }

    }

    public static void main(String[] args) {
        SimpleRouter inst = new SimpleRouter();

        String[] s1 = { "FORWARD *.*.*.* * 00.012.00000.099",
                "FORWARD 192.168.0.1 110 00.00.00.00 000001" };
        String[] s2 = { "213.148.161.82 9484", "172.230.108.145 16627",
                "122.141.122.130 46874", "241.145.145.77 26390",
                "139.97.106.125 35305", "244.131.151.77 26390" };
        String[] r = inst.route(s1, s2);
        System.out.println(r[0]);
        System.out.println(r[1]);
        System.out.println(r[2]);
        System.out.println(r[3]);
    }
}