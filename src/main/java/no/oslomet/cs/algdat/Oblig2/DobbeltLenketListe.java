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
        throw new UnsupportedOperationException();
    }

    @Override
    public int antall() {
        return antall;
    }

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
        Objects.requireNonNull(verdi);      //sjekker om verdi er null, om den er det så vil den throwe en exception

        Node <T> nyNode = new Node(verdi);  //setter opp noden til å få verdien til den innlagte verdien

        if(antall == 0){                    //spesiell case om det ikke enda finnes et element i listen
            hode = hale = nyNode;           //deklarer både hode og hale til å være den nye noden
            hode.neste = nyNode;            //Oppgaven ber om at hode og hale skal peke tibake på nyNode
            hale.forrige = nyNode;
            hode.forrige = null;            //hode sin forrige er da null og hale sin neste er null;
            hale.neste = null;
            antall++;                       //øker antall ettersom vi nå har 1 element i lista, men øker ikke endringer ettersom at dette er da en ny liste
        }

        else{                               //i andre cases er det i orden å gjøre det på denne måten
            hale.neste = nyNode;            //deklarerer hale.neste til å være den nyenoden
            nyNode.forrige = hale;          //deretter får vi den nye noden til å peke på hale som forrige
            hale = nyNode;                  //setter så hale sin verdi til å være den nye noden
            hale.neste = null;              //setter tilbake at hale sin neste verdi er null
            antall++;                       //øker antall, ettersom vi nå har enda flere elementer i lista
            endringer++;                    //her endres den ekisterende lista
        }
        return true;
    }

    @Override
    public void leggInn(int indeks, T verdi) {
        throw new UnsupportedOperationException();
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
        throw new UnsupportedOperationException();
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
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean fjern(T verdi) {
        throw new UnsupportedOperationException();
    }

    @Override
    public T fjern(int indeks) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void nullstill() {
        //måte 1 - 12 ms

        if(antall == 0){return;}    //tom tabell er nullstilt

        Node<T> valgtNode = hode;

        while(valgtNode != null){
            valgtNode.verdi = null;
            valgtNode.forrige = null;

            valgtNode = valgtNode.neste;

            if(valgtNode != null){
                valgtNode.forrige.neste = null;
            }
        }

        hode = hale = null;
        endringer++; antall = 0;


        //måte 2 - 14ms
/*
        Node<T> valgtNode1 = hode;

        while(valgtNode1 != null){
            fjern(0);
            valgtNode1 = valgtNode1.neste;
        }
        */
    }

    @Override
    public String toString() {

        StringBuilder utString = new StringBuilder();
        utString.append("[");                           //setter opp starten av strengen

        Node<T> current = hode;                         //starter i posisjonen til hodet. Current er en verdi som
                                                        // forteller hvor vi står i
        for (int i = 0; i < antall; i++) {
            if(i != antall-1){                          //sjekker om vi er i siste element i listen eller ikke
                utString.append(current.verdi+ ", ");
            }
            else{
                utString.append(current.verdi);         // om vi er i siste element, så vil vi ikke legge på ", " på slutten av strengen
            }
            current = current.neste;                    //går videre til neste element
        }
        utString.append("]");                           //avslutter utskriften
        return utString.toString();                     //returnerer en String verdi til toString() funksjonen
    }

    public String omvendtString() {

        StringBuilder utString = new StringBuilder();
        utString.append("[");                           //bygger opp starten av strengen

        Node<T> current = hale;                         //starter i halen

        for (int i = 0; i < antall; i++) {
            if(i != antall-1){                          //samme som i toString(), sjekker at vi ikke er i siste element
                utString.append(current.verdi +", ");
            }
            else{
                utString.append(current.verdi);         //om vi er i siste element legger vi ikke til noe mer enn verdien
            }

            current = current.forrige;                  //går bakover gjennom lista, ved å bruke .forrige
        }

        utString.append("]");                           //avslutten utstrengen
        return utString.toString();
    }

    @Override
    public Iterator<T> iterator() {
        throw new UnsupportedOperationException();
    }

    public Iterator<T> iterator(int indeks) {
        throw new UnsupportedOperationException();
    }

    private Node<T> finnNode(int indeks){ throw new UnsupportedOperationException(); }

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
            while(c.compare(liste.hent(left), pivot) <= 0 && left < right){
                left++;
            }

            while(c.compare(liste.hent(right), pivot) >= 0 && left < right){
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


