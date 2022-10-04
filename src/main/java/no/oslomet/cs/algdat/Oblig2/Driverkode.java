package no.oslomet.cs.algdat.Oblig2;

import java.util.*;

public class Driverkode {
    public static void main(String[] args) {
        //Oppg 1
        Liste<String> liste = new DobbeltLenketListe<>();
        System.out.println(liste.antall() + " " + liste.tom());
// Utskrift: 0 true

        String[] s = {"Ole", null, "Per", "Kari", null};
        Liste<String> liste1 = new DobbeltLenketListe<>(s);
        System.out.println(liste1.antall() + " " + liste1.tom());
// Utskrift: 3 false

        //Oppg 2a
        String[] s1 = {}, s2 = {"A"}, s3 = {null,"A",null,"B",null};
        DobbeltLenketListe<String> l1 = new DobbeltLenketListe<>(s1);
        DobbeltLenketListe<String> l2 = new DobbeltLenketListe<>(s2);
        DobbeltLenketListe<String> l3 = new DobbeltLenketListe<>(s3);
        System.out.println(l1.toString() + " " + l2.toString()
                + " " + l3.toString() + " " + l1.omvendtString() + " "
                + l2.omvendtString() + " " + l3.omvendtString());
// Utskrift: [] [A] [A, B] [] [A] [B, A]

        //Oppg 2b
        DobbeltLenketListe<Integer> liste2 = new DobbeltLenketListe<>();
        System.out.println(liste2.toString() + " " + liste2.omvendtString());
        for (int i = 1; i <= 3; i++) {
            liste2.leggInn(i);
            System.out.println(liste2.toString() + " " + liste2.omvendtString());
        }
// Utskrift:
// [] []
// [1] [1]
// [1, 2] [2, 1]
// [1, 2, 3] [3, 2, 1]

        //Oppg 3
        Character[] c = {'A','B','C','D','E','F','G','H','I','J',};
        DobbeltLenketListe<Character> liste3 = new DobbeltLenketListe<>(c);
        System.out.println(liste3.subliste(3,8)); // [D, E, F, G, H]
        System.out.println(liste3.subliste(5,5)); // []
        System.out.println(liste3.subliste(8,liste3.antall())); // [I, J]
// System.out.println(liste.subliste(0,11)); // skal kaste unntak

        //Oppg 8
        String[] navn = {"Lars","Anders","Bodil","Kari","Per","Berit"};
        Liste<String> liste4 = new DobbeltLenketListe<>(navn);
        liste4.forEach(st -> System.out.print(st + " "));
        System.out.println();
        for (String st : liste4) System.out.print(st + " ");
// Utskrift:
// Lars Anders Bodil Kari Per Berit
// Lars Anders Bodil Kari Per Berit

        //Oppg 9
        DobbeltLenketListe<String> liste5 =
                new DobbeltLenketListe<>(new String[]
                        {"Birger","Lars","Anders","Bodil","Kari","Per","Berit"});
        liste5.fjernHvis(navn1 -> navn1.charAt(0) == 'B'); // fjerner navn som starter med B
        System.out.println(liste5 + " " + liste5.omvendtString());
// Utskrift: [Lars, Anders, Kari, Per] [Per, Kari, Anders, Lars]

        //Oppg 10
        String[] navn2 = {"Lars","Anders","Bodil","Kari","Per","Berit"};
        Liste<String> listeA = new DobbeltLenketListe<>(navn2);
        //Liste<String> listeB = new TabellListe<>(navn2);
        //Liste<String> listeC = new EnkeltLenketListe<>(navn2);
        DobbeltLenketListe.sorter(listeA, Comparator.naturalOrder());
        //DobbeltLenketListe.sorter(listeB, Comparator.naturalOrder());
        //DobbeltLenketListe.sorter(listeC, Comparator.naturalOrder());
        System.out.println(listeA); // [Anders, Berit, Bodil, Kari, Lars, Per]
        //System.out.println(listeB); // [Anders, Berit, Bodil, Kari, Lars, Per]
        //System.out.println(listeC); // [Anders, Berit, Bodil, Kari, Lars, Per]
// Tabellen navn er upåvirket:
        System.out.println(Arrays.toString(navn2));
// [Lars, Anders, Bodil, Kari, Per, Berit]

    }
}
