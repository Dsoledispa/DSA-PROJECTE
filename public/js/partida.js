const isEmulator = /Android/i.test(navigator.userAgent);
const BASE_URL = isEmulator ? "http://10.0.2.2:8080/dsaApp" : "https://dsa1.upc.edu/dsaApp";
const token = localStorage.getItem("token");

let currentUser = null;
let userInsignias = [];
let allInsignias = [];
let pendingAction = null;

// === FUNCIONES DE UI ===

function mostrarSpinner() {
    $("#spinner").removeClass('d-none');
}

function ocultarSpinner() {
    $("#spinner").addClass('d-none');
}

// Sistema de notificaciones mejorado
function mostrarNotificacion(mensaje, tipo = 'success') {
    const notificationSystem = $('#notificationSystem');
    const icon = notificationSystem.find('.notification-icon');
    const text = notificationSystem.find('.notification-message');

    // Configurar icono según el tipo
    if (tipo === 'success') {
        icon.text('✓');
        icon.css('color', 'var(--gold-bright)');
    } else if (tipo === 'error') {
        icon.text('⚠');
        icon.css('color', 'var(--red-medium)');
    } else if (tipo === 'info') {
        icon.text('ℹ');
        icon.css('color', 'var(--gold-main)');
    }

    text.text(mensaje);
    notificationSystem.addClass('show');

    // Auto-ocultar después de 4 segundos
    setTimeout(() => {
        notificationSystem.removeClass('show');
    }, 4000);
}

// Modal de confirmación mejorado
function mostrarModalConfirmacion(titulo, mensaje, callback = null, requiereTexto = false) {
    const modal = $('#confirmationModal');
    const overlay = $('#modalOverlay');
    const titleEl = $('#modalTitle');
    const messageEl = $('#modalMessage');
    const confirmInput = $('#confirmationInput');
    const confirmBtn = $('#confirmAction');
    const cancelBtn = $('#cancelAction');
    const textInput = $('#confirmText');

    titleEl.text(titulo);
    messageEl.text(mensaje);

    if (requiereTexto) {
        confirmInput.show();
        confirmBtn.prop('disabled', true);
        textInput.val('').focus();
    } else {
        confirmInput.hide();
        confirmBtn.prop('disabled', false);
    }

    // Mostrar modal
    modal.addClass('show');

    // Guardar callback
    pendingAction = callback;

    // Limpiar event listeners anteriores
    confirmBtn.off('click');
    cancelBtn.off('click');
    overlay.off('click');
    textInput.off('input');

    // Event listeners
    confirmBtn.on('click', function() {
        modal.removeClass('show');
        if (pendingAction) {
            pendingAction();
            pendingAction = null;
        }
    });

    cancelBtn.on('click', function() {
        modal.removeClass('show');
        pendingAction = null;
    });

    overlay.on('click', function() {
        modal.removeClass('show');
        pendingAction = null;
    });

    // Validación del campo de texto
    if (requiereTexto) {
        textInput.on('input', function() {
            const valor = $(this).val().toUpperCase();
            if (valor === 'BORRAR') {
                confirmBtn.prop('disabled', false);
            } else {
                confirmBtn.prop('disabled', true);
            }
        });
    }
}

// Toggle del panel de perfil
function togglePerfilPanel() {
    const panel = $('#profilePanel');
    if (panel.is(':visible')) {
        panel.fadeOut(300);
    } else {
        panel.fadeIn(300);
    }
}

// === FUNCIONES DE API ===

function ajaxConToken(opciones) {
    opciones.headers = opciones.headers || {};
    opciones.headers["Authorization"] = "Bearer " + token;
    mostrarSpinner();
    return $.ajax(opciones).always(ocultarSpinner);
}

// Cargar información del usuario
function cargarInfoUsuario() {
    try {
        const payload = JSON.parse(atob(token.split('.')[1]));
        const userId = payload.sub;

        ajaxConToken({
            url: `${BASE_URL}/usuarios/${userId}`,
            method: 'GET'
        }).done(function(usuario) {
            currentUser = usuario;
            $('#username-display').text(usuario.nombre);
            $('#heroName').text(usuario.nombre);
        }).fail(function(xhr) {
            $('#username-display').text(userId);
            $('#heroName').text(userId);
            currentUser = { id_usuario: userId, nombre: userId };
        });
    } catch (e) {
        console.error("Error al decodificar token:", e);
        mostrarNotificacion("Error al cargar perfil de usuario", "error");
    }
}

// Cargar todas las insignias disponibles
function cargarTodasInsignias() {
    ajaxConToken({
        url: `${BASE_URL}/insignia`,
        method: 'GET'
    }).done(function(insignias) {
        allInsignias = insignias;
        cargarInsigniasUsuario();
    }).fail(function(xhr) {
        console.error("Error al cargar insignias:", xhr.responseText);
        mostrarNotificacion("Error al cargar insignias", "error");
    });
}

// Cargar insignias del usuario
function cargarInsigniasUsuario() {
    ajaxConToken({
        url: `${BASE_URL}/Usuario_Insignia`,
        method: 'GET'
    }).done(function(insignias) {
        userInsignias = insignias;
        $('#insignia-count').text(insignias.length);
        $('#totalInsignias').text(insignias.length);
        mostrarInsigniasEnPanel();
    }).fail(function(xhr) {
        console.error("Error al cargar insignias del usuario:", xhr.responseText);
    });
}

// Mostrar insignias en el panel de perfil
function mostrarInsigniasEnPanel() {
    const grid = $('#insigniasGrid');
    grid.empty();

    allInsignias.forEach(insignia => {
        const tieneInsignia = userInsignias.some(ui => ui.id_insignia === insignia.id_insignia);

        const insigniaHtml = `
            <div class="achievement-item ${tieneInsignia ? 'unlocked' : 'locked'}">
                <img src="${insignia.avatar}" alt="${insignia.nombre}" class="achievement-icon"
                     onerror="this.src='img/insignia-default.png'">
                <div class="achievement-name">${insignia.nombre}</div>
            </div>
        `;

        grid.append(insigniaHtml);
    });
}

// === FUNCIONES DE PARTIDAS ===

// Crear nueva partida
function crearPartida() {
    ajaxConToken({
        url: BASE_URL + "/partidas",
        type: "POST"
    }).done(() => {
        mostrarNotificacion("¡Nueva aventura creada exitosamente!");
        mostrarPartidas();
    }).fail(function(xhr) {
        console.error("Error:", xhr.responseText);
        let mensaje = "Error al crear nueva partida";
        try {
            mensaje = JSON.parse(xhr.responseText).error || mensaje;
        } catch (e) {}
        mostrarNotificacion(mensaje, "error");
    });
}

// Obtener y mostrar partidas
function mostrarPartidas() {
    ajaxConToken({
        url: BASE_URL + "/partidas",
        type: "GET"
    }).done(partidas => {
        renderizarPartidas(partidas);
    }).fail(function(xhr) {
        console.error("Error:", xhr.responseText);
        let mensaje = "Error al cargar partidas";
        try {
            mensaje = JSON.parse(xhr.responseText).error || mensaje;
        } catch (e) {}
        mostrarNotificacion(mensaje, "error");
    });
}

// Renderizar partidas en el DOM
function renderizarPartidas(partidas) {
    const contenedor = $("#contenedor-partidas");
    contenedor.empty();

    if (!partidas || partidas.length === 0) {
        contenedor.html(`
            <div class="empty-state">
                <div class="empty-icon">📜</div>
                <div class="empty-title">No hay aventuras disponibles</div>
                <div class="empty-subtitle">¡Crea tu primera misión épica!</div>
            </div>
        `);
        return;
    }

    partidas.forEach((partida, index) => {
        const cardHtml = `
            <div class="mission-card">
                <div class="mission-header">
                    <div class="mission-title">Aventura #${index + 1}</div>
                    <div class="mission-id-container">
                        <div class="mission-id">id: ${partida.id_partida}</div>
                        <div class="mission-number">#${String(index + 1).padStart(2, '0')}</div>
                    </div>
                </div>

                <div class="mission-stats">
                    <div class="stat-item">
                        <span class="stat-icon">❤️</span>
                        <span class="stat-label">Vidas</span>
                        <span class="stat-value">${partida.vidas}</span>
                    </div>
                    <div class="stat-item">
                        <span class="stat-icon">💰</span>
                        <span class="stat-label">Oro</span>
                        <span class="stat-value">${partida.monedas}</span>
                    </div>
                    <div class="stat-item">
                        <span class="stat-icon">⭐</span>
                        <span class="stat-label">Puntos</span>
                        <span class="stat-value">${partida.puntuacion}</span>
                    </div>
                </div>

                <div class="mission-actions">
                    <button class="mission-btn btn-shop" data-id="${partida.id_partida}">
                        🏪 Tienda
                    </button>
                    <button class="mission-btn btn-inventory" data-id="${partida.id_partida}">
                        🎒 Inventario
                    </button>
                    <button class="mission-btn btn-delete" data-id="${partida.id_partida}">
                        🗑️ Eliminar
                    </button>
                </div>
            </div>
        `;

        contenedor.append(cardHtml);
    });
}

// Eliminar partida
function eliminarPartida(id) {
    ajaxConToken({
        url: BASE_URL + "/partidas/" + id,
        type: "DELETE"
    }).done(() => {
        mostrarNotificacion("Aventura eliminada correctamente");
        mostrarPartidas();
    }).fail(function(xhr) {
        console.error("Error:", xhr.responseText);
        let mensaje = "Error al eliminar la partida";
        try {
            mensaje = JSON.parse(xhr.responseText).error || mensaje;
        } catch (e) {}
        mostrarNotificacion(mensaje, "error");
    });
}

// Cerrar sesión
function cerrarSesion() {
    localStorage.removeItem("token");
    mostrarNotificacion("Sesión cerrada. ¡Hasta la próxima aventura!");
    setTimeout(() => {
        window.location.href = "/";
    }, 2000);
}

// === EVENT LISTENERS ===

$(document).ready(function() {
    // Cargar datos iniciales
    cargarInfoUsuario();
    cargarTodasInsignias();
    mostrarPartidas();

    // Toggle panel de perfil
    $('#userProfile').on('click', function(e) {
        e.stopPropagation();
        togglePerfilPanel();
    });

    // Cerrar panel de perfil
    $('#closePanel').on('click', function() {
        $('#profilePanel').fadeOut(300);
    });

    // Cerrar panel al hacer clic fuera
    $(document).on('click', function(e) {
        if (!$(e.target).closest('#userProfile, #profilePanel').length) {
            $('#profilePanel').fadeOut(300);
        }
    });

    // Crear nueva partida
    $("#crear").on("click", function() {
        crearPartida();
    });

    // Salir de la taberna
    $("#volver").on("click", function() {
        mostrarModalConfirmacion(
            "🚪 Abandonar la Taberna",
            "¿Estás seguro de que quieres cerrar sesión y salir de la taberna?",
            cerrarSesion,
            false
        );
    });

    // Delegación de eventos para botones dinámicos
    $(document).on("click", ".btn-delete", function() {
        const id = $(this).data("id");
        const numeroPartida = $(this).closest('.mission-card').find('.mission-title').text();

        mostrarModalConfirmacion(
            "⚠️ Eliminar Aventura",
            `Estás a punto de eliminar permanentemente la "${numeroPartida}". Esta acción no se puede deshacer.`,
            () => eliminarPartida(id),
            true // Requiere escribir "BORRAR"
        );
    });

    $(document).on("click", ".btn-shop", function() {
        const id = $(this).data("id");
        mostrarNotificacion("Dirigiéndose a la tienda...", "info");
        setTimeout(() => {
            window.location.href = `tienda.html?id_partida=${id}`;
        }, 500);
    });

    $(document).on("click", ".btn-inventory", function() {
        const id = $(this).data("id");
        mostrarNotificacion("Abriendo inventario...", "info");
        setTimeout(() => {
            window.location.href = `inventario.html?id_partida=${id}`;
        }, 500);
    });

    // Manejo de teclas para el modal
    $(document).on('keydown', function(e) {
        const modal = $('#confirmationModal');
        if (modal.hasClass('show')) {
            if (e.key === 'Escape') {
                modal.removeClass('show');
                pendingAction = null;
            } else if (e.key === 'Enter') {
                const confirmBtn = $('#confirmAction');
                if (!confirmBtn.prop('disabled')) {
                    confirmBtn.click();
                }
            }
        }
    });

    // Manejo de errores globales
    $(document).ajaxError(function(event, xhr, settings) {
        if (xhr.status === 401) {
            mostrarNotificacion("Sesión expirada. Redirigiendo al login...", "error");
            setTimeout(() => {
                localStorage.removeItem("token");
                window.location.href = "/";
            }, 2000);
        } else if (xhr.status === 0) {
            mostrarNotificacion("Error de conexión con el servidor", "error");
        }
    });

    // Efectos adicionales al cargar
    setTimeout(() => {
        $('.mission-card').each(function(index) {
            $(this).delay(index * 100).animate({
                opacity: 1
            }, 300);
        });
    }, 500);
});

// === FUNCIONES DE UTILIDAD ===

// Manejo de errores mejorado
function manejarError(xhr, status, error) {
    console.error("Error AJAX:", {
        status: xhr.status,
        statusText: xhr.statusText,
        responseText: xhr.responseText,
        error: error
    });

    let mensaje = "Ha ocurrido un error inesperado";

    try {
        const response = JSON.parse(xhr.responseText);
        mensaje = response.error || response.mensaje || mensaje;
    } catch (e) {
        // Si no se puede parsear JSON, usar mensaje genérico
        if (xhr.status === 404) {
            mensaje = "Recurso no encontrado";
        } else if (xhr.status === 500) {
            mensaje = "Error interno del servidor";
        } else if (xhr.status === 403) {
            mensaje = "No tienes permisos para realizar esta acción";
        }
    }

    mostrarNotificacion(mensaje, "error");
}

// Formatear números
function formatearNumero(numero) {
    if (numero >= 1000000) {
        return (numero / 1000000).toFixed(1) + 'M';
    } else if (numero >= 1000) {
        return (numero / 1000).toFixed(1) + 'K';
    }
    return numero.toString();
}

// Obtener rango basado en puntuación
function obtenerRango(puntuacion) {
    if (puntuacion >= 10000) return { nombre: "LEYENDA", color: "#FFD700" };
    if (puntuacion >= 5000) return { nombre: "MAESTRO", color: "#9370DB" };
    if (puntuacion >= 2000) return { nombre: "EXPERTO", color: "#FF6347" };
    if (puntuacion >= 1000) return { nombre: "VETERANO", color: "#32CD32" };
    if (puntuacion >= 500) return { nombre: "AVENTURERO", color: "#4169E1" };
    if (puntuacion >= 100) return { nombre: "NOVATO", color: "#FFA500" };
    return { nombre: "PRINCIPIANTE", color: "#808080" };
}

// Validar token
function validarToken() {
    if (!token) {
        window.location.href = "/";
        return false;
    }

    try {
        const payload = JSON.parse(atob(token.split('.')[1]));
        const exp = payload.exp * 1000; // Convertir a milisegundos

        if (Date.now() >= exp) {
            localStorage.removeItem("token");
            mostrarNotificacion("Tu sesión ha expirado", "error");
            setTimeout(() => {
                window.location.href = "/";
            }, 2000);
            return false;
        }

        return true;
    } catch (e) {
        localStorage.removeItem("token");
        window.location.href = "/";
        return false;
    }
}

// === FUNCIONES DEL MODAL DE ID ===

function mostrarModalId(idPartida) {
    const modal = $('#idModal');
    const idValue = $('#idModalValue');

    // Establecer el ID en el modal
    idValue.text(idPartida);

    // Mostrar modal
    modal.addClass('show');

    // Focus para accesibilidad
    $('#idModalClose').focus();
}

function cerrarModalId() {
    const modal = $('#idModal');
    modal.removeClass('show');
}

function copiarIdAlPortapapeles(idPartida) {
    const copyBtn = $('#idCopyBtn');

    // Usar la API moderna de clipboard si está disponible
    if (navigator.clipboard && window.isSecureContext) {
        navigator.clipboard.writeText(idPartida).then(() => {
            mostrarFeedbackCopia(copyBtn);
        }).catch(() => {
            // Fallback si falla
            copiarIdFallback(idPartida, copyBtn);
        });
    } else {
        // Fallback para navegadores más antiguos
        copiarIdFallback(idPartida, copyBtn);
    }
}

function copiarIdFallback(idPartida, copyBtn) {
    // Crear elemento temporal
    const tempInput = $('<input>');
    $('body').append(tempInput);
    tempInput.val(idPartida).select();

    try {
        document.execCommand('copy');
        mostrarFeedbackCopia(copyBtn);
    } catch (err) {
        console.error('Error al copiar:', err);
        mostrarNotificacion("No se pudo copiar el ID", "error");
    }

    tempInput.remove();
}

function mostrarFeedbackCopia(copyBtn) {
    // Cambiar apariencia del botón temporalmente
    const textoOriginal = copyBtn.text();
    copyBtn.addClass('copied');
    copyBtn.text('✓ Copiado!');

    // Mostrar notificación
    mostrarNotificacion("ID copiado al portapapeles", "success");

    // Restaurar después de 2 segundos
    setTimeout(() => {
        copyBtn.removeClass('copied');
        copyBtn.text(textoOriginal);
    }, 2000);
}

// === EVENT LISTENERS PARA EL MODAL ===

$(document).ready(function() {
    // ... tu código existente ...

    // Event listener para abrir modal al hacer clic en ID
    $(document).on('click', '.mission-id', function(e) {
        e.stopPropagation();
        const missionCard = $(this).closest('.mission-card');
        const idPartida = missionCard.find('.btn-shop').data('id'); // Obtener ID del botón
        mostrarModalId(idPartida);
    });

    // Cerrar modal con botón X
    $('#idModalClose').on('click', function() {
        cerrarModalId();
    });

    // Cerrar modal haciendo clic fuera
    $('#idModalOverlay').on('click', function() {
        cerrarModalId();
    });

    // Cerrar modal con tecla ESC
    $(document).on('keydown', function(e) {
        if (e.key === 'Escape' && $('#idModal').hasClass('show')) {
            cerrarModalId();
        }
    });

    // Copiar ID al portapapeles
    $('#idCopyBtn').on('click', function() {
        const idPartida = $('#idModalValue').text();
        copiarIdAlPortapapeles(idPartida);
    });

    // Prevenir que se cierre el modal al hacer clic dentro del contenido
    $('.id-modal-content').on('click', function(e) {
        e.stopPropagation();
    });
});

// Validar token al cargar la página
validarToken();