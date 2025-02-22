// dishes.js
console.log("dishes.js loaded");

// Inicializa el offset según los elementos ya renderizados
let offset = 0;
const limit = 10;

document.addEventListener('DOMContentLoaded', () => {
    // Calcula el número de platos ya presentes en el contenedor
    const container = document.getElementById('menuContainer');
    offset = container.children.length; // Por ejemplo, 10 si ya se renderizaron 10 platos

    const loadMoreBtn = document.getElementById('loadMoreBtn');
    if (loadMoreBtn) {
        loadMoreBtn.addEventListener('click', loadDishes);
    }
});

function loadDishes() {
    fetch(`/api/dishes?offset=${offset}&limit=${limit}`)
        .then(response => {
            if (!response.ok) {
                throw new Error("Network response was not ok");
            }
            return response.json();
        })
        .then(data => {
            const container = document.getElementById('menuContainer');
            data.forEach(dish => {
                // Creamos el HTML para cada plato, similar a la plantilla Mustache
                const dishElement = document.createElement('div');
                dishElement.className = "col-lg-12"; // Ajusta la clase según tu grid
                dishElement.innerHTML = `
          <div class="d-flex align-items-center">
            <div class="w-100 d-flex flex-column text-start ps-4">
              <div class="d-flex">
                <img class="flex-shrink-0 img-fluid rounded" src="/menu/${dish.id}/image" alt="Imagen del plato" style="width: 10vw;"/>
                <div class="w-100 d-flex flex-column text-start ps-4">
                  <h5 class="d-flex justify-content-between border-bottom pb-2">
                    <span>${dish.name}</span>
                    <span class="text-primary">${dish.price}$</span>
                  </h5>
                  <small class="fst-italic">${dish.description}</small>
                  <a class="btn btn-primary btn-sm m-2 p-1 w-10" href="/menu/${dish.id}">
                    <i class="fa fa-info"></i> More info
                  </a>
                </div>
              </div>
              <div class="d-flex align-items-center">
                <button class="btn btn-primary btn-sm m-2 p-2">
                  <i class="fa fa-shopping-cart"></i> Add to Cart
                </button>
              </div>
            </div>
          </div>
        `;
                container.appendChild(dishElement);
            });
            offset += limit;
        })
        .catch(error => console.error('Error cargando platos:', error));
}
