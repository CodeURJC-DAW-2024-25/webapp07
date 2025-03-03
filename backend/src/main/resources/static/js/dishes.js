$(document).ready(function () {
    let filters = getQueryParams();
    console.log("Filtros detectados en la URL:", filters);
    loadFilteredDishes(filters, false);

    let currentPage = 0;
    $("#loadMoreDishes").on("click", function () {
        currentPage++;
        filters.page = currentPage; // Incrementamos la página correctamente
        loadFilteredDishes(filters, true);
    });
});

// 🔹 1️⃣ Obtener filtros desde la URL
function getQueryParams() {
    let params = new URLSearchParams(window.location.search);
    return {
        name: params.get("name") || null,
        ingredient: params.get("ingredient") || null,
        maxPrice: params.get("maxPrice") ? parseInt(params.get("maxPrice")) : null,
        page: 0,
        pageSize: 10
    };
}

// 🔹 2️⃣ Cargar platos filtrados (y manejar "Load More")
function loadFilteredDishes(filters, append = false) {
    $.ajax({
        url: "/api/menu",
        method: "GET",
        dataType: "json",
        data: filters,
        success: function (data) {
            if (!append) {
                $("#menuContainer").empty(); // Limpiamos la lista si no es "Load More"
                currentPage = 0; // Reiniciamos la página en nueva búsqueda
            }

            if (data.length > 0) {
                let newDishesHtml = "";
                data.forEach(function (dish) {
                    newDishesHtml += `
                        <div class="col-lg-12">
                            <div class="d-flex align-items-center">
                                <div class="w-100 d-flex flex-column text-start ps-4">
                                    <div class="d-flex">
                                        <img class="flex-shrink-0 img-fluid rounded" src="data:image/png;base64,${dish.dishImagePath}" alt="${dish.name}" style="width: 10vw; height: 15vh"/>
                                        <div class="w-100 d-flex flex-column text-start ps-4">
                                            <h5 class="d-flex justify-content-between border-bottom pb-2">
                                                <span>${dish.name}</span>
                                                <span class="text-primary">${dish.price}$</span>
                                            </h5>
                                            <small class="fst-italic">${dish.description}</small>
                                            <h5><span>${dish.rate}<i class="fas fa-star text-warning"></i></span></h5>
                                            <a class="btn btn-primary btn-sm m-2 p-1 w-10" href="/menu/${dish.id}">
                                                <i class="fa fa-info"></i> More info
                                            </a>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>`;
                });

                $("#menuContainer").append(newDishesHtml);

                // 🔹 Si hay menos platos de los esperados, desactivar "Ver Más"
                if (data.length < filters.pageSize) {
                    $("#loadMoreDishes").prop("disabled", true).text("No more dishes");
                } else {
                    $("#loadMoreDishes").prop("disabled", false).text("View More");
                }
            } else {
                if (!append) {
                    $("#menuContainer").html("<p>No hay platos que coincidan con los filtros.</p>");
                }
                $("#loadMoreDishes").prop("disabled", true).text("No more dishes");
            }
        },
        error: function (error) {
            console.error("Error al cargar platos:", error);
        }
    });
}
