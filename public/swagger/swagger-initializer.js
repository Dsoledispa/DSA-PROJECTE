window.onload = function() {
  //<editor-fold desc="Changeable Configuration Block">

  // the following lines will be replaced by docker/configurator, when it runs in a docker-container
  window.ui = SwaggerUIBundle({
    url: "https://dsa1.upc.edu/dsaApp/openapi.json",
    dom_id: '#swagger-ui',
    deepLinking: true,
    presets: [
      SwaggerUIBundle.presets.apis,
      SwaggerUIStandalonePreset
    ],
    plugins: [
      SwaggerUIBundle.plugins.DownloadUrl
    ],
    layout: "StandaloneLayout",

    // Interceptor para capturar el token después del login
    responseInterceptor: function (response) {
      if (response.url.includes("/usuarios/login") && response.status === 201) {
        const json = JSON.parse(response.data);
        const token = json.token;
        if (token) {
            // Setear el token automáticamente
            ui.preauthorizeApiKey("bearerAuth", token);
            console.log("Token JWT establecido en Swagger automáticamente:", token);
        }
      }
      return response;
    }
  });

  //</editor-fold>
};
