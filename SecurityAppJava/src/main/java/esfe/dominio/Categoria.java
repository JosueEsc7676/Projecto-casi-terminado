package esfe.dominio;

public class Categoria {
    // ID único de la categoría
    private int id_categoria;

    // Nombre de la categoría
    public String nombre;

    // Constructor con ID (por ejemplo, al leer desde la base de datos)
    public Categoria(int id_categoria, String nombre) {
        this.id_categoria = id_categoria;
        this.nombre = nombre;
    }

    // Constructor sin ID (por ejemplo, al crear una nueva categoría)
    public Categoria(String nombre) {
        this.nombre = nombre;
    }

    // Obtener ID de la categoría
    public int getIdCategoria() {
        return id_categoria;
    }

    public int getId_categoria() {
        return id_categoria;
    }

    // Obtener nombre
    public String getNombre() {
        return nombre;
    }

    // Cambiar nombre
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    // Representación en texto (útil para listas desplegables)
    @Override
    public String toString() {
        return nombre;
    }
}
