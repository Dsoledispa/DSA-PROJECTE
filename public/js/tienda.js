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

$(document).ready(function () {
  cargarCategorias();
  cargarCarrito();
  cargarMonedas();

  $("#btnCarrito").click(() => {
    $("#modalCarrito").modal("show");
  });

  $("#comprarBtn").click(function () {
    if (!confirm("¿Deseas realizar la compra?")) return;

    const boton = $(this);
    boton.prop("disabled", true);

    ajaxConToken({
      url: `${BASE_URL}/carrito/comprar/${idPartida}`,
      method: "POST"
    }).done(() => {
      alert("¡Compra realizada!");
      cargarCarrito();
      cargarMonedas();
    }).fail(manejarError)
      .always(() => boton.prop("disabled", false));
  });

  $("#irPartidaBtn").click(() => {
    ajaxConToken({
      url: `${BASE_URL}/carrito`,
      method: "DELETE"
    }).done(() => {
      window.location.href = "partida.html";
    }).fail(manejarError);
  });
});

function cargarMonedas() {
  ajaxConToken({
    url: `${BASE_URL}/partidas/${idPartida}/monedas`,
    method: "GET"
  }).done(data => {
    $("#monedasCantidad").text(data.monedas + " monedas");
  }).fail(manejarError);
}

function cargarCategorias() {
  ajaxConToken({
    url: `${BASE_URL}/categoria`,
    method: "GET"
  }).done(categorias => {
    $("#categorias").append(`<button class="btn btn-outline-primary me-2 mb-2 categoria-btn" data-cat="">Todos</button>`);
    
    categorias.forEach(cat => {
      $("#categorias").append(`<button class="btn btn-outline-primary me-2 mb-2 categoria-btn" data-cat="${cat.id_categoria}">${cat.nombre}</button>`);
    });

    $(".categoria-btn").click(function () {
      const idCategoria = $(this).data("cat");
      cargarProductos(idCategoria);
    });

    cargarProductos(); // Todos por defecto
  }).fail(manejarError);
}

function cargarProductos(categoria = "") {
  const url = categoria ? `${BASE_URL}/tienda/productos/categoria/${categoria}` : `${BASE_URL}/tienda/productos`;

  ajaxConToken({
    url: url,
    method: "GET"
  }).done(productos => {
    console.log(productos)
    $("#productos").empty();
    productos.forEach(p => {
      $("#productos").append(`
        <div class="col-sm-6 col-md-4 col-lg-3">
          <div class="card h-100">
            <img src="${p.imagen}" class="card-img-top" alt="${p.nombre}" onerror="this.src='img/objeto.png';">
            <div class="card-body d-flex flex-column">
              <h5 class="card-title">${p.nombre}</h5>
              <p class="card-text">${p.descripcion}</p>
              <p class="card-text">Precio: ${p.precio} monedas</p>
              <p class="card-text"><span class="badge bg-secondary">${p.categoria.nombre}</span></p>
              <button class="btn btn-primary mt-auto agregar-btn" data-id="${p.id_objeto}">Agregar</button>
            </div>
          </div>
        </div>`);
    });

    $(".agregar-btn").click(function () {
      const id = $(this).data("id");
      ajaxConToken({
        url: `${BASE_URL}/carrito/${id}`,
        method: "POST"
      }).done(cargarCarrito).fail(manejarError);
    });
  }).fail(manejarError);
}

function cargarCarrito() {
  ajaxConToken({
    url: `${BASE_URL}/carrito`,
    method: "GET"
  }).done(objetos => {
    const tbody = $("#tablaCarrito").empty();
    let total = 0;

    objetos.forEach(o => {
      total += o.precio;
      tbody.append(`
        <tr>
          <td>
            <div class="d-flex align-items-center">
              <img src="${o.imagen}" alt="${o.nombre}" class="img-thumbnail me-2" style="width: 50px; height: 50px; object-fit: cover;" onerror="this.src='img/objeto.png';">
              <div>${o.nombre}</div>
            </div>
          </td>
          <td>${o.precio}</td>
          <td>${o.categoria}</td>
          <td><button class="btn btn-danger btn-sm quitar-btn" data-id="${o.id_objeto}">🗑</button></td>
        </tr>`);
    });

    $("#totalCarrito").text(total);

    $(".quitar-btn").click(function () {
      const id = $(this).data("id");
      ajaxConToken({
        url: `${BASE_URL}/carrito/${id}`,
        method: "DELETE"
      }).done(cargarCarrito).fail(manejarError);
    });
  }).fail(manejarError);
}