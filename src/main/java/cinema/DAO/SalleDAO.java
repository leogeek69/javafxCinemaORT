package cinema.DAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import cinema.BO.Salle;

public class SalleDAO extends DAO<Salle> {

    @Override
    public boolean create(Salle obj) {
        boolean controle = false;
        try {
            String query = "INSERT INTO salle(numero, description, nb_places, id_cinema) VALUES (?, ?, ?, ?);";
            PreparedStatement statement = this.connect.prepareStatement(query);
            statement.setInt(1, obj.getNumSalle());
            statement.setString(2, obj.getDescSalle());
            statement.setInt(3, obj.getNbPlaces());
            statement.setInt(4, obj.getIdCinema());

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                controle = true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return controle;
    }

    @Override
    public boolean delete(Salle obj) {
        boolean controle = false;
        try {
            String sql = "DELETE FROM salle WHERE id_salle = ?;";
            PreparedStatement statement = this.connect.prepareStatement(sql);
            statement.setInt(1, obj.getIdSalle());

            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                controle = true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return controle;
    }

    @Override
    public boolean update(Salle obj) {
        boolean controle = false;
        try {
            String query = "UPDATE salle SET numero = ?, description = ?, nb_places = ?, id_cinema = ? WHERE id_salle = ?";
            PreparedStatement statement = this.connect.prepareStatement(query);
            statement.setInt(1, obj.getNumSalle());
            statement.setString(2, obj.getDescSalle());
            statement.setInt(3, obj.getNbPlaces());
            statement.setInt(4, obj.getIdCinema());
            statement.setInt(5, obj.getIdSalle());

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                controle = true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return controle;
    }

    @Override
    public Salle find(int id) {
        Salle salle = null;
        String query = "SELECT * FROM salle WHERE id_salle = ?;";
        try {
            PreparedStatement ps = this.connect.prepareStatement(query);
            ps.setInt(1, id);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                salle = hydrate(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return salle;
    }

    @Override
    public List<Salle> findAll() {
        List<Salle> mesSalles = new ArrayList<>();
        Salle salle;

        try {
            String query = "SELECT * FROM salle ORDER BY id_salle";
            Statement ps = this.connect.createStatement();
            ResultSet rs = ps.executeQuery(query);
            while (rs.next()) {
                salle = hydrate(rs);
                mesSalles.add(salle);
            }

        } catch (SQLException e) {
            return null;
        }
        return mesSalles;
    }

    public List<Salle> getAllByCinema(int idCinema) {
        List<Salle> mesSalles = new ArrayList<>();
        Salle salle;
        try {
            String sql = "SELECT * FROM salle WHERE id_cinema = ?";
            PreparedStatement ps = this.connect.prepareStatement(sql);
            ps.setInt(1, idCinema);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                salle = hydrate(rs);
                mesSalles.add(salle);
            }
        } catch (SQLException e) {
            return null;
        }
        return mesSalles;
    }

    private Salle hydrate(ResultSet resultSet) throws SQLException {
        return new Salle(
                resultSet.getInt("id_salle"),
                resultSet.getInt("numero"),
                resultSet.getString("description"),
                resultSet.getInt("nb_places"),
                resultSet.getInt("id_cinema")
        );
    }
}