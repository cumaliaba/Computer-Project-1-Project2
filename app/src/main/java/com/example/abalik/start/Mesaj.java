package com.example.abalik.start;


public class Mesaj {

    private String gonderen,mesaj,alici;

    public Mesaj(){}

    public Mesaj(String gonderen,String mesaj, String alici) {
        this.gonderen = gonderen;
        this.mesaj = mesaj;
        this.alici = alici;
    }

    public String getGonderen() {return gonderen;
    }

    public void setGonderen(String gonderen) {
        this.gonderen = gonderen;
    }

    public String getMesaj() {
        return mesaj;
    }

    public void setMesaj(String mesaj) {
        this.mesaj = mesaj;
    }

    public String getAlici() {
        return alici;
    }

    public void setAlici(String alici) {
        this.alici = alici;
    }
}
