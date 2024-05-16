package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import db.Db;
import db.DbException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.AulaDto;

@WebServlet(urlPatterns = { "/prova1", "/nova", "/edit" })
public class ControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public ControllerServlet() {
		super();
	}

	/*
	 * 	Este método é responsável pelo roteamento do projeto.
	 * 	Se você fez alguma alteração no nome do projeto, isso pode
	 * 	causar impacto aqui e talvez você precise ajustar alguns parâmetros.
	 * 	IMPORTANTE: no caso da rota 'edit', o AJAX envia um parâmetro (via GET) que
	 * 				identifica um id. Isso terá um impacto essencial na página edit.jsp.
	 * 				Lá, você precisará recuperar esse valor.
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getServletPath();
		if (action.equals("/nova")) {
			RequestDispatcher rd = request.getRequestDispatcher("nova.jsp");
			rd.forward(request, response);
		} else if (action.equals("/edit")) {
			String id = request.getParameter("id");
			HttpSession session = request.getSession();
			Db db = Db.getInstance();
			AulaDto dto = db.findById(id);
			session.setAttribute("dto", dto);
			RequestDispatcher rd = request.getRequestDispatcher("edit.jsp");
			rd.forward(request, response);
		}
	}

	//!!passando o db por argumento
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String op = request.getParameter("op");
        Db db = Db.getInstance();

        switch (op) {
            case "START_SESSION":
                this.poeDadosNaSessao(session);
                break;
            case "RESET":
                db.reset();
                break;
            case "CREATE":
                this.create(request, db);
                break;
            case "READ":
                this.getAula(request, response, db);
                break;
            case "UPDATE":
                this.update(request, db);
                break;
            case "DELETE":
                this.delete(request, db);
                break;
        }
    }
	
	//!!Lista de DTO
	private void poeDadosNaSessao(HttpSession session) throws ServletException {
        // Consulta o banco de dados para obter uma lista de todos os registros
        Db db = Db.getInstance();
        ArrayList<AulaDto> lista;
		lista = db.findAll();
		session.setAttribute("lista", lista);
    }

	//!!Não recebe http por parametro
	private void create(HttpServletRequest request, Db db) {
        String codDisciplina = request.getParameter("codDisciplina");
        String assunto = request.getParameter("assunto");
        String duracao = request.getParameter("duracao");
        String data = request.getParameter("data");
        String horario = request.getParameter("horario");

        AulaDto dto = new AulaDto();
        dto.codDisciplina = codDisciplina;
        dto.assunto = assunto;
        dto.duracao = duracao;
        dto.data = data;
        dto.horario = horario;

        db.create(dto);
    }

	private void delete(HttpServletRequest request, Db db) {
        String id = request.getParameter("id");
        db.delete(id);
    }

	//!!add try catch, modificar append e TUDO(não tem como resolver)
	private void getAula(HttpServletRequest request, HttpServletResponse response, Db db) throws IOException {
	    String id = request.getParameter("id");
	    AulaDto dto = db.findById(id);
	    response.setContentType("application/json");
	    StringBuilder stb = new StringBuilder();
	    stb.append("{\"id\": \"").append(id).append("\",")
	       .append("\"disciplina\": \"").append(dto.disciplina).append("\",")
	       .append("\"codDisciplina\": \"").append(dto.codDisciplina).append("\",")
	       .append("\"assunto\": \"").append(dto.assunto).append("\",")
	       .append("\"duracao\": \"").append(dto.duracao).append("\",")
	       .append("\"data\": \"").append(dto.data).append("\",")
	       .append("\"horario\": \"").append(dto.horario).append("\"}");
	    String json = stb.toString();
	    try {
	        response.getWriter().write(json);
	    } catch (IOException e) {
	        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	        response.getWriter().write("Erro ao processar a requisição: " + e.getMessage());
	    }
	}
	
	//!Ultiliza http
	private void update(HttpServletRequest request, Db db) {
        String id = request.getParameter("id");
        String codDisciplina = request.getParameter("codDisciplina");
        String assunto = request.getParameter("assunto");
        String duracao = request.getParameter("duracao");
        String data = request.getParameter("data");
        String horario = request.getParameter("horario");

        AulaDto dto = new AulaDto();
        dto.id = id;
        dto.codDisciplina = codDisciplina;
        dto.assunto = assunto;
        dto.duracao = duracao;
        dto.data = data;
        dto.horario = horario;

        db.update(dto);
	}

}
