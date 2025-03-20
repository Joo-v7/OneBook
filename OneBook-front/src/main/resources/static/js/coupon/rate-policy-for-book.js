
const rangeInput = document.getElementById('discountRange');
const rangeValue = document.getElementById('rangeValue');

// 초기값 설정
rangeValue.textContent = rangeInput.value + '%';

// range slider 값이 변경될 때마다 실행되는 이벤트 리스너
rangeInput.addEventListener('input', function () {
    rangeValue.textContent = this.value + '%';
});
