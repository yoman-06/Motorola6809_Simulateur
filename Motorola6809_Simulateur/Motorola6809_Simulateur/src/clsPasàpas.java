import static java.lang.Integer.parseInt;
import static java.lang.Integer.toHexString;
import java.util.ArrayList;

public class clsPasàpas {

    public static int currentline = 0;
	public static ArrayList<String>c = new ArrayList<>();
    public static boolean  pas = false;
    

    public static void setpc()
    {
        clsRegisters.PC = (clsRegisters.PC+c.size()) & 0xFFFF;
        clsRegisters.txtPC.setText(clsRegisters.formatTo4Digits(clsRegisters.PC));
        clsROM.focusAddress(clsRegisters.txtPC.getText());;
    }
    public static void updateregisters()
    {
        clsRegisters.D = Integer.parseInt(clsRegisters.formatTo2Digits(clsRegisters.A)  + clsRegisters.formatTo2Digits(clsRegisters.B),16);
        clsRegisters.txtA.setText(clsRegisters.formatTo2Digits(clsRegisters.A));
        clsRegisters.txtB.setText(clsRegisters.formatTo2Digits(clsRegisters.B));
        clsRegisters.txtX.setText(clsRegisters.formatTo4Digits(clsRegisters.X));
        clsRegisters.txtY.setText(clsRegisters.formatTo4Digits(clsRegisters.Y));
        clsRegisters.txtU.setText(clsRegisters.formatTo4Digits(clsRegisters.U));
        clsRegisters.txtS.setText(clsRegisters.formatTo4Digits(clsRegisters.S));
        clsRegisters.txtDP.setText(clsRegisters.formatTo2Digits(clsRegisters.DP));
       
    }

    public static int binaryToDecimal5Bits(String bits) {
    int value = Integer.parseInt(bits, 2);

    if ((value & (1 << 4)) != 0) {
        value -= (1 << 5); 
    }

    return value;
}

public static int binaryToDecimal8bits(String binary) {
    int value = Integer.parseInt(binary, 2);

    if ((value & 0x80) != 0) {
        value -= 0x100;
    }

    return value;
}

    private static int dep_base(String dcd)
    { 
        int decimal = Integer.parseInt(dcd, 16);
        dcd = clsCompiler.complementà2(decimal, 8);
        char b6 = dcd.charAt(1);
        char b5 = dcd.charAt(2);

        int base = 0, dep;

        dep = binaryToDecimal5Bits(dcd.substring(3,8));

        if(b6 == '0' && b5 == '0')
            base = clsRegisters.X;
        else if(b6 == '0' && b5 == '1')
            base = clsRegisters.Y;
        else if(b6 == '1' && b5 == '0')
            base = clsRegisters.U;
        else if(b6 == '1' && b5 == '1')
            base =clsRegisters.S;

        return base +dep;
    }

    public static String getaddress()
    {
        String postoctet = "";
        String octet3 = "";
        String octet4 = "";
        int vdep = 0;
        if(clsCompiler.dep == clsAdressingModes.deplacement.nul_inc_dec || clsCompiler.dep == clsAdressingModes.deplacement._4bit)
        {
            if(c.size() == 2)
                postoctet = c.get(1);
            else
                postoctet = c.get(2);
        }
        else if(clsCompiler.dep == clsAdressingModes.deplacement._7bit)
        {
            if(c.size() == 3){
                postoctet = c.get(1);
                vdep = binaryToDecimal8bits(clsCompiler.complementà2(Integer.parseInt(c.get(2),16), 8));
            }
                
            else{
                postoctet = c.get(2);
                vdep = binaryToDecimal8bits(clsCompiler.complementà2(Integer.parseInt(c.get(3),16), 8));
            } 
        }
        else if(clsCompiler.dep == clsAdressingModes.deplacement._15bit)
        {
            if(c.size() < 5){
                postoctet = c.get(1);
                vdep = binaryToDecimal8bits(clsCompiler.complementà2(Integer.parseInt(c.get(2) + c.get(3),16), 16));
            }
                
            else{
                postoctet = c.get(2);
                vdep = binaryToDecimal8bits(clsCompiler.complementà2(Integer.parseInt(c.get(3) + c.get(4),16), 16));
            } 
        }
        else if(clsCompiler.dep == clsAdressingModes.deplacement.pc)
        {
            if(c.size() == 3){
                postoctet = c.get(1);
                vdep = binaryToDecimal8bits(clsCompiler.complementà2(Integer.parseInt(c.get(2),16), 8));
            }
                
            else if(c.size() == 4){
                postoctet = c.get(1);
                vdep = binaryToDecimal8bits(clsCompiler.complementà2(Integer.parseInt(c.get(2) + c.get(3),16), 16));
            } 
            else{
                postoctet = c.get(2);
                vdep = binaryToDecimal8bits(clsCompiler.complementà2(Integer.parseInt(c.get(3) + c.get(4),16), 16));
            }
        }

        String address = "";

            if(postoctet.equals("84") || postoctet.equals("94")) 
        {
            address = clsRegisters.formatTo4Digits(clsRegisters.X);
        }
        else if(postoctet.equals("A4") || postoctet.equals("B4")) 
        {
            address = clsRegisters.formatTo4Digits(clsRegisters.Y);
        }
        else if(postoctet.equals("C4") || postoctet.equals("D4")) 
        {
            address = clsRegisters.formatTo4Digits(clsRegisters.U);
        }
        else if(postoctet.equals("E4") || postoctet.equals("F4")) 
        {
            address = clsRegisters.formatTo4Digits(clsRegisters.S);
        }

        else if(postoctet.equals("80") || postoctet.equals("90")) 
        {
            address = clsRegisters.formatTo4Digits(clsRegisters.X);
            clsRegisters.X = (clsRegisters.X + 1) & 0xFFFF;
        }
        else if(postoctet.equals("A0") || postoctet.equals("B0")) 
        {
            address = clsRegisters.formatTo4Digits(clsRegisters.Y);
            clsRegisters.Y = (clsRegisters.Y + 1) & 0xFFFF;
        }
        else if(postoctet.equals("C0") || postoctet.equals("D0")) 
        {
            address = clsRegisters.formatTo4Digits(clsRegisters.U);
            clsRegisters.U = (clsRegisters.U + 1) & 0xFFFF;
        }
        else if(postoctet.equals("E0") || postoctet.equals("F0")) 
        {
            address = clsRegisters.formatTo4Digits(clsRegisters.S);
            clsRegisters.S = (clsRegisters.S + 1) & 0xFFFF;
        }

        else if(postoctet.equals("81") || postoctet.equals("91")) 
        {
            address = clsRegisters.formatTo4Digits(clsRegisters.X);
            clsRegisters.X = (clsRegisters.X + 2) & 0xFFFF;
        }
        else if(postoctet.equals("A1") || postoctet.equals("B1")) 
        {
            address = clsRegisters.formatTo4Digits(clsRegisters.Y);
            clsRegisters.Y = (clsRegisters.Y + 2) & 0xFFFF;
        }
        else if(postoctet.equals("C1") || postoctet.equals("D1")) 
        {
            address = clsRegisters.formatTo4Digits(clsRegisters.U);
            clsRegisters.U = (clsRegisters.U + 2) & 0xFFFF;
        }
        else if(postoctet.equals("E1") || postoctet.equals("F1")) 
        {
            address = clsRegisters.formatTo4Digits(clsRegisters.S);
            clsRegisters.S = (clsRegisters.S + 2) & 0xFFFF;
        }

        else if(postoctet.equals("82") || postoctet.equals("92")) 
        {
            clsRegisters.X = (clsRegisters.X - 1) & 0xFFFF;
            address = clsRegisters.formatTo4Digits(clsRegisters.X);
        }
        else if(postoctet.equals("A2") || postoctet.equals("B2")) 
        {
            clsRegisters.Y = (clsRegisters.Y - 1) & 0xFFFF;
            address = clsRegisters.formatTo4Digits(clsRegisters.Y);
        }
        else if(postoctet.equals("C2") || postoctet.equals("D2")) 
        {
            clsRegisters.U = (clsRegisters.U - 1) & 0xFFFF;
            address = clsRegisters.formatTo4Digits(clsRegisters.U);
        }
        else if(postoctet.equals("E2") || postoctet.equals("F2")) 
        {
            clsRegisters.S = (clsRegisters.S - 1) & 0xFFFF;
            address = clsRegisters.formatTo4Digits(clsRegisters.S);
        }

        else if(postoctet.equals("83") || postoctet.equals("93")) 
        {
            clsRegisters.X = (clsRegisters.X - 2) & 0xFFFF;
            address = clsRegisters.formatTo4Digits(clsRegisters.X);
        }
        else if(postoctet.equals("A3") || postoctet.equals("B3")) 
        {
            clsRegisters.Y = (clsRegisters.Y - 2) & 0xFFFF;
            address = clsRegisters.formatTo4Digits(clsRegisters.Y);
        }
        else if(postoctet.equals("C3") || postoctet.equals("D3")) 
        {
            clsRegisters.U = (clsRegisters.U - 2) & 0xFFFF;
            address = clsRegisters.formatTo4Digits(clsRegisters.U);
        }
        else if(postoctet.equals("E3") || postoctet.equals("F3")) 
        {
            clsRegisters.S = (clsRegisters.S - 2) & 0xFFFF;
            address = clsRegisters.formatTo4Digits(clsRegisters.S);
        }

        else if(postoctet.equals("86") || postoctet.equals("96")) 
        {
            address = clsRegisters.formatTo4Digits(clsRegisters.A + clsRegisters.X);
        }
        else if(postoctet.equals("A6") || postoctet.equals("B6")) 
        {
            address = clsRegisters.formatTo4Digits(clsRegisters.A + clsRegisters.Y);
        }
        else if(postoctet.equals("C6") || postoctet.equals("D6")) 
        {
            address = clsRegisters.formatTo4Digits(clsRegisters.A + clsRegisters.U);
        }
        else if(postoctet.equals("E6") || postoctet.equals("F6")) 
        {
            address = clsRegisters.formatTo4Digits(clsRegisters.A + clsRegisters.S);
        }

        else if(postoctet.equals("85") || postoctet.equals("95")) 
        {
            address = clsRegisters.formatTo4Digits(clsRegisters.B + clsRegisters.X);
        }
        else if(postoctet.equals("A5") || postoctet.equals("B5")) 
        {
            address = clsRegisters.formatTo4Digits(clsRegisters.B + clsRegisters.Y);
        }
        else if(postoctet.equals("C5") || postoctet.equals("D5")) 
        {
            address = clsRegisters.formatTo4Digits(clsRegisters.B + clsRegisters.U);
        }
        else if(postoctet.equals("E5") || postoctet.equals("F5")) 
        {
            address = clsRegisters.formatTo4Digits(clsRegisters.B + clsRegisters.S);
        }

        else if(postoctet.equals("8B") || postoctet.equals("9B")) 
        {
            address = clsRegisters.formatTo4Digits(clsRegisters.D + clsRegisters.X);
        }
        else if(postoctet.equals("AB") || postoctet.equals("BB")) 
        {
            address = clsRegisters.formatTo4Digits(clsRegisters.D + clsRegisters.Y);
        }
        else if(postoctet.equals("CB") || postoctet.equals("DB")) 
        {
            address = clsRegisters.formatTo4Digits(clsRegisters.D + clsRegisters.U);
        }
        else if(postoctet.equals("EB") || postoctet.equals("FB")) 
        {
            address = clsRegisters.formatTo4Digits(clsRegisters.D + clsRegisters.S);
        }

        else if (postoctet.equals("88")  || postoctet.equals("98")) 
        {
            address = clsRegisters.formatTo4Digits(vdep + clsRegisters.X);
        }
        else if (postoctet.equals("A8") || postoctet.equals("B8")) 
        {
            address = clsRegisters.formatTo4Digits(vdep + clsRegisters.Y);
        }
        else if (postoctet.equals("C8") || postoctet.equals("D8")) 
        {
            address = clsRegisters.formatTo4Digits(vdep + clsRegisters.U);
        }
        else if (postoctet.equals("E8") || postoctet.equals("F8")) 
        {
            address = clsRegisters.formatTo4Digits(vdep + clsRegisters.S);
        }

        else if (postoctet.equals("89") || postoctet.equals("99")) 
        {
            address = clsRegisters.formatTo4Digits(vdep + clsRegisters.X);
        }
        else if (postoctet.equals("A9") || postoctet.equals("B9")) 
        {
            address = clsRegisters.formatTo4Digits(vdep + clsRegisters.Y);
        }
        else if (postoctet.equals("C9") || postoctet.equals("D9")) 
        {
            address = clsRegisters.formatTo4Digits(vdep + clsRegisters.U);
        }
        else if (postoctet.equals("E9") || postoctet.equals("F9")) 
        {
            address = clsRegisters.formatTo4Digits(vdep + clsRegisters.S);
        }
        else if(postoctet.equals("8C") || postoctet.equals("9C"))
        {
            address = clsRegisters.formatTo4Digits(vdep + clsRegisters.PC);
        }
        else if(postoctet.equals("8D") || postoctet.equals("9D"))
        {
            address = clsRegisters.formatTo4Digits(vdep + clsRegisters.PC);
        }
        else
        {
            if(clsCompiler.mode == clsAdressingModes.AdressingMode.IndexeDirect)
                address = toHexString(dep_base(postoctet));
        }
       


        return address;
    }


    public static void pasapas()
    {
        if(clsCompiler.debug == false)
            {
                clsErreur.afficherMessage("Compiler le code avant d'exécuter.");
                return;
            }
        
        pas = true;
        
        ArrayList<String>inst = clsCompiler.lines;
        ArrayList<String> parsedinstruction = clsCompiler.parseInstruction(inst.get(currentline));
        c = clsCompiler.Decode(parsedinstruction);
        int taille = c.size();
        if(currentline == taille)
            pas = false;

        if(c== null || c.isEmpty())
        {
            return;
        }
        
        
        clsRegisters.txtinstruction.setText(inst.get(currentline));
        currentline++;

        
        String octet1 = c.get(0);
        String octet2 ="";
        String octet3 ="";
        String octet4 ="";
        String octet5 ="";

        if(taille>1)
            octet2 = c.get(1);
        if(taille > 2)
            octet3 = c.get(2);
        if(taille > 3)
            octet4 = c.get(3);
        if(taille > 4)
            octet5 = c.get(4);

        

        if(octet1.equals("86")) //LDA immediat ex : LDA #$34
        {
            if(taille != 2)
            {
                clsErreur.afficherMessage();
                return;
            }
            clsRegisters.A = Integer.parseInt(octet2, 16);
            
        }
        else if(octet1.equals("96")) //LDA direct ex : LDA $34 ou LDA <$34
        {
            if(taille != 2)
            {
                clsErreur.afficherMessage();
                return;
            }
            String adress= clsRegisters.DP + octet2;
            clsRegisters.A = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress, 16), 1).toString(), 16);
            
        }
        else if(octet1.equals("B6") && octet2.equals("9F")) //LDA etendu indirect ex : LDA [$1234]
        {
            if(taille != 4)
            {
                clsErreur.afficherMessage();
                return;
            }
            String adress1= octet3 + octet4;
            String adress2=clsRAM.model.getValueAt(Integer.parseInt(adress1, 16), 1).toString();
            adress2+=clsRAM.model.getValueAt(Integer.parseInt(adress1, 16)+1, 1).toString();
            clsRegisters.A = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress2, 16), 1).toString(), 16);
            
        }
        else if(octet1.equals("B6")) //LDA etendu direct ex : LDA $1234 ou LDA >$1234
        {
            if(taille != 3)
            {
                clsErreur.afficherMessage();
                return;
            }
            String adress= octet2 +octet3;
            clsRegisters.A = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress, 16), 1).toString(), 16);
           
        }
        else if(octet1.equals("A6") && (Integer.parseInt(octet2, 16) & 16) != 16) //LDA indexe direct ex : LDA ,X
        {
            String address = getaddress();
            clsRegisters.A = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(address, 16), 1).toString(), 16);
        }
        else if(octet1.equals("A6") && (Integer.parseInt(octet2, 16) & 16) == 16) //LDA indexe indirect ex : LDA [$1234,X]
        {
            String adress1= getaddress();
            String adress2=clsRAM.model.getValueAt(Integer.parseInt(adress1, 16), 1).toString();
            adress2+=clsRAM.model.getValueAt(Integer.parseInt(adress1, 16)+1, 1).toString();
            clsRegisters.A = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress2, 16), 1).toString(), 16);
            
        }
        
        else if(octet1.equals("C6")) //LDB immediat ex : LDB #$34
        {
            if(taille != 2)
            {
                clsErreur.afficherMessage();
                return;
            }
            clsRegisters.B = Integer.parseInt(octet2, 16);
            
        }
        else if(octet1.equals("D6")) //LDB direct ex : LDB $34
        {
            if(taille != 2)
            {
                clsErreur.afficherMessage();
                return;
            }
            String adress= clsRegisters.DP + octet2;
            clsRegisters.B = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress, 16), 1).toString(), 16);
           
        }
        else if(octet1.equals("F6") && octet2.equals("9F")) //LDB etendu indirect ex : LDB [$1234]
        {
            if(taille != 4)
            {
                clsErreur.afficherMessage();
                return;
            }
            String adress1= octet3 + octet4;
            String adress2=clsRAM.model.getValueAt(Integer.parseInt(adress1, 16), 1).toString();
            adress2+=clsRAM.model.getValueAt(Integer.parseInt(adress1, 16)+1, 1).toString();
            int x = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress2, 16), 1).toString(), 16);
            clsRegisters.B = x;
            
        }
        else if(octet1.equals("F6")) //LDB etendu direct ex : LDB $1234 ou LDB >$1234
        {
            if(taille != 3)
            {
                clsErreur.afficherMessage();
                return;
            }
            String adress= octet2 + octet3;
            int x = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress, 16), 1).toString(), 16);
            clsRegisters.B = x;
            
        }
         else if(octet1.equals("E6") && (Integer.parseInt(octet2, 16) & 16) != 16) //LDB indexe direct ex : LDB ,X
        {
         String address = getaddress();   

            int x = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(address, 16), 1).toString(), 16);
            clsRegisters.B = x;
        }
        else if(octet1.equals("E6") && (Integer.parseInt(octet2, 16) & 16) == 16) //LDB indexe indirect
        {
            String adress1= getaddress();
            String adress2=clsRAM.model.getValueAt(Integer.parseInt(adress1, 16), 1).toString();
            adress2+=clsRAM.model.getValueAt(Integer.parseInt(adress1, 16)+1, 1).toString();
            int x = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress2, 16), 1).toString(), 16);
            clsRegisters.B = x;
            
        }

        else if(octet1.equals("CC")) //LDD immediat ex : LDD #$1234
        {
            if(taille != 3)
            {
                clsErreur.afficherMessage();
                return;
            }
            clsRegisters.D = Integer.parseInt(octet2 + octet3, 16);
            clsRegisters.A = Integer.parseInt(octet2, 16);
            clsRegisters.B = Integer.parseInt(octet3, 16);
            
        }
        else if(octet1.equals("DC")) //LDD direct ex : LDD $34
        {
            if(taille != 2)
            {
                clsErreur.afficherMessage();
                return;
            }
            String adress= clsRegisters.DP + octet2;
            int vd1 = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress, 16), 1).toString(), 16);
            int vd2 = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress, 16)+1, 1).toString(), 16);
            String va = String.format("%02X", vd1);
            String vb = String.format("%02X", vd2);
            clsRegisters.D = Integer.parseInt(va + vb, 16);
            clsRegisters.A = Integer.parseInt(va, 16);
            clsRegisters.B = Integer.parseInt(vb, 16);
            
        }
        else if(octet1.equals("FC")&& octet2.equals("9F")) //LDD etendu indirect ex : LDD [$1234]
        {
            if(taille != 4)
            {
                clsErreur.afficherMessage();
                return;
            }
            String adress=octet3 + octet4;
            String adress2 = clsRAM.model.getValueAt(Integer.parseInt(adress, 16), 1).toString();
            adress2+=clsRAM.model.getValueAt(Integer.parseInt(adress, 16)+1, 1).toString();
            int vd1 = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress2, 16), 1).toString(), 16);
            int vd2 = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress2, 16)+1, 1).toString(), 16);
            String va = String.format("%02X", vd1);
            String vb = String.format("%02X", vd2);
            clsRegisters.D = Integer.parseInt(va + vb, 16);
            clsRegisters.A = Integer.parseInt(va, 16);
            clsRegisters.B = Integer.parseInt(vb, 16);
            
        }
        else if(octet1.equals("FC")) //LDD etendu direct ex : LDD $1234
        {
            if(taille != 3)
            {
                clsErreur.afficherMessage();
                return;
            }
            String adress=octet2+octet3;
            int vd1 = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress, 16), 1).toString(), 16);
            int vd2 = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress, 16)+1, 1).toString(), 16);
            String va = String.format("%02X", vd1);
            String vb = String.format("%02X", vd2);
            clsRegisters.D = Integer.parseInt(va + vb, 16);
            clsRegisters.A = Integer.parseInt(va, 16);
            clsRegisters.B = Integer.parseInt(vb, 16);
            
        }
         else if(octet1.equals("EC") && (Integer.parseInt(octet2, 16) & 16) != 16) //LDD indexe direct ex : LDD ,X
        {
         String address = getaddress();  
         
         int vd1 = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(address, 16), 1).toString(), 16);
            int vd2 = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(address, 16)+1, 1).toString(), 16);
            String va = String.format("%02X", vd1);
            String vb = String.format("%02X", vd2);
            clsRegisters.D = Integer.parseInt(va + vb, 16);
            clsRegisters.A = Integer.parseInt(va, 16);
            clsRegisters.B = Integer.parseInt(vb, 16);
        }
        else if(octet1.equals("EC") && (Integer.parseInt(octet2, 16) & 16) == 16) //LDD indexe indirect
        {
            String adress=getaddress();
            String adress2 = clsRAM.model.getValueAt(Integer.parseInt(adress, 16), 1).toString();
            adress2+=clsRAM.model.getValueAt(Integer.parseInt(adress, 16)+1, 1).toString();
            int vd1 = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress2, 16), 1).toString(), 16);
            int vd2 = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress2, 16)+1, 1).toString(), 16);
            String va = String.format("%02X", vd1);
            String vb = String.format("%02X", vd2);
            clsRegisters.D = Integer.parseInt(va + vb, 16);
            clsRegisters.A = Integer.parseInt(va, 16);
            clsRegisters.B = Integer.parseInt(vb, 16);
        }

        else if(octet1.equals("10") && octet2.equals("CE")) //LDS immediat ex : LDS #$1234
        {
            if(taille != 4)
            {
                clsErreur.afficherMessage();
                return;
            }
            clsRegisters.S = Integer.parseInt(octet3 + octet4, 16);
            
        }
        else if(octet1.equals("10") && octet2.equals("DE")) //LDS direct ex : LDS $34
        {
            if(taille != 3)
            {
                clsErreur.afficherMessage();
                return;
            }
            String adress= clsRegisters.DP + octet3;
            int vs1 = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress, 16), 1).toString(), 16);
            int vs2 = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress, 16)+1, 1).toString(), 16);
            String v1 = String.format("%02X", vs1);
            String v2 = String.format("%02X", vs2);
            clsRegisters.S = Integer.parseInt(v1 + v2, 16);
            
        }
        else if(octet1.equals("10") && octet2.equals("FE")&& octet3.equals("9F")) //LDS etendu indirect ex : LDS [$1234]
        {
            if(taille != 5)
            {
                clsErreur.afficherMessage();
                return;
            }
            String adress=octet4+octet5;
            String adress2 = clsRAM.model.getValueAt(Integer.parseInt(adress, 16), 1).toString();
            adress2+=clsRAM.model.getValueAt(Integer.parseInt(adress, 16)+1, 1).toString();
            int vd1 = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress2, 16), 1).toString(), 16);
            int vd2 = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress2, 16)+1, 1).toString(), 16);
            String va = String.format("%02X", vd1);
            String vb = String.format("%02X", vd2);
            clsRegisters.S = Integer.parseInt(va + vb, 16);
            
        }
        else if(octet1.equals("10") && octet2.equals("FE")) //LDS etendu direct ex : LDS $1234
        {
            if(taille != 4)
            {
                clsErreur.afficherMessage();
                return;
            }
            String adress= octet3 + octet4;
            int vs1 = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress, 16), 1).toString(), 16);
            int vs2 = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress, 16)+1, 1).toString(), 16);
            String v1 = String.format("%02X", vs1);
            String v2 = String.format("%02X", vs2);
            clsRegisters.S = Integer.parseInt(v1 + v2, 16);
            
        }
         else if(octet1.equals("10")&& octet2.equals("EE") && (Integer.parseInt(octet3, 16) & 16) != 16) //LDS indexe direct ex : LDS ,X
        {
            String address = getaddress();  
         
            int vd1 = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(address, 16), 1).toString(), 16);
            int vd2 = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(address, 16)+1, 1).toString(), 16);
            String va = String.format("%02X", vd1);
            String vb = String.format("%02X", vd2);
            clsRegisters.S = Integer.parseInt(va + vb, 16);
        }
        else if(octet1.equals("10") && octet2.equals("EE") && (Integer.parseInt(octet3, 16) & 16) == 16) //LDS indexe indirect
        {
            String adress=getaddress();
            String adress2 = clsRAM.model.getValueAt(Integer.parseInt(adress, 16), 1).toString();
            adress2+=clsRAM.model.getValueAt(Integer.parseInt(adress, 16)+1, 1).toString();
            int vd1 = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress2, 16), 1).toString(), 16);
            int vd2 = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress2, 16)+1, 1).toString(), 16);
            String va = String.format("%02X", vd1);
            String vb = String.format("%02X", vd2);
            clsRegisters.S = Integer.parseInt(va + vb, 16);
            
        }

        else if(octet1.equals("CE")) //LDU immediat ex : LDU #$1234
        {
            if(taille != 3)
            {
                clsErreur.afficherMessage();
                return;
            }
            clsRegisters.U = Integer.parseInt(octet2 + octet3, 16);
            
        }
        else if(octet1.equals("DE")) //LDU direct ex : LDU $34
        {
            if(taille != 2)
            {
                clsErreur.afficherMessage();
                return;
            }
            String adress= clsRegisters.DP + octet2;
            int vs1 = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress, 16), 1).toString(), 16);
            int vs2 = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress, 16)+1, 1).toString(), 16);
            String v1 = String.format("%02X", vs1);
            String v2 = String.format("%02X", vs2);
            clsRegisters.U = Integer.parseInt(v1 + v2, 16);
            
        }
        else if(octet1.equals("FE")&& octet2.equals("9F")) //LDU etendu indirect ex : LDU [$1234]
        {
            if(taille != 4)
            {
                clsErreur.afficherMessage();
                return;
            }
            String adress=octet3+octet4;
            String adress2 = clsRAM.model.getValueAt(Integer.parseInt(adress, 16), 1).toString();
            adress2+=clsRAM.model.getValueAt(Integer.parseInt(adress, 16)+1, 1).toString();
            int vd1 = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress2, 16), 1).toString(), 16);
            int vd2 = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress2, 16)+1, 1).toString(), 16);
            String va = String.format("%02X", vd1);
            String vb = String.format("%02X", vd2);
            clsRegisters.U = Integer.parseInt(va + vb, 16);
           
        }
        else if(octet1.equals("FE")) //LDU etendu direct ex : LDU $1234
        {
            if(taille != 3)
            {
                clsErreur.afficherMessage();
                return;
            }
            String adress= octet2 + octet3;
            int vs1 = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress, 16), 1).toString(), 16);
            int vs2 = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress, 16)+1, 1).toString(), 16);
            String v1 = String.format("%02X", vs1);
            String v2 = String.format("%02X", vs2);
            clsRegisters.U = Integer.parseInt(v1 + v2, 16);
            
        }
        else if(octet1.equals("EE") && (Integer.parseInt(octet2, 16) & 16) != 16) //LDU indexe direct ex : LDU ,X
        {
         String address = getaddress();  
         
         int vd1 = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(address, 16), 1).toString(), 16);
            int vd2 = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(address, 16)+1, 1).toString(), 16);
            String va = String.format("%02X", vd1);
            String vb = String.format("%02X", vd2);
            clsRegisters.U = Integer.parseInt(va + vb, 16);
        }
        else if(octet1.equals("EE") && (Integer.parseInt(octet2, 16) & 16) == 16) //LDU indexe indirect
        {
            String adress=getaddress();
            String adress2 = clsRAM.model.getValueAt(Integer.parseInt(adress, 16), 1).toString();
            adress2+=clsRAM.model.getValueAt(Integer.parseInt(adress, 16)+1, 1).toString();
            int vd1 = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress2, 16), 1).toString(), 16);
            int vd2 = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress2, 16)+1, 1).toString(), 16);
            String va = String.format("%02X", vd1);
            String vb = String.format("%02X", vd2);
            clsRegisters.U = Integer.parseInt(va + vb, 16);
           
        }

        else if(octet1.equals("8E")) //LDX immediat ex : LDX #$1234
        {
            if(taille != 3)
            {
                clsErreur.afficherMessage();
                return;
            }
            clsRegisters.X = Integer.parseInt(octet2 + octet3, 16);
           
        }
        else if(octet1.equals("BE")&& octet2.equals("9F")) //LDX etendu indirect ex : LDX [$1234]
        {
            if(taille != 4)
            {
                clsErreur.afficherMessage();
                return;
            }
            String adress=octet3+octet4;
            String adress2 = clsRAM.model.getValueAt(Integer.parseInt(adress, 16), 1).toString();
            adress2+=clsRAM.model.getValueAt(Integer.parseInt(adress, 16)+1, 1).toString();
            int vd1 = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress2, 16), 1).toString(), 16);
            int vd2 = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress2, 16)+1, 1).toString(), 16);
            String va = String.format("%02X", vd1);
            String vb = String.format("%02X", vd2);
            clsRegisters.X = Integer.parseInt(va + vb, 16);
            
        }
        else if(octet1.equals("9E")) //LDX direct ex : LDX $34
        {
            if(taille != 2)
            {
                clsErreur.afficherMessage();
                return;
            }
            String adress= clsRegisters.DP + octet2;
            int vs1 = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress, 16), 1).toString(), 16);
            int vs2 = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress, 16)+1, 1).toString(), 16);
            String v1 = String.format("%02X", vs1);
            String v2 = String.format("%02X", vs2);
            clsRegisters.X = Integer.parseInt(v1 + v2, 16);
            
        }
        else if(octet1.equals("BE")) //LDX etendu direct ex : LDX $1234
        {
            if(taille != 3)
            {
                clsErreur.afficherMessage();
                return;
            }
            String adress= octet2 + octet3;
            int vs1 = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress, 16), 1).toString(), 16);
            int vs2 = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress, 16)+1, 1).toString(), 16);
            String v1 = String.format("%02X", vs1);
            String v2 = String.format("%02X", vs2);
            clsRegisters.X = Integer.parseInt(v1 + v2, 16);
            
        }
        else if(octet1.equals("AE") && (Integer.parseInt(octet2, 16) & 16) != 16) //LDX indexe direct ex : LDX ,X
        {
         String address = getaddress();  
         
         int vd1 = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(address, 16), 1).toString(), 16);
            int vd2 = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(address, 16)+1, 1).toString(), 16);
            String va = String.format("%02X", vd1);
            String vb = String.format("%02X", vd2);
            clsRegisters.X = Integer.parseInt(va + vb, 16);
        }
        else if(octet1.equals("AE") && (Integer.parseInt(octet2, 16) & 16) == 16) //LDX indexe indirect 
        {
            String adress=getaddress();
            String adress2 = clsRAM.model.getValueAt(Integer.parseInt(adress, 16), 1).toString();
            adress2+=clsRAM.model.getValueAt(Integer.parseInt(adress, 16)+1, 1).toString();
            int vd1 = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress2, 16), 1).toString(), 16);
            int vd2 = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress2, 16)+1, 1).toString(), 16);
            String va = String.format("%02X", vd1);
            String vb = String.format("%02X", vd2);
            clsRegisters.X = Integer.parseInt(va + vb, 16);
            
        }

        else if(octet1.equals("10") && octet2.equals("8E")) //LDY immediat ex : LDY #$1234
        {
            if(taille != 4)
            {
                clsErreur.afficherMessage();
                return;
            }
            clsRegisters.Y = Integer.parseInt(octet3 + octet4, 16);
            
        }
        else if(octet1.equals("10") && octet2.equals("9E")) //LDY direct ex : LDY $34
        {
            if(taille != 3)
            {
                clsErreur.afficherMessage();
                return;
            }
            String adress= clsRegisters.DP + octet3;
            int vs1 = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress, 16), 1).toString(), 16);
            int vs2 = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress, 16)+1, 1).toString(), 16);
            String v1 = String.format("%02X", vs1);
            String v2 = String.format("%02X", vs2);
            clsRegisters.Y = Integer.parseInt(v1 + v2, 16);
            
        }
        else if(octet1.equals("10") && octet2.equals("BE")&& octet3.equals("9F")) //LDY etendu indirect ex : LDY [$1234]
        {
            if(taille != 5)
            {
                clsErreur.afficherMessage();
                return;
            }
            String adress=octet4+octet5;
            String adress2 = clsRAM.model.getValueAt(Integer.parseInt(adress, 16), 1).toString();
            adress2+=clsRAM.model.getValueAt(Integer.parseInt(adress, 16)+1, 1).toString();
            int vd1 = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress2, 16), 1).toString(), 16);
            int vd2 = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress2, 16)+1, 1).toString(), 16);
            String va = String.format("%02X", vd1);
            String vb = String.format("%02X", vd2);
            clsRegisters.Y = Integer.parseInt(va + vb, 16);
            
        }
        else if(octet1.equals("10") && octet2.equals("BE")) //LDY etendu direct ex : LDY $1234
        {
            if(taille != 4)
            {
                clsErreur.afficherMessage();
                return;
            }
            String adress= octet3 + octet4;
            int vs1 = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress, 16), 1).toString(), 16);
            int vs2 = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress, 16)+1, 1).toString(), 16);
            String v1 = String.format("%02X", vs1);
            String v2 = String.format("%02X", vs2);
            clsRegisters.Y = Integer.parseInt(v1 + v2, 16);
            
        }
        else if(octet1.equals("10")&& octet2.equals("AE") && (Integer.parseInt(octet3, 16) & 16) != 16) //LDY indexe direct ex : LDY ,X
        {
            String address = getaddress();  
         
            int vd1 = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(address, 16), 1).toString(), 16);
            int vd2 = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(address, 16)+1, 1).toString(), 16);
            String va = String.format("%02X", vd1);
            String vb = String.format("%02X", vd2);
            clsRegisters.Y = Integer.parseInt(va + vb, 16);
        }
        else if(octet1.equals("10") && octet2.equals("AE") && (Integer.parseInt(octet3, 16) & 16) == 16) //LDY indexe indirect ex : LDY [$1234,PC]
        {
            String adress=getaddress();
            String adress2 = clsRAM.model.getValueAt(Integer.parseInt(adress, 16), 1).toString();
            adress2+=clsRAM.model.getValueAt(Integer.parseInt(adress, 16)+1, 1).toString();
            int vd1 = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress2, 16), 1).toString(), 16);
            int vd2 = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress2, 16)+1, 1).toString(), 16);
            String va = String.format("%02X", vd1);
            String vb = String.format("%02X", vd2);
            clsRegisters.Y = Integer.parseInt(va + vb, 16);
            
        }

        else if(octet1.equals("97")) //STA DIRECT ex : STA $34 OU STA <$34
        {
            if(taille != 2)
            {
                clsErreur.afficherMessage();
                return;
            }
            String adress= clsRegisters.DP + octet2;
            int y = Integer.parseInt(adress, 16);
            clsRAM.model.setValueAt(Integer.parseInt(clsMoto6809.intToHex(clsRegisters.A & 0XFF)), y, 1);
            clsRAM.focusAddress(adress);
            
        }
        else if(octet1.equals("B7") && octet2.equals("9F")) //STA ETENDU INDIRECTE ex : STA [$3412]
        {
            if(taille != 4)
            {
                clsErreur.afficherMessage();
                return;
            }
            String adress = octet3 + octet4;
            int contenta1 = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress, 16), 1).toString(), 16);
            int contenta2 = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress, 16) + 1, 1).toString(), 16);
            int adressFinale = ((contenta1 << 8) | contenta2) & 0xFFFF;
            clsRAM.model.setValueAt(Integer.parseInt(clsMoto6809.intToHex(clsRegisters.A & 0XFF)), adressFinale, 1);
            clsRAM.focusAddress(adress);
        }
        else if(octet1.equals("B7")) //STA ETENDU DIRECTE ex : STA $3412 OU STA >$3412
        {
            if(taille != 3)
            {
                clsErreur.afficherMessage();
                return;
            }
            String adress = octet2 + octet3;
            int y = Integer.parseInt(adress, 16);
            clsRAM.model.setValueAt(Integer.parseInt(clsMoto6809.intToHex(clsRegisters.A & 0XFF)), y, 1);
            clsRAM.focusAddress(adress);
           
        }
        else if(octet1.equals("A7") && (Integer.parseInt(octet2, 16) & 16) != 16) //STA INDEXE DIRECTE ex : STA -3,Y OU STA A,S
        {
            String adress = getaddress();
            int y = Integer.parseInt(adress, 16);
            clsRAM.model.setValueAt(Integer.parseInt(clsMoto6809.intToHex(clsRegisters.A & 0XFF)), y, 1);
            clsRAM.focusAddress(adress);
        }
        else if(octet1.equals("A7") && (Integer.parseInt(octet2, 16) & 16) == 16) //STA INDEXE INDIRECT
        {
            String adress = getaddress();
            int contenta1 = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress, 16), 1).toString(), 16);
            int contenta2 = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress, 16) + 1, 1).toString(), 16);
            int adressFinale = ((contenta1 << 8) | contenta2) & 0xFFFF;
            clsRAM.model.setValueAt(Integer.parseInt(clsMoto6809.intToHex(clsRegisters.A & 0XFF)), adressFinale, 1);
            clsRAM.focusAddress(Integer.toHexString(adressFinale));
        }


        else if(octet1.equals("D7")) //STB DIRECT ex : STB $34 OU STA <$34
        {
            if(taille != 2)
            {
                clsErreur.afficherMessage();
                return;
            }
            String adress= clsRegisters.DP + octet2;
            int y = Integer.parseInt(adress, 16);
            clsRAM.model.setValueAt(Integer.parseInt(clsMoto6809.intToHex(clsRegisters.B & 0XFF)), y, 1);
            clsRAM.focusAddress(adress);
            
        }
        else if(octet1.equals("F7") && octet2.equals("9F")) //STB ETENDU INDIRECTE ex : STB [$3412]
        {
            if(taille != 4)
            {
                clsErreur.afficherMessage();
                return;
            }
            String adress = octet3 + octet4;
            int contenta1 = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress, 16), 1).toString(), 16);
            int contenta2 = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress, 16) + 1, 1).toString(), 16);
            int adressFinale = ((contenta1 << 8) | contenta2) & 0xFFFF;
            clsRAM.model.setValueAt(Integer.parseInt(clsMoto6809.intToHex(clsRegisters.B & 0XFF)), adressFinale, 1);
            clsRAM.focusAddress(adress);
        }
        else if(octet1.equals("F7")) //STB ETENDU DIRECTE ex : STB $3412 OU STB >$3412
        {
            if(taille != 3)
            {
                clsErreur.afficherMessage();
                return;
            }
            String adress = octet2 + octet3;
            int y = Integer.parseInt(adress, 16);
            clsRAM.model.setValueAt(Integer.parseInt(clsMoto6809.intToHex(clsRegisters.B & 0XFF)), y, 1);
            clsRAM.focusAddress(adress);
            
        }
        else if(octet1.equals("E7") && (Integer.parseInt(octet2, 16) & 16) != 16) //STB INDEXE DIRECTE ex : STB -3,Y OU STB A,S
        {
            String adress = getaddress();
            int y = Integer.parseInt(adress, 16);
            clsRAM.model.setValueAt(Integer.parseInt(clsMoto6809.intToHex(clsRegisters.A & 0XFF)), y, 1);
            clsRAM.focusAddress(adress);
        }
        else if(octet1.equals("E7") && (Integer.parseInt(octet2, 16) & 16) == 16) //STB INDEXE INDIRECT
        {
            String adress = getaddress();
            int contenta1 = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress, 16), 1).toString(), 16);
            int contenta2 = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress, 16) + 1, 1).toString(), 16);
            int adressFinale = ((contenta1 << 8) | contenta2) & 0xFFFF;
            clsRAM.model.setValueAt(Integer.parseInt(clsMoto6809.intToHex(clsRegisters.B & 0XFF)), adressFinale, 1);
            clsRAM.focusAddress(Integer.toHexString(adressFinale));
        }

        else if(octet1.equals("DD")) //STD DIRECT ex : STD $34 OU STD <$34
        {
            if(taille != 2)
            {
                clsErreur.afficherMessage();
                return;
            }
            String adress = clsRegisters.DP + octet2;    
            int a1 = Integer.parseInt(adress, 16);
            int a2 = a1 + 1;
            clsRAM.model.setValueAt(Integer.parseInt(clsMoto6809.intToHex(clsRegisters.A & 0XFF)), a1, 1);
            clsRAM.model.setValueAt(Integer.parseInt(clsMoto6809.intToHex(clsRegisters.B & 0XFF)), a2, 1);
            clsRAM.focusAddress(clsRegisters.formatTo4Digits(a1));
            clsRAM.focusAddress(adress);
        }
        else if(octet1.equals("FD") && octet2.equals("9F")) //STD ETENDU INDIRECT ex : STD [$1234]
        {
            if(taille != 4)
            {
                clsErreur.afficherMessage();
                return;
            }            
            String adress = octet3 + octet4;
            int contenta1 = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress, 16), 1).toString(), 16);
            int contenta2 = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress, 16) + 1, 1).toString(), 16);
            int a1 = ((contenta1 << 8 )| contenta2) & 0xFFFF;
            int a2 = a1 + 1;
            clsRAM.model.setValueAt(Integer.parseInt(clsMoto6809.intToHex(clsRegisters.A & 0XFF)), a1, 1);
            clsRAM.model.setValueAt(Integer.parseInt(clsMoto6809.intToHex(clsRegisters.B & 0XFF)), a2, 1);
            clsRAM.focusAddress(adress);
        }
        else if(octet1.equals("FD")) //STD ETENDU DIRECT ex : STD $1234 OU STD >$1234
        {
            if(taille != 3)
            {
                clsErreur.afficherMessage();
                return;
            }            
            int a1 = Integer.parseInt((octet2 + octet3), 16);
            int a2 = a1 + 1;
            clsRAM.model.setValueAt(Integer.parseInt(clsMoto6809.intToHex(clsRegisters.A & 0XFF)), a1, 1);
            clsRAM.model.setValueAt(Integer.parseInt(clsMoto6809.intToHex(clsRegisters.B & 0XFF)), a2, 1);
            clsRAM.focusAddress(clsRegisters.formatTo4Digits(a1));
        }
        else if(octet1.equals("ED") && (Integer.parseInt(octet2, 16) & 16) != 16) //STD INDEXE DIRECTE ex : STD -3,Y OU STA A,S
        {
            
            String adress = getaddress();
            int y = Integer.parseInt(adress, 16);
            clsRAM.model.setValueAt(Integer.parseInt(clsMoto6809.intToHex(clsRegisters.A & 0XFF)), y, 1);
            clsRAM.model.setValueAt(Integer.parseInt(clsMoto6809.intToHex(clsRegisters.B & 0XFF)), y+1, 1);
            clsRAM.focusAddress(adress);
        }
        else if(octet1.equals("ED") && (Integer.parseInt(octet2, 16) & 16) == 16) //STD INDEXE INDIRECT
        {           
            String adress = getaddress();
            int contenta1 = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress, 16), 1).toString(), 16);
            int contenta2 = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress, 16) + 1, 1).toString(), 16);
            int a1 = ((contenta1 << 8 )| contenta2) & 0xFFFF;
            int a2 = a1 + 1;
            clsRAM.model.setValueAt(Integer.parseInt(clsMoto6809.intToHex(clsRegisters.A & 0XFF)), a1, 1);
            clsRAM.model.setValueAt(Integer.parseInt(clsMoto6809.intToHex(clsRegisters.B & 0XFF)), a2, 1);
            clsRAM.focusAddress(Integer.toHexString(a1));
        }

        else if(octet1.equals("DF")) //STU DIRECT ex : STU $34 OU STD <$34
        {
            if(taille != 2)
            {
                clsErreur.afficherMessage();
                return;
            }
            String adress = clsRegisters.DP + octet2;    
            int a1 = Integer.parseInt(adress, 16);
            int a2 = a1 + 1;
            int octethaut = (clsRegisters.U >> 8) & 0xFF;
            int octetbas = clsRegisters.U & 0xFF;
            clsRAM.model.setValueAt(clsRegisters.formatTo2Digits(octethaut), a1, 1);
            clsRAM.model.setValueAt(clsRegisters.formatTo2Digits(octetbas), a2, 1);
            clsRAM.focusAddress(clsRegisters.formatTo4Digits(a1));
        }
        else if(octet1.equals("FF") && octet2.equals("9F")) //STU ETENDU INDIRECT ex : STU [$1234]
        {
            if(taille != 4)
            {
                clsErreur.afficherMessage();
                return;
            }            
            String adress = octet3 + octet4;
            int contenta1 = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress, 16), 1).toString(), 16);
            int contenta2 = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress, 16) + 1, 1).toString(), 16);
            int a1 = ((contenta1 << 8 )| contenta2) & 0xFFFF;
            int a2 = a1 + 1;
            int octethaut = (clsRegisters.U >> 8) & 0xFF;
            int octetbas = clsRegisters.U & 0xFF;
            clsRAM.model.setValueAt(clsRegisters.formatTo2Digits(octethaut), a1, 1);
            clsRAM.model.setValueAt(clsRegisters.formatTo2Digits(octetbas), a2, 1);
            clsRAM.focusAddress(adress);
        }
        else if(octet1.equals("FF")) //STU ETENDU DIRECT ex : STU $1234 OU STD >$1234
        {
            if(taille != 3)
            {
                clsErreur.afficherMessage();
                return;
            } 
            int a1 = Integer.parseInt((octet2 + octet3), 16);
            int a2 = a1 + 1;
            int octethaut = (clsRegisters.U >> 8) & 0xFF;
            int octetbas = clsRegisters.U & 0xFF;
            clsRAM.model.setValueAt(clsRegisters.formatTo2Digits(octethaut), a1, 1);
            clsRAM.model.setValueAt(clsRegisters.formatTo2Digits(octetbas), a2, 1);
            clsRAM.focusAddress(clsRegisters.formatTo4Digits(a1));
            
        }
        else if(octet1.equals("EF") && (Integer.parseInt(octet2, 16) & 16) != 16) //STU INDEXE DIRECTE ex : STU -3,Y OU STU A,S
        {
            
            String adress = getaddress();
            int y = Integer.parseInt(adress, 16);
            clsRAM.model.setValueAt(Integer.parseInt(clsMoto6809.intToHex((clsRegisters.U >> 8) & 0XFF)), y, 1);
            clsRAM.model.setValueAt(Integer.parseInt(clsMoto6809.intToHex(clsRegisters.U & 0XFF)), y+1, 1);
            clsRAM.focusAddress(adress);
           
        }
        else if(octet1.equals("EF") && (Integer.parseInt(octet2, 16) & 16) == 16) //STU INDEXE INDIRECT
        {          
            String adress = getaddress();
            int contenta1 = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress, 16), 1).toString(), 16);
            int contenta2 = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress, 16) + 1, 1).toString(), 16);
            int a1 = ((contenta1 << 8 )| contenta2) & 0xFFFF;
            int a2 = a1 + 1;
            int octethaut = (clsRegisters.U >> 8) & 0xFF;
            int octetbas = clsRegisters.U & 0xFF;
            clsRAM.model.setValueAt(clsRegisters.formatTo2Digits(octethaut), a1, 1);
            clsRAM.model.setValueAt(clsRegisters.formatTo2Digits(octetbas), a2, 1);
            clsRAM.focusAddress(Integer.toHexString(a1));
        }

        else if(octet1.equals("BF") && octet2.equals("9F")) //STX ETENDU INDIRECT ex : STX [$1234]
        {
            if(taille != 4)
            {
                clsErreur.afficherMessage();
                return;
            }            
            String adress = octet3 + octet4;
            int contenta1 = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress, 16), 1).toString(), 16);
            int contenta2 = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress, 16) + 1, 1).toString(), 16);
            int a1 = ((contenta1 << 8 )| contenta2) & 0xFFFF;
            int a2 = (a1 + 1) & 0xFFFF;
            int octethaut = (clsRegisters.X >> 8) & 0xFF;
            int octetbas = clsRegisters.X & 0xFF;
            clsRAM.model.setValueAt(clsRegisters.formatTo2Digits(octethaut), a1, 1);
            clsRAM.model.setValueAt(clsRegisters.formatTo2Digits(octetbas), a2, 1);
            clsRAM.focusAddress(adress);
        }
        else if(octet1.equals("BF")) //STX ETENDU DIRECT ex : STX $1234 OU STD >$1234
        {
            if(taille != 3)
            {
                clsErreur.afficherMessage();
                return;
            } 
            int a1 = Integer.parseInt((octet2 + octet3), 16);
            int a2 = a1 + 1;
            int octethaut = (clsRegisters.X >> 8) & 0xFF;
            int octetbas = clsRegisters.X & 0xFF;
            clsRAM.model.setValueAt(clsRegisters.formatTo2Digits(octethaut), a1, 1);
            clsRAM.model.setValueAt(clsRegisters.formatTo2Digits(octetbas), a2, 1);
            clsRAM.focusAddress(clsRegisters.formatTo4Digits(a1));
           
        }
        else if(octet1.equals("9F")) //STX DIRECT ex : STX $34 OU STD <$34
        {
            if(taille != 2)
            {
                clsErreur.afficherMessage();
                return;
            }
            String adress = clsRegisters.DP + octet2;    
            int a1 = Integer.parseInt(adress, 16);
            int a2 = a1 + 1;
            int octethaut = (clsRegisters.X >> 8) & 0xFF;
            int octetbas = clsRegisters.X & 0xFF;
            clsRAM.model.setValueAt(clsRegisters.formatTo2Digits(octethaut), a1, 1);
            clsRAM.model.setValueAt(clsRegisters.formatTo2Digits(octetbas), a2, 1);
            clsRAM.focusAddress(clsRegisters.formatTo4Digits(a1));
            
        }
        else if(octet1.equals("AF") && (Integer.parseInt(octet2, 16) & 16) != 16) //STX INDEXE DIRECTE ex : STX -3,Y OU STU A,S
        {
            String adress = getaddress();
            int y = Integer.parseInt(adress, 16);
            clsRAM.model.setValueAt(Integer.parseInt(clsMoto6809.intToHex((clsRegisters.X >> 8) & 0XFF)), y, 1);
            clsRAM.model.setValueAt(Integer.parseInt(clsMoto6809.intToHex(clsRegisters.X & 0XFF)), y+1, 1);
            clsRAM.focusAddress(adress);
           
        }
        else if(octet1.equals("AF") && (Integer.parseInt(octet2, 16) & 16) == 16) //STX INDEXE INDIRECT
        {           
            String adress = getaddress();
            int contenta1 = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress, 16), 1).toString(), 16);
            int contenta2 = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress, 16) + 1, 1).toString(), 16);
            int a1 = ((contenta1 << 8 )| contenta2) & 0xFFFF;
            int a2 = (a1 + 1) & 0xFFFF;
            int octethaut = (clsRegisters.X >> 8) & 0xFF;
            int octetbas = clsRegisters.X & 0xFF;
            clsRAM.model.setValueAt(clsRegisters.formatTo2Digits(octethaut), a1, 1);
            clsRAM.model.setValueAt(clsRegisters.formatTo2Digits(octetbas), a2, 1);
            clsRAM.focusAddress(Integer.toHexString(a1));
        }

        else if(octet1.equals("10") && octet2.equals("DF")) //STS DIRECT ex : STS $34 OU STS <$34
        {
            if(taille != 3)
            {
                clsErreur.afficherMessage();
                return;
            }
            String adress = clsRegisters.DP + octet3;    
            int a1 = Integer.parseInt(adress, 16);
            int a2 = a1 + 1;
            int octethaut = (clsRegisters.S >> 8) & 0xFF;
            int octetbas = clsRegisters.S & 0xFF;
            clsRAM.model.setValueAt(clsRegisters.formatTo2Digits(octethaut), a1, 1);
            clsRAM.model.setValueAt(clsRegisters.formatTo2Digits(octetbas), a2, 1);
            clsRAM.focusAddress(clsRegisters.formatTo4Digits(a1));
           
        }
        else if(octet1.equals("10") && octet2.equals("FF") && octet3.equals("9F")) //STS ETENDU INDIRECT ex : STS [$1234]
        {
            if(taille != 5)
            {
                clsErreur.afficherMessage();
                return;
            }            
            String adress = octet4 + octet5;
            int contenta1 = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress, 16), 1).toString(), 16);
            int contenta2 = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress, 16) + 1, 1).toString(), 16);
            int a1 = ((contenta1 << 8 )| contenta2) & 0xFFFF;
            int a2 = (a1 + 1) & 0xFFFF;
            int octethaut = (clsRegisters.S >> 8) & 0xFF;
            int octetbas = clsRegisters.S & 0xFF;
            clsRAM.model.setValueAt(clsRegisters.formatTo2Digits(octethaut), a1, 1);
            clsRAM.model.setValueAt(clsRegisters.formatTo2Digits(octetbas), a2, 1);
            clsRAM.focusAddress(clsRegisters.formatTo4Digits(a1));
        }
        else if(octet1.equals("10") && octet2.equals("FF")) //STS ETENDU DIRECT ex : STS $1234 OU STD >$1234
        {
            if(taille != 4)
            {
                clsErreur.afficherMessage();
                return;
            } 
            int a1 = Integer.parseInt((octet3 + octet4), 16);
            int a2 = a1 + 1;
            int octethaut = (clsRegisters.S >> 8) & 0xFF;
            int octetbas = clsRegisters.S & 0xFF;
            clsRAM.model.setValueAt(clsRegisters.formatTo2Digits(octethaut), a1, 1);
            clsRAM.model.setValueAt(clsRegisters.formatTo2Digits(octetbas), a2, 1);
            clsRAM.focusAddress(clsRegisters.formatTo4Digits(a1));
        }
        else if(octet1.equals("10") && octet2.equals("EF") && (Integer.parseInt(octet3, 16) & 16) != 16) //STS INDEXE DIRECTE ex : STS -3,Y OU STS A,S
        {
            String adress = getaddress();
            int y = Integer.parseInt(adress, 16);
            clsRAM.model.setValueAt(Integer.parseInt(clsMoto6809.intToHex((clsRegisters.S >> 8) & 0XFF)), y, 1);
            clsRAM.model.setValueAt(Integer.parseInt(clsMoto6809.intToHex(clsRegisters.S & 0XFF)), y+1, 1);
            clsRAM.focusAddress(adress);
        }
        else if(octet1.equals("10") && octet2.equals("EF") && (Integer.parseInt(octet3, 16) & 16) == 16) //STS INDEXE INDIRECT
        {           
            String adress = getaddress();
            int contenta1 = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress, 16), 1).toString(), 16);
            int contenta2 = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress, 16) + 1, 1).toString(), 16);
            int a1 = ((contenta1 << 8 )| contenta2) & 0xFFFF;
            int a2 = (a1 + 1) & 0xFFFF;
            int octethaut = (clsRegisters.S >> 8) & 0xFF;
            int octetbas = clsRegisters.S & 0xFF;
            clsRAM.model.setValueAt(clsRegisters.formatTo2Digits(octethaut), a1, 1);
            clsRAM.model.setValueAt(clsRegisters.formatTo2Digits(octetbas), a2, 1);
            clsRAM.focusAddress(clsRegisters.formatTo4Digits(a1));
        }

        else if(octet1.equals("10") && octet2.equals("9F")) //STY DIRECT ex : STY $34 OU STY <$34
        {
            if(taille != 3)
            {
                clsErreur.afficherMessage();
                return;
            }
            String adress = clsRegisters.DP + octet3;    
            int a1 = Integer.parseInt(adress, 16);
            int a2 = a1 + 1;
            int octethaut = (clsRegisters.Y >> 8) & 0xFF;
            int octetbas = clsRegisters.Y & 0xFF;
            clsRAM.model.setValueAt(clsRegisters.formatTo2Digits(octethaut), a1, 1);
            clsRAM.model.setValueAt(clsRegisters.formatTo2Digits(octetbas), a2, 1);
            clsRAM.focusAddress(clsRegisters.formatTo4Digits(a1));
            
        }
        else if(octet1.equals("10") && octet2.equals("BF") && octet3.equals("9F")) //STY ETENDU INDIRECT ex : STY [$1234]
        {
            if(taille != 5)
            {
                clsErreur.afficherMessage();
                return;
            }            
            String adress = octet4 + octet5;
            int contenta1 = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress, 16), 1).toString(), 16);
            int contenta2 = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress, 16) + 1, 1).toString(), 16);
            int a1 = ((contenta1 << 8 )| contenta2) & 0xFFFF;
            int a2 = (a1 + 1) & 0xFFFF;
            int octethaut = (clsRegisters.Y >> 8) & 0xFF;
            int octetbas = clsRegisters.Y & 0xFF;
            clsRAM.model.setValueAt(clsRegisters.formatTo2Digits(octethaut), a1, 1);
            clsRAM.model.setValueAt(clsRegisters.formatTo2Digits(octetbas), a2, 1);
            clsRAM.focusAddress(clsRegisters.formatTo4Digits(a1));
        }
        else if(octet1.equals("10") && octet2.equals("BF")) //STY ETENDU DIRECT ex : STY $1234 OU STY >$1234
        {
            if(taille != 4)
            {
                clsErreur.afficherMessage();
                return;
            } 
            int a1 = Integer.parseInt((octet3 + octet4), 16);
            int a2 = a1 + 1;
            int octethaut = (clsRegisters.Y >> 8) & 0xFF;
            int octetbas = clsRegisters.Y & 0xFF;
            clsRAM.model.setValueAt(clsRegisters.formatTo2Digits(octethaut), a1, 1);
            clsRAM.model.setValueAt(clsRegisters.formatTo2Digits(octetbas), a2, 1);
            clsRAM.focusAddress(clsRegisters.formatTo4Digits(a1));
            
        }
        else if(octet1.equals("10") && octet2.equals("AF") && (Integer.parseInt(octet3, 16) & 16) != 16) //STY INDEXE DIRECTE ex : STY -3,Y OU STY A,S
        {
            String adress = getaddress();
            int y = Integer.parseInt(adress, 16);
            clsRAM.model.setValueAt(Integer.parseInt(clsMoto6809.intToHex((clsRegisters.Y >> 8) & 0XFF)), y, 1);
            clsRAM.model.setValueAt(Integer.parseInt(clsMoto6809.intToHex(clsRegisters.Y & 0XFF)), y+1, 1);
            clsRAM.focusAddress(adress);
           
        }
        else if(octet1.equals("10") && octet2.equals("AF") && (Integer.parseInt(octet3, 16) & 16) == 16) //STY INDEXE INDIRECT
        {           
            String adress = getaddress();
            int contenta1 = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress, 16), 1).toString(), 16);
            int contenta2 = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress, 16) + 1, 1).toString(), 16);
            int a1 = ((contenta1 << 8 )| contenta2) & 0xFFFF;
            int a2 = (a1 + 1) & 0xFFFF;
            int octethaut = (clsRegisters.Y >> 8) & 0xFF;
            int octetbas = clsRegisters.Y & 0xFF;
            clsRAM.model.setValueAt(clsRegisters.formatTo2Digits(octethaut), a1, 1);
            clsRAM.model.setValueAt(clsRegisters.formatTo2Digits(octetbas), a2, 1);
            clsRAM.focusAddress(clsRegisters.formatTo4Digits(a1));
        }

        else if (octet1.equals("81"))  // CMPA IMMEDIAT EX : CMPA #$12
        {
            if(taille != 2)
        	{
        		clsErreur.afficherMessage();
        		return;
        	}
    		
        	int value = Integer.parseInt(octet2, 16); 
        	int CMPA = (clsRegisters.A & 0xFF) - (value & 0xFF); 
        	
        	clsRegisters.CC &= ~(clsRegisters.CC_N 
                   | clsRegisters.CC_Z 
                   | clsRegisters.CC_V 
                   | clsRegisters.CC_C);	

        	if ((CMPA & 0xFF) == 0 )
        		clsRegisters.CC |= clsRegisters.CC_Z;
        	if ((CMPA & 0x80) != 0)
        		clsRegisters.CC |= clsRegisters.CC_N;
        	if ((clsRegisters.A & 0xFF) < (value & 0xFF))
                clsRegisters.CC |= clsRegisters.CC_C;
        	if (( (clsRegisters.A ^ value) & (clsRegisters.A ^ CMPA) & 0x80 ) != 0)
        		clsRegisters.CC |= clsRegisters.CC_V;
        	
        }
        else if (octet1.equals("91"))  // CMPA DIRECT EX : CMPA $12 OU CMPA <$12
        {
            if(taille != 2)
        	{
        		clsErreur.afficherMessage();
        		return;
        	}

    		String adress = clsMoto6809.intToHex(clsRegisters.DP) + octet2;
    		
        	int value = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress, 16), 1).toString(), 16); 
        	int CMPA = (clsRegisters.A & 0xFF) - (value & 0xFF); 
        	
        	
        	clsRegisters.CC &= ~(clsRegisters.CC_N 
                   | clsRegisters.CC_Z 
                   | clsRegisters.CC_V 
                   | clsRegisters.CC_C);	

        	if ((CMPA & 0xFF) == 0 )
        		clsRegisters.CC |= clsRegisters.CC_Z;
        	if ((CMPA & 0x80) != 0)
        		clsRegisters.CC |= clsRegisters.CC_N;
        	if ((clsRegisters.A & 0xFF) < (value & 0xFF))
                clsRegisters.CC |= clsRegisters.CC_C;
        	if (( (clsRegisters.A ^ value) & (clsRegisters.A ^ CMPA) & 0x80 ) != 0)
        		clsRegisters.CC |= clsRegisters.CC_V;

        }
        else if (octet1.equals("B1") && octet2.equals("9F"))  // CMPA ETENDU INDIRECT EX : CMPA [$1213]
        {
            if(taille != 4)
        	{
        		clsErreur.afficherMessage();
        		return;
        	}
    		String adress = octet3 + octet4;
    		
        	int c1 = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress, 16), 1).toString(), 16); 
        	int c2 = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress, 16) + 1, 1).toString(), 16); 
        	int address = ( (c1 << 8)| c2) & 0xFFFF ;
        	int value = Integer.parseInt(clsRAM.model.getValueAt(address, 1).toString(), 16); 
        	int CMPA = (clsRegisters.A & 0xFF) - (value & 0xFF);
        	
        	
        	clsRegisters.CC &= ~(clsRegisters.CC_N 
                   | clsRegisters.CC_Z 
                   | clsRegisters.CC_V 
                   | clsRegisters.CC_C);	

        	if ((CMPA & 0xFF) == 0 )
        		clsRegisters.CC |= clsRegisters.CC_Z;
        	if ((CMPA & 0x80) != 0)
        		clsRegisters.CC |= clsRegisters.CC_N;
        	if ((clsRegisters.A & 0xFF) < (value & 0xFF))
                clsRegisters.CC |= clsRegisters.CC_C;
        	if (( (clsRegisters.A ^ value) & (clsRegisters.A ^ CMPA) & 0x80 ) != 0)
        		clsRegisters.CC |= clsRegisters.CC_V;

        }
        else if (octet1.equals("B1"))  // CMPA ETENDU DIRECT EX : CMPA $1213 OU CMPA >$1213
        {
            if(taille != 3)
        	{
        		clsErreur.afficherMessage();
        		return;
        	}

        	String adress = octet2 + octet3;
    		
        	int value = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress, 16), 1).toString(), 16); 
        	int CMPA = (clsRegisters.A & 0xFF) - (value & 0xFF); 
        	
        	
        	clsRegisters.CC &= ~(clsRegisters.CC_N 
                   | clsRegisters.CC_Z 
                   | clsRegisters.CC_V 
                   | clsRegisters.CC_C);	

        	if ((CMPA & 0xFF) == 0 )
        		clsRegisters.CC |= clsRegisters.CC_Z;
        	if ((CMPA & 0x80) != 0)
        		clsRegisters.CC |= clsRegisters.CC_N;
        	if ((clsRegisters.A & 0xFF) < (value & 0xFF))
                clsRegisters.CC |= clsRegisters.CC_C;
        	if (( (clsRegisters.A ^ value) & (clsRegisters.A ^ CMPA) & 0x80 ) != 0)
        		clsRegisters.CC |= clsRegisters.CC_V;
        	
        }
        else if (octet1.equals("A1") && (Integer.parseInt(octet2, 16) & 16) != 16)  // CMPA INDEXE DIRECT EX : CMPA ,S OU CMPA -5,X
        {
    		String adress = getaddress();
    		
        	int value = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress, 16), 1).toString(), 16); 
        	int CMPA = (clsRegisters.A & 0xFF) - (value & 0xFF); 
        	
        	clsRegisters.CC &= ~(clsRegisters.CC_N 
                   | clsRegisters.CC_Z 
                   | clsRegisters.CC_V 
                   | clsRegisters.CC_C);	

        	if ((CMPA & 0xFF) == 0 )
        		clsRegisters.CC |= clsRegisters.CC_Z;
        	if ((CMPA & 0x80) != 0)
        		clsRegisters.CC |= clsRegisters.CC_N;
        	if ((clsRegisters.A & 0xFF) < (value & 0xFF))
                clsRegisters.CC |= clsRegisters.CC_C;
        	if (( (clsRegisters.A ^ value) & (clsRegisters.A ^ CMPA) & 0x80 ) != 0)
        		clsRegisters.CC |= clsRegisters.CC_V;
        	
        }
        else if (octet1.equals("A1") && (Integer.parseInt(octet2, 16) & 16) == 16)  // CMPA INDEXE INDIRECT
        {
    		String adress = getaddress();
    		
        	int c1 = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress, 16), 1).toString(), 16); 
        	int c2 = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress, 16) + 1, 1).toString(), 16); 
        	int address = ( (c1 << 8)| c2) & 0xFFFF ;
        	int value = Integer.parseInt(clsRAM.model.getValueAt(address, 1).toString(), 16); 
        	int CMPA = (clsRegisters.A & 0xFF) - (value & 0xFF);
        	
        	clsRegisters.CC &= ~(clsRegisters.CC_N 
                   | clsRegisters.CC_Z 
                   | clsRegisters.CC_V 
                   | clsRegisters.CC_C);	

        	if ((CMPA & 0xFF) == 0 )
        		clsRegisters.CC |= clsRegisters.CC_Z;
        	if ((CMPA & 0x80) != 0)
        		clsRegisters.CC |= clsRegisters.CC_N;
        	if ((clsRegisters.A & 0xFF) < (value & 0xFF))
                clsRegisters.CC |= clsRegisters.CC_C;
        	if (( (clsRegisters.A ^ value) & (clsRegisters.A ^ CMPA) & 0x80 ) != 0)
        		clsRegisters.CC |= clsRegisters.CC_V;

        }

        else if (octet1.equals("C1"))  // CMPB IMMEDIAT EX : CMPB #$12
        {
            if(taille != 2)
        	{
        		clsErreur.afficherMessage();
        		return;
        	}
        	int value = Integer.parseInt(octet2, 16); 
        	int CMPB = (clsRegisters.B & 0xFF) - (value & 0xFF); 
            clsRegisters.CC &= ~(clsRegisters.CC_N 
                   | clsRegisters.CC_Z 
                   | clsRegisters.CC_V 
                   | clsRegisters.CC_C);

        	if ((CMPB & 0xFF) == 0 )
        		clsRegisters.CC |= clsRegisters.CC_Z;
        	if ((CMPB & 0x80) != 0)
        		clsRegisters.CC |= clsRegisters.CC_N;
        	if ((clsRegisters.B & 0xFF) < (value & 0xFF) )
        		clsRegisters.CC |= clsRegisters.CC_C;
        	if (( (clsRegisters.B ^ value) & (clsRegisters.B ^ CMPB) & 0x80 ) != 0)
        		clsRegisters.CC |= clsRegisters.CC_V;
        	

        }
        else if (octet1.equals("D1"))  // CMPB DIRECT EX : CMPB $12 OU CMPB <$12
        {
            if(taille != 2)
        	{
        		clsErreur.afficherMessage();
        		return;
        	}
    		String adress = clsMoto6809.intToHex(clsRegisters.DP) + octet2;
    		
        	int value = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress, 16), 1).toString(), 16); 
        	int CMPB = (clsRegisters.B & 0xFF) - (value & 0xFF); 
        	
        	 clsRegisters.CC &= ~(clsRegisters.CC_N 
                   | clsRegisters.CC_Z 
                   | clsRegisters.CC_V 
                   | clsRegisters.CC_C);
                   
        	if ((CMPB & 0xFF) == 0 )
        		clsRegisters.CC |= clsRegisters.CC_Z;
        	if ((CMPB & 0x80) != 0)
        		clsRegisters.CC |= clsRegisters.CC_N;
        	if ((clsRegisters.B & 0xFF) < (value & 0xFF) )
        		clsRegisters.CC |= clsRegisters.CC_C;
        	if (( (clsRegisters.B ^ value) & (clsRegisters.B ^ CMPB) & 0x80 ) != 0)
        		clsRegisters.CC |= clsRegisters.CC_V;
        	
        }
        else if (octet1.equals("F1") && octet2.equals("9F"))  // CMPB ETENDU INDIRECT EX : CMPB [$1213]
        {
            if(taille != 4)
        	{
        		clsErreur.afficherMessage();
        		return;
        	}
    		String adress = octet3 + octet4;
    		
        	int c1 = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress, 16), 1).toString(), 16); 
        	int c2 = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress, 16) + 1, 1).toString(), 16); 
        	int address = ( (c1 << 8)| c2) & 0xFFFF ;
        	int value = Integer.parseInt(clsRAM.model.getValueAt(address, 1).toString(), 16); 
        	int CMPB = (clsRegisters.B & 0xFF) - (value & 0xFF); 
        	
        	
        	 clsRegisters.CC &= ~(clsRegisters.CC_N 
                   | clsRegisters.CC_Z 
                   | clsRegisters.CC_V 
                   | clsRegisters.CC_C);
                   
        	if ((CMPB & 0xFF) == 0 )
        		clsRegisters.CC |= clsRegisters.CC_Z;
        	if ((CMPB & 0x80) != 0)
        		clsRegisters.CC |= clsRegisters.CC_N;
        	if ((clsRegisters.B & 0xFF) < (value & 0xFF) )
        		clsRegisters.CC |= clsRegisters.CC_C;
        	if (( (clsRegisters.B ^ value) & (clsRegisters.B ^ CMPB) & 0x80 ) != 0)
        		clsRegisters.CC |= clsRegisters.CC_V;

        }
        else if (octet1.equals("F1"))  // CMPB ETENDU DIRECT EX : CMPB $1213 OU CMPB >$1213
        {
            if(taille != 3)
        	{
        		clsErreur.afficherMessage();
        		return;
        	}
    		String adress = octet2 + octet3;
    		
        	int value = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress, 16), 1).toString(), 16); 
        	int CMPB = (clsRegisters.B & 0xFF) - (value & 0xFF); 
        	
        	clsRegisters.CC &= ~(clsRegisters.CC_N 
                   | clsRegisters.CC_Z 
                   | clsRegisters.CC_V 
                   | clsRegisters.CC_C);
                   
        	if ((CMPB & 0xFF) == 0 )
        		clsRegisters.CC |= clsRegisters.CC_Z;
        	if ((CMPB & 0x80) != 0)
        		clsRegisters.CC |= clsRegisters.CC_N;
        	if ((clsRegisters.B & 0xFF) < (value & 0xFF) )
        		clsRegisters.CC |= clsRegisters.CC_C;
        	if (( (clsRegisters.B ^ value) & (clsRegisters.B ^ CMPB) & 0x80 ) != 0)
        		clsRegisters.CC |= clsRegisters.CC_V;
        	
        }
        else if (octet1.equals("E1") && (Integer.parseInt(octet2, 16) & 16) != 16)  // CMPB INDEXE DIRECT EX : CMPB 4,U OU CMPB ,X
        {
    		String adress = getaddress();
    		
        	int value = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress, 16), 1).toString(), 16); 
        	int CMPB = (clsRegisters.B & 0xFF) - (value & 0xFF); 
        	
        	
        	clsRegisters.CC &= ~(clsRegisters.CC_N 
                   | clsRegisters.CC_Z 
                   | clsRegisters.CC_V 
                   | clsRegisters.CC_C);
                   
        	if ((CMPB & 0xFF) == 0 )
        		clsRegisters.CC |= clsRegisters.CC_Z;
        	if ((CMPB & 0x80) != 0)
        		clsRegisters.CC |= clsRegisters.CC_N;
        	if ((clsRegisters.B & 0xFF) < (value & 0xFF) )
        		clsRegisters.CC |= clsRegisters.CC_C;
        	if (( (clsRegisters.B ^ value) & (clsRegisters.B ^ CMPB) & 0x80 ) != 0)
        		clsRegisters.CC |= clsRegisters.CC_V;
        }
        else if (octet1.equals("E1") && (Integer.parseInt(octet2, 16) & 16) == 16)  // CMPB INDEXE INDIRECT
        {
    		String adress = getaddress();
    		
        	int c1 = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress, 16), 1).toString(), 16); 
        	int c2 = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress, 16) + 1, 1).toString(), 16); 
        	int address = ( (c1 << 8)| c2) & 0xFFFF ;
        	int value = Integer.parseInt(clsRAM.model.getValueAt(address, 1).toString(), 16); 
        	int CMPB = (clsRegisters.B & 0xFF) - (value & 0xFF); 
        	
        	
        	 clsRegisters.CC &= ~(clsRegisters.CC_N 
                   | clsRegisters.CC_Z 
                   | clsRegisters.CC_V 
                   | clsRegisters.CC_C);
                   
        	if ((CMPB & 0xFF) == 0 )
        		clsRegisters.CC |= clsRegisters.CC_Z;
        	if ((CMPB & 0x80) != 0)
        		clsRegisters.CC |= clsRegisters.CC_N;
        	if ((clsRegisters.B & 0xFF) < (value & 0xFF) )
        		clsRegisters.CC |= clsRegisters.CC_C;
        	if (( (clsRegisters.B ^ value) & (clsRegisters.B ^ CMPB) & 0x80 ) != 0)
        		clsRegisters.CC |= clsRegisters.CC_V;

        }

        else if (octet1.equals("10") && octet2.equals("83"))  // CMPD IMMEDIAT EX : CMPD #$1213
        {	
            if(taille != 4)
        	{
        		clsErreur.afficherMessage();
        		return;
        	}	

        	int value = Integer.parseInt((octet3 +octet4), 16); 
        	int CMPD = (clsRegisters.D  & 0xFFFF)- (value & 0xFFFF); 

            clsRegisters.CC &= ~(clsRegisters.CC_N 
                   | clsRegisters.CC_Z 
                   | clsRegisters.CC_V 
                   | clsRegisters.CC_C);

        	if ((CMPD & 0xFFFF) == 0 )
        		clsRegisters.CC |= clsRegisters.CC_Z;
        	if ((CMPD & 0x8000) != 0)
        		clsRegisters.CC |= clsRegisters.CC_N;
        	if ((clsRegisters.D & 0xFFFF) < (value & 0xFFFF) )
        		clsRegisters.CC |= clsRegisters.CC_C;
        	if (( (clsRegisters.D ^ value) & (clsRegisters.D ^ CMPD) & 0x8000 ) != 0)
        		clsRegisters.CC |= clsRegisters.CC_V;
        	
        }
        else if (octet1.equals("10") && octet2.equals("93"))  // CMPD DIRECT EX : CMPD $12
        {	
            if(taille != 3)
        	{
        		clsErreur.afficherMessage();
        		return;
        	}	
    		String adress = clsMoto6809.intToHex(clsRegisters.DP) + octet3 ;
    		int a1 = Integer.parseInt(adress, 16);
    		int a2 = a1 + 1;
        	int value = Integer.parseInt(clsRAM.model.getValueAt(a1, 1).toString() + clsRAM.model.getValueAt(a2, 1).toString(), 16);
        	int CMPD = (clsRegisters.D & 0xFFFF)- (value & 0xFFFF); 
        	
        	
        	clsRegisters.CC &= ~(clsRegisters.CC_N 
                   | clsRegisters.CC_Z 
                   | clsRegisters.CC_V 
                   | clsRegisters.CC_C);

        	if ((CMPD & 0xFFFF) == 0 )
        		clsRegisters.CC |= clsRegisters.CC_Z;
        	if ((CMPD & 0x8000) != 0)
        		clsRegisters.CC |= clsRegisters.CC_N;
        	if ((clsRegisters.D & 0xFFFF) < (value & 0xFFFF) )
        		clsRegisters.CC |= clsRegisters.CC_C;
        	if (( (clsRegisters.D ^ value) & (clsRegisters.D ^ CMPD) & 0x8000 ) != 0)
        		clsRegisters.CC |= clsRegisters.CC_V;
        	
        }
        else if (octet1.equals("10") && octet2.equals("B3") && octet3.equals("9F"))  // CMPD ETENDU INDIRECT EX : CMPD [$1234]
        {		
            if(taille != 5)
        	{
        		clsErreur.afficherMessage();
        		return;
        	}
    		String adress = octet4 + octet5 ;
    		int a1 = Integer.parseInt(adress, 16);
    		int a2 = a1 + 1;
    		int address1 = Integer.parseInt(clsRAM.model.getValueAt(a1, 1).toString(), 16);
    		int address2 = Integer.parseInt(clsRAM.model.getValueAt(a2, 1).toString(), 16);
    		int AEI1 = ((address1 << 8) | address2 )& 0xFFFF;
    		int AEI2 = (AEI1 + 1 ) & 0xFFFF;
        	int value = Integer.parseInt((clsRAM.model.getValueAt(AEI1, 1).toString() + clsRAM.model.getValueAt(AEI2, 1).toString()), 16);
        	int CMPD = (clsRegisters.D & 0xFFFF) - (value & 0xFFFF); 
        	
        	
        	clsRegisters.CC &= ~(clsRegisters.CC_N 
                   | clsRegisters.CC_Z 
                   | clsRegisters.CC_V 
                   | clsRegisters.CC_C);

        	if ((CMPD & 0xFFFF) == 0 )
        		clsRegisters.CC |= clsRegisters.CC_Z;
        	if ((CMPD & 0x8000) != 0)
        		clsRegisters.CC |= clsRegisters.CC_N;
        	if ((clsRegisters.D & 0xFFFF) < (value & 0xFFFF) )
        		clsRegisters.CC |= clsRegisters.CC_C;
        	if (( (clsRegisters.D ^ value) & (clsRegisters.D ^ CMPD) & 0x8000 ) != 0)
        		clsRegisters.CC |= clsRegisters.CC_V;

        }
        else if (octet1.equals("10") && octet2.equals("B3"))  // CMPD ETENDU DIRECT EX : CMPD $1234 OU CMPD >$1234
        { 	
            if(taille != 4)
        	{
        		clsErreur.afficherMessage();
        		return;
        	}	
    		String adress = octet3 + octet4 ;
    		int a1 = Integer.parseInt(adress, 16);
    		int a2 = a1 + 1;
        	int value = Integer.parseInt(clsRAM.model.getValueAt(a1, 1).toString() + clsRAM.model.getValueAt(a2, 1).toString(), 16);
        	int CMPD = (clsRegisters.D & 0xFFFF)- (value & 0xFFFF); 
        	
        	
        	clsRegisters.CC &= ~(clsRegisters.CC_N 
                   | clsRegisters.CC_Z 
                   | clsRegisters.CC_V 
                   | clsRegisters.CC_C);

        	if ((CMPD & 0xFFFF) == 0 )
        		clsRegisters.CC |= clsRegisters.CC_Z;
        	if ((CMPD & 0x8000) != 0)
        		clsRegisters.CC |= clsRegisters.CC_N;
        	if ((clsRegisters.D & 0xFFFF) < (value & 0xFFFF) )
        		clsRegisters.CC |= clsRegisters.CC_C;
        	if (( (clsRegisters.D ^ value) & (clsRegisters.D ^ CMPD) & 0x8000 ) != 0)
        		clsRegisters.CC |= clsRegisters.CC_V;
        	
        }
        else if (octet1.equals("10") && octet2.equals("A3") && (Integer.parseInt(octet3, 16) & 16) != 16)  // CMPD INDEXE DIRECT EX : CMPD 4,U OU CMPD ,Y
        {  		
    		String adress = getaddress() ;
    		int a1 = Integer.parseInt(adress, 16);
    		int a2 = a1 + 1;
        	int value = Integer.parseInt(clsRAM.model.getValueAt(a1, 1).toString() + clsRAM.model.getValueAt(a2, 1).toString(), 16);
        	int CMPD = (clsRegisters.D & 0xFFFF)- (value & 0xFFFF); 
        	
        	clsRegisters.CC &= ~(clsRegisters.CC_N 
                   | clsRegisters.CC_Z 
                   | clsRegisters.CC_V 
                   | clsRegisters.CC_C);

        	if ((CMPD & 0xFFFF) == 0 )
        		clsRegisters.CC |= clsRegisters.CC_Z;
        	if ((CMPD & 0x8000) != 0)
        		clsRegisters.CC |= clsRegisters.CC_N;
        	if ((clsRegisters.D & 0xFFFF) < (value & 0xFFFF))
        		clsRegisters.CC |= clsRegisters.CC_C;
        	if (( (clsRegisters.D ^ value) & (clsRegisters.D ^ CMPD) & 0x8000 ) != 0)
        		clsRegisters.CC |= clsRegisters.CC_V;
        }
        else if (octet1.equals("10") && octet2.equals("A3") && (Integer.parseInt(octet3, 16) & 16) == 16)  // CMPD INDEXE INDIRECT
        {
    		String adress = getaddress() ;
    		int a1 = Integer.parseInt(adress, 16);
    		int a2 = a1 + 1;
    		int address1 = Integer.parseInt(clsRAM.model.getValueAt(a1, 1).toString(), 16);
    		int address2 = Integer.parseInt(clsRAM.model.getValueAt(a2, 1).toString(), 16);
    		int AEI1 = ((address1 << 8) | address2 )& 0xFFFF;
    		int AEI2 = (AEI1 + 1 ) & 0xFFFF;
        	int value = Integer.parseInt((clsRAM.model.getValueAt(AEI1, 1).toString() + clsRAM.model.getValueAt(AEI2, 1).toString()), 16);
        	int CMPD = (clsRegisters.D & 0xFFFF) - (value & 0xFFFF); 
        	
        	
        	clsRegisters.CC &= ~(clsRegisters.CC_N 
                   | clsRegisters.CC_Z 
                   | clsRegisters.CC_V 
                   | clsRegisters.CC_C);

        	if ((CMPD & 0xFFFF) == 0 )
        		clsRegisters.CC |= clsRegisters.CC_Z;
        	if ((CMPD & 0x8000) != 0)
        		clsRegisters.CC |= clsRegisters.CC_N;
        	if ((clsRegisters.D & 0xFFFF) < (value & 0xFFFF) )
        		clsRegisters.CC |= clsRegisters.CC_C;
        	if (( (clsRegisters.D ^ value) & (clsRegisters.D ^ CMPD) & 0x8000 ) != 0)
        		clsRegisters.CC |= clsRegisters.CC_V;

        }

        else if (octet1.equals("11") && octet2.equals("8C"))  // CMPS IMMEDIAT EX : CMPS #$1213
        {	
            if(taille != 4)
        	{
        		clsErreur.afficherMessage();
        		return;
        	}	
        	int value = Integer.parseInt((octet3 +octet4), 16); 
        	int CMPS = ((clsRegisters.S  & 0xFFFF)- (value & 0xFFFF))& 0xFFFF; 
        	
        	clsRegisters.CC &= ~(clsRegisters.CC_N 
                   | clsRegisters.CC_Z 
                   | clsRegisters.CC_V 
                   | clsRegisters.CC_C);

        	if ((CMPS & 0xFFFF) == 0 )
        		clsRegisters.CC |= clsRegisters.CC_Z;
        	if ((CMPS & 0x8000) != 0)
        		clsRegisters.CC |= clsRegisters.CC_N;
        	if ((clsRegisters.S & 0xFFFF) < (value & 0xFFFF))
        		clsRegisters.CC |= clsRegisters.CC_C;
        	if (( (clsRegisters.S ^ value) & (clsRegisters.S ^ CMPS) & 0x8000 ) != 0)
        		clsRegisters.CC |= clsRegisters.CC_V;
        	
        }
        else if (octet1.equals("11") && octet2.equals("9C"))  // CMPS DIRECT EX : CMPS $12 OU CMPS <$12
        {  	
            if(taille != 3)
        	{
        		clsErreur.afficherMessage();
        		return;
        	}	
    		String adress = clsMoto6809.intToHex(clsRegisters.DP) + octet3 ;
    		int a1 = Integer.parseInt(adress, 16);
    		int a2 = a1 + 1;
        	int value = Integer.parseInt(clsRAM.model.getValueAt(a1, 1).toString() + clsRAM.model.getValueAt(a2, 1).toString(), 16);
        	int CMPS = (clsRegisters.S & 0xFFFF)- (value & 0xFFFF); 
        	
        	clsRegisters.CC &= ~(clsRegisters.CC_N 
                   | clsRegisters.CC_Z 
                   | clsRegisters.CC_V 
                   | clsRegisters.CC_C);
                   
        	if ((CMPS & 0xFFFF) == 0 )
        		clsRegisters.CC |= clsRegisters.CC_Z;
        	if ((CMPS & 0x8000) != 0)
        		clsRegisters.CC |= clsRegisters.CC_N;
        	if ((clsRegisters.S & 0xFFFF) < (value & 0xFFFF))
        		clsRegisters.CC |= clsRegisters.CC_C;
        	if (( (clsRegisters.S ^ value) & (clsRegisters.S ^ CMPS) & 0x8000 ) != 0)
        		clsRegisters.CC |= clsRegisters.CC_V;
        	
        }
        else if (octet1.equals("11") && octet2.equals("BC") && octet3.equals("9F"))  // CMPS ETENDU INDIRECT EX : CMPS [$1234]
        { 	
            if(taille != 5)
        	{
        		clsErreur.afficherMessage();
        		return;
        	}	
    		String adress = octet4 + octet5 ;
    		int a1 = Integer.parseInt(adress, 16);
    		int a2 = a1 + 1;
    		int address1 = Integer.parseInt(clsRAM.model.getValueAt(a1, 1).toString(), 16);
    		int address2 = Integer.parseInt(clsRAM.model.getValueAt(a2, 1).toString(), 16);
    		int AEI1 = ((address1 << 8) | address2 )& 0xFFFF;
    		int AEI2 = (AEI1 + 1 ) & 0xFFFF;
        	int value = Integer.parseInt((clsRAM.model.getValueAt(AEI1, 1).toString() + clsRAM.model.getValueAt(AEI2, 1).toString()), 16);
        	int CMPS = (clsRegisters.S & 0xFFFF) - (value & 0xFFFF); 
        	
        	clsRegisters.CC &= ~(clsRegisters.CC_N 
                   | clsRegisters.CC_Z 
                   | clsRegisters.CC_V 
                   | clsRegisters.CC_C);
                   
        	if ((CMPS & 0xFFFF) == 0 )
        		clsRegisters.CC |= clsRegisters.CC_Z;
        	if ((CMPS & 0x8000) != 0)
        		clsRegisters.CC |= clsRegisters.CC_N;
        	if ((clsRegisters.S & 0xFFFF) < (value & 0xFFFF))
        		clsRegisters.CC |= clsRegisters.CC_C;
        	if (( (clsRegisters.S ^ value) & (clsRegisters.S ^ CMPS) & 0x8000 ) != 0)
        		clsRegisters.CC |= clsRegisters.CC_V;

        }
        else if (octet1.equals("11") && octet2.equals("BC"))  // CMPS ETENDU DIRECT EX : CMPS $1234 OU CMPS >$1234
        {	
            if(taille != 4)
        	{
        		clsErreur.afficherMessage();
        		return;
        	}	
    		String adress = octet3 + octet4 ;
    		int a1 = Integer.parseInt(adress, 16);
    		int a2 = a1 + 1;
        	int value = Integer.parseInt(clsRAM.model.getValueAt(a1, 1).toString() + clsRAM.model.getValueAt(a2, 1).toString(), 16);
        	int CMPS = (clsRegisters.S & 0xFFFF)- (value & 0xFFFF); 
        	
        	clsRegisters.CC &= ~(clsRegisters.CC_N 
                   | clsRegisters.CC_Z 
                   | clsRegisters.CC_V 
                   | clsRegisters.CC_C);
                   
        	if ((CMPS & 0xFFFF) == 0 )
        		clsRegisters.CC |= clsRegisters.CC_Z;
        	if ((CMPS & 0x8000) != 0)
        		clsRegisters.CC |= clsRegisters.CC_N;
        	if ((clsRegisters.S & 0xFFFF) < (value & 0xFFFF))
        		clsRegisters.CC |= clsRegisters.CC_C;
        	if (( (clsRegisters.S ^ value) & (clsRegisters.S ^ CMPS) & 0x8000 ) != 0)
        		clsRegisters.CC |= clsRegisters.CC_V;
        	
        }
        else if (octet1.equals("11") && octet2.equals("AC")&& (Integer.parseInt(octet3, 16) & 16) != 16)  // CMPS INDEXE DIRECT 
        {  		
    		String adress = getaddress() ;
    		int a1 = Integer.parseInt(adress, 16);
    		int a2 = a1 + 1;
        	int value = Integer.parseInt(clsRAM.model.getValueAt(a1, 1).toString() + clsRAM.model.getValueAt(a2, 1).toString(), 16);
        	int CMPS = (clsRegisters.S & 0xFFFF)- (value & 0xFFFF); 
        	
        	clsRegisters.CC &= ~(clsRegisters.CC_N 
                   | clsRegisters.CC_Z 
                   | clsRegisters.CC_V 
                   | clsRegisters.CC_C);
                   
        	if ((CMPS & 0xFFFF) == 0 )
        		clsRegisters.CC |= clsRegisters.CC_Z;
        	if ((CMPS & 0x8000) != 0)
        		clsRegisters.CC |= clsRegisters.CC_N;
        	if ((clsRegisters.S & 0xFFFF) < (value & 0xFFFF))
        		clsRegisters.CC |= clsRegisters.CC_C;
        	if (( (clsRegisters.S ^ value) & (clsRegisters.S ^ CMPS) & 0x8000 ) != 0)
        		clsRegisters.CC |= clsRegisters.CC_V;
        }
        else if (octet1.equals("11") && octet2.equals("AC")&& (Integer.parseInt(octet3, 16) & 16) == 16)  // CMPS INDEXE INDIRECT
        { 		
    		String adress = getaddress();
    		int a1 = Integer.parseInt(adress, 16);
    		int a2 = a1 + 1;
    		int address1 = Integer.parseInt(clsRAM.model.getValueAt(a1, 1).toString(), 16);
    		int address2 = Integer.parseInt(clsRAM.model.getValueAt(a2, 1).toString(), 16);
    		int AEI1 = ((address1 << 8) | address2 )& 0xFFFF;
    		int AEI2 = (AEI1 + 1 ) & 0xFFFF;
        	int value = Integer.parseInt((clsRAM.model.getValueAt(AEI1, 1).toString() + clsRAM.model.getValueAt(AEI2, 1).toString()), 16);
        	int CMPS = (clsRegisters.S & 0xFFFF) - (value & 0xFFFF); 
        	
        	clsRegisters.CC &= ~(clsRegisters.CC_N 
                   | clsRegisters.CC_Z 
                   | clsRegisters.CC_V 
                   | clsRegisters.CC_C);
                   
        	if ((CMPS & 0xFFFF) == 0 )
        		clsRegisters.CC |= clsRegisters.CC_Z;
        	if ((CMPS & 0x8000) != 0)
        		clsRegisters.CC |= clsRegisters.CC_N;
        	if ((clsRegisters.S & 0xFFFF) < (value & 0xFFFF))
        		clsRegisters.CC |= clsRegisters.CC_C;
        	if (( (clsRegisters.S ^ value) & (clsRegisters.S ^ CMPS) & 0x8000 ) != 0)
        		clsRegisters.CC |= clsRegisters.CC_V;

        }

        else if (octet1.equals("11") && octet2.equals("83"))  // CMPU IMMEDIAT EX : CMPU #$1213
        { 	
            if(taille != 4)
        	{
        		clsErreur.afficherMessage();
        		return;
        	}	
        	int value = Integer.parseInt((octet3 +octet4), 16); 
        	int CMPU = ((clsRegisters.U  & 0xFFFF)- (value & 0xFFFF))& 0xFFFF; 
        	
        	clsRegisters.CC &= ~(clsRegisters.CC_N 
                   | clsRegisters.CC_Z 
                   | clsRegisters.CC_V 
                   | clsRegisters.CC_C);

        	if ((CMPU & 0xFFFF) == 0 )
        		clsRegisters.CC |= clsRegisters.CC_Z;
        	if ((CMPU & 0x8000) != 0)
        		clsRegisters.CC |= clsRegisters.CC_N;
        	if ((clsRegisters.U & 0xFFFF) < (value & 0xFFFF))
        		clsRegisters.CC |= clsRegisters.CC_C;
        	if (( (clsRegisters.U ^ value) & (clsRegisters.U ^ CMPU) & 0x8000 ) != 0)
        		clsRegisters.CC |= clsRegisters.CC_V;
        	
        }
        else if (octet1.equals("11") && octet2.equals("93"))  // CMPU DIRECT EX : CMPU $12 OU CMPU <$12
        {	
            if(taille != 3)
        	{
        		clsErreur.afficherMessage();
        		return;
        	}	
    		String adress = clsMoto6809.intToHex(clsRegisters.DP) + octet3 ;
    		int a1 = Integer.parseInt(adress, 16);
    		int a2 = a1 + 1;
        	int value = Integer.parseInt(clsRAM.model.getValueAt(a1, 1).toString() + clsRAM.model.getValueAt(a2, 1).toString(), 16);
        	int CMPU = (clsRegisters.U & 0xFFFF)- (value & 0xFFFF); 
        	
        	clsRegisters.CC &= ~(clsRegisters.CC_N 
                   | clsRegisters.CC_Z 
                   | clsRegisters.CC_V 
                   | clsRegisters.CC_C);

        	if ((CMPU & 0xFFFF) == 0 )
        		clsRegisters.CC |= clsRegisters.CC_Z;
        	if ((CMPU & 0x8000) != 0)
        		clsRegisters.CC |= clsRegisters.CC_N;
        	if ((clsRegisters.U & 0xFFFF) < (value & 0xFFFF))
        		clsRegisters.CC |= clsRegisters.CC_C;
        	if (( (clsRegisters.U ^ value) & (clsRegisters.U ^ CMPU) & 0x8000 ) != 0)
        		clsRegisters.CC |= clsRegisters.CC_V;
        	
        }
        else if (octet1.equals("11") && octet2.equals("B3") && octet3.equals("9F"))  // CMPU ETENDU INDIRECT EX : CMPU [$1234]
        { 	
            if(taille != 5)
        	{
        		clsErreur.afficherMessage();
        		return;
        	}	
    		String adress = octet4 + octet5 ;
    		int a1 = Integer.parseInt(adress, 16);
    		int a2 = a1 + 1;
    		int address1 = Integer.parseInt(clsRAM.model.getValueAt(a1, 1).toString(), 16);
    		int address2 = Integer.parseInt(clsRAM.model.getValueAt(a2, 1).toString(), 16);
    		int AEI1 = ((address1 << 8) | address2 )& 0xFFFF;
    		int AEI2 = (AEI1 + 1 ) & 0xFFFF;
        	int value = Integer.parseInt((clsRAM.model.getValueAt(AEI1, 1).toString() + clsRAM.model.getValueAt(AEI2, 1).toString()), 16);
        	int CMPU = (clsRegisters.U & 0xFFFF) - (value & 0xFFFF); 
        	
        	clsRegisters.CC &= ~(clsRegisters.CC_N 
                   | clsRegisters.CC_Z 
                   | clsRegisters.CC_V 
                   | clsRegisters.CC_C);

        	if ((CMPU & 0xFFFF) == 0 )
        		clsRegisters.CC |= clsRegisters.CC_Z;
        	if ((CMPU & 0x8000) != 0)
        		clsRegisters.CC |= clsRegisters.CC_N;
        	if ((clsRegisters.U & 0xFFFF) < (value & 0xFFFF))
        		clsRegisters.CC |= clsRegisters.CC_C;
        	if (( (clsRegisters.U ^ value) & (clsRegisters.U ^ CMPU) & 0x8000 ) != 0)
        		clsRegisters.CC |= clsRegisters.CC_V;

        }
        else if (octet1.equals("11") && octet2.equals("B3"))  // CMPU ETENDU DIRECT EX : CMPU $1234 OU CMPU >$1234
        { 	
            if(taille != 4)
        	{
        		clsErreur.afficherMessage();
        		return;
        	}	
    		String adress = octet3 + octet4 ;
    		int a1 = Integer.parseInt(adress, 16);
    		int a2 = a1 + 1;
        	int value = Integer.parseInt(clsRAM.model.getValueAt(a1, 1).toString() + clsRAM.model.getValueAt(a2, 1).toString(), 16);
        	int CMPU = (clsRegisters.U & 0xFFFF)- (value & 0xFFFF);
        	
        	clsRegisters.CC &= ~(clsRegisters.CC_N 
                   | clsRegisters.CC_Z 
                   | clsRegisters.CC_V 
                   | clsRegisters.CC_C);

        	if ((CMPU & 0xFFFF) == 0 )
        		clsRegisters.CC |= clsRegisters.CC_Z;
        	if ((CMPU & 0x8000) != 0)
        		clsRegisters.CC |= clsRegisters.CC_N;
        	if ((clsRegisters.U & 0xFFFF) < (value & 0xFFFF))
        		clsRegisters.CC |= clsRegisters.CC_C;
        	if (( (clsRegisters.U ^ value) & (clsRegisters.U ^ CMPU) & 0x8000 ) != 0)
        		clsRegisters.CC |= clsRegisters.CC_V;
        }
        else if (octet1.equals("11") && octet2.equals("A3")&& (Integer.parseInt(octet3, 16) & 16) != 16)  // CMPU INDEXE DIRECT 
        {		
    		String adress = getaddress() ;
    		int a1 = Integer.parseInt(adress, 16);
    		int a2 = a1 + 1;
        	int value = Integer.parseInt(clsRAM.model.getValueAt(a1, 1).toString() + clsRAM.model.getValueAt(a2, 1).toString(), 16);
        	int CMPU = (clsRegisters.U & 0xFFFF)- (value & 0xFFFF); 
        	
        	clsRegisters.CC &= ~(clsRegisters.CC_N 
                   | clsRegisters.CC_Z 
                   | clsRegisters.CC_V 
                   | clsRegisters.CC_C);

        	if ((CMPU & 0xFFFF) == 0 )
        		clsRegisters.CC |= clsRegisters.CC_Z;
        	if ((CMPU & 0x8000) != 0)
        		clsRegisters.CC |= clsRegisters.CC_N;
        	if ((clsRegisters.U & 0xFFFF) < (value & 0xFFFF))
        		clsRegisters.CC |= clsRegisters.CC_C;
        	if (( (clsRegisters.U ^ value) & (clsRegisters.U ^ CMPU) & 0x8000 ) != 0)
        		clsRegisters.CC |= clsRegisters.CC_V;
        }
        else if (octet1.equals("11") && octet2.equals("A3")&& (Integer.parseInt(octet3, 16) & 16) == 16)  // CMPU INDEXE INDIRECT
        { 	
    		String adress = getaddress() ;
    		int a1 = Integer.parseInt(adress, 16);
    		int a2 = a1 + 1;
    		int address1 = Integer.parseInt(clsRAM.model.getValueAt(a1, 1).toString(), 16);
    		int address2 = Integer.parseInt(clsRAM.model.getValueAt(a2, 1).toString(), 16);
    		int AEI1 = ((address1 << 8) | address2 )& 0xFFFF;
    		int AEI2 = (AEI1 + 1 ) & 0xFFFF;
        	int value = Integer.parseInt((clsRAM.model.getValueAt(AEI1, 1).toString() + clsRAM.model.getValueAt(AEI2, 1).toString()), 16);
        	int CMPU = (clsRegisters.U & 0xFFFF) - (value & 0xFFFF); 
        	
        	clsRegisters.CC &= ~(clsRegisters.CC_N 
                   | clsRegisters.CC_Z 
                   | clsRegisters.CC_V 
                   | clsRegisters.CC_C);

        	if ((CMPU & 0xFFFF) == 0 )
        		clsRegisters.CC |= clsRegisters.CC_Z;
        	if ((CMPU & 0x8000) != 0)
        		clsRegisters.CC |= clsRegisters.CC_N;
        	if ((clsRegisters.U & 0xFFFF) < (value & 0xFFFF))
        		clsRegisters.CC |= clsRegisters.CC_C;
        	if (( (clsRegisters.U ^ value) & (clsRegisters.U ^ CMPU) & 0x8000 ) != 0)
        		clsRegisters.CC |= clsRegisters.CC_V;

        }

        else if (octet1.equals("10") && octet2.equals("8C"))  // CMPY IMMEDIAT EX : CMPY #$1213
        {	
            if(taille != 4)
        	{
        		clsErreur.afficherMessage();
        		return;
        	}	
        	int value = Integer.parseInt((octet3 +octet4), 16); 
        	int CMPY = ((clsRegisters.Y  & 0xFFFF)- (value & 0xFFFF))& 0xFFFF; 
        	
        	clsRegisters.CC &= ~(clsRegisters.CC_N 
                   | clsRegisters.CC_Z 
                   | clsRegisters.CC_V 
                   | clsRegisters.CC_C);

        	if ((CMPY & 0xFFFF) == 0 )
        		clsRegisters.CC |= clsRegisters.CC_Z;
        	if ((CMPY & 0x8000) != 0)
        		clsRegisters.CC |= clsRegisters.CC_N;
        	if ((clsRegisters.Y & 0xFFFF) < (value & 0xFFFF))
        		clsRegisters.CC |= clsRegisters.CC_C;
        	if (( (clsRegisters.Y ^ value) & (clsRegisters.Y ^ CMPY) & 0x8000 ) != 0)
        		clsRegisters.CC |= clsRegisters.CC_V;
        	
        }
        else if (octet1.equals("10") && octet2.equals("9C"))  // CMPY DIRECT EX : CMPY $12 OU CMPY <$12
        {  	
            if(taille != 3)
        	{
        		clsErreur.afficherMessage();
        		return;
        	}	
    		String adress = clsMoto6809.intToHex(clsRegisters.DP) + octet3 ;
    		int a1 = Integer.parseInt(adress, 16);
    		int a2 = a1 + 1;
        	int value = Integer.parseInt(clsRAM.model.getValueAt(a1, 1).toString() + clsRAM.model.getValueAt(a2, 1).toString(), 16);
        	int CMPY = (clsRegisters.Y & 0xFFFF)- (value & 0xFFFF); 
        	
        	
        	clsRegisters.CC &= ~(clsRegisters.CC_N 
                   | clsRegisters.CC_Z 
                   | clsRegisters.CC_V 
                   | clsRegisters.CC_C);

        	if ((CMPY & 0xFFFF) == 0 )
        		clsRegisters.CC |= clsRegisters.CC_Z;
        	if ((CMPY & 0x8000) != 0)
        		clsRegisters.CC |= clsRegisters.CC_N;
        	if ((clsRegisters.Y & 0xFFFF) < (value & 0xFFFF))
        		clsRegisters.CC |= clsRegisters.CC_C;
        	if (( (clsRegisters.Y ^ value) & (clsRegisters.Y ^ CMPY) & 0x8000 ) != 0)
        		clsRegisters.CC |= clsRegisters.CC_V;

        }
        else if (octet1.equals("10") && octet2.equals("BC") && octet3.equals("9F"))  // CMPY ETENDU INDIRECT EX : CMPY [$1234]
        { 	
            if(taille != 5)
        	{
        		clsErreur.afficherMessage();
        		return;
        	}	
    		String adress = octet4 + octet5 ;
    		int a1 = Integer.parseInt(adress, 16);
    		int a2 = a1 + 1;
    		int address1 = Integer.parseInt(clsRAM.model.getValueAt(a1, 1).toString(), 16);
    		int address2 = Integer.parseInt(clsRAM.model.getValueAt(a2, 1).toString(), 16);
    		int AEI1 = ((address1 << 8) | address2 )& 0xFFFF;
    		int AEI2 = (AEI1 + 1 ) & 0xFFFF;
        	int value = Integer.parseInt((clsRAM.model.getValueAt(AEI1, 1).toString() + clsRAM.model.getValueAt(AEI2, 1).toString()), 16);
        	int CMPY = (clsRegisters.Y & 0xFFFF) - (value & 0xFFFF); 
        	
        	
        	clsRegisters.CC &= ~(clsRegisters.CC_N 
                   | clsRegisters.CC_Z 
                   | clsRegisters.CC_V 
                   | clsRegisters.CC_C);

        	if ((CMPY & 0xFFFF) == 0 )
        		clsRegisters.CC |= clsRegisters.CC_Z;
        	if ((CMPY & 0x8000) != 0)
        		clsRegisters.CC |= clsRegisters.CC_N;
        	if ((clsRegisters.Y & 0xFFFF) < (value & 0xFFFF))
        		clsRegisters.CC |= clsRegisters.CC_C;
        	if (( (clsRegisters.Y ^ value) & (clsRegisters.Y ^ CMPY) & 0x8000 ) != 0)
        		clsRegisters.CC |= clsRegisters.CC_V;
        }
        else if (octet1.equals("10") && octet2.equals("BC"))  // CMPY ETENDU DIRECT EX : CMPY $1234 OU CMPY >$1234
        {  	
            if(taille != 4)
        	{
        		clsErreur.afficherMessage();
        		return;
        	}	
    		String adress = octet3 + octet4 ;
    		int a1 = Integer.parseInt(adress, 16);
    		int a2 = a1 + 1;
        	int value = Integer.parseInt(clsRAM.model.getValueAt(a1, 1).toString() + clsRAM.model.getValueAt(a2, 1).toString(), 16);
        	int CMPY = (clsRegisters.Y & 0xFFFF)- (value & 0xFFFF); 
        	
        	
        	clsRegisters.CC &= ~(clsRegisters.CC_N 
                   | clsRegisters.CC_Z 
                   | clsRegisters.CC_V 
                   | clsRegisters.CC_C);

        	if ((CMPY & 0xFFFF) == 0 )
        		clsRegisters.CC |= clsRegisters.CC_Z;
        	if ((CMPY & 0x8000) != 0)
        		clsRegisters.CC |= clsRegisters.CC_N;
        	if ((clsRegisters.Y & 0xFFFF) < (value & 0xFFFF))
        		clsRegisters.CC |= clsRegisters.CC_C;
        	if (( (clsRegisters.Y ^ value) & (clsRegisters.Y ^ CMPY) & 0x8000 ) != 0)
        		clsRegisters.CC |= clsRegisters.CC_V;
        }
        else if (octet1.equals("10") && octet2.equals("AC")&& (Integer.parseInt(octet3, 16) & 16) != 16)  // CMPY INDEXE DIRECT 
        {   		
    		String adress = getaddress() ;
    		int a1 = Integer.parseInt(adress, 16);
    		int a2 = a1 + 1;
        	int value = Integer.parseInt(clsRAM.model.getValueAt(a1, 1).toString() + clsRAM.model.getValueAt(a2, 1).toString(), 16);
        	int CMPY = (clsRegisters.Y & 0xFFFF)- (value & 0xFFFF); 
        	
        	clsRegisters.CC &= ~(clsRegisters.CC_N 
                   | clsRegisters.CC_Z 
                   | clsRegisters.CC_V 
                   | clsRegisters.CC_C);

        	if ((CMPY & 0xFFFF) == 0 )
        		clsRegisters.CC |= clsRegisters.CC_Z;
        	if ((CMPY & 0x8000) != 0)
        		clsRegisters.CC |= clsRegisters.CC_N;
        	if ((clsRegisters.Y & 0xFFFF) < (value & 0xFFFF))
        		clsRegisters.CC |= clsRegisters.CC_C;
        	if (( (clsRegisters.Y ^ value) & (clsRegisters.Y ^ CMPY) & 0x8000 ) != 0)
        		clsRegisters.CC |= clsRegisters.CC_V;
        }
        else if (octet1.equals("10") && octet2.equals("AC")&& (Integer.parseInt(octet3, 16) & 16) == 16)  // CMPY INDEXE INDIRECT
        { 	
    		String adress = getaddress() ;
    		int a1 = Integer.parseInt(adress, 16);
    		int a2 = a1 + 1;
    		int address1 = Integer.parseInt(clsRAM.model.getValueAt(a1, 1).toString(), 16);
    		int address2 = Integer.parseInt(clsRAM.model.getValueAt(a2, 1).toString(), 16);
    		int AEI1 = ((address1 << 8) | address2 )& 0xFFFF;
    		int AEI2 = (AEI1 + 1 ) & 0xFFFF;
        	int value = Integer.parseInt((clsRAM.model.getValueAt(AEI1, 1).toString() + clsRAM.model.getValueAt(AEI2, 1).toString()), 16);
        	int CMPY = (clsRegisters.Y & 0xFFFF) - (value & 0xFFFF); 
        	
        	
        	clsRegisters.CC &= ~(clsRegisters.CC_N 
                   | clsRegisters.CC_Z 
                   | clsRegisters.CC_V 
                   | clsRegisters.CC_C);

        	if ((CMPY & 0xFFFF) == 0 )
        		clsRegisters.CC |= clsRegisters.CC_Z;
        	if ((CMPY & 0x8000) != 0)
        		clsRegisters.CC |= clsRegisters.CC_N;
        	if ((clsRegisters.Y & 0xFFFF) < (value & 0xFFFF))
        		clsRegisters.CC |= clsRegisters.CC_C;
        	if (( (clsRegisters.Y ^ value) & (clsRegisters.Y ^ CMPY) & 0x8000 ) != 0)
        		clsRegisters.CC |= clsRegisters.CC_V;
        }

        else if (octet1.equals("8C"))  // CMPX IMMEDIAT EX : CMPX #$1213
        {	
            if(taille != 3)
        	{
        		clsErreur.afficherMessage();
        		return;
        	}	
        	int value = Integer.parseInt((octet2 +octet3), 16); 
        	int CMPX = ((clsRegisters.X  & 0xFFFF)- (value & 0xFFFF))& 0xFFFF; 
        	
        	clsRegisters.CC &= ~(clsRegisters.CC_N 
                   | clsRegisters.CC_Z 
                   | clsRegisters.CC_V 
                   | clsRegisters.CC_C);
        	
        	if ((CMPX & 0xFFFF) == 0 )
        		clsRegisters.CC |= clsRegisters.CC_Z;
        	if ((CMPX & 0x8000) != 0)
        		clsRegisters.CC |= clsRegisters.CC_N;
        	if ((clsRegisters.X & 0xFFFF) < (value & 0xFFFF))
        		clsRegisters.CC |= clsRegisters.CC_C;
        	if (( (clsRegisters.X ^ value) & (clsRegisters.X ^ CMPX) & 0x8000 ) != 0)
        		clsRegisters.CC |= clsRegisters.CC_V;

        }
        else if (octet1.equals("9C"))  // CMPX DIRECT EX : CMPX $12 OU CMPX <$12
        {   
            if(taille != 2)
        	{
        		clsErreur.afficherMessage();
        		return;
        	}		
    		String adress = clsMoto6809.intToHex(clsRegisters.DP) + octet2 ;
    		int a1 = Integer.parseInt(adress, 16);
    		int a2 = a1 + 1;
        	int value = Integer.parseInt(clsRAM.model.getValueAt(a1, 1).toString() + clsRAM.model.getValueAt(a2, 1).toString(), 16);
        	int CMPX = (clsRegisters.X & 0xFFFF)- (value & 0xFFFF); 
        	
        	clsRegisters.CC &= ~(clsRegisters.CC_N 
                   | clsRegisters.CC_Z 
                   | clsRegisters.CC_V 
                   | clsRegisters.CC_C);
        	
        	if ((CMPX & 0xFFFF) == 0 )
        		clsRegisters.CC |= clsRegisters.CC_Z;
        	if ((CMPX & 0x8000) != 0)
        		clsRegisters.CC |= clsRegisters.CC_N;
        	if ((clsRegisters.X & 0xFFFF) < (value & 0xFFFF))
        		clsRegisters.CC |= clsRegisters.CC_C;
        	if (( (clsRegisters.X ^ value) & (clsRegisters.X ^ CMPX) & 0x8000 ) != 0)
        		clsRegisters.CC |= clsRegisters.CC_V;
        }
        else if (octet1.equals("BC") && octet2.equals("9F"))  // CMPX ETENDU INDIRECT EX : CMPX [$1234]
        {  	
            if(taille != 4)
        	{
        		clsErreur.afficherMessage();
        		return;
        	}	
    		String adress = octet3 + octet4 ;
    		int a1 = Integer.parseInt(adress, 16);
    		int a2 = a1 + 1;
    		int address1 = Integer.parseInt(clsRAM.model.getValueAt(a1, 1).toString(), 16);
    		int address2 = Integer.parseInt(clsRAM.model.getValueAt(a2, 1).toString(), 16);
    		int AEI1 = ((address1 << 8) | address2 )& 0xFFFF;
    		int AEI2 = (AEI1 + 1 ) & 0xFFFF;
        	int value = Integer.parseInt((clsRAM.model.getValueAt(AEI1, 1).toString() + clsRAM.model.getValueAt(AEI2, 1).toString()), 16);
        	int CMPX = (clsRegisters.X & 0xFFFF) - (value & 0xFFFF); 
        	
        	clsRegisters.CC &= ~(clsRegisters.CC_N 
                   | clsRegisters.CC_Z 
                   | clsRegisters.CC_V 
                   | clsRegisters.CC_C);
        	
        	if ((CMPX & 0xFFFF) == 0 )
        		clsRegisters.CC |= clsRegisters.CC_Z;
        	if ((CMPX & 0x8000) != 0)
        		clsRegisters.CC |= clsRegisters.CC_N;
        	if ((clsRegisters.X & 0xFFFF) < (value & 0xFFFF))
        		clsRegisters.CC |= clsRegisters.CC_C;
        	if (( (clsRegisters.X ^ value) & (clsRegisters.X ^ CMPX) & 0x8000 ) != 0)
        		clsRegisters.CC |= clsRegisters.CC_V;
        }
        else if (octet1.equals("BC"))  // CMPX ETENDU DIRECT EX : CMPX $1234 OU CMPX >$1234
        {	
            if(taille != 4)
        	{
        		clsErreur.afficherMessage();
        		return;
        	}	
    		String adress = octet2 + octet3 ;
    		int a1 = Integer.parseInt(adress, 16);
    		int a2 = a1 + 1;
        	int value = Integer.parseInt(clsRAM.model.getValueAt(a1, 1).toString() + clsRAM.model.getValueAt(a2, 1).toString(), 16);
        	int CMPX = (clsRegisters.X & 0xFFFF)- (value & 0xFFFF); 
        	
        	clsRegisters.CC &= ~(clsRegisters.CC_N 
                   | clsRegisters.CC_Z 
                   | clsRegisters.CC_V 
                   | clsRegisters.CC_C);
        	
        	if ((CMPX & 0xFFFF) == 0 )
        		clsRegisters.CC |= clsRegisters.CC_Z;
        	if ((CMPX & 0x8000) != 0)
        		clsRegisters.CC |= clsRegisters.CC_N;
        	if ((clsRegisters.X & 0xFFFF) < (value & 0xFFFF))
        		clsRegisters.CC |= clsRegisters.CC_C;
        	if (( (clsRegisters.X ^ value) & (clsRegisters.X ^ CMPX) & 0x8000 ) != 0)
        		clsRegisters.CC |= clsRegisters.CC_V;
        }   
        else if (octet1.equals("AC")&& (Integer.parseInt(octet2, 16) & 16) != 16)  // CMPX INDEXE DIRECT 
        { 		
    		String adress = getaddress() ;
    		int a1 = Integer.parseInt(adress, 16);
    		int a2 = a1 + 1;
        	int value = Integer.parseInt(clsRAM.model.getValueAt(a1, 1).toString() + clsRAM.model.getValueAt(a2, 1).toString(), 16);
        	int CMPX = (clsRegisters.X & 0xFFFF)- (value & 0xFFFF); 
        	
        	clsRegisters.CC &= ~(clsRegisters.CC_N 
                   | clsRegisters.CC_Z 
                   | clsRegisters.CC_V 
                   | clsRegisters.CC_C);
        	
        	if ((CMPX & 0xFFFF) == 0 )
        		clsRegisters.CC |= clsRegisters.CC_Z;
        	if ((CMPX & 0x8000) != 0)
        		clsRegisters.CC |= clsRegisters.CC_N;
        	if ((clsRegisters.X & 0xFFFF) < (value & 0xFFFF))
        		clsRegisters.CC |= clsRegisters.CC_C;
        	if (( (clsRegisters.X ^ value) & (clsRegisters.X ^ CMPX) & 0x8000 ) != 0)
        		clsRegisters.CC |= clsRegisters.CC_V;
        }
        else if (octet1.equals("AC")&& (Integer.parseInt(octet2, 16) & 16) == 16)  // CMPX INDEXE INDIRECT
        {  	
    		String adress = getaddress();
    		int a1 = Integer.parseInt(adress, 16);
    		int a2 = a1 + 1;
    		int address1 = Integer.parseInt(clsRAM.model.getValueAt(a1, 1).toString(), 16);
    		int address2 = Integer.parseInt(clsRAM.model.getValueAt(a2, 1).toString(), 16);
    		int AEI1 = ((address1 << 8) | address2 )& 0xFFFF;
    		int AEI2 = (AEI1 + 1 ) & 0xFFFF;
        	int value = Integer.parseInt((clsRAM.model.getValueAt(AEI1, 1).toString() + clsRAM.model.getValueAt(AEI2, 1).toString()), 16);
        	int CMPX = (clsRegisters.X & 0xFFFF) - (value & 0xFFFF); 
        	
        	clsRegisters.CC &= ~(clsRegisters.CC_N 
                   | clsRegisters.CC_Z 
                   | clsRegisters.CC_V 
                   | clsRegisters.CC_C);
        	
        	if ((CMPX & 0xFFFF) == 0 )
        		clsRegisters.CC |= clsRegisters.CC_Z;
        	if ((CMPX & 0x8000) != 0)
        		clsRegisters.CC |= clsRegisters.CC_N;
        	if ((clsRegisters.X & 0xFFFF) < (value & 0xFFFF))
        		clsRegisters.CC |= clsRegisters.CC_C;
        	if (( (clsRegisters.X ^ value) & (clsRegisters.X ^ CMPX) & 0x8000 ) != 0)
        		clsRegisters.CC |= clsRegisters.CC_V;
        }

        else if(octet1.equals("84")) //ANDA immediat ex : ANDA #$34
        {
            if(taille != 2)
            {
                clsErreur.afficherMessage();
                return;
            }
            int value = Integer.parseInt(octet2, 16);
            clsRegisters.A = (clsRegisters.A & value) & 0xFF;

            clsRegisters.CC &= ~(clsRegisters.CC_N | clsRegisters.CC_Z | clsRegisters.CC_V);

            if ((clsRegisters.A & 0xFF) == 0)
                clsRegisters.CC |= clsRegisters.CC_Z;
            if ((clsRegisters.A & 0x80) != 0)
                clsRegisters.CC |= clsRegisters.CC_N;

        }
        else if(octet1.equals("94")) //ANDA direct ex : ANDA $34
        {
            if(taille != 2)
            {
                clsErreur.afficherMessage();
                return;
            }
            String adress= clsRegisters.DP + octet2;
            int value = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress, 16), 1).toString(), 16);
            clsRegisters.A = (clsRegisters.A & value) & 0xFF;

            clsRegisters.CC &= ~(clsRegisters.CC_N | clsRegisters.CC_Z | clsRegisters.CC_V);

            if ((clsRegisters.A & 0xFF) == 0)
                clsRegisters.CC |= clsRegisters.CC_Z;
            if ((clsRegisters.A & 0x80) != 0)
                clsRegisters.CC |= clsRegisters.CC_N;
        }
        else if(octet1.equals("B4") && octet2.equals("9F")) //ANDA etendu indirect ex : ANDA [$1234]
        {
            if(taille != 4)
            {
                clsErreur.afficherMessage();
                return;
            }
            String adress1= octet3 + octet4;
            String adress2=clsRAM.model.getValueAt(Integer.parseInt(adress1, 16), 1).toString();
            adress2+=clsRAM.model.getValueAt(Integer.parseInt(adress1, 16)+1, 1).toString();
            int value = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress2, 16), 1).toString(), 16);
            clsRegisters.A = (clsRegisters.A & value) & 0xFF;

            clsRegisters.CC &= ~(clsRegisters.CC_N | clsRegisters.CC_Z | clsRegisters.CC_V);

            if ((clsRegisters.A & 0xFF) == 0)
                clsRegisters.CC |= clsRegisters.CC_Z;
            if ((clsRegisters.A & 0x80) != 0)
                clsRegisters.CC |= clsRegisters.CC_N;
        }
        else if(octet1.equals("B4")) //ANDA etendu direct ex : ANDA $1234
        {
            if(taille != 3)
            {
                clsErreur.afficherMessage();
                return;
            }
            String adress= octet2 + octet3;
            int value = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress, 16), 1).toString(), 16);
            clsRegisters.A = (clsRegisters.A & value) & 0xFF;

            clsRegisters.CC &= ~(clsRegisters.CC_N | clsRegisters.CC_Z | clsRegisters.CC_V);

            if ((clsRegisters.A & 0xFF) == 0)
                clsRegisters.CC |= clsRegisters.CC_Z;
            if ((clsRegisters.A & 0x80) != 0)
                clsRegisters.CC |= clsRegisters.CC_N;
        }
        else if(octet1.equals("A4")&& (Integer.parseInt(octet2, 16) & 16) != 16) //ANDA INDEXE direct 
        {
            String adress= getaddress();
            int value = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress, 16), 1).toString(), 16);
            clsRegisters.A = (clsRegisters.A & value) & 0xFF;

            clsRegisters.CC &= ~(clsRegisters.CC_N | clsRegisters.CC_Z | clsRegisters.CC_V);

            if ((clsRegisters.A & 0xFF) == 0)
                clsRegisters.CC |= clsRegisters.CC_Z;
            if ((clsRegisters.A & 0x80) != 0)
                clsRegisters.CC |= clsRegisters.CC_N;
        }
        else if(octet1.equals("A4")&& (Integer.parseInt(octet2, 16) & 16) == 16) //ANDA INDEXE indirect
        {
            String adress1= getaddress();
            String adress2=clsRAM.model.getValueAt(Integer.parseInt(adress1, 16), 1).toString();
            adress2+=clsRAM.model.getValueAt(Integer.parseInt(adress1, 16)+1, 1).toString();
            int value = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress2, 16), 1).toString(), 16);
            clsRegisters.A = (clsRegisters.A & value) & 0xFF;

            clsRegisters.CC &= ~(clsRegisters.CC_N | clsRegisters.CC_Z | clsRegisters.CC_V);

            if ((clsRegisters.A & 0xFF) == 0)
                clsRegisters.CC |= clsRegisters.CC_Z;
            if ((clsRegisters.A & 0x80) != 0)
                clsRegisters.CC |= clsRegisters.CC_N;
        }

        else if(octet1.equals("C4")) //ANDB immediat ex : ANDB #$34
        {
            if(taille != 2)
            {
                clsErreur.afficherMessage();
                return;
            }
            int value = Integer.parseInt(octet2, 16);
            clsRegisters.B = (clsRegisters.B & value) & 0xFF;

            clsRegisters.CC &= ~(clsRegisters.CC_N | clsRegisters.CC_Z | clsRegisters.CC_V);

            if ((clsRegisters.B & 0xFF) == 0)
                clsRegisters.CC |= clsRegisters.CC_Z;
            if ((clsRegisters.B & 0x80) != 0)
                clsRegisters.CC |= clsRegisters.CC_N;
        }
        else if(octet1.equals("D4")) //ANDB direct ex : ANDB $34
        {
            if(taille != 2)
            {
                clsErreur.afficherMessage();
                return;
            }
            String adress= clsRegisters.DP + octet2;
            int value = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress, 16), 1).toString(), 16);
            clsRegisters.B = (clsRegisters.B & value) & 0xFF;
            clsRegisters.CC &= ~(clsRegisters.CC_N | clsRegisters.CC_Z | clsRegisters.CC_V);

            if ((clsRegisters.B & 0xFF) == 0)
                clsRegisters.CC |= clsRegisters.CC_Z;
            if ((clsRegisters.B & 0x80) != 0)
                clsRegisters.CC |= clsRegisters.CC_N;
        }
        else if(octet1.equals("E4")&& (Integer.parseInt(octet2, 16) & 16) != 16) //ANDB indexe direct ex : ANDB ,X
        {
            String adress= getaddress();
            int value = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress, 16), 1).toString(), 16);
            clsRegisters.B = (clsRegisters.B & value) & 0xFF;
            clsRegisters.CC &= ~(clsRegisters.CC_N | clsRegisters.CC_Z | clsRegisters.CC_V);

            if ((clsRegisters.B & 0xFF) == 0)
                clsRegisters.CC |= clsRegisters.CC_Z;
            if ((clsRegisters.B & 0x80) != 0)
                clsRegisters.CC |= clsRegisters.CC_N;
        }
        else if(octet1.equals("E4")&& (Integer.parseInt(octet2, 16) & 16) == 16) //ANDB indexe indirect
        {
            String adress1= getaddress();
            String adress2=clsRAM.model.getValueAt(Integer.parseInt(adress1, 16), 1).toString();
            adress2+=clsRAM.model.getValueAt(Integer.parseInt(adress1, 16)+1, 1).toString();
            int value = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress2, 16), 1).toString(), 16);
            clsRegisters.B = (clsRegisters.B & value) & 0xFF;
            clsRegisters.CC &= ~(clsRegisters.CC_N | clsRegisters.CC_Z | clsRegisters.CC_V);

            if ((clsRegisters.B & 0xFF) == 0)
                clsRegisters.CC |= clsRegisters.CC_Z;
            if ((clsRegisters.B & 0x80) != 0)
                clsRegisters.CC |= clsRegisters.CC_N;
        }
        else if(octet1.equals("F4") && octet2.equals("9F")) //ANDB etendu indirect ex : ANDB [$1234]
        {
            if(taille != 4)
            {
                clsErreur.afficherMessage();
                return;
            }
            String adress1= octet3 + octet4;
            String adress2=clsRAM.model.getValueAt(Integer.parseInt(adress1, 16), 1).toString();
            adress2+=clsRAM.model.getValueAt(Integer.parseInt(adress1, 16)+1, 1).toString();
            int value = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress2, 16), 1).toString(), 16);
            clsRegisters.B = (clsRegisters.B & value) & 0xFF;
            clsRegisters.CC &= ~(clsRegisters.CC_N | clsRegisters.CC_Z | clsRegisters.CC_V);

            if ((clsRegisters.B & 0xFF) == 0)
                clsRegisters.CC |= clsRegisters.CC_Z;
            if ((clsRegisters.B & 0x80) != 0)
                clsRegisters.CC |= clsRegisters.CC_N;
        }
        else if(octet1.equals("F4")) //ANDB etendu direct ex : ANDB $1234
        {
            if(taille != 3)
            {
                clsErreur.afficherMessage();
                return;
            }
            String adress= octet2 + octet3;
            int value = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress, 16), 1).toString(), 16);
            clsRegisters.B = (clsRegisters.B & value) & 0xFF;

            clsRegisters.CC &= ~(clsRegisters.CC_N | clsRegisters.CC_Z | clsRegisters.CC_V);

            if ((clsRegisters.B & 0xFF) == 0)
                clsRegisters.CC |= clsRegisters.CC_Z;
            if ((clsRegisters.B & 0x80) != 0)
                clsRegisters.CC |= clsRegisters.CC_N;
        }
        
        else if(octet1.equals("1C")) //ANDCC immediat ex : ANDCC #$34
        {
            if(taille != 2)
            {
                clsErreur.afficherMessage();
                return;
            }
            int value = Integer.parseInt(octet2, 16);
            clsRegisters.CC = (clsRegisters.CC & value) & 0xFF;
        }
        
        else if(octet1.equals("3A")) //ABX inherent ex : ABX
        {
            if(taille != 1)
            {
                clsErreur.afficherMessage();
                return;
            }
            clsRegisters.X = (clsRegisters.X + clsRegisters.B) & 0xFFFF;
        }

        else if(octet1.equals("89")) //ADCA immediat ex : ADCA #$34
        {
            if(taille != 2)
            {
                clsErreur.afficherMessage();
                return;
            }
        int value = Integer.parseInt(octet2, 16);
        int oldA  = clsRegisters.A;
        int carry = clsRegisters.CC & 1;

        int result = oldA + value + carry;
        clsRegisters.A = result & 0xFF;

        clsRegisters.CC &= ~(clsRegisters.CC_N | clsRegisters.CC_Z | clsRegisters.CC_V | clsRegisters.CC_C);

        if ((clsRegisters.A & 0x80) != 0)
            clsRegisters.CC |= clsRegisters.CC_N;

        if (clsRegisters.A == 0)
            clsRegisters.CC |= clsRegisters.CC_Z;

        if (result > 0xFF)
            clsRegisters.CC |= clsRegisters.CC_C;

        if (((oldA ^ value) & 0x80) == 0 &&
            ((oldA ^ result) & 0x80) != 0)
            clsRegisters.CC |= clsRegisters.CC_V;
            
        }
        else if(octet1.equals("99")) //ADCA direct ex : ADCA $34
        {
            if(taille != 2)
            {
                clsErreur.afficherMessage();
                return;
            }
            String adress= clsRegisters.formatTo2Digits(clsRegisters.DP) + String.format("%02X",octet2);
            int value = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress, 16), 1).toString(), 16);
            int carry = clsRegisters.CC & 1;
            int oldA  = clsRegisters.A;
            int result = oldA + value + carry;
            clsRegisters.A = (clsRegisters.A + value + carry) & 0xFF;

            clsRegisters.CC &= ~(clsRegisters.CC_N | clsRegisters.CC_Z | clsRegisters.CC_V | clsRegisters.CC_C);

        if ((clsRegisters.A & 0x80) != 0)
            clsRegisters.CC |= clsRegisters.CC_N;

        if (clsRegisters.A == 0)
            clsRegisters.CC |= clsRegisters.CC_Z;

        if (result > 0xFF)
            clsRegisters.CC |= clsRegisters.CC_C;

        if (((oldA ^ value) & 0x80) == 0 &&
            ((oldA ^ result) & 0x80) != 0)
            clsRegisters.CC |= clsRegisters.CC_V;
        }
        else if(octet1.equals("A9")&& (Integer.parseInt(octet2, 16) & 16) != 16) //ADCA indexe direct ex : ADCA ,X
        {
            String adress= getaddress();
            int value = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress, 16), 1).toString(), 16);
            int carry = clsRegisters.CC & 1;
            int oldA  = clsRegisters.A;
            int result = oldA + value + carry;
            clsRegisters.A = (clsRegisters.A + value + carry) & 0xFF;

            clsRegisters.CC &= ~(clsRegisters.CC_N | clsRegisters.CC_Z | clsRegisters.CC_V | clsRegisters.CC_C);

        if ((clsRegisters.A & 0x80) != 0)
            clsRegisters.CC |= clsRegisters.CC_N;

        if (clsRegisters.A == 0)
            clsRegisters.CC |= clsRegisters.CC_Z;

        if (result > 0xFF)
            clsRegisters.CC |= clsRegisters.CC_C;

        if (((oldA ^ value) & 0x80) == 0 &&
            ((oldA ^ result) & 0x80) != 0)
            clsRegisters.CC |= clsRegisters.CC_V;
        }
        else if(octet1.equals("A9")&& (Integer.parseInt(octet2, 16) & 16) == 16) //ADCA indexe indirect
        {
            String adress1 =getaddress();
            String adress2=clsRAM.model.getValueAt(Integer.parseInt(adress1, 16), 1).toString();
            adress2+=clsRAM.model.getValueAt(Integer.parseInt(adress1, 16)+1, 1).toString();
            int value = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress2, 16), 1).toString(), 16);
            int carry = clsRegisters.CC & 1;
            int oldA  = clsRegisters.A;
            int result = oldA + value + carry;
            clsRegisters.A = (clsRegisters.A + value + carry) & 0xFF;

            clsRegisters.CC &= ~(clsRegisters.CC_N | clsRegisters.CC_Z | clsRegisters.CC_V | clsRegisters.CC_C);

        if ((clsRegisters.A & 0x80) != 0)
            clsRegisters.CC |= clsRegisters.CC_N;

        if (clsRegisters.A == 0)
            clsRegisters.CC |= clsRegisters.CC_Z;

        if (result > 0xFF)
            clsRegisters.CC |= clsRegisters.CC_C;

        if (((oldA ^ value) & 0x80) == 0 &&
            ((oldA ^ result) & 0x80) != 0)
            clsRegisters.CC |= clsRegisters.CC_V;
        }
        else if(octet1.equals("B9") && octet2.equals("9F")) //ADCA etendu indirect ex : ADCA [$1234]
        {
            if(taille != 4)
            {
                clsErreur.afficherMessage();
                return;
            }
            String adress1 = octet3 + octet4;
            String adress2=clsRAM.model.getValueAt(Integer.parseInt(adress1, 16), 1).toString();
            adress2+=clsRAM.model.getValueAt(Integer.parseInt(adress1, 16)+1, 1).toString();
            int value = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress2, 16), 1).toString(), 16);
            int carry = clsRegisters.CC & 1;
            int oldA  = clsRegisters.A;
            int result = oldA + value + carry;
            clsRegisters.A = (clsRegisters.A + value + carry) & 0xFF;

            clsRegisters.CC &= ~(clsRegisters.CC_N | clsRegisters.CC_Z | clsRegisters.CC_V | clsRegisters.CC_C);

        if ((clsRegisters.A & 0x80) != 0)
            clsRegisters.CC |= clsRegisters.CC_N;

        if (clsRegisters.A == 0)
            clsRegisters.CC |= clsRegisters.CC_Z;

        if (result > 0xFF)
            clsRegisters.CC |= clsRegisters.CC_C;

        if (((oldA ^ value) & 0x80) == 0 &&
            ((oldA ^ result) & 0x80) != 0)
            clsRegisters.CC |= clsRegisters.CC_V;
        }
        else if(octet1.equals("B9") && !octet2.equals("9F")) //ADCA etendu direct ex : ADCA $1234
        {
            if(taille != 3)
            {
                clsErreur.afficherMessage();
                return;
            }
            String adress= octet2 + octet3;
            int value = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress, 16), 1).toString(), 16);
            int carry = clsRegisters.CC & 1;
            int oldA  = clsRegisters.A;
            int result = oldA + value + carry;
            clsRegisters.A = (clsRegisters.A + value + carry) & 0xFF;

            clsRegisters.CC &= ~(clsRegisters.CC_N | clsRegisters.CC_Z | clsRegisters.CC_V | clsRegisters.CC_C);

        if ((clsRegisters.A & 0x80) != 0)
            clsRegisters.CC |= clsRegisters.CC_N;

        if (clsRegisters.A == 0)
            clsRegisters.CC |= clsRegisters.CC_Z;

        if (result > 0xFF)
            clsRegisters.CC |= clsRegisters.CC_C;

        if (((oldA ^ value) & 0x80) == 0 &&
            ((oldA ^ result) & 0x80) != 0)
            clsRegisters.CC |= clsRegisters.CC_V;
        }

        else if(octet1.equals("C9")) //ADCB immediat ex : ADCB #$34
        {
            if(taille != 2)
            {
                clsErreur.afficherMessage();
                return;
            }
        int value = Integer.parseInt(octet2, 16);
        int oldB  = clsRegisters.B;
        int carry = clsRegisters.CC & 1;

        int result = oldB + value + carry;
        clsRegisters.B = result & 0xFF;

        clsRegisters.CC &= ~(clsRegisters.CC_N | clsRegisters.CC_Z | clsRegisters.CC_V | clsRegisters.CC_C);

        if ((clsRegisters.B & 0x80) != 0)
            clsRegisters.CC |= clsRegisters.CC_N;

        if (clsRegisters.B == 0)
            clsRegisters.CC |= clsRegisters.CC_Z;

        if (result > 0xFF)
            clsRegisters.CC |= clsRegisters.CC_C;

        if (((oldB ^ value) & 0x80) == 0 &&
            ((oldB ^ result) & 0x80) != 0)
            clsRegisters.CC |= clsRegisters.CC_V;
        }
        else if(octet1.equals("D9")) //ADCB direct ex : ADCB $34
        {
            if(taille != 2)
            {
                clsErreur.afficherMessage();
                return;
            }
            String adress= clsRegisters.formatTo2Digits(clsRegisters.DP) + String.format("%02X",octet2);
            int value = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress, 16), 1).toString(), 16);
            int carry = clsRegisters.CC & 1;
            int oldB  = clsRegisters.B;
            int result = oldB + value + carry;
            clsRegisters.B = (clsRegisters.B + value + carry) & 0xFF;

            clsRegisters.CC &= ~(clsRegisters.CC_N | clsRegisters.CC_Z | clsRegisters.CC_V | clsRegisters.CC_C);

        if ((clsRegisters.B & 0x80) != 0)
            clsRegisters.CC |= clsRegisters.CC_N;

        if (clsRegisters.B == 0)
            clsRegisters.CC |= clsRegisters.CC_Z;

        if (result > 0xFF)
            clsRegisters.CC |= clsRegisters.CC_C;

        if (((oldB ^ value) & 0x80) == 0 &&
            ((oldB ^ result) & 0x80) != 0)
            clsRegisters.CC |= clsRegisters.CC_V;
        }
        else if(octet1.equals("E9")&& (Integer.parseInt(octet2, 16) & 16) != 16) //ADCB indexe direct ex : ADCB ,X
        {
            String adress= getaddress();
            int value = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress, 16), 1).toString(), 16);
            int carry = clsRegisters.CC & 1;
            int oldB  = clsRegisters.B;
            int result = oldB + value + carry;
            clsRegisters.B = (clsRegisters.B + value + carry) & 0xFF;

            clsRegisters.CC &= ~(clsRegisters.CC_N | clsRegisters.CC_Z | clsRegisters.CC_V | clsRegisters.CC_C);

        if ((clsRegisters.B & 0x80) != 0)
            clsRegisters.CC |= clsRegisters.CC_N;

        if (clsRegisters.B == 0)
            clsRegisters.CC |= clsRegisters.CC_Z;

        if (result > 0xFF)
            clsRegisters.CC |= clsRegisters.CC_C;

        if (((oldB ^ value) & 0x80) == 0 &&
            ((oldB ^ result) & 0x80) != 0)
            clsRegisters.CC |= clsRegisters.CC_V;
        }
        else if(octet1.equals("E9")&& (Integer.parseInt(octet2, 16) & 16) == 16) //ADCB indexe indirect 
        {
            String adress1= getaddress();
            String adress2=clsRAM.model.getValueAt(Integer.parseInt(adress1, 16), 1).toString();
            adress2+=clsRAM.model.getValueAt(Integer.parseInt(adress1, 16)+1, 1).toString();
            int value = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress2, 16), 1).toString(), 16);
            int carry = clsRegisters.CC & 1;
            int oldB  = clsRegisters.B;
            int result = oldB + value + carry;
            clsRegisters.B = (clsRegisters.B + value + carry) & 0xFF;

            clsRegisters.CC &= ~(clsRegisters.CC_N | clsRegisters.CC_Z | clsRegisters.CC_V | clsRegisters.CC_C);

        if ((clsRegisters.B & 0x80) != 0)
            clsRegisters.CC |= clsRegisters.CC_N;

        if (clsRegisters.B == 0)
            clsRegisters.CC |= clsRegisters.CC_Z;

        if (result > 0xFF)
            clsRegisters.CC |= clsRegisters.CC_C;

        if (((oldB ^ value) & 0x80) == 0 &&
            ((oldB ^ result) & 0x80) != 0)
            clsRegisters.CC |= clsRegisters.CC_V;
        }
        else if(octet1.equals("F9") && octet2.equals("9F")) //ADCB etendu indirect ex : ADCB [$1234]
        {
            if(taille != 4)
            {
                clsErreur.afficherMessage();
                return;
            }
            String adress1= octet3 + octet4;
            String adress2=clsRAM.model.getValueAt(Integer.parseInt(adress1, 16), 1).toString();
            adress2+=clsRAM.model.getValueAt(Integer.parseInt(adress1, 16)+1, 1).toString();
            int value = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress2, 16), 1).toString(), 16);
            int carry = clsRegisters.CC & 1;
            int oldB  = clsRegisters.B;
            int result = oldB + value + carry;
            clsRegisters.B = (clsRegisters.B + value + carry) & 0xFF;

            clsRegisters.CC &= ~(clsRegisters.CC_N | clsRegisters.CC_Z | clsRegisters.CC_V | clsRegisters.CC_C);

        if ((clsRegisters.B & 0x80) != 0)
            clsRegisters.CC |= clsRegisters.CC_N;

        if (clsRegisters.B == 0)
            clsRegisters.CC |= clsRegisters.CC_Z;

        if (result > 0xFF)
            clsRegisters.CC |= clsRegisters.CC_C;

        if (((oldB ^ value) & 0x80) == 0 &&
            ((oldB ^ result) & 0x80) != 0)
            clsRegisters.CC |= clsRegisters.CC_V;
        }
        else if(octet1.equals("F9")) //ADCB etendu direct ex : ADCB $1234
        {
            if(taille != 3)
            {
                clsErreur.afficherMessage();
                return;
            }
            String adress= octet2 + octet3;
            int value = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress, 16), 1).toString(), 16);
            int carry = clsRegisters.CC & 1;
            int oldB  = clsRegisters.B;
            int result = oldB + value + carry;
            clsRegisters.B = (clsRegisters.B + value + carry) & 0xFF;

            clsRegisters.CC &= ~(clsRegisters.CC_N | clsRegisters.CC_Z | clsRegisters.CC_V | clsRegisters.CC_C);

        if ((clsRegisters.B & 0x80) != 0)
            clsRegisters.CC |= clsRegisters.CC_N;

        if (clsRegisters.B == 0)
            clsRegisters.CC |= clsRegisters.CC_Z;

        if (result > 0xFF)
            clsRegisters.CC |= clsRegisters.CC_C;

        if (((oldB ^ value) & 0x80) == 0 &&
            ((oldB ^ result) & 0x80) != 0)
            clsRegisters.CC |= clsRegisters.CC_V;
        }

        else if(octet1.equals("4F")) //CLRA INHERENT
        {
            if(taille != 1)
            {
                clsErreur.afficherMessage();
                return;
            }
            clsRegisters.A = 0;
            clsRegisters.CC &= ~(clsRegisters.CC_N | clsRegisters.CC_Z | clsRegisters.CC_V);
            clsRegisters.CC |= clsRegisters.CC_Z;
        }
        else if(octet1.equals("5F")) //CLRB INHERENT
        {
            if(taille != 1)
            {
                clsErreur.afficherMessage();
                return;
            }
            clsRegisters.B = 0;
            clsRegisters.CC &= ~(clsRegisters.CC_N | clsRegisters.CC_Z | clsRegisters.CC_V);
            clsRegisters.CC |= clsRegisters.CC_Z;
        }
        else if(octet1.equals("0F")) //CLR DIRECT EX : CLR $12
        {
            if(taille != 2)
            {
                clsErreur.afficherMessage();
                return;
            }
            String adress= clsRegisters.DP + octet2;
            int y = Integer.parseInt(adress, 16);
            clsRAM.model.setValueAt("00", y, 1);
            
            clsRegisters.CC &= ~(clsRegisters.CC_N | clsRegisters.CC_Z | clsRegisters.CC_V);
            clsRegisters.CC |= clsRegisters.CC_Z;

        }
        else if(octet1.equals("6F")&& (Integer.parseInt(octet2, 16) & 16) != 16) //CLR INDEXE DIRECT EX : CLR 4,U
        {
            String adress= getaddress();
            int y = Integer.parseInt(adress, 16);
            clsRAM.model.setValueAt("00", y, 1);
            
            clsRegisters.CC &= ~(clsRegisters.CC_N | clsRegisters.CC_Z | clsRegisters.CC_V);
            clsRegisters.CC |= clsRegisters.CC_Z;
        }
        else if(octet1.equals("6F")&& (Integer.parseInt(octet2, 16) & 16) == 16) //CLR INDEXE INDIRECT
        {
            String adress = getaddress();
            String adress2 = clsRAM.model.getValueAt(Integer.parseInt(adress, 16), 1).toString();
            adress2+=clsRAM.model.getValueAt(Integer.parseInt(adress, 16)+1, 1).toString();
            int y = Integer.parseInt(adress2, 16);
            clsRAM.model.setValueAt("00", y, 1);

            clsRegisters.CC &= ~(clsRegisters.CC_N | clsRegisters.CC_Z | clsRegisters.CC_V);
            clsRegisters.CC |= clsRegisters.CC_Z;
        }
        else if(octet1.equals("0F")) //CLR ETENDU DIRECT EX : CLR $1234
        {
            if(taille != 3)
            {
                clsErreur.afficherMessage();
                return;
            }
            String adress = octet2 + octet3;
            int y = Integer.parseInt(adress, 16);
            clsRAM.model.setValueAt("00", y, 1);
            
            clsRegisters.CC &= ~(clsRegisters.CC_N | clsRegisters.CC_Z | clsRegisters.CC_V);
            clsRegisters.CC |= clsRegisters.CC_Z;
        }
        else if(octet1.equals("0F") && octet2.equals("9F")) //CLR ETENDU INDIRECT EX : CLR [$1234]
        {
            String adress = octet3 + octet4;
            String adress2 = clsRAM.model.getValueAt(Integer.parseInt(adress, 16), 1).toString();
            adress2+=clsRAM.model.getValueAt(Integer.parseInt(adress, 16)+1, 1).toString();
            int y = Integer.parseInt(adress2, 16);
            clsRAM.model.setValueAt("00", y, 1);

            clsRegisters.CC &= ~(clsRegisters.CC_N | clsRegisters.CC_Z | clsRegisters.CC_V);
            clsRegisters.CC |= clsRegisters.CC_Z;
        }

        else if (octet1.equals("8B"))      	//ADDA IMMEDIAT EX : ADDA #$12
        {
        	if (taille != 2)
        	{
        		clsErreur.afficherMessage();
        		return;
        	}
        	int value = Integer.parseInt(octet2, 16);
        	int oldA = clsRegisters.A;
            clsRegisters.A = ( clsRegisters.A + value ) & 0xFF;
        	int result = oldA + value;
        	
            clsRegisters.CC &= ~(clsRegisters.CC_N | clsRegisters.CC_Z | clsRegisters.CC_V | clsRegisters.CC_C);

        	if ((clsRegisters.A & 0x80) != 0)
                clsRegisters.CC |= clsRegisters.CC_N;
        	if (clsRegisters.A == 0)
                clsRegisters.CC |= clsRegisters.CC_Z;
        	if (result > 0xFF)
        		clsRegisters.CC |= clsRegisters.CC_C;
        	if (((oldA ^ value) & 0x80) == 0  &&  ((oldA ^ result) & 0x80) != 0)
        	    clsRegisters.CC |= clsRegisters.CC_V;
            
        }
        else if(octet1.equals("9B")) 			//ADDA DIRECT EX  : ADDA $34 OU ADDA <$34
        {
            if(taille != 2)
            {
                clsErreur.afficherMessage();
                return;
            }
            String adress= clsRegisters.DP + octet2;
            int value = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress, 16), 1).toString(), 16);
            int oldA = clsRegisters.A;
            clsRegisters.A = (clsRegisters.A + value) & 0xFF;
        	int result = oldA + value;

            clsRegisters.CC &= ~(clsRegisters.CC_N | clsRegisters.CC_Z | clsRegisters.CC_V | clsRegisters.CC_C);

        	if ((clsRegisters.A & 0x80) != 0)
                clsRegisters.CC |= clsRegisters.CC_N;
        	if (clsRegisters.A == 0)
                clsRegisters.CC |= clsRegisters.CC_Z;
        	if (result > 0xFF)
        		clsRegisters.CC |= clsRegisters.CC_C;
        	if (((oldA ^ value) & 0x80) == 0  &&  ((oldA ^ result) & 0x80) != 0)
        	    clsRegisters.CC |= clsRegisters.CC_V;
        }
        else if(octet1.equals("BB") && octet2.equals("9F")) //ADDA ETENDU INDIRECT ex : ADDA [$1234]
        {
            if(taille != 4)
            {
                clsErreur.afficherMessage();
                return;
            }
            String adress= octet3 + octet4;
            int ca1 = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress, 16), 1).toString(), 16);
            int ca2 = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress, 16) + 1 , 1).toString(), 16);
            int FullAdress = (ca1 << 8 | ca2 ) & 0xFFFF;
            int value = Integer.parseInt(clsRAM.model.getValueAt(FullAdress , 1).toString(), 16);
            int oldA = clsRegisters.A;
            clsRegisters.A = (clsRegisters.A + value) & 0xFF;
        	int result = oldA + value;

            clsRegisters.CC &= ~(clsRegisters.CC_N | clsRegisters.CC_Z | clsRegisters.CC_V | clsRegisters.CC_C);

        	if ((clsRegisters.A & 0x80) != 0)
                clsRegisters.CC |= clsRegisters.CC_N;
        	if (clsRegisters.A == 0)
                clsRegisters.CC |= clsRegisters.CC_Z;
        	if (result > 0xFF)
        		clsRegisters.CC |= clsRegisters.CC_C;
        	if (((oldA ^ value) & 0x80) == 0  &&  ((oldA ^ result) & 0x80) != 0)
        	    clsRegisters.CC |= clsRegisters.CC_V;
        }
        else if(octet1.equals("BB"))              //ADDA ETENDU DIRECT EX : ADDA $1234 OU ADDA >$1234
        {
            if(taille != 3)
            {
                clsErreur.afficherMessage();
                return;
            }
            String adress= octet2 + octet3;
            int value = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress, 16), 1).toString(), 16);
            int oldA = clsRegisters.A;
            clsRegisters.A = (clsRegisters.A + value) & 0xFF;
        	int result = oldA + value;

            clsRegisters.CC &= ~(clsRegisters.CC_N | clsRegisters.CC_Z | clsRegisters.CC_V | clsRegisters.CC_C);

        	if ((clsRegisters.A & 0x80) != 0)
                clsRegisters.CC |= clsRegisters.CC_N;
        	if (clsRegisters.A == 0)
                clsRegisters.CC |= clsRegisters.CC_Z;
        	if (result > 0xFF)
        		clsRegisters.CC |= clsRegisters.CC_C;
        	if (((oldA ^ value) & 0x80) == 0  &&  ((oldA ^ result) & 0x80) != 0)
        	    clsRegisters.CC |= clsRegisters.CC_V;
        }
        else if(octet1.equals("AB") && (Integer.parseInt(octet2, 16) & 16) != 16)              //ADDA INDEXE DIRECT
        {
            String adress= getaddress();
            int value = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress, 16), 1).toString(), 16);
            int oldA = clsRegisters.A;
            clsRegisters.A = (clsRegisters.A + value) & 0xFF;
        	int result = oldA + value;

            clsRegisters.CC &= ~(clsRegisters.CC_N | clsRegisters.CC_Z | clsRegisters.CC_V | clsRegisters.CC_C);

        	if ((clsRegisters.A & 0x80) != 0)
                clsRegisters.CC |= clsRegisters.CC_N;
        	if (clsRegisters.A == 0)
                clsRegisters.CC |= clsRegisters.CC_Z;
        	if (result > 0xFF)
        		clsRegisters.CC |= clsRegisters.CC_C;
        	if (((oldA ^ value) & 0x80) == 0  &&  ((oldA ^ result) & 0x80) != 0)
        	    clsRegisters.CC |= clsRegisters.CC_V;
        }
        else if(octet1.equals("AB") && (Integer.parseInt(octet2, 16) & 16) == 16)              //ADDA INDEXE INDIRECT
        {
            String adress= getaddress();
            int ca1 = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress, 16), 1).toString(), 16);
            int ca2 = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress, 16) + 1 , 1).toString(), 16);
            int FullAdress = (ca1 << 8 | ca2 ) & 0xFFFF;
            int value = Integer.parseInt(clsRAM.model.getValueAt(FullAdress , 1).toString(), 16);
            int oldA = clsRegisters.A;
            clsRegisters.A = (clsRegisters.A + value) & 0xFF;
        	int result = oldA + value;

            clsRegisters.CC &= ~(clsRegisters.CC_N | clsRegisters.CC_Z | clsRegisters.CC_V | clsRegisters.CC_C);

        	if ((clsRegisters.A & 0x80) != 0)
                clsRegisters.CC |= clsRegisters.CC_N;
        	if (clsRegisters.A == 0)
                clsRegisters.CC |= clsRegisters.CC_Z;
        	if (result > 0xFF)
        		clsRegisters.CC |= clsRegisters.CC_C;
        	if (((oldA ^ value) & 0x80) == 0  &&  ((oldA ^ result) & 0x80) != 0)
        	    clsRegisters.CC |= clsRegisters.CC_V;
        }

        else if (octet1.equals("CB"))      	//ADDB IMMEDIAT EX : ADDB #$12
        {
        	if (taille != 2)
        	{
        		clsErreur.afficherMessage();
        		return;
        	}
        	int value = Integer.parseInt(octet2, 16);
        	int oldB = clsRegisters.B;
        	clsRegisters.B = ( clsRegisters.B + value ) & 0xFF;
        	int result = oldB + value;

            clsRegisters.CC &= ~(clsRegisters.CC_N | clsRegisters.CC_Z | clsRegisters.CC_V | clsRegisters.CC_C);

        	if ((clsRegisters.B & 0x80) != 0)
                clsRegisters.CC |= clsRegisters.CC_N;
        	if (clsRegisters.B == 0)
                clsRegisters.CC |= clsRegisters.CC_Z;
        	if (result > 0xFF)
        		clsRegisters.CC |= clsRegisters.CC_C;
        	if (((oldB ^ value) & 0x80) == 0  &&  ((oldB ^ result) & 0x80) != 0)
        	    clsRegisters.CC |= clsRegisters.CC_V;
        }
        else if(octet1.equals("DB")) 			//ADDB DIRECT EX  : ADDB $34 OU ADDB <$34
        {
            if(taille != 2)
            {
                clsErreur.afficherMessage();
                return;
            }
            String adress= clsRegisters.DP + octet2;
            int value = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress, 16), 1).toString(), 16);
            int oldB = clsRegisters.B;
            clsRegisters.B = (clsRegisters.B + value) & 0xFF;
        	int result = oldB + value;

            clsRegisters.CC &= ~(clsRegisters.CC_N | clsRegisters.CC_Z | clsRegisters.CC_V | clsRegisters.CC_C);

        	if ((clsRegisters.B & 0x80) != 0)
                clsRegisters.CC |= clsRegisters.CC_N;
        	if (clsRegisters.B == 0)
                clsRegisters.CC |= clsRegisters.CC_Z;
        	if (result > 0xFF)
        		clsRegisters.CC |= clsRegisters.CC_C;
        	if (((oldB ^ value) & 0x80) == 0  &&  ((oldB ^ result) & 0x80) != 0)
        	    clsRegisters.CC |= clsRegisters.CC_V;
        }
        else if(octet1.equals("FB") && octet2.equals("9F")) //ADDB ETENDU INDIRECT ex : ADDB [$1234]
        {
            if(taille != 4)
            {
                clsErreur.afficherMessage();
                return;
            }
            String adress= octet3 + octet4;
            int ca1 = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress, 16), 1).toString(), 16);
            int ca2 = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress, 16) + 1 , 1).toString(), 16);
            int FullAdress = (ca1 << 8 | ca2 ) & 0xFFFF;
            int value = Integer.parseInt(clsRAM.model.getValueAt(FullAdress , 1).toString(), 16);
            int oldB = clsRegisters.B;
            clsRegisters.B = (clsRegisters.B + value) & 0xFF;
        	int result = oldB + value;

            clsRegisters.CC &= ~(clsRegisters.CC_N | clsRegisters.CC_Z | clsRegisters.CC_V | clsRegisters.CC_C);

        	if ((clsRegisters.B & 0x80) != 0)
                clsRegisters.CC |= clsRegisters.CC_N;
        	if (clsRegisters.B == 0)
                clsRegisters.CC |= clsRegisters.CC_Z;
        	if (result > 0xFF)
        		clsRegisters.CC |= clsRegisters.CC_C;
        	if (((oldB ^ value) & 0x80) == 0  &&  ((oldB ^ result) & 0x80) != 0)
        	    clsRegisters.CC |= clsRegisters.CC_V;
        }
        else if(octet1.equals("FB"))              //ADDB ETENDU DIRECT EX : ADDB $1234 OU ADDB >$1234
        {
            if(taille != 3)
            {
                clsErreur.afficherMessage();
                return;
            }
            String adress= octet2 + octet3;
            int value = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress, 16), 1).toString(), 16);
            int oldB = clsRegisters.B;
            clsRegisters.B = (clsRegisters.B + value) & 0xFF;
        	int result = oldB + value;

            clsRegisters.CC &= ~(clsRegisters.CC_N | clsRegisters.CC_Z | clsRegisters.CC_V | clsRegisters.CC_C);

        	if ((clsRegisters.B & 0x80) != 0)
                clsRegisters.CC |= clsRegisters.CC_N;
        	if (clsRegisters.B == 0)
                clsRegisters.CC |= clsRegisters.CC_Z;
        	if (result > 0xFF)
        		clsRegisters.CC |= clsRegisters.CC_C;
        	if (((oldB ^ value) & 0x80) == 0  &&  ((oldB ^ result) & 0x80) != 0)
        	    clsRegisters.CC |= clsRegisters.CC_V;
        }
        else if(octet1.equals("EB") && (Integer.parseInt(octet2, 16) & 16) != 16)              //ADDB INDEXE DIRECT 
        {
            String adress= getaddress();
            int value = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress, 16), 1).toString(), 16);
            int oldB = clsRegisters.B;
            clsRegisters.B = (clsRegisters.B + value) & 0xFF;
        	int result = oldB + value;

            clsRegisters.CC &= ~(clsRegisters.CC_N | clsRegisters.CC_Z | clsRegisters.CC_V | clsRegisters.CC_C);

        	if ((clsRegisters.B & 0x80) != 0)
                clsRegisters.CC |= clsRegisters.CC_N;
        	if (clsRegisters.B == 0)
                clsRegisters.CC |= clsRegisters.CC_Z;
        	if (result > 0xFF)
        		clsRegisters.CC |= clsRegisters.CC_C;
        	if (((oldB ^ value) & 0x80) == 0  &&  ((oldB ^ result) & 0x80) != 0)
        	    clsRegisters.CC |= clsRegisters.CC_V;
        }
        else if(octet1.equals("EB") && (Integer.parseInt(octet2, 16) & 16) == 16)             //ADDB INDEXE INDIRECT ex : ADDB [$1234]
        {
            String adress= getaddress();
            int ca1 = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress, 16), 1).toString(), 16);
            int ca2 = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress, 16) + 1 , 1).toString(), 16);
            int FullAdress = (ca1 << 8 | ca2 ) & 0xFFFF;
            int value = Integer.parseInt(clsRAM.model.getValueAt(FullAdress , 1).toString(), 16);
            int oldB = clsRegisters.B;
            clsRegisters.B = (clsRegisters.B + value) & 0xFF;
        	int result = oldB + value;

            clsRegisters.CC &= ~(clsRegisters.CC_N | clsRegisters.CC_Z | clsRegisters.CC_V | clsRegisters.CC_C);

        	if ((clsRegisters.B & 0x80) != 0)
                clsRegisters.CC |= clsRegisters.CC_N;
        	if (clsRegisters.B == 0)
                clsRegisters.CC |= clsRegisters.CC_Z;
        	if (result > 0xFF)
        		clsRegisters.CC |= clsRegisters.CC_C;
        	if (((oldB ^ value) & 0x80) == 0  &&  ((oldB ^ result) & 0x80) != 0)
        	    clsRegisters.CC |= clsRegisters.CC_V;
        }

        else if(octet1.equals("C3")) //ADDD immediat ex : ADDD #$1234
        {
            if(taille != 3)
            {
                clsErreur.afficherMessage();
                return;
            }
            int value = Integer.parseInt(octet2 + octet3, 16);
            int oldD = clsRegisters.D;
            clsRegisters.D = (clsRegisters.D + value) & 0xFFFF;
            int result = oldD + value;

            clsRegisters.CC &= ~(clsRegisters.CC_N | clsRegisters.CC_Z | clsRegisters.CC_V | clsRegisters.CC_C);

            if ((clsRegisters.D & 0x8000) != 0)
                clsRegisters.CC |= clsRegisters.CC_N;
            if (clsRegisters.D == 0)
                clsRegisters.CC |= clsRegisters.CC_Z;
            if (result > 0xFFFF) 
            	clsRegisters.CC |= clsRegisters.CC_C;
            if (((oldD ^ value) & 0x8000) == 0  && ((oldD ^ result) & 0x8000) != 0)
                clsRegisters.CC |= clsRegisters.CC_V;
            
            
            clsRegisters.A = (clsRegisters.D >> 8) & 0xFF;
            clsRegisters.B = clsRegisters.D & 0xFF;
        }
        else if(octet1.equals("D3")) //ADDD direct ex : ADDD $34
        {
            if(taille != 2)
            {
                clsErreur.afficherMessage();
                return;
            }
            String adress= clsRegisters.DP + octet2;
            int vd1 = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress, 16), 1).toString(), 16);
            int vd2 = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress, 16)+1, 1).toString(), 16);
            String va = String.format("%02X", vd1);
            String vb = String.format("%02X", vd2);
            int value = Integer.parseInt(va + vb, 16);
            int oldD = clsRegisters.D;
            clsRegisters.D = (clsRegisters.D + value) & 0xFFFF;
            int result = oldD + value;

            clsRegisters.CC &= ~(clsRegisters.CC_N | clsRegisters.CC_Z | clsRegisters.CC_V | clsRegisters.CC_C);

            if ((clsRegisters.D & 0x8000) != 0)
                clsRegisters.CC |= clsRegisters.CC_N;
            if (clsRegisters.D == 0)
                clsRegisters.CC |= clsRegisters.CC_Z;
            if (result > 0xFFFF) 
            	clsRegisters.CC |= clsRegisters.CC_C;
            if (((oldD ^ value) & 0x8000) == 0  && ((oldD ^ result) & 0x8000) != 0)
                clsRegisters.CC |= clsRegisters.CC_V;


            clsRegisters.A = (clsRegisters.D >> 8) & 0xFF;
            clsRegisters.B = clsRegisters.D & 0xFF;
        }
        else if(octet1.equals("F3") && octet2.equals("9F")) //ADDD etendu indirect ex : ADDD [$1234]
        {
            if(taille != 4)
            {
                clsErreur.afficherMessage();
                return;
            }
            String adress = octet3 + octet4;
            String adress2 = clsRAM.model.getValueAt(Integer.parseInt(adress, 16), 1).toString();
            adress2 +=clsRAM.model.getValueAt(Integer.parseInt(adress, 16)+1, 1).toString();
            int vd1 = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress2, 16), 1).toString(), 16);
            int vd2 = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress2, 16)+1, 1).toString(), 16);
            String va = String.format("%02X", vd1);
            String vb = String.format("%02X", vd2);
            int value = Integer.parseInt(va + vb, 16);
            int oldD = clsRegisters.D;
            clsRegisters.D = (clsRegisters.D + value) & 0xFFFF;
            int result = oldD + value;

            clsRegisters.CC &= ~(clsRegisters.CC_N | clsRegisters.CC_Z | clsRegisters.CC_V | clsRegisters.CC_C);

            if ((clsRegisters.D & 0x8000) != 0)
                clsRegisters.CC |= clsRegisters.CC_N;
            if (clsRegisters.D == 0)
                clsRegisters.CC |= clsRegisters.CC_Z;
            if (result > 0xFFFF) 
            	clsRegisters.CC |= clsRegisters.CC_C;
            if (((oldD ^ value) & 0x8000) == 0  && ((oldD ^ result) & 0x8000) != 0)
                clsRegisters.CC |= clsRegisters.CC_V;

            clsRegisters.A = (clsRegisters.D >> 8) & 0xFF;
            clsRegisters.B = clsRegisters.D & 0xFF;
        }
        else if(octet1.equals("F3")) //ADDD etendu direct ex : ADDD $1234
        {
            if(taille != 3)
            {
                clsErreur.afficherMessage();
                return;
            }
            String adress = octet2 + octet3;
            int vd1 = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress, 16), 1).toString(), 16);
            int vd2 = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress, 16)+1, 1).toString(), 16);
            String va = String.format("%02X", vd1);
            String vb = String.format("%02X", vd2);
            int value = Integer.parseInt(va + vb, 16);
            int oldD = clsRegisters.D;
            clsRegisters.D = (clsRegisters.D + value) & 0xFFFF;
            int result = oldD + value;

            clsRegisters.CC &= ~(clsRegisters.CC_N | clsRegisters.CC_Z | clsRegisters.CC_V | clsRegisters.CC_C);

            if ((clsRegisters.D & 0x8000) != 0)
                clsRegisters.CC |= clsRegisters.CC_N;
            if (clsRegisters.D == 0)
                clsRegisters.CC |= clsRegisters.CC_Z;
            if (result > 0xFFFF) 
            	clsRegisters.CC |= clsRegisters.CC_C;
            if (((oldD ^ value) & 0x8000) == 0  && ((oldD ^ result) & 0x8000) != 0)
                clsRegisters.CC |= clsRegisters.CC_V;

            clsRegisters.A = (clsRegisters.D >> 8) & 0xFF;
            clsRegisters.B = clsRegisters.D & 0xFF;
        }
        else if(octet1.equals("E3") && (Integer.parseInt(octet2, 16) & 16) != 16) //ADDD indexe direct ex : ADDD ,Y
        {
            String adress = getaddress();
            int vd1 = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress, 16), 1).toString(), 16);
            int vd2 = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress, 16)+1, 1).toString(), 16);
            String va = String.format("%02X", vd1);
            String vb = String.format("%02X", vd2);
            int value = Integer.parseInt(va + vb, 16);
            int oldD = clsRegisters.D;
            clsRegisters.D = (clsRegisters.D + value) & 0xFFFF;
            int result = oldD + value;

            clsRegisters.CC &= ~(clsRegisters.CC_N | clsRegisters.CC_Z | clsRegisters.CC_V | clsRegisters.CC_C);

            if ((clsRegisters.D & 0x8000) != 0)
                clsRegisters.CC |= clsRegisters.CC_N;
            if (clsRegisters.D == 0)
                clsRegisters.CC |= clsRegisters.CC_Z;
            if (result > 0xFFFF) 
            	clsRegisters.CC |= clsRegisters.CC_C;
            if (((oldD ^ value) & 0x8000) == 0  && ((oldD ^ result) & 0x8000) != 0)
                clsRegisters.CC |= clsRegisters.CC_V;

            clsRegisters.A = (clsRegisters.D >> 8) & 0xFF;
            clsRegisters.B = clsRegisters.D & 0xFF;
        }
        else if(octet1.equals("E3") && (Integer.parseInt(octet2, 16) & 16) == 16) //ADDD indexe indirect
        {
            String adress = getaddress();
            String adress2 = clsRAM.model.getValueAt(Integer.parseInt(adress, 16), 1).toString();
            adress2 +=clsRAM.model.getValueAt(Integer.parseInt(adress, 16)+1, 1).toString();
            int vd1 = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress2, 16), 1).toString(), 16);
            int vd2 = Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress2, 16)+1, 1).toString(), 16);
            String va = String.format("%02X", vd1);
            String vb = String.format("%02X", vd2);
            int value = Integer.parseInt(va + vb, 16);
            int oldD = clsRegisters.D;
            clsRegisters.D = (clsRegisters.D + value) & 0xFFFF;
            int result = oldD + value;

            clsRegisters.CC &= ~(clsRegisters.CC_N | clsRegisters.CC_Z | clsRegisters.CC_V | clsRegisters.CC_C);

            if ((clsRegisters.D & 0x8000) != 0)
                clsRegisters.CC |= clsRegisters.CC_N;
            if (clsRegisters.D == 0)
                clsRegisters.CC |= clsRegisters.CC_Z;
            if (result > 0xFFFF) 
            	clsRegisters.CC |= clsRegisters.CC_C;
            if (((oldD ^ value) & 0x8000) == 0  && ((oldD ^ result) & 0x8000) != 0)
                clsRegisters.CC |= clsRegisters.CC_V;

            clsRegisters.A = (clsRegisters.D >> 8) & 0xFF;
            clsRegisters.B = clsRegisters.D & 0xFF;
        }

        else if (octet1.equals("4C"))		//INCA
        {
        	if (taille != 1)
        	{
        		clsErreur.afficherMessage();
        		return;
        	}
        	clsRegisters.A++;
        	
        }
        else if (c.get(0).equals("5C"))		//INCB
        {
        	if (taille != 1)
        	{
        		clsErreur.afficherMessage();
        		return;
        	}
        	clsRegisters.B++;
        	
        }
        else if (octet1.equals("0C"))		// INC DIRECT EX : INC $12 OU INC <$12
        {
        	if (taille != 2)
        	{
        		clsErreur.afficherMessage();
        		return;
        	}
        	String adress = clsRegisters.DP + octet2;
        	int value = (Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress, 16),  1).toString(), 16));
        	value = (value + 1) & 0xFF;  
        	clsRAM.model.setValueAt(value, Integer.parseInt(adress, 16), 1);
        	clsRAM.focusAddress(adress);
        	
        }
        else if (octet1.equals("7C") && octet2.equals("9F"))		// INC ETENDU INDIRECTE EX : INC [$1321]
        {
        	if (taille != 4)
        	{
        		clsErreur.afficherMessage();
        		return;
        	}
        	String adress = octet3 + octet4;
        	int CA1 = (Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress, 16),  1).toString(), 16));
        	int CA2 = (Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress, 16) +1,  1).toString(), 16));
        	int FullAddress = ( CA1 << 8 | CA2 ) & 0xFFFF;
        	int value = Integer.parseInt(clsRAM.model.getValueAt(FullAddress, 1).toString(), 16);
        	value = (value + 1) & 0xFF;  
        	String hexValue = String.format("%02X", value);
        	clsRAM.model.setValueAt(hexValue, FullAddress, 1);
        	clsRAM.focusAddress(clsRegisters.formatTo4Digits(FullAddress));
        }
        else if (octet1.equals("7C"))		// INC ETENDU DIRECT EX : INC $1321 OU INC >$1321
        {
        	if (taille != 3)
        	{
        		clsErreur.afficherMessage();
        		return;
        	}
        	String adress = octet2 + octet3;
        	int value = (Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress, 16),  1).toString(), 16));
        	value = (value + 1) & 0xFF;  
        	clsRAM.model.setValueAt(value, Integer.parseInt(adress, 16), 1);
        	clsRAM.focusAddress(adress);
        }
        else if (octet1.equals("6C") && (Integer.parseInt(octet2, 16) & 16) != 16)		// INC INDEXE DIRECT EX : INC ,U OU INC -3,Y
        {
        	String adress = getaddress();
        	int value = (Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress, 16),  1).toString(), 16));
        	value = (value + 1) & 0xFF;  
        	clsRAM.model.setValueAt(value, Integer.parseInt(adress, 16), 1);
        	clsRAM.focusAddress(adress);
        }
        else if (octet1.equals("6C") && (Integer.parseInt(octet2, 16) & 16) == 16)
        {
        	String adress = getaddress();
        	int CA1 = (Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress, 16),  1).toString(), 16));
        	int CA2 = (Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress, 16) +1,  1).toString(), 16));
        	int FullAddress = ( CA1 << 8 | CA2 ) & 0xFFFF;
        	int value = Integer.parseInt(clsRAM.model.getValueAt(FullAddress, 1).toString(), 16);
        	value = (value + 1) & 0xFF;  
        	String hexValue = String.format("%02X", value);
        	clsRAM.model.setValueAt(hexValue, FullAddress, 1);
        	clsRAM.focusAddress(clsRegisters.formatTo4Digits(FullAddress));
        }

        else if (octet1.equals("4A"))		//DECA
        {
        	if (taille != 1)
        	{
        		clsErreur.afficherMessage();
        		return;
        	}
        	clsRegisters.A--;
        }
        else if (octet1.equals("5A"))		//DECB
        {
        	if (taille != 1)
        	{
        		clsErreur.afficherMessage();
        		return;
        	}
        	clsRegisters.B--;
        }
        else if (octet1.equals("0A"))		// DEC DIRECT EX : DEC $12 OU INC <$12
        {
        	if (taille != 2)
        	{
        		clsErreur.afficherMessage();
        		return;
        	}
        	String adress = clsRegisters.DP + octet2;
        	int value = (Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress, 16),  1).toString(), 16));
        	value = (value - 1) & 0xFF;  
        	clsRAM.model.setValueAt(value, Integer.parseInt(adress, 16), 1);
        	clsRAM.focusAddress(adress);
        }
        else if (octet1.equals("7A") && octet2.equals("9F"))		// DEC ETENDU INDIRECTE EX : DEC [$1321]
        {
        	if (taille != 4)
        	{
        		clsErreur.afficherMessage();
        		return;
        	}
        	String adress = octet3 + octet4;
        	int CA1 = (Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress, 16),  1).toString(), 16));
        	int CA2 = (Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress, 16) +1,  1).toString(), 16));
        	int FullAddress = ( CA1 << 8 | CA2 ) & 0xFFFF;
        	int value = Integer.parseInt(clsRAM.model.getValueAt(FullAddress, 1).toString(), 16);
        	value = (value - 1) & 0xFF;  
        	String hexValue = String.format("%02X", value);
        	clsRAM.model.setValueAt(hexValue, FullAddress, 1);
            clsRAM.focusAddress(clsRegisters.formatTo4Digits(FullAddress));
        }
        else if (octet1.equals("7A"))		// DEC ETENDU DIRECT EX : DEC $1321 OU DEC >$1321
        {
        	if (taille != 3)
        	{
        		clsErreur.afficherMessage();
        		return;
        	}
        	String adress = octet2 + octet3;
        	int value = (Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress, 16),  1).toString(), 16));
        	value = (value - 1) & 0xFF;  
        	clsRAM.model.setValueAt(value, Integer.parseInt(adress, 16), 1);
        	clsRAM.focusAddress(adress);
        }
        else if (octet1.equals("6A") && (Integer.parseInt(octet2, 16) & 16) != 16)		// DEC INDEXE DIRECT EX : DEC ,S OU DEC $10,X
        {
        	String adress = getaddress();
        	int value = (Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress, 16),  1).toString(), 16));
        	value = (value - 1) & 0xFF;  
        	clsRAM.model.setValueAt(value, Integer.parseInt(adress, 16), 1);
        	clsRAM.focusAddress(adress);
        }
        else if (octet1.equals("6A") && (Integer.parseInt(octet2, 16) & 16) == 16)		// DEC INDEXE INDIRECT
        {
        	String adress = getaddress();
        	int CA1 = (Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress, 16),  1).toString(), 16));
        	int CA2 = (Integer.parseInt(clsRAM.model.getValueAt(Integer.parseInt(adress, 16) +1,  1).toString(), 16));
        	int FullAddress = ( CA1 << 8 | CA2 ) & 0xFFFF;
        	int value = Integer.parseInt(clsRAM.model.getValueAt(FullAddress, 1).toString(), 16);
        	value = (value - 1) & 0xFF;  
        	String hexValue = String.format("%02X", value);
        	clsRAM.model.setValueAt(hexValue, FullAddress, 1);
            clsRAM.focusAddress(clsRegisters.formatTo4Digits(FullAddress));
        }

        else if(octet1.equals("1F"))   // TFR X,Y
        {
            if (taille != 2)
        	{
        		clsErreur.afficherMessage();
        		return;
        	}

            String src = "", dest = "";
            for (String r : clsCompiler.code_reg.keySet()) {
 		   if (octet2.startsWith(r)) {
		        src = clsCompiler.code_reg.get(r);
 		       break;
  		  }
		}

		for (String r : clsCompiler.code_reg.keySet()) {
		    if (octet2.endsWith(r)) {
  		      dest = clsCompiler.code_reg.get(r);
  		      break;
  		  }
		}

        int bitsrc = 0;
        if(src.equals("A") || src.equals("B") || src.equals("DP") || src.equals("CC"))
            bitsrc = 8;
        else bitsrc = 16;

        int bitdest = 0;
        if(dest.equals("A") || dest.equals("B") || dest.equals("DP") || dest.equals("CC"))
            bitdest = 8;
        else bitdest = 16;

        if(bitsrc != bitdest)
            {
        		clsErreur.afficherMessage();
        		return;
        	}

        int source = 0;

        if(src.equals("A"))
            source = clsRegisters.A;
        else if(src.equals("B"))
            source = clsRegisters.B;
        else if(src.equals("X"))
            source = clsRegisters.X;
        else if(src.equals("Y"))
            source = clsRegisters.Y;
        else if(src.equals("U"))
            source = clsRegisters.U;
        else if(src.equals("S"))
            source = clsRegisters.S;
        else if(src.equals("DP"))
            source = clsRegisters.DP;
        else if(src.equals("CC"))
            source = clsRegisters.CC;
        else if(src.equals("PC"))
            source = clsRegisters.PC;
        else if(src.equals("D"))
            source = clsRegisters.D;
        

        if(dest.equals("A"))
            clsRegisters.A = source;
        else if(dest.equals("B"))
            clsRegisters.B = source;
        else if(dest.equals("X"))
            clsRegisters.X = source ;
        else if(dest.equals("Y"))
            clsRegisters.Y = source;
        else if(dest.equals("U"))
            clsRegisters.U = source ;
        else if(dest.equals("S"))
            clsRegisters.S = source;
        else if(dest.equals("D"))
            clsRegisters.D = source ;
        else if(dest.equals("PC"))
            clsRegisters.PC = source;
        else if(dest.equals("DP"))
            clsRegisters.DP = source ;
        else if(dest.equals("CC"))
            clsRegisters.CC = source;

    }
        else if(octet1.equals("1E"))   // EXG U,S
        {
            if (taille != 2)
        	{
        		clsErreur.afficherMessage();
        		return;
        	}

            String src = "", dest = "";
            for (String r : clsCompiler.code_reg.keySet()) {
 		   if (octet2.startsWith(r)) {
		        src = clsCompiler.code_reg.get(r);
 		       break;
  		  }
		}

		for (String r : clsCompiler.code_reg.keySet()) {
		    if (octet2.endsWith(r)) {
  		      dest = clsCompiler.code_reg.get(r);
  		      break;
  		  }
		}

        int bitsrc = 0;
        if(src.equals("A") || src.equals("B") || src.equals("DP") || src.equals("CC"))
            bitsrc = 8;
        else bitsrc = 16;

        int bitdest = 0;
        if(dest.equals("A") || dest.equals("B") || dest.equals("DP") || dest.equals("CC"))
            bitdest = 8;
        else bitdest = 16;

        if(bitsrc != bitdest)
            {
        		clsErreur.afficherMessage();
        		return;
        	}

        int temp = 0;
        int temp2 = 0;

        if(src.equals("A"))
            temp = clsRegisters.A;
        else if(src.equals("B"))
            temp = clsRegisters.B;
        else if(src.equals("X"))
            temp = clsRegisters.X;
        else if(src.equals("Y"))
            temp = clsRegisters.Y;
        else if(src.equals("U"))
            temp = clsRegisters.U;
        else if(src.equals("S"))
            temp = clsRegisters.S;
        else if(src.equals("DP"))
            temp = clsRegisters.DP;
        else if(src.equals("CC"))
            temp = clsRegisters.CC;
        else if(src.equals("PC"))
            temp = clsRegisters.PC;
        else if(src.equals("D"))
            temp = clsRegisters.D;
        

        if(dest.equals("A"))
            {
                temp2 = clsRegisters.A;
                clsRegisters.A = temp;
            }
        else if(dest.equals("B"))
           {
                temp2 = clsRegisters.B;
                clsRegisters.B = temp;
            }
        else if(dest.equals("X"))
            {
                temp2 = clsRegisters.X;
                clsRegisters.X = temp;
            }
        else if(dest.equals("Y"))
            {
                temp2 = clsRegisters.Y;
                clsRegisters.Y = temp;
            }
        else if(dest.equals("U"))
            {
                temp2 = clsRegisters.U;
                clsRegisters.U = temp;
            }
        else if(dest.equals("S"))
            {
                temp2 = clsRegisters.S;
                clsRegisters.S = temp;
            }
        else if(dest.equals("D"))
            {
                temp2 = clsRegisters.D;
                clsRegisters.D = temp;
            }
        else if(dest.equals("PC"))
            {
                temp2 = clsRegisters.PC;
                if(temp < 0xFC00)
                    {clsErreur.afficherMessage();return;}
                clsRegisters.PC = temp;
            }
        else if(dest.equals("DP"))
            {
                temp2 = clsRegisters.DP;
                clsRegisters.DP = temp;
            }
        else if(dest.equals("CC"))
            {
                temp2 = clsRegisters.CC;
                clsRegisters.CC = temp;
            }
            if(src.equals("A"))
            clsRegisters.A = temp2;
        else if(src.equals("B"))
            clsRegisters.B = temp2;
        else if(src.equals("X"))
            clsRegisters.X = temp2;
        else if(src.equals("Y"))
            clsRegisters.Y = temp2;
        else if(src.equals("U"))
            clsRegisters.U = temp2;
        else if(src.equals("S"))
            clsRegisters.S = temp2;
        else if(src.equals("DP"))
            clsRegisters.DP = temp2;
        else if(src.equals("CC"))
            clsRegisters.CC = temp2;
        else if(src.equals("PC"))
            {
                if(temp2 < 0xFC00)
                    {clsErreur.afficherMessage();return;}
            clsRegisters.PC = temp2;
            }
        else if(src.equals("D"))
            clsRegisters.D = temp2;

    }

        else if(octet1.equals("34"))   // PSHS A,B,X
        {
            int vl = parseInt(octet2, 16);
            if((vl & 128) == 128)
            {
                clsRegisters.S--;
                int octethaut = (clsRegisters.PC >> 8) & 0xFF;
                int octetbas = clsRegisters.PC & 0xFF;
                clsRAM.model.setValueAt(clsRegisters.formatTo2Digits(octetbas), clsRegisters.S, 1);
                clsRAM.model.setValueAt(clsRegisters.formatTo2Digits(octethaut), --clsRegisters.S, 1);
                clsRAM.focusAddress(clsRegisters.formatTo4Digits(clsRegisters.S));
            }
            if((vl & 64) == 64)
            {
                clsRegisters.S--;
                int octethaut = (clsRegisters.U >> 8) & 0xFF;
                int octetbas = clsRegisters.U & 0xFF;
                clsRAM.model.setValueAt(clsRegisters.formatTo2Digits(octetbas), clsRegisters.S, 1);
                clsRAM.model.setValueAt(clsRegisters.formatTo2Digits(octethaut), --clsRegisters.S, 1);
				clsRAM.focusAddress(clsRegisters.formatTo4Digits(clsRegisters.S));
            }
            if((vl & 32) == 32)
            {
                clsRegisters.S--;
                int octethaut = (clsRegisters.Y >> 8) & 0xFF;
                int octetbas = clsRegisters.Y & 0xFF;
                clsRAM.model.setValueAt(clsRegisters.formatTo2Digits(octetbas), clsRegisters.S, 1);
                clsRAM.model.setValueAt(clsRegisters.formatTo2Digits(octethaut), --clsRegisters.S, 1);
				clsRAM.focusAddress(clsRegisters.formatTo4Digits(clsRegisters.S));
            }
            if((vl & 16) == 16)
            {
                clsRegisters.S--;
                int octethaut = (clsRegisters.X >> 8) & 0xFF;
                int octetbas = clsRegisters.X & 0xFF;
                clsRAM.model.setValueAt(clsRegisters.formatTo2Digits(octetbas), clsRegisters.S, 1);
                clsRAM.model.setValueAt(clsRegisters.formatTo2Digits(octethaut), --clsRegisters.S, 1);
				clsRAM.focusAddress(clsRegisters.formatTo4Digits(clsRegisters.S));
            }
            if((vl & 8) == 8)
            {
                clsRegisters.S--;
                clsRAM.model.setValueAt(clsRegisters.formatTo2Digits(clsRegisters.DP), clsRegisters.S, 1);
				clsRAM.focusAddress(clsRegisters.formatTo4Digits(clsRegisters.S));
            }
            if((vl & 4) == 4)
            {
                clsRegisters.S--;
                clsRAM.model.setValueAt(clsRegisters.formatTo2Digits(clsRegisters.B), clsRegisters.S, 1);
				clsRAM.focusAddress(clsRegisters.formatTo4Digits(clsRegisters.S));
            }
            if((vl & 2) == 2)
            {
                clsRegisters.S--;
                clsRAM.model.setValueAt(clsRegisters.formatTo2Digits(clsRegisters.A), clsRegisters.S, 1);
				clsRAM.focusAddress(clsRegisters.formatTo4Digits(clsRegisters.S));
            }
            if((vl & 1) == 1)
            {
                clsRegisters.S--;
                clsRAM.model.setValueAt(clsRegisters.formatTo2Digits(clsRegisters.CC), clsRegisters.S, 1);
				clsRAM.focusAddress(clsRegisters.formatTo4Digits(clsRegisters.S));
            }
        }
        else if(octet1.equals("36"))   // PSHU A,B,X
        {
            int vl = parseInt(octet2, 16);
            if((vl & 128) == 128)
            {
                clsRegisters.U--;
                int octethaut = (clsRegisters.PC >> 8) & 0xFF;
                int octetbas = clsRegisters.PC & 0xFF;
                clsRAM.model.setValueAt(clsRegisters.formatTo2Digits(octetbas), clsRegisters.U, 1);
                clsRAM.model.setValueAt(clsRegisters.formatTo2Digits(octethaut), --clsRegisters.U, 1);
                clsRAM.focusAddress(clsRegisters.formatTo4Digits(clsRegisters.U));
            }
            if((vl & 64) == 64)
            {
                clsRegisters.U--;
                int octethaut = (clsRegisters.S >> 8) & 0xFF;
                int octetbas = clsRegisters.S & 0xFF;
                clsRAM.model.setValueAt(clsRegisters.formatTo2Digits(octetbas), clsRegisters.U, 1);
                clsRAM.model.setValueAt(clsRegisters.formatTo2Digits(octethaut), --clsRegisters.U, 1);
				clsRAM.focusAddress(clsRegisters.formatTo4Digits(clsRegisters.U));
            }
            if((vl & 32) == 32)
            {
                clsRegisters.U--;
                int octethaut = (clsRegisters.Y >> 8) & 0xFF;
                int octetbas = clsRegisters.Y & 0xFF;
                clsRAM.model.setValueAt(clsRegisters.formatTo2Digits(octetbas), clsRegisters.U, 1);
                clsRAM.model.setValueAt(clsRegisters.formatTo2Digits(octethaut), --clsRegisters.U, 1);
				clsRAM.focusAddress(clsRegisters.formatTo4Digits(clsRegisters.U));
            }
            if((vl & 16) == 16)
            {
                clsRegisters.U--;
                int octethaut = (clsRegisters.X >> 8) & 0xFF;
                int octetbas = clsRegisters.X & 0xFF;
                clsRAM.model.setValueAt(clsRegisters.formatTo2Digits(octetbas), clsRegisters.U, 1);
                clsRAM.model.setValueAt(clsRegisters.formatTo2Digits(octethaut), --clsRegisters.U, 1);
				clsRAM.focusAddress(clsRegisters.formatTo4Digits(clsRegisters.U));
            }
            if((vl & 8) == 8)
            {
                clsRegisters.U--;
                clsRAM.model.setValueAt(clsRegisters.formatTo2Digits(clsRegisters.DP), clsRegisters.U, 1);
				clsRAM.focusAddress(clsRegisters.formatTo4Digits(clsRegisters.U));
            }
            if((vl & 4) == 4)
            {
                clsRegisters.U--;
                clsRAM.model.setValueAt(clsRegisters.formatTo2Digits(clsRegisters.B), clsRegisters.U, 1);
				clsRAM.focusAddress(clsRegisters.formatTo4Digits(clsRegisters.U));
            }
            if((vl & 2) == 2)
            {
                clsRegisters.U--;
                clsRAM.model.setValueAt(clsRegisters.formatTo2Digits(clsRegisters.A), clsRegisters.U, 1);
				clsRAM.focusAddress(clsRegisters.formatTo4Digits(clsRegisters.U));
            }
            if((vl & 1) == 1)
            {
                clsRegisters.U--;
                clsRAM.model.setValueAt(clsRegisters.formatTo2Digits(clsRegisters.CC), clsRegisters.U, 1);
				clsRAM.focusAddress(clsRegisters.formatTo4Digits(clsRegisters.U));
            }
        }

        else if(octet1.equals("35"))   // PULS A,B,X
        {
            int vl = parseInt(octet2, 16);
            if((vl & 128) == 128)
            {
				clsRAM.focusAddress(clsRegisters.formatTo4Digits(clsRegisters.S));
                int octethaut = Integer.parseInt(clsRAM.model.getValueAt(clsRegisters.S, 1).toString(), 16);
                int octetbas = Integer.parseInt(clsRAM.model.getValueAt(++clsRegisters.S, 1).toString(), 16);
                clsRegisters.PC = Integer.parseInt(clsRegisters.formatTo2Digits(octethaut) + clsRegisters.formatTo2Digits(octetbas), 16);
                clsRegisters.S++;
            }
            if((vl & 64) == 64)
            {
				clsRAM.focusAddress(clsRegisters.formatTo4Digits(clsRegisters.S));
                int octethaut = Integer.parseInt(clsRAM.model.getValueAt(clsRegisters.S, 1).toString(), 16);
                int octetbas = Integer.parseInt(clsRAM.model.getValueAt(++clsRegisters.S, 1).toString(), 16);
                clsRegisters.U = Integer.parseInt(clsRegisters.formatTo2Digits(octethaut) + clsRegisters.formatTo2Digits(octetbas), 16);
                clsRegisters.S++;
            }
            if((vl & 32) == 32)
            {
				clsRAM.focusAddress(clsRegisters.formatTo4Digits(clsRegisters.S));
                int octethaut = Integer.parseInt(clsRAM.model.getValueAt(clsRegisters.S, 1).toString(), 16);
                int octetbas = Integer.parseInt(clsRAM.model.getValueAt(++clsRegisters.S, 1).toString(), 16);
                clsRegisters.Y = Integer.parseInt(clsRegisters.formatTo2Digits(octethaut) + clsRegisters.formatTo2Digits(octetbas), 16);
                clsRegisters.S++;
            }
            if((vl & 16) == 16)
            {
				clsRAM.focusAddress(clsRegisters.formatTo4Digits(clsRegisters.S));
                int octethaut = Integer.parseInt(clsRAM.model.getValueAt(clsRegisters.S, 1).toString(), 16);
                int octetbas = Integer.parseInt(clsRAM.model.getValueAt(++clsRegisters.S, 1).toString(), 16);
                clsRegisters.X = Integer.parseInt(clsRegisters.formatTo2Digits(octethaut) + clsRegisters.formatTo2Digits(octetbas), 16);
                clsRegisters.S++;
            }
            if((vl & 8) == 8)
            {
				clsRAM.focusAddress(clsRegisters.formatTo4Digits(clsRegisters.S));
                clsRegisters.DP = Integer.parseInt(clsRAM.model.getValueAt(clsRegisters.S, 1).toString(), 16);
                clsRegisters.S++;
            }
            if((vl & 4) == 4)
            {
				clsRAM.focusAddress(clsRegisters.formatTo4Digits(clsRegisters.S));
                clsRegisters.B = Integer.parseInt(clsRAM.model.getValueAt(clsRegisters.S, 1).toString(), 16);
                clsRegisters.S++;
            }
            if((vl & 2) == 2)
            {
				clsRAM.focusAddress(clsRegisters.formatTo4Digits(clsRegisters.S));
                clsRegisters.A = Integer.parseInt(clsRAM.model.getValueAt(clsRegisters.S, 1).toString(), 16);
                clsRegisters.S++;
            }
            if((vl & 1) == 1)
            {
				clsRAM.focusAddress(clsRegisters.formatTo4Digits(clsRegisters.S));
                clsRegisters.CC = Integer.parseInt(clsRAM.model.getValueAt(clsRegisters.S, 1).toString(), 16);
                clsRegisters.S++;
            }
        }
        else if(octet1.equals("37"))   // PULU A,B,X
        {
            int vl = parseInt(octet2, 16);
            if((vl & 128) == 128)
            {
				clsRAM.focusAddress(clsRegisters.formatTo4Digits(clsRegisters.U));
                int octethaut = Integer.parseInt(clsRAM.model.getValueAt(clsRegisters.U, 1).toString(), 16);
                int octetbas = Integer.parseInt(clsRAM.model.getValueAt(++clsRegisters.U, 1).toString(), 16);
                clsRegisters.PC = Integer.parseInt(clsRegisters.formatTo2Digits(octethaut) + clsRegisters.formatTo2Digits(octetbas), 16);
                clsRegisters.U++;
            }
            if((vl & 64) == 64)
            {
				clsRAM.focusAddress(clsRegisters.formatTo4Digits(clsRegisters.U));
                int octethaut = Integer.parseInt(clsRAM.model.getValueAt(clsRegisters.U, 1).toString(), 16);
                int octetbas = Integer.parseInt(clsRAM.model.getValueAt(++clsRegisters.U, 1).toString(), 16);
                clsRegisters.S = Integer.parseInt(clsRegisters.formatTo2Digits(octethaut) + clsRegisters.formatTo2Digits(octetbas), 16);
                clsRegisters.U++;
            }
            if((vl & 32) == 32)
            {
				clsRAM.focusAddress(clsRegisters.formatTo4Digits(clsRegisters.U));
                int octethaut = Integer.parseInt(clsRAM.model.getValueAt(clsRegisters.U, 1).toString(), 16);
                int octetbas = Integer.parseInt(clsRAM.model.getValueAt(++clsRegisters.U, 1).toString(), 16);
                clsRegisters.Y = Integer.parseInt(clsRegisters.formatTo2Digits(octethaut) + clsRegisters.formatTo2Digits(octetbas), 16);
                clsRegisters.U++;
            }
            if((vl & 16) == 16)
            {
				clsRAM.focusAddress(clsRegisters.formatTo4Digits(clsRegisters.U));
                int octethaut = Integer.parseInt(clsRAM.model.getValueAt(clsRegisters.U, 1).toString(), 16);
                int octetbas = Integer.parseInt(clsRAM.model.getValueAt(++clsRegisters.U, 1).toString(), 16);
                clsRegisters.X = Integer.parseInt(clsRegisters.formatTo2Digits(octethaut) + clsRegisters.formatTo2Digits(octetbas), 16);
                clsRegisters.U++;
            }
            if((vl & 8) == 8)
            {
				clsRAM.focusAddress(clsRegisters.formatTo4Digits(clsRegisters.U));
                clsRegisters.DP = Integer.parseInt(clsRAM.model.getValueAt(clsRegisters.U, 1).toString(), 16);
                clsRegisters.U++;
            }
            if((vl & 4) == 4)
            {
				clsRAM.focusAddress(clsRegisters.formatTo4Digits(clsRegisters.U));
                clsRegisters.B = Integer.parseInt(clsRAM.model.getValueAt(clsRegisters.U, 1).toString(), 16);
                clsRegisters.U++;
            }
            if((vl & 2) == 2)
            {
				clsRAM.focusAddress(clsRegisters.formatTo4Digits(clsRegisters.U));
                clsRegisters.A = Integer.parseInt(clsRAM.model.getValueAt(clsRegisters.U, 1).toString(), 16);
                clsRegisters.U++;
            }
            if((vl & 1) == 1)
            {
				clsRAM.focusAddress(clsRegisters.formatTo4Digits(clsRegisters.U));
                clsRegisters.CC = Integer.parseInt(clsRAM.model.getValueAt(clsRegisters.U, 1).toString(), 16);
                clsRegisters.U++;
            }
        }

        else
        {
            clsErreur.afficherMessage();
            return;
        }


        clsRegisters.assignFlag();
        setpc();
        updateregisters();
}

public static String hexToBinary(String hex) {
    StringBuilder binary = new StringBuilder();
    for (char c : hex.toCharArray()) {
        int value = Integer.parseInt(String.valueOf(c), 16);
        String bin = Integer.toBinaryString(value);
        // Pad with leading zeros to 4 bits
        while (bin.length() < 4) {
            bin = "0" + bin;
        }
        binary.append(bin);
    }
    return binary.toString();
}
    
}

