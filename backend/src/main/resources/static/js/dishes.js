$(document).ready(function () {
    let filters = getQueryParams();
    console.log("Filtros detectados en la URL:", filters);
    loadFilteredDishes(filters, false);

    let currentPage = 0;
    $("#loadMoreDishes").on("click", function () {
        currentPage++;
        filters.page = currentPage;
        loadFilteredDishes(filters, true);
    });

    // Obtener filtros desde la URL
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

    // Cargar platos filtrados (y manejar "Load More")
    function loadFilteredDishes(filters, append = false) {
        $.ajax({
            url: "/api/menu",
            method: "GET",
            dataType: "json",
            data: filters,
            xhrFields: { withCredentials: true },
            success: function (response) {
                let isAuthenticated = response.isAuthenticated;
                let data = response.dishes;

                if (!append) {
                    $("#menuContainer").empty();
                    currentPage = 0;
                }

                if (data.length > 0) {
                    let newDishesHtml = "";
                    data.forEach(function (dish) {
                        let dishImage = dish.dishImagePath
                            ? `<img class="flex-shrink-0 img-fluid rounded shadow" 
                                    src="data:image/png;base64,${dish.dishImagePath}" 
                                    alt="${dish.name}" 
                                    style="width: 10vw; height: 15vh">`
                            : `<img class="flex-shrink-0 img-fluid rounded shadow" 
                                    src="/img/logo.jpg" 
                                    alt="Dish Image" 
                                    style="width: 10vw; height: 15vh">`;

                        let authButton = isAuthenticated
                            ? `<form id="addToCartForm-${dish.id}" class="addToCartForm">
                                    <input type="hidden" name="_csrf" value="${response.csrfToken}">
                                    <input type="hidden" name="dishId" value="${dish.id}" />
                                    <button type="button" class="btn btn-primary btn-sm m-2 p-2 addToCartButton" data-id="${dish.id}">
                                        <i class="fa fa-shopping-cart"></i> Add to Cart
                                    </button>
                               </form>
                               <div id="cartMessage-${dish.id}" class="alert alert-success d-none mt-2" role="alert">
                                    Added successfully
                               </div>`
                            : "";

                        newDishesHtml += `
                            <div class="col-lg-12">
                                <div class="d-flex align-items-center">
                                    <div class="w-100 d-flex flex-column text-start ps-4">
                                        <div class="d-flex">
                                            ${dishImage}
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
                                        <div class="d-flex align-items-center">
                                            ${authButton}
                                        </div>
                                    </div>
                                </div>
                            </div>`;
                    });

                    $("#menuContainer").append(newDishesHtml);

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

    // Manejar la adición al carrito con AJAX y token CSRF
    $(document).on("click", ".addToCartButton", function (event) {
        event.preventDefault();

        let dishId = $(this).data("id");
        let csrfToken = $("input[name='_csrf']").val();
        let button = $(this);
        let messageBox = $("#cartMessage-" + dishId);

        $.ajax({
            url: "/orders/cart/add",
            method: "POST",
            contentType: "application/x-www-form-urlencoded",
            data: { dishId: dishId, _csrf: csrfToken },
            success: function (response) {
                messageBox.removeClass("d-none");
                setTimeout(() => messageBox.addClass("d-none"), 2000);
            },
            error: function (xhr, status, error) {
                console.error("Error al añadir al carrito:", error);
                alert("Error añadiendo el plato al carrito.");
            }
        });
    });
});
