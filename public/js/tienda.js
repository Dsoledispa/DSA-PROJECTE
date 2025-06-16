//----Estructura basica
const isEmulator = /Android/i.test(navigator.userAgent);
const BASE_URL = isEmulator ? "http://10.0.2.2:8080/dsaApp" : "http://localhost:8080/dsaApp";
const token = localStorage.getItem("token");
const idPartida = new URLSearchParams(window.location.search).get("id_partida");

// Estado global de la tienda
let currentCategory = "";
let cartItems = [];
let availableCoins = 0;

function mostrarSpinner() {
  $("#spinner").removeClass('d-none');
}

function ocultarSpinner() {
  $("#spinner").addClass('d-none');
}

// === SISTEMA DE SONIDOS ===
function reproducirSonido(nombreArchivo) {
  try {
    const audio = new Audio(`sounds/${nombreArchivo}`);
    audio.volume = 0.7; // Volumen al 70%
    audio.play().catch(error => {
      console.log("No se pudo reproducir el sonido:", error);
    });
  } catch (error) {
    console.log("Error al cargar el sonido:", error);
  }
}

// Sistema de notificaciones mejorado
function mostrarNotificacion(mensaje, tipo = 'success', duracion = 4000) {
  const notificationSystem = $('#notificationSystem');
  const template = $('#notificationTemplate').prop('content');
  const notification = $(template.cloneNode(true)).find('.notification');

  // Configurar icono según el tipo
  const icon = notification.find('.notification-icon');
  const text = notification.find('.notification-text');

  switch(tipo) {
    case 'success':
      icon.text('✅');
      notification.addClass('success');
      break;
    case 'error':
      icon.text('❌');
      notification.addClass('error');
      break;
    case 'info':
      icon.text('ℹ️');
      notification.addClass('info');
      break;
    case 'warning':
      icon.text('⚠️');
      notification.addClass('warning');
      break;
  }

  text.text(mensaje);
  notificationSystem.append(notification);

  // Mostrar con animación
  setTimeout(() => notification.addClass('show'), 100);

  // Auto-ocultar
  setTimeout(() => {
    notification.removeClass('show');
    setTimeout(() => notification.remove(), 300);
  }, duracion);
}

// Manejo de errores centralizado mejorado
function manejarError(xhr, status, error) {
  console.error("Error:", xhr.responseText);
  let mensaje = "Error desconocido.";
  try {
    const response = JSON.parse(xhr.responseText);
    mensaje = response.error || response.mensaje || mensaje;
  } catch (e) {
    if (xhr.status === 404) {
      mensaje = "Recurso no encontrado";
    } else if (xhr.status === 500) {
      mensaje = "Error del servidor";
    } else if (xhr.status === 403) {
      mensaje = "No tienes permisos";
    }
  }
  mostrarNotificacion(`💥 ${mensaje}`, 'error');
}

// AJAX con token y spinner automático
function ajaxConToken(opciones) {
  opciones.headers = opciones.headers || {};
  opciones.headers["Authorization"] = "Bearer " + token;
  mostrarSpinner();
  return $.ajax(opciones).always(ocultarSpinner);
}

// Funciones de animación y efectos visuales
function animateProductCard(card) {
  card.css('transform', 'scale(0.95)');
  setTimeout(() => {
    card.css('transform', 'scale(1)');
  }, 150);
}

function animateCartButton() {
  const cartBtn = $('#btnCarrito');
  cartBtn.addClass('animate__animated animate__bounceIn');
  setTimeout(() => {
    cartBtn.removeClass('animate__animated animate__bounceIn');
  }, 600);
}

// Actualizar contador del carrito
function actualizarContadorCarrito() {
  const cartCount = $('#cartCount');
  const count = cartItems.length;

  if (count > 0) {
    cartCount.text(count).removeClass('d-none');
    animateCartButton();
  } else {
    cartCount.addClass('d-none');
  }
}

// Función principal de inicialización
$(document).ready(function () {
  console.log("🏪 Iniciando Tienda del Guerrero...");

  // Verificar que tenemos id_partida
  if (!idPartida) {
    mostrarNotificacion("⚠️ No se encontró la partida. Regresando...", 'warning');
    setTimeout(() => {
      window.location.href = "partida.html";
    }, 2000);
    return;
  }

  // Inicializar componentes
  cargarCategorias();
  cargarCarrito();
  cargarMonedas();
  configurarEventListeners();

  // Mostrar mensaje de bienvenida
  setTimeout(() => {
    mostrarNotificacion("🛡️ ¡Bienvenido a la Tienda del Guerrero!", 'success');
  }, 1000);
});

// Configurar todos los event listeners
function configurarEventListeners() {
  // Botón del carrito
  $("#btnCarrito").click(() => {
    mostrarCarrito();
  });

  // Cerrar carrito
  $("#cartClose, #cartOverlay").click(() => {
    ocultarCarrito();
  });

  // Comprar
  $("#comprarBtn").click(() => {
    realizarCompra();
  });

  // Vaciar carrito
  $("#clearCartBtn").click(() => {
    vaciarCarrito();
  });

  // Volver a partida
  $("#irPartidaBtn").click(() => {
    mostrarNotificacion("🏰 Regresando a la aventura...", 'info', 2000);
    setTimeout(() => {
      window.location.href = "partida.html";
    }, 1000);
  });

  // Cerrar modal con ESC
  $(document).keydown(function(e) {
    if (e.key === 'Escape') {
      ocultarCarrito();
      cerrarModalConfirmacion();
    }
  });
}

// Cargar monedas disponibles
function cargarMonedas() {
  ajaxConToken({
    url: `${BASE_URL}/partidas/${idPartida}/monedas`,
    method: "GET"
  }).done(data => {
    availableCoins = data.monedas;
    $("#monedasCantidad").text(`${data.monedas} monedas`);

    // Animación de monedas
    const moneyDisplay = $('.money-display');
    moneyDisplay.addClass('animate__animated animate__pulse');
    setTimeout(() => {
      moneyDisplay.removeClass('animate__animated animate__pulse');
    }, 1000);
  }).fail(manejarError);
}

// Cargar categorías con diseño mejorado
function cargarCategorias() {
  ajaxConToken({
    url: `${BASE_URL}/categoria`,
    method: "GET"
  }).done(categorias => {
    const container = $("#categorias");
    container.empty();

    // Botón "Todos"
    const todosBtn = crearBotonCategoria("", "🎯 Todos", true);
    container.append(todosBtn);

    // Botones de categorías con iconos
    const iconos = {
      'ARMAS': '⚔️',
      'ARMADURAS': '🛡️',
      'POCIONES': '🧪'
    };

    categorias.forEach(cat => {
      const icono = iconos[cat.nombre.toUpperCase()] || '📦';
      const btn = crearBotonCategoria(cat.id_categoria, `${icono} ${cat.nombre}`);
      container.append(btn);
    });

    // Cargar todos los productos por defecto
    cargarProductos();
  }).fail(manejarError);
}

// Crear botón de categoría
function crearBotonCategoria(id, texto, activo = false) {
  const btn = $(`<button class="category-btn ${activo ? 'active' : ''}" data-cat="${id}">
    ${texto}
  </button>`);

  btn.click(function () {
    $('.category-btn').removeClass('active');
    $(this).addClass('active');
    currentCategory = id;
    cargarProductos(id);

    // Efecto visual
    $(this).css('transform', 'scale(0.95)');
    setTimeout(() => {
      $(this).css('transform', 'scale(1)');
    }, 150);
  });

  return btn;
}

// Cargar productos con animaciones
function cargarProductos(categoria = "") {
  const url = categoria ?
    `${BASE_URL}/tienda/productos/categoria/${categoria}` :
    `${BASE_URL}/tienda/productos`;

  ajaxConToken({
    url: url,
    method: "GET"
  }).done(productos => {
    console.log("📦 Productos cargados:", productos);
    renderizarProductos(productos);
  }).fail(manejarError);
}

// Renderizar productos con template y animaciones
function renderizarProductos(productos) {
  const container = $("#productos");
  container.empty();

  if (!productos || productos.length === 0) {
    container.html(`
      <div class="empty-shop">
        <div class="empty-icon">📭</div>
        <div class="empty-message">No hay productos disponibles</div>
        <div class="empty-submessage">El mercader está reabasteciendo su inventario...</div>
      </div>
    `);
    return;
  }

  // Mapeo de imágenes
  const imageMap = {
    'Espada': 'img/espada.jpg',
    'Armadura': 'img/armadura.png',
    'Poción': 'img/pocion.png'
  };

  productos.forEach((producto, index) => {
    const card = crearTarjetaProducto(producto, imageMap);
    container.append(card);

    // Animación escalonada
    setTimeout(() => {
      card.css('opacity', '1');
      card.css('transform', 'translateY(0)');
    }, index * 100);
  });
}

// Crear tarjeta de producto
function crearTarjetaProducto(producto, imageMap) {
  const template = $('#productTemplate').prop('content');
  const card = $(template.cloneNode(true)).find('.product-card');

  // Configurar imagen
  const img = card.find('.product-img');
  const imageSrc = imageMap[producto.nombre] || producto.imagen || 'img/objeto.png';
  img.attr('src', imageSrc).attr('alt', producto.nombre);

  // Configurar contenido
  card.find('.product-name').text(producto.nombre);
  card.find('.product-description').text(producto.descripcion);
  card.find('.price-value').text(producto.precio);
  card.find('.product-category').text(producto.categoria.nombre);

  // Configurar botón
  const btn = card.find('.add-to-cart-btn');
  btn.attr('data-id', producto.id_objeto);

  // Verificar si se puede comprar
  if (producto.precio > availableCoins) {
    btn.addClass('disabled').text('💸 Sin fondos');
    card.addClass('no-funds');
  }

  // Event listener del botón
  btn.click(function() {
    if (!$(this).hasClass('disabled')) {
      agregarAlCarrito(producto.id_objeto);
      animateProductCard(card);
    }
  });

  // Estilo inicial para animación
  card.css({
    'opacity': '0',
    'transform': 'translateY(20px)',
    'transition': 'all 0.3s ease'
  });

  return card;
}

// Agregar producto al carrito
function agregarAlCarrito(idObjeto) {
  ajaxConToken({
    url: `${BASE_URL}/carrito/${idPartida}/${idObjeto}`,
    method: "POST"
  }).done(() => {
    mostrarNotificacion("🛒 ¡Producto añadido al carrito!", 'success');
    cargarCarrito();
  }).fail(manejarError);
}

// Cargar carrito mejorado
function cargarCarrito() {
  ajaxConToken({
    url: `${BASE_URL}/carrito/${idPartida}`,
    method: "GET"
  }).done(objetos => {
    cartItems = objetos;
    actualizarContadorCarrito();
    renderizarCarrito(objetos);
  }).fail(manejarError);
}

// Renderizar carrito
function renderizarCarrito(objetos) {
  const tbody = $("#tablaCarrito");
  tbody.empty();

  let total = 0;

  // Mapeo de imágenes
  const imageMap = {
    'Espada': 'img/espada.jpg',
    'Armadura': 'img/armadura.png',
    'Poción': 'img/pocion.png'
  };

  objetos.forEach(obj => {
    total += obj.precio;

    const template = $('#cartItemTemplate').prop('content');
    const row = $(template.cloneNode(true)).find('.cart-item');

    // Configurar imagen
    const img = row.find('.cart-item-image');
    const imageSrc = imageMap[obj.nombre] || obj.imagen || 'img/objeto.png';
    img.attr('src', imageSrc).attr('alt', obj.nombre);

    // Configurar contenido
    row.find('.cart-item-name').text(obj.nombre);
    row.find('.item-price').text(`${obj.precio} 💰`);
    row.find('.item-category').text(obj.categoria.nombre);

    // Configurar botón eliminar
    const removeBtn = row.find('.remove-item');
    removeBtn.attr('data-id', obj.id_objeto);
    removeBtn.click(function() {
      eliminarDelCarrito(obj.id_objeto);
    });

    tbody.append(row);
  });

  $("#totalCarrito").text(total);

  // Habilitar/deshabilitar botón de compra
  const comprarBtn = $("#comprarBtn");
  if (total > availableCoins) {
    comprarBtn.addClass('disabled').text('💸 Fondos insuficientes');
  } else {
    comprarBtn.removeClass('disabled').text('⚡ Realizar Compra');
  }
}

// Eliminar del carrito
function eliminarDelCarrito(idObjeto) {
  ajaxConToken({
    url: `${BASE_URL}/carrito/${idPartida}/${idObjeto}`,
    method: "DELETE"
  }).done(() => {
    mostrarNotificacion("🗑️ Producto eliminado del carrito", 'info');
    cargarCarrito();
  }).fail(manejarError);
}

// Mostrar modal del carrito
function mostrarCarrito() {
  const modal = $('#modalCarrito');
  modal.addClass('show');

  // Foco en el modal para accesibilidad
  setTimeout(() => {
    modal.find('.cart-close').focus();
  }, 300);
}

// Ocultar modal del carrito
function ocultarCarrito() {
  $('#modalCarrito').removeClass('show');
}

// === NUEVA MODAL DE CONFIRMACIÓN PERSONALIZADA ===
function mostrarModalConfirmacion(itemsComprados, total) {
  const itemsText = itemsComprados.map(item => `• ${item.nombre} - ${item.precio} 💰`).join('<br>');

  const modalHtml = `
    <div class="epic-confirmation-modal" id="confirmationModal">
      <div class="confirmation-overlay"></div>
      <div class="confirmation-panel">
        <div class="confirmation-header">
          <h3 class="confirmation-title">💰 ¿CONFIRMAR COMPRA ÉPICA?</h3>
        </div>

        <div class="confirmation-body">
          <div class="purchase-summary">
            <div class="summary-section">
              <h4>🛒 Artículos (${itemsComprados.length}):</h4>
              <div class="items-list">${itemsText}</div>
            </div>

            <div class="money-summary">
              <div class="money-row">
                <span>💵 Tu saldo:</span>
                <span>${availableCoins} monedas</span>
              </div>
              <div class="money-row">
                <span>💸 Costo total:</span>
                <span>${total} monedas</span>
              </div>
              <div class="money-row total-row">
                <span>💎 Saldo restante:</span>
                <span>${availableCoins - total} monedas</span>
              </div>
            </div>
          </div>
        </div>

        <div class="confirmation-footer">
          <button class="confirmation-btn btn-cancel" id="cancelPurchase">
            ❌ Cancelar
          </button>
          <button class="confirmation-btn btn-confirm" id="confirmPurchase">
            ⚔️ Proceder con la transacción
          </button>
        </div>
      </div>
    </div>
  `;

  // Agregar modal al DOM
  $('body').append(modalHtml);

  // Mostrar modal
  setTimeout(() => {
    $('#confirmationModal').addClass('show');
  }, 50);

  // Event listeners
  $('#cancelPurchase, .confirmation-overlay').click(() => {
    cerrarModalConfirmacion();
  });

  $('#confirmPurchase').click(() => {
    cerrarModalConfirmacion();
    procesarCompra();
  });
}

function cerrarModalConfirmacion() {
  const modal = $('#confirmationModal');
  if (modal.length) {
    modal.removeClass('show');
    setTimeout(() => {
      modal.remove();
    }, 300);
  }
}

// Realizar compra con confirmación personalizada
function realizarCompra() {
  if (cartItems.length === 0) {
    mostrarNotificacion("🛒 Tu carrito está vacío", 'warning');
    return;
  }

  const total = cartItems.reduce((sum, item) => sum + item.precio, 0);

  if (total > availableCoins) {
    mostrarNotificacion("💸 No tienes suficientes monedas", 'error');
    return;
  }

  // Mostrar confirmación personalizada
  mostrarModalConfirmacion(cartItems, total);
}

// Procesar la compra
function procesarCompra() {
  const total = cartItems.reduce((sum, item) => sum + item.precio, 0);
  const itemsComprados = [...cartItems]; // Copia para la notificación

  const boton = $("#comprarBtn");
  const textoOriginal = boton.text();
  boton.prop("disabled", true).text("⏳ Procesando...");

  ajaxConToken({
    url: `${BASE_URL}/carrito/comprar/${idPartida}`,
    method: "POST"
  }).done(() => {
    // ¡Reproducir sonido de compra!
    reproducirSonido('caja.registradora.mp3');

    // Notificación épica de éxito
    mostrarNotificacionEpica(itemsComprados, total);

    cargarCarrito();
    cargarMonedas();
    ocultarCarrito();

    // Actualizar productos por si cambió el saldo
    setTimeout(() => {
      cargarProductos(currentCategory);
    }, 1000);
  }).fail(manejarError)
    .always(() => {
      boton.prop("disabled", false).text(textoOriginal);
    });
}

// Vaciar carrito
function vaciarCarrito() {
  if (cartItems.length === 0) {
    mostrarNotificacion("🛒 El carrito ya está vacío", 'info');
    return;
  }

  // Crear confirmación simple para vaciar carrito
  const modalHtml = `
    <div class="epic-confirmation-modal" id="clearCartModal">
      <div class="confirmation-overlay"></div>
      <div class="confirmation-panel">
        <div class="confirmation-header">
          <h3 class="confirmation-title">🗑️ ¿Vaciar carrito?</h3>
        </div>

        <div class="confirmation-body">
          <p>¿Estás seguro de que quieres eliminar todos los artículos del carrito?</p>
        </div>

        <div class="confirmation-footer">
          <button class="confirmation-btn btn-cancel" id="cancelClear">
            ❌ Cancelar
          </button>
          <button class="confirmation-btn btn-confirm" id="confirmClear">
            🗑️ Sí, vaciar carrito
          </button>
        </div>
      </div>
    </div>
  `;

  $('body').append(modalHtml);
  setTimeout(() => $('#clearCartModal').addClass('show'), 50);

  $('#cancelClear, .confirmation-overlay').click(() => {
    $('#clearCartModal').removeClass('show');
    setTimeout(() => $('#clearCartModal').remove(), 300);
  });

  $('#confirmClear').click(() => {
    $('#clearCartModal').removeClass('show');
    setTimeout(() => $('#clearCartModal').remove(), 300);

    // Eliminar todos los items
    const promesas = cartItems.map(item =>
      ajaxConToken({
        url: `${BASE_URL}/carrito/${idPartida}/${item.id_objeto}`,
        method: "DELETE"
      })
    );

    Promise.all(promesas)
      .then(() => {
        mostrarNotificacion("🗑️ Carrito vaciado correctamente", 'success');
        cargarCarrito();
      })
      .catch(() => {
        mostrarNotificacion("❌ Error al vaciar el carrito", 'error');
        cargarCarrito();
      });
  });
}

// === NOTIFICACIÓN ÉPICA SIMPLIFICADA ===
function mostrarNotificacionEpica(itemsComprados, totalGastado) {
  // Crear overlay con efectos de partículas
  const overlay = crearOverlayEpico();
  $('body').append(overlay);

  // Crear la notificación principal (simplificada)
  const epicNotification = crearNotificacionEpicaSimplificada(itemsComprados, totalGastado);
  $('body').append(epicNotification);

  // Secuencia de animaciones épicas
  ejecutarSecuenciaEpicaSimplificada(overlay, epicNotification, itemsComprados, totalGastado);
}

function crearNotificacionEpicaSimplificada(itemsComprados, totalGastado) {
  const randomMessage = getRandomMerchantMessage();
  const purchaseRank = getPurchaseRank(totalGastado);

  return $(`
    <div class="epic-purchase-notification-v2">
      <!-- Header simplificado -->
      <div class="epic-header-v2">
        <div class="epic-title-container">
          <div class="epic-icon-animated">🎉</div>
          <h2 class="epic-title-v2">¡COMPRA ${purchaseRank.name.toUpperCase()}!</h2>
          <div class="epic-subtitle-v2">⚔️ ¡El mercader está ${purchaseRank.merchantMood}! ⚔️</div>
        </div>

        <div class="purchase-stats">
          <div class="stat-bubble">
            <span class="stat-number">${itemsComprados.length}</span>
            <span class="stat-label">Artículos</span>
          </div>
          <div class="stat-bubble">
            <span class="stat-number">${totalGastado}</span>
            <span class="stat-label">Monedas</span>
          </div>
        </div>
      </div>

      <!-- Contenido simplificado -->
      <div class="epic-content-v2">
        <!-- Contador de dinero animado -->
        <div class="money-counter">
          <div class="counter-label">💰 Monedas gastadas:</div>
          <div class="counter-value" data-target="${totalGastado}">0</div>
        </div>

        <!-- Lista de items -->
        <div class="items-showcase">
          <h3 class="showcase-title">🎒 Tesoros Adquiridos:</h3>
          <div class="items-carousel">
            ${itemsComprados.map((item, index) => `
              <div class="item-card" data-index="${index}">
                <div class="item-image">
                  <img src="${obtenerImagenProducto(item.nombre)}" alt="${item.nombre}"
                       class="item-img" onerror="this.src='img/objeto.png'">
                  <div class="item-shine"></div>
                </div>
                <div class="item-details">
                  <div class="item-name">${item.nombre}</div>
                  <div class="item-category">${item.categoria.nombre}</div>
                  <div class="item-price">${item.precio} 💰</div>
                </div>
                <div class="item-rarity ${getItemRarity(item.precio)}">${getRarityIcon(item.precio)}</div>
              </div>
            `).join('')}
          </div>
        </div>

        <!-- Mensaje del mercader -->
        <div class="merchant-dialogue">
          <div class="merchant-avatar">
            <img src="img/comerciante.png" alt="Comerciante" class="merchant-img"
                 onerror="this.innerHTML='🧙‍♂️'">
            <div class="speech-bubble">
              <div class="dialogue-text">${randomMessage}</div>
            </div>
          </div>
        </div>

        <!-- Efectos de sonido con click -->
        <div class="sound-effects" id="soundEffectsBtn">
          <div class="sound-indicator">🔊</div>
          <div class="sound-text">¡Ka-ching! ¡Compra completada!</div>
        </div>
      </div>

      <!-- Footer simplificado -->
      <div class="epic-footer-v2">
        <button class="epic-btn continue-shopping">
          🛒 Seguir Comprando
        </button>
        <button class="epic-btn view-inventory primary">
          🎒 Ver el Inventario
        </button>
      </div>
    </div>
  `);
}

function ejecutarSecuenciaEpicaSimplificada(overlay, notification, items, total) {
  // 1. Mostrar overlay con efectos
  setTimeout(() => {
    overlay.addClass('show');
    crearConfeti();
    crearLluviaMonedas();
  }, 100);

  // 2. Mostrar notificación
  setTimeout(() => {
    notification.addClass('show');
  }, 300);

  // 3. Animar contador de dinero
  setTimeout(() => {
    animarContadorDinero(total);
  }, 800);

  // 4. Animar items uno por uno
  setTimeout(() => {
    animarItemsSecuencialmente();
  }, 1200);

  // 5. Configurar event listeners
  configurarEventListenersEpicos(overlay, notification);
}

// El resto de funciones auxiliares se mantienen igual...
function crearOverlayEpico() {
  return $(`
    <div class="epic-overlay">
      <div class="confetti-container"></div>
      <div class="coins-rain"></div>
    </div>
  `);
}

function crearConfeti() {
  const container = $('.confetti-container');
  const colors = ['#FFD700', '#FF6347', '#32CD32', '#4169E1', '#FF69B4'];

  for (let i = 0; i < 50; i++) {
    setTimeout(() => {
      const confetti = $(`
        <div class="confetti-piece" style="
          left: ${Math.random() * 100}%;
          background: ${colors[Math.floor(Math.random() * colors.length)]};
          animation-delay: ${Math.random() * 3}s;
          animation-duration: ${3 + Math.random() * 2}s;
        "></div>
      `);
      container.append(confetti);
      setTimeout(() => confetti.remove(), 5000);
    }, i * 50);
  }
}

function crearLluviaMonedas() {
  const container = $('.coins-rain');

  for (let i = 0; i < 20; i++) {
    setTimeout(() => {
      const coin = $(`
        <div class="coin-drop" style="
          left: ${Math.random() * 100}%;
          animation-delay: ${Math.random() * 2}s;
        ">💰</div>
      `);
      container.append(coin);
      setTimeout(() => coin.remove(), 4000);
    }, i * 100);
  }
}

function animarContadorDinero(target) {
  const counter = $('.counter-value');
  const duration = 2000;
  const steps = 60;
  const increment = target / steps;
  let current = 0;

  const timer = setInterval(() => {
    current += increment;
    if (current >= target) {
      current = target;
      clearInterval(timer);
      counter.addClass('counter-complete');
    }
    counter.text(Math.floor(current));
  }, duration / steps);
}

function animarItemsSecuencialmente() {
  $('.item-card').each((index, item) => {
    setTimeout(() => {
      $(item).addClass('item-revealed');
    }, index * 300);
  });
}

function configurarEventListenersEpicos(overlay, notification) {
  // Ver inventario
  notification.find('.view-inventory').click(() => {
    cerrarNotificacionEpica(overlay, notification);
    setTimeout(() => {
      mostrarNotificacion("🎒 Dirigiéndose al inventario...", 'info');
      window.location.href = `inventario.html?id_partida=${idPartida}`;
    }, 500);
  });

  // Seguir comprando
  notification.find('.continue-shopping').click(() => {
    cerrarNotificacionEpica(overlay, notification);
    mostrarNotificacion("🛒 ¡Excelente! Sigue explorando nuestros productos", 'success');
  });

  // Click en efectos de sonido para reproducir de nuevo
  notification.find('#soundEffectsBtn').click(() => {
    reproducirSonido('caja.registradora.mp3');
    const soundIndicator = notification.find('.sound-indicator');
    soundIndicator.addClass('sound-playing');
    setTimeout(() => {
      soundIndicator.removeClass('sound-playing');
    }, 1000);
  });

  // Auto-cerrar después de 15 segundos
  setTimeout(() => {
    if (notification.hasClass('show')) {
      cerrarNotificacionEpica(overlay, notification);
    }
  }, 15000);
}

function cerrarNotificacionEpica(overlay, notification) {
  notification.removeClass('show');
  overlay.removeClass('show');

  setTimeout(() => {
    notification.remove();
    overlay.remove();
  }, 500);
}

// === FUNCIONES AUXILIARES ===
function obtenerImagenProducto(nombre) {
  const imageMap = {
    'Espada': 'img/espada.jpg',
    'Armadura': 'img/armadura.png',
    'Poción': 'img/pocion.png'
  };
  return imageMap[nombre] || 'img/objeto.png';
}

function getPurchaseRank(total) {
  if (total >= 200) return {
    name: 'LEGENDARIA',
    merchantMood: 'extasiado'
  };
  if (total >= 100) return {
    name: 'ÉPICA',
    merchantMood: 'muy contento'
  };
  if (total >= 50) return {
    name: 'EXCELENTE',
    merchantMood: 'satisfecho'
  };
  return {
    name: 'BUENA',
    merchantMood: 'complacido'
  };
}

function getItemRarity(precio) {
  if (precio >= 50) return 'legendary';
  if (precio >= 30) return 'epic';
  if (precio >= 20) return 'rare';
  return 'common';
}

function getRarityIcon(precio) {
  if (precio >= 50) return '💎';
  if (precio >= 30) return '⭐';
  if (precio >= 20) return '✨';
  return '🔹';
}

function getRandomMerchantMessage() {
  const messages = [
    "🧙‍♂️ \"¡Por las barbas de mi abuelo! ¡Excelente elección, noble aventurero!\"",
    "👨‍💼 \"¡Ja! ¡Otro cliente que sabe reconocer la calidad cuando la ve!\"",
    "⚖️ \"¡Transacción completada con honores! Estos artículos te servirán bien.\"",
    "🏪 \"¡Magnífico! Has invertido sabiamente en tu futuro épico, joven héroe.\"",
    "💼 \"¡Por todos los dragones! ¡Esta ha sido una de mis mejores ventas del día!\"",
    "🎯 \"¡Brillante decisión! Con este equipo, las leyendas hablarán de ti.\"",
    "⚒️ \"¡Como mi padre solía decir: 'Buenos artículos para buenos aventureros'!\"",
    "🎪 \"¡Vuelve pronto! Siempre tengo nuevos tesoros para clientes tan distinguidos.\"",
    "🌟 \"¡Exquisito gusto! Estos objetos han sido bendecidos por los antiguos maestros.\"",
    "🔮 \"¡Las estrellas se alinean! Esta compra traerá fortuna a tu próxima aventura.\""
  ];
  return messages[Math.floor(Math.random() * messages.length)];
}

// Manejo de errores de imagen
$(document).on('error', 'img', function() {
  if ($(this).attr('src') !== 'img/objeto.png') {
    $(this).attr('src', 'img/objeto.png');
  }
});

// Log de depuración
console.log("🏪 Tienda del Guerrero inicializada");
console.log(`📋 ID Partida: ${idPartida}`);
console.log(`🔑 Token: ${token ? 'Presente' : 'Ausente'}`);