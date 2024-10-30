function mostrarCamposCliente() {
    const grupo = document.getElementById("grupo").value;
    const clienteCampos = document.getElementById("clienteCampos");

    clienteCampos.style.display = grupo === "Cliente" ? "block" : "none";
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

    fetch("http://localhost:8080/usuarios/salvarUsuario", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(usuarioData)
    })
    .then(response => response.json())
    .then(usuario => {
        if (grupo === "Cliente") {
            // Se o grupo for Cliente, cria o registro de cliente
            criarCliente(usuario.id);
        } else {
            alert("Conta criada com sucesso!");
            window.location.href = "/";
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

    fetch("http://localhost:8085/clientes/cadastrar", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(clienteData)
    })
    .then(response => response.json())
    .then(() => {
        alert("Conta de cliente criada com sucesso!");
        window.location.href = "/";
    })
    .catch(error => {
        console.error("Erro:", error);
        alert("Erro ao criar conta de cliente.");
    });
}
