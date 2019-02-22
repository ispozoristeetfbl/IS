/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.etfbl.is.pozoriste.model.dto;

/**
 *
 * @author djord
 */
public class Umjetnik extends Radnik {

    private String biografija="biografija";
    private String tipRadnika = "Umjetnik";

    public Umjetnik(String ime, String prezime, String opisPosla, String jmb, boolean statusRadnika, String kontak, String biografija) {
        super(ime, prezime, opisPosla, jmb, statusRadnika, kontak);
        this.biografija = biografija;
    }

    public Umjetnik() {
        
    }

    public void setBiografija(String biografija) {
        this.biografija = biografija;
    }

    public String getTipKorisnika() {
        return tipRadnika;
    }

    public void setTipKorisnika(String tipKorisnika) {
        this.tipRadnika = tipKorisnika;
    }
    

    public String getBiografija() {
        return biografija;
    }
    
}
