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
      msgDiv.innerText = "Por favor completa todos los campos.";
      msgDiv.className = "text-danger";
      return;
    }

    // Validación de contraseña confirmada
    if (tipo === "register" && password !== confirmPassword) {
      msgDiv.innerText = "Las contraseñas no coinciden.";
      msgDiv.className = "text-danger";
      return;
    }

    const datos = {
      id_usuario: null,
      nombre: usuario,
      password: password
    };

    const ruta = tipo === "login" ? "login" : "register";
    const isEmulator = /Android/i.test(navigator.userAgent);
    const baseUrl = isEmulator ? "http://10.0.2.2:8080/dsaApp" : "http://localhost:8080/dsaApp";

    try {
      const res = await fetch(`${baseUrl}/usuarios/${ruta}`, {
        method: "POST",
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(datos)
      });

      const data = await res.json();

      if (res.ok) {
        msgDiv.innerText = data.mensaje || (tipo === "login" ? "Inicio de sesión exitoso." : "Registro exitoso.");
        msgDiv.className = "text-success";

        if (tipo === "login") {
          localStorage.setItem("token", data.token);
          setTimeout(() => {
            window.location.href = "partida.html";
          }, 1000);
        }
      } else {
        msgDiv.innerText = data.mensaje || data.error || "Error al procesar la solicitud.";
        msgDiv.className = "text-danger";
      }
    } catch (error) {
      msgDiv.innerText = "Error de conexión con el servidor.";
      msgDiv.className = "text-danger";
    }
  }

  document.getElementById("loginForm").addEventListener("submit", e => enviarFormulario(e, "login"));
  document.getElementById("registerForm").addEventListener("submit", e => enviarFormulario(e, "register"));
}


