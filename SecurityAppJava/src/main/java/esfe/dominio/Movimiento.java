package esfe.dominio;

public class Movimiento {
    public int id;
    public String tipo;
    public String descripcion;
    public double monto;
    public String fecha;

    public Movimiento(String tipo, String descripcion, double monto, String fecha) {
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.monto = monto;
        this.fecha = fecha;
    }
}
