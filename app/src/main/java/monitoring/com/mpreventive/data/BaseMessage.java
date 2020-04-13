package monitoring.com.mpreventive.data;

import com.pusher.client.channel.User;



public class BaseMessage {

    String id,pengirim,pesan,waktu,namaNik;
    public BaseMessage(){

    }
    public BaseMessage(String id,String pengirim, String pesan, String waktu, String namaNik){
        this.id = id;
        this.pengirim = pengirim;
        this.pesan = pesan;
        this.waktu = waktu;
        this.namaNik = namaNik;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPengirim() {
        return pengirim;
    }

    public void setPengirim(String pengirim) {
        this.pengirim = pengirim;
    }

    public String getPesan() {
        return pesan;
    }

    public void setPesan(String pesan) {
        this.pesan = pesan;
    }

    public String getWaktu() {
        return waktu;
    }

    public void setWaktu(String waktu) {
        this.waktu = waktu;
    }

    public String getNamaNik() {
        return namaNik;
    }

    public void setNamaNik(String namaNik) {
        this.namaNik = namaNik;
    }
}
