document.addEventListener("DOMContentLoaded", async function () {
  // 사용자 정보 가져오기
  try {
    const response = await fetch("/auth/me", {
      credentials: "include"
    });

    if (!response.ok) {
      throw new Error("인증 실패");
    }

    const user = await response.json();
    document.getElementById("welcome").textContent = `안녕하세요, ${user.username}님 👋`;
    //document.getElementById("roleInfo").textContent = `권한: ${user.role}`;
  } catch (err) {
    console.error("사용자 정보 요청 실패:", err);
    alert("로그인이 필요합니다.");
    window.location.href = "/login";
  }

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

  // 시스템 상태 초기 로딩
  fetchSystemStatus();

  // 새로고침 버튼 이벤트
  const refreshBtn = document.getElementById("refreshBtn");
  if (refreshBtn) {
    refreshBtn.addEventListener("click", fetchSystemStatus);
  }

  // 자동 갱신 토글 이벤트
  const autoRefreshToggle = document.getElementById("autoRefreshToggle");
  let autoRefreshInterval = null;

  if (autoRefreshToggle) {
    autoRefreshToggle.addEventListener("change", function () {
      if (this.checked) {
        autoRefreshInterval = setInterval(fetchSystemStatus, 5000); // 5초마다 갱신
      } else {
        clearInterval(autoRefreshInterval);
      }
    });
  }
});

// 시스템 상태 가져오기
async function fetchSystemStatus() {
  setLoadingState();

  try {
    const response = await fetch('/api/status');
    if (!response.ok) throw new Error('API 호출 실패');

    const data = await response.json();

    document.getElementById('osName').textContent = data.os || '알 수 없음';
    document.getElementById('cpuUsage').textContent = data.cpu || '--%';
    document.getElementById('ramUsage').textContent = data.ram || '--GB';

    renderDiskCharts(data.disk);
  } catch (error) {
    console.error('시스템 상태 가져오기 오류:', error);
    setErrorState();
  }
}

// 로딩 상태 표시
function setLoadingState() {
  document.getElementById('osName').textContent = '로딩 중...';
  document.getElementById('cpuUsage').textContent = '로딩 중...';
  document.getElementById('ramUsage').textContent = '로딩 중...';

  const diskContainer = document.getElementById("diskCharts");
  if (diskContainer) diskContainer.innerHTML = '<p>디스크 정보 로딩 중...</p>';
}

// 오류 상태 표시
function setErrorState() {
  document.getElementById('osName').textContent = '오류';
  document.getElementById('cpuUsage').textContent = '오류';
  document.getElementById('ramUsage').textContent = '오류';

  const diskContainer = document.getElementById("diskCharts");
  if (diskContainer) diskContainer.innerHTML = '<p>디스크 정보 오류</p>';
}

// 디스크 차트 렌더링
function renderDiskCharts(diskArray) {
  const container = document.getElementById("diskCharts");
  container.innerHTML = "";

  diskArray.forEach((disk) => {
    const { name, usedGB, totalGB } = disk;
    const percent = usedGB / totalGB;
    const percentText = (percent * 100).toFixed(1) + "%";

    const wrapper = document.createElement("div");
    wrapper.className = "disk-chart";

    if (percent > 0.9) {
      wrapper.classList.add("danger");
    } else if (percent > 0.7) {
      wrapper.classList.add("warning");
    }

    const canvas = document.createElement("canvas");
    canvas.width = 120;
    canvas.height = 120;
    const ctx = canvas.getContext("2d");

    // 배경 원
    ctx.beginPath();
    ctx.arc(60, 60, 50, 0, 2 * Math.PI);
    ctx.fillStyle = "#eee";
    ctx.fill();

    // 사용량 원
    ctx.beginPath();
    ctx.moveTo(60, 60);
    ctx.arc(60, 60, 50, -0.5 * Math.PI, -0.5 * Math.PI + percent * 2 * Math.PI);
    ctx.lineTo(60, 60);
    ctx.fillStyle = "#4caf50";
    ctx.fill();

    const label = document.createElement("div");
    label.innerHTML = `
      <strong>${name}</strong><br>
      ${usedGB.toFixed(1)}GB / ${totalGB.toFixed(1)}GB<br>
      사용률: ${percentText}
    `;

    wrapper.appendChild(canvas);
    wrapper.appendChild(label);
    container.appendChild(wrapper);
  });
}