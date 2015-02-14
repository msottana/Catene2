/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package catene2;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Matteo e Giulia
 */
public class Catene2 {

    /**
     * @param args the command line arguments
     */
    public static Tripla getChain(int n) {
        Tripla ret = new Tripla();
        ret.chain = new double[n][n];
        ret.pi = new double[n];
        ret.ro = new int[n];
        ArrayList<ArrayList<Integer>> s = new ArrayList();
        ArrayList<Integer> appoggio = new ArrayList();
        ArrayList<ArrayList<Integer>> gruppi;

        Random gen = new Random();
        for (int i = 0; i < n; i++) {
            appoggio.add(i);
        }
        for (int i = 0; i < n; i++) {
            ret.ro[i] = appoggio.remove(gen.nextInt(n - i));
        }
        //generazione pi e inserimento in u
        ArrayList<Integer> nodi = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (ret.pi[i] == 0) {
                ret.pi[i] = gen.nextDouble();
            }
            ret.pi[ret.ro[i]] = ret.pi[i];
            nodi.add(i);
        }
        //gruppi corrisponde all'insieme u
        gruppi = createGroups(ret.ro, nodi);
        System.out.println("i gruppi sono" + gruppi);
        s.add(gruppi.remove(gen.nextInt(gruppi.size())));
        while (!gruppi.isEmpty()) {
            ArrayList<Integer> gruppoU = gruppi.remove(gen.nextInt(gruppi.size()));
            System.out.println("il gruppo u scelto é" + gruppoU); 
            ArrayList<Integer> gruppoS = s.get(gen.nextInt(s.size()));
            System.out.println("il gruppo s scelto é" + gruppoS); 
            s.add(gruppoU);
            int a = gruppoS.get(gen.nextInt(gruppoS.size()));
            System.out.println("a é " + a );
            int b = gruppoU.get(gen.nextInt(gruppoU.size()));
            System.out.println("b é " + b);
            double pesoInverso = rinomine(a, b, ret.pi, ret.chain, ret.ro);
            if(((gruppoS.size()/2)) > 0){
                int c = gruppoS.get(2*(gen.nextInt((gruppoS.size()/2))));
                rinomineInverse(b, c, ret.pi, ret.chain, ret.ro, pesoInverso);
            }
            else
                rinomineInverse(b, a, ret.pi, ret.chain, ret.ro, pesoInverso);
        }
        boolean gruppoDispari = false;
        int i = 0;
        while (!gruppoDispari && i < s.size()){
            gruppoDispari = s.get(i).size() % 2 != 0;
            i++;
        }
        System.out.println("c'é un gruppo dispari?" + gruppoDispari);
                
        if(!gruppoDispari) {
            ArrayList<Integer> gruppoConnesso = s.get(gen.nextInt(s.size()));
            int a = gruppoConnesso.get(gen.nextInt(gruppoConnesso.size()));
            int b = ret.ro[a];
            double pesoInverso = rinomine(a, b, ret.pi, ret.chain, ret.ro);
            if(((gruppoConnesso.size()/2)) > 0){
                int c = gruppoConnesso.get(2*(gen.nextInt((gruppoConnesso.size()/2))));
                rinomineInverse(b, c, ret.pi, ret.chain, ret.ro, pesoInverso);
            }
            else
                rinomineInverse(b, a, ret.pi, ret.chain, ret.ro, pesoInverso);
        }
        return ret;
    }

    //funzione che ci restituisce tutti i gruppi. Ogni gruppo é un array list e i gruppi sono contenuti tutti in un'array list di array list
    public static ArrayList<ArrayList<Integer>> createGroups(int[] ro, ArrayList<Integer> nodi) {
        ArrayList<ArrayList<Integer>> gruppi;
        gruppi = new ArrayList<>();
        while (!nodi.isEmpty()) {
            int i = nodi.get((Integer) 0);
            int r = ro[i];
            int nIniz = i;
            int rIniz = r;
            ArrayList<Integer> gruppo;
            gruppo = new ArrayList<>();
            gruppi.add(gruppo);
            do {
                gruppo.add((Integer) i);//n è l'etichetta, voglio togliere il nodo chiamato n non quello alla posizione n
                nodi.remove((Integer) i);
                i = r;
                r = ro[r];
            } while (i != nIniz && r != rIniz);
        }
        return gruppi;
    }

    public static void main(String[] args) {
        int n = 4;
        long startTime = System.currentTimeMillis();
        long stopTime;
        long elapsedTime;
        Tripla tests[] = new Tripla[1];
        for (int k = 0; k < 1; k++) {
            Tripla chain = getChain(n);
            double archi[][] = chain.chain;
            double nodi[] = chain.pi;
            tests[k] = chain;
            for (int i = 0; i < n; i++) {
                System.out.print(nodi[i] + " ");
            }
            System.out.println("");
            for (int i = 0; i < n; i++) {
                System.out.print(i + "->" + chain.ro[i] + "/");
            }
            System.out.println("");
            System.out.println("");
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    System.out.print(archi[i][j] + " | ");
                }
                System.out.println("");
            }
            System.out.println("---------------------------------------------");
        }
        stopTime = System.currentTimeMillis();
        elapsedTime = stopTime - startTime;
        System.out.println("Elapsed time: " + elapsedTime + "ms");
    }

    //algoritmo che genera un arco e tutte gli archi tra le rinomine necessari per la solita formuletta
    private static double rinomine(int a, int b, double[] pi, double[][] chain, int[] ro) {
        Random gen = new Random();
        int aR = ro[b];
        int bR = ro[a];
        int temp;
        chain[a][b] = gen.nextDouble();
        System.out.println("prima rate =" + chain[a][b] + "tra archi" + a + b);
        chain[aR][bR] = pi[a] * chain[a][b] / pi[b];
        System.out.println("seconda rate =" + chain[aR][bR] + "tra archi" + aR + bR);
        temp = aR;
        aR = ro[bR];
        bR = ro[temp];
        while (a != aR || b != bR) {
            chain[aR][bR] = chain[a][b];
            System.out.println("PRIMA rate =" + chain[aR][bR] + "tra archi" + aR + bR);
            chain[ro[bR]][ro[aR]] = chain[ro[b]][ro[a]];
            System.out.println("SECONDA rate =" + chain[ro[bR]][ro[aR]] + "tra archi" + ro[bR] +ro[aR]);
            aR = ro[aR];
            bR = ro[bR];
            System.out.println("ro aR "+ aR);
            System.out.println("ro bR "+ bR);
        }
        System.out.println(" rate INVERSA =" + chain[ro[b]][ro[a]]);
        return chain[ro[b]][ro[a]];
    }
    private static void rinomineInverse(int a, int b, double[] pi, double[][] chain, int[] ro, double pesoInverso) {
        int aR = ro[b];
        int bR = ro[a];
        int temp;
        chain[a][b] = pesoInverso;
        System.out.println("INVERSIONE prima rate =" + chain[a][b] + "tra archi" + a + b);
        chain[aR][bR] = pi[a] * chain[a][b] / pi[b];
        System.out.println("INVERSIONE seconda rate =" + chain[aR][bR] + "tra archi" + aR + bR);
        temp = aR;
        aR = ro[bR];
        bR = ro[temp];
        while (a != aR || b != bR) {
            chain[aR][bR] = chain[a][b];
            System.out.println("INVERSIONE PRIMA rate =" + chain[aR][bR] + "tra archi" + aR + bR);
            chain[ro[bR]][ro[aR]] = chain[ro[b]][ro[a]];
            System.out.println("INVERSIONE SECONDA rate =" + chain[ro[bR]][ro[aR]] + "tra archi" + ro[bR] +ro[aR]);
            aR = ro[aR];
            bR = ro[bR];
            System.out.println("INVERSIONE ro aR "+ aR);
            System.out.println("INVERSIONE ro bR "+ bR);
        }
    }
}

class Tripla {

    double pi[];
    double chain[][];
    int ro[];
}
