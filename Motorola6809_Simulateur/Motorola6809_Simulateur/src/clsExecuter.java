public class clsExecuter {

    public static void executer(){

        if(clsCompiler.debug == false)
            {
                clsErreur.afficherMessage("Compiler le code avant d'exécuter.");
            }
            else{
                for(int i = 0; i < clsCompiler.getLinesFromTextField(clsMoto6809.txtEditeur).size(); i++)
                    clsPasàpas.pasapas();
            }
    }

}
