document.addEventListener("DOMContentLoaded", function () {
    const produtoForm = document.getElementById("produto-form");
    const produtosUl = document.getElementById("produtos-ul");
    const mensagemSucesso = document.getElementById("mensagem-sucesso");
    //<td>${produto.ativo ? '1' : '0'}</td>
    function carregarProdutos() {
        fetch('http://localhost:8090/listarProdutos')
            .then(response => response.json())
            .then(produtos => {
                produtosUl.innerHTML = '';
                produtos.forEach(produto => {
                    const tr = document.createElement("tr");
                    tr.innerHTML = `
                        <td>${produto.id}</td>
                        <td>${produto.titulo}</td>
                        <td>${produto.descricao}</td>
                        <td>R$ ${produto.preco.toFixed(2)}</td>
        
                        <td>
                            <button onclick="editarProduto(${produto.id})">Editar</button>
                            <button onclick="deletarProduto(${produto.id})">🗑️</button>
                        </td>
                    `;
                    produtosUl.appendChild(tr);
                });
            })
            .catch(error => console.error("Erro ao carregar produtos:", error));
    }

    produtoForm.addEventListener("submit", function (event) {
        event.preventDefault();
        const produtoId = document.getElementById("produto-id").value;
        const titulo = document.getElementById("titulo").value;
        const descricao = document.getElementById("descricao").value;
        //const ativo = 1;
        const preco = document.getElementById("preco").value;
        

        const url = produtoId 
            ? `http://localhost:8090/atualizarProduto?titulo=${titulo}&descricao=${descricao}&ativo=${ativo}&preco=${preco}` 
            : `http://localhost:8090/salvarProduto?titulo=${titulo}&descricao=${descricao}&ativo=${ativo}&preco=${preco}`;
        const method = 'POST';

        fetch(url, { method: method })
            .then(response => {
                if (response.ok) {
                    produtoForm.reset();
                    carregarProdutos();
                    mensagemSucesso.textContent = 'Produto salvo com sucesso!';
                    mensagemSucesso.classList.remove("hidden");
                    setTimeout(() => {
                        mensagemSucesso.classList.add("hidden");
                    }, 3000);
                } else {
                    throw new Error("Erro ao salvar produto");
                }
            })
            .catch(error => {
                console.error("Erro ao salvar produto:", error);
                mensagemSucesso.textContent = 'Erro ao salvar produto!';
                mensagemSucesso.classList.remove("hidden");
                setTimeout(() => {
                    mensagemSucesso.classList.add("hidden");
                }, 3000);
            });
    });

    window.editarProduto = function (id) {
        fetch(`http://localhost:8090/produtos/${id}`)
            .then(response => response.json())
            .then(produto => {
                document.getElementById("produto-id").value = produto.id;
                document.getElementById("titulo").value = produto.titulo;
                document.getElementById("descricao").value = produto.descricao;
                document.getElementById("preco").value = produto.preco;
                document.getElementById("ativo").value = produto.ativo;
            })
            .catch(error => console.error("Erro ao editar produto:", error));
    };

    window.deletarProduto = function (id) {
        if (confirm("Tem certeza que deseja excluir este produto?")) {
            fetch(`http://localhost:8090/deletarProduto?produtoId=${id}`, { method: 'DELETE' })
                .then(() => {
                    carregarProdutos();
                    mensagemSucesso.textContent = 'Produto excluído com sucesso!';
                    mensagemSucesso.classList.remove("hidden");
                    setTimeout(() => {
                        mensagemSucesso.classList.add("hidden");
                    }, 3000);
                })
                .catch(error => console.error("Erro ao excluir produto:", error));
        }
    };

    carregarProdutos();
});
