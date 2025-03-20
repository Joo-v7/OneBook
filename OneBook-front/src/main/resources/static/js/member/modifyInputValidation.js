document.addEventListener("DOMContentLoaded", () => {
    const nameInput = document.querySelector("input[name='name']");
    const passwordInput = document.querySelector("input[name='password']");
    const confirmPasswordInput = document.querySelector("#confirmPassword");
    const emailInput = document.querySelector("input[name='email']");
    const phoneInput = document.querySelector("input[name='phoneNumber']");
    const submitButton = document.querySelector("button[name='submitButton']");
    const allInputs = [nameInput, passwordInput, confirmPasswordInput, emailInput, phoneInput];

    // 초기값 검사 실행
    validateName();
    validatePassword();
    validateConfirmPassword()
    validateEmail();
    validatePhone();

    // 유효성 검사 함수들
    function validateName() {
        const value = nameInput.value;
        if (!/^[가-힣]{1,10}$/.test(value)) {
            setInvalid(nameInput, "한글만 입력 가능하고, 최대 10자까지 작성 가능합니다.");
        } else {
            setValid(nameInput);
        }
    }

    function validatePassword() {
        const value = passwordInput.value;
        if (!/^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{10,20}$/.test(value)) {
            setInvalid(passwordInput, "영문, 숫자, 특수 문자를 모두 하나씩 포함한 10~20자를 입력하세요.");
        } else {
            setValid(passwordInput);
        }
    }

    function validateConfirmPassword() {
        if (confirmPasswordInput.value !== passwordInput.value) {
            setInvalid(confirmPasswordInput, "비밀번호가 일치하지 않습니다.");
        } else {
            setValid(confirmPasswordInput);
        }
    }

    function validateEmail() {
        const value = emailInput.value;
        if (/[*!#$^&]/.test(value)) {
            setInvalid(emailInput, "특수 문자를 사용할 수 없습니다.");
        }
        else if (!/^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/.test(value)) {
            setInvalid(emailInput, "올바른 이메일 형식을 입력하세요.");
        } else {
            setValid(emailInput);
        }
    }

    function validatePhone() {
        const value = phoneInput.value;
        if (/[*!#$^&]/.test(value)) {
            setInvalid(phoneInput, "특수 문자를 사용할 수 없습니다.");
        } else if (!/^\d{3}-\d{4}-\d{4}$/.test(value)) {
            setInvalid(phoneInput, "전화번호는 010-1234-5678 형식으로 입력하세요.");
        } else {
            setValid(phoneInput);
        }
    }


    // 공통 유효성 검사 설정
    function setInvalid(element, message) {
        element.classList.add("is-invalid");
        element.classList.remove("is-valid");
        const errorElement = element.nextElementSibling || document.createElement("small");
        errorElement.classList.add("text-danger");
        errorElement.innerText = message;
        element.parentNode.appendChild(errorElement);
    }

    function setValid(element) {
        element.classList.add("is-valid");
        element.classList.remove("is-invalid");
        const errorElement = element.nextElementSibling;
        if (errorElement) {
            errorElement.innerText = "";
        }
    }

    // 전화번호 자동 포맷팅
    phoneInput.addEventListener("input", () => {
        const value = phoneInput.value.replace(/[^0-9]/g, "");
        if (value.length <= 3) {
            phoneInput.value = value;
        } else if (value.length <= 7) {
            phoneInput.value = `${value.slice(0, 3)}-${value.slice(3)}`;
        } else {
            phoneInput.value = `${value.slice(0, 3)}-${value.slice(3, 7)}-${value.slice(7, 11)}`;
        }
        validatePhone();
    });

    // 제출 시 하이픈 제거
    const form = document.querySelector("#loginForm");
    form.addEventListener("submit", () => {
        phoneInput.value = phoneInput.value.replace(/-/g, ""); // 하이픈 제거
    });

    // 이벤트 리스너 추가
    nameInput.addEventListener("input", validateName);
    passwordInput.addEventListener("input", validatePassword);
    confirmPasswordInput.addEventListener("input", validateConfirmPassword);
    emailInput.addEventListener("input", validateEmail);
    phoneInput.addEventListener("input", validatePhone);

    // 제출 버튼 활성화/비활성화
    allInputs.forEach(input =>
        input.addEventListener("input", () => {
            const allValid = allInputs.every(input => input.classList.contains("is-valid"));
            submitButton.disabled = !allValid;
        })
    );
});
