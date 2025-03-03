$(document).ready(function() {
    let page = 1;  // Número de página inicial
    let pageSize = 10;  // Número de jugadores por página

    function loadMoreDishes() {
        $.ajax({
            url: '/api/menu?page=' + page + '&pageSize=' + pageSize,
            method: 'GET',
            dataType: 'json',
            success: function(data) {
                if (data.length > 0) {
                    let newDishesHtml = '';
                    data.forEach(function(dish) {
                        let cartButton = '';
                        if (isAuthenticated === "true") {
                            cartButton = `
                                <button class="btn btn-primary btn-sm m-2 p-2">
                                    <i class="fa fa-shopping-cart"></i> Add to Cart
                                </button>
                            `;
                        }
                        newDishesHtml += `
                            <div class="col-lg-12">
                    <div class="d-flex align-items-center">
                        <div class="w-100 d-flex flex-column text-start ps-4">
                            <div class="d-flex">
                                <img class="flex-shrink-0 img-fluid rounded" src="data:image/png;base64,${dish.dishImagePath}" alt="${dish.name}" style="width: 10vw;"/>
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
                                ${cartButton}
                            </div>
                        </div>
                    </div>
                </div>`;
                    });
                    console.log("dataLenght", data)
                    console.log("pageSize", pageSize)
                    console.log("page", page)

                    $('#menuContainer').append(newDishesHtml);
                    if (data.length < pageSize) {
                        $('#loadMoreBtn').prop('disabled', true).text('No more dishes');
                    }
                    page++;
                } else {
                    $('#loadMoreDishes').prop('disabled', true).text('No more dishes');
                }
            },
            error: function(error) {
                console.error('Error al cargar platos:', error);
            }
        });
    }

    $('#loadMoreDishes').on('click', function() {
        loadMoreDishes();
    });
});

