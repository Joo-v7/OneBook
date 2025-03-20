document.addEventListener('DOMContentLoaded', function() {

    // 페이지 로딩 시 초기 계산
    setTotalInfo();

    // 장바구니 상품 금액 계산해주는 함수.
    function setTotalInfo() {
        let totalPrice = 0;            // 총 상품 금액
        let totalSalePrice = 0;        // 총 판매 금액
        let totalDiscount = 0;         // 총 할인 금액
        let deliveryPrice = 0;         // 배송비
        let finalTotalPrice = 0;       // 최종 결제 금액 (판매 금액 + 배송비)

        $(".cart_info_td").each(function(index, element) {
            const isChecked = $(element).find(".cart-item-checkbox").prop("checked");

            if (isChecked) {
                const price = parseInt($(element).find(".individual-price").val());
                const salePrice = parseInt($(element).find(".individual-salePrice").val());
                const quantity = parseInt($(element).find(".individual-quantity").val());

                // 총 상품 금액
                totalPrice += price * quantity;

                // 총 판매 금액
                totalSalePrice += salePrice * quantity;

                // 총 할인 금액 (상품 금액 - 판매 금액)
                totalDiscount += (price - salePrice) * quantity;

            }
        });

        // 배송비 계산 (3만원 이상이면 무료, 미만이면 3000원)
        deliveryPrice = totalSalePrice === 0 ? 0 : (totalPrice) >= 30000 ? 0 : 3000;

        // 최종 결제 금액 (판매 금액 + 배송비)
        finalTotalPrice = totalSalePrice + deliveryPrice;

        // 계산된 값들을 화면에 표시
        $("#total-price").text(totalPrice + "원");                    // 총 상품 금액
        $("#total-sale-price").text(totalSalePrice + "원");          // 총 판매 금액
        $("#total-discount").text(totalDiscount + "원");             // 총 할인 금액
        $("#total-amount").text(finalTotalPrice + "원");              // 결제 예상 금액
        $("#shipping-fee").text(deliveryPrice + "원");                // 배송비
    }

    // 체크박스를 클릭할 때마다 setTotalInfo 함수 실행
    $(".cart-item-checkbox").on("change", function() {
        setTotalInfo();
    });

    // 전체 선택 체크박스
    const selectAllCheckbox = document.getElementById("selectAll");
// 개별 체크박스들
    const individualCheckboxes = document.querySelectorAll(".cart-item-checkbox");

// 전체 선택 체크박스의 상태 변경 시 실행
    selectAllCheckbox.addEventListener("change", function () {
        const isChecked = this.checked;
        individualCheckboxes.forEach(checkbox => {
            checkbox.checked = isChecked; // 전체 선택 체크박스의 상태를 개별 체크박스에 반영
        });
        setTotalInfo(); // 전체 선택 후 금액 계산
    });

// 개별 체크박스 상태 변경 시 전체 선택 체크박스 업데이트
    individualCheckboxes.forEach(checkbox => {
        checkbox.addEventListener("change", function () {
            // 전체 체크박스가 모두 선택되었는지 확인
            const allChecked = Array.from(individualCheckboxes).every(cb => cb.checked);
            // 하나라도 체크가 해제되면 전체 선택 체크박스도 해제
            selectAllCheckbox.checked = allChecked;
        });
    });


});
