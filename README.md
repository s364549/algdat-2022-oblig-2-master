# Obligatorisk oppgave 2 i Algoritmer og Datastrukturer

Denne oppgaven er en innlevering i Algoritmer og Datastrukturer. 
Oppgaven er levert av følgende studenter:
* Dahlstrøm, Thor-Kristian, s360745, s360745@oslomet.no
* Kristiansen, Thomas, s364532, s364532@oslomet.no
* Dinh, Daniel, s364549, s364549@oslomet.no

# Arbeidsfordeling

I oppgaven har vi hatt følgende arbeidsfordeling:
* Daniel har hatt hovedansvar for oppgave 1, 4, 7, og 10. 
* Thor-Kristian har hatt hovedansvar for oppgave 2, 5, og 8. 
* Thomas har hatt hovedansvar for oppgave 3, 6 og 9.

# Oppgavebeskrivelse

1 -
int antall()
Her gikk vi frem ved å skrive én return-løsning, som returnerer variabelen antall, 
som holder rede på hvor mange noder den dobbeltlenkede listen inneholder.

boolean tom() 
Her gikk vi frem ved å kontrollere om antall er lik 0, og returnerer i så fall true, og false ellers.

public DobbeltLenketListe(T[] a) {}
Vi begynner metoden ved å kaste unntak hvis argumentet inneholder et null-array. 
Vi itererer gjennom hele arrayet og oppretter ny node hvis plassen inneholder en ikke-null verdi.
Hvis den nye noden er den første hittil (hode == null), settes den som hode og hale, og dens forrige- og neste-
pekere blir null. Ellers settes noden på slutten, tidligere hale sin neste-peker blir ny node, ny nodes forrige-
peker blir tidligere hale, ny hale blir ny node og ny nodes neste-peker blir null.

2a -
String toString()
Her bruker vi en StringBuilder-instanse kalt ut og starter den med startbraketter, så iterer vi så lenge valgt node 
ikke er null, for i så fall har vi iterert gjennom hele listen. Hver iterasjon legger den valgte nodens verdi inn i ut, 
og så lenge vi ikke er på siste element i listen legges det til komma og mellomrom. 
Deretter endres den valgte noden til sin neste-pekers node. Til slutt returneres StringBuilder-objektet med sin toString-metode.

String omvendtString()
Denne metoden implementerte vi med samme fremgangsmåte, men vi traverserer i motsatt retning, fra halens posisjon.

2b - 
boolean leggInn(T verdi)
Her gjør vi først en null-sjekk med Objects.requireNonNull(), for i så fall er det ingen verdi å legge inn. 
Hvis antall er null, vil ny node være den første hittil og settes til både hode og hale, med forrige- og neste-
pekerne satt til null.
Ellers settes noden på slutten, tidligere hale sin neste-peker blir ny node, ny nodes forrige-
peker blir tidligere hale, ny hale blir ny node og ny nodes neste-peker blir null.

3a - 
Node<T> finnNode(int indeks) 
Her traverserer metoden listen med for-løkke fra den halvdelen hvor indeksplassen ligger. 
Hvis indeksen er mindre enn antall / 2, begynner vi ved hode og traverserer til indeks.
Ellers traverserer vi fra hale bakover, også til indeks. Til slutt returneres noden på den valgt plassen.

T oppdater(int indeks, T nyverdi) 
Her begynner vi med null-sjekk med Objects.requireNonNull(), deretter sjekk av gyldige indekser med indeksKontroll().
Vi lagrer gammel verdi i en variabel av generisk type. Deretter endres noden på indeksen sin verdi til ny verdi, og endringer inkrementeres.
Den opprinnelige verdien retureres.

public T hent(int indeks) 
Metoden returnerer noden med indeksen oppgitt ved hjelp av finnNode(indeks) etter at indeksen er kontrollert som gyldig.

3b - 
Liste<T> subliste(int fra, int til)
Her oppretter vi ny instanse av et DobbeltLenketListe<T>-objekt, med antall og endringer satt til null.
Så kontrollerer vi intervallet med fratilKontroll() og itererer med for-løkke [fra -> til>. 
I hver av løkkens iterasjon legges den aktuelle opprinnelige nodens verdi på slutten av subliste, og antall økes.
Til slutt returneres subliste.

4 - 
int indeksTil(T verdi) 
Her itererer en for-løkke gjennom listen og sjekker om den aktuelle noden er lik verdien i argumentet.
I så fall returneres i, ellers returneres -1, fordi verdien ikke ble funnet.

boolean inneholder(T verdi)
Her brukes indeksTil(). Hvis den returnerer -1, vet vi at verdien ikke er inneholdt og returnerer false.
Alle andre returnerte verdier indikerer at verdien er inneholdt, og da returnerer vi true.

5 - 
void leggInn(int indeks, T verdi)
Metoden begynner med null-sjekk og indekssjekk for indeks > antall og indeks < 0, og kaster unntak hvis de ikke passerer.
Her tar vi høyde for de etterspurte tilfellene i oppgaven:
i) listen er tom (antall == 0)
    Metoden kaller leggInn() med oppgitt verdi som argument.
ii) verdien skal legges først (indeks == 0)
    nyNode sin neste-peker blir opprinnelig hode,
    hode sin forrige-peker blir til den nye noden,
    den nye noden settes til hode, med forrige peker satt til null, og 
    endringer og antall inkrementerer.
iii) verdien skal legges bakerst (indeks == antall)
    Vi kaller leggInn() med verdi som argument, fordi metoden automatisk legger den nye noden bakerst.
iv) verdien skal legges mellom to andre verdier
    For å effektivisere traverserer vi fra nærmeste endepunkt.
    Ny node sin forrigepeker blir satt til valgt node,
    ny node sin neste-peker blir satt til valgt node sin neste, og
    valgt node sin neste-peker blir satt til ny node.
    Vi beveger oss deretter til noden etter ny node, og setter dens forrige-peker 
    til ny node. Avslutningsvis inkrementerer endringer og antall.

6 - 
T fjern(int indeks) 
Hvis listen er tom (antall == 0) returneres null. Hvis det er en node i listen, settes hode og hale til null.
Metoden vår tar også høyde for de etterspurte tilfellene i oppgaven:
i)den første fjernes (indeks == 0)
    Hode settes til sin neste node, og det nye hodets forrige-peker blir satt til null.
ii) den siste fjernes (indeks+1 == antall)
    Vi velger siste node og oppretter hjelpenode for nest siste node. 
    Hjelpenodens neste-peker blir null,
    halen, som skal fjernes, sin forrige-peker, og 
    hjelpenoden settes som ned nye halen.
iii) en verdi mellom to andre fjernes
    For å effektivisere traverserer vi fra nærmeste endepunkt.
    Vi oppretter deretter en hjelpenode for både current.neste og current.forrige.
    Så har vi gjort slik at de to nodene rundt valgt node, peker forbi valgt node, som nå er glemt.
    Antall reduseres, endringer inkrementeres.

boolean fjern(T verdi).
Her bruker vi en teller, som med en while-løkke blir satt til indeksen hvor den etterspurte verdien finnes.
Hvis valgt node ender opp med å være null, har ikke verdien blitt funnet, og vi returnerer false.
i)den første fjernes (indeks == 0)
    Hode settes til sin neste node, og det nye hodets forrige-peker blir satt til null.
ii) den siste fjernes (indeks+1 == antall)
    Vi velger siste node og oppretter hjelpenode for nest siste node.
    Hjelpenodens neste-peker blir null,
    halen, som skal fjernes, sin forrige-peker, og
    hjelpenoden settes som ned nye halen.
iii) en verdi mellom to andre fjernes
    For å effektivisere traverserer vi fra nærmeste endepunkt.
    Vi oppretter deretter en hjelpenode for både current.neste og current.forrige.
    Så har vi gjort slik at de to nodene rundt valgt node, peker forbi valgt node, som nå er glemt.
    Antall reduseres, endringer inkrementeres.

7 - 
void nullstill(), velg den beste av to måter
I fremgangsmåte 1 lagrer opprinnelig antall i variabelen teller.
Vi bruker to hjelpenodeobjekter kalt current og hjelpeNode.
For hver iterasjon, nuller vi current sin verdi.
Hjelpenoden blir satt til current.neste, og får deretter sin forrige-peker satt til null.
Current får sin neste-peker satt til null. Current settes avslutningsvis til hjelpenode, 
som er noden etterpå, endringer inkrementeres og antall reduseres.

Når vi befinner oss på siste node i listen (i == teller-1), settes hode og hale til null;
på det tidspunktet vet vi at det ikke lenger finnes pekere til noder i listen.

8 - DobbeltLenketListeIterator
8a - 
T next()
Metoden starte med kontroll av hvis endringer ikke er lik iteratorendringer, og av hvorvidt 
denne har neste (hasNext()). Kontrollene kaster unntak hvis ikke passert.
Metoden returnerer denne.verdi, setter fjernOK til true og setter denne til denne.neste.

8b - 
Iterator<T> iterator()
Metoden vår ppretter en instanse av DobbeltLenketListeIterator og returnerer den på neste linje.

8c - 
private DobbeltLenketListeIterator(int indeks)
Her begynner metoden vår med indeksKontroll().
Konstruktøren vår setter denne til noden på plassen til den oppgitte indeksen,
setter instansens fjernOK til false, og setter iteratorendringer til endringer.

8d - 
Iterator<T> iterator(int indeks)
Her begynner metoden vår med indeksKontroll(). Deretter opprettes en instanse av DobbeltLenketListeIterator, 
med indeks som konstruktørargument, og dette objektet returneres.

9 - 
void remove()
Metoden vår sjekker først at frenOK er true, og at endringer er lik iteratorendringer, og kaster ellers unntak.
Metoden tar høyde for det etterspurte tilfellene i oppgaven:
i) Hvis den som skal fjernes er eneste verdi (antall == 1), så nulles hode og hale.
ii) Hvis den siste skal fjernes (denne == null), så må hale oppdateres.
    halen settes til noden som kommer før, og den nye halen.neste blir til null.
iii) Hvis den første skal fjernes (denne.forrige == hode), så må hode oppdateres.
    Hode settes til noden etterpå, og det nye hodets forrige-peker blir til null.
iv) Hvis en node inne i listen skal fjernes (noden denne.forrige), så må pekerne i nodene på hver side oppdateres.
    Pekeren, fra noden to plasser før, til sin neste node, settes til denne, som da er noden etter den som skal fjernes.
    Forrige-pekeren til noden etter den som skal fjernes settes til noden to plasser før.
    Antall reduseres og endringer & iteratorendringer inkrementeres.

10 - 
public static <T> void sorter(Liste<T> liste, Comparator<? super T> c)
Metoden vår kaller på en quicksortalgoritmemetode, med listen, dens hele intervall 
og en comparator som argument.
Algoritmen partisjonerer ved hjelp av comparatoren, og kaller seg rekursivt.
Når to noders verdier skal byttes om, brukes swap-metoden, som kaller liste.oppdater 
for å sette de to verdiene til å bli hverandre.

