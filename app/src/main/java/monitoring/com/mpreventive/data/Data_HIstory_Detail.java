package monitoring.com.mpreventive.data;

public class Data_HIstory_Detail {
    private String id_meisn, tgl_inspeksi, ov_H, ov_V, bv_H, bv_V, non_ov_H, non_ov_V, non_bv_H, non_bv_V;

    public Data_HIstory_Detail() {
    }

    public Data_HIstory_Detail(String id_meisn, String tgl_inspeksi, String ov_H, String ov_V, String bv_H, String bv_V,
                               String non_ov_H, String non_ov_V, String non_bv_H, String non_bv_V) {

        this.id_meisn = id_meisn;
        this.tgl_inspeksi = tgl_inspeksi;
        this.ov_H = ov_H;
        this.ov_V = ov_V;
        this.bv_H = bv_H;
        this.bv_V = bv_V;

        this.non_ov_H = non_ov_H;
        this.non_ov_V = non_ov_V;
        this.non_bv_H = non_bv_H;
        this.non_bv_V = non_bv_V;
    }

    public String getId_meisn() {
        return id_meisn;
    }

    public void setId_meisn(String id_meisn) {
        this.id_meisn = id_meisn;
    }

    public String getTgl_inspeksi() {
        return tgl_inspeksi;
    }

    public void setTgl_inspeksi(String tgl_inspeksi) {
        this.tgl_inspeksi = tgl_inspeksi;
    }

    public String getOv_H() {
        return ov_H;
    }

    public void setOv_H(String ov_H) {
        this.ov_H = ov_H;
    }

    public String getOv_V() {
        return ov_V;
    }

    public void setOv_V(String ov_V) {
        this.ov_V = ov_V;
    }

    public String getBv_H() {
        return bv_H;
    }

    public void setBv_H(String bv_H) {
        this.bv_H = bv_H;
    }

    public String getBv_V() {
        return bv_V;
    }

    public void setBv_V(String bv_V) {
        this.bv_V = bv_V;
    }

    public String getNon_ov_H() {
        return non_ov_H;
    }

    public void setNon_ov_H(String non_ov_H) {
        this.non_ov_H = non_ov_H;
    }

    public String getNon_ov_V() {
        return non_ov_V;
    }

    public void setNon_ov_V(String non_ov_V) {
        this.non_ov_V = non_ov_V;
    }

    public String getNon_bv_H() {
        return non_bv_H;
    }

    public void setNon_bv_H(String non_bv_H) {
        this.non_bv_H = non_bv_H;
    }

    public String getNon_bv_V() {
        return non_bv_V;
    }

    public void setNon_bv_V(String non_bv_V) {
        this.non_bv_V = non_bv_V;
    }
}
