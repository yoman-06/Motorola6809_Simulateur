public class clsAdressingModes {

    public static enum AdressingMode {
        Immediat,
        Direct,
        EtenduDirect,
        EtenduIndirect,
        IndexeDirect,
        IndexeIndirect,
        INHERENT
    };

    public static enum deplacement{
        nul_inc_dec,
        _4bit,
        _7bit,
        _15bit,
        pc
    };
    
}
