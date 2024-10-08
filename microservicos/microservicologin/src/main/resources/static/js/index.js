function mostrarCamposCliente() {
    const grupo = document.getElementById("grupo").value;
    const clienteCampos = document.getElementById("clienteCampos");

    if (grupo === "Cliente") {
        clienteCampos.style.display = "block"; 
    } else {
        clienteCampos.style.display = "none";  
    }
}

function criarConta() {
    const nome = document.getElementById("nome").value;
    const email = document.getElementById("email").value;
    const senha = document.getElementById("senha").value;
    const sexo = document.getElementById("sexo").value;
    const grupo = document.getElementById("grupo").value;

    
    const usuarioData = {
        nome: nome,
        email: email,
        senha: senha,
        sexo: sexo,
        grupo: grupo,
        ativo: 1 
    };

    
    fetch("http://localhost:8080/salvarUsuario", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(usuarioData)
    })
    .then(response => response.json())
    .then(usuario => {
        if (grupo === "Cliente") {
            // Se o grupo for Cliente, também cria o registro de cliente
            criarCliente(usuario.id); 
        } else {
            alert("Conta criada com sucesso!");
            window.location.href = "login.html";
        }
    })
    .catch(error => {
        console.error("Erro:", error);
        alert("Erro ao criar conta.");
    });
}

function criarCliente(idUsuario) {
    const nascimento = document.getElementById("nascimento").value;
    const cpf = document.getElementById("cpf").value;
    const celular = document.getElementById("celular").value;


    const clienteData = {
        nascimento: nascimento,
        cpf: cpf,
        celular: celular,
        idUsuario: idUsuario
    };

    
    fetch("http://localhost:8085/salvarCliente", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(clienteData)
    })
    .then(response => response.json())
    .then(() => {
        alert("Conta de cliente criada com sucesso!");
        window.location.href = "login.html"; // criar a tela de home para redirecionar para ela
    })
    .catch(error => {
        console.error("Erro:", error);
        alert("Erro ao criar conta de cliente.");
    });
}
