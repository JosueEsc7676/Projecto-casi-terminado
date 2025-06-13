package esfe.persistencia;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import esfe.dominio.User;
import esfe.utils.PasswordHasher;

public class UserDAO {
    private ConnectionManager conn;
    private PreparedStatement ps;
    private ResultSet rs;

    public UserDAO(){
        conn = ConnectionManager.getInstance();
    }

    public User create(User user) throws SQLException {
        User res = null;
        try{
            PreparedStatement ps = conn.connect().prepareStatement(
                    "INSERT INTO Users (name, passwordHash, email, status) VALUES (?, ?, ?, ?)",
                    java.sql.Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, user.getName());
            ps.setString(2, PasswordHasher.hashPassword(user.getPasswordHash()));
            ps.setString(3, user.getEmail());
            ps.setByte(4, user.getStatus());

            int affectedRows = ps.executeUpdate();

            if (affectedRows != 0) {
                ResultSet  generatedKeys = ps.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int idGenerado = generatedKeys.getInt(1);
                    res = getById(idGenerado);
                } else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }
            ps.close();
        }catch (SQLException ex){
            throw new SQLException("Error al crear el usuario: " + ex.getMessage(), ex);
        } finally {
            ps = null;
            conn.disconnect();
        }
        return res;
    }

    public boolean update(User user) throws SQLException{
        boolean res = false;
        try{
            ps = conn.connect().prepareStatement(
                    "UPDATE Users SET name = ?, email = ?, status = ? WHERE id = ?"
            );
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setByte(3, user.getStatus());
            ps.setInt(4, user.getId());

            if(ps.executeUpdate() > 0){
                res = true;
            }
            ps.close();
        }catch (SQLException ex){
            throw new SQLException("Error al modificar el usuario: " + ex.getMessage(), ex);
        } finally {
            ps = null;
            conn.disconnect();
        }

        return res;
    }

    public boolean delete(User user) throws SQLException{
        boolean res = false;
        try{
            ps = conn.connect().prepareStatement("DELETE FROM Users WHERE id = ?");
            ps.setInt(1, user.getId());

            if(ps.executeUpdate() > 0){
                res = true;
            }
            ps.close();
        }catch (SQLException ex){
            throw new SQLException("Error al eliminar el usuario: " + ex.getMessage(), ex);
        } finally {
            ps = null;
            conn.disconnect();
        }

        return res;
    }

    public ArrayList<User> search(String name) throws SQLException{
        ArrayList<User> records  = new ArrayList<>();

        try {
            ps = conn.connect().prepareStatement("SELECT id, name, email, status FROM Users WHERE name LIKE ?");
            ps.setString(1, "%" + name + "%");
            rs = ps.executeQuery();

            while (rs.next()){
                User user = new User();
                user.setId(rs.getInt(1));
                user.setName(rs.getString(2));
                user.setEmail(rs.getString(3));
                user.setStatus(rs.getByte(4));
                records.add(user);
            }
            ps.close();
            rs.close();
        } catch (SQLException ex){
            throw new SQLException("Error al buscar usuarios: " + ex.getMessage(), ex);
        } finally {
            ps = null;
            rs = null;
            conn.disconnect();
        }
        return records;
    }

    public User getById(int id) throws SQLException{
        User user  = new User();

        try {
            ps = conn.connect().prepareStatement("SELECT id, name, email, status FROM Users WHERE id = ?");
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                user.setId(rs.getInt(1));
                user.setName(rs.getString(2));
                user.setEmail(rs.getString(3));
                user.setStatus(rs.getByte(4));
            } else {
                user = null;
            }
            ps.close();
            rs.close();
        } catch (SQLException ex){
            throw new SQLException("Error al obtener un usuario por id: " + ex.getMessage(), ex);
        } finally {
            ps = null;
            rs = null;
            conn.disconnect();
        }
        return user;
    }

    public User authenticate(User user) throws SQLException{
        User userAutenticate = new User();

        try {
            ps = conn.connect().prepareStatement("SELECT id, name, email, status FROM Users WHERE email = ? AND passwordHash = ? AND status = 1");
            ps.setString(1, user.getEmail());
            ps.setString(2, PasswordHasher.hashPassword(user.getPasswordHash()));
            rs = ps.executeQuery();

            if (rs.next()) {
                userAutenticate.setId(rs.getInt(1));
                userAutenticate.setName(rs.getString(2));
                userAutenticate.setEmail(rs.getString(3));
                userAutenticate.setStatus(rs.getByte(4));
            } else {
                userAutenticate = null;
            }
            ps.close();
            rs.close();
        } catch (SQLException ex){
            throw new SQLException("Error al autenticar un usuario por id: " + ex.getMessage(), ex);
        } finally {
            ps = null;
            rs = null;
            conn.disconnect();
        }
        return userAutenticate;
    }

    public boolean updatePassword(User user) throws SQLException{
        boolean res = false;
        try{
            ps = conn.connect().prepareStatement(
                    "UPDATE Users SET passwordHash = ? WHERE id = ?"
            );
            ps.setString(1, PasswordHasher.hashPassword(user.getPasswordHash()));
            ps.setInt(2, user.getId());

            if(ps.executeUpdate() > 0){
                res = true;
            }
            ps.close();
        }catch (SQLException ex){
            throw new SQLException("Error al actualizar la contraseña: " + ex.getMessage(), ex);
        } finally {
            ps = null;
            conn.disconnect();
        }
        return res;
    }
}
