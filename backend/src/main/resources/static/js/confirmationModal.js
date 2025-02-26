// confirmationModal.js


function initializeConfirmationModal(formId, saveButtonId, confirmSaveButtonId, modalId) {

    const form = document.getElementById(formId);
    const saveButton = document.getElementById(saveButtonId);
    const confirmSaveButton = document.getElementById(confirmSaveButtonId);
    const confirmationModal = new bootstrap.Modal(document.getElementById(modalId));


    if (form && saveButton && confirmSaveButton && confirmationModal) {

        saveButton.addEventListener('click', function (e) {
            e.preventDefault();
            confirmationModal.show();
        });


        confirmSaveButton.addEventListener('click', function () {
            form.submit();
        });
    } else {
        console.error('Not all the elements necessary for the confirmation mode were found.');
    }
}