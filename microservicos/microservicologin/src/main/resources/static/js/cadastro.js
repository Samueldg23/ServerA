function mostrarCamposCliente() {
    const grupo = document.getElementById("grupo").value;
    const clienteCampos = document.getElementById("clienteCampos");
    //se no grupo estiver cliente vai mostrar os campos que pertencem ao cliente além de somente os do usuários
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
    .then(response => {
        if (!response.ok) {
            return response.json().then(err => { throw new Error(err.message || "Erro ao criar usuário."); });
        }
        return response.json();
    })
    .then(usuario => {
        const idUsuario = usuario.id;
        if (!idUsuario) {
            throw new Error("ID do usuário não retornado.");
        }

        if (grupo === "Cliente") {
            criarCliente(idUsuario);//se for cliente chama a função	de criar cliente
        } else {
            alert("Conta criada com sucesso!");
            window.location.href = "/"; //chama a tela de login
        }
    })
    .catch(error => {
        console.error("Erro:", error);
        alert("Erro ao criar conta: " + error.message);
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

    console.log(clienteData);

    fetch("http://localhost:8085/clientes/cadastrar", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(clienteData)
    })
    .then(response => {
        if (!response.ok) {
            return response.json().then(err => { throw new Error(err.message || "Erro ao criar cliente."); });
        }
        return response.json();
    })
    .then(() => {
        alert("Conta de cliente criada com sucesso!");
        window.location.href = "/"; 
    })
    .catch(error => {
        console.error("Erro:", error);
        alert("Erro ao criar conta de cliente: " + error.message);
    });
}
