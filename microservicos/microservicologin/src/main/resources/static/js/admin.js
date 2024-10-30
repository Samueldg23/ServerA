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
    function carregarProdutos() { /*... */ }
    function carregarClientes() { /*... */ }

    // Listeners para os formulários de CRUD e associar/desassociar
    document.getElementById("produto-form").addEventListener("submit", /*... */);
    document.getElementById("perfil-form").addEventListener("submit", /*... */);
    window.associarProduto = function () { /*... */ };
    window.desassociarProduto = function () { /*... */ };
});
