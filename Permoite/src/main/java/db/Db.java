package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import model.Aula;
import model.AulaDto;

public class Db {

	private static Db instance = null;
	private Connection connection = null;

	private String driver;
	private String url;
	private String user;
	private String password;

	private Db() {
		this.confDB();
		this.conectar();
		this.criarTabela();
	}

	public static Db getInstance() {
		if (instance == null) {
			instance = new Db();
		}
		return instance;
	}

	private void confDB() {
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

	// Inicia a conexão com o banco de dados
	private void conectar() {
		try {
			this.connection = DriverManager.getConnection(this.url, this.user, this.password);
		} catch (SQLException e) {
			throw new DbException(e.getMessage());	
		}
	}

	private void criarTabela() {
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

	// Encerra a conexão
	public void close() {
		try {
			this.connection.close();
		} catch (SQLException e) {
			throw new DbException(e.getMessage());	
		}
	}
	
	//Para poder encerrar o Statement
		private static void closeStatement(Statement st) {
			if(st != null) {
				try {
					st.close();
				} catch (SQLException e) {
					throw new DbException(e.getMessage());
				}
			}
		}
		
		//Para poder encerrar o ResultSet
		private static void closeResultSet(ResultSet rs) {
			if(rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					throw new DbException(e.getMessage());
				}
			}
		}

	/*
	 * ****************************************************************
	 * CRUD
	 * ****************************************************************
	 */

	// CRUD READ
	public ArrayList<AulaDto> findAll() {
			PreparedStatement ps = null;
			ResultSet rs = null;
			String query = "SELECT ID, COD_DISCIPLINA, ASSUNTO, DURACAO, DATA, HORARIO FROM AULA;";
			ArrayList<AulaDto> lista = new ArrayList<AulaDto>();
			try {
				ps = connection.prepareStatement(query);
				rs= ps.executeQuery();


				while(rs.next()) {
					Aula aula = instantiateAula(rs);
					AulaDto aulaDto =new  AulaDto(aula);
					lista.add(aulaDto);
				}

			return lista;
			}catch (SQLException e) {
				throw new DbException(e.getMessage());
			}  finally {
		        closeResultSet(rs);
		        closeStatement(ps);
		    }
	}

	public AulaDto findById(String id) {
		
			PreparedStatement ps = null;
			ResultSet rs = null;
			String query = "SELECT ID, COD_DISCIPLINA, ASSUNTO, DURACAO, DATA, HORARIO FROM AULA "
					+ "WHERE ID = ?";
			
			try {
				ps = connection.prepareStatement(query);
				ps.setString(1, id);
				
				rs = ps.executeQuery();
				
				if(rs.next()) {
					Aula aula = instantiateAula(rs);
					AulaDto aulaDto = new AulaDto(aula);
					return aulaDto;
				}
				
				return null;
				
			} catch (SQLException e) {
				throw new DbException(e.getMessage());
			}
			finally {
				closeStatement(ps);
				closeResultSet(rs);
			}
		}

	// CRUD CREATAE
	public void create(AulaDto dto) {
		PreparedStatement pst = null;
		String query = "INSERT INTO AULA (COD_DISCIPLINA, ASSUNTO, DURACAO, DATA, HORARIO) "
				+ "VALUES (?,?,?,?,?)";
		try {
			 Aula aula = new Aula(dto);

		        pst = this.connection.prepareStatement(query);

		        pst.setInt(1, aula.getCodDisciplina());
		        pst.setString(2, aula.getAssunto());
		        pst.setInt(3, aula.getDuracao());
		        pst.setString(4, aula.getData());
		        pst.setString(5, aula.getHorario());

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			closeStatement(pst);
		}
	}

	// CRUD DELETE
	public void deleteAll() {
		String query = "DELETE FROM AULA";
		Statement st = null;
		try {
			st = this.connection.createStatement();
			st.execute(query);
		} catch (SQLException e) {
			e.getStackTrace();
			throw new DbException(e.getMessage());
		}
		finally {
			closeStatement(st);
		}
	}

	// CRUD DELETE
	public void delete(String id) {
		String query = "DELETE FROM AULA WHERE ID = ?";
		PreparedStatement pst = null;
		try {
			pst = this.connection.prepareStatement(query);
			pst.setString(1, id);
			pst.execute();
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
	        closeStatement(pst);
	    }
	}

	// CRUD UPDATE
	public void update(AulaDto dto) {
		PreparedStatement ps = null;
		String query = "UPDATE AULA SET "
				+ "COD_DISCIPLINA = ?, ASSUNTO = ?, DURACAO = ?, DATA = ?, HORARIO = ? "
				+ "WHERE ID = ?";
		
		 try {
		        ps = this.connection.prepareStatement(query);
		        ps.setInt(1, Integer.parseInt(dto.codDisciplina)); 
		        ps.setString(2, dto.assunto); 
		        ps.setInt(3, Integer.parseInt(dto.duracao)); 
		        ps.setString(4, dto.data);
		        ps.setString(5, dto.horario);
		        ps.setString(6, dto.id);
		        ps.executeUpdate();
		    } catch (SQLException e) {
		        e.printStackTrace();
		        throw new DbException(e.getMessage());
		    } finally {
		        closeStatement(ps);
		    }
		}
	
	
	//Método criado para poder instanciar a classe Aula de forma mais organizada, sem ter que fazer isso em cada método
		private Aula instantiateAula(ResultSet rs) throws SQLException{
			Aula aula = new Aula();
			aula.setAssunto(rs.getString("ASSUNTO"));
			aula.setCodDisciplina(rs.getInt("COD_DISCIPLINA"));
			aula.setData(rs.getString("DATA"));
			aula.setDuracao(rs.getInt("DURACAO"));
			aula.setHorario(rs.getString("HORARIO"));
			aula.setId(rs.getLong("ID"));
			return aula;
		}

	/*
	 * PARA EFEITO DE TESTES
	 */

		public void reset() {
			this.deleteAll();
			this.popularTabela();
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
