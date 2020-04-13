package monitoring.com.mpreventive.data;

public class Data_History {
    private String id_mesin, no_mesin, nama_mesin, area, tanggal, gambaran;

    public Data_History() {
    }

    public Data_History(String id_mesin, String no_mesin, String nama_mesin, String area, String tanggal, String gambaran) {
        this.id_mesin = id_mesin;
        this.no_mesin = no_mesin;
        this.nama_mesin = nama_mesin;
        this.area = area;
        this.tanggal = tanggal;
        this.gambaran = gambaran;

    }

    public String getId() {
        return id_mesin;
    }

    public void setId(String id) {
        this.id_mesin = id;
    }

    public String getNo_mesin() {
        return no_mesin;
    }

    public void setNo_mesin(String no_mesin) {
        this.no_mesin = no_mesin;
    }

    public String getNama_mesin() {
        return nama_mesin;
    }

    public void setNama_mesin(String nama_mesin) {
        this.nama_mesin = nama_mesin;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getTanggal() {
        return tanggal;
    }
    public void setTanggal(String tanggal){
        this.tanggal = tanggal;
    }

    public String getGambaran() {
        return gambaran;
    }

    public void setGambaran(String gambaran) {
        this.gambaran = gambaran;
    }
}
