package cinema.DAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import cinema.BO.Utilisateur;
import org.mindrot.jbcrypt.BCrypt;

public class UtilisateurDAO extends DAO<Utilisateur> {
    @Override
    public boolean create(Utilisateur obj) {
        boolean result = false;
        try {
            String mdpHashe = BCrypt.hashpw(obj.getMdp(), BCrypt.gensalt());

            String sql = "INSERT INTO utilisateur(login, mdp) VALUES(?,?)";
            PreparedStatement ps = this.connect.prepareStatement(sql);
            ps.setString(1, obj.getLogin());
            ps.setString(2, mdpHashe);
            int rowsInserted = ps.executeUpdate();
            if (rowsInserted > 0) {
                result = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public boolean delete(Utilisateur obj) {
        boolean result = false;
        try {
            String sql = "DELETE FROM utilisateur WHERE id_utilisateur = ?";
            PreparedStatement ps = this.connect.prepareStatement(sql);
            ps.setInt(1, obj.getIdUtilisateur());

            int rowsDeleted = ps.executeUpdate();
            if (rowsDeleted > 0) {
                result = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public boolean update(Utilisateur obj) {
        boolean result = false;
        try {
            String sql = "UPDATE Utilisateur SET login=?, mdp=? WHERE id_utilisateur = ?";
            PreparedStatement ps = this.connect.prepareStatement(sql);
            ps.setString(1, obj.getLogin());
            ps.setString(2, obj.getMdp());
            ps.setInt(3, obj.getIdUtilisateur());
            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                result = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    private Utilisateur hydrate(ResultSet resultSet) throws SQLException {
        return new Utilisateur(resultSet.getInt("id_utilisateur"),
                resultSet.getString("nom"),
                resultSet.getString("prenom"),
                resultSet.getString("login"),
                resultSet.getString("mdp"));
    }

    @Override
    public List<Utilisateur> findAll() {
        List<Utilisateur> mesUtilisateurs = new ArrayList<>();
        Utilisateur utilisateur;
        try {
            String sql = "SELECT * FROM utilisateur";
            Statement statement = this.connect.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                utilisateur = hydrate(rs);
                mesUtilisateurs.add(utilisateur);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mesUtilisateurs;
    }

    @Override
    public Utilisateur find(int idUtilisateur) {
        Utilisateur user;
        try {
            String sql = "SELECT * FROM utilisateur WHERE id_utilisateur = ?";
            PreparedStatement ps = this.connect.prepareStatement(sql);
            ps.setInt(1, idUtilisateur);
            ResultSet result = ps.executeQuery();
            if (result.next()) {
                user = hydrate(result);
            } else {
                user = null;
            }

        } catch (SQLException e) {
            return null;
        }
        return user;
    }

    public Utilisateur authenticate(String login, String mdp) {
        try {
            String sql = "SELECT * FROM utilisateur WHERE login = ?";
            PreparedStatement ps = this.connect.prepareStatement(sql);
            ps.setString(1, login);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String mdpEnBDD = rs.getString("mdp");
                boolean mdpOk = false;

                // ✅ Si le mdp en BDD commence par $2a$ c'est un hash BCrypt
                if (mdpEnBDD.startsWith("$2a$")) {
                    mdpOk = BCrypt.checkpw(mdp, mdpEnBDD);
                } else {
                    // ❌ Mdp en clair, on compare directement
                    mdpOk = mdpEnBDD.equals(mdp);

                    // ✅ Et on en profite pour le migrer vers BCrypt
                    if (mdpOk) {
                        String mdpHashe = BCrypt.hashpw(mdp, BCrypt.gensalt());
                        String sqlUpdate = "UPDATE utilisateur SET mdp = ? WHERE login = ?";
                        PreparedStatement psUpdate = this.connect.prepareStatement(sqlUpdate);
                        psUpdate.setString(1, mdpHashe);
                        psUpdate.setString(2, login);
                        psUpdate.executeUpdate();
                    }
                }

                if (mdpOk) {
                    Utilisateur u = new Utilisateur();
                    u.setIdUtilisateur(rs.getInt("id_Utilisateur"));
                    u.setLogin(rs.getString("login"));
                    u.setMdp(rs.getString("mdp"));
                    return u;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
