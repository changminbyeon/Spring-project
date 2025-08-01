const token = localStorage.getItem("accessToken");
if(token){
    alert("이미 로그인된 사용자입니다.");
    window.location.href = "/";
}

document.getElementById("signup-form").addEventListener("submit", async (e) => {
    e.preventDefault();

    const username = document.getElementById("username").value.trim();
    const password = document.getElementById("password").value.trim();
    const errorDiv = document.getElementById("error-message");

    try {
        const response = await fetch("/api/users/signup", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({ username, password})
        });

        if(response.ok){
            alert("회원가입 성공! 로그인 페이지로 이동합니다.");
            window.location.href="/login.html";
        } else{
            const result = await response.text();
            errorDiv.textContent = result;
            errorDiv.classList.remove("d-none");
        }
    } catch(error){
        console.error("회원가입 실패: ", error);
        errorDiv.textContent = "알 수 없는 오류가 발생했습니다.";
        errorDiv.classList.remove("d-none");
    }
});