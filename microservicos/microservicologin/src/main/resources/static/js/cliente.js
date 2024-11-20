document.addEventListener("DOMContentLoaded", () => {
    //cria uma contante usuarioId com o id que o index armazenou
    const usuarioId = localStorage.getItem("usuarioId");

    if (!usuarioId) {
        window.location.href = '/';
        return;
    }

    carregarDadosUsuario(usuarioId);
    carregarDadosCliente(usuarioId);
    carregarProdutosAssociados(usuarioId);

    document.getElementById("perfil-link").addEventListener("click", () => {
        mostrarSeção("perfil-section");
    });

    document.getElementById("produtos-link").addEventListener("click", () => {
        mostrarSeção("produtos-section");
        carregarProdutosDisponiveis();
    });
});

function mostrarSeção(seçãoId) {
    const seções = document.querySelectorAll(".section");
    seções.forEach(seção => seção.classList.add("hidden"));

    const seçãoSelecionada = document.getElementById(seçãoId);
    if (seçãoSelecionada) {
        seçãoSelecionada.classList.remove("hidden");
    }
}



async function carregarDadosUsuario() {
    //busca os id do usuário no localStorage
    const usuarioId = localStorage.getItem("usuarioId");

    try {
        const response = await fetch(`http://localhost:8080/usuarios/${usuarioId}`);
        if (response.ok) {
            const usuario = await response.json();

            document.getElementById("usuario-id").value = usuario.id;
            document.getElementById("nome").value = usuario.nome;
            document.getElementById("email").value = usuario.email;
            document.getElementById("senha").value = usuario.senha;
            document.getElementById("ativo").value = usuario.ativo === 1 ? "Ativo" : "Inativo";

            const sexo = usuario.sexo === 'M' ? 'Masculino' : 'Feminino';
            document.getElementById("sexo").value = sexo;


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

async function carregarDadosCliente() {
    const usuarioId = localStorage.getItem("usuarioId");

    try {
        //tive que mudar pois tava enviando o id do usuário e tava pedindo o id do cliente
        const response = await fetch(`http://localhost:8085/clientes/buscarPorUsuario/${usuarioId}`);
        const cliente = await response.json();

        if (!cliente) {
            alert("Cliente não encontrado.");
            return;
        }

        document.getElementById("cliente-id").value = cliente.id;
        document.getElementById("nascimento").value = cliente.nascimento;
        document.getElementById("cpf").value = cliente.cpf;
        document.getElementById("celular").value = cliente.celular;

        // Carregar produtos associados
        await carregarProdutosAssociados(cliente.id);
    } catch (error) {
        alert("Erro ao carregar dados do cliente: " + error.message);
    }
}


async function carregarProdutosAssociados(clienteId) {
    try {
        //tive que mudar pois tava enviando o id do usuário e tava pedindo o id do cliente
        //tava dando erro e não tava trazendo os produtos vi no stack um código usando .stream().map().collect()
        const response = await fetch(`http://localhost:8085/clienteProduto/produtos-associados/${clienteId}`);
        if (!response.ok) {
            throw new Error("Erro ao buscar produtos associados");
        }

        const produtosIds = await response.json();

        //ver porque se esse if tá ativo ele imprime o alert mesmo trazendo os produtos
        /*if (produtosIds.length === 0) {
            alert("Nenhum produto associado encontrado.");
            return;
        }*/
        //agora que foi pego os produtos associados agora tem que pegar os dados e colocar na tabela
        //pega o serviço de produtos agora as informações
        const produtosPromises = produtosIds.map(id =>
            fetch(`http://localhost:8090/produtos/obterProduto?id=${id}`).then(res => res.json())
        );

        const produtos = await Promise.all(produtosPromises);

        const tabela = document.querySelector("#produtos-table tbody");
        tabela.innerHTML = "";

        produtos.forEach(produto => {
            const row = document.createElement("tr");
            row.innerHTML = `
                <td>${produto.id}</td>
                <td>${produto.titulo}</td>
                <td>${produto.descricao}</td>
                <td>${produto.dataAtivacao || '-'}</td>
                <td>${produto.dataInativacao || '-'}</td>
                <td>${produto.preco}</td>
                <td>${produto.ativo ? "Sim" : "Não"}</td>
            `;
            tabela.appendChild(row);
        });
    } catch (error) {
        alert("Erro ao carregar produtos associados: " + error.message);
    }
}

/* não realizei ainda toda uma implementação dessa função
verificar se atualiza direito as coisas nos dois bancos*/
async function salvarAlteracoes() {
    const idUsuario = document.getElementById("usuario-id").value;
    const idCliente = document.getElementById("cliente-id").value;

    const usuarioAtualizado = {
        nome: document.getElementById("nome").value,
        email: document.getElementById("email").value,
        senha: document.getElementById("senha").value,
        ativo: document.getElementById("ativo").value
    };

    const novoCelular = document.getElementById("celular").value;

    try {
        await fetch(`http://localhos:8080/usuarios/alterar/${idUsuario}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(usuarioAtualizado)
        });
        // só pode ser atualizado o celular do cliente pq o resto são coisas que não muda, excluir a outra função de atualizar
        await fetch(`http://localhost:8085/clientes/atualizarCelular/${idCliente}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(novoCelular)
        });

        alert("Dados atualizados com sucesso!");
    } catch (error) {
        alert("Erro ao salvar alterações: " + error.message);
    }
}

// outra seção da tela que mostra todos os produtos - título - descrição - preço
async function carregarProdutosDisponiveis() {
    try {
        const response = await fetch("http://localhost:8090/produtos/listarProdutos");

        if (!response.ok) {
            throw new Error(`Erro ${response.status}: ${response.statusText}`);
        }

        const produtos = await response.json();

        const produtosLista = document.getElementById("produtos-lista");
        produtosLista.innerHTML = "";

        produtos.forEach(produto => {
            if (produto.ativo) {
                const row = document.createElement("tr");
                row.innerHTML = `
                    <td>${produto.titulo}</td>
                    <td>${produto.descricao}</td>
                    <td>R$ ${produto.preco.toFixed(2)}</td>
                `;
                produtosLista.appendChild(row);
            }
        });
    } catch (error) {
        alert("Erro ao carregar produtos disponíveis: " + error.message);
        console.error(error);
    }
}
