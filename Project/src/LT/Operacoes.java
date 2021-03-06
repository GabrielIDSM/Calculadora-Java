package LT;
/*
Essa classe deve ser capaz de usar a classe funcionamento para retornar 
a interface o resultado da expressão informada.
A classe funcionamento é capaz de resolver qualquer expressão númerica e retornar seu valor.
A Classe Operacoes deve ser capaz de organizar as expressão que contém parentêsis em outras
mais simples.
*/
public class Operacoes {

    //Atributos IN
    private String operacoes; //Uma string com todas as operações
    private char[] char_operacoes; //Um array com os chars da String
    private boolean validade = true;
    private boolean parentesis = false;
    private boolean sit = true;
    private boolean dep_mod = false;
    private int cont_parentesis_AB = 0;
    private int cont_parentesis_FE = 0;

    //FIM
    //Método construtor IN
    public Operacoes(String operacoes) {
        //Parentêsis automáticos IN
        this.operacoes = operacoes;
        this.char_operacoes = this.operacoes.toCharArray();
        if(getdep_mod()) System.out.println("OPERACOES (CONS) : "+this.operacoes);
    }

    //FIM
    //Getters IN
    public boolean getvalidade() {
        return validade;
    }

    public boolean getparentesis() {
        return parentesis;
    }

    public boolean getsit() {
        return sit;
    }

    public boolean getdep_mod(){
        return dep_mod;
    }
    //FIM
    //Setters IN
    private void setvalidade(boolean validade) {
        this.validade = validade;
    }

    private void setparentesis(boolean parentesis) {
        this.parentesis = parentesis;
    }

    private void setsit(boolean sit) {
        this.sit = sit;
    }
    
    public void setdep_mod(boolean dep_mod){
        this.dep_mod = dep_mod;
    }
    //FIM
    //Métodos IN
    public float main() {
        float total = 0;
        try {
            verifica_parentesis();
            if(getdep_mod()) System.out.println("getvalidade() == "+getvalidade());
            if (getvalidade()) {
                if(getdep_mod()) System.out.println("getvalidade() == "+getvalidade());
                if (getparentesis()) {
                    verifica();
                    cont_par();
                    if(getdep_mod()) System.out.println("getparentesis() == true");
                    if (getvalidade()) {
                        situacao();
                        if(getdep_mod()) System.out.println("getvalidade() == true");
                        if (getvalidade()) {
                            if(getdep_mod()) System.out.println("getvalidade() == true POS situacao()");
                            if(getdep_mod()) System.out.println("getsit() == " + getsit());
                            if (getsit()) {
                                try {
                                    String operacoes_f = cria_operacoes_s_par();
                                    if(getdep_mod()) System.out.println(operacoes_f);
                                    //cria uma string sem parentesis
                                    String[] array_par = cria_array_par();
                                    //cria um array com o conteudo dos parentêsis
                                    float[] array_float_par = cria_array_float_par(array_par);
                                    //Cria um array com o resultado das operacoes entre parentêsis
                                    operacoes_f = substitui_operacoes_s_par(operacoes_f, array_float_par);
                                    if(getdep_mod()) System.out.println(operacoes_f);
                                    //Substitui o array final pelos valores do parentêsis   
                                    if(getdep_mod()) System.out.println("Aqui IN");
                                    operacoes_f = duplo_sinal(operacoes_f);
                                    if(getdep_mod()) System.out.println("Aqui FIM");
                                    if(getdep_mod()) System.out.println("OPERACOES_F"+operacoes_f);
                                    //Verificação e substituição do duplo sinal IN
                                    Funcionamento F = new Funcionamento(operacoes_f);
                                    float T = F.main();
                                    if(getdep_mod()) System.out.println(T);
                                    if (F.getvalidade()) {
                                        total = T;
                                    } else {
                                        setvalidade(false);
                                    }
                                } catch (Exception e) {
                                    total = 0;
                                    return total;
                                }
                            } else {
                                if(getdep_mod()) System.out.println("Na funcao main : " + operacoes);
                                try {
                                    total = par_encadeados_sit(operacoes);
                                } catch (Exception e) {
                                    setvalidade(false);
                                }
                                if (total == Float.NaN) {
                                    setvalidade(false);
                                }
                                return total;
                            }
                        } else {
                            return total;
                        }
                    } else {
                        return total;
                    }
                } else {
                    Funcionamento F = new Funcionamento(this.operacoes);
                    total = F.main();
                    setvalidade(F.getvalidade());
                    if (total == Float.NaN) {
                        setvalidade(false);
                    }
                    return total;
                }
            } else {
                return total;
            }
        } catch (Exception e) {
            setvalidade(false);
        }
        return total;
    }

    private void verifica_parentesis() {
        for (int i = 0; i < char_operacoes.length; i++) {
            if (char_operacoes[i] == '(' || char_operacoes[i] == ')') {
                setparentesis(true);
                break;
            }
        }
    }

    private void cont_par() {
        if (getparentesis()) {
            for (int i = 0; i < char_operacoes.length; i++) {
                if (char_operacoes[i] == '(') {
                    cont_parentesis_AB++;
                } else if (char_operacoes[i] == ')') {
                    cont_parentesis_FE++;
                }
            }
            if (cont_parentesis_AB != cont_parentesis_FE) {
                setvalidade(false);
            }
        }
    }

    private void situacao() {
        char auxc = '(';
        int cont = 0;
        for (int i = 0; i < char_operacoes.length; i++) {
            if (cont == 0 && (char_operacoes[i] == '(' || char_operacoes[i] == ')')) {
                auxc = char_operacoes[i];
                cont++;
            } else {
                if (auxc == char_operacoes[i]) {
                    setsit(false);
                    break;
                } else {
                    if (auxc == '(' && char_operacoes[i] == ')') {
                        auxc = char_operacoes[i];
                    } else if (auxc == ')' && char_operacoes[i] == '(') {
                        auxc = char_operacoes[i];
                    }
                }
            }
        }
    }

    private String cria_operacoes_s_par() {
        String operacoes_f = "";
        int i = 0;
        while (i < char_operacoes.length) {
            if (char_operacoes[i] == '(' || char_operacoes[i] == ')') {
                if(getdep_mod()) System.out.println("char_operacoes[i] = '(' ou ')' i : " + char_operacoes[i]);
                i++;
                operacoes_f += "P";
                while (true) {
                    if (char_operacoes[i] == '(' || char_operacoes[i] == ')') {
                        break;
                    }
                    if(getdep_mod()) System.out.println("char_operacoes[i] != '(' ou ')' i : " + char_operacoes[i]);
                    i++;
                    if (i >= char_operacoes.length) {
                        break;
                    }
                }
                i++;

            } else {
                operacoes_f += char_operacoes[i];
                i++;
            }
        }
        return operacoes_f;
    }

    private String[] cria_array_par() {
        String[] array_par = new String[cont_parentesis_AB];
        int i = 0, index = 0;
        String aux = "";
        while (i < char_operacoes.length) {
            if (char_operacoes[i] == '(' || char_operacoes[i] == ')') {
                i++;
                while (true) {
                    if (char_operacoes[i] == '(' || char_operacoes[i] == ')') {
                        break;
                    }
                    aux += char_operacoes[i];
                    i++;
                    if (i >= char_operacoes.length) {
                        break;
                    }
                }
                i++;
                array_par[index] = aux;
                index++;
                aux = "";
            } else {
                i++;
            }
        }
        return array_par;
    }

    private float[] cria_array_float_par(String[] operacoes_par) {
        float[] array_float_par = new float[operacoes_par.length];
        Funcionamento F;
        for (int i = 0; i < array_float_par.length; i++) {
            F = new Funcionamento(operacoes_par[i]);
            array_float_par[i] = F.main();
            F = null;
        }
        return array_float_par;
    }

    private String substitui_operacoes_s_par(String operacoes_s_par, float[] array_float_par) {
        String aux = "";
        char[] aux_operacoes = operacoes_s_par.toCharArray();
        int index = 0;
        for (int i = 0; i < aux_operacoes.length; i++) {
            if (aux_operacoes[i] == 'P') {
                aux += array_float_par[index];
                index++;
            } else {
                aux += aux_operacoes[i];
            }
        }
        return aux;
    }

    private boolean verifica_par(String string) {
        boolean par = false;
        char[] char_string = string.toCharArray();
        for (int i = 0; i < char_string.length; i++) {
            if (char_string[i] == '(' || char_string[i] == ')') {
                par = true;
                break;
            }
        }
        return par;
    }

    private float calcula_operacao(String string) {
        float total = 0;
        try {
            Funcionamento F = new Funcionamento(string);
            total = F.main();
            if(F.getvalidade() == false) setvalidade(false);
        } catch (Exception e) {
            setvalidade(false);
        }
        return total;
    }

    private float par_encadeados_sit(String ope) {
        float resultado = 0, aux_r = 0;
        int i = 0, f = 0, l = 0, k = 0;
        String aux = "", str_aux = "";
        char[] char_ope;
        //Substituicao de parentesis com um único número IN
        ope = remove_par_desnecessarios(ope);
        //FIM
        //Verificação e substituição do duplo sinal IN
        ope = duplo_sinal(ope);
        char_ope = ope.toCharArray();
        //FIM
        while (verifica_par(ope)) {
            if(getdep_mod()) System.out.println("Comecou : while (verifica_par(ope))");
            while (i < char_ope.length) {
                if (char_ope[i] == '(') {
                    f = i;
                    f++;
                    if(getdep_mod()) System.out.println("ENTROU : char_ope[i] == '('");
                    while (f < char_ope.length) {
                        if (char_ope[f] == '(') {
                            break;
                        }
                        if (char_ope[f] == ')') {
                            if(getdep_mod()) System.out.println("ENTROU: char_ope[f] == ')'");
                            char_ope[i] = 'A';
                            char_ope[f] = 'A';
                            l = i;
                            l++;
                            while (l < f) {
                                aux += char_ope[l];
                                l++;
                            }
                            aux_r = calcula_operacao(aux);
                            k = 0;
                            if(getdep_mod()) System.out.print("char_ope com A : ");
                            if(getdep_mod()) print_char_array(char_ope);
                            while (k < char_ope.length) {
                                if (char_ope[k] == 'A') {
                                    str_aux += Float.toString(aux_r);
                                    k++;
                                    while (char_ope[k] != 'A') {
                                        if (k >= char_ope.length) {
                                            break;
                                        }
                                        k++;
                                    }
                                    if (k >= char_ope.length) {
                                        break;
                                    }
                                    k++;
                                } else {
                                    str_aux += char_ope[k];
                                    k++;
                                }
                            }
                            k = 0;
                            //Verificação e substituição do duplo sinal IN
                            str_aux = duplo_sinal(str_aux);
                            ope = str_aux;
                            ope = remove_par_desnecessarios(ope);
                            if(getdep_mod()) System.out.print("char_ope antigo : ");
                            if(getdep_mod()) print_char_array(char_ope);
                            char_ope = ope.toCharArray();
                            if(getdep_mod()) System.out.print("char_ope novo : ");
                            if(getdep_mod()) print_char_array(char_ope);
                            str_aux = "";
                            break;
                        }
                        f++;
                        l = 0;
                    }
                    aux = "";
                    f = 0;
                }
                i++;
            }
            i = 0;
            if(getdep_mod()) System.out.println(ope);
            //Verificação e substituição do duplo sinal IN
            ope = duplo_sinal(ope);
            char_ope = ope.toCharArray();
            //FIM
        }
        resultado = calcula_operacao(ope);
        return resultado;
    }

    private void verifica() {
        int cont = 1;
        if (char_operacoes[0] == ')' || char_operacoes[(char_operacoes.length - 1)] == '(') {
            setvalidade(false);
            return;
        }
        if (char_operacoes[0] == '/'
                || char_operacoes[0] == '*'
                || char_operacoes[0] == '.'
                || char_operacoes[0] == 'R'
                || char_operacoes[0] == 'p') {
            setvalidade(false);
            return;
        }
        if (char_operacoes[(char_operacoes.length - 1)] == '/'
                || char_operacoes[(char_operacoes.length - 1)] == '*'
                || char_operacoes[(char_operacoes.length - 1)] == 'r'
                || char_operacoes[(char_operacoes.length - 1)] == '.'
                || char_operacoes[(char_operacoes.length - 1)] == 'R'
                || char_operacoes[(char_operacoes.length - 1)] == 'p'
                || char_operacoes[(char_operacoes.length - 1)] == 'L'
                || char_operacoes[(char_operacoes.length - 1)] == 'l'
                || char_operacoes[(char_operacoes.length - 1)] == 'T'
                || char_operacoes[(char_operacoes.length - 1)] == 'M'
                || char_operacoes[(char_operacoes.length - 1)] == 'J'
                || char_operacoes[(char_operacoes.length - 1)] == 'Q'
                || char_operacoes[(char_operacoes.length - 1)] == 'W'
                || char_operacoes[(char_operacoes.length - 1)] == 'K'
                || char_operacoes[(char_operacoes.length - 1)] == 'H'
                || char_operacoes[(char_operacoes.length - 1)] == 'k'
                || char_operacoes[(char_operacoes.length - 1)] == 'h') {
            setvalidade(false);
            return;
        }
        while (cont < (char_operacoes.length - 1)) {
            if (char_operacoes[cont] == '(' && !(char_operacoes[cont - 1] == '+'
                    || char_operacoes[cont - 1] == '-'
                    || char_operacoes[cont - 1] == '*'
                    || char_operacoes[cont - 1] == '/'
                    || char_operacoes[cont - 1] == 'r'
                    || char_operacoes[cont - 1] == '^'
                    || char_operacoes[cont - 1] == 'R'
                    || char_operacoes[cont - 1] == 'p'
                    || char_operacoes[cont - 1] == '('
                    || char_operacoes[cont - 1] == 'L'
                    || char_operacoes[cont - 1] == 'l'
                    || char_operacoes[cont - 1] == 'T'
                    || char_operacoes[cont - 1] == 'M'
                    || char_operacoes[cont - 1] == 'J'
                    || char_operacoes[cont - 1] == ','
                    || char_operacoes[cont - 1] == 'Q'
                    || char_operacoes[cont - 1] == 'W'
                    || char_operacoes[cont - 1] == 'K'
                    || char_operacoes[cont - 1] == 'H'
                    || char_operacoes[cont - 1] == 'k'
                    || char_operacoes[cont - 1] == 'h')) {
                setvalidade(false);
                break;
            } else if (char_operacoes[cont] == ')' && !(char_operacoes[cont + 1] == '+'
                    || char_operacoes[cont + 1] == '-'
                    || char_operacoes[cont + 1] == '*'
                    || char_operacoes[cont + 1] == '/'
                    || char_operacoes[cont + 1] == '^'
                    || char_operacoes[cont + 1] == 'R'
                    || char_operacoes[cont + 1] == 'p'
                    || char_operacoes[cont + 1] == ')'
                    || char_operacoes[cont + 1] == '!'
                    || char_operacoes[cont + 1] == ',')) {
                setvalidade(false);
                break;
            }
            cont++;
        }
    }

    private void print_char_array(char[] ca) {
        for (int i = 0; i < ca.length; i++) {
            System.out.print(ca[i]);
        }
        System.out.println();
    }

    private String duplo_sinal(String string) {
        String string_f = "";
        int i = 1;
        char[] char_array = string.toCharArray();
        while (i < char_array.length) {
            if (char_array[i] == '-' && char_array[i - 1] == '+') {
                char_array[i - 1] = 'S';
            }
            if (char_array[i] == '+' && char_array[i - 1] == '-') {
                char_array[i] = 'S';
            }
            if (char_array[i] == '-' && char_array[i - 1] == '-') {
                char_array[i - 1] = '+';
                char_array[i] = 'S';
            }
            if (char_array[i] == '+' && char_array[i - 1] == '+') {
                char_array[i] = 'S';
            }
            i++;
        }
        i = 0;
        while (i < char_array.length) {
            if (char_array[i] == 'S') {
                i++;
                continue;
            } else {
                string_f += char_array[i];
            }
            i++;
        }
        return string_f;
    }

    private String remove_par_desnecessarios(String string) {
        if(getdep_mod()) System.out.println("ENTROU: remove_par_desnecessarios");
        int i = 0, f = 0, k = 0;
        String str_aux = "";
        char[] char_string = string.toCharArray();
        //Substituicao de parentesis com um único número IN
        while (i < char_string.length) {
            if (char_string[i] == '(') {
                f = i;
                f++;
                while (f < char_string.length) {
                    if (char_string[f] == '('
                            || char_string[f] == '+'
                            || char_string[f] == '-'
                            || char_string[f] == '*'
                            || char_string[f] == '/'
                            || char_string[f] == 'r'
                            || char_string[f] == '^'
                            || char_string[f] == 'R'
                            || char_string[f] == 'p'
                            || char_string[f] == 'L'
                            || char_string[f] == 'l'
                            || char_string[f] == 'T'
                            || char_string[f] == 'M'
                            || char_string[f] == 'J'
                            || char_string[f] == 'Q'
                            || char_string[f] == 'W'
                            || char_string[f] == 'K'
                            || char_string[f] == 'H'
                            || char_string[f] == 'k'
                            || char_string[f] == 'h') {
                        break;
                    }
                    if (char_string[f] == ')') {
                        if(getdep_mod()) print_char_array(char_string);
                        char_string[i] = 'A';
                        char_string[f] = 'A';
                        if(getdep_mod()) print_char_array(char_string);
                        k = 0;
                        while (k < char_string.length) {
                            if (char_string[k] == 'A') {
                                k++;
                            } else {
                                str_aux += char_string[k];
                                k++;
                            }
                        }
                        string = str_aux;
                        char_string = null;
                        char_string = string.toCharArray();
                        if(getdep_mod()) print_char_array(char_string);
                        str_aux = "";
                        k = 0;
                        System.out.println(string);
                        i = 0;
                        break;
                    }
                    f++;
                }
            }
            i++;
        }
        if(getdep_mod()) System.out.println("SAIU: remove_par_desnecessarios");
        return string;
    }
    //FIM
}