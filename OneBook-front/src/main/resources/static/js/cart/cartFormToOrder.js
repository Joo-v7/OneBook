document.addEventListener('DOMContentLoaded', function() {
    // 주문 버튼 클릭 시 실행
    document.getElementById('orderButton').addEventListener('click', function(event) {
        event.preventDefault();  // 폼 제출을 막음

        // 선택된 항목들
        let selectedItems = [];

        // 체크된 체크박스를 순회
        $(".cart-item-checkbox:checked").each(function() {
            const bookId = $(this).closest('tr').find('.individual-bookId').val();
            const quantity = $(this).closest('tr').find('.individual-quantity').val();

            console.log("선택된 책 ID:", bookId);
            console.log("선택된 수량:", quantity);

            // 선택된 bookId와 quantity를 리스트에 추가
            selectedItems.push({
                bookId: bookId,
                quantity: quantity
            });
        });

        // 체크된 항목이 하나도 없다면, 경고 메시지 표시하고 폼 제출 막기
        if (selectedItems.length === 0) {
            alert("주문할 상품을 선택해 주세요.");
            return;  // 함수 종료, 폼 제출을 막음
        }

        console.log(selectedItems);

        // 주문 폼에 데이터를 추가
        let toOrder = document.getElementById('toOrder');
        selectedItems.forEach(function(item, index) {
            let bookIdInput = document.createElement('input');
            bookIdInput.type = 'hidden';
            bookIdInput.name = `bookOrderRequests[${index}].bookId`;  // 서버에서 받을 때 배열처럼 받기 위해
            bookIdInput.value = item.bookId;

            let quantityInput = document.createElement('input');
            quantityInput.type = 'hidden';
            quantityInput.name = `bookOrderRequests[${index}].quantity`;
            quantityInput.value = item.quantity;

            console.log(`추가된 bookId: ${item.bookId}, quantity: ${item.quantity}`);

            toOrder.appendChild(bookIdInput);
            toOrder.appendChild(quantityInput);
        });

        // 폼 제출
        toOrder.submit();
    });
});
