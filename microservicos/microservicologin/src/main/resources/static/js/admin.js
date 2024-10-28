document.addEventListener("DOMContentLoaded", function () {
    const sections = {
        produtos: document.getElementById("produtos-section"),
        clientes: document.getElementById("clientes-section"),
        perfil: document.getElementById("perfil-section"),
    };

    document.getElementById("produtos-link").addEventListener("click", function () {
        showSection("produtos");
    });

    document.getElementById("clientes-link").addEventListener("click", function () {
        showSection("clientes");
    });

    document.getElementById("perfil-link").addEventListener("click", function () {
        showSection("perfil");
    });

    function showSection(section) {
        Object.keys(sections).forEach((key) => {
            sections[key].classList.remove("active");
            sections[key].classList.add("hidden");
        });
        sections[section].classList.add("active");
        sections[section].classList.remove("hidden");
    }

    showSection("produtos");
});
