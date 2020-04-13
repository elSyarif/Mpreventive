package monitoring.com.mpreventive;

public class Server {

    public static final String URL = "http://192.168.100.3/M-preventive/api/";
//    public static final String URL = "https://mpreventive2.000webhostapp.com/api/";

//    public static final String URL_Login = "https://mpreventive.000webhostapp.com/api/login.php";

//    public static final String URL_getMesin = "https://mpreventive.000webhostapp.com/api/mesin/select_all.php";
//    public static final String URL_getHistory = "https://mpreventive.000webhostapp.com/api/history/get_data.php";
//    public static final String URL_getJadwal = "https://mpreventive.000webhostapp.com/api/jadwal/get_jadwal.php";
//    public static final String URL_cariJadwal = "https://mpreventive.000webhostapp.com/api/jadwal/cari_data.php";
//    public static final String URL_Inspeksi = "https://mpreventive.000webhostapp.com/api/Laporan/Tambah_Inspeksi.php";
//    public static final String URL_Cek = "https://mpreventive.000webhostapp.com/api/Laporan/cek_data.php";
//    public static final String URL_Chat_ambil = "https://mpreventive.000webhostapp.com/api/chat/ambil_pesan.php";
//    public static final String URL_Chat_Post = "https://mpreventive.000webhostapp.com/api/chat/tambah_chat.php";


    public static final String URL_Login = URL+"login.php";
    public static final String URL_getMesin = URL+"mesin/select_all.php";
    public static final String URL_getHistory = URL+"history/get_data.php";
    public static final String URL_getJadwal = URL+"jadwal/get_jadwal.php";
    public static final String URL_cariJadwal = URL+"jadwal/cari_data.php";
    public static final String URL_Inspeksi = URL+"Laporan/Tambah_Inspeksi.php";
    public static final String URL_Cek = URL+"Laporan/cek_data.php";
    public static final String URL_Chat_ambil = URL+"chat/ambil_pesan.php";
    public static final String URL_Chat_Post = URL+"chat/tambah_chat.php";

    //Dibawah ini merupakan Kunci yang akan di gunakan untuk mengirim permintaan ke Skrip PHP
    public static final String KEY_EMP_ID = "Id";
    public static final String KEY_EMP_ID_MESIN = "id_mesin";
    public static final String KEY_EMP_ID_USER = "id_user";
    public static final String KEY_EMP_TGL_INS = "tgl_inspeksi";
    public static final String KEY_EMP_OV_H = "drive_end_OV_H";
    public static final String KEY_EMP_OV_V = "drive_end_OV_V";
    public static final String KEY_EMP_BV_H = "drive_end_BV_H";
    public static final String KEY_EMP_BV_V = "drive_end_BV_V";
    public static final String KEY_EMP_TEMP_DRIVE = "temperatur_Drive";
    public static final String KEY_EMP_NON_OV_H = "non_drive_end_OV_H";
    public static final String KEY_EMP_NON_OV_V = "non_drive_end_OV_V";
    public static final String KEY_EMP_NON_BV_H = "non_drive_end_BV_H";
    public static final String KEY_EMP_NON_BV_V = "non_drive_end_BV_V";
    public static final String KEY_EMP_TEMP_NON_DRIVE = "temperatur_Non_Drive";
    public static final String KEY_EMP_OIL ="oil_seil";
    public static final String KEY_EMP_OIL_KONDISI ="oil_status";

    public static final String KEY_ID_PENGIRIM ="id_pengirim";
    public static final String KEY_PESAN ="pesan";

    //JSON Tags
    public static final String TAG_JSON_ARRAY="result";
    public static final String TAG_ID_MESIN = "id_mesin";
    public static final String TAG_ID_USER = "id_user";
    public static final String TAG_TGL_INS = "tgl_inspeksi";
    public static final String TAG_OV_H = "drive_end_OV_H";
    public static final String TAG_OV_V = "drive_end_OV_V";
    public static final String TAG_BV_H = "drive_end_BV_H";
    public static final String TAG_BV_V = "drive_end_BV_V";
    public static final String TAG_TEMP_DRIVE = "temperatur_Drive";
    public static final String TAG_NON_OV_H = "non_drive_end_OV_H";
    public static final String TAG_NON_OV_V = "non_drive_end_OV_V";
    public static final String TAG_NON_BV_H = "non_drive_end_BV_H";
    public static final String TAG_NON_BV_V = "non_drive_end_BV_V";
    public static final String TAG_TEMP_NON_DRIVE = "temperatur_Non_Drive";
    public static final String TAG_OIL ="oil_seil";

}
