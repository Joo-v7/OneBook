// 본인인증 버튼 클릭 시 동작
document.addEventListener("DOMContentLoaded", () => {

    document.getElementById("sendAuthCode").addEventListener("click", function (event) {
        event.preventDefault();

        // 인증번호를 요청하는 GET 요청
        fetch("/dooray-message-authentication", {
            method: "GET",
        })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    document.getElementById("resultMessage").textContent = data.message;
                    document.getElementById("sendAuthCode").disabled = true; // 본인 인증 버튼 비활성화
                    document.getElementById("sendAuthCode").style.display = "none"; // 본인 인증 버튼 숨기기

                    // 인증번호 전송이 성공한 후에 타이머 시작
                    document.getElementById("authSection").style.display = "block"; // 인증번호 입력란 표시
                    startTimer(); // 타이머 시작
                } else {
                    document.getElementById("resultMessage").textContent = data.message;
                }
            })
            .catch(error => {
                console.error("Error:", error);
                document.getElementById("resultMessage").textContent = "인증번호 전송 실패. 서버와 연결을 확인해주세요.";
            });
    });

// 인증번호 확인 버튼 클릭 시 동작
    document.getElementById("verifyAuthCode").addEventListener("click", function () {
        const authCode = document.getElementById("code").value;

        // 인증번호를 확인하기 위한 POST 요청
        fetch("/dooray-message-authentication", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({authCode: authCode}),
        })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    document.getElementById("resultMessage").textContent = "인증 성공!";

                    // // 인증 성공 시 타이머 멈추고 숨기기
                    clearInterval(timerInterval); // 타이머 멈추기
                    document.getElementById("authSection").style.display = "none";
                    // document.getElementById("timer").style.display = "none"; // 타이머 숨기기
                    document.getElementById("verifyAuthCode").disabled = true; // 인증번호 확인 버튼 비활성화

                    document.getElementById("wakeUp").style.display = "block"; // 아이디 입력 폼 표시

                } else {
                    document.getElementById("resultMessage").textContent = "인증 실패. 인증번호를 다시 확인해주세요.";
                }
            })
            .catch(error => {
                console.error("Error:", error);
                document.getElementById("resultMessage").textContent = "인증 실패. 서버와 연결을 확인해주세요.";
            });
    });

// 타이머 시작 함수
    let timerInterval; // 타이머 인터벌을 저장할 변수

    function startTimer() {
        let timer = 180; // 3분 (180초)
        const timerElement = document.getElementById("timer");
        const code = document.getElementById("code");
        const verifyButton = document.getElementById("verifyAuthCode");

        // 타이머 업데이트 함수
        timerInterval = setInterval(function () {
            let minutes = Math.floor(timer / 60);
            let seconds = timer % 60;

            // 타이머 표시
            timerElement.textContent = `남은 시간: ${minutes}분 ${seconds}초`;

            if (timer <= 0) {
                clearInterval(timerInterval);
                timerElement.textContent = "시간 초과";
                code.disabled = true; // 인증번호 입력 필드 비활성화
                verifyButton.disabled = true; // 확인 버튼 비활성화
            }

            timer--;
        }, 1000); // 1초마다 타이머 업데이트
    }
});
