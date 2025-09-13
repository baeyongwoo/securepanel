document.addEventListener("DOMContentLoaded", () => {
    const container = document.getElementById("serviceStatusContainer");

    // 서비스 상태 불러오기
    fetch("/api/service-status")
        .then(res => res.json())
        .then(data => {
            container.innerHTML = "";

            data.forEach(service => {
                const card = document.createElement("div");
                card.className = "service-card";

                const statusClass = service.running ? "green" : "red";
                const statusText = service.running ? "실행 중" : "중지됨";

                card.innerHTML = `
                    <span class="service-name">${service.name}</span>
                    <span class="status-indicator ${statusClass}">${statusText}</span>
                    <button class="control-btn" data-action="restart" data-target="${service.name.toLowerCase()}">재시작</button>
                `;
                container.appendChild(card);
            });
        });

    // 버튼 클릭 이벤트 처리
    container.addEventListener("click", (e) => {
        if (e.target.classList.contains("control-btn")) {
            const target = e.target.dataset.target;
            const action = e.target.dataset.action;

            fetch(`/api/service-control?target=${target}&action=${action}`, {
                method: "POST"
            })
            .then(res => res.json())
            .then(result => {
                if (result.success) {
                    alert(`${target} 서비스가 재시작되었습니다.`);
                } else {
                    alert(`재시작 실패: ${result.message || "알 수 없는 오류"}`);
                }
                location.reload(); // 또는 상태만 다시 불러오는 함수로 대체
            })
            .catch(err => {
                alert("요청 실패: " + err.message);
            });
        }
    });
});
// 로그아웃 버튼 이벤트
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