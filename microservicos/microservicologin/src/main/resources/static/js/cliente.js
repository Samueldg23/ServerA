// Alterna entre as seções com base no menu selecionado
document.getElementById("perfil-link").addEventListener("click", function () {
    toggleSection("perfil-section");
});
document.getElementById("produtos-link").addEventListener("click", function () {
    toggleSection("produtos-section");
});

function toggleSection(sectionId) {
    document.querySelectorAll(".section").forEach(section => {
        section.classList.add("hidden");
    });
    document.getElementById(sectionId).classList.remove("hidden");
}

// Exemplo de carregamento e exibição de dados do perfil
document.getElementById("perfil-form").addEventListener("submit", function (e) {
    e.preventDefault();
    // Lógica para salvar alterações do perfil
    alert("Perfil atualizado com sucesso!");
});

// Carregar produtos associados e exibir na tabela
function carregarProdutosAssociados() {
    const produtos = [
        { titulo: "Produto A", dataAtivacao: "2024-01-01", dataInativacao: "N/A", preco: "100.00" },
        { titulo: "Produto B", dataAtivacao: "2024-02-01", dataInativacao: "2024-03-01", preco: "150.00" }
    ];
    const tabela = document.getElementById("produtos-associados-lista");
    produtos.forEach(produto => {
        const row = document.createElement("tr");
        row.innerHTML = `
            <td>${produto.titulo}</td>
            <td>${produto.dataAtivacao}</td>
            <td>${produto.dataInativacao}</td>
            <td>${produto.preco}</td>
        `;
        tabela.appendChild(row);
    });
}

// Carregar produtos disponíveis e exibir na tabela
function carregarProdutosDisponiveis() {
    const produtos = [
        { titulo: "Produto C", descricao: "Descrição do produto C", preco: "200.00" },
        { titulo: "Produto D", descricao: "Descrição do produto D", preco: "250.00" }
    ];
    const tabela = document.getElementById("produtos-lista");
    produtos.forEach(produto => {
        const row = document.createElement("tr");
        row.innerHTML = `
            <td>${produto.titulo}</td>
            <td>${produto.descricao}</td>
            <td>${produto.preco}</td>
        `;
        tabela.appendChild(row);
    });
}

// Chamadas para carregar os dados
carregarProdutosAssociados();
carregarProdutosDisponiveis();
