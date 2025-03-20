document.addEventListener('DOMContentLoaded', function() {
    const form = document.querySelector('form');
    const returnRadio = document.getElementById('return');
    const modal = document.getElementById('returnModal');
    const confirmBtn = document.getElementById('confirmReturn');
    const cancelBtn = document.getElementById('cancelReturn');

    form.addEventListener('submit', function(e) {
        if (returnRadio.checked) {
            e.preventDefault();
            modal.style.display = 'block';
        }
    });

    confirmBtn.addEventListener('click', function() {
        modal.style.display = 'none';
        form.submit();
    });

    cancelBtn.addEventListener('click', function() {
        modal.style.display = 'none';
        returnRadio.checked = false;
    });

    window.addEventListener('click', function(e) {
        if (e.target === modal) {
            modal.style.display = 'none';
            returnRadio.checked = false;
        }
    });
});

// step2
function submitForm() {
    const checkedBoxes = document.querySelectorAll('.book-checkbox:checked');

    if (checkedBoxes.length === 0) {
        alert('최소 한 개의 상품을 선택해주세요.');
        return;
    }

    const selectedBooks = Array.from(checkedBoxes).map(checkbox => ({
        orderDetailId: parseInt(checkbox.dataset.orderDetailId),
        bookId: parseInt(checkbox.dataset.bookId),
        quantity: parseInt(checkbox.dataset.quantity),
        price: parseInt(checkbox.dataset.price)
    }));

    const requestData = {
        bookOrderRequests: selectedBooks
    };

    fetch(document.getElementById('returnForm').action, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(requestData)
    })
        .then(response => {
            if(response.ok) {
                window.location.href = response.url;  // 리다이렉트 처리
            }
        })
        .catch(error => console.error('Error:', error));
}

// // step1.js
// document.addEventListener('DOMContentLoaded', function() {
//     const form = document.querySelector('form');
//     const returnRadio = document.getElementById('return');
//     const modal = document.getElementById('returnModal');
//     const confirmBtn = document.getElementById('confirmReturn');
//     const cancelBtn = document.getElementById('cancelReturn');
//
//     form.addEventListener('submit', function(e) {
//         if (returnRadio.checked) {
//             e.preventDefault();
//             modal.style.display = 'block';
//         }
//     });
//
//     confirmBtn.addEventListener('click', function() {
//         form.submit();
//     });
//
//     cancelBtn.addEventListener('click', function() {
//         modal.style.display = 'none';
//         returnRadio.checked = false;
//     });
// });
//
// // step2.js
// document.addEventListener('DOMContentLoaded', function() {
//     const form = document.querySelector('form');
//     const checkboxes = document.querySelectorAll('input[type="checkbox"]');
//     const submitBtn = document.querySelector('button[type="submit"]');
//
//     // 최소 1개 이상 선택되어야 다음 버튼 활성화
//     function updateSubmitButton() {
//         const checkedCount = Array.from(checkboxes).filter(cb => cb.checked).length;
//         submitBtn.disabled = checkedCount === 0;
//     }
//
//     checkboxes.forEach(checkbox => {
//         checkbox.addEventListener('change', updateSubmitButton);
//     });
//
//     updateSubmitButton();
// });

