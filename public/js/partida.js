const isEmulator = /Android/i.test(navigator.userAgent);
const BASE_URL = isEmulator ? "http://10.0.2.2:8080/dsaApp" : "https://dsa1.upc.edu/dsaApp";
const token = localStorage.getItem("token");

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

// AJAX con token + spinner automático
function ajaxConToken(opciones) {
  opciones.headers = opciones.headers || {};
  opciones.headers["Authorization"] = "Bearer " + token;
  mostrarSpinner();
  return $.ajax(opciones).always(ocultarSpinner);
}

// Crear nueva partida
function crearPartida() {
  ajaxConToken({
    url: BASE_URL + "/partidas",
    type: "POST"
  }).done(() => {
    mostrarPartidas();
  }).fail(manejarError);
}

// Obtener partidas del usuario
function mostrarPartidas() {
  ajaxConToken({
    url: BASE_URL + "/partidas",
    type: "GET"
  }).done(partidas => {
    if (!partidas || partidas.length === 0) {
      console.log("No hay partidas.");
    }
    filasPartidas(partidas);
  }).fail(manejarError);
}

// Pintar tarjetas de partidas
function filasPartidas(partidas) {
  const contenedor = $("#contenedor-partidas");
  contenedor.empty();

  partidas.forEach((partida, i) => {
    contenedor.append(`
      <div class="col-12 col-md-6 col-lg-4">
        <div class="card h-100">
          <div class="card-body d-flex flex-column justify-content-between">
            <div>
              <h5 class="card-title">Partida #${i + 1}</h5>
              <p class="card-text"><strong>Vidas:</strong> ${partida.vidas}</p>
              <p class="card-text"><strong>Monedas:</strong> ${partida.monedas}</p>
              <p class="card-text"><strong>Puntuación:</strong> ${partida.puntuacion}</p>
            </div>
            <div class="mt-3 d-flex justify-content-between">
              <button class="btn btn-primary btn-sm tienda-btn" data-id="${partida.id_partida}">Tienda</button>
              <button class="btn btn-info btn-sm inventario-btn" data-id="${partida.id_partida}">Inventario</button>
              <button class="btn btn-danger btn-sm eliminar-btn" data-id="${partida.id_partida}">Eliminar</button>
            </div>
          </div>
        </div>
      </div>
    `);
  });
}

// Eliminar una partida
function eliminarPartida(id) {
  ajaxConToken({
    url: BASE_URL + "/partidas/" + id,
    type: "DELETE"
  }).done(() => {
    mostrarPartidas();
  }).fail(manejarError);
}

// Inicializar
mostrarPartidas();

// Botón de crear partida
$("#crear").on("click", crearPartida);

// Botón de cerrar sesión
$("#volver").on("click", () => {
  localStorage.removeItem("token");
  alert("Sesión cerrada correctamente.");
  window.location.href = "/";
});

// Botón de eliminar partida
$(document).on("click", ".eliminar-btn", function () {
  const id = $(this).data("id");
  if (window.confirm("¿Estás seguro de que deseas eliminar esta partida?")) {
    eliminarPartida(id);
  }
});

// Botón de ir a la tienda
$(document).on("click", ".tienda-btn", function () {
  const id = $(this).data("id");
  window.location.href = `tienda.html?id_partida=${id}`;
});

// Botón de ir al inventario
$(document).on("click", ".inventario-btn", function () {
  const id = $(this).data("id");
  window.location.href = `inventario.html?id_partida=${id}`;
});
