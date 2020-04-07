package class_pkg;

import java.util.Arrays;

public class Funcionamento {

    //Atributos IN
    private String operacoes;
    private int cont_operandos;
    private int cont_operadores;
    private int cont_raiz;
    private int cont_exp;
    private int cont_raiz2;
    private int cont_p;
    private int cont_sp1;
    private int cont_sp2;
    private int cont_mul;
    private int cont_div;
    private int cont_fat;
    private String[] operandos;
    private String[] operadores;
    private String[][] Array_rad;
    private String[][] Array_exp;
    private String[][] Array_mul_div;
    private int[] index_sp1;
    private boolean validade = true;
    private boolean raiz = false;
    private boolean exp = false;
    private boolean raiz2 = false;
    private boolean p = false;
    private boolean sp1 = false;
    private boolean mul = false;
    private boolean div = false;
    private boolean fat = false;
    private boolean sem_operadores = false;
    private boolean unica_raiz = false;
    private char[] aux_operacoes;

    //FIM
    //Construtor IN
    public Funcionamento(String operacoes) {
        //Preparação da string IN
        operacoes = operacoes.replace("\n", "");
        operacoes = operacoes.replace("\t", "");
        operacoes = operacoes.replace("\r", "");
        //FIM
        operacoes = duplo_sinal(operacoes);
        this.operacoes = operacoes;
        aux_operacoes = operacoes.toCharArray();
    }

    //Construtor FIM
    //Getters IN
    public boolean getvalidade() {
        return validade;
    }

    //FIM
    //Setters IN
    private void setvalidade(boolean validade) {
        this.validade = validade;
    }

    //FIM
    //Métodos IN
    public float main() {
        float resultado = 0;
        try {
            verifica_fat();
            verifica_raiz();
            verifica_exp();
            verifica_raiz2();
            verifica_porc();
            verifica_mul_div();
            verifica_sp1();
            verifica_sp2();
            verifica_validade_fat();
            define_operandos_operadores();
            //Caso não haja operadores IN
            if (sem_operadores) {
                try {
                    resultado = Float.parseFloat(operacoes);
                } catch (Exception e) {
                    setvalidade(false);
                }
                return resultado;
            }
            //FIM
            //Caso só haja um operador raiz IN
            if (unica_raiz) {
                try {
                    resultado = met_unica_raiz();
                } catch (Exception e) {
                    setvalidade(false);
                }
                if (resultado == Float.NaN) {
                    resultado = 0;
                    setvalidade(false);
                }
                return resultado;
            }
            //FIM
            if (validade) {
                aux_validade("INICIO", operadores, operandos);
                if (verifica()) {
                    try {
                        calcula_rad(operadores, operandos);
                        aux_validade("RAD", Array_rad[0], Array_rad[1]);
                        calcula_exp(Array_rad[0], Array_rad[1]);
                        aux_validade("EXP", Array_exp[0], Array_exp[1]);
                        calcula_mul_div(Array_exp[0], Array_exp[1]);
                        aux_validade("MUL E DIV", Array_mul_div[0], Array_mul_div[1]);
                        resultado += calcula_som_sub(Array_mul_div[0], Array_mul_div[1]);
                        return resultado;
                    } catch (Exception e) {
                        setvalidade(false);
                        return resultado;
                    }
                } else {
                    return resultado;
                }
            } else {
                return resultado;
            }
        } catch (Exception e) {
            setvalidade(false);
            return resultado;
        }
    }

    private void sinal(String[] operadores, String[] operandos) {
        int i = 0;
        while (i < operadores.length) {
            String sinal = "-";
            if (i > 0) {
                if ("-".equals(operadores[(i - 1)])) {
                    sinal += operandos[i];
                    operandos[i] = sinal;
                    sinal = "-";
                    operadores[(i - 1)] = "+";
                }
            }else{
                if ("-".equals(operadores[i])){
                    sinal += operandos[(i+1)];
                    operandos[(i+1)] = sinal;
                    sinal = "-";
                    operadores[i] = "+";
                }
            }
            i++;
        }
    }

    private void verifica_sp1() {
        for (int i = 1; i < aux_operacoes.length - 1; i++) {
            if ((aux_operacoes[i - 1] == '/'
                    || aux_operacoes[i - 1] == '*'
                    || aux_operacoes[i - 1] == '^'
                    || aux_operacoes[i - 1] == 'R'
                    || aux_operacoes[i - 1] == 'p')
                    && aux_operacoes[i] == '-'
                    && aux_operacoes[i + 1] == 'r') {
                cont_sp1++;
                sp1 = true;
            }
        }
        //Determinar a posição das raízes em que o caso especial ocorre IN
        int aux_cont_raiz = 0;
        int aux_cont_sp1 = 0;
        index_sp1 = new int[cont_sp1];
        if (aux_operacoes[0] == 'r') {
            aux_cont_raiz++;
        }
        for (int i = 1; i < aux_operacoes.length - 1; i++) {
            if (aux_operacoes[i + 1] == 'r') {
                aux_cont_raiz++;
            }
            if ((aux_operacoes[i - 1] == '/'
                    || aux_operacoes[i - 1] == '*'
                    || aux_operacoes[i - 1] == '^'
                    || aux_operacoes[i - 1] == 'R'
                    || aux_operacoes[i - 1] == 'p')
                    && aux_operacoes[i] == '-'
                    && aux_operacoes[i + 1] == 'r') {
                index_sp1[aux_cont_sp1] = aux_cont_raiz;
                aux_cont_sp1++;
            }
        }
        //FIM
    }

    private void verifica_sp2() {
        for (int i = 1; i < aux_operacoes.length - 1; i++) {
            if ((aux_operacoes[i - 1] == '/'
                    || aux_operacoes[i - 1] == '*'
                    || aux_operacoes[i - 1] == '^'
                    || aux_operacoes[i - 1] == 'R'
                    || aux_operacoes[i - 1] == 'p')
                    && aux_operacoes[i] == '+'
                    && aux_operacoes[i + 1] == 'r') {
                cont_sp2++;
            }
        }
    }

    private void verifica_raiz() {
        boolean ver_rad = false;
        int cont = 0;
        for (int i = 0; i < aux_operacoes.length; i++) {
            if (aux_operacoes[i] == 'r') {
                ver_rad = true;
                cont++;
            }
        }
        cont_raiz = cont;
        raiz = ver_rad;
    }

    private void verifica_exp() {
        boolean ver = false;
        int cont = 0;
        for (int i = 0; i < aux_operacoes.length; i++) {
            if (aux_operacoes[i] == '^') {
                ver = true;
                cont++;
            }
        }
        cont_exp = cont;
        exp = ver;
    }
    
    private void verifica_raiz2(){
        boolean ver_rad2 = false;
        int cont = 0;
        for (int i = 0; i < aux_operacoes.length; i++) {
            if (aux_operacoes[i] == 'R') {
                ver_rad2 = true;
                cont++;
            }
        }
        cont_raiz2 = cont;
        raiz2 = ver_rad2;
    }
    
    private void verifica_porc(){
        boolean ver_p = false;
        int cont = 0;
        for (int i = 0; i < aux_operacoes.length; i++) {
            if (aux_operacoes[i] == 'p') {
                ver_p = true;
                cont++;
            }
        }
        cont_p = cont;
        p = ver_p;
    }

    private void verifica_mul_div() {
        for (int i = 0; i < aux_operacoes.length; i++) {
            if (aux_operacoes[i] == '/') {
                div = true;
                cont_div++;
            }
        }
        for (int i = 0; i < aux_operacoes.length; i++) {
            if (aux_operacoes[i] == '*') {
                mul = true;
                cont_mul++;
            }
        }
    }

    private void verifica_fat(){
        boolean ver_fat = false;
        int cont = 0;
        for (int i = 0; i < aux_operacoes.length; i++) {
            if (aux_operacoes[i] == '!') {
                ver_fat = true;
                cont++;
            }
        }
        cont_fat = cont;
        fat = ver_fat;
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
    
    private String calcula_fat(String ope){
        String sfinal = "", aux = "";
        char[] aux_ope = ope.toCharArray();
        int i = 0, j = 0;
        float auxf = 0, resto = 0;
        while(i < aux_ope.length){
            if(aux_ope[i] == '!'){
                j = i;
                aux_ope[j] = 'S';
                j--;
                while(aux_ope[j] != 'r' &&
                        aux_ope[j] != 'R' &&
                        aux_ope[j] != 'p' &&
                        aux_ope[j] != '+' &&
                        aux_ope[j] != '-' &&
                        aux_ope[j] != '*' &&
                        aux_ope[j] != '/' &&
                        aux_ope[j] != '^'){
                    aux += aux_ope[j];
                    aux_ope[j] = 'S';
                    j--;
                    if(j < 0) break;
                }
                //Inverter o aux IN
                StringBuffer sb = new StringBuffer(aux);
                sb.reverse();
                aux = sb.toString();
                //FIM
                //Verifica parte decimal IN
                auxf = Float.parseFloat(aux);
                resto = auxf - (float) Math.floor((float) auxf);
                if(resto != 0){
                    auxf = 0;
                    resto = 0;
                    setvalidade(false);
                    break;
                }
                auxf = 0;
                resto = 0;
                //FIM
                System.out.println("AUX OPE IN");
                for(int l = 0; l < aux_ope.length; l++) System.out.print(aux_ope[l]);
                System.out.println();
                System.out.println("AUX: "+aux);
                System.out.println("AUX OPE FIM");
                System.out.println("CALCULO IN");
                System.out.println("AUX: "+aux);
                auxf = calculofat(aux);
                aux = Float.toString(auxf);
                System.out.println("AUX POS: "+aux);
                System.out.println("CALCULO FIM");
                System.out.println("SUBSTITUICAO IN");
                j = 0;
                while(j < aux_ope.length){
                    if(aux_ope[j] == 'S'){
                        sfinal += aux;
                        while(aux_ope[j] == 'S'){
                            j++;
                            if(j >= aux_ope.length) break;
                        }
                    }
                    if(j >= aux_ope.length) break;
                    sfinal += aux_ope[j];
                    j++;
                }
                System.out.println("SFINAL : "+sfinal);
                System.out.println("SUBSTITUICAO FIM");
                ope = sfinal;
                aux_ope = ope.toCharArray();
                i = 0;
                System.out.println("STRING F : "+ope);
            }
            i++;
        }
        return sfinal;
    }
    
    private float calculofat(String op1){
        float resultado = 1, aux_op1 = 0;
        float i = 1;
        try{
            aux_op1 = Float.parseFloat(op1);
            if(aux_op1 >= 120){
                aux_op1 = Float.POSITIVE_INFINITY;
                resultado = aux_op1;
            }
            else while(i <= aux_op1){
                resultado *= i;
                i++;
            }
        }catch(Exception e){
            setvalidade(false);
            resultado = 0;
        }
        return resultado;
    }
    
    private void calcula_rad(String[] operadores, String[] operandos) {
        int index_r = 0, index_op = 0;
        //Exponenciação e radiciação IN
        //Radiciação IN
        if (raiz) {
            //Operando os valores e substituindo nos arrays IN
            float aux_r = 0;
            int aux_rad_ng = 0;
            int aux_index = 0;
            String sinal_r = "-";
            while (index_r != operadores.length) {
                if ("r".equals(operadores[index_r])) {
                    aux_r = calculando(operandos[index_op], "1", "r");
                    aux_rad_ng++;
                    if (index_r > 0) {
                        if ("-".equals(operadores[(index_r - 1)])) {
                            sinal_r += aux_r;
                            operandos[index_op] = sinal_r;
                            operadores[(index_r - 1)] = "+";
                            sinal_r = "-";
                        } else {
                            if (sp1) {
                                if (index_sp1[aux_index] == aux_rad_ng) {
                                    sinal_r += aux_r;
                                    operandos[index_op] = sinal_r;
                                    sinal_r = "-";
                                    if (aux_index < index_sp1.length - 1) {
                                        aux_index++;
                                    }
                                } else {
                                    operandos[index_op] = Float.toString(aux_r);
                                }
                            } else {
                                operandos[index_op] = Float.toString(aux_r);
                            }
                        }
                    } else {
                        if (sp1) {
                            if (index_sp1[aux_index] == aux_rad_ng) {
                                sinal_r += aux_r;
                                operandos[index_op] = sinal_r;
                                sinal_r = "-";
                                if (aux_index < index_sp1.length - 1) {
                                    aux_index++;
                                }
                            } else {
                                operandos[index_op] = Float.toString(aux_r);
                            }
                        } else {
                            operandos[index_op] = Float.toString(aux_r);
                        }
                    }
                    index_r++;
                } else {
                    index_op++;
                    index_r++;
                }
            }
            //FIM
            //Caso haja apenas um operador e um operando IN
            //Caso haja sinal de + ou - IN
            if (operadores.length == 1 && operandos.length == 1) {
                switch (aux_operacoes[0]) {
                    case '-':
                        operandos[0] = Float.toString(aux_r * -1);
                    case '+':
                        operandos[0] = Float.toString(aux_r);
                    default:
                        operandos[0] = Float.toString(aux_r);
                }
            }
            //FIM
            //FIM
            //Caso o primeiro valor tenha quer ser negativo IN
            if (aux_operacoes[0] == '-' && "r".equals(operadores[0])) {
                operandos[0] = sinal_r + operandos[0];
                sinal_r = "-";
            }
            //FIM
        }
        aux_validade("POS RAD", operadores, operandos);
        //FIM
        index_r = 0;
        index_op = 0;
        //Radiciação 2 IN
        String sinal_raiz2 = "-";
        float aux_raiz2 = 0;
        if (raiz2) {
            while (index_r != operadores.length) {
                if ("r".equals(operadores[index_r])) {
                    index_r++;
                } else if ("+".equals(operadores[index_r])
                        || "-".equals(operadores[index_r])
                        || "*".equals(operadores[index_r])
                        || "/".equals(operadores[index_r])
                        || "p".equals(operadores[index_r])
                        || "^".equals(operadores[index_r])) {
                    index_op++;
                    index_r++;
                } else {
                    aux_raiz2 = calculando(operandos[index_op], operandos[(index_op + 1)], operadores[index_r]);
                    //Substituição dos operandos IN                    
                    operandos[index_op] = "S";
                    operandos[(index_op + 1)] = Float.toString(aux_raiz2);
                    //FIM
                    index_op++;
                    index_r++;
                }
            }
            //Caso haja apenas um operador e um operando IN
            //Caso haja sinal de + ou - IN
            if (operadores.length == 1 && operandos.length == 2) {
                switch (aux_operacoes[0]) {
                    case '-':
                        operandos[1] = Float.toString(aux_raiz2 * -1);
                    case '+':
                        operandos[1] = Float.toString(aux_raiz2);
                    default:
                        operandos[1] = Float.toString(aux_raiz2);
                }
            }
            //FIM
            //FIM
            //Caso o primeiro valor tenha quer ser negativo IN
            if (aux_operacoes[0] == '-' && "R".equals(operadores[0])) {
                operandos[1] = sinal_raiz2 + operandos[1];
                sinal_raiz2 = "-";
            }
        }
        aux_validade("POS RAD 2", operadores, operandos);
        //FIM        
        substitui_rad();
    }

    private void substitui_rad() {
        int cont_index = 0;
        cont_operadores -= cont_raiz;
        cont_operadores -= cont_raiz2;
        cont_operandos -= cont_raiz2;
        Array_rad = new String[2][];
        Array_rad[0] = new String[cont_operadores];
        Array_rad[1] = new String[cont_operandos];
        for (int i = 0; i < operadores.length; i++) {
            if (!("r".equals(operadores[i]) || "R".equals(operadores[i]))) {
                Array_rad[0][cont_index] = operadores[i];
                cont_index++;
            }
        }
        cont_index = 0;
        for (int i = 0; i < operandos.length; i++) {
            if (!("S".equals(operandos[i]))) {
                Array_rad[1][cont_index] = operandos[i];
                cont_index++;
            }
        }
    }
    
    private void calcula_exp(String[] operadores, String[] operandos){
        //Exponenciação IN
        int index_r = 0, index_op = 0;
        String sinal_exp = "-";
        float aux_exp = 0;
        if (exp) {
            while (index_r != operadores.length) {
                if ("r".equals(operadores[index_r])) {
                    index_r++;
                } else if ("+".equals(operadores[index_r])
                        || "-".equals(operadores[index_r])
                        || "*".equals(operadores[index_r])
                        || "/".equals(operadores[index_r])
                        || "R".equals(operadores[index_r])
                        || "p".equals(operadores[index_r])) {
                    index_op++;
                    index_r++;
                } else {
                    //Verifica o sinal IN
                    if (index_r > 0) {
                        if ("-".equals(operadores[(index_r - 1)])) {
                            sinal_exp += operandos[index_r];
                            operandos[index_r] = sinal_exp;
                            sinal_exp = "-";
                            operadores[(index_r - 1)] = "+";
                        }
                    }
                    //FIM
                    aux_exp = calculando(operandos[index_op], operandos[(index_op + 1)], operadores[index_r]);
                    //Substituição dos operandos IN                    
                    operandos[index_op] = "S";
                    operandos[(index_op + 1)] = Float.toString(aux_exp);
                    //FIM
                    index_op++;
                    index_r++;
                }
            }
        }
        aux_validade("POS EXP", operadores, operandos);
        substitui_exp(Array_rad[0], Array_rad[1]);
    }
    
    private void substitui_exp(String[] operadores, String[] operandos){
        int cont_index = 0;
        cont_operadores -= cont_exp;
        cont_operandos -= cont_exp;
        Array_exp = new String[2][];
        Array_exp[0] = new String[cont_operadores];
        Array_exp[1] = new String[cont_operandos];
        for (int i = 0; i < operadores.length; i++) {
            if (!("^".equals(operadores[i]))) {
                Array_exp[0][cont_index] = operadores[i];
                cont_index++;
            }
        }
        cont_index = 0;
        for (int i = 0; i < operandos.length; i++) {
            if (!("S".equals(operandos[i]))) {
                Array_exp[1][cont_index] = operandos[i];
                cont_index++;
            }
        }
    }

    private void calcula_mul_div(String[] operadores, String[] operandos) {
        //Multiplicação e divisão IN
        //Verifical o sinal IN
        sinal(operadores, operandos);
        //FIM
        if (div || mul || p) {
            float aux_mul_div = 0;
            int i = 0;
            while (i < operadores.length) {
                if ("*".equals(operadores[i]) || "/".equals(operadores[i]) || "p".equals(operadores[i])) {
                    aux_mul_div = calculando(operandos[i], operandos[(i + 1)], operadores[i]);
                    operandos[i] = "S";
                    operandos[(i + 1)] = Float.toString(aux_mul_div);
                }
                i++;
            }
        }
        //FIM
        substitui_mul_div(Array_exp[0], Array_exp[1]);
    }

    private void substitui_mul_div(String[] operadores, String[] operandos) {
        aux_validade("SUBSTITUI_MUL_DIV IN", operadores, operandos);
        int cont_index = 0;
        cont_operadores -= cont_mul;
        cont_operadores -= cont_div;
        cont_operadores -= cont_p;
        cont_operandos -= cont_mul;
        cont_operandos -= cont_div;
        cont_operandos -= cont_p;
        Array_mul_div = new String[2][];
        Array_mul_div[0] = new String[cont_operadores];
        Array_mul_div[1] = new String[cont_operandos];
        for (int i = 0; i < operadores.length; i++) {
            if (!("/".equals(operadores[i]) || "*".equals(operadores[i]) || "p".equals(operadores[i]))) {
                Array_mul_div[0][cont_index] = operadores[i];
                cont_index++;
            }
        }
        cont_index = 0;
        for (int i = 0; i < operandos.length; i++) {
            if (!("S".equals(operandos[i]))) {
                Array_mul_div[1][cont_index] = operandos[i];
                cont_index++;
            }
        }
        aux_validade("SUBSTITUI_MUL_DIV FIM", Array_mul_div[0], Array_mul_div[1]);
    }

    private float calcula_som_sub(String[] operadores, String[] operandos) {
        float total = 0;
        //Verifical o sinal IN
        sinal(operadores, operandos);
        //FIM
        //Caso especial de um único operador (raiz) IN
        if (operadores.length == 1 && operandos.length == 1) {
            return Float.parseFloat(operandos[0]);
        }
        //FIM
        //Soma e subtração IN
        for (int i = 0; i < operandos.length; i++) {
            total += Float.parseFloat(operandos[i]);
        }
        //FIM
        return total;
    }

    private void define_operandos_operadores() {
        try {
            //Verifica se há fatorial IN
            if(fat){
                System.out.println("OPERACOES FAT IN : "+operacoes);
                operacoes = calcula_fat(operacoes);
                System.out.println("OPERACOES FAT FIM : "+operacoes);
                aux_operacoes = operacoes.toCharArray();
                
            }
            //FIM
            //Determinar o número de operadores e operandos IN
            int i = 1; //O primeiro valor não será considerado
            //Número de operadores IN
            while (i < aux_operacoes.length - 1) {
                if (aux_operacoes[i] == '*'
                        || aux_operacoes[i] == '/'
                        || aux_operacoes[i] == '^'
                        || aux_operacoes[i] == 'r'
                        || aux_operacoes[i] == 'R'
                        || aux_operacoes[i] == 'p') {
                    cont_operadores++;
                } else if (aux_operacoes[i] == '+'
                        || aux_operacoes[i] == '-') {
                    if(aux_operacoes[i - 1] == 'r'
                            || aux_operacoes[i - 1] == '*'
                            || aux_operacoes[i - 1] == '/'
                            || aux_operacoes[i - 1] == '^'
                            || aux_operacoes[i - 1] == 'R'
                            || aux_operacoes[i - 1] == 'p'){
                        i++;
                        continue;
                    }
                    if (!(aux_operacoes[i + 1] == '*'
                            || aux_operacoes[i + 1] == '/'
                            || aux_operacoes[i + 1] == '^'
                            || aux_operacoes[i + 1] == 'R'
                            || aux_operacoes[i + 1] == 'p')) {
                        cont_operadores++;
                    }
                }
                i++;
            }
            //Caso o primeiro e unico operacor seja uma raiz IN
            if (aux_operacoes[0] == 'r' && cont_operadores == 0) {
                unica_raiz = true;
            }
            //FIM
            if (!(unica_raiz)) {
                i = 0;
                //FIM
                //Verifica se não há operadores IN
                if (cont_operadores == 0) {
                    sem_operadores = true;
                }
                //FIM
                if (!(sem_operadores)) {
                    //Número de operandos IN
                    cont_operadores -= cont_sp2;
                    if (sp1) {
                        System.out.println(cont_sp1);
                        cont_operadores -= cont_sp1;
                        cont_operandos = cont_operadores - cont_raiz + 1;
                    } else {
                        cont_operandos = cont_operadores - cont_raiz + 1;
                    }
                    //FIM
                    //FIM
                    //Instanciando os Arrays IN
                    int acr = 0;
                    if (aux_operacoes[0] == 'r') {
                        i++;
                        acr++;
                        cont_operadores++;
                        operadores = new String[cont_operadores];
                        operadores[0] = "r";
                        cont_operandos++;
                        operandos = new String[cont_operandos];
                    } else {
                        operadores = new String[cont_operadores];
                        operandos = new String[cont_operandos];
                    }
                    aux_validade("INSTANCIA 2 ARRAYS", operadores, operandos);
                    //FIM
                    //Atribuindo valores aos Arrays IN
                    //Array operadores IN
                    String aux_atr = "";
                    int aux_ind = 0 + acr;
                    //Verificando se o primeiro sinal é de + IN
                    if (aux_operacoes[0] == '+' || aux_operacoes[0] == '-') {
                        i++;
                    }
                    //FIM
                    while (i < aux_operacoes.length - 1) {
                        if (i > 0) {
                            if ((aux_operacoes[i - 1] == '/'
                                    || aux_operacoes[i - 1] == '*'
                                    || aux_operacoes[i - 1] == '^'
                                    || aux_operacoes[i - 1] == 'R'
                                    || aux_operacoes[i - 1] == 'p')
                                    && (aux_operacoes[i] == '-' || aux_operacoes[i] == '+')
                                    && aux_operacoes[i + 1] == 'r') {
                                i++;
                                continue;
                            }
                            if (aux_operacoes[i] == '+'
                                    || aux_operacoes[i] == '-') {
                                if (aux_operacoes[i - 1] == 'r'
                                        || aux_operacoes[i - 1] == '*'
                                        || aux_operacoes[i - 1] == '/'
                                        || aux_operacoes[i - 1] == '^'
                                        || aux_operacoes[i - 1] == 'R'
                                        || aux_operacoes[i - 1] == 'p') {
                                    i++;
                                    continue;
                                }
                            }
                        }
                        if (aux_operacoes[i] == '*'
                                || aux_operacoes[i] == '/'
                                || aux_operacoes[i] == '^'
                                || aux_operacoes[i] == 'r'
                                || aux_operacoes[i] == 'R'
                                || aux_operacoes[i] == 'p') {
                            aux_atr += aux_operacoes[i];
                            operadores[aux_ind] = aux_atr;
                            aux_atr = "";
                            aux_ind++;
                        } else if (aux_operacoes[i] == '+'
                                || aux_operacoes[i] == '-') {
                            if (!(aux_operacoes[i + 1] == '*'
                                    || aux_operacoes[i + 1] == '/'
                                    || aux_operacoes[i + 1] == '^'
                                    || aux_operacoes[i + 1] == 'R'
                                    || aux_operacoes[i + 1] == 'p')) {
                                aux_atr += aux_operacoes[i];
                                operadores[aux_ind] = aux_atr;
                                aux_atr = "";
                                aux_ind++;
                            }
                        }
                        i++;
                    }
                    i = 1;
                    aux_ind = 0;
                    aux_atr = "";
                    aux_validade("INSTANCIA OPERADORES", operadores, operandos);
                    //FIM
                    //Array Operandos IN
                    if (!(aux_operacoes[0] == '*'
                            || aux_operacoes[0] == '/'
                            || aux_operacoes[0] == '.'
                            || aux_operacoes[0] == 'r'
                            || aux_operacoes[0] == '^'
                            || aux_operacoes[0] == 'R'
                            || aux_operacoes[0] == 'p') && aux_operacoes[1] != 'r') {
                        System.out.println("aux_operacoes[" + 0 + "] = " + aux_operacoes[0]);
                        aux_atr += aux_operacoes[0];
                    } else if (!(aux_operacoes[0] == '*'
                            || aux_operacoes[0] == '/'
                            || aux_operacoes[0] == '.'
                            || aux_operacoes[0] == 'r'
                            || aux_operacoes[0] == '^'
                            || aux_operacoes[0] == 'R'
                            || aux_operacoes[0] == 'p') && aux_operacoes[1] == 'r') {
                        System.out.println("aux_operacoes[" + 0 + "] = " + aux_operacoes[0]);
                        i++;
                    }
                    while (i < aux_operacoes.length) {
                        System.out.println("aux_operacoes[" + i + "] = " + aux_operacoes[i]);
                        if (i > 0 && i < aux_operacoes.length) {
                            if ((aux_operacoes[i - 1] == '/'
                                    || aux_operacoes[i - 1] == '*'
                                    || aux_operacoes[i - 1] == '^'
                                    || aux_operacoes[i - 1] == 'R'
                                    || aux_operacoes[i - 1] == 'p')
                                    && (aux_operacoes[i] == '-' || aux_operacoes[i] == '+')
                                    && aux_operacoes[i + 1] == 'r') {
                                i += 2;
                                continue;
                            }
                        }
                        //Permite sinais nos números IN
                        if ((aux_operacoes[i] == '+'
                                || aux_operacoes[i] == '-')
                                && (aux_operacoes[(i - 1)] == '*'
                                || aux_operacoes[(i - 1)] == '/'
                                || aux_operacoes[(i - 1)] == 'r'
                                || aux_operacoes[(i - 1)] == '^'
                                || aux_operacoes[(i - 1)] == 'R'
                                || aux_operacoes[(i - 1)] == 'p')) {
                            aux_atr += aux_operacoes[i];
                            i++;
                            continue;
                        }
                        //FIM
                        //Permite operadores antes de uma raiz IN
                        if (i < (aux_operacoes.length - 2)) {
                            if ((aux_operacoes[i] == '*'
                                    || aux_operacoes[i] == '/'
                                    || aux_operacoes[i] == '+'
                                    || aux_operacoes[i] == '-'
                                    || aux_operacoes[i] == '^'
                                    || aux_operacoes[i] == 'R'
                                    || aux_operacoes[i] == 'p')
                                    && aux_operacoes[(i + 1)] == 'r') {
                                i++;
                                continue;
                            }
                        }
                        //FIM
                        if (!(aux_operacoes[i] == '+'
                                || aux_operacoes[i] == '-'
                                || aux_operacoes[i] == '*'
                                || aux_operacoes[i] == '/'
                                || aux_operacoes[i] == 'r'
                                || aux_operacoes[i] == '^'
                                || aux_operacoes[i] == 'R'
                                || aux_operacoes[i] == 'p')) {
                            aux_atr += aux_operacoes[i];
                            i++;
                        } else {
                            operandos[aux_ind] = aux_atr;
                            aux_ind++;
                            i++;
                            aux_atr = "";
                        }
                    }
                    operandos[aux_ind] = aux_atr;
                    aux_validade("INSTANCIA OPERANDOS", operadores, operandos);
                    //FIM
                    //FIM
                }
            }
        } catch (Exception e) {
            setvalidade(false);
        }
    }

    private float calculando(String op1, String op2, String operador) {
        float resultado = 0;
        float aux_op1, aux_op2;
        try {
            aux_op1 = Float.parseFloat(op1);
            aux_op2 = Float.parseFloat(op2);
            switch (operador) {
                case "+":
                    resultado = aux_op1 + aux_op2;
                    break;
                case "-":
                    resultado = aux_op1 - aux_op2;
                    break;
                case "*":
                    resultado = aux_op1 * aux_op2;
                    break;
                case "/":
                    resultado = aux_op1 / aux_op2;
                    break;
                case "^":
                    resultado = (float) Math.pow(aux_op1, aux_op2);
                    break;
                case "r":
                    resultado = (float) Math.sqrt(aux_op1);
                    break;
                case "R":
                    float aux;
                    aux = (float) 1/aux_op1;
                    resultado = (float) Math.pow(aux_op2, aux);
                    break;
                case "p":
                    resultado = (float) (aux_op1/100)*aux_op2;
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            resultado = 0;
            setvalidade(false);
        }
        return resultado;
    }
    
    private float met_unica_raiz(){
        float resultado;
        try {
            if(aux_operacoes[1] == '-') setvalidade(false);
            String aux = "";
            for (int i = 1; i < aux_operacoes.length; i++) {
                aux += aux_operacoes[i];
            }
            resultado = calculando(aux, "1", "r");
            if(resultado == Float.NaN){
                resultado = 0;
                setvalidade(false);
            }
        } catch (Exception e) {
            resultado = 0;
            setvalidade(false);
        }
        return resultado;
    }

    public boolean verifica() {
        setvalidade(true);
        if (raiz) {
            if (operacoes == null || "".equals(operacoes)) {
                //System.out.println("A");
                setvalidade(false);
                return validade;
            } else if (cont_raiz == 1 && cont_operadores == 1 && cont_operandos != 1) {
                //System.out.println("B");
                setvalidade(false);
                return validade;
            } else if (cont_operandos < 1 || cont_operadores < 1) {
                //System.out.println("C");
                setvalidade(false);
                return validade;
            } else {
                if ((cont_operadores - cont_raiz) != (cont_operandos - 1)) {
                    setvalidade(false);
                    return validade;
                }
            }
        } else {
            if (operacoes == null || "".equals(operacoes)) {
                setvalidade(false);
                return validade;
            } else if (cont_operandos < 2 || cont_operadores < 1) {
                setvalidade(false);
                return validade;
            } else if (cont_operandos != (cont_operadores + 1)) {
                setvalidade(false);
                return validade;
            }
        }
        if (aux_operacoes[0] == '*'
                || aux_operacoes[0] == '/'
                || aux_operacoes[0] == '.'
                || aux_operacoes[0] == '^'
                || aux_operacoes[0] == 'R'
                || aux_operacoes[0] == 'p') {
            //System.out.println("I");
            setvalidade(false);
            return validade;
        } else if (aux_operacoes[(aux_operacoes.length - 1)] == '+'
                || aux_operacoes[(aux_operacoes.length - 1)] == '-'
                || aux_operacoes[(aux_operacoes.length - 1)] == '*'
                || aux_operacoes[(aux_operacoes.length - 1)] == '/'
                || aux_operacoes[(aux_operacoes.length - 1)] == '.'
                || aux_operacoes[(aux_operacoes.length - 1)] == '^'
                || aux_operacoes[(aux_operacoes.length - 1)] == 'r'
                || aux_operacoes[(aux_operacoes.length - 1)] == 'R'
                || aux_operacoes[(aux_operacoes.length - 1)] == 'p') {
            //System.out.println("U");
            setvalidade(false);
            return validade;
        }
        for (int i = 0; i < (aux_operacoes.length - 1); i++) {
            if (aux_operacoes[i] == '.' && aux_operacoes[(i + 1)] == '.') {
                setvalidade(false);
                break;
            } else if ((aux_operacoes[i] == 'r' || aux_operacoes[i] == 'R') && aux_operacoes[(i + 1)] == '-') {
                //System.out.println("V");
                setvalidade(false);
                break;
            } else if (aux_operacoes[(i + 1)] == 'r') {
                if (!(aux_operacoes[i] == '+'
                        || aux_operacoes[i] == '-'
                        || aux_operacoes[i] == '*'
                        || aux_operacoes[i] == '/'
                        || aux_operacoes[i] == '^'
                        || aux_operacoes[i] == 'R'
                        || aux_operacoes[i] == 'p')) {
                    //System.out.println("Y");
                    setvalidade(false);
                    break;
                }
            } else if (aux_operacoes[i] == '.' && (aux_operacoes[(i + 1)] == '+'
                    || aux_operacoes[(i + 1)] == '-'
                    || aux_operacoes[(i + 1)] == '*'
                    || aux_operacoes[(i + 1)] == '/'
                    || aux_operacoes[(i + 1)] == '^'
                    || aux_operacoes[(i + 1)] == 'r'
                    || aux_operacoes[(i + 1)] == 'R'
                    || aux_operacoes[(i + 1)] == 'p')) {
                //System.out.println("X");
                setvalidade(false);
                break;
            } else if ((aux_operacoes[i] == '+'
                    || aux_operacoes[i] == '-'
                    || aux_operacoes[i] == '*'
                    || aux_operacoes[i] == '/'
                    || aux_operacoes[i] == '^'
                    || aux_operacoes[i] == 'r'
                    || aux_operacoes[i] == 'R'
                    || aux_operacoes[i] == 'p') && aux_operacoes[(i + 1)] == '.') {
                //System.out.println("Z");
                setvalidade(false);
                break;
            } else if ((aux_operacoes[i] == '*'
                    || aux_operacoes[i] == '/'
                    || aux_operacoes[i] == '^'
                    || aux_operacoes[i] == 'R'
                    || aux_operacoes[i] == 'p') 
                    && 
                    (aux_operacoes[i + 1] == '*'
                    || aux_operacoes[i + 1] == '/'
                    || aux_operacoes[i + 1] == '^'
                    || aux_operacoes[i + 1] == 'R'
                    || aux_operacoes[i + 1] == 'p')){
                setvalidade(false);
                break;
            }
        }
        return validade;
    }
    
    public void verifica_validade_fat(){
        boolean validadel = true;
        if(aux_operacoes[0] == '!'){
            setvalidade(false);
        }
        int i = 1;
        while(i < aux_operacoes.length){
            if(aux_operacoes[i] == '!' && (aux_operacoes[i - 1] == '!'
                    || aux_operacoes[i - 1] == 'r'
                    || aux_operacoes[i - 1] == 'R'
                    || aux_operacoes[i - 1] == '^'
                    || aux_operacoes[i - 1] == '.'
                    || aux_operacoes[i - 1] == '+'
                    || aux_operacoes[i - 1] == '-'
                    || aux_operacoes[i - 1] == '*'
                    || aux_operacoes[i - 1] == '/'
                    || aux_operacoes[i - 1] == 'p')){
                setvalidade(false);
            }else if(aux_operacoes[i - 1] == '!' && !(aux_operacoes[i] == '+'
                    || aux_operacoes[i] == '-'
                    || aux_operacoes[i] == '*'
                    || aux_operacoes[i] == '/'
                    || aux_operacoes[i] == 'R'
                    || aux_operacoes[i] == '^'
                    || aux_operacoes[i] == 'p')){
                setvalidade(false);
            }
            i++;
        }
    }

    public void aux_validade(String situacao, String[] operadores, String[] operandos) {
        System.out.println("------------" + situacao + "------------");
        int i;
        for (i = 0; i < operandos.length; i++) {
            System.out.print("[" + operandos[i] + "]");
        }
        System.out.println();
        for (i = 0; i < operadores.length; i++) {
            System.out.print("[" + operadores[i] + "]");
        }
        System.out.println();
        System.out.println("Existem " + cont_operadores + " operadores");
        System.out.println("Existem " + cont_operandos + " operandos");
        System.out.println("Existem " + cont_raiz + " raizes");
        System.out.println("Existem " + cont_sp1 + " caso especial /-r");
        System.out.println("Vetor index_sp1 " + Arrays.toString(index_sp1));
        System.out.println("-------------------------------");
    }
    //FIM
}