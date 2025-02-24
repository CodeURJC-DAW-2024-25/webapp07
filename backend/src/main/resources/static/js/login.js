// Show success or error messages using SweetAlert2
document.addEventListener("DOMContentLoaded", function() {
    const urlParams = new URLSearchParams(window.location.search);
    const message = urlParams.get('message');
    const error = urlParams.get('error');

    if (message) {
        Swal.fire({
            icon: 'success',
            title: 'Success',
            text: message,
            confirmButtonText: 'OK'
        });
    }

    if (error) {
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: error,
            confirmButtonText: 'OK'
        });
    }
});