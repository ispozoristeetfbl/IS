/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.etfbl.is.pozoriste.model.dto;

/**
 *
 * @author Darko
 */
public class Predstava {
    
    private String naziv;
    private String opis;
    private String tip;
    
    public Predstava(String naziv, String opis, String tip){
        this.naziv=naziv;
        this.opis=opis;
        this.tip=tip;
    }
    
    public void setNaziv(String naziv){
        this.naziv=naziv;
    }
    
    public String getNaziv(){
        return naziv;
    }
    
    public void setOpis(String opis){
        this.opis=opis;
    }
    
    public String getOpis(){
        return opis;
    }
    
    public void setTip(String tip){
        this.tip=tip;
    }
    
    public String getTip(){
        return tip;
    }
    
}
