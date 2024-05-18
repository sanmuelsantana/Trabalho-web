reset = function() {
    let req = new XMLHttpRequest();
    req.open("POST", "ControllerServlet", true);
    req.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    req.onreadystatechange = () => {
        if (req.readyState == 4 && req.status == 200) {
            atualizaSessao();
            window.location.href = "/prova1";
        } else {
            console.error("Failed to reset.");
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
    let data = document.getElementById('data-id').value;
    let horario = document.getElementById('hora-id').value;
    let duracao = document.getElementById('dur-id').value;
    let codDisciplina = document.getElementById('disc-id').value;
    let assunto = document.getElementById('ass-id').value;

    if (!validaNovaAula(data, horario, duracao, codDisciplina, assunto)) {
        document.getElementById('msg-id').style.display = 'block';
        return;
    }

    let req = new XMLHttpRequest();
    req.open("POST", "ControllerServlet", true);
    req.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    req.onreadystatechange = () => {
        if (req.readyState == 4 && req.status == 200) {
            atualizaSessao();
            window.location.href = "/prova1";
        } else {
            console.error("Failed to send new class content.");
        }
    }
    req.send("op=CREATE&data=" + data + "&horario=" + horario + "&duracao=" + duracao + "&codDisciplina=" + codDisciplina + "&assunto=" + assunto);
}


// ENVIA CONTEÚDO EM EDIÇÃO
enviarEdit = function() {
    let id = document.getElementById('id').innerHTML;
    let data = document.getElementById('data-id').value;
    let horario = document.getElementById('hora-id').value;
    let duracao = document.getElementById('dur-id').value;
    let codDisciplina = document.getElementById('disc-id').value;
    let assunto = document.getElementById('ass-id').value;

    let req = new XMLHttpRequest();
    req.open("POST", "ControllerServlet", true);
    req.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    req.onreadystatechange = () => {
        if (req.readyState == 4 && req.status == 200) {
            atualizaSessao();
            window.location.href = "/prova1";
        } else {
            console.error("Failed to send edited class content.");
        }
    }
    req.send("op=UPDATE&id=" + id + "&data=" + data + "&horario=" + horario + "&duracao=" + duracao + "&codDisciplina=" + codDisciplina + "&assunto=" + assunto);
}


// DELETA UMA AULA
deleta = function(id) {
    let req = new XMLHttpRequest();
    req.open("POST", "ControllerServlet", true);
    req.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    req.onreadystatechange = () => {
        if (req.readyState == 4 && req.status == 200) {
            atualizaSessao();
            window.location.href = "/prova1";
        } else {
            console.error("Failed to delete class.");
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
            // Do something if successful
        } else {
            console.error("Failed to update session.");
        }
    }
    req.send("op=START_SESSION");
}


// ============================================================
// 			VALIDAÇÕES

validaNovaAula = function(data, horario, duracao, codDisciplina, assunto) {
    if (data.trim() === '' || horario.trim() === '' || duracao.trim() === '' || codDisciplina.trim() === '' || assunto.trim() === '') {
        return false; 
    }
    
    return true;
}
// ===================================================================================
// 		INICIALIZA O PROCESSAMENTO

atualizaSessao();
