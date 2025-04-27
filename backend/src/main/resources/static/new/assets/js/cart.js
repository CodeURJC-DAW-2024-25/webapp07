document.addEventListener("DOMContentLoaded", function () {
    document.querySelectorAll(".addToCartButton").forEach(button => {
        button.addEventListener("click", function () {
            let dishId = this.getAttribute("data-id");
            let formData = new FormData(document.getElementById("addToCartForm-" + dishId));

            fetch("/orders/cart/add", {
                method: "POST",
                body: formData
            })
                .then(response => response.json())
                .then(data => {
                    if (data.success) {
                        let messageBox = document.getElementById("cartMessage-" + dishId);
                        messageBox.classList.remove("d-none");
                        setTimeout(() => {
                            messageBox.classList.add("d-none");
                        }, 2000);
                    }
                })
                .catch(error => console.error("Error:", error));
        });
    });
});
