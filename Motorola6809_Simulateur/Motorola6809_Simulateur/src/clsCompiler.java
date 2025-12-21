import static java.lang.Integer.toHexString;
import java.util.ArrayList;
import java.util.Map;
import javax.swing.JTextArea;

public class clsCompiler {

	public static int counter = 0;
	public static boolean debug = false;
	public static clsAdressingModes.AdressingMode mode = null;
	public static clsAdressingModes.deplacement dep = null;
	public static ArrayList<String> lines;
	public static final Map<String, String> reg_code = Map.of(
    "D",  "0",
    "X",  "1",
    "Y",  "2",
    "U",  "3",
    "S",  "4",
    "PC", "5",
    "A",  "8",
    "B",  "9",
    "CC", "A",
    "DP", "B"
);
public static Map<String, String> code_reg = Map.of(
    "0","D",
    "1","X",
    "2","Y",
    "3","U",
    "4","S",
    "5","PC",
    "8","A",
    "9","B",  
    "A","CC", 
	"B","DP"
);

	public static String complementà2(int value, int bits) {
        int mask = (1 << bits) - 1;
        return String.format("%" + bits + "s", Integer.toBinaryString(value & mask)).replace(' ', '0');
    }

	private static String _15or7(String s)
{
	long depv = Integer.parseInt(s.substring(1, s.indexOf(',')), 16);
	if(depv < 127 && depv > -128){
		dep = clsAdressingModes.deplacement._7bit;
		return value_7bit(s);
	}
		
	else if(depv < 32767 && depv > -32768){
		dep = clsAdressingModes.deplacement._15bit;
		return value_15bit(s);
	}
		
	else 
		{
			clsErreur.afficherMessage();
			return null;
		}
}

	private static String value_4bit(String s, char c)
	{
		int dep = Integer.parseInt(s.substring(0, s.indexOf(',')), 16);
		if(dep > 15 || dep < -16)
			clsErreur.afficherMessage();
		char b7,b6= '-',b5= '-';
		b7 ='0';
            switch (c) {
                case 'X':
                    b6='0';
                    b5='0';
                    break;
                case 'Y':
                    b6='0';
                    b5='1';
                    break;
                case 'U':
                    b6='1';
                    b5='0';
                    break;
                case 'S':
                    b6='1';
                    b5='1';
                    break;
                default:
                    break;
            }

		String binary = complementà2(dep, 5);
		String bits = "" + b7 + b6 + b5 + binary;

		int value = Integer.parseInt(bits, 2);        
		String hex = String.format("%02X", value); 

		return hex;

	}
	
	private static String value_7bit(String s){
		int dep = Integer.parseInt(s.substring(1, s.indexOf(',')), 16);
		if(dep > 127 || dep < -128)
			clsErreur.afficherMessage();
		char c = s.charAt(s.length()-1);
		char b7 = '1', b6 = '0', b5 = '0', b4 = '0', b3 = '1', b2 = '0', b1 = '0', b0 = '0';
		if(mode == clsAdressingModes.AdressingMode.IndexeIndirect)
			b4 = '1';
		switch (c) {
                case 'X':
                    b6='0';
                    b5='0';
                    break;
                case 'Y':
                    b6='0';
                    b5='1';
                    break;
                case 'U':
                    b6='1';
                    b5='0';
                    break;
                case 'S':
                    b6='1';
                    b5='1';
                    break;
                default:
					clsErreur.afficherMessage();
                    break;
	}
	String bin = "" + b7 + b6 + b5 + b4 + b3 + b2 + b1 + b0;

	int value = Integer.parseInt(complementà2(dep, 8), 2);        
	String hexdep = toHexString(value); 

	return (toHexString(Integer.parseInt(bin, 2)) + hexdep).toUpperCase();

}

	private static String value_15bit(String s){
		int dep = Integer.parseInt(s.substring(1, s.indexOf(',')), 16);
		if(dep > 32767 || dep < -32768)
			clsErreur.afficherMessage();
		char c = s.charAt(s.length()-1);
		char b7 = '1', b6 = '0', b5 = '0', b4 = '0', b3 = '1', b2 = '0', b1 = '0', b0 = '1';
		if(mode == clsAdressingModes.AdressingMode.IndexeIndirect)
			b4 = '1';
		switch (c) {
                case 'X':
                    b6='0';
                    b5='0';
                    break;
                case 'Y':
                    b6='0';
                    b5='1';
                    break;
                case 'U':
                    b6='1';
                    b5='0';
                    break;
                case 'S':
                    b6='1';
                    b5='1';
                    break;
                default:
					clsErreur.afficherMessage();
                    break;
	}
	String bin = "" + b7 + b6 + b5 + b4 + b3 + b2 + b1 + b0;

	int value = Integer.parseInt(complementà2(dep, 16), 2);        
	String hexdep = toHexString(value); 

	return (toHexString(Integer.parseInt(bin, 2)) + hexdep).toUpperCase();

}
    
	private static String value_PC(String s){
		
		char b7 = '1', b6 = '0', b5 = '0', b4 = '0', b3 = '1', b2 = '1', b1 = '0', b0 = '1';
		int dep = Integer.parseInt(s.substring(1, s.indexOf(',')), 16);
		int value = 0;
		if(dep < 127 && dep > -128)
		{
			b0 = '0';
		}
		else if(dep < 32767 && dep > -32768)
		{
			b0 = '1';
		}
		if(mode == clsAdressingModes.AdressingMode.IndexeIndirect)
			b4 = '1';
			
	String bin = "" + b7 + b6 + b5 + b4 + b3 + b2 + b1 + b0;   
	String hexdep = clsRegisters.formatTo2Digits(dep); 

	return (toHexString(Integer.parseInt(bin, 2)) + hexdep).toUpperCase();

}

	private static String tfr(String s)
	{
		String post = "";

		for (String r : reg_code.keySet()) {
 		   if (s.startsWith(r)) {
		        post += reg_code.get(r);
 		       break;
  		  }
		}

		for (String r : reg_code.keySet()) {
		    if (s.endsWith(r)) {
  		      post += reg_code.get(r);
  		      break;
  		  }
		}
		return post;
	}

	private static String pshpul(String s)
	{
		char b7 = '0',b6 = '0',b5 = '0',b4 = '0',b3 = '0',b2 = '0',b1 = '0',b0 = '0';
		String[] parts = s.split(",");
		for(int i = 0; i < parts.length; i++)
		{
			if(parts[i].equals("PC"))
				b7 = '1';
			else if(parts[i].equals("U") || parts[i].equals("S"))
				b6 = '1';
			else if(parts[i].equals("Y"))
				b5 = '1';
			else if(parts[i].equals("X"))
				b4 = '1';
			else if(parts[i].equals("DP"))
				b3 = '1';
			else if(parts[i].equals("B"))
				b2 = '1';
			else if(parts[i].equals("A"))
				b1 = '1';
			else if(parts[i].equals("CC"))
				b0 = '1';
			else 
				clsErreur.afficherMessage();
		}

		return toHexString(clsPasàpas.binaryToDecimal8bits(""+b7+b6+b5+b4+b3+b2+b1+b0));
	}

public static void compiler() 
	{
		if(clsPasàpas.pas)
			return;
		clsPasàpas.setpc();
		clsROM.focusAddress(clsRegisters.txtPC.getText());
		 counter = 0;
		 debug = true;
		 lines = getLinesFromTextField(clsMoto6809.txtEditeur);
		 ArrayList<String> decoded;
		 for(int i = 0; i < lines.size(); i++)
		 {
			if(!debug){
				clsMoto6809.Reset();
				break;
			}
					
			decoded = Decode(parseInstruction(lines.get(i)));
			
			for(int j = 0; j < decoded.size(); j++)
			{
				clsROM.model.setValueAt(decoded.get(j), counter, 1);
				counter++;
			}
		 }
		 clsPasàpas.currentline = 0;
		 counter = 0;
    }

    public static ArrayList<String> getLinesFromTextField(JTextArea textField) {
        ArrayList<String> lns = new ArrayList<>();

        String content = textField.getText().toUpperCase();

        String[] splitLines = content.split("\\n");
		
        for (String line : splitLines) {
			if(line.trim().isEmpty())
				continue;
            lns.add(line);
        }
        return lns;
    }

    public static ArrayList<String> parseInstruction(String instruction) {
        
		if(!instruction.contains(";"))
			{
				clsErreur.afficherMessage("Vous avez oubliez  ;");
				debug = false;
			}
			else
				instruction = instruction.substring(0, instruction.indexOf(';'));

        instruction = instruction.trim();
        
        String[] parts = instruction.split("\\s+");
		
        String operation = parts[0];
        String stValue = "";

        ArrayList<String>arr = new ArrayList();
        
        if (parts.length == 1) {
            mode = clsAdressingModes.AdressingMode.INHERENT;
			arr.add(operation);
        	return arr;

        }
        if (parts.length == 2) {
			if(operation.equals("TFR") || operation.equals("EXG"))
			{
				stValue = tfr(parts[1]);
			}
			else if(operation.startsWith("PSH") || operation.startsWith("PUL"))
			{
				mode = clsAdressingModes.AdressingMode.Immediat;
				stValue = pshpul(parts[1]);
			}
        	else if(parts[1].startsWith("#$"))
        	{
        		mode = clsAdressingModes.AdressingMode.Immediat;
        		stValue = parts[1].substring(2);
				if(stValue.length() == 1 || stValue.length() == 3 || stValue.length() > 4){
					clsErreur.afficherMessage();
					return null;
				}
					
        	}
        	
        	else if(parts[1].startsWith("$") && parts[1].length()==3)
        	{
			
        		stValue = parts[1].substring(1);
        		mode = clsAdressingModes.AdressingMode.Direct;
        	}
        	else if(parts[1].startsWith("<$") && parts[1].length()==4)
        	{
        		stValue = parts[1].substring(2);
        		mode = clsAdressingModes.AdressingMode.Direct;
        	}
            
        	else if(parts[1].startsWith("$") && parts[1].length()==5 && !parts[1].contains(","))
        	{
        		stValue = parts[1].substring(1);
        		mode = clsAdressingModes.AdressingMode.EtenduDirect;
        	}
        	else if(parts[1].startsWith(">$")&& parts[1].length()==6)
        	{
        		stValue = parts[1].substring(2);
        		mode = clsAdressingModes.AdressingMode.EtenduDirect;
        	}
        	else if(parts[1].startsWith("[") && parts[1].endsWith("]") && parts[1].contains("$")&& parts[1].length()==7 && !parts[1].contains(","))
        	{
        			stValue = parts[1].substring(2 ,6);
        			mode = clsAdressingModes.AdressingMode.EtenduIndirect;
        	}
        	else if(parts[1].contains(",") && !parts[1].contains("["))
            {
					mode = clsAdressingModes.AdressingMode.IndexeDirect;
					if(parts[1].startsWith(",")){
						dep = clsAdressingModes.deplacement.nul_inc_dec;
                        switch (parts[1]) {
                            case ",X":
                            	stValue = "84";
                                 break;
                            case ",Y":
                                stValue = "A4";
                                break;
                            case ",U":
                                stValue = "C4";
                                break;
                            case ",S":
                                stValue = "E4";
                                break;
                            case ",X+":
                                stValue = "80";
                                break;
                            case ",Y+":
                                stValue = "A0";
                                break;
                            case ",U+":
        	                    stValue = "C0";
                                break;
                            case ",S+":
                                stValue = "E0";
                                break;
                            case ",X++":
                                stValue = "81";
                                break;
                            case ",Y++":
                                stValue = "A1";
                                break;
                            case ",U++":
                                stValue = "C1";
                                break;
                            case ",S++":
                                stValue = "E1";
                                break;
                            case ",-X":
                                stValue = "82";
                                break;
                            case ",-Y":
                                stValue = "A2";
                                break;
                            case ",-U":
                                stValue = "C2";
                                break;
                            case ",-S":
                                stValue = "E2";
                                break;
                            case ",--X":
                            	stValue = "83";
                            	break;
                            case ",--Y":
                                stValue = "A3";
                                break;
                            case ",--U":
                                stValue = "C3";
                                break;
                            case ",--S":
                                stValue = "E3";
                                break;
                            default:
                                break;
                                            }

					}
						
					else if(parts[1].startsWith("A"))
					{
						dep = clsAdressingModes.deplacement.nul_inc_dec;
                        switch (parts[1].substring(1)) {
                            case ",X":
                                stValue = "86";
                                break;
                            case ",Y":
                                stValue = "A6";
                                break;
                            case ",U":
                                stValue = "C6";
                                break;
                            case ",S":
                                stValue = "E6";
                                break;
                            default:
                                break;
                                            }
					}
					else if(parts[1].startsWith("B"))
					{
						dep = clsAdressingModes.deplacement.nul_inc_dec;
                        switch (parts[1].substring(1)) {
                            case ",X":
                                stValue = "85";
                                break;
                            case ",Y":
                                stValue = "A5";
                                break;
                            case ",U":
                                stValue = "C5";
                                break;
                            case ",S":
                                stValue = "E5";
                                break;
                            default:
                                break;
                                            }
					}
					else if(parts[1].startsWith("D"))
					{
						dep = clsAdressingModes.deplacement.nul_inc_dec;
                        switch (parts[1].substring(1)) {
                            case ",X":
                                stValue = "8B";
                                break;
                            case ",Y":
                                stValue = "AB";
                                break;
                            case ",U":
                                stValue = "CB";
                                break;
                            case ",S":
                                stValue = "EB";
                                break;
                            default:
                                break;
                                            }
					}
					else if(parts[1].endsWith("PC"))
					{
						dep = clsAdressingModes.deplacement.pc;
						stValue = value_PC(parts[1]);
					}
					else if(parts[1].startsWith("$"))
					{
						stValue =_15or7(parts[1]);
					}
					else
					{
						dep = clsAdressingModes.deplacement._4bit;
						if(parts[1].endsWith(",X"))
						stValue = value_4bit(parts[1], 'X');
					else if(parts[1].endsWith(",Y"))
						stValue = value_4bit(parts[1], 'Y');
					else if(parts[1].endsWith(",U"))
						stValue = value_4bit(parts[1], 'U');
					else if(parts[1].endsWith(",S"))
						stValue = value_4bit(parts[1], 'S');
					}
				   
        }

			else if(parts[1].startsWith("[") && parts[1].endsWith("]") && parts[1].contains(","))
            {
					mode = clsAdressingModes.AdressingMode.IndexeIndirect;
                    parts[1]= parts[1].substring(1,parts[1].length() - 1);
					if(parts[1].startsWith(",")){
						dep = clsAdressingModes.deplacement.nul_inc_dec;
                        switch (parts[1]) {
                            case ",X":
                            	stValue = "94";
                                 break;
                            case ",Y":
                                stValue = "B4";
                                break;
                            case ",U":
                                stValue = "D4";
                                break;
                            case ",S":
                                stValue = "F4";
                                break;
                            case ",X+":
                                stValue = "90";
                                break;
                            case ",Y+":
                                stValue = "B0";
                                break;
                            case ",U+":
        	                    stValue = "D0";
                                break;
                            case ",S+":
                                stValue = "F0";
                                break;
                            case ",X++":
                                stValue = "91";
                                break;
                            case ",Y++":
                                stValue = "B1";
                                break;
                            case ",U++":
                                stValue = "D1";
                                break;
                            case ",S++":
                                stValue = "F1";
                                break;
                            case ",-X":
                                stValue = "92";
                                break;
                            case ",-Y":
                                stValue = "B2";
                                break;
                            case ",-U":
                                stValue = "D2";
                                break;
                            case ",-S":
                                stValue = "F2";
                                break;
                            case ",--X":
                            	stValue = "93";
                            	break;
                            case ",--Y":
                                stValue = "B3";
                                break;
                            case ",--U":
                                stValue = "D3";
                                break;
                            case ",--S":
                                stValue = "F3";
                                break;
                            default:
                                break;
                                            }

					}
						
					else if(parts[1].startsWith("A"))
					{
						dep = clsAdressingModes.deplacement.nul_inc_dec;
                        switch (parts[1].substring(1)) {
                            case ",X":
                                stValue = "96";
                                break;
                            case ",Y":
                                stValue = "B6";
                                break;
                            case ",U":
                                stValue = "D6";
                                break;
                            case ",S":
                                stValue = "F6";
                                break;
                            default:
                                break;
                                            }
					}
					else if(parts[1].startsWith("B"))
					{
						dep = clsAdressingModes.deplacement.nul_inc_dec;
                        switch (parts[1].substring(1)) {
                            case ",X":
                                stValue = "95";
                                break;
                            case ",Y":
                                stValue = "B5";
                                break;
                            case ",U":
                                stValue = "D5";
                                break;
                            case ",S":
                                stValue = "F5";
                                break;
                            default:
                                break;
                                            }
					}
					else if(parts[1].startsWith("D"))
					{
						dep = clsAdressingModes.deplacement.nul_inc_dec;
                        switch (parts[1].substring(1)) {
                            case ",X":
                                stValue = "9B";
                                break;
                            case ",Y":
                                stValue = "BB";
                                break;
                            case ",U":
                                stValue = "DB";
                                break;
                            case ",S":
                                stValue = "FB";
                                break;
                            default:
                                break;
                                            }
					}
					else if(parts[1].endsWith("PC"))
					{
						dep = clsAdressingModes.deplacement.pc;
						stValue = value_PC(parts[1]);
					}
					else if(parts[1].startsWith("$"))
					{
						stValue =_15or7(parts[1]);
					}
					else
					{
						clsErreur.afficherMessage();
					}
				   
        }




        arr.add(operation);
        arr.add(stValue);
        return arr;
    }
	clsErreur.afficherMessage();
	return null;
}


public static ArrayList<String> Decode(ArrayList<String> instruction) {
	    ArrayList<String> dcd = new ArrayList<>();
	    clsInstructions Instruction = new clsInstructions();
		String hexValue ="";
	    for(clsInstructions.instruction i : Instruction.table) 
		{
			if(i.mnemonic.equals(instruction.get(0)) && i.mode == mode) 
			{
				hexValue = toHexString(i.opcode).toUpperCase(); //op code 
				
					if(hexValue.length() == 6) {
	                dcd.add(hexValue.substring(0, 2));
	                dcd.add(hexValue.substring(2, 4));
					dcd.add(hexValue.substring(4, 6));
	            } else if(hexValue.length() == 4) {
	                dcd.add(hexValue.substring(0, 2));
	                dcd.add(hexValue.substring(2, 4));
	            } else {
					 //dcd.add(hexValue);
	                dcd.add(String.format("%2s", hexValue).replace(' ', '0'));
	            }//had lpartie katchouf wach l opcode byte wa7ed ola zouj ola tlata

				if(mode != clsAdressingModes.AdressingMode.INHERENT){
					hexValue = instruction.get(1); //operande
	            	if(hexValue.length() == 6) {
	                dcd.add(hexValue.substring(0, 2));
	                dcd.add(hexValue.substring(2, 4));
					dcd.add(hexValue.substring(4, 6));
	            } else if(hexValue.length() == 4) {
	                	dcd.add(hexValue.substring(0, 2));
	            		dcd.add(hexValue.substring(2, 4));
	            	}else if(hexValue.length() == 2){
	          	  	dcd.add(hexValue);
	           	 }//had lpartie katchouf wach l operande byte wa7ed ola zouj
				
				}
				
				break;
			
			}
	    }
	    return dcd;
	}
}
