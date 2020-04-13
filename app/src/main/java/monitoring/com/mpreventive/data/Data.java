package monitoring.com.mpreventive.data;

public class Data {
    private String id_mesin, no_mesin, nama_mesin, area, gambaran;

    public Data() {
    }

    public Data(String id_mesin, String no_mesin, String nama_mesin, String area,String gambaran) {
        this.id_mesin = id_mesin;
        this.no_mesin = no_mesin;
        this.nama_mesin = nama_mesin;
        this.area = area;
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

    public String getGambaran() {
        return gambaran;
    }

    public void setGambaran(String gambaran) {
        this.gambaran = gambaran;
    }
}
