package no.oslomet.cs.algdat.Oblig2;
//KLADD-----------

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

    public DobbeltLenketListe() {}

    public DobbeltLenketListe(T[] a) {

        if (a == null){
            throw new NullPointerException("Tabellen a er null!");
        }

        if(a.length != 0){

        antall = 0;

            for(int i = 0 ; i < a.length ; i++) {

                if(a[i] != null) {

                    Node<T> newNode = new Node<>(a[i]);

                    if (hode == null) {
                        hode = hale = newNode;
                        //hodes forrige vil være null
                        hode.forrige = null;
                        //hales neste vil være null
                        hale.neste = null;
                    } else {
                        //legg newNode1 til slutten av listen. hale->neste vil være newNode1
                        hale.neste = newNode;
                        //newNode1->forrige vil være hale
                        newNode.forrige = hale;
                        //newNode1 blir ny hale
                        hale = newNode;
                        //hales neste peker blir null
                        hale.neste = null;
                    }

                    antall++;
                }
            }

        //Variabelen endringer skal være 0.
        endringer = 0;

        }
    }

    public Liste<T> subliste(int fra, int til) {
        DobbeltLenketListe<T> subliste = new DobbeltLenketListe<>();

        subliste.antall = subliste.endringer = 0;

        fratilKontroll(antall, fra, til);

        for(int i = fra; i < fra + til ; i++){
            subliste.leggInn(finnNode(i).verdi);
            subliste.antall++;
        }

        return subliste;
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

    //returnere antallet verdier i listen
    @Override
    public int antall() {
        return antall;
    }

    //returnere true hvis tom
    @Override
    public boolean tom() {
        if(this.antall == 0){
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean leggInn(T verdi) {
        //Objects.requireNonNull(s)
        Node<T> newNode = new Node<>(Objects.requireNonNull(verdi));

        if (antall == 0){          //tom liste
            hode = hale = newNode;  //hode og hale er den nye noden
            newNode.forrige = null;
            newNode.neste = null;   //den nye noden er eneste node og har ingen neste eller forrige peker
        } else {
            hale.neste = newNode;   //hale->neste blir ny node
            newNode.forrige = hale; //newNode->forrige blir opprinnelig hale
            newNode.neste = null;   //siden newNode er siste har den null-peker neste
            hale = newNode;         //ny hale er nå den nye noden
        }

        //antallet må økes etter innlegging, det samme med endringer.
        endringer++; antall++;

        return true;
    }

    @Override
    public void leggInn(int indeks, T verdi) {
        indeksKontroll(indeks, false);
        /*
        1) listen er tom,
        2) verdien skal legges først,
        3) verdien skal legges bakerst og
        4) verdien skal legges mellom to andre verdier.
        */

        if (antall == 0){
            leggInn(verdi);
        } else if(indeks == 0){
            Node<T> newNode = new Node<>(verdi);

            hode.forrige = newNode;
            newNode.neste = hode;
            newNode.forrige = null;
            hode = newNode;
        } else if(indeks == antall - 1){
            leggInn(verdi);
        } else {
            Node<T> newNode = new Node<>(verdi);

            finnNode(indeks).forrige = newNode;
            newNode.neste = finnNode(indeks);
            newNode.forrige = finnNode(indeks - 1);
            finnNode(indeks - 1).neste = newNode;
        }

        //antallet og endringer må økes etter innlegging.
        endringer++; antall++;
    }

    @Override
    public boolean inneholder(T verdi) {
        if(indeksTil(verdi) == -1){
            return false;
        } else {
            return true;
        }
    }

    @Override
    public T hent(int indeks) {
        indeksKontroll(indeks, false);
        return finnNode(indeks).verdi;
    }

    @Override
    public int indeksTil(T verdi) {
        if(verdi == null){return -1;}

        for(int i = 0 ; i < antall ; i++){
            if(finnNode(i).verdi == verdi){
                return i;
            }
        }

        return -1;
    }

    @Override
    public T oppdater(int indeks, T nyverdi) {
        indeksKontroll(indeks, false);
        T opprinneligVerdi = finnNode(indeks).verdi;
        finnNode(indeks).verdi = Objects.requireNonNull(nyverdi);
        return opprinneligVerdi;
    }

    @Override
    public boolean fjern(T verdi) {
        if(antall == 0) {return false;}

        for(int i = 0 ; i < antall ; i++){

            if(finnNode(i).verdi == verdi){

                if(i == 0){
                    finnNode(i + 1).forrige = null;
                    hode = finnNode(i + 1);
                } else if(i == antall - 1){
                    finnNode(i - 1).neste = null;
                    hale = finnNode(i - 1);
                } else {
                    finnNode(i - 1).neste = finnNode(i + 1);
                    finnNode(i + 1).forrige = finnNode(i - 1);
                }

                //antall reduseres og endringer økes.
                endringer++; antall--;

                return true;
            }
        }

        return false;
    }

    @Override
    public T fjern(int indeks) {
        if(antall == 0){return null;}

        T opprinneligVerdi = finnNode(indeks).verdi;
        /*
        1) den første fjernes,
        2) den siste fjernes og
        3) en verdi mellom to andre fjernes.
         */
        if(indeks == 0){
            finnNode(indeks + 1).forrige = null;
            hode = finnNode(indeks + 1);
        } else if(indeks == antall - 1){
            finnNode(indeks - 1).neste = null;
            hale = finnNode(indeks - 1);
        } else {
            finnNode(indeks - 1).neste = finnNode(indeks + 1);
            finnNode(indeks + 1).forrige = finnNode(indeks - 1);
        }

        //Variabelen antall reduseres og endringer økes.
        endringer++; antall--;

        return opprinneligVerdi;
    }

    @Override
    public void nullstill() {
        //måte 1.
        Node<T> valgtNode = hode;

        while(valgtNode != null){
            valgtNode.neste = valgtNode.forrige = null;
            valgtNode.verdi = null;
            valgtNode = valgtNode.neste;
        }

        endringer++; antall = 0;

        //måte 2

        while(valgtNode != null){
            fjern(0);
            valgtNode = valgtNode.neste;
        }
    }

    @Override
    public String toString() {
        Node<T> skrivNode = hode;
        StringBuilder ut = new StringBuilder();

        ut.append("[");

        while(skrivNode != null){
            ut.append(skrivNode.verdi + "");

            if(skrivNode.neste != null) {
                ut.append(", ");
            }

            skrivNode = skrivNode.neste;
        }

        return ut.toString();
    }

    public String omvendtString() {
        Node<T> skrivNode = hale;
        StringBuilder ut = new StringBuilder();

        ut.append("[");

        while(skrivNode != null){
            ut.append(skrivNode.verdi + "");

            if(skrivNode.forrige != null) {
                ut.append(", ");
            }

            skrivNode = skrivNode.forrige;
        }

        return ut.toString();
    }

    @Override
    public Iterator<T> iterator() {
        throw new UnsupportedOperationException();
    }

    public Iterator<T> iterator(int indeks) {
        throw new UnsupportedOperationException();
    }

    private Node<T> finnNode(int indeks){
        if(antall == 0){return null;}

        Node<T> valgtNode;

        if(indeks < antall / 2){
            valgtNode = hode;
            for (int i = 0 ; i < indeks ; i++){
                valgtNode = valgtNode.neste;
            }
            return valgtNode;
        } else {
            valgtNode = hale;

            //[a,b,c,d,e,f]
            for (int i = antall - 1 ; i > indeks ; i--){
                valgtNode = valgtNode.forrige;
            }
            return valgtNode;
        }
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


