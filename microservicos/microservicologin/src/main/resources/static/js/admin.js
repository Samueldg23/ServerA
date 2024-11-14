document.addEventListener("DOMContentLoaded", function () {
    const sections = {
        produtos: document.getElementById("produtos-section"),
        clientes: document.getElementById("clientes-section"),
        perfil: document.getElementById("perfil-section"),
        associarProdutos: document.getElementById("associar-produtos-section"),
    };
    const links = {
        produtos: document.getElementById("produtos-link"),
        clientes: document.getElementById("clientes-link"),
        perfil: document.getElementById("perfil-link"),
        associarProdutos: document.getElementById("associar-produtos-link"),
    };

    function showSection(section) {
        Object.values(sections).forEach(s => s.classList.add("hidden"));
        sections[section].classList.remove("hidden");
    }

    Object.keys(links).forEach(key => {
        links[key].addEventListener("click", (e) => {
            e.preventDefault();
            showSection(key);
        });
    });

    // Funções de CRUD Produtos
    // Função para carregar produtos
    async function carregarProdutos() {
        const response = await fetch("http://localhost:8090/produtos/listarProdutos");
        const produtos = await response.json();
        const produtosUl = document.getElementById("produtos-ul");
        produtosUl.innerHTML = "";

        produtos.forEach(produto => {
            const row = document.createElement("tr");
            row.innerHTML = `
            <td>${produto.id}</td>
            <td>${produto.titulo}</td>
            <td>${produto.descricao}</td>
            <td>${produto.preco}</td>
            <td>
                <span class="status-icon ${produto.ativo ? 'ativo' : 'inativo'}">
                    ${produto.ativo ? "✔️" : "❌"}
                </span>
            </td>
        `;
            produtosUl.appendChild(row);
        });
    }

    async function salvarProduto(event) {
        event.preventDefault();

        const titulo = document.getElementById("titulo").value;
        const descricao = document.getElementById("descricao").value;
        const preco = parseFloat(document.getElementById("preco").value);
        const ativo = 1; 

        const formData = new URLSearchParams();
        formData.append("titulo", document.getElementById("titulo").value);
        formData.append("descricao", document.getElementById("descricao").value);
        formData.append("preco", parseFloat(document.getElementById("preco").value) || 0);
        formData.append("ativo", 1);


        try {
            const response = await fetch("http://localhost:8090/produtos/salvarProduto", {
                method: "POST",
                headers: {
                    "Content-Type": "application/x-www-form-urlencoded",
                },
                body: formData.toString(),
            });

            if (response.ok) {
                alert("Produto salvo com sucesso!");
                carregarProdutos();
                document.getElementById("produto-form").reset();
            } else {
                alert("Erro ao salvar produto. Verifique os dados e tente novamente.");
            }
        } catch (error) {
            console.error("Erro na requisição:", error);
            alert("Erro ao salvar produto. Verifique a conexão com o servidor.");
        }
    }


    async function editarProduto(id) {
        const response = await fetch(`http://localhost:8090/produtos/obterProduto?id=${id}`);
        const produto = await response.json();

        document.getElementById("produto-id").value = produto.id;
        document.getElementById("titulo").value = produto.titulo;
        document.getElementById("descricao").value = produto.descricao;
        document.getElementById("preco").value = produto.preco;
        document.getElementById("ativo").value = produto.ativo;
    }

    async function excluirProduto(id) {
        if (confirm("Tem certeza que deseja excluir este produto?")) {
            await fetch(`http://localhost:8090/produtos/deletarProduto?produtoId=${id}`, { method: "DELETE" });
            carregarProdutos();
        }
    }

    document.getElementById("produto-form").addEventListener("submit", salvarProduto);
    carregarProdutos();
});

//Gerenciar clienteas
async function carregarClientes() {
    try {
        const response = await fetch("http://localhost:8080/usuarios/listarUsuariosDetalhados");
        if (!response.ok) {
            throw new Error("Erro ao buscar clientes: " + response.statusText);
        }

        const usuarios = await response.json();
        const clientesUl = document.getElementById("clientes-ul");
        clientesUl.innerHTML = "";

        usuarios
            .filter(usuario => usuario.grupo === "Cliente") // Filtro para selecionar apenas os usuários do grupo 'cliente'
            .forEach(usuario => {
                const row = document.createElement("tr");
                row.innerHTML = `
                    <td>${usuario.id}</td>
                    <td>${usuario.idCliente || "N/A"}</td>
                    <td>${usuario.nome}</td>
                    <td>${usuario.email}</td>
                    <td>${usuario.ativo ? "✔️" : "❌"}</td>
                    <td>${usuario.dataNascimento || "N/A"}</td>
                    <td>${usuario.telefone || "N/A"}</td>
                    <td>${usuario.produtos ? usuario.produtos.join(", ") : "Nenhum"}</td>
                `;
                clientesUl.appendChild(row);
            });
    } catch (error) {
        console.error(error);
        alert("Não foi possível carregar os clientes.");
    }
}

document.addEventListener("DOMContentLoaded", () => {
    carregarClientes();
});

// Gerenciar o perfil
document.addEventListener("DOMContentLoaded", () => {
    carregarPerfil();

    const form = document.getElementById("perfil-form");
    form.addEventListener("submit", (event) => {
        event.preventDefault();
        atualizarPerfil();
    });

    const deletarContaBtn = document.getElementById("deletar-conta");
    if (deletarContaBtn) {
        deletarContaBtn.addEventListener("click", deletarConta);
    }
});

async function carregarPerfil() {
    const usuarioId = localStorage.getItem("usuarioId");

    if (!usuarioId) {
        alert("Usuário não autenticado. Redirecionando para login.");
        window.location.href = "/login";
        return;
    }

    try {
        const response = await fetch(`http://localhost:8080/usuarios/${usuarioId}`);
        if (response.ok) {
            const usuario = await response.json();

            document.getElementById("admin-nome").value = usuario.nome;
            document.getElementById("admin-email").value = usuario.email;
            document.getElementById("admin-senha").value = usuario.senha;
            document.getElementById("admin-ativo").value = usuario.ativo;

             const sexo = usuario.sexo === 'M' ? 'Masculino' : 'Feminino';
             document.getElementById("admin-sexo").value = sexo;
 
            document.getElementById("admin-ativo").value = usuario.ativo === 1 ? "1" : "0";

 
             document.getElementById("admin-grupo").value = usuario.grupo;
         } else if (response.status === 404) {
             alert("Usuário não encontrado.");
         } else {
             alert("Erro ao carregar perfil.");
         }
    } catch (error) {
        console.error("Erro ao carregar perfil:", error);
        alert("Erro ao carregar perfil. Verifique a conexão com o servidor.");
    }
}

async function atualizarPerfil() {
    const usuarioId = localStorage.getItem("usuarioId");
    const nome = document.getElementById("admin-nome").value;
    const email = document.getElementById("admin-email").value;
    const senha = document.getElementById("admin-senha").value;
    const ativo = document.getElementById("admin-ativo").value === 'Ativo' ? 1 : 0;
    const data = { nome, email, senha, ativo };

    try {
        const response = await fetch(`http://localhost:8080/usuarios/alterar/${usuarioId}`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(data),
        });

        if (response.ok) {
            alert("Perfil atualizado com sucesso!");
        } else {
            alert("Erro ao atualizar perfil.");
        }
    } catch (error) {
        console.error("Erro ao atualizar perfil:", error);
        alert("Erro ao atualizar perfil. Verifique a conexão com o servidor.");
    }
}

async function deletarConta() {
    const usuarioId = localStorage.getItem("usuarioId");

    if (confirm("Tem certeza que deseja deletar sua conta?")) {
        try {
            const response = await fetch(`http://localhost:8080/usuarios/deletar/${usuarioId}`, {
                method: "DELETE",
            });

            if (response.ok) {
                alert("Conta deletada com sucesso.");
                localStorage.clear();
                window.location.href = "/";
            } else {
                alert("Erro ao deletar conta.");
            }
        } catch (error) {
            console.error("Erro ao deletar conta:", error);
        }
    }
}

async function associarProduto() {
    const clienteId = parseInt(document.getElementById('cliente-id').value);
    const produtoId = parseInt(document.getElementById('produto-id-2').value);

    if (isNaN(clienteId) || isNaN(produtoId)) {
        alert("ID do cliente ou ID do produto inválido.");
        return;
    }

    try {
        const response = await fetch('http://localhost:8085/clienteProduto/associar', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ clienteId, produtoId })
        });

        if (response.ok) {
            const result = await response.json();
            alert('Produto associado com sucesso!');
            console.log(result);
        } else {
            const error = await response.text();
            alert(`Erro ao associar produto: ${error}`);
        }
    } catch (error) {
        console.error('Erro na solicitação:', error);
        alert('Erro ao fazer a solicitação.');
    }
}

async function desassociarProduto() {
    const clienteId = parseInt(document.getElementById('cliente-id').value);
    const produtoId = parseInt(document.getElementById('produto-id-2').value);

    try {
        const response = await fetch('http://localhost:8085/clienteProduto/desassociar', {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ clienteId, produtoId })
        });

        if (response.ok) {
            alert('Produto desassociado com sucesso!');
        } else {
            const error = await response.text();
            alert(`Erro ao desassociar produto: ${error}`);
        }
    } catch (error) {
        console.error('Erro na solicitação:', error);
        alert('Erro ao fazer a solicitação.');
    }
}
