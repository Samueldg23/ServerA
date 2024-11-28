// Adiciona evento para capturar o envio do formulário
document.getElementById('loginForm').addEventListener('submit', async function (event) {
    event.preventDefault();

    const email = document.getElementById('email').value;
    const senha = document.getElementById('senha').value;

    try {
        const response = await fetch('http://localhost:8080/usuarios/login', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ email, senha })
        });

        if (response.ok) {
            const data = await response.json();
            localStorage.setItem('usuarioId', data.id);
            localStorage.setItem('usuarioGrupo', data.grupo);

            if (data.grupo === 'Administrador') {
                window.location.href = '/admin';
            } else {
                window.location.href = '/cliente';
            }
        } else {
            throw new Error('Email ou senha inválidos');
        }
    } catch (error) {
        alert('Falha no login: ' + error.message);
        console.error('Erro ao realizar login:', error);
    }
});

document.getElementById('toggleSenha').addEventListener('click', function () {
    const senhaInput = document.getElementById('senha');
    const isPassword = senhaInput.type === 'password';
    senhaInput.type = isPassword ? 'text' : 'password';
    this.classList.toggle('bx-hide');
    this.classList.toggle('bx-show');
});
