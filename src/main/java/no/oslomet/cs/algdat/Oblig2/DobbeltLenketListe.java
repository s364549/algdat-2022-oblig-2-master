package no.oslomet.cs.algdat.Oblig2;


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

    private Node<T> finnNode(int indeks) {
        Node<T> node;

        if (indeks < antall / 2) {               // om indeksen er lavere eller lik halvparten av antall starter det ved hode
            node = hode;
            for (int i = 0; i < indeks; i++) {    // for løkke som går i gjennom listen
                node = node.neste;
            }
        } else {                                // om indeksen er større enn halvparten av antall starter det ved halen
            node = hale;
            for (int i = antall - 1; i > indeks; i--) { // for løkke som går gjennom listen fra halen
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
                    ("til(" + til + ") > antall(" + antall + ")");

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

            nyNode.neste = current;     //nyNode sin neste peker blir til hode sin verdi
            current.forrige = nyNode;   //hode sin forrige peker til den nye noden
            hode = nyNode;              //nyNode er nå det nye hodet
            hode.forrige = null;
            endringer++; antall++;
        }

        else if(indeks == antall){          //om den nye noden skal legges inn som hale
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
        if(indeksTil(verdi) == -1){ //dette betyr at verdien ikke finnes i listen
            return false;
        } else {
            return true;
        }
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
        endringer++;
        return gammelverdi; // returnerer gammelverdi

    }

    @Override
    public boolean fjern(T verdi) {

        if(verdi == null){
            return false;
        }

        int teller = 0;         //teller som hjelpevariabel
        Node<T> current = hode;

        while(!current.verdi.equals(verdi) ){ //så sant vi ikke står i riktig verdi fortsetter vi å iterere
            current = current.neste;
            teller++;
            if(current == null){
                return false;
            }
        }
        if(antall == 0){
            return false;
        }
        else if(antall == 1){ //om det kun er igjen et element så kan vi sette hode og hale til null

            hode = hale = null;
        }
        else if(teller+1 == antall){  //om teller+1 = antall står vi i halen og vil dermed fjerne den
            current = hale;
            Node<T> hjelpeNode = current.forrige;

            hjelpeNode.neste = null;
            hale.forrige = null;
            hale = hjelpeNode;
        }
        else if(teller == 0){ //om teller er 0 så står vi i hodet og vil dermed fjerne den
            hode = hode.neste;
            hode.forrige = null;
        }
        else if(teller < antall/2){ //sjekker om vi skal begynne i hode eller i halen gjennom lista
            current = hode;
            for (int i = 0; i < teller; i++) {
                current = current.neste;

            }

            Node<T> hjelpeNodeNeste = current.neste;    //hjelpeNode som referer til neste verdi i lista
            Node<T> hjelpeNodeForrige = current.forrige;//hjelpeNode som referer til forrige verdi i lista
            hjelpeNodeForrige.neste = hjelpeNodeNeste;  //setter riktige verdier
            hjelpeNodeNeste.forrige = hjelpeNodeForrige;//setter riktige verdier

        }
        else{
            current = hale; //starter bakerst
            for (int i = antall-1; i > teller; i--) {
                current = current.forrige;

            }

            Node<T> hjelpeNodeNeste = current.neste;
            Node<T> hjelpeNodeForrige = current.forrige;
            hjelpeNodeForrige.neste = hjelpeNodeNeste;
            hjelpeNodeNeste.forrige = hjelpeNodeForrige;

        }
        antall--; endringer ++;
        return true;
    }

    @Override
    public T fjern(int indeks) {

        indeksKontroll(indeks, false);
        if(indeks < 0){
            throw new NullPointerException("out of bounds");
        }

        else{
            T returVerdi = finnNode(indeks).verdi; //skal returnere verdien til noden av indeks
            Node<T> current;
            //ellers fungerer denne metoden meget likt som fjern(T)
            if(antall == 0){
                return null;
            }
            else if(antall == 1){
                hale = hode = null;
            }
            else if(indeks+1 == antall){
                current = hale;
                Node<T> hjelpeNode = current.forrige;

                hjelpeNode.neste = null;
                hale.forrige = null;
                hale = hjelpeNode;
            }
            else if(indeks == 0){
                hode = hode.neste;
                hode.forrige = null;
            }
            else if(indeks < antall/2){
                current = hode;
                for (int i = 0; i < indeks; i++) {
                    current = current.neste;

                }

                Node<T> hjelpeNodeNeste = current.neste;
                Node<T> hjelpeNodeForrige = current.forrige;
                hjelpeNodeForrige.neste = hjelpeNodeNeste;
                hjelpeNodeNeste.forrige = hjelpeNodeForrige;

            }
            else{
                current = hale;
                for (int i = antall-1; i > indeks; i--) {
                    current = current.forrige;

                }

                Node<T> hjelpeNodeNeste = current.neste;
                Node<T> hjelpeNodeForrige = current.forrige;
                hjelpeNodeForrige.neste = hjelpeNodeNeste;
                hjelpeNodeNeste.forrige = hjelpeNodeForrige;

            }
            antall--; endringer ++;
            return returVerdi;
        }
    }

    @Override
    public void nullstill() {

        //måte 1 - 14ms
        if(antall != 0){ //sjekker at vi ikke har en tom liste

            Node<T> current = hode;
            Node<T> hjelpeNode;

            int teller = antall;
            for (int i = 0; i < teller; i++) {
                if(i == teller-1){
                    hode = hale = null;
                }else{
                    current.verdi = null;
                    hjelpeNode = current.neste;
                    hjelpeNode.forrige = null;
                    current.neste = null;
                    current = hjelpeNode;
                }

            endringer++; antall--;
            }
        }

        //måte 2 - 15ms

        int teller = antall;
        for (int i = 0; i < teller; i++) {
            fjern(0);
        }
    }

    @Override
    public String toString() {
        Node<T> skrivNode = hode;
        StringBuilder ut = new StringBuilder();

        ut.append("[");

        while(skrivNode != null){   //så lenge den valgte node ikke er null
            ut.append(skrivNode.verdi);

            if(skrivNode.neste != null) {   //hvis valgt el. ikke er siste
                ut.append(", ");            //så printer vi komma
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
            ut.append(skrivNode.verdi);

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
        DobbeltLenketListeIterator iterator = new DobbeltLenketListeIterator(); //lager en ny DobbeltLenkeListeIterator
        return iterator;                                                        //og returnerer den
    }

    public Iterator<T> iterator(int indeks) {
        indeksKontroll(indeks, false); //sjekker at indeks er lovlig
        DobbeltLenketListeIterator iterator = new DobbeltLenketListeIterator(indeks);  //samme som i iterator()
        return iterator;
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
            indeksKontroll(indeks, false);
            denne = finnNode(indeks);                       //setter denne til å være hode
            fjernOK = false;                    //setter fjernOK til false, denne blir true gjennom Next
            iteratorendringer = endringer;      //teller endringer
        }

        @Override
        public boolean hasNext() {
            return denne != null;
        }

        @Override
        public T next() {
            if(endringer != iteratorendringer){   // sjekker at antall endringer stemmer
                throw new ConcurrentModificationException();
            }
            if(!hasNext()){                       //sjekker at variablen "denne" ikke er null;
                throw new NoSuchElementException();
            }
            T utVerdi = denne.verdi; //verdi som skal returneres settes
            fjernOK = true;
            denne = denne.neste;     //denne flyttes til neste node
            return utVerdi;
        }

        @Override
        public void remove() {
            if (!fjernOK) {
                throw new IllegalStateException("Ugyldig tilstand");//melding som blir gitt om det ikke er tilatt å kalle metoden
            }

            if (endringer != iteratorendringer) { // sjekker om endringer og iteratorendringer er forskjellige
                throw new ConcurrentModificationException();
            }

            fjernOK = false; // setter fjernOK til false om den gikk gjennom begge if setningene

            if(antall == 1){ //hvis vi bare har et element i lista, så fjernes det
                hale = hode = null;
            }
            else if(denne == null){ //om denne = null betyr det at vi står i halen
                hale = hale.forrige;
                hale.neste = null;
            }
            else if(denne.forrige.forrige == null){ //hvis denne.forrige.forrige = null så vet vi at hodet skal fjernes
                hode = denne;
                hode.forrige = null;
            }
            else{ //base-case 
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


