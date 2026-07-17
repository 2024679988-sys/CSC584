document.addEventListener('DOMContentLoaded', function () {
    document.addEventListener('click', function (event) {
        const deleteLink = event.target.closest('a[data-swal-delete]');
        if (!deleteLink) {
            return;
        }

        event.preventDefault();

        showDeleteConfirmation(
            deleteLink.dataset.swalTitle,
            deleteLink.dataset.swalText
        ).then(function (result) {
            if (result.isConfirmed) {
                window.location.href = deleteLink.href;
            }
        });
    });

    document.addEventListener('submit', function (event) {
        const deleteForm = event.target.closest('form[data-swal-delete]');
        if (!deleteForm || deleteForm.dataset.confirmed === 'true') {
            return;
        }

        event.preventDefault();

        showDeleteConfirmation(
            deleteForm.dataset.swalTitle,
            deleteForm.dataset.swalText
        ).then(function (result) {
            if (result.isConfirmed) {
                deleteForm.dataset.confirmed = 'true';
                deleteForm.submit();
            }
        });
    });
});

function showDeleteConfirmation(title, text) {
    return Swal.fire({
        title: title || 'Are you sure?',
        text: text || 'This record will be permanently deleted.',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#dc2626',
        cancelButtonColor: '#6b7280',
        confirmButtonText: 'Yes, delete it',
        cancelButtonText: 'Cancel',
        reverseButtons: true
    });
}
