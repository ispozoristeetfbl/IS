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
public class AdministrativniRadnik extends RadnikKojiKoristiSistem {

    public AdministrativniRadnik(String ime, String prezime, String opisPosla, String jmb, boolean statusRadnika, String kontak) {
        super(ime, prezime, opisPosla, jmb, statusRadnika, kontak);
    }
    
    public AdministrativniRadnik(Number rs){
        System.err.println("============");
    }
    
   
    
}
