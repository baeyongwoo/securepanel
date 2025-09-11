document.addEventListener("DOMContentLoaded", async function () {
    try {
        const response = await fetch("/auth/me", {
            credentials: "include" // 세션 쿠키 명시적으로 포함
        });

        if (!response.ok) {
            throw new Error("인증 실패");
        }

        const user = await response.json();

        document.getElementById("welcome").textContent = `안녕하세요, ${user.username}님 👋`;
        document.getElementById("roleInfo").textContent = `권한: ${user.role}`;
    } catch (err) {
        console.error("사용자 정보 요청 실패:", err);
        alert("로그인이 필요합니다.");
        window.location.href = "/login";
    }
});

const logoutBtn = document.getElementById("logoutBtn");
if (logoutBtn) {
    logoutBtn.addEventListener("click", async function () {
        try {
            await fetch("/logout", {
                method: "POST",
                credentials: "include"
            });
            alert("로그아웃 되었습니다.");
            window.location.href = "/login";
        } catch (err) {
            console.error("로그아웃 실패:", err);
            alert("로그아웃 중 오류가 발생했습니다.");
        }
    });
}