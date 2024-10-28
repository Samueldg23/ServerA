document.getElementById('loginForm').addEventListener('submit', function(event) {
    event.preventDefault();

    const email = document.getElementById('email').value;
    const senha = document.getElementById('senha').value;

    fetch('http://localhost:8080/usuarios/login', { 
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ email: email, senha: senha })
    })
    .then(response => {
        if (response.ok) {
            return response.json(); 
        } else {
            throw new Error('Email ou senha inválidos');
        }
    })
    .then(data => {
        if (data.grupo === 'Administrador') {
            window.location.href = '/admin';
        } else {
            window.location.href = '/cliente';
        }
    })
    .catch(error => {
        alert('Falha no login: ' + error.message);
        console.error('Erro ao realizar login:', error);
    });
});
