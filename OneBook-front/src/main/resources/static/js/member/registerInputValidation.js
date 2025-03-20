document.addEventListener("DOMContentLoaded", () => {

    const nameInput = document.querySelector("input[name='name']");
    const loginIdInput = document.querySelector("input[name='loginId']");
    const passwordInput = document.querySelector("input[name='password']");
    const confirmPasswordInput = document.querySelector("#confirmPassword");
    const dobInput = document.querySelector("input[name='dateOfBirth']");
    const emailInput = document.querySelector("input[name='email']");
    const phoneInput = document.querySelector("input[name='phoneNumber']");
    const submitButton = document.querySelector("button[name='submitButton']");
    const allInputs = [nameInput, loginIdInput, passwordInput, confirmPasswordInput, dobInput, emailInput, phoneInput];

    // 초기값 검사 실행
    validateName();
    validateLoginId();
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

    function validateLoginId() {
        const value = loginIdInput.value;
        if (!/^[a-zA-Z0-9]{1,20}$/.test(value)) {
            setInvalid(loginIdInput, "영문자와 숫자만 입력 가능하고, 최대 20자까지 작성 가능합니다.");
        } else {
            setValid(loginIdInput);
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

    function validateDob() {
        const value = dobInput.value;
        if (!/^\d{4}-\d{2}-\d{2}$/.test(value)) {
            setInvalid(dobInput, "생년월일을 달력에서 선택해주세요.");
        } else {
            setValid(dobInput);
        }
    }

    function validateEmail() {
        const value = emailInput.value;
        if (!/^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/.test(value)) {
            setInvalid(emailInput, "올바른 이메일 형식을 입력하세요.");
        } else {
            setValid(emailInput);
        }
    }

    function validatePhone() {
        const value = phoneInput.value;
        if (!/^\d{3}-\d{4}-\d{4}$/.test(value)) {
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
    form.addEventListener("submit", (event) => {
        phoneInput.value = phoneInput.value.replace(/-/g, ""); // 하이픈 제거
    });

    // 생년월일 달력 추가
    dobInput.type = "date";

    // 이벤트 리스너 추가
    nameInput.addEventListener("input", validateName);
    loginIdInput.addEventListener("input", validateLoginId);
    passwordInput.addEventListener("input", validatePassword);
    confirmPasswordInput.addEventListener("input", validateConfirmPassword);
    dobInput.addEventListener("input", validateDob);
    emailInput.addEventListener("input", validateEmail);
    phoneInput.addEventListener("input", validatePhone);

    // 제출 버튼 활성화/비활성화
    allInputs.forEach(input => input.addEventListener("input", () => {
        const allValid = allInputs.every(input => input.classList.contains("is-valid"));
        submitButton.disabled = !(allValid);
    }));

    // 본인인증 버튼이 전화번호 입력을 올바르게 했을 때만 활성화되도록 하기
    // phoneInput.addEventListener("input", () => {
    //     const phoneValid = /^\d{3}-\d{4}-\d{4}$/.test(phoneInput.value);
    //     const sendAuthCodeButton = document.getElementById("sendAuthCode");
    //     sendAuthCodeButton.disabled = !phoneValid;
    // });


});
