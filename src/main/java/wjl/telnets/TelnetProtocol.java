package wjl.telnets;

import java.io.IOException;
import java.io.InputStream;

public class TelnetProtocol {
    static final int SE =  240; // Marks end of sub negotiation.
    static final int NOP = 241; // No operation
    static final int DM =  242; // Data mark
    static final int BRK = 243; // Break
    static final int IP =  244; // Interrupt Process
    static final int AO =  245; // Abort Output
    static final int AYT = 246; // Are You There
    static final int EC =  247; // Erase Char
    static final int EL =  248; // Erase Line
    static final int GA =  249; // Go Ahead
    static final int SB =  250; // Marks start of a sub negotiation.
    static final int WILL = 251; // Negotiation: Will do option
    static final int WONT = 252; // Negotiation: Wont do option
    static final int DO =   253; // Negotiation: Do option
    static final int DONT = 254; // Negotiation:  Don't do option
    static final int IAC =  255; // Interpret As Command

    static void skipTelnetOption(InputStream in) throws IOException {
        int cmd1 = in.read();
        switch (cmd1) {
            case AYT:
            case IP:
            case EC:
            case BRK:
            case AO:
            case EL:
            case NOP:
                return;
        }

        int cmd2 = in.read();
        if (cmd2 == SB) {
            int ch;
            do {
                ch = in.read();
            } while (ch != SE);
        }
    }
}
