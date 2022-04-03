package main;

import com.google.common.collect.HashMultiset;

public class Zugriffsdialog {

    HashMultiset<Sammelbild> sammelBilder = HashMultiset.create();


    public void nutzungsdialog() {
        int eingabe = -1;
        while (eingabe != 0) {
            System.out.println(
                    "---------------\n"
                    + " (0) Programm beenden\n"
                    + " (1) Sammelbild hinzufuegen\n"
                    + " (2) Sammelbilder herausgeben\n"
                    + " (3) Nummern einer Serie zeigen\n"
                    + " (4) Gesamtbestand anzeigen:");
            eingabe = Eingabe.leseInt();
            switch (eingabe) {
                case 1:
                    this.sammelbildHinzufuegen();
                    break;
                case 2:
                    this.sammelbilderHerausgeben();
                    break;
                case 3:
                    this.serienbilderZeigen();
                    break;
                case 4:
                    this.gesamtbestand();
                    break;
            }
        }
    }

    public void gesamtbestand() {

        System.out.println("Bilder:" + sammelBilder);
    }

    public void sammelbildHinzufuegen() {
        System.out.print("Welche Serie? "); 
        String serie = Eingabe.leseString();
        System.out.print("Welche Nummer? " ); 
        int nr = Eingabe.leseInt();

        sammelBilder.add(new Sammelbild(serie,nr));
        
    }

    public void sammelbilderHerausgeben() {
        System.out.print("Welche Serie? "); 
        String serie = Eingabe.leseString();
        System.out.print("Welche Nummer? " ); 
        int nr = Eingabe.leseInt();
        int anzahl = 0;
        while (anzahl < 1) {
            System.out.print("Wieviele? ");
            anzahl = Eingabe.leseInt();
        }

        Sammelbild temp = new Sammelbild(serie,nr);

        if(sammelBilder.contains(temp) && sammelBilder.count(temp) >= anzahl){
            sammelBilder.remove(temp,anzahl);
            System.out.println("Sammelbild gelöscht");
        }else{
            System.out.println("Sammelbild nicht gefunden oder zu wenig Exemplare");
        }


        
    }

    public void serienbilderZeigen() {
        System.out.print("Welche Serie? "); 
        String serie = Eingabe.leseString();

        HashMultiset<Integer> einzelneSerie = HashMultiset.create();

        for (Sammelbild sammelbild : sammelBilder) {
            if(sammelbild.getBildSerie().equals(serie)){
                einzelneSerie.add(sammelbild.getBildNr());
            }

        }
        System.out.println(einzelneSerie);
    }
}
