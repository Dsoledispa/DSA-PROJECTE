//----Estructura basica
const isEmulator = /Android/i.test(navigator.userAgent);
const BASE_URL = isEmulator ? "http://10.0.2.2:8080/dsaApp" : "http://localhost:8080/dsaApp";
const token = localStorage.getItem("token");
const idPartida = new URLSearchParams(window.location.search).get("id_partida");

function mostrarSpinner() {
  $("#spinner").show();
}
function ocultarSpinner() {
  $("#spinner").hide();
}

// Manejo de errores centralizado
function manejarError(xhr, status, error) {
  console.error("Error:", xhr.responseText);
  let mensaje = "Error desconocido.";
  try {
    mensaje = JSON.parse(xhr.responseText).error || mensaje;
  } catch (e) {}
  alert(mensaje);
}

// AJAX con token, spinner automático
function ajaxConToken(opciones) {
  opciones.headers = opciones.headers || {};
  opciones.headers["Authorization"] = "Bearer " + token;
  mostrarSpinner();
  return $.ajax(opciones).always(ocultarSpinner);
}
//----Estructura basica

// Función para cargar el inventario de la partida
function cargarInventario() {
  ajaxConToken({
    url: `${BASE_URL}/inventario/${idPartida}`,
    method: 'GET'
  }).done(function(data) {
    const inventario = data.inventario;
    const inventarioList = $('#inventario-list');
    inventarioList.empty();

    data.forEach(objeto => {
      const imagenUrl = objeto.imagen || "img/objeto.png";
      const imagen = `<img src="${imagenUrl}" class="card-img-top" alt="${objeto.nombre}">`;

      inventarioList.append(`
        <div class="col-12 col-md-4 mb-4">
          <div class="card">
            ${imagen}
            <div class="card-body">
              <h5 class="card-title">${objeto.nombre}</h5>
              <p class="card-text">${objeto.descripcion}</p>
              <p class="card-text"><strong>Precio:</strong> ${objeto.precio} monedas</p>
            </div>
          </div>
        </div>
      `);
    });
  }).fail(manejarError);
}

// Cargar el inventario al iniciar
cargarInventario();