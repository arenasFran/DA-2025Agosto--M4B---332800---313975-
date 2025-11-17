package obligatorio.obli.models;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Notificacion {
    private static final SimpleDateFormat FORMATTER = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    private static int contadorId = 1;

    private int id;
    private String timestamp;
    private String mensaje;

    public Notificacion(String mensaje) {
        this.id = contadorId++;
        this.timestamp = FORMATTER.format(new Date());
        this.mensaje = mensaje;
    }

    public Notificacion(String mensaje, String timestamp) {
        this.id = contadorId++;
        this.timestamp = timestamp;
        this.mensaje = mensaje;
    }

    public int getId() {
        return id;
    }

    public String gettimestamp() {
        return timestamp;
    }

    public String getMensaje() {
        return mensaje;
    }

    public String getMensajeCompleto() {
        return String.format("[%s] %s", timestamp, mensaje);
    }

    @Override
    public String toString() {
        return getMensajeCompleto();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Notificacion that = (Notificacion) o;
        return id == that.id;
    }
}
