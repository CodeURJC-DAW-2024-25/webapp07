let formToSubmit = null;

function openConfirmationModal(formId) {
    formToSubmit = document.getElementById(formId);
    const confirmationModal = new bootstrap.Modal(document.getElementById('confirmationModal'));
    confirmationModal.show();
}

document.addEventListener('DOMContentLoaded', function () {
    const confirmButton = document.getElementById('confirmAction');
    if (confirmButton) {
        confirmButton.addEventListener('click', function () {
            if (formToSubmit) {
                formToSubmit.submit();
            }
        });
    } else {
        console.error('Confirm button not found in the confirmation modal.');
    }
});
