import java.util.ArrayList;
import java.util.List;

public class clsInstructions {

	    static public class instruction{
	    String mnemonic;
	    Integer opcode;
	    clsAdressingModes.AdressingMode mode;

	    public instruction(String mnemonic, Integer opcode, clsAdressingModes.AdressingMode mode) {
	        this.mnemonic = mnemonic;
	        this.opcode = opcode;
	        this.mode = mode;
	    }
	}
    static public instruction inst;
    static public List<instruction> table = new ArrayList<>();

    public clsInstructions() {
        table.add(new instruction("LDA", 0x86,  clsAdressingModes.AdressingMode.Immediat));
        table.add(new instruction("LDA", 0x96,  clsAdressingModes.AdressingMode.Direct));
        table.add(new instruction("LDA", 0xA6,  clsAdressingModes.AdressingMode.IndexeDirect));
        table.add(new instruction("LDA", 0xA6,  clsAdressingModes.AdressingMode.IndexeIndirect));
        table.add(new instruction("LDA", 0xB6,  clsAdressingModes.AdressingMode.EtenduDirect));
        table.add(new instruction("LDA", 0xB69F,  clsAdressingModes.AdressingMode.EtenduIndirect));
        

        table.add(new instruction("LDB", 0xC6, clsAdressingModes.AdressingMode.Immediat));
        table.add(new instruction("LDB", 0xD6, clsAdressingModes.AdressingMode.Direct));
        table.add(new instruction("LDB", 0xE6,  clsAdressingModes.AdressingMode.IndexeDirect));
        table.add(new instruction("LDB", 0xE6,  clsAdressingModes.AdressingMode.IndexeIndirect));
        table.add(new instruction("LDB", 0xF6,  clsAdressingModes.AdressingMode.EtenduDirect));
        table.add(new instruction("LDB", 0xF69F,  clsAdressingModes.AdressingMode.EtenduIndirect));

        table.add(new instruction("LDD", 0xCC,  clsAdressingModes.AdressingMode.Immediat));
        table.add(new instruction("LDD", 0xDC,  clsAdressingModes.AdressingMode.Direct));
        table.add(new instruction("LDD", 0xEC, clsAdressingModes.AdressingMode.IndexeDirect));
        table.add(new instruction("LDD", 0xFC,  clsAdressingModes.AdressingMode.EtenduDirect));
        table.add(new instruction("LDD", 0xFC9F,  clsAdressingModes.AdressingMode.EtenduIndirect));
        
        table.add(new instruction("LDS", 0x10CE,  clsAdressingModes.AdressingMode.Immediat));
        table.add(new instruction("LDS", 0x10DE,  clsAdressingModes.AdressingMode.Direct));
        table.add(new instruction("LDS", 0x10EE,  clsAdressingModes.AdressingMode.IndexeDirect));
        table.add(new instruction("LDS", 0x10FE,  clsAdressingModes.AdressingMode.EtenduDirect));
        table.add(new instruction("LDS", 0x10FE9F,  clsAdressingModes.AdressingMode.EtenduIndirect));
        
        table.add(new instruction("LDU", 0xCE,  clsAdressingModes.AdressingMode.Immediat));
        table.add(new instruction("LDU", 0xDE, clsAdressingModes.AdressingMode.Direct));
        table.add(new instruction("LDU", 0xEE, clsAdressingModes.AdressingMode.IndexeDirect));
        table.add(new instruction("LDU", 0xFE,  clsAdressingModes.AdressingMode.EtenduDirect));
        table.add(new instruction("LDU", 0xFE9F,  clsAdressingModes.AdressingMode.EtenduIndirect));
        
        table.add(new instruction("LDX", 0x8E, clsAdressingModes.AdressingMode.Immediat));
        table.add(new instruction("LDX", 0x9E, clsAdressingModes.AdressingMode.Direct));
        table.add(new instruction("LDX", 0xAE, clsAdressingModes.AdressingMode.IndexeDirect));
        table.add(new instruction("LDX", 0xBE, clsAdressingModes.AdressingMode.EtenduDirect));
        table.add(new instruction("LDX", 0xBE9F, clsAdressingModes.AdressingMode.EtenduIndirect));
        
        table.add(new instruction("LDY", 0x108E, clsAdressingModes.AdressingMode.Immediat));
        table.add(new instruction("LDY", 0x109E, clsAdressingModes.AdressingMode.Direct));
        table.add(new instruction("LDY", 0x10AE, clsAdressingModes.AdressingMode.IndexeDirect));
        table.add(new instruction("LDY", 0x10BE, clsAdressingModes.AdressingMode.EtenduDirect));
        table.add(new instruction("LDY", 0x10BE9F, clsAdressingModes.AdressingMode.EtenduIndirect));
        
        table.add(new instruction("INCA", 0x4C,  clsAdressingModes.AdressingMode.INHERENT));
        table.add(new instruction("INCB", 0x5C,  clsAdressingModes.AdressingMode.INHERENT));
        
       
        table.add(new instruction("INC", 0x0C,  clsAdressingModes.AdressingMode.Direct));
        table.add(new instruction("INC", 0x6C, clsAdressingModes.AdressingMode.IndexeDirect));
        table.add(new instruction("INC", 0x7C,  clsAdressingModes.AdressingMode.EtenduDirect));
        table.add(new instruction("INC", 0x7C9F,  clsAdressingModes.AdressingMode.EtenduIndirect));
        
        table.add(new instruction("DECA", 0x4A,  clsAdressingModes.AdressingMode.INHERENT));
        table.add(new instruction("DECB", 0x5A,  clsAdressingModes.AdressingMode.INHERENT));
        
       
        table.add(new instruction("DEC", 0x0A,  clsAdressingModes.AdressingMode.Direct));
        table.add(new instruction("DEC", 0x6A, clsAdressingModes.AdressingMode.IndexeDirect));
        table.add(new instruction("DEC", 0x7A,  clsAdressingModes.AdressingMode.EtenduDirect));
        table.add(new instruction("DEC", 0x7A9F, clsAdressingModes.AdressingMode.EtenduIndirect));
        
        table.add(new instruction("CMPA", 0x81, clsAdressingModes.AdressingMode.Immediat));
        table.add(new instruction("CMPA", 0x91, clsAdressingModes.AdressingMode.Direct));
        table.add(new instruction("CMPA", 0xA1, clsAdressingModes.AdressingMode.IndexeDirect));
        table.add(new instruction("CMPA", 0xB1,  clsAdressingModes.AdressingMode.EtenduDirect));
        table.add(new instruction("CMPA", 0xB19F,  clsAdressingModes.AdressingMode.EtenduIndirect));
        
        table.add(new instruction("CMPB", 0xC1,  clsAdressingModes.AdressingMode.Immediat));
        table.add(new instruction("CMPB", 0xD1,  clsAdressingModes.AdressingMode.Direct));
        table.add(new instruction("CMPB", 0xE1, clsAdressingModes.AdressingMode.IndexeDirect));
        table.add(new instruction("CMPB", 0xF1,  clsAdressingModes.AdressingMode.EtenduDirect));
        table.add(new instruction("CMPB", 0xF19F,  clsAdressingModes.AdressingMode.EtenduIndirect));
        
        table.add(new instruction("CMPD", 0x1083,  clsAdressingModes.AdressingMode.Immediat));
        table.add(new instruction("CMPD", 0x1093,  clsAdressingModes.AdressingMode.Direct));
        table.add(new instruction("CMPD", 0x10A3,  clsAdressingModes.AdressingMode.IndexeDirect));
        table.add(new instruction("CMPD", 0x10B3,  clsAdressingModes.AdressingMode.EtenduDirect));
        table.add(new instruction("CMPD", 0x10B39F,  clsAdressingModes.AdressingMode.EtenduIndirect));
        
        table.add(new instruction("CMPS", 0x118C,  clsAdressingModes.AdressingMode.Immediat));
        table.add(new instruction("CMPS", 0x119C,  clsAdressingModes.AdressingMode.Direct));
        table.add(new instruction("CMPS", 0x11AC,  clsAdressingModes.AdressingMode.IndexeDirect));
        table.add(new instruction("CMPS", 0x11BC,  clsAdressingModes.AdressingMode.EtenduDirect));
        table.add(new instruction("CMPS", 0x11BC9F,  clsAdressingModes.AdressingMode.EtenduIndirect));
        
        table.add(new instruction("CMPU", 0x1183,  clsAdressingModes.AdressingMode.Immediat));
        table.add(new instruction("CMPU", 0x1193,  clsAdressingModes.AdressingMode.Direct));
        table.add(new instruction("CMPU", 0x11A3,  clsAdressingModes.AdressingMode.IndexeDirect));
        table.add(new instruction("CMPU", 0x11B3,  clsAdressingModes.AdressingMode.EtenduDirect));
        table.add(new instruction("CMPU", 0x11B39F, clsAdressingModes.AdressingMode.EtenduIndirect));
        
        table.add(new instruction("CMPX", 0x8C,  clsAdressingModes.AdressingMode.Immediat));
        table.add(new instruction("CMPX", 0x9C,  clsAdressingModes.AdressingMode.Direct));
        table.add(new instruction("CMPX", 0xAC,  clsAdressingModes.AdressingMode.IndexeDirect));
        table.add(new instruction("CMPX", 0xBC,  clsAdressingModes.AdressingMode.EtenduDirect));
        table.add(new instruction("CMPX", 0xBC9F, clsAdressingModes.AdressingMode.EtenduIndirect));
        
        table.add(new instruction("CMPY", 0x108C,  clsAdressingModes.AdressingMode.Immediat));
        table.add(new instruction("CMPY", 0x109C,  clsAdressingModes.AdressingMode.Direct));
        table.add(new instruction("CMPY", 0x10AC,  clsAdressingModes.AdressingMode.IndexeDirect));
        table.add(new instruction("CMPY", 0x10BC,  clsAdressingModes.AdressingMode.EtenduDirect));
        table.add(new instruction("CMPY", 0x10BC9F, clsAdressingModes.AdressingMode.EtenduIndirect));
        
        table.add(new instruction("CLRA", 0x4F,  clsAdressingModes.AdressingMode.INHERENT));
        table.add(new instruction("CLRB", 0x5F,  clsAdressingModes.AdressingMode.INHERENT));
        
       
        table.add(new instruction("CLR", 0x0F,  clsAdressingModes.AdressingMode.Direct));
        table.add(new instruction("CLR", 0x6F, clsAdressingModes.AdressingMode.IndexeDirect));
        table.add(new instruction("CLR", 0x7F,  clsAdressingModes.AdressingMode.EtenduDirect));
        table.add(new instruction("CLR", 0x7F9F, clsAdressingModes.AdressingMode.EtenduIndirect));
        
        table.add(new instruction("ADDA", 0x8B,  clsAdressingModes.AdressingMode.Immediat));
        table.add(new instruction("ADDA", 0x9B, clsAdressingModes.AdressingMode.Direct));
        table.add(new instruction("ADDA", 0xAB,  clsAdressingModes.AdressingMode.IndexeDirect));
        table.add(new instruction("ADDA", 0xBB,  clsAdressingModes.AdressingMode.EtenduDirect));
        table.add(new instruction("ADDA", 0xBB9F,  clsAdressingModes.AdressingMode.EtenduIndirect));
        
        table.add(new instruction("ADDB", 0xCB, clsAdressingModes.AdressingMode.Immediat));
        table.add(new instruction("ADDB", 0xDB,  clsAdressingModes.AdressingMode.Direct));
        table.add(new instruction("ADDB", 0xEB,  clsAdressingModes.AdressingMode.IndexeDirect));
        table.add(new instruction("ADDB", 0xFB,  clsAdressingModes.AdressingMode.EtenduDirect));
        table.add(new instruction("ADDB", 0xFB9F,  clsAdressingModes.AdressingMode.EtenduIndirect));
        
        table.add(new instruction("ADDD", 0xC3,  clsAdressingModes.AdressingMode.Immediat));
        table.add(new instruction("ADDD", 0xD3,  clsAdressingModes.AdressingMode.Direct));
        table.add(new instruction("ADDD", 0xE3,  clsAdressingModes.AdressingMode.IndexeDirect));
        table.add(new instruction("ADDD", 0xF3,  clsAdressingModes.AdressingMode.EtenduDirect));
        table.add(new instruction("ADDD", 0xF39F, clsAdressingModes.AdressingMode.EtenduIndirect));
        
        table.add(new instruction("ADCA", 0x89,  clsAdressingModes.AdressingMode.Immediat));
        table.add(new instruction("ADCA", 0x99,  clsAdressingModes.AdressingMode.Direct));
        table.add(new instruction("ADCA", 0xA9,  clsAdressingModes.AdressingMode.IndexeDirect));
        table.add(new instruction("ADCA", 0xB9,  clsAdressingModes.AdressingMode.EtenduDirect));
        table.add(new instruction("ADCA", 0xB99F,  clsAdressingModes.AdressingMode.EtenduIndirect));
        
        table.add(new instruction("ADCB", 0xC0,  clsAdressingModes.AdressingMode.Immediat));
        table.add(new instruction("ADCB", 0xD9,  clsAdressingModes.AdressingMode.Direct));
        table.add(new instruction("ADCB", 0xE9,  clsAdressingModes.AdressingMode.IndexeDirect));
        table.add(new instruction("ADCB", 0xF9,  clsAdressingModes.AdressingMode.EtenduDirect));
        table.add(new instruction("ADCB", 0xF99F,  clsAdressingModes.AdressingMode.EtenduIndirect));
        
        table.add(new instruction("ABX", 0x3A,  clsAdressingModes.AdressingMode.INHERENT));
        
        table.add(new instruction("JMP", 0x0E,  clsAdressingModes.AdressingMode.Direct));
        table.add(new instruction("JMP", 0x6E,  clsAdressingModes.AdressingMode.IndexeDirect));
        table.add(new instruction("JMP", 0x7E,  clsAdressingModes.AdressingMode.EtenduDirect));
        table.add(new instruction("JMP", 0x7E9F, clsAdressingModes.AdressingMode.EtenduIndirect));
        
        table.add(new instruction("PSHS", 0x34,  clsAdressingModes.AdressingMode.Immediat));
        table.add(new instruction("PSHU", 0x36,  clsAdressingModes.AdressingMode.Immediat));
        table.add(new instruction("PULS", 0x35,  clsAdressingModes.AdressingMode.Immediat));
        table.add(new instruction("PULU", 0x37,  clsAdressingModes.AdressingMode.Immediat));
        
        table.add(new instruction("STA", 0x97,  clsAdressingModes.AdressingMode.Direct));
        table.add(new instruction("STA", 0xA7, clsAdressingModes.AdressingMode.IndexeDirect));
        table.add(new instruction("STA", 0xB7,  clsAdressingModes.AdressingMode.EtenduDirect));
        table.add(new instruction("STA", 0xB79F,  clsAdressingModes.AdressingMode.EtenduIndirect));
       
        table.add(new instruction("STB", 0xD7, clsAdressingModes.AdressingMode.Direct));
        table.add(new instruction("STB", 0xE7,  clsAdressingModes.AdressingMode.IndexeDirect));
        table.add(new instruction("STB", 0xF7,  clsAdressingModes.AdressingMode.EtenduDirect));
        table.add(new instruction("STB", 0xF79F,  clsAdressingModes.AdressingMode.EtenduIndirect));
        
        table.add(new instruction("STD", 0xDD,  clsAdressingModes.AdressingMode.Direct));
        table.add(new instruction("STD", 0xED,  clsAdressingModes.AdressingMode.IndexeDirect));
        table.add(new instruction("STD", 0xFD, clsAdressingModes.AdressingMode.EtenduDirect));
        table.add(new instruction("STD", 0xFD9F,  clsAdressingModes.AdressingMode.EtenduIndirect));
        
        table.add(new instruction("STS", 0x10DF,  clsAdressingModes.AdressingMode.Direct));
        table.add(new instruction("STS", 0x10EF,  clsAdressingModes.AdressingMode.IndexeDirect));
        table.add(new instruction("STS", 0x10FF,  clsAdressingModes.AdressingMode.EtenduDirect));
        table.add(new instruction("STS", 0x10FF9F,  clsAdressingModes.AdressingMode.EtenduIndirect));
        
        table.add(new instruction("STU", 0xDF, clsAdressingModes.AdressingMode.Direct));
        table.add(new instruction("STU", 0xEF,  clsAdressingModes.AdressingMode.IndexeDirect));
        table.add(new instruction("STU", 0xFF,  clsAdressingModes.AdressingMode.EtenduDirect));
        table.add(new instruction("STU", 0xFF9F,  clsAdressingModes.AdressingMode.EtenduIndirect));
        
        table.add(new instruction("STX", 0x9F,  clsAdressingModes.AdressingMode.Direct));
        table.add(new instruction("STX", 0xAF,  clsAdressingModes.AdressingMode.IndexeDirect));
        table.add(new instruction("STX", 0xBF,  clsAdressingModes.AdressingMode.EtenduDirect));
        table.add(new instruction("STX", 0xBF9F,  clsAdressingModes.AdressingMode.EtenduIndirect));
        
        table.add(new instruction("STY", 0x109F,  clsAdressingModes.AdressingMode.Direct));
        table.add(new instruction("STY", 0x10AF,  clsAdressingModes.AdressingMode.IndexeDirect));
        table.add(new instruction("STY", 0x10BF,  clsAdressingModes.AdressingMode.EtenduDirect));
        table.add(new instruction("STY", 0x10BF9F,  clsAdressingModes.AdressingMode.EtenduIndirect));
        
        table.add(new instruction("TFR", 0x1F, clsAdressingModes.AdressingMode.Immediat));
        table.add(new instruction("EXG", 0x1E, clsAdressingModes.AdressingMode.Immediat));
        
    }



    
}
