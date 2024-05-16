// ==============================================================
// 		EVENTOS

// RESET
reset = function() {
	let req = new XMLHttpRequest();
	req.open("POST", "ControllerServlet", true);
	req.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	req.onreadystatechange = () => {
		if (req.readyState == 4 && req.status == 200) { 
			   atualizaSessao();
			   expoeListaContatos();
			}else {
				console.error("Ocorreu um erro na requisição: ", req.status);
				}
	   }
		req.send("op=RESET");
}

// NOVA AULA
novaAula = function() {
	window.location.href = "nova";
}

// CANCELA NOVA AULA (OU EDIÇÃO)
calcelarNovaAula = function() {
	window.location.href = "/prova1";
}

// EDITA UMA AULA COM ID ESPECIFICADO
editarAula = function(id) {
	window.location.href = "edit?id=" + id;
}

// ENVIA CONTEÚDO DA NOVA AULA
enviarNovaAula = function() {
	// obtém os valores a partir do formulário
	let data = document.getElementById('data-id').value;
	let horario = document.getElementById('hora-id').value;
	let duracao = document.getElementById('dur-id').value;
	let codDisciplina = document.getElementById('disc-id').value;
	let assunto = document.getElementById('ass-id').value;
	// verificando a validação
	if (!validaNovaAula(data, horario, duracao, codDisciplina, assunto)) {
        document.getElementById('msg-id').style.display = 'block';
        return;
    }
	let req = new XMLHttpRequest();
	req.open("POST", "ControllerServlet", true);
	req.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	req.onreadystatechange = function() {
		if (req.readyState == 4) {
			if (req.status == 200) {  
			   atualizaSessao();
			   window.location.href = "/prova1";
			} else {
				console.error("Erro na requisição: ", req.status);
					}
			}
		}
	let parametros = "op=CREATE&data=" + encodeURIComponent(data) + "&horario=" + encodeURIComponent(horario) + "&duracao=" + encodeURIComponent(duracao) + "&codDisciplina=" + encodeURIComponent(codDisciplina) + "&assunto=" + encodeURIComponent(assunto);
   	req.send(parametros);
}
enviarEdit = function() {
    // obtém os valores a partir do formulário
    let id = document.getElementById('id').innerHTML;
    let data = document.getElementById('data-id').value;
    let horario = document.getElementById('hora-id').value;
    let duracao = document.getElementById('dur-id').value;
    let codDisciplina = document.getElementById('disc-id').value;
    let assunto = document.getElementById('ass-id').value;

    // Criar uma nova instância do objeto XMLHttpRequest
    let xhr = new XMLHttpRequest();

    // Configurar a requisição
    xhr.open("POST", "ControllerServlet", true);
    xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

    // Definir a função a ser chamada quando a resposta da requisição estiver pronta
    xhr.onreadystatechange = function() {
        if (xhr.readyState == 4) {
            if (xhr.status == 200) {
                // Se a requisição for bem sucedida, execute atualizaSessao() e redirecione para "/prova1"
                atualizaSessao();
                window.location.href = "/prova1";
            } else {
                // Se não for bem sucedida, faça algo
                console.error("Ocorreu um erro na requisição: " + xhr.status);
            }
        }
    };

    // Montar os dados a serem enviados na requisição
    let params = "op=UPDATE&id=" + id + "&data=" + data + "&horario=" + horario + "&duracao=" + duracao + "&codDisciplina=" + codDisciplina + "&assunto=" + assunto;

    // Enviar a requisição
    xhr.send(params);
}

deleta = function(id) {
	// Aqui, você faz uma requisição AJAX POST a ControllerServlet e
   // envia a chave 'op' valendo 'DELETE'. Envie, do mesmo modo, o parâmetro id
   // Se a requisição for bem sucedida, execute atualizaSessao() e
   // window.location.href = "/prova1"
   // Se não for bem sucedida, decida o que fazer
	let req = new XMLHttpRequest();
	req.open("POST", "ControllerServlet", true);
	req.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	req.onreadystatechange = () => {
		if (req.readyState == 4 && req.status == 200) {
			atualizaSessao();
			window.location.href = "/prova1";
		} else {
			// O QUE FAZER SE DEU ERRADO
			alert("Ocorreu um erro ao excluir o item.");
					}
	}
	req.send("op=DELETE&id=" + id);
}



const atualizaSessao = function() {
	let req = new XMLHttpRequest();
	req.open("POST", "ControllerServlet", true);
	req.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	req.onreadystatechange = () => {
		if (req.readyState == 4 && req.status == 200) {
			expoeListaContatos();
			console.log("A sessão foi atualizada com sucesso.");
		} else {
			console.log("Ocorreu um erro ao tentar atualizar a sessão.");
		}
	}
	req.send("op=START_SESSION");
}



// ============================================================
// 			VALIDAÇÕES

validaNovaAula = function(data, horario, duracao, codDisciplina, assunto) {
    // Verificar se todos os campos são preenchidos
    if (!data || !horario || !duracao || !codDisciplina || !assunto) {
	document.getElementById('msg-vazio').style.display = 'block';
        return false; // Se algum campo estiver vazio, a validação falha
    }

    // Validar o formato da data (exemplo: AAAA-MM-DD)
    let dataRegex = /^\d{4}-\d{2}-\d{2}$/;
    if (!dataRegex.test(data)) {
	document.getElementById('msg-data').style.display = 'block';
        return false; // Se a data não estiver no formato correto, a validação falha
    }

    // Validar o formato do horário (exemplo: HH:MM)
    let horarioRegex = /^\d{2}:\d{2}$/;
    if (!horarioRegex.test(horario)) {
	document.getElementById('msg-horario').style.display = 'block';
        return false; // Se o horário não estiver no formato correto, a validação falha
    }

    // Validar a duração (deve ser um número positivo)
    if (isNaN(parseFloat(duracao)) || parseFloat(duracao) <= 0) {
	document.getElementById('msg-duracao').style.display = 'block';
        return false; // Se a duração não for um número positivo, a validação falha
    }

    // Se todos os critérios de validação forem atendidos, retornar true
    return true;
}






// ===================================================================================
// 		INICIALIZA O PROCESSAMENTO

atualizaSessao();
