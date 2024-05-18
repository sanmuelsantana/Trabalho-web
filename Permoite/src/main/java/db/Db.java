package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import model.AulaDto;

public class Db {

    private static Db instance = null;
    private Connection connection = null;

    private String driver;
    private String url;
    private String user;
    private String password;

    private Db() {
        try {
            this.confDB();
            this.conectar();
            this.criarTabela();
            this.popularTabela();
        } catch (DbException e) {
            throw new DbException(e.getMessage());
        }
    }

    public static Db getInstance() {
        if (instance == null) {
            instance = new Db();
        }
        return instance;
    }

    private void confDB() throws DbException {
        try {
            this.driver = "org.h2.Driver";
            this.url = "jdbc:h2:mem:testdb";
            this.user = "sa";
            this.password = "";
            Class.forName(this.driver);
        } catch (ClassNotFoundException e) {
            throw new DbException(e.getMessage());
        }
    }

    private void conectar() throws DbException {
        try {
            this.connection = DriverManager.getConnection(this.url, this.user, this.password);
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }

    private void criarTabela() throws DbException {
        String query = "CREATE TABLE AULA ("
                + "    ID BIGINT AUTO_INCREMENT PRIMARY KEY,"
                + "    COD_DISCIPLINA INT,"
                + "    ASSUNTO VARCHAR(255),"
                + "    DURACAO INT,"
                + "    DATA VARCHAR(20),"
                + "    HORARIO VARCHAR(20)"
                + ")";
        try {
            Statement statement = this.connection.createStatement();
            statement.executeUpdate(query);
            this.connection.commit();
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }

    public void close() {
        try {
            if (this.connection != null) {
                this.connection.close();
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }

    public ArrayList<AulaDto> findAll() {
        String query = "SELECT ID, COD_DISCIPLINA, ASSUNTO, DURACAO, DATA, HORARIO FROM AULA;";
        ArrayList<AulaDto> lista = new ArrayList<>();
        try {
            Statement statement = this.connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                AulaDto dto = new AulaDto();
                dto.id = resultSet.getString("ID");
                dto.codDisciplina = resultSet.getString("COD_DISCIPLINA");
                dto.assunto = resultSet.getString("ASSUNTO");
                dto.duracao = resultSet.getString("DURACAO");
                dto.data = resultSet.getString("DATA");
                dto.horario = resultSet.getString("HORARIO");
                lista.add(dto);
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        return lista;
    }

    public AulaDto findById(String id) {
        String query = "SELECT ID, COD_DISCIPLINA, ASSUNTO, DURACAO, DATA, HORARIO FROM AULA WHERE ID = ?";
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(query);
            preparedStatement.setString(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                AulaDto dto = new AulaDto();
                dto.id = resultSet.getString("ID");
                dto.codDisciplina = resultSet.getString("COD_DISCIPLINA");
                dto.assunto = resultSet.getString("ASSUNTO");
                dto.duracao = resultSet.getString("DURACAO");
                dto.data = resultSet.getString("DATA");
                dto.horario = resultSet.getString("HORARIO");
                return dto;
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        return null;
    }

    public void create(AulaDto dto) {
        String query = "INSERT INTO AULA (COD_DISCIPLINA, ASSUNTO, DURACAO, DATA, HORARIO) VALUES (?,?,?,?,?)";
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(query);
            preparedStatement.setString(1, dto.codDisciplina);
            preparedStatement.setString(2, dto.assunto);
            preparedStatement.setString(3, dto.duracao);
            preparedStatement.setString(4, dto.data);
            preparedStatement.setString(5, dto.horario);
            preparedStatement.executeUpdate();
            this.connection.commit();
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }

    public void deleteAll() {
        String query = "DELETE FROM AULA";
        try {
            Statement st = this.connection.createStatement();
            st.execute(query);
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }

    public void delete(String id) {
        String query = "DELETE FROM AULA WHERE ID = ?";
        try {
            PreparedStatement pst = this.connection.prepareStatement(query);
            pst.setString(1, id);
            pst.execute();
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }

    public void update(AulaDto dto) {
        String query = "UPDATE AULA SET COD_DISCIPLINA = ?, ASSUNTO = ?, DURACAO = ?, DATA = ?, HORARIO = ? WHERE ID = ?";
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(query);
            preparedStatement.setString(1, dto.codDisciplina);
            preparedStatement.setString(2, dto.assunto);
            preparedStatement.setString(3, dto.duracao);
            preparedStatement.setString(4, dto.data);
            preparedStatement.setString(5, dto.horario);
            preparedStatement.setString(6, dto.id);
            preparedStatement.executeUpdate();
            this.connection.commit();
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }

    public void reset() {
        try {
            this.deleteAll();
            this.popularTabela();
        } catch (DbException e) {
            throw new DbException(e.getMessage());
        }
    }

    public void popularTabela() {
        AulaDto dto = new AulaDto();

        dto.codDisciplina = "1";
        dto.assunto = "Derivadas";
        dto.duracao = "2";
        dto.data = "2024-04-12";
        dto.horario = "14:00";
        this.create(dto);

        dto.codDisciplina = "3";
        dto.assunto = "Coordenadas Cartesianas";
        dto.duracao = "2";
        dto.data = "2024-04-13";
        dto.horario = "14:00";
        this.create(dto);

        dto.codDisciplina = "4";
        dto.assunto = "O Problema dos Três Corpos";
        dto.duracao = "4";
        dto.data = "2024-04-14";
        dto.horario = "14:00";
        this.create(dto);
    }
}
