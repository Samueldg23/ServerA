document.getElementById('loginForm').addEventListener('submit', async function(event) {
    event.preventDefault();

    const email = document.getElementById('email').value;
    const senha = document.getElementById('senha').value;

    try {
        const response = await fetch('http://localhost:8080/usuarios/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ email, senha })
        });

        if (response.ok) {
            const data = await response.json();
            // Salva as informações no localStorage
            localStorage.setItem('usuarioId', data.id);
            localStorage.setItem('usuarioEmail', data.email);
            localStorage.setItem('usuarioGrupo', data.grupo);

            // Redireciona com base no grupo
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
