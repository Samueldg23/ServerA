// Adiciona evento para capturar o envio do formulário
document.getElementById('loginForm').addEventListener('submit', async function (event) {
    event.preventDefault();

    const email = document.getElementById('email').value;
    const senha = document.getElementById('senha').value;

    try {
        // Envia uma requisição POST para autenticar o usuário
        const response = await fetch('http://localhost:8080/usuarios/login', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ email, senha })
        });

        if (response.ok) {
            const data = await response.json();
            // Armazena informações no localStorage
            localStorage.setItem('usuarioId', data.id);
            localStorage.setItem('usuarioEmail', data.email);
            localStorage.setItem('usuarioGrupo', data.grupo);

            // Redireciona com base no grupo do usuário
            if (data.grupo === 'Administrador') {
                window.location.href = '/admin';
            } else {
                window.location.href = '/cliente';
            }
        } else {
            throw new Error('Email ou senha inválidos');
        }
    } catch (error) {
        // Mostra erro ao usuário
        alert('Falha no login: ' + error.message);
        console.error('Erro ao realizar login:', error);
    }
});

// Alternar visibilidade da senha
document.getElementById('toggleSenha').addEventListener('click', function () {
    const senhaInput = document.getElementById('senha');
    const isPassword = senhaInput.type === 'password';
    senhaInput.type = isPassword ? 'text' : 'password';
    this.classList.toggle('bx-hide');
    this.classList.toggle('bx-show');
});
