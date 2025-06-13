package esfe.dominio;



public class Movimiento {
    public int id;
    public String tipo;
    public String descripcion;
    public double monto;
    public String fecha;
    public int id_categoria;
    public String nombreCategoria;
    // NUEVO

    // constructor original
    public Movimiento(String tipo, String descripcion, double monto, String fecha) {
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.monto = monto;
        this.fecha = fecha;

    }


    // nuevo constructor con categoría
    public Movimiento(String tipo, String descripcion, double monto, String fecha, int id_categoria) {
        this(tipo, descripcion, monto, fecha);
        this.id_categoria = id_categoria;



    }
}
