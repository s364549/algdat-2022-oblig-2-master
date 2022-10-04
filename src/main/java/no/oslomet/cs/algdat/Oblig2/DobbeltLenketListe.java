package no.oslomet.cs.algdat.Oblig2;
//KLADD-----------

////////////////// class DobbeltLenketListe //////////////////////////////

import java.util.*;

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

        if(a.length != 0){  //hvis a ikke er tomt array

        antall = 0;

            for(int i = 0 ; i < a.length ; i++) {   //iterererer gjennom hvert array-elem

                if(a[i] != null) {  //lager ikke node for null-verdier i a

                    Node<T> newNode = new Node<>(a[i]); //opprett instanse av Node<T> med verdi fra i-plass i a

                    if (hode == null) {                 //hvis hode ikke er laget hittill
                        hode = hale = newNode;          //er den nye noden den eneste og
                                                        //  derfor både hale og hode
                        hode.forrige = null;            //hode->forrige vil være null
                        hale.neste = null;              //hale->neste vil være null

                        //[]
                        //[<-N->]
                    } else {

                        hale.neste = newNode;           //legg newNode til slutten av listen.
                                                        //opprinnelig hale->neste vil være newNode
                        newNode.forrige = hale;         //newNode->forrige vil være hale
                        hale = newNode;                 //newNode blir ny hale
                        hale.neste = null;              //hales neste peker blir null

                        //[a,b]
                        //[a,b-> N]
                        //[a,b-> <-N]
                        //[a,b-> <-N <-hale]
                        //[a,b-> <-N->]
                    }

                    antall++;
                }
            }

        //Variabelen endringer skal være 0.
        endringer = 0;

        }
    }

    public Liste<T> subliste(int fra, int til) {
        DobbeltLenketListe<T> subliste = new DobbeltLenketListe<>();    //ny instanse som til slutt returneres

        subliste.antall = subliste.endringer = 0;   //antall noder og endringer er 0

        fratilKontroll(antall, fra, til);

        for(int i = fra; i < til ; i++){
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
                    ("til(" + til + ") > antall(" + antall + ")");

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
        if(verdi.equals(null)){throw new IllegalArgumentException();}
        if (indeks < 0 || indeks > antall()) {
            throw new IndexOutOfBoundsException(melding(indeks));
        }
        /*
        1) listen er tom,
        2) verdien skal legges først,
        3) verdien skal legges bakerst og
        4) verdien skal legges mellom to andre verdier.
        */

        if (hode == null){
            leggInn(verdi);
        } else if(indeks == 0){
            Node<T> newNode = new Node<>(verdi);

            hode.forrige = newNode;
            newNode.neste = hode;
            newNode.forrige = null;
            hode = newNode;

            antall++;
        } else if(indeks == antall - 1){
            leggInn(verdi);
        } else {
            Node<T> newNode = new Node<>(verdi);

            finnNode(indeks).forrige = newNode;
            newNode.neste = finnNode(indeks);
            newNode.forrige = finnNode(indeks - 1);
            finnNode(indeks - 1).neste = newNode;
            antall++;
        }

        //antallet og endringer må økes etter innlegging.
        endringer++;
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
            if(finnNode(i).verdi.equals(verdi)){
                return i;
            }
        }

        return -1;
    }

    @Override
    public T oppdater(int indeks, T nyverdi) {
        indeksKontroll(indeks, false);
        T opprinneligVerdi = finnNode(indeks).verdi;
        Objects.requireNonNull(nyverdi);
        finnNode(indeks).verdi = nyverdi;
        return opprinneligVerdi;
    }

    @Override
    public boolean fjern(T verdi) {
        if(antall == 0 || verdi.equals(null)) {return false;}

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

        ut.append("]");
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

        ut.append("]");

        return ut.toString();
    }

    @Override
    public Iterator<T> iterator() {
        DobbeltLenketListeIterator iterator = new DobbeltLenketListeIterator();
        return iterator;
    }

    public Iterator<T> iterator(int indeks) {
        indeksKontroll(indeks, false);
        DobbeltLenketListeIterator iterator = new DobbeltLenketListeIterator(indeks);
        return iterator;
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

    //---ITERATOREN---//

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
            denne = finnNode(indeks);
            fjernOK = false;  // blir sann når next() kalles
            iteratorendringer = endringer;  // teller endringer
        }

        @Override
        public boolean hasNext() {
            return denne != null;
        }

        @Override
        public T next() {
            if(iteratorendringer != endringer){ throw new ConcurrentModificationException();}
            if(!hasNext()){throw new NoSuchElementException();}
            fjernOK = true;
            T returverdi = denne.verdi;
            denne = denne.neste;
            return returverdi;
        }

        @Override
        public void remove() {
            //Hvis det ikke er tillatt å kalle metoden, kastes IllegalStateException./
            if(fjernOK = false){throw new IllegalStateException();}

            //Hvis endringer og iteratorendringer er forskjellige, kastes ConcurrentModificationException
            if(endringer != iteratorendringer){throw new ConcurrentModificationException();}

            fjernOK = false;

            if(antall == 1){
                hode = hale = null;
                denne.forrige = null;
            }else if(denne == null){
                hale = hale.forrige;
                hale.neste = null;
            }else if(denne.forrige == hode){
                hode = denne;
                denne.forrige = null;
            }else{
                denne.forrige.forrige.neste = denne;
                denne.forrige = denne.forrige.forrige;
            }

            antall--; endringer++; iteratorendringer++;

        }

    } // class DobbeltLenketListeIterator

    public static <T> void sorter(Liste<T> liste, Comparator<? super T> c) {
        quicksort(liste, 0, liste.antall() - 1, c);
    }

    private static <T> void quicksort(Liste<T> liste, int low, int high, Comparator<? super T> c){
        if(low >= high){
            return;
        }

        T pivot = liste.hent(high);

        int left = low;
        int right = high;

        while(left < right){
            while(c.compare(liste.hent(left), pivot) < 0 && left < right){
                left++;
            }

            while(c.compare(liste.hent(right), pivot) > 0 && left < right){
                right--;
            }
            swap(liste, left, right);
        }

        swap(liste, left, high);

        quicksort(liste, low, left - 1, c);
        quicksort(liste, left + 1, high, c);
    }

    private static <T> void swap(Liste<T> liste, int a, int b){
        T temp = liste.oppdater(a, liste.hent(b));;
        liste.oppdater(b, temp);
    }
} // class DobbeltLenketListe


