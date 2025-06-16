//----Estructura basica
const isEmulator = /Android/i.test(navigator.userAgent);
const BASE_URL = isEmulator ? "http://10.0.2.2:8080/dsaApp" : "https://dsa1.upc.edu/dsaApp";
const token = localStorage.getItem("token");
const idPartida = new URLSearchParams(window.location.search).get("id_partida");

// Estado del inventario
let inventoryItems = [];
let totalValue = 0;
let rareItemCount = 0;

function mostrarSpinner() {
  $("#spinner").removeClass('d-none');
}

function ocultarSpinner() {
  $("#spinner").addClass('d-none');
}

// Sistema de notificaciones (opcional para inventario)
function mostrarNotificacion(mensaje, tipo = 'info') {
  console.log(`📢 ${tipo.toUpperCase()}: ${mensaje}`);
  // Aquí podrías implementar un sistema de notificaciones toast si lo necesitas
}

// Manejo de errores centralizado
function manejarError(xhr, status, error) {
  console.error("Error:", xhr.responseText);
  let mensaje = "Error desconocido al cargar el inventario.";
  try {
    mensaje = JSON.parse(xhr.responseText).error || mensaje;
  } catch (e) {
    if (xhr.status === 404) {
      mensaje = "Inventario no encontrado";
    } else if (xhr.status === 500) {
      mensaje = "Error del servidor";
    }
  }

  mostrarInventarioVacio(mensaje);
}

// AJAX con token, spinner automático
function ajaxConToken(opciones) {
  opciones.headers = opciones.headers || {};
  opciones.headers["Authorization"] = "Bearer " + token;
  mostrarSpinner();
  return $.ajax(opciones).always(ocultarSpinner);
}

// Funciones de utilidad para rareza
function determinarRareza(precio) {
  if (precio >= 100) return 'legendary';
  if (precio >= 75) return 'epic';
  if (precio >= 50) return 'rare';
  if (precio >= 25) return 'uncommon';
  return 'common';
}

function obtenerIconoRareza(rareza) {
  const iconos = {
    'legendary': '👑',
    'epic': '💜',
    'rare': '💙',
    'uncommon': '💚',
    'common': '⚪'
  };
  return iconos[rareza] || '⚪';
}

// Mapeo de imágenes mejorado
function obtenerImagenItem(nombre) {
  const imageMap = {
    'Espada': 'img/espada.jpg',
    'Armadura': 'img/armadura.png',
    'Poción': 'img/pocion.png'
  };

  return imageMap[nombre] || 'img/objeto.png';
}

// Función principal de inicialización
$(document).ready(function() {
  console.log("🎒 Iniciando Inventario del Aventurero...");

  // Verificar que tenemos id_partida
  if (!idPartida) {
    console.warn("⚠️ No se encontró ID de partida");
    mostrarInventarioVacio("No se pudo identificar la partida");
    setTimeout(() => {
      window.location.href = "partida.html";
    }, 3000);
    return;
  }

  // Cargar inventario
  cargarInventario();

  // Configurar tooltips
  configurarTooltips();

  console.log(`📋 Cargando inventario para partida: ${idPartida}`);
});

// Función principal para cargar el inventario
function cargarInventario() {
  ajaxConToken({
    url: `${BASE_URL}/inventario/${idPartida}`,
    method: 'GET'
  }).done(function(data) {
    console.log("📦 Datos del inventario:", data);
    inventoryItems = data || [];
    renderizarInventario(inventoryItems);
    actualizarEstadisticas(inventoryItems);
  }).fail(manejarError);
}

// Renderizar el inventario completo
function renderizarInventario(items) {
  const container = $('#inventario-list');
  container.empty();

  if (!items || items.length === 0) {
    mostrarInventarioVacio();
    return;
  }

  // Renderizar cada item con animación escalonada
  items.forEach((item, index) => {
    const itemCard = crearTarjetaItem(item, index);
    container.append(itemCard);

    // Animación de aparición escalonada
    setTimeout(() => {
      itemCard.addClass('animate-in');
    }, index * 100);
  });

  console.log(`✅ Renderizados ${items.length} items en el inventario`);
}

// Crear tarjeta individual de item
function crearTarjetaItem(item, index) {
  const template = $('#inventoryItemTemplate').prop('content');
  const itemCard = $(template.cloneNode(true)).find('.inventory-item');

  // Determinar rareza basada en precio
  const rareza = determinarRareza(item.precio);
  const iconoRareza = obtenerIconoRareza(rareza);

  // Configurar imagen
  const img = itemCard.find('.item-img');
  const imageSrc = obtenerImagenItem(item.nombre);
  img.attr('src', imageSrc).attr('alt', item.nombre);

  // Configurar contenido
  itemCard.find('.item-name').text(`${iconoRareza} ${item.nombre}`);
  itemCard.find('.item-description').text(item.descripcion);
  itemCard.find('.value-amount').text(item.precio);

  // Aplicar clase de rareza
  itemCard.addClass(rareza);

  // Configurar tooltip de información adicional
  itemCard.attr('data-tooltip', `
    🏷️ ${item.nombre}
    💰 Valor: ${item.precio} monedas
    ✨ Rareza: ${rareza.charAt(0).toUpperCase() + rareza.slice(1)}
    📝 ${item.descripcion}
  `);

  // Efectos hover
  itemCard.hover(
    function() {
      // Mouseenter
      $(this).css('transform', 'translateY(-5px) rotate(-1deg) scale(1.02)');
    },
    function() {
      // Mouseleave
      $(this).css('transform', 'translateY(0) rotate(0deg) scale(1)');
    }
  );

  return itemCard;
}

// Mostrar inventario vacío
function mostrarInventarioVacio(mensaje = null) {
  const container = $('#inventario-list');
  const template = $('#emptyInventoryTemplate').prop('content');
  const emptyState = $(template.cloneNode(true));

  if (mensaje) {
    emptyState.find('.empty-message').text(mensaje);
    emptyState.find('.empty-submessage').text('Regresa a la partida para continuar tu aventura');
  }

  container.html(emptyState);

  // Resetear estadísticas
  actualizarEstadisticas([]);
}

// Actualizar estadísticas del inventario
function actualizarEstadisticas(items) {
  const totalItems = items.length;
  totalValue = items.reduce((sum, item) => sum + item.precio, 0);
  rareItemCount = items.filter(item => {
    const rareza = determinarRareza(item.precio);
    return ['rare', 'epic', 'legendary'].includes(rareza);
  }).length;

  // Actualizar UI con animaciones
  animarEstadistica('#totalItems', totalItems);
  animarEstadistica('#totalValue', totalValue);
  animarEstadistica('#rareItems', rareItemCount);

  console.log(`📊 Estadísticas: ${totalItems} items, ${totalValue} monedas, ${rareItemCount} raros`);
}

// Animar cambio de estadística
function animarEstadistica(selector, nuevoValor) {
  const elemento = $(selector);
  const valorActual = parseInt(elemento.text()) || 0;

  if (valorActual === nuevoValor) return;

  // Animación de conteo
  let valorTemporal = valorActual;
  const diferencia = nuevoValor - valorActual;
  const pasos = Math.min(Math.abs(diferencia), 20);
  const incremento = diferencia / pasos;

  const intervalo = setInterval(() => {
    valorTemporal += incremento;

    if ((incremento > 0 && valorTemporal >= nuevoValor) ||
        (incremento < 0 && valorTemporal <= nuevoValor)) {
      valorTemporal = nuevoValor;
      clearInterval(intervalo);
    }

    elemento.text(Math.round(valorTemporal));
  }, 50);
}

// Configurar sistema de tooltips
function configurarTooltips() {
  const tooltip = $('#itemTooltip');

  $(document).on('mouseenter', '.inventory-item[data-tooltip]', function(e) {
    const tooltipText = $(this).attr('data-tooltip');
    const content = tooltip.find('.tooltip-content');

    // Formatear contenido del tooltip
    const lines = tooltipText.trim().split('\n').map(line => line.trim()).filter(line => line);
    content.html(lines.join('<br>'));

    // Posicionar tooltip
    const mouseX = e.pageX;
    const mouseY = e.pageY;

    tooltip.css({
      left: mouseX + 15,
      top: mouseY - 10
    }).addClass('show');
  });

  $(document).on('mouseleave', '.inventory-item[data-tooltip]', function() {
    tooltip.removeClass('show');
  });

  $(document).on('mousemove', '.inventory-item[data-tooltip]', function(e) {
    if (tooltip.hasClass('show')) {
      tooltip.css({
        left: e.pageX + 15,
        top: e.pageY - 10
      });
    }
  });
}

// Funciones de utilidad adicionales
function formatearNumero(numero) {
  return numero.toLocaleString();
}

function obtenerColorRareza(rareza) {
  const colores = {
    'legendary': '#FFD700',
    'epic': '#9370DB',
    'rare': '#4169E1',
    'uncommon': '#32CD32',
    'common': '#808080'
  };
  return colores[rareza] || '#808080';
}

// Manejo de errores de imagen
$(document).on('error', 'img', function() {
  if ($(this).attr('src') !== 'img/objeto.png') {
    $(this).attr('src', 'img/objeto.png');
  }
});

// Función para ordenar inventario (opcional)
function ordenarInventario(criterio = 'nombre') {
  if (!inventoryItems.length) return;

  const criterios = {
    'nombre': (a, b) => a.nombre.localeCompare(b.nombre),
    'precio': (a, b) => b.precio - a.precio,
    'rareza': (a, b) => {
      const rarezaA = determinarRareza(a.precio);
      const rarezaB = determinarRareza(b.precio);
      const orden = ['common', 'uncommon', 'rare', 'epic', 'legendary'];
      return orden.indexOf(rarezaB) - orden.indexOf(rarezaA);
    }
  };

  if (criterios[criterio]) {
    inventoryItems.sort(criterios[criterio]);
    renderizarInventario(inventoryItems);
  }
}

// Efectos visuales adicionales
function aplicarEfectoUso(itemCard) {
  itemCard.addClass('item-used');
  setTimeout(() => {
    itemCard.removeClass('item-used');
  }, 1000);
}

// Agregar efectos de carga inicial
$(window).on('load', function() {
  // Efecto de aparición del inventario
  $('.inventory-showcase').css({
    'opacity': '0',
    'transform': 'translateY(20px)'
  }).animate({
    'opacity': '1',
    'transform': 'translateY(0)'
  }, 600);
});

// Log de depuración
console.log("🎒 Inventario del Aventurero inicializado");
console.log(`📋 ID Partida: ${idPartida}`);
console.log(`🔑 Token: ${token ? 'Presente' : 'Ausente'}`);