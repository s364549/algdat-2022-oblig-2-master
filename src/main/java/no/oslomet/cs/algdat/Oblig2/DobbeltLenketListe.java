package no.oslomet.cs.algdat.Oblig2;


////////////////// class DobbeltLenketListe //////////////////////////////


import java.util.Comparator;
import java.util.Iterator;
import java.util.Objects;


public class DobbeltLenketListe<T> implements Liste<T> {
    /**
     * Node class
     *
     * @param <T>
     */
    private static final class Node<T> {
        private T verdi;                   // nodens verdi
        private Node<T> forrige, neste;    // pekere

        private Node(T verdi, Node<T> forrige, Node<T> neste) {
            this.verdi = verdi;
            this.forrige = forrige;
            this.neste = neste;
        }

        private Node(T verdi) {
            this(verdi, null, null);
        }
    }

    // instansvariabler
    private Node<T> hode;          // peker til den første i listen
    private Node<T> hale;          // peker til den siste i listen
    private int antall;            // antall noder i listen
    private int endringer;         // antall endringer i listen

    public DobbeltLenketListe() {
        throw new UnsupportedOperationException();
    }

    public DobbeltLenketListe(T[] a) { throw new UnsupportedOperationException();}

    private Node<T> finnNode(int indeks) {
        Node<T> node;

        if (indeks < antall / 2) {               // om indeksen er lavere eller lik halvparten av antall starter det ved hode
            node = hode;
            for (int i = 0; i < indeks; i++) {    // for løkke som går i gjennom listen
                node = node.neste;
            }
        } else {                                // om indeksen er større enn halvparten av antall starter det ved halen
            node = hale;
            for (int i = antall -1; i > indeks; i--) { // for løkke som går gjennom listen fra halen
                node = node.forrige;
            }
        }
        return node;                        // returner noden
    }

    private static void fratilKontroll(int antall, int fra, int til)
    {
        if (fra < 0)                                  // fra er negativ
            throw new IndexOutOfBoundsException
                    ("fra(" + fra + ") er negativ!");

        if (til > antall)                          // til er utenfor tabellen
            throw new IndexOutOfBoundsException
                    ("til(" + til + ") > tablengde(" + antall + ")");

        if (fra > til)                                // fra er større enn til
            throw new IllegalArgumentException
                    ("fra(" + fra + ") > til(" + til + ") - illegalt intervall!");
    }

    public Liste<T> subliste(int fra, int til) {
        DobbeltLenketListe<T> subliste = new DobbeltLenketListe<>(); //lager en ny lenket liste

        subliste.antall = subliste.endringer = 0;
        fratilKontroll(antall,fra,til);         // kontrollerer intervallet

        for (int i = fra; i < til; i++) {       // lager en forløkke
            subliste.leggInn(finnNode(i).verdi); // legger inn nodene
            subliste.antall++;                  // øker antallet
        }
        return subliste;                        // returnerer listen
    }

    @Override
    public int antall() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean tom() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean leggInn(T verdi) { throw new UnsupportedOperationException(); }

    @Override
    public void leggInn(int indeks, T verdi) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean inneholder(T verdi) {
        throw new UnsupportedOperationException();
    }

    @Override
    public T hent(int indeks) {
        indeksKontroll(indeks,false); // Sjekker indeksen ved bruk av metoden indeksKontroll
        return finnNode(indeks).verdi; //henter verdien ved bruk av finnNode metoden
    }

    @Override
    public int indeksTil(T verdi) {
        throw new UnsupportedOperationException();
    }

    @Override
    public T oppdater(int indeks, T nyverdi) {
        Objects.requireNonNull(nyverdi, "Det er ikke tilatt med null-verdier"); //feilmelding om det er nullverdier
        indeksKontroll(indeks,false); // kontrollerer indeksen

        T gammelverdi = finnNode(indeks).verdi; // Setter gammelverdi til indeks verdi
        finnNode(indeks).verdi = nyverdi; // setter indeks verdi til nyverdi
        return gammelverdi; // returnerer gammelverdi
    }

    @Override
    public boolean fjern(T verdi) {
        if (verdi == null) {        // sjekker om verdi er null
            return false;
        }

        Node<T> q = hode, p = null; // opretter hjelpepekere

        while (q != null) {         // løkke for å finne verdien
            if (q.verdi.equals(verdi)) {
                break;              // bryter ut av løkka når verdien er funnet
            }
            p = q;                  // setter p til forgjengeren til q
            q = q.neste;
        }

        if (q == null) {            // fant ikke verdien
            return false;
        } else if (q == hode) {     // sjekker om q er første node
            hode = hode.neste;      // går over q
        } else {
            p.neste = q.neste;      // hopper over q og fjerner valgt node
        }

        if (q == hale) {            // sjekker om q er siste node
            hale = p;               // setter hale til p slik at q blir fjernet
        }

        q.verdi = null;             // nuller ut verdien til q
        q.neste = null;             // nuller ut nestepekeren

        antall--;                   // reduserer antallet til listen
        return true;                // returnerer at fjerningen var vellykket

    }

    @Override
    public T fjern(int indeks) {
        T temp;                     // oppretter hjelpvariabel

        if (indeks == 0) {          // sjekker om indeksen er første noden
            temp = hode.verdi;      //setter hjelpevariabelen til noden som fjernes
            hode = hode.neste;      // setter hoden til neste verdi og fjerner første node
            if (antall == 1) {      //brukes om det bare er en verdi i listen
                hale = null;
            }
        } else {
            Node<T> p = finnNode(indeks - 1); // setter p til noden før indeksen
            Node<T> q = p.neste;    // setter q til noden som skal fjernes
            temp = q.verdi;         // setter hjelpevariabelen til noden som fjernes

            if (q == hale) {        // sjekker om noden som skal fjernes er siste node
                hale = p;           // fjerner siste node
            }
            p.neste = q.neste;      // hopper over q og fjerner q fra listen
        }
        antall--;                   // reduserer antallet til listen
        return temp;
    }

    @Override
    public void nullstill() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        throw new UnsupportedOperationException();
    }

    public String omvendtString() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<T> iterator() {
        throw new UnsupportedOperationException();
    }

    public Iterator<T> iterator(int indeks) {
        throw new UnsupportedOperationException();
    }

    private class DobbeltLenketListeIterator implements Iterator<T> {
        private Node<T> denne;
        private boolean fjernOK;
        private int iteratorendringer;

        private DobbeltLenketListeIterator() {
            denne = hode;     // p starter på den første i listen
            fjernOK = false;  // blir sann når next() kalles
            iteratorendringer = endringer;  // teller endringer
        }

        private DobbeltLenketListeIterator(int indeks) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean hasNext() {
            return denne != null;
        }

        @Override
        public T next() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

    } // class DobbeltLenketListeIterator

    public static <T> void sorter(Liste<T> liste, Comparator<? super T> c) {
        throw new UnsupportedOperationException();
    }

} // class DobbeltLenketListe


