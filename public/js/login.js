// Funcionalidad de tabs
document.querySelectorAll('.pixel-tab').forEach(tab => {
    tab.addEventListener('click', (e) => {
        e.preventDefault();
        // Remover active de todos los tabs
        document.querySelectorAll('.pixel-tab').forEach(t => t.classList.remove('active'));
        document.querySelectorAll('.pixel-form').forEach(f => f.classList.remove('active'));
        // Activar tab clickeado
        tab.classList.add('active');
        const targetForm = tab.dataset.tab + '-form';
        document.getElementById(targetForm).classList.add('active');
        // Limpiar mensajes
        document.getElementById('mensaje').innerHTML = '';
        document.getElementById('mensaje').className = 'message-area';
    });
});
// Funcionalidad de toggle de contraseñas
document.querySelectorAll('.password-toggle').forEach(toggle => {
    toggle.addEventListener('click', function() {
        const inputName = this.dataset.input;
        const input = this.parentElement.querySelector(`input[name="${inputName}"]`);
        if (input.type === 'password') {
            input.type = 'text';
            this.textContent = '🙈';
            this.style.color = '#FF6347';
        } else {
            input.type = 'password';
            this.textContent = '👁';
            this.style.color = '#FFD700';
        }
    });
});
// Función para efecto épico de entrada
function epicTransition() {
    const loginPanel = document.getElementById('loginPanel');
    const heroSection = document.getElementById('heroSection');
    const overlay = document.getElementById('transitionOverlay');
    // Activar overlay
    overlay.classList.add('active');
    // Añadir clases de animación
    loginPanel.classList.add('zoom-out');
    heroSection.classList.add('hero-charge');
    // Crear texto épico
    const epicText = document.createElement('div');
    epicText.className = 'epic-text';
    epicText.textContent = '¡ADENTRÁNDOTE EN LA AVENTURA!';
    document.body.appendChild(epicText);
    // Eliminar el texto después de la animación
    setTimeout(() => {
        if (epicText.parentNode) {
            epicText.parentNode.removeChild(epicText);
        }
    }, 2000);
}
// Frases motivacionales del héroe
const heroMessages = [
    "¡Hey, aventurero! 🗡️<br>¡La épica te aguarda!<br>¿Listo para la aventura?",
    "¡Vamos, héroe! ⚔️<br>¡Tu destino te espera!<br>¡Adelante!",
    "¡El reino necesita<br>de tu valor! 🛡️<br>¡Únete a nosotros!",
    "¡Grandes tesoros<br>aguardan! 💎<br>¿Te atreves?",
    "¡La aventura épica<br>comienza aquí! 🏰<br>¡Vamos!"
];
// Función para cambiar el mensaje del héroe
function changeSpeechBubble() {
    const bubble = document.getElementById('speechBubble');
    if (bubble) {
        const randomMessage = heroMessages[Math.floor(Math.random() * heroMessages.length)];
        bubble.innerHTML = randomMessage;
        // Añadir una pequeña animación al cambiar
        bubble.style.transform = 'scale(0.9)';
        setTimeout(() => {
            bubble.style.transform = 'scale(1)';
        }, 150);
    }
}
// Cambiar mensaje cada 6 segundos
setInterval(changeSpeechBubble, 6000);
// Tu código JavaScript original (login.js)
window.onload = function () {
    formulario();
}
function formulario() {
    async function enviarFormulario(event, tipo) {
        event.preventDefault();
        const form = event.target;
        const usuario = form.username.value.trim();
        const password = form.password.value.trim();
        const confirmPassword = form.confirmPassword ? form.confirmPassword.value.trim() : null;
        const msgDiv = document.getElementById("mensaje");
        // Validación de campos vacíos
        if (!usuario || !password || (tipo === "register" && !confirmPassword)) {
            msgDiv.innerText = "⚠️ Completa todos los campos, aventurero!";
            msgDiv.className = "message-area message-error";
            return;
        }
        // Validación de contraseña confirmada
        if (tipo === "register" && password !== confirmPassword) {
            msgDiv.innerText = "⚠️ Las claves no coinciden!";
            msgDiv.className = "message-area message-error";
            return;
        }
        const datos = {
            id_usuario: null,
            nombre: usuario,
            password: password
        };
        const ruta = tipo === "login" ? "login" : "register";
        const isEmulator = /Android/i.test(navigator.userAgent);
        const baseUrl = isEmulator ? "http://10.0.2.2:8080/dsaApp" : "https://dsa1.upc.edu/dsaApp";
        // Mostrar loading
        form.classList.add('loading');
        try {
            const res = await fetch(`${baseUrl}/usuarios/${ruta}`, {
                method: "POST",
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(datos)
            });
            const data = await res.json();
            if (res.ok) {
                msgDiv.innerText = "✨ " + (data.mensaje || (tipo === "login" ? "¡Bienvenido, aventurero!" : "¡Héroe registrado con éxito!"));
                msgDiv.className = "message-area message-success";
                if (tipo === "login") {
                    localStorage.setItem("token", data.token);
                    // Activar efecto épico antes de redirigir
                    setTimeout(() => {
                        epicTransition();
                        // Redirigir después del efecto
                        setTimeout(() => {
                            window.location.href = "partida.html";
                        }, 2000);
                    }, 500);
                }
            } else {
                msgDiv.innerText = "💥 " + (data.mensaje || data.error || "Error en la aventura!");
                msgDiv.className = "message-area message-error";
            }
        } catch (error) {
            msgDiv.innerText = "🌐 Error de conexión con el reino!";
            msgDiv.className = "message-area message-error";
        } finally {
            form.classList.remove('loading');
        }
    }
    document.getElementById("loginForm").addEventListener("submit", e => enviarFormulario(e, "login"));
    document.getElementById("registerForm").addEventListener("submit", e => enviarFormulario(e, "register"));
}
// Generar más partículas aleatoriamente
function createRandomParticles() {
    const particles = document.querySelector('.particles');
    for (let i = 0; i < 8; i++) {
        const particle = document.createElement('div');
        particle.className = 'particle';
        particle.style.top = Math.random() * 100 + '%';
        particle.style.left = Math.random() * 100 + '%';
        particle.style.animationDelay = Math.random() * 3 + 's';
        particles.appendChild(particle);
    }
}
createRandomParticles();