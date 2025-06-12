package esfe.dominio;

public class Categoria {
    private int id_categoria;
    public String nombre;

    public Categoria(int id_categoria, String nombre) {
        this.id_categoria = id_categoria;
        this.nombre = nombre;
    }

    public Categoria(String nombre) {
        this.nombre = nombre;
    }
    // ✅ Método para obtener el ID
    public int getIdCategoria() {
        return id_categoria;
    }
    public int getId_categoria() {
        return id_categoria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
