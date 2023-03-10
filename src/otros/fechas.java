package otros;

import java.text.SimpleDateFormat;
import java.util.Date;

public class fechas {
    public static Date obtener_fecha(){
        Date hoy = new Date();
        return hoy;
    }
    public static String transformar_fecha(Date fecha){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String nueva_fecha = sdf.format(fecha);
        return nueva_fecha;
    }
}
