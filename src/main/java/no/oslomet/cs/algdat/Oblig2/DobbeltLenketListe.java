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
        return antall; //returnerer verdien av antall fra "DobbeltLenketListe" Dette er for nå alltid 0 for testing
    }

    @Override
    public boolean tom() {
        boolean check=false;  //sjekker om lista er tom ved å sjekke om antall == 0, om den er 0 vil den returnere true
        if(antall == 0){
            check = true;
        }
        return check;
    }

    @Override
    public boolean leggInn(T verdi) { Objects.requireNonNull(verdi);      //sjekker om verdi er null, om den er det så vil den throwe en exception

        Node <T> nyNode = new Node(verdi);  //setter opp noden til å få verdien til den innlagte verdien

        if(antall == 0){                    //spesiell case om det ikke enda finnes et element i listen
            hode = hale = nyNode;           //deklarer både hode og hale til å være den nye noden
            nyNode.forrige = nyNode.neste = null;            //hode sin forrige er da null og hale sin neste er null;
        }

        else{                               //i andre cases er det i orden å gjøre det på denne måten
            hale.neste = nyNode;            //deklarerer hale.neste til å være den nyenoden
            nyNode.forrige = hale;          //deretter får vi den nye noden til å peke på hale som forrige
            hale = nyNode;                  //setter så hale sin verdi til å være den nye noden
            hale.neste = null;              //setter tilbake at hale sin neste verdi er null
        }

        antall++;                       //øker antall, ettersom vi nå har enda flere elementer i lista
        endringer++;        //samme med endringer
        return true; }

    @Override
    public void leggInn(int indeks, T verdi) {
        if(verdi == null){
            throw new NullPointerException("Verdi er null!");
        }
        if(indeks > antall ||indeks < 0){      //sjekker at ingenting skal legges inn utenfor listen
            throw new IndexOutOfBoundsException("indeks er utenfor lista!");
        }

        Node<T> nyNode = new Node(verdi);

        if(antall == 0) { //hvis det ikke finnes noe liste fra før av så skapes den her
            leggInn(verdi);
        }

        else if(indeks == 0){            //om den nye skal legges inn i 0, blir den nytt hode
            Node current = hode;

            nyNode.neste = current;     //nyNode sin neste peker til hode sin verdi
            current.forrige = nyNode;   //hode sin forrige peker til den nye noden
            hode = nyNode;              //nyNode er nå det nye hodet
            hode.forrige = null;
            endringer++; antall++;
        }

        else if(indeks == antall){          //om den nye noden skal legges inn som hale
            /*
            Node current = hale;

            nyNode.forrige = current;       //nyNode forrige blir peker til halen
            current.neste = nyNode;         //halen sin neste peker til nyNode
            hale = nyNode;                  //nyNode blir her til halen
            hale.neste = null;              //halen neste peker er null
            endringer++; antall++;

             */
            leggInn(verdi);
        }

        else if(indeks < antall/2){         //om vi skal starte i hode
            Node current = hode;

            for (int i = 0; i < indeks - 1; i++) {
                current = current.neste;            //finner frem til riktig indeksering
            }

            nyNode.forrige = current;               //nyNode forrige blir til noden vi står i
            nyNode.neste = current.neste;           //nyNode neste blir til den noden vi står i sin neste node
            current.neste = nyNode;                 //peker nå til at den vi står i sin neste nå blir den nye noden

            current = nyNode.neste;                 //beveger oss til noden som er etter nyNode
            current.forrige = nyNode;               //erklærer noden vi står i sin forrige node som nyNode
            endringer++; antall++;
        }

        else{
            Node current = hale; // hvis ikke så starter vi i halen

            for (int i = antall-1; i > indeks; i--) {
                current = current.forrige;          //indekserer oss bakfra
            }

            nyNode.neste = current;                 //nyNode blir til den vi står i
            nyNode.forrige = current.forrige;       //nyNode forrige blir til den vi står i sin forrige
            current.forrige = nyNode;               //den vi står i sin forrige blir til nyNode

            current = nyNode.forrige;               //den vi står i blir til nyNode sin forrige node
            current.neste = nyNode;                 //den vi står i sin neste blir til nyNode
            endringer++; antall++;

        }
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
        //måte 1 - 12 ms

        if(antall == 0){return;}    //tom tabell er nullstilt

        Node<T> valgtNode = hode;

        //måte 1.
        while(valgtNode != null){
            valgtNode.neste = valgtNode.forrige = null;
            valgtNode.verdi = null;
            valgtNode = valgtNode.neste;
        }
        valgtNode.forrige = null;

        //måte 2
        /*
        while(valgtNode != null){
            fjern(0);
            valgtNode = valgtNode.neste;
        }

         */
        if(valgtNode != null){
            valgtNode.forrige.neste = null;
        }
    }

    @Override
    public String toString() {
        StringBuilder utString = new StringBuilder();
        utString.append("["); //setter opp starten av strengen

        Node<T> current = hode; //starter i posisjonen til hodet. Current er en verdi som forteller hvor vi står i

        for (int i = 0; i < antall; i++) {
            if(i != antall-1){ //sjekker om vi er i siste element i listen eller ikke
                utString.append(current.verdi+ ", ");
            }
            else{
                utString.append(current.verdi); // om vi er i siste element, så vil vi ikke legge på ", " på slutten av strengen
            }
            current = current.neste; //går videre til neste element
        }
        utString.append("]"); //avslutter utskriften
        return utString.toString(); //returnerer en String verdi til toString() funksjonen
    }

    public String omvendtString() {
        StringBuilder utString = new StringBuilder();
        utString.append("["); //bygger opp starten av strengen

        Node<T> current = hale; //starter i halen

        for (int i = 0; i < antall; i++) {
            if(i != antall-1){ //samme som i toString(), sjekker at vi ikke er i siste element
                utString.append(current.verdi +", ");
            }
            else{
                utString.append(current.verdi); //om vi er i siste element legger vi ikke til noe mer enn verdien
            }

            current = current.forrige; //går bakover gjennom lista, ved å bruke .forrige
        }

        utString.append("]"); //avslutten utstrengen
        return utString.toString();
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


