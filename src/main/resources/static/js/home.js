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

// cpu, ram 기능
// 시스템 상태를 가져와서 화면에 표시하는 함수
async function fetchSystemStatus() {
  // 로딩 상태 표시
  setLoadingState();

  try {
    const response = await fetch('/api/status');
    if (!response.ok) throw new Error('API 호출 실패');

    const data = await response.json();

    // 정상 응답일 경우 데이터 바인딩
    document.getElementById('osName').textContent = data.os || '알 수 없음';
    document.getElementById('cpuUsage').textContent = data.cpu || '--%';
    document.getElementById('ramUsage').textContent = data.ram || '--GB';
    document.getElementById('diskStatus').textContent = data.disk || '--';
  } catch (error) {
    console.error('시스템 상태 가져오기 오류:', error);
    setErrorState();
  }
}

// 로딩 중일 때 표시
function setLoadingState() {
  document.getElementById('osName').textContent = '로딩 중...';
  document.getElementById('cpuUsage').textContent = '로딩 중...';
  document.getElementById('ramUsage').textContent = '로딩 중...';
  document.getElementById('diskStatus').textContent = '로딩 중...';
}

// 오류 발생 시 표시
function setErrorState() {
  document.getElementById('osName').textContent = '오류';
  document.getElementById('cpuUsage').textContent = '오류';
  document.getElementById('ramUsage').textContent = '오류';
  document.getElementById('diskStatus').textContent = '오류';
}

// 최초 실행 + 주기적 갱신
fetchSystemStatus();
setInterval(fetchSystemStatus, 3000); // 3초마다 갱신