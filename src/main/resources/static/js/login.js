document.addEventListener("DOMContentLoaded", function () {
    const form = document.getElementById("loginForm");

    form.addEventListener("submit", async function (event) {
        event.preventDefault();

        const username = document.getElementById("username").value;
        const password = document.getElementById("password").value;

        try {
            const response = await fetch("/auth/login", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ username, password })
            });

            if (response.ok) {
                const data = await response.json();
                localStorage.setItem("token", data.token);
                alert("로그인 성공!");
                window.location.href = "/";
            } else {
                alert("로그인 실패: 아이디 또는 비밀번호를 확인하세요.");
            }
        } catch (error) {
            console.error("로그인 오류:", error);
            alert("서버 오류가 발생했습니다.");
        }
    });
});