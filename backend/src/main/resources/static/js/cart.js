document.addEventListener("DOMContentLoaded", function () {
    let cart = JSON.parse(localStorage.getItem("cart")) || [];

    function saveCart() {
        localStorage.setItem("cart", JSON.stringify(cart));
    }

    function renderCart() {
        let cartSummary = document.getElementById("cartSummary");
        cartSummary.innerHTML = "";

        if (cart.length === 0) {
            cartSummary.innerHTML = "<p class='text-center'>Your cart is empty.</p>";
        } else {
            cart.forEach((item, index) => {
                let cartItem = document.createElement("div");
                cartItem.className = "card mb-3";
                cartItem.style.width = "18rem";
                cartItem.innerHTML = `
                    <div class="card-body d-flex justify-content-between align-items-center">
                        <h5 class="card-title mb-0">${item.name}</h5>
                        <div class="d-flex align-items-center">
                            <i class="fa fa-minus text-primary px-3 cart-decrease" data-index="${index}"></i>
                            <span>${item.quantity}</span>
                            <i class="fa fa-plus text-primary px-3 cart-increase" data-index="${index}"></i>
                        </div>
                    </div>
                `;
                cartSummary.appendChild(cartItem);
            });
        }
    }

    function addToCart(dishName, dishPrice) {
        let existingItem = cart.find(item => item.name === dishName);

        if (existingItem) {
            existingItem.quantity++;
        } else {
            cart.push({ name: dishName, price: dishPrice, quantity: 1 });
        }

        saveCart();
        renderCart();
    }

    document.addEventListener("click", function (event) {
        if (event.target.classList.contains("add-to-cart")) {
            let dishName = event.target.getAttribute("data-name");
            let dishPrice = parseFloat(event.target.getAttribute("data-price"));

            addToCart(dishName, dishPrice);
        } else if (event.target.classList.contains("cart-increase")) {
            let index = event.target.getAttribute("data-index");
            cart[index].quantity++;
            saveCart();
            renderCart();
        } else if (event.target.classList.contains("cart-decrease")) {
            let index = event.target.getAttribute("data-index");
            if (cart[index].quantity > 1) {
                cart[index].quantity--;
            } else {
                cart.splice(index, 1);
            }
            saveCart();
            renderCart();
        }
    });

    renderCart();
});
